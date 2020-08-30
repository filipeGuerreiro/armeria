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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.protocol.TMessage;

/**
 * Jackson {@link Module} for Thrift types. You can serialize {@link AvroCall}, {@link AvroReply},
 * {@link TMessage}, {@link TBase} and {@link TApplicationException} by registering this module:
 * <pre>{@code
 * ObjectMapper objectMapper = new ObjectMapper();
 * objectMapper.registerModule(new ThriftJacksonModule());
 * }</pre>
 *
 * @see ObjectMapper#registerModule(Module)
 */
public final class AvroJacksonModule extends Module {

    private static final Version VERSION =
            new Version(0, 0, 0, null, "com.linecorp.armeria", "armeria-thrift");

    private final boolean useNamedEnums;

    /**
     * Creates a new instance with the default settings.
     */
    public AvroJacksonModule() {
        this(false);
    }

    /**
     * Creates a new instance.
     *
     * @param useNamedEnums whether to serialize an enum as a string or an integer
     */
    public AvroJacksonModule(boolean useNamedEnums) {
        this.useNamedEnums = useNamedEnums;
    }

    @Override
    public String getModuleName() {
        return "ThriftModule";
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(new AvroJacksonSerializers(useNamedEnums));
    }
}
