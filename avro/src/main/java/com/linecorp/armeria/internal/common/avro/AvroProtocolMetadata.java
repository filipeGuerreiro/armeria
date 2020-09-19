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

package com.linecorp.armeria.internal.common.avro;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.linecorp.armeria.internal.common.util.Types;
import org.apache.thrift.AsyncProcessFunction;
import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBaseAsyncProcessor;
import org.apache.thrift.TBaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Provides the metadata of a Thrift service interface or implementation.
 */
public final class AvroProtocolMetadata {

    private static final Logger logger = LoggerFactory.getLogger(AvroProtocolMetadata.class);

    private final Set<Class<?>> interfaces;

    /**
     * A map whose key is a method name and whose value is {@link AsyncProcessFunction} or
     * {@link ProcessFunction}.
     */
    private final Map<String, AvroMessage> messages = new HashMap<>();

    /**
     * Creates a new instance from a Thrift service implementation that implements one or more Thrift service
     * interfaces.
     */
    public AvroProtocolMetadata(Object implementation) {
        this(ImmutableList.of(requireNonNull(implementation, "implementation")));
    }

    /**
     * Creates a new instance from a list of Thrift service implementations, while each service can implement
     * one or more Thrift service interfaces.
     */
    public AvroProtocolMetadata(Iterable<?> implementations) {
        requireNonNull(implementations, "implementations");

        final ImmutableSet.Builder<Class<?>> interfaceBuilder = ImmutableSet.builder();
        implementations.forEach(implementation -> {
            requireNonNull(implementation, "implementations contains null.");
            interfaceBuilder.addAll(init(implementation));
        });
        interfaces = interfaceBuilder.build();
    }

    /**
     * Creates a new instance from a single Thrift service interface.
     */
    public AvroProtocolMetadata(Class<?> serviceType) {
        requireNonNull(serviceType, "serviceType");
        interfaces = init(null, Collections.singleton(serviceType));
    }

    private Set<Class<?>> init(Object implementation) {
        return init(implementation, Types.getAllInterfaces(implementation.getClass()));
    }

    private Set<Class<?>> init(@Nullable Object implementation, Iterable<Class<?>> candidateInterfaces) {

        // Build the map of method names and their corresponding process functions.
        // If a method is defined multiple times, we take the first definition
        final Set<Class<?>> interfaces = new HashSet<>();

        for (Class<?> iface : candidateInterfaces) {
            final Map<String, AsyncProcessFunction<?, ?, ?>> asyncProcessMap;
            asyncProcessMap = getThriftAsyncProcessMap(implementation, iface);
            if (asyncProcessMap != null) {
                asyncProcessMap.forEach(
                        (name, func) -> registerFunction(iface, name, func, implementation));
                interfaces.add(iface);
            }

            final Map<String, ProcessFunction<?, ?>> processMap;
            processMap = getThriftProcessMap(implementation, iface);
            if (processMap != null) {
                processMap.forEach(
                        (name, func) -> registerFunction(iface, name, func, implementation));
                interfaces.add(iface);
            }
        }

        if (messages.isEmpty()) {
            if (implementation != null) {
                throw new IllegalArgumentException('\'' + implementation.getClass().getName() +
                                                   "' is not a Thrift service implementation.");
            } else {
                throw new IllegalArgumentException("not a Thrift service interface: " + candidateInterfaces);
            }
        }

        return Collections.unmodifiableSet(interfaces);
    }

    @Nullable
    private static Map<String, ProcessFunction<?, ?>> getThriftProcessMap(@Nullable Object service,
                                                                          Class<?> iface) {
        final String name = iface.getName();
        if (!name.endsWith("$Iface")) {
            return null;
        }

        final String processorName = name.substring(0, name.length() - 5) + "Processor";
        try {
            final Class<?> processorClass = Class.forName(processorName, false, iface.getClassLoader());
            if (!TBaseProcessor.class.isAssignableFrom(processorClass)) {
                return null;
            }

            final Constructor<?> processorConstructor = processorClass.getConstructor(iface);

            @SuppressWarnings("rawtypes")
            final TBaseProcessor processor = (TBaseProcessor) processorConstructor.newInstance(service);

            @SuppressWarnings("unchecked")
            final Map<String, ProcessFunction<?, ?>> processMap =
                    (Map<String, ProcessFunction<?, ?>>) processor.getProcessMapView();

            return processMap;
        } catch (Exception e) {
            logger.debug("Failed to retrieve the process map from: {}", iface, e);
            return null;
        }
    }

    @Nullable
    private static Map<String, AsyncProcessFunction<?, ?, ?>> getThriftAsyncProcessMap(
            @Nullable Object service, Class<?> iface) {

        final String name = iface.getName();
        if (!name.endsWith("$AsyncIface")) {
            return null;
        }

        final String processorName = name.substring(0, name.length() - 10) + "AsyncProcessor";
        try {
            final Class<?> processorClass = Class.forName(processorName, false, iface.getClassLoader());
            if (!TBaseAsyncProcessor.class.isAssignableFrom(processorClass)) {
                return null;
            }

            final Constructor<?> processorConstructor = processorClass.getConstructor(iface);

            @SuppressWarnings("rawtypes")
            final TBaseAsyncProcessor processor =
                    (TBaseAsyncProcessor) processorConstructor.newInstance(service);

            @SuppressWarnings("unchecked")
            final Map<String, AsyncProcessFunction<?, ?, ?>> processMap =
                    (Map<String, AsyncProcessFunction<?, ?, ?>>) processor.getProcessMapView();

            return processMap;
        } catch (Exception e) {
            logger.debug("Failed to retrieve the asynchronous process map from:: {}", iface, e);
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    private void registerFunction(Class<?> iface, String name,
                                  Object func, @Nullable Object implementation) {
        if (messages.containsKey(name)) {
            logger.warn("duplicate Thrift method name: " + name);
            return;
        }

        try {
            final AvroMessage f;
            if (func instanceof ProcessFunction) {
                f = new AvroMessage(iface, (ProcessFunction) func, implementation);
            } else {
                f = new AvroMessage(iface, (AsyncProcessFunction) func, implementation);
            }
            messages.put(name, f);
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to retrieve function metadata: " +
                                               iface.getName() + '.' + name + "()", e);
        }
    }

    /**
     * Returns the Thrift service interfaces implemented.
     */
    public Set<Class<?>> interfaces() {
        return interfaces;
    }

    /**
     * Returns the {@link AvroMessage} that provides the metadata of the specified Thrift function.
     *
     * @return the {@link AvroMessage}, or {@code null} if there's no such function.
     */
    @Nullable
    public AvroMessage message(String method) {
        return messages.get(method);
    }
}
