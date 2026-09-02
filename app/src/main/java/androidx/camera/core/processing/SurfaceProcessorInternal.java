package androidx.camera.core.processing;

import androidx.camera.core.SurfaceProcessor;

public interface SurfaceProcessorInternal extends SurfaceProcessor {
    void release();
}
