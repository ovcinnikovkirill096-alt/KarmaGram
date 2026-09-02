package androidx.camera.camera2.pipe.graph;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import androidx.camera.camera2.pipe.AeMode;
import androidx.camera.camera2.pipe.AfMode;
import androidx.camera.camera2.pipe.AwbMode;
import androidx.camera.camera2.pipe.FlashMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;

public abstract class GraphState3AKt {
    public static final Map toCaptureRequestParameterMap(State3A state3A) {
        Intrinsics.checkNotNullParameter(state3A, "<this>");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        AeMode aeModeM550getAeModeO_cDUUs = state3A.m550getAeModeO_cDUUs();
        if (aeModeM550getAeModeO_cDUUs != null) {
            int iM122unboximpl = aeModeM550getAeModeO_cDUUs.m122unboximpl();
            CaptureRequest.Key CONTROL_AE_MODE = CaptureRequest.CONTROL_AE_MODE;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AE_MODE, "CONTROL_AE_MODE");
            linkedHashMap.put(CONTROL_AE_MODE, Integer.valueOf(iM122unboximpl));
        }
        AfMode afModeM551getAfMode32_E3BI = state3A.m551getAfMode32_E3BI();
        if (afModeM551getAfMode32_E3BI != null) {
            int iM133unboximpl = afModeM551getAfMode32_E3BI.m133unboximpl();
            CaptureRequest.Key CONTROL_AF_MODE = CaptureRequest.CONTROL_AF_MODE;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AF_MODE, "CONTROL_AF_MODE");
            linkedHashMap.put(CONTROL_AF_MODE, Integer.valueOf(iM133unboximpl));
        }
        AwbMode awbModeM552getAwbModeaLFtWSU = state3A.m552getAwbModeaLFtWSU();
        if (awbModeM552getAwbModeaLFtWSU != null) {
            int iM152unboximpl = awbModeM552getAwbModeaLFtWSU.m152unboximpl();
            CaptureRequest.Key CONTROL_AWB_MODE = CaptureRequest.CONTROL_AWB_MODE;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AWB_MODE, "CONTROL_AWB_MODE");
            linkedHashMap.put(CONTROL_AWB_MODE, Integer.valueOf(iM152unboximpl));
        }
        FlashMode flashModeM553getFlashModecL19HE = state3A.m553getFlashModecL19HE();
        if (flashModeM553getFlashModecL19HE != null) {
            int iM266unboximpl = flashModeM553getFlashModecL19HE.m266unboximpl();
            CaptureRequest.Key FLASH_MODE = CaptureRequest.FLASH_MODE;
            Intrinsics.checkNotNullExpressionValue(FLASH_MODE, "FLASH_MODE");
            linkedHashMap.put(FLASH_MODE, Integer.valueOf(iM266unboximpl));
        }
        List aeRegions = state3A.getAeRegions();
        if (aeRegions != null) {
            CaptureRequest.Key CONTROL_AE_REGIONS = CaptureRequest.CONTROL_AE_REGIONS;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AE_REGIONS, "CONTROL_AE_REGIONS");
            linkedHashMap.put(CONTROL_AE_REGIONS, aeRegions.toArray(new MeteringRectangle[0]));
        }
        List afRegions = state3A.getAfRegions();
        if (afRegions != null) {
            CaptureRequest.Key CONTROL_AF_REGIONS = CaptureRequest.CONTROL_AF_REGIONS;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AF_REGIONS, "CONTROL_AF_REGIONS");
            linkedHashMap.put(CONTROL_AF_REGIONS, afRegions.toArray(new MeteringRectangle[0]));
        }
        List awbRegions = state3A.getAwbRegions();
        if (awbRegions != null) {
            CaptureRequest.Key CONTROL_AWB_REGIONS = CaptureRequest.CONTROL_AWB_REGIONS;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AWB_REGIONS, "CONTROL_AWB_REGIONS");
            linkedHashMap.put(CONTROL_AWB_REGIONS, awbRegions.toArray(new MeteringRectangle[0]));
        }
        Boolean aeLock = state3A.getAeLock();
        if (aeLock != null) {
            CaptureRequest.Key CONTROL_AE_LOCK = CaptureRequest.CONTROL_AE_LOCK;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AE_LOCK, "CONTROL_AE_LOCK");
            linkedHashMap.put(CONTROL_AE_LOCK, aeLock);
        }
        Boolean awbLock = state3A.getAwbLock();
        if (awbLock != null) {
            CaptureRequest.Key CONTROL_AWB_LOCK = CaptureRequest.CONTROL_AWB_LOCK;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AWB_LOCK, "CONTROL_AWB_LOCK");
            linkedHashMap.put(CONTROL_AWB_LOCK, awbLock);
        }
        return linkedHashMap;
    }
}
