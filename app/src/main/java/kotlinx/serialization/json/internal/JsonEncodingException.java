package kotlinx.serialization.json.internal;

import kotlin.jvm.internal.Intrinsics;

public final class JsonEncodingException extends JsonException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JsonEncodingException(String message) {
        super(message);
        Intrinsics.checkNotNullParameter(message, "message");
    }
}
