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

package com.linecorp.armeria.common;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

final class HttpRequestAggregator extends HttpMessageAggregator {

    private final HttpRequest request;
    private HttpHeaders trailingHeaders;

    HttpRequestAggregator(HttpRequest request, CompletableFuture<AggregatedHttpMessage> future,
                          @Nullable ByteBufAllocator alloc) {
        super(future, alloc);
        this.request = request;
        trailingHeaders = HttpHeaders.of();
    }

    @Override
    protected void onHeaders(HttpHeaders headers) {
        if (headers.isEmpty()) {
            return;
        }

        if (trailingHeaders.isEmpty()) {
            trailingHeaders = headers;
        } else {
            // Optionally, only one trailers can be present.
            // See https://tools.ietf.org/html/rfc7540#section-8.1
        }
    }

    @Override
    protected void onData(HttpData data) {
        if (!trailingHeaders.isEmpty()) {
            ReferenceCountUtil.safeRelease(data);
            // Data can't come after trailers.
            // See https://tools.ietf.org/html/rfc7540#section-8.1
            return;
        }
        super.onData(data);
    }

    @Override
    protected AggregatedHttpMessage onSuccess(HttpData content) {
        return AggregatedHttpMessage.of(request.headers(), content, trailingHeaders);
    }

    @Override
    protected void onFailure() {
        trailingHeaders = HttpHeaders.of();
    }
}
