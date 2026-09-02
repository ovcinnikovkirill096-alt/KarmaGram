package androidx.camera.camera2.compat.quirk;

import android.annotation.SuppressLint;
import android.os.Build;
import androidx.camera.core.impl.Quirk;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@SuppressLint({"CameraXQuirksClassDetector"})
public final class CloseCameraDeviceOnCameraGraphCloseQuirk implements Quirk {
    public static final Companion Companion = new Companion(null);
    private static final boolean isSamsungExynos7570Device;
    private static final boolean isSamsungExynos7870Device;
    private static final boolean isSamsungProblematicDevice;
    private static final boolean isSonyProblematicDevice;
    private static final boolean isXiaomiProblematicDevice;

    public final boolean shouldCloseCameraDevice(boolean z) {
        if (isXiaomiProblematicDevice) {
            return z;
        }
        if (!isSamsungProblematicDevice || isSamsungExynos7570Device || isSamsungExynos7870Device) {
            return true;
        }
        return z;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isEnabled() {
            if (CloseCameraDeviceOnCameraGraphCloseQuirk.isSamsungExynos7570Device || CloseCameraDeviceOnCameraGraphCloseQuirk.isSamsungExynos7870Device) {
                return true;
            }
            int i = Build.VERSION.SDK_INT;
            if (30 <= i && i < 34) {
                Device device = Device.INSTANCE;
                if (device.isOppoDevice() || device.isOnePlusDevice() || device.isRealmeDevice()) {
                    return true;
                }
            }
            return Device.INSTANCE.isVivoDevice() || CloseCameraDeviceOnCameraGraphCloseQuirk.isXiaomiProblematicDevice || CloseCameraDeviceOnCameraGraphCloseQuirk.isSamsungProblematicDevice || CloseCameraDeviceOnCameraGraphCloseQuirk.isSonyProblematicDevice;
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x008d A[EDGE_INSN: B:21:0x008d->B:22:0x008e BREAK  A[LOOP:0: B:16:0x0074->B:33:?]] */
    /* JADX WARN: Code duplicated, block: B:7:0x0049  */
    static {
        boolean z;
        boolean z2;
        int i;
        String str = Build.HARDWARE;
        isSamsungExynos7570Device = Intrinsics.areEqual(str, "samsungexynos7570");
        isSamsungExynos7870Device = Intrinsics.areEqual(str, "samsungexynos7870");
        Device device = Device.INSTANCE;
        boolean z3 = false;
        if (device.isXiaomiDevice()) {
            String DEVICE = Build.DEVICE;
            Intrinsics.checkNotNullExpressionValue(DEVICE, "DEVICE");
            String lowerCase = DEVICE.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            if (ArraysKt.contains(new String[]{"aurora", "houji"}, lowerCase)) {
                z = true;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        isXiaomiProblematicDevice = z;
        if (!device.isSonyDevice()) {
            z2 = false;
            break;
        }
        List listListOf = CollectionsKt.listOf((Object[]) new String[]{"XQ-DQ", "SO", "A301SO"});
        if (!(listListOf instanceof Collection) || !listListOf.isEmpty()) {
            Iterator it = listListOf.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z2 = false;
                    break;
                }
                String str2 = (String) it.next();
                String DEVICE2 = Build.DEVICE;
                Intrinsics.checkNotNullExpressionValue(DEVICE2, "DEVICE");
                if (StringsKt.startsWith(DEVICE2, str2, true)) {
                    z2 = true;
                    break;
                }
            }
        } else {
            z2 = false;
            break;
        }
        isSonyProblematicDevice = z2;
        if (Device.INSTANCE.isSamsungDevice() && (i = Build.VERSION.SDK_INT) >= 31 && i <= 34) {
            z3 = true;
        }
        isSamsungProblematicDevice = z3;
    }
}
