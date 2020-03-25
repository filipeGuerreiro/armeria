/*
 * Copyright 2020 LINE Corporation
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

package com.linecorp.armeria.internal.server;

import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.stream.ClosedStreamException;
import com.linecorp.armeria.internal.common.ArmeriaHttpUtil;
import com.linecorp.armeria.internal.common.Http2ObjectEncoder;
import com.linecorp.armeria.internal.common.util.HttpTimestampSupplier;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Headers;

public final class ServerHttp2ObjectEncoder extends Http2ObjectEncoder implements ServerHttpObjectEncoder {
    private final boolean enableServerHeader;
    private final boolean enableDateHeader;

    public ServerHttp2ObjectEncoder(ChannelHandlerContext ctx, Http2ConnectionEncoder encoder,
                                    boolean enableServerHeader, boolean enableDateHeader) {
        super(ctx, encoder);
        this.enableServerHeader = enableServerHeader;
        this.enableDateHeader = enableDateHeader;
    }

    @Override
    public ChannelFuture doWriteHeaders(int id, int streamId, ResponseHeaders headers, boolean endStream,
                                        boolean isTrailersEmpty) {
        if (!isStreamPresentAndWritable(streamId)) {
            // One of the following cases:
            // - Stream has been closed already.
            // - (bug) Server tried to send a response HEADERS frame before receiving a request HEADERS frame.
            return newFailedFuture(ClosedStreamException.get());
        }

        final Http2Headers converted = convertHeaders(headers, isTrailersEmpty);
        return encoder().writeHeaders(ctx(), streamId, converted, 0, endStream, ctx().newPromise());
    }

    private Http2Headers convertHeaders(HttpHeaders inputHeaders, boolean isTrailersEmpty) {
        final Http2Headers outHeaders = ArmeriaHttpUtil.toNettyHttp2ServerHeaders(inputHeaders);
        if (!isTrailersEmpty && outHeaders.contains(HttpHeaderNames.CONTENT_LENGTH)) {
            // We don't apply chunked encoding when the content-length header is set, which would
            // prevent the trailers from being sent so we go ahead and remove content-length to force
            // chunked encoding.
            outHeaders.remove(HttpHeaderNames.CONTENT_LENGTH);
        }

        if (enableServerHeader && !outHeaders.contains(HttpHeaderNames.SERVER)) {
            outHeaders.add(HttpHeaderNames.SERVER, ArmeriaHttpUtil.SERVER_HEADER);
        }

        if (enableDateHeader && !outHeaders.contains(HttpHeaderNames.DATE)) {
            outHeaders.add(HttpHeaderNames.DATE, HttpTimestampSupplier.currentTime());
        }
        return outHeaders;
    }

    @Override
    public ChannelFuture doWriteTrailers(int id, int streamId, HttpHeaders headers) {
        if (!isStreamPresentAndWritable(streamId)) {
            // One of the following cases:
            // - Stream has been closed already.
            // - (bug) Server tried to send a response HEADERS frame before receiving a request HEADERS frame.
            return newFailedFuture(ClosedStreamException.get());
        }

        final Http2Headers converted = ArmeriaHttpUtil.toNettyHttp2ServerTrailer(headers);
        return encoder().writeHeaders(ctx(), streamId, converted, 0, true, ctx().newPromise());
    }
}
