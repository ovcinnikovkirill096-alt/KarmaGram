package kotlin.jdk7;

import androidx.camera.camera2.config.UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0;
import kotlin.ExceptionsKt;

public abstract class AutoCloseableKt {
    public static final void closeFinally(AutoCloseable autoCloseable, Throwable th) throws Exception {
        if (autoCloseable != null) {
            if (th == null) {
                UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0.m(autoCloseable);
                return;
            }
            try {
                UseCaseGraphContext$$ExternalSyntheticAutoCloseableDispatcher0.m(autoCloseable);
            } catch (Throwable th2) {
                ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }
}
