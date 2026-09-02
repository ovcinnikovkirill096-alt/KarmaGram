package androidx.camera.camera2.pipe.core;

public interface Token {
    boolean getReleased();

    boolean release();
}
