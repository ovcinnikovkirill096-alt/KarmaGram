package androidx.camera.core.imagecapture;

import androidx.camera.core.ImageCaptureException;

public abstract class RequestWithCallback {
    abstract void abortAndSendErrorToApp(ImageCaptureException imageCaptureException);

    abstract void abortSilentlyAndRetry();
}
