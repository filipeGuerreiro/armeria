/*
 *  Copyright 2017 LINE Corporation
 *
 *  LINE Corporation licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */
package com.linecorp.armeria.common.avro;

import com.google.common.collect.ImmutableSet;
import com.linecorp.armeria.common.SerializationFormat;

import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Avro-related {@link SerializationFormat} instances.
 */
public final class AvroSerializationFormats {

    /**
     * Avro Binary serialization format.
     */
    public static final SerializationFormat BINARY = SerializationFormat.of("avro/binary");

    /**
     * Avro JSON serialization format.
     */
    public static final SerializationFormat JSON = SerializationFormat.of("avro/json");


    private static final Set<SerializationFormat> AVRO_FORMATS = ImmutableSet.of(BINARY, JSON);

    /**
     * Returns the set of all known Avro serialization formats.
     */
    public static Set<SerializationFormat> values() {
        return AVRO_FORMATS;
    }

    /**
     * Returns whether the specified {@link SerializationFormat} is Avro.
     */
    public static boolean isAvro(SerializationFormat format) {
        return values().contains(requireNonNull(format, "format"));
    }

    private AvroSerializationFormats() {}
}
