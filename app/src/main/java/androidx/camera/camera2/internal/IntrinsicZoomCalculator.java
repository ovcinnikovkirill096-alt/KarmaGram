package androidx.camera.camera2.internal;

import androidx.camera.camera2.pipe.CameraMetadata;

public interface IntrinsicZoomCalculator {
    Float calculateIntrinsicZoomRatio(CameraMetadata cameraMetadata);
}
