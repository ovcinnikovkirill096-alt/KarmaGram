package androidx.camera.camera2.pipe.compat;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraExtensionCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Trace;
import android.util.ArrayMap;
import androidx.camera.camera2.pipe.CameraError;
import androidx.camera.camera2.pipe.CameraExtensionMetadata;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.CameraMetadata;
import androidx.camera.camera2.pipe.CameraPipe;
import androidx.camera.camera2.pipe.DoNotDisturbException;
import androidx.camera.camera2.pipe.core.Debug;
import androidx.camera.camera2.pipe.core.DurationNs;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.core.Permissions;
import androidx.camera.camera2.pipe.core.Threads;
import androidx.camera.camera2.pipe.core.TimeSource;
import androidx.camera.camera2.pipe.core.Timestamps;
import java.util.Arrays;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import okhttp3.internal.url._UrlKt;

public final class Camera2MetadataCache implements Camera2MetadataProvider {
    private final ArrayMap cache;
    private final CameraPipe.CameraMetadataConfig cameraMetadataConfig;
    private final Context cameraPipeContext;
    private final ArrayMap extensionCache;
    private final ArrayMap extensionCharacteristicsCache;
    private final Permissions permissions;
    private final Threads threads;
    private final TimeSource timeSource;

    public Camera2MetadataCache(Context cameraPipeContext, Threads threads, Permissions permissions, CameraPipe.CameraMetadataConfig cameraMetadataConfig, TimeSource timeSource) {
        Intrinsics.checkNotNullParameter(cameraPipeContext, "cameraPipeContext");
        Intrinsics.checkNotNullParameter(threads, "threads");
        Intrinsics.checkNotNullParameter(permissions, "permissions");
        Intrinsics.checkNotNullParameter(cameraMetadataConfig, "cameraMetadataConfig");
        Intrinsics.checkNotNullParameter(timeSource, "timeSource");
        this.cameraPipeContext = cameraPipeContext;
        this.threads = threads;
        this.permissions = permissions;
        this.cameraMetadataConfig = cameraMetadataConfig;
        this.timeSource = timeSource;
        this.cache = new ArrayMap();
        this.extensionCache = new ArrayMap();
        this.extensionCharacteristicsCache = new ArrayMap();
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2MetadataProvider
    /* JADX INFO: renamed from: getCameraMetadata-0r8Bogc, reason: not valid java name */
    public Object mo473getCameraMetadata0r8Bogc(String str, Continuation continuation) {
        synchronized (this.cache) {
            CameraMetadata cameraMetadata = (CameraMetadata) this.cache.get(str);
            if (cameraMetadata != null) {
                return cameraMetadata;
            }
            Unit unit = Unit.INSTANCE;
            return BuildersKt.withContext(this.threads.getBackgroundDispatcher(), new Camera2MetadataCache$getCameraMetadata$3(this, str, null), continuation);
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2MetadataProvider
    /* JADX INFO: renamed from: awaitCameraMetadata-EfqyGwQ, reason: not valid java name */
    public CameraMetadata mo472awaitCameraMetadataEfqyGwQ(String cameraId) {
        CameraMetadata cameraMetadataM469createCameraMetadata0r8Bogc;
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(((Object) CameraId.m238toStringimpl(cameraId)) + "#awaitMetadata");
            synchronized (this.cache) {
                try {
                    cameraMetadataM469createCameraMetadata0r8Bogc = (CameraMetadata) this.cache.get(cameraId);
                    if (cameraMetadataM469createCameraMetadata0r8Bogc == null) {
                        if (isMetadataRedacted()) {
                            Unit unit = Unit.INSTANCE;
                            cameraMetadataM469createCameraMetadata0r8Bogc = m469createCameraMetadata0r8Bogc(cameraId, true);
                        } else {
                            cameraMetadataM469createCameraMetadata0r8Bogc = m469createCameraMetadata0r8Bogc(cameraId, false);
                            this.cache.put(cameraId, cameraMetadataM469createCameraMetadata0r8Bogc);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            Trace.endSection();
            return cameraMetadataM469createCameraMetadata0r8Bogc;
        } catch (Throwable th2) {
            Trace.endSection();
            throw th2;
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2MetadataProvider
    /* JADX INFO: renamed from: awaitCameraExtensionMetadata-0r8Bogc, reason: not valid java name */
    public CameraExtensionMetadata mo471awaitCameraExtensionMetadata0r8Bogc(String cameraId, int i) throws Exception {
        CameraExtensionMetadata cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE;
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        int i2 = Build.VERSION.SDK_INT;
        if (i2 < 31) {
            throw new Exception("Extension sessions are only supported on Android S or higher. Device SDK is " + i2);
        }
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(((Object) CameraId.m238toStringimpl(cameraId)) + "#awaitExtensionMetadata");
            synchronized (this.extensionCache) {
                try {
                    cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE = (CameraExtensionMetadata) this.extensionCache.get(cameraId);
                    if (cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE == null) {
                        if (isMetadataRedacted()) {
                            Unit unit = Unit.INSTANCE;
                            cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE = m468createCameraExtensionMetadataRzXb1QE(cameraId, true, i);
                        } else {
                            cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE = m468createCameraExtensionMetadataRzXb1QE(cameraId, false, i);
                            this.extensionCache.put(cameraId, cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            Trace.endSection();
            return cameraExtensionMetadataM468createCameraExtensionMetadataRzXb1QE;
        } catch (Throwable th2) {
            Trace.endSection();
            throw th2;
        }
    }

    @Override // androidx.camera.camera2.pipe.compat.Camera2MetadataProvider
    /* JADX INFO: renamed from: getSupportedCameraExtensions-EfqyGwQ, reason: not valid java name */
    public Set mo474getSupportedCameraExtensionsEfqyGwQ(String cameraId) {
        Intrinsics.checkNotNullParameter(cameraId, "cameraId");
        if (Build.VERSION.SDK_INT >= 31) {
            return CollectionsKt.toSet(Api31Compat.getSupportedExtensions(m470getCameraExtensionCharacteristicsEfqyGwQ(cameraId)));
        }
        return SetsKt.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: createCameraMetadata-0r8Bogc, reason: not valid java name */
    public final Camera2CameraMetadata m469createCameraMetadata0r8Bogc(String str, boolean z) {
        Set setPlus;
        String str2;
        Timestamps timestamps = Timestamps.INSTANCE;
        long jMo521nowvQl9yQU = this.timeSource.mo521nowvQl9yQU();
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(((Object) CameraId.m238toStringimpl(str)) + "#readCameraMetadata");
            try {
                Log log = Log.INSTANCE;
                if (log.getDEBUG_LOGGABLE()) {
                    android.util.Log.d("CXCP", "Loading metadata for " + ((Object) CameraId.m238toStringimpl(str)));
                }
                Object systemService = this.cameraPipeContext.getSystemService("camera");
                Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.camera2.CameraManager");
                CameraCharacteristics cameraCharacteristics = ((CameraManager) systemService).getCameraCharacteristics(str);
                Intrinsics.checkNotNullExpressionValue(cameraCharacteristics, "getCameraCharacteristics(...)");
                if (cameraCharacteristics != null) {
                    if (shouldBlockSensorOrientationCache(cameraCharacteristics)) {
                        Set setEmptySet = (Set) this.cameraMetadataConfig.getCameraCacheBlocklist().get(CameraId.m233boximpl(str));
                        if (setEmptySet == null) {
                            setEmptySet = SetsKt.emptySet();
                        }
                        setPlus = SetsKt.plus(setEmptySet, CameraCharacteristics.SENSOR_ORIENTATION);
                    } else {
                        setPlus = (Set) this.cameraMetadataConfig.getCameraCacheBlocklist().get(CameraId.m233boximpl(str));
                    }
                    Camera2CameraMetadata camera2CameraMetadata = new Camera2CameraMetadata(str, z, cameraCharacteristics, this, MapsKt.emptyMap(), setPlus == null ? this.cameraMetadataConfig.getCacheBlocklist() : SetsKt.plus(this.cameraMetadataConfig.getCacheBlocklist(), (Iterable) setPlus), null);
                    if (log.getINFO_LOGGABLE()) {
                        long jM513constructorimpl = DurationNs.m513constructorimpl(this.timeSource.mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                        if (!z) {
                            str2 = _UrlKt.FRAGMENT_ENCODE_SET;
                        } else {
                            if (!z) {
                                throw new NoWhenBranchMatchedException();
                            }
                            str2 = " (redacted)";
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Loaded metadata for ");
                        sb.append((Object) CameraId.m238toStringimpl(str));
                        sb.append(" in ");
                        String str3 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl / 1000000.0d)}, 1));
                        Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                        sb.append(str3);
                        sb.append(str2);
                        android.util.Log.i("CXCP", sb.toString());
                    }
                    Trace.endSection();
                    return camera2CameraMetadata;
                }
                throw new IllegalStateException(("Failed to get CameraCharacteristics for " + ((Object) CameraId.m238toStringimpl(str)) + '!').toString());
            } catch (Throwable th) {
                if (CameraError.Companion.shouldHandleDoNotDisturbException$camera_camera2_pipe(th)) {
                    throw new DoNotDisturbException("Failed to load metadata: Do Not Disturb mode is on!");
                }
                throw new IllegalStateException("Failed to load metadata for " + ((Object) CameraId.m238toStringimpl(str)) + '!', th);
            }
        } catch (Throwable th2) {
            Trace.endSection();
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: createCameraExtensionMetadata-RzXb1QE, reason: not valid java name */
    public final Camera2CameraExtensionMetadata m468createCameraExtensionMetadataRzXb1QE(String str, boolean z, int i) {
        String str2;
        Throwable th;
        String str3;
        Timestamps timestamps = Timestamps.INSTANCE;
        long jMo521nowvQl9yQU = this.timeSource.mo521nowvQl9yQU();
        Debug debug = Debug.INSTANCE;
        try {
            Trace.beginSection(((Object) CameraId.m238toStringimpl(str)) + "#readCameraExtensionMetadata");
            try {
                Log log = Log.INSTANCE;
                if (log.getDEBUG_LOGGABLE()) {
                    try {
                        android.util.Log.d("CXCP", "Loading extension metadata for " + ((Object) CameraId.m238toStringimpl(str)));
                    } catch (Throwable th2) {
                        th = th2;
                        str2 = str;
                        throw new IllegalStateException("Failed to load extension metadata for " + ((Object) CameraId.m238toStringimpl(str2)) + '!', th);
                    }
                }
                str2 = str;
                try {
                    Camera2CameraExtensionMetadata camera2CameraExtensionMetadata = new Camera2CameraExtensionMetadata(str2, z, i, m470getCameraExtensionCharacteristicsEfqyGwQ(str), MapsKt.emptyMap(), null);
                    if (log.getINFO_LOGGABLE()) {
                        long jM513constructorimpl = DurationNs.m513constructorimpl(this.timeSource.mo521nowvQl9yQU() - jMo521nowvQl9yQU);
                        if (!z) {
                            str3 = _UrlKt.FRAGMENT_ENCODE_SET;
                        } else {
                            if (!z) {
                                throw new NoWhenBranchMatchedException();
                            }
                            str3 = " (redacted)";
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Loaded extension metadata for ");
                        sb.append((Object) CameraId.m238toStringimpl(str2));
                        sb.append(" in ");
                        try {
                            String str4 = String.format(null, "%.3f ms", Arrays.copyOf(new Object[]{Double.valueOf(jM513constructorimpl / 1000000.0d)}, 1));
                            Intrinsics.checkNotNullExpressionValue(str4, "format(...)");
                            sb.append(str4);
                            sb.append(str3);
                            android.util.Log.i("CXCP", sb.toString());
                        } catch (Throwable th3) {
                            th = th3;
                            throw new IllegalStateException("Failed to load extension metadata for " + ((Object) CameraId.m238toStringimpl(str2)) + '!', th);
                        }
                    }
                    Trace.endSection();
                    return camera2CameraExtensionMetadata;
                } catch (Throwable th4) {
                    th = th4;
                    th = th;
                    throw new IllegalStateException("Failed to load extension metadata for " + ((Object) CameraId.m238toStringimpl(str2)) + '!', th);
                }
            } catch (Throwable th5) {
                th = th5;
                str2 = str;
            }
        } catch (Throwable th6) {
            Trace.endSection();
            throw th6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: getCameraExtensionCharacteristics-EfqyGwQ, reason: not valid java name */
    public final CameraExtensionCharacteristics m470getCameraExtensionCharacteristicsEfqyGwQ(String str) throws CameraAccessException {
        synchronized (this.extensionCharacteristicsCache) {
            CameraExtensionCharacteristics cameraExtensionCharacteristicsM = Camera2MetadataCache$$ExternalSyntheticApiModelOutline0.m(this.extensionCharacteristicsCache.get(str));
            if (cameraExtensionCharacteristicsM != null) {
                return cameraExtensionCharacteristicsM;
            }
            Unit unit = Unit.INSTANCE;
            if (Log.INSTANCE.getDEBUG_LOGGABLE()) {
                android.util.Log.d("CXCP", "Retrieving CameraExtensionCharacteristics for " + ((Object) CameraId.m238toStringimpl(str)));
            }
            Object systemService = this.cameraPipeContext.getSystemService("camera");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.camera2.CameraManager");
            CameraExtensionCharacteristics cameraExtensionCharacteristics = Api31Compat.getCameraExtensionCharacteristics((CameraManager) systemService, str);
            if (cameraExtensionCharacteristics != null) {
                return cameraExtensionCharacteristics;
            }
            throw new IllegalStateException(("Failed to get CameraExtensionCharacteristics for " + ((Object) CameraId.m238toStringimpl(str)) + '!').toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isMetadataRedacted() {
        return !this.permissions.getHasCameraPermission();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean shouldBlockSensorOrientationCache(CameraCharacteristics cameraCharacteristics) {
        return Build.VERSION.SDK_INT >= 32 && cameraCharacteristics.get(CameraCharacteristics.INFO_DEVICE_STATE_SENSOR_ORIENTATION_MAP) != null;
    }
}
