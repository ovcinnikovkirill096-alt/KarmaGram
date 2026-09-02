package androidx.camera.core;

import androidx.lifecycle.LiveData;

public interface CameraInfo {
    CameraIdentifier getCameraIdentifier();

    LiveData getCameraState();

    int getLensFacing();

    int getSensorRotationDegrees();

    int getSensorRotationDegrees(int i);

    LiveData getZoomState();

    boolean hasFlashUnit();
}
