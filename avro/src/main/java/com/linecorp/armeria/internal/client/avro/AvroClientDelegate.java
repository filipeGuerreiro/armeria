/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.internal.client.avro;

import com.linecorp.armeria.common.avro.AvroSerializationFormatFactories;
import com.linecorp.armeria.internal.common.avro.AvroMessage;
import com.linecorp.armeria.internal.common.avro.AvroProtocolMetadata;
import com.google.common.base.Strings;
import com.linecorp.armeria.client.*;
import com.linecorp.armeria.common.*;
import com.linecorp.armeria.common.logging.RequestLogProperty;
import com.linecorp.armeria.common.thrift.ThriftCall;
import com.linecorp.armeria.common.thrift.ThriftProtocolFactories;
import com.linecorp.armeria.common.thrift.ThriftReply;
import com.linecorp.armeria.common.util.CompletionActions;
import com.linecorp.armeria.common.util.Exceptions;
import com.linecorp.armeria.internal.common.thrift.*;
import io.netty.buffer.ByteBuf;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.Decoder;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

final class AvroClientDelegate extends DecoratingClient<HttpRequest, HttpResponse, RpcRequest, RpcResponse>
        implements RpcClient {

    private final AtomicInteger nextSeqId = new AtomicInteger();

    private final SerializationFormat serializationFormat;
    private final AvroSerializationFormat avroSerializationFormat;
    private final MediaType mediaType;
    private final Map<Class<?>, AvroProtocolMetadata> metadataMap = new ConcurrentHashMap<>();

    AvroClientDelegate(HttpClient httpClient, SerializationFormat serializationFormat) {
        super(httpClient);
        this.serializationFormat = serializationFormat;
        avroSerializationFormat = AvroSerializationFormatFactories.get(serializationFormat);
        mediaType = serializationFormat.mediaType();
    }

    @Override
    public RpcResponse execute(ClientRequestContext ctx, RpcRequest call) {
        final int seqId = nextSeqId.incrementAndGet();
        final String method = call.method();
        final List<Object> args = call.params();
        final CompletableRpcResponse reply = new CompletableRpcResponse();

        ctx.logBuilder().serializationFormat(serializationFormat);

        final AvroMessage msg;
        try {
            msg = metadata(call.serviceType()).message(method);
            if (msg == null) {
                throw new IllegalArgumentException("Avro method not found: " + method);
            }
        } catch (Throwable cause) {
            reply.completeExceptionally(cause);
            return reply;
        }

        try {
            final TMessage header = new TMessage(fullMethod(ctx, method), msg.messageType(), seqId);

            final ByteBuf buf = ctx.alloc().buffer(128);

            try {
                final TByteBufTransport outTransport = new TByteBufTransport(buf);
                final TProtocol tProtocol = avroSerializationFormat.getProtocol(outTransport);
                tProtocol.writeMessageBegin(header);
                @SuppressWarnings("rawtypes")
                final TBase tArgs = msg.newArgs(args);
                tArgs.write(tProtocol);
                tProtocol.writeMessageEnd();

                ctx.logBuilder().requestContent(call, new ThriftCall(header, tArgs));
            } catch (Throwable t) {
                buf.release();
                Exceptions.throwUnsafely(t);
            }

            final Endpoint endpoint = ctx.endpoint();
            final HttpRequest httpReq = HttpRequest.of(
                    RequestHeaders.builder(HttpMethod.POST, ctx.path())
                                  .scheme(ctx.sessionProtocol())
                                  .authority(endpoint != null ? endpoint.authority() : "UNKNOWN")
                                  .contentType(mediaType)
                                  .build(),
                    HttpData.wrap(buf).withEndOfStream());

            ctx.updateRequest(httpReq);
            ctx.logBuilder().defer(RequestLogProperty.RESPONSE_CONTENT);

            final HttpResponse httpResponse;
            try {
                httpResponse = unwrap().execute(ctx, httpReq);
            } catch (Throwable t) {
                httpReq.abort();
                throw t;
            }

            httpResponse.aggregateWithPooledObjects(ctx.eventLoop(), ctx.alloc()).handle((res, cause) -> {
                if (cause != null) {
                    handlePreDecodeException(ctx, reply, msg, Exceptions.peel(cause));
                    return null;
                }

                try (HttpData content = res.content()) {
                    final HttpStatus status = res.status();
                    if (status.code() != HttpStatus.OK.code()) {
                        handlePreDecodeException(
                                ctx, reply, msg,
                                new InvalidResponseHeadersException(res.headers()));
                        return null;
                    }

                    try {
                        handle(ctx, seqId, reply, msg, content);
                    } catch (Throwable t) {
                        handlePreDecodeException(ctx, reply, msg, t);
                    }
                }

                return null;
            }).exceptionally(CompletionActions::log);
        } catch (Throwable cause) {
            handlePreDecodeException(ctx, reply, msg, cause);
        }

        return reply;
    }

    private static String fullMethod(ClientRequestContext ctx, String method) {
        final String service = ctx.fragment();
        if (Strings.isNullOrEmpty(service)) {
            return method;
        } else {
            return service + ':' + method;
        }
    }

    private AvroProtocolMetadata metadata(Class<?> serviceType) {
        final AvroProtocolMetadata metadata = metadataMap.get(serviceType);
        if (metadata != null) {
            return metadata;
        }

        return metadataMap.computeIfAbsent(serviceType, AvroProtocolMetadata::new);
    }

    private void handle(ClientRequestContext ctx, int seqId, CompletableRpcResponse reply,
                        AvroMessage msg, HttpData content) throws AvroRuntimeException {

        if (msg.isOneWay()) {
            handleSuccess(ctx, reply, null, null);
            return;
        }

        if (content.isEmpty()) {
            throw new AvroRuntimeException("MISSING_RESULT");
        }

        final TTransport inputTransport = new TByteBufTransport(content.byteBuf());
        final Decoder decoder = avroSerializationFormat.getDecoder();

        //final TMessage header = decoder..readMessageBegin();
//        final TApplicationException appEx = readApplicationException(seqId, msg, decoder, header);
//        if (appEx != null) {
//            handleException(ctx, reply, new ThriftReply(header, appEx), appEx);
//            return;
//        }

        final TBase<?, ?> result = msg.newResult();
        result.read(inputProtocol);
        inputProtocol.readMessageEnd();

        final ThriftReply rawResponseContent = new ThriftReply(header, result);

        for (TFieldIdEnum fieldIdEnum : msg.exceptionFields()) {
            if (ThriftFieldAccess.isSet(result, fieldIdEnum)) {
                final TException cause = (TException) ThriftFieldAccess.get(result, fieldIdEnum);
                handleException(ctx, reply, rawResponseContent, cause);
                return;
            }
        }

        final TFieldIdEnum successField = msg.successField();
        if (successField == null) { // void method
            handleSuccess(ctx, reply, null, rawResponseContent);
            return;
        }

        if (ThriftFieldAccess.isSet(result, successField)) {
            final Object returnValue = ThriftFieldAccess.get(result, successField);
            handleSuccess(ctx, reply, returnValue, rawResponseContent);
            return;
        }

        handleException(
                ctx, reply, rawResponseContent,
                new TApplicationException(TApplicationException.MISSING_RESULT,
                                          result.getClass().getName() + '.' + successField.getFieldName()));
    }

    @Nullable
    private static TApplicationException readApplicationException(int seqId, AvroMessage msg,
                                                                  TProtocol inputProtocol,
                                                                  TMessage msg) throws TException {
        if (msg.seq.seqid != seqId) {
            throw new TApplicationException(TApplicationException.BAD_SEQUENCE_ID);
        }

        if (!msg.name().equals(msg.name)) {
            return new TApplicationException(TApplicationException.WRONG_METHOD_NAME, msg.name);
        }

        if (msg.type == TMessageType.EXCEPTION) {
            final TApplicationException appEx = TApplicationExceptions.read(inputProtocol);
            inputProtocol.readMessageEnd();
            return appEx;
        }

        return null;
    }

    private static void handleSuccess(ClientRequestContext ctx, CompletableRpcResponse reply,
                                      @Nullable Object returnValue, @Nullable ThriftReply rawResponseContent) {
        reply.complete(returnValue);
        ctx.logBuilder().responseContent(reply, rawResponseContent);
    }

    private static void handleException(ClientRequestContext ctx, CompletableRpcResponse reply,
                                        @Nullable ThriftReply rawResponseContent, Exception cause) {
        reply.completeExceptionally(cause);
        ctx.logBuilder().responseContent(reply, rawResponseContent);
    }

    private static void handlePreDecodeException(ClientRequestContext ctx, CompletableRpcResponse reply,
                                                 ThriftFunction thriftMethod, Throwable cause) {
        handleException(ctx, reply, null,
                        decodeException(cause, thriftMethod.declaredExceptions()));
    }

    private static Exception decodeException(Throwable cause,
                                             @Nullable Class<?>[] declaredThrowableExceptions) {
        if (cause instanceof RuntimeException || cause instanceof TException) {
            return (Exception) cause;
        }

        final boolean isDeclaredException;
        if (declaredThrowableExceptions != null) {
            isDeclaredException = Arrays.stream(declaredThrowableExceptions).anyMatch(v -> v.isInstance(cause));
        } else {
            isDeclaredException = false;
        }
        if (isDeclaredException) {
            return (Exception) cause;
        } else if (cause instanceof Error) {
            return new RuntimeException(cause);
        } else {
            return new TTransportException(cause);
        }
    }
}
