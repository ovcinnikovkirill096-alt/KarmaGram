package okhttp3.internal;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.Response;

public final class UnreadableResponseBodyKt {
    public static final Response stripBody(Response response) {
        Intrinsics.checkNotNullParameter(response, "<this>");
        return response.newBuilder().body(new UnreadableResponseBody(response.body().contentType(), response.body().contentLength())).build();
    }
}
