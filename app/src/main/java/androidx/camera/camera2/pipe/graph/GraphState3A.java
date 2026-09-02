package androidx.camera.camera2.pipe.graph;

import androidx.camera.camera2.pipe.AeMode;
import androidx.camera.camera2.pipe.AfMode;
import androidx.camera.camera2.pipe.AwbMode;
import androidx.camera.camera2.pipe.FlashMode;
import java.util.List;
import java.util.Map;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;

public final class GraphState3A {
    private final AtomicRef _state = AtomicFU.atomic(new State3A(null, null, null, null, null, null, null, null, null, null, 1023, null));

    public final State3A getCurrent() {
        return (State3A) this._state.getValue();
    }

    /* JADX INFO: renamed from: update-7jOEVJU$default, reason: not valid java name */
    public static /* synthetic */ void m542update7jOEVJU$default(GraphState3A graphState3A, AeMode aeMode, AfMode afMode, AwbMode awbMode, FlashMode flashMode, List list, List list2, List list3, Boolean bool, Boolean bool2, Boolean bool3, int i, Object obj) {
        if ((i & 1) != 0) {
            aeMode = null;
        }
        if ((i & 2) != 0) {
            afMode = null;
        }
        if ((i & 4) != 0) {
            awbMode = null;
        }
        if ((i & 8) != 0) {
            flashMode = null;
        }
        if ((i & 16) != 0) {
            list = null;
        }
        if ((i & 32) != 0) {
            list2 = null;
        }
        if ((i & 64) != 0) {
            list3 = null;
        }
        if ((i & 128) != 0) {
            bool = null;
        }
        if ((i & 256) != 0) {
            bool2 = null;
        }
        if ((i & 512) != 0) {
            bool3 = null;
        }
        graphState3A.m543update7jOEVJU(aeMode, afMode, awbMode, flashMode, list, list2, list3, bool, bool2, bool3);
    }

    /* JADX WARN: Code duplicated, block: B:23:0x003e  */
    /* JADX WARN: Code duplicated, block: B:30:0x0053  */
    /* JADX WARN: Code duplicated, block: B:38:0x0069  */
    /* JADX INFO: renamed from: update-7jOEVJU, reason: not valid java name */
    public final void m543update7jOEVJU(AeMode aeMode, AfMode afMode, AwbMode awbMode, FlashMode flashMode, List list, List list2, List list3, Boolean bool, Boolean bool2, Boolean bool3) {
        Object value;
        State3A state3A;
        AeMode aeModeM550getAeModeO_cDUUs;
        AfMode afModeM551getAfMode32_E3BI;
        AwbMode awbModeM552getAwbModeaLFtWSU;
        FlashMode flashModeM553getFlashModecL19HE;
        List aeRegions;
        List afRegions;
        List awbRegions;
        Boolean aeLock;
        Boolean afLock;
        List list4;
        Boolean awbLock;
        AtomicRef atomicRef = this._state;
        do {
            value = atomicRef.getValue();
            state3A = (State3A) value;
            aeModeM550getAeModeO_cDUUs = aeMode == null ? state3A.m550getAeModeO_cDUUs() : aeMode;
            afModeM551getAfMode32_E3BI = afMode == null ? state3A.m551getAfMode32_E3BI() : afMode;
            awbModeM552getAwbModeaLFtWSU = awbMode == null ? state3A.m552getAwbModeaLFtWSU() : awbMode;
            flashModeM553getFlashModecL19HE = flashMode == null ? state3A.m553getFlashModecL19HE() : flashMode;
            if (list != null) {
                List list5 = list;
                if (list5.isEmpty()) {
                    list5 = null;
                }
                aeRegions = list5;
                if (aeRegions == null) {
                    aeRegions = state3A.getAeRegions();
                }
            } else {
                aeRegions = state3A.getAeRegions();
            }
            if (list2 != null) {
                List list6 = list2;
                if (list6.isEmpty()) {
                    list6 = null;
                }
                afRegions = list6;
                if (afRegions == null) {
                    afRegions = state3A.getAfRegions();
                }
            } else {
                afRegions = state3A.getAfRegions();
            }
            if (list3 != null) {
                List list7 = list3;
                awbRegions = list7.isEmpty() ? null : list7;
                if (awbRegions == null) {
                    awbRegions = state3A.getAwbRegions();
                }
            } else {
                awbRegions = state3A.getAwbRegions();
            }
            aeLock = bool == null ? state3A.getAeLock() : bool;
            afLock = bool2 == null ? state3A.getAfLock() : bool2;
            if (bool3 == null) {
                awbLock = state3A.getAwbLock();
                list4 = afRegions;
            } else {
                list4 = afRegions;
                awbLock = bool3;
            }
        } while (!atomicRef.compareAndSet(value, state3A.m549copy7jOEVJU(aeModeM550getAeModeO_cDUUs, afModeM551getAfMode32_E3BI, awbModeM552getAwbModeaLFtWSU, flashModeM553getFlashModecL19HE, aeRegions, list4, awbRegions, aeLock, afLock, awbLock)));
    }

    public final Map toCaptureRequestParametersMap() {
        return GraphState3AKt.toCaptureRequestParameterMap(getCurrent());
    }
}
