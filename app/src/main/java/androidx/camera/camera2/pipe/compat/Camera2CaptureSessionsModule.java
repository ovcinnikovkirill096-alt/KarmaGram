package androidx.camera.camera2.pipe.compat;

import android.os.Build;
import androidx.camera.camera2.pipe.CameraGraph;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;

public final class Camera2CaptureSessionsModule {
    public static final Camera2CaptureSessionsModule INSTANCE = new Camera2CaptureSessionsModule();

    private Camera2CaptureSessionsModule() {
    }

    public final CaptureSessionFactory provideSessionFactory(Provider androidMProvider, Provider androidMHighSpeedProvider, Provider androidNProvider, Provider androidPProvider, Provider androidExtensionProvider, CameraGraph.Config graphConfig) {
        Intrinsics.checkNotNullParameter(androidMProvider, "androidMProvider");
        Intrinsics.checkNotNullParameter(androidMHighSpeedProvider, "androidMHighSpeedProvider");
        Intrinsics.checkNotNullParameter(androidNProvider, "androidNProvider");
        Intrinsics.checkNotNullParameter(androidPProvider, "androidPProvider");
        Intrinsics.checkNotNullParameter(androidExtensionProvider, "androidExtensionProvider");
        Intrinsics.checkNotNullParameter(graphConfig, "graphConfig");
        int iM210getSessionMode2uNL3no = graphConfig.m210getSessionMode2uNL3no();
        CameraGraph.OperatingMode.Companion companion = CameraGraph.OperatingMode.Companion;
        if (CameraGraph.OperatingMode.m222equalsimpl0(iM210getSessionMode2uNL3no, companion.m226getEXTENSION2uNL3no())) {
            if (Build.VERSION.SDK_INT < 31) {
                throw new IllegalStateException("Cannot use Extension sessions below Android S");
            }
            Object obj = androidExtensionProvider.get();
            Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
            return (CaptureSessionFactory) obj;
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 28) {
            Object obj2 = androidPProvider.get();
            Intrinsics.checkNotNullExpressionValue(obj2, "get(...)");
            return (CaptureSessionFactory) obj2;
        }
        if (CameraGraph.OperatingMode.m222equalsimpl0(graphConfig.m210getSessionMode2uNL3no(), companion.m227getHIGH_SPEED2uNL3no())) {
            Object obj3 = androidMHighSpeedProvider.get();
            Intrinsics.checkNotNullExpressionValue(obj3, "get(...)");
            return (CaptureSessionFactory) obj3;
        }
        if (i >= 24) {
            Object obj4 = androidNProvider.get();
            Intrinsics.checkNotNullExpressionValue(obj4, "get(...)");
            return (CaptureSessionFactory) obj4;
        }
        if (graphConfig.getInput() != null) {
            throw new IllegalStateException("Reprocessing is not supported on Android M");
        }
        Object obj5 = androidMProvider.get();
        Intrinsics.checkNotNullExpressionValue(obj5, "get(...)");
        return (CaptureSessionFactory) obj5;
    }
}
