package okhttp3.internal.cache;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.internal.http.HttpMethod;

public final class CacheInterceptorKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final Request requestForCache(Request request) {
        HttpUrl httpUrlCacheUrlOverride = request.cacheUrlOverride();
        if (httpUrlCacheUrlOverride != null) {
            return (HttpMethod.INSTANCE.isCacheable(request.method()) || Intrinsics.areEqual(request.method(), "POST")) ? request.newBuilder().get().url(httpUrlCacheUrlOverride).cacheUrlOverride(null).build() : request;
        }
        return request;
    }
}
