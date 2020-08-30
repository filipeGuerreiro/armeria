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

package armeria.common.avro;

import org.apache.avro.Schema;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A container of an Avro message produced by Apache Avro.
 */
public abstract class AvroMessage {

    private final Schema schema;

    AvroMessage(Schema schema) {
        this.schema = requireNonNull(schema, "schema");
    }

    /**
     * Returns the schema part of the message.
     */
    public final Schema schema() {
        return schema;
    }

    @Override
    public int hashCode() {
        return schema.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvroMessage)) {
            return false;
        }
        return schema.equals(((AvroMessage) o).schema);
    }

    final String typeStr() {
        return typeStr(schema.getType());
    }

    static String typeStr(byte type) {
        switch (type) {
            case TMessageType.CALL:
                return "CALL";
            case TMessageType.ONEWAY:
                return "ONEWAY";
            case TMessageType.REPLY:
                return "REPLY";
            case TMessageType.EXCEPTION:
                return "EXCEPTION";
            default:
                return "UNKNOWN(" + (type & 0xFF) + ')';
        }
    }
}
