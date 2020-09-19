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

import com.linecorp.armeria.common.util.EventLoopCheckingFuture;
import org.apache.avro.ipc.Callback;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.Objects.requireNonNull;

/**
 * A {@link CompletableFuture} that can be passed in as a {@link Callback}
 * when making an asynchronous client-side Avro RPC.
 */
public final class AvroFuture<T> extends EventLoopCheckingFuture<T> implements Callback<T> {

    /**
     * Returns a new {@link AvroFuture} instance that has its value set immediately.
     */
    public static <T> AvroFuture<T> completedFuture(@Nullable T value) {
        final AvroFuture<T> future = new AvroFuture<>();
        future.handleResult(value);
        return future;
    }

    /**
     * Returns a new {@link AvroFuture} instance that has an exception set immediately.
     */
    public static <T> AvroFuture<T> exceptionallyCompletedFuture(Throwable cause) {
        requireNonNull(cause, "cause");
        final AvroFuture<T> future = new AvroFuture<>();
        future.handleError(cause instanceof Exception ? cause : new CompletionException(cause));
        return future;
    }

    @Override
    public void handleResult(T result) {
        complete(result);
    }

    @Override
    public void handleError(Throwable error) {
        completeExceptionally(error);
    }
}
