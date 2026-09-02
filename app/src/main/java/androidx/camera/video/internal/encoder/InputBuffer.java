package androidx.camera.video.internal.encoder;

import com.google.common.util.concurrent.ListenableFuture;

public interface InputBuffer {
    ListenableFuture getTerminationFuture();

    void setEndOfStream(boolean z);

    void setPresentationTimeUs(long j);

    boolean submit();
}
