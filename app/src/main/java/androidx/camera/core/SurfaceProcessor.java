package androidx.camera.core;

public interface SurfaceProcessor {
    void onInputSurface(SurfaceRequest surfaceRequest);

    void onOutputSurface(SurfaceOutput surfaceOutput);
}
