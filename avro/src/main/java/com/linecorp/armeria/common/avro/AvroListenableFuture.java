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

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.avro.ipc.Callback;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionException;

import static java.util.Objects.requireNonNull;

/**
 * A {@link ListenableFuture} that can be passed in as an {@link Callback}
 * when making an asynchronous client-side Avro RPC.
 */
public final class AvroListenableFuture<T> extends AbstractFuture<T> implements Callback<T> {

    /**
     * Returns a new {@link AvroListenableFuture} instance that has its value set immediately.
     */
    public static <T> AvroListenableFuture<T> completedFuture(@Nullable T value) {
        final AvroListenableFuture<T> future = new AvroListenableFuture<>();
        future.handleResult(value);
        return future;
    }

    /**
     * Returns a new {@link AvroListenableFuture} instance that has an exception set immediately.
     */
    public static <T> AvroListenableFuture<T> exceptionallyCompletedFuture(Throwable cause) {
        requireNonNull(cause, "cause");
        final AvroListenableFuture<T> future = new AvroListenableFuture<>();
        future.handleError(cause instanceof Exception ? (Exception) cause : new CompletionException(cause));
        return future;
    }

    @Override
    public void handleResult(T result) {
        set(result);
    }

    @Override
    public void handleError(Throwable error) {
        setException(error);
    }
}
