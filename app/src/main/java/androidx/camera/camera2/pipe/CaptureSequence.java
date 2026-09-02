package androidx.camera.camera2.pipe;

import java.util.List;

public interface CaptureSequence {

    public interface CaptureSequenceListener {
        void onCaptureSequenceComplete(CaptureSequence captureSequence);
    }

    List getCaptureMetadataList();

    List getListeners();

    boolean getRepeating();

    void setSequenceNumber(int i);
}
