package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.core.DurationNs;
import okhttp3.internal.connection.RealConnection;

public abstract class RetryingCameraStateOpenerKt {
    private static final long defaultCameraRetryTimeoutNs = DurationNs.m513constructorimpl(RealConnection.IDLE_CONNECTION_HEALTHY_NS);
    private static final long activeResumeCameraRetryTimeoutNs = DurationNs.m513constructorimpl(1800000000000L);
    private static final DurationNs[] activeResumeCameraRetryThresholds = {DurationNs.m511boximpl(DurationNs.m513constructorimpl(120000000000L)), DurationNs.m511boximpl(DurationNs.m513constructorimpl(300000000000L))};
}
