package androidx.camera.camera2.pipe.media;

import androidx.camera.camera2.pipe.UnsafeWrapper;

public interface ImageWriterWrapper extends UnsafeWrapper, AutoCloseable {
    boolean queueInputImage(ImageWrapper imageWrapper);
}
