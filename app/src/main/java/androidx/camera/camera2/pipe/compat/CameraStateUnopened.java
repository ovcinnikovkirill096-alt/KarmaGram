package androidx.camera.camera2.pipe.compat;

public final class CameraStateUnopened extends CameraState {
    public static final CameraStateUnopened INSTANCE = new CameraStateUnopened();

    private CameraStateUnopened() {
        super(null);
    }
}
