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

package com.linecorp.armeria.common.avro;

import com.linecorp.armeria.internal.client.avro.AvroSerializationFormat;
import com.linecorp.armeria.common.SerializationFormat;
import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;

import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

/**
 * Provides a set of the known {@link AvroSerializationFormat} instances.
 */
public final class AvroSerializationFormatFactories {

    // TODO: cannot be null
    private static OutputStream out = null;
    private static InputStream in = null;

    /**
     * {@link AvroSerializationFormat} for Avro Binary protocol.
     */
    // TODO by default is buffered, but there is blocking and direct too
    public static final AvroSerializationFormat BINARY = new AvroSerializationFormat(
            EncoderFactory.get().binaryEncoder(out, null),
            DecoderFactory.get().binaryDecoder(in, null));

//    public static final TProtocolFactory COMPACT = new TCompactProtocol.Factory() {
//        private static final long serialVersionUID = 1629726795326210377L;
//
//        @Override
//        public String toString() {
//            return "TProtocolFactory(compact)";
//        }
//    };

    // TODO: cannot be null
    private static Schema schema = null;

    /**
     * {@link AvroSerializationFormat} for the Avro JSON protocol.
     */
    public static final AvroSerializationFormat JSON = new AvroSerializationFormat(
            EncoderFactory.get().jsonEncoder(schema, out),
            DecoderFactory.get().jsonDecoder(schema, in));

    /**
     * Returns the {@link AvroSerializationFormat} for the specified {@link SerializationFormat}.
     *
     * @throws IllegalArgumentException if the specified {@link SerializationFormat} is not for Avro
     */
    public static AvroSerializationFormat get(SerializationFormat serializationFormat) {
        requireNonNull(serializationFormat, "serializationFormat");

        if (serializationFormat == AvroSerializationFormats.BINARY) {
            return BINARY;
        }

        if (serializationFormat == AvroSerializationFormats.JSON) {
            return JSON;
        }

        throw new IllegalArgumentException("non-Avro serializationFormat: " + serializationFormat);
    }

    /**
     * Returns the {@link SerializationFormat} for the specified {@link AvroSerializationFormat}.
     *
     * @throws IllegalArgumentException if the specified {@link AvroSerializationFormat} is not known by this class
     */
//    public static SerializationFormat toSerializationFormat(AvroSerializationFormat format) {
//        requireNonNull(format, "avroSerializationFormat");
//
//        if (format instanceof TBinaryProtocol.Factory) {
//            return AvroSerializationFormats.BINARY;
//        } else if (format instanceof TJSONProtocol.Factory) {
//            return AvroSerializationFormats.JSON;
//        } else if (format instanceof TTextProtocolFactory) {
//            final TTextProtocolFactory factory = (TTextProtocolFactory) format;
//            return factory.usesNamedEnums() ? AvroSerializationFormats.TEXT_NAMED_ENUM
//                                            : AvroSerializationFormats.TEXT;
//        } else {
//            throw new IllegalArgumentException(
//                    "unsupported TProtocolFactory: " + format.getClass().getName());
//        }
//    }

    private AvroSerializationFormatFactories() {}
}
