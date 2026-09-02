package androidx.camera.extensions.internal;

import android.hardware.camera2.CameraExtensionCharacteristics;
import android.hardware.camera2.CameraManager;
import androidx.camera.camera2.pipe.compat.Camera2MetadataCache$$ExternalSyntheticApiModelOutline0;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class Camera2ExtensionsInfo {
    private static final Companion Companion = new Companion(null);
    private final Map cachedCharacteristics;
    private final Map cachedSupportedExtensions;
    private final Map cachedSupportedOutputSizes;
    private final CameraManager cameraManager;
    private final Object lock;

    public Camera2ExtensionsInfo(CameraManager cameraManager) {
        Intrinsics.checkNotNullParameter(cameraManager, "cameraManager");
        this.cameraManager = cameraManager;
        this.lock = new Object();
        this.cachedCharacteristics = new LinkedHashMap();
        this.cachedSupportedOutputSizes = new LinkedHashMap();
        this.cachedSupportedExtensions = new LinkedHashMap();
    }

    public CameraExtensionCharacteristics getExtensionCharacteristics(String cameraId) {
        CameraExtensionCharacteristics cameraExtensionCharacteristicsM;
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        synchronized (this.lock) {
            cameraExtensionCharacteristicsM = Camera2MetadataCache$$ExternalSyntheticApiModelOutline0.m(this.cachedCharacteristics.get(cameraId));
            if (cameraExtensionCharacteristicsM == null) {
                cameraExtensionCharacteristicsM = this.cameraManager.getCameraExtensionCharacteristics(cameraId);
                this.cachedCharacteristics.put(cameraId, cameraExtensionCharacteristicsM);
                Intrinsics.checkNotNullExpressionValue(cameraExtensionCharacteristicsM, "also(...)");
            }
        }
        return cameraExtensionCharacteristicsM;
    }

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
