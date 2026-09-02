package androidx.camera.camera2.pipe.graph;

import androidx.camera.camera2.pipe.FrameMetadata;

public interface Result3AStateListener extends GraphLoop.Listener {
    /* JADX INFO: renamed from: onRequestSequenceCreated-DThHKJ0, reason: not valid java name */
    void mo547onRequestSequenceCreatedDThHKJ0(long j);

    /* JADX INFO: renamed from: update-voP-kFw, reason: not valid java name */
    boolean mo548updatevoPkFw(long j, FrameMetadata frameMetadata);
}
