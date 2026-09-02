package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.Intrinsics;

public final class DoNotDisturbException extends RuntimeException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DoNotDisturbException(String message) {
        super(message);
        Intrinsics.checkNotNullParameter(message, "message");
    }
}
