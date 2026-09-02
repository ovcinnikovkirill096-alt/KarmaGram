package androidx.camera.core.imagecapture;

import androidx.camera.core.ImageCaptureException;

public interface TakePictureManager {

    public interface Provider {
        TakePictureManager newInstance(ImageCaptureControl imageCaptureControl);
    }

    void abortRequests();

    void pause();

    void resume();

    void setImagePipeline(ImagePipeline imagePipeline);

    public static abstract class CaptureError {
        abstract ImageCaptureException getImageCaptureException();

        abstract int getRequestId();

        static CaptureError of(int i, ImageCaptureException imageCaptureException) {
            return new AutoValue_TakePictureManager_CaptureError(i, imageCaptureException);
        }
    }
}
