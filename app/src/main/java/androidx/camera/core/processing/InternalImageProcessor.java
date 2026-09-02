package androidx.camera.core.processing;

import androidx.camera.core.ImageProcessor$Request;
import androidx.camera.core.ImageProcessor$Response;

public abstract class InternalImageProcessor {
    public abstract ImageProcessor$Response safeProcess(ImageProcessor$Request imageProcessor$Request);
}
