package androidx.camera.core.processing.util;

import android.opengl.EGLSurface;

public abstract class OutputSurface {
    public abstract EGLSurface getEglSurface();

    public abstract int getHeight();

    public abstract int getWidth();

    public static OutputSurface of(EGLSurface eGLSurface, int i, int i2) {
        return new AutoValue_OutputSurface(eGLSurface, i, i2);
    }
}
