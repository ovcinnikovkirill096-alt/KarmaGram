package androidx.camera.camera2.pipe.graph;

import androidx.camera.camera2.pipe.Lock3ABehavior;
import java.util.Collection;

public abstract class Controller3AKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isNullOrIn(Object obj, Collection collection) {
        if (obj != null) {
            return collection.contains(obj);
        }
        return true;
    }

    /* JADX INFO: renamed from: shouldUnlockAe-t6FjEyI, reason: not valid java name */
    public static final boolean m536shouldUnlockAet6FjEyI(Lock3ABehavior lock3ABehavior) {
        int iM294getAFTER_NEW_SCANhRqSH3k = Lock3ABehavior.Companion.m294getAFTER_NEW_SCANhRqSH3k();
        if (lock3ABehavior == null) {
            return false;
        }
        return Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), iM294getAFTER_NEW_SCANhRqSH3k);
    }

    /* JADX INFO: renamed from: shouldUnlockAf-t6FjEyI, reason: not valid java name */
    public static final boolean m537shouldUnlockAft6FjEyI(Lock3ABehavior lock3ABehavior) {
        int iM294getAFTER_NEW_SCANhRqSH3k = Lock3ABehavior.Companion.m294getAFTER_NEW_SCANhRqSH3k();
        if (lock3ABehavior == null) {
            return false;
        }
        return Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), iM294getAFTER_NEW_SCANhRqSH3k);
    }

    /* JADX INFO: renamed from: shouldUnlockAwb-t6FjEyI, reason: not valid java name */
    public static final boolean m538shouldUnlockAwbt6FjEyI(Lock3ABehavior lock3ABehavior) {
        int iM294getAFTER_NEW_SCANhRqSH3k = Lock3ABehavior.Companion.m294getAFTER_NEW_SCANhRqSH3k();
        if (lock3ABehavior == null) {
            return false;
        }
        return Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), iM294getAFTER_NEW_SCANhRqSH3k);
    }

    /* JADX INFO: renamed from: shouldWaitForAeToConverge-t6FjEyI, reason: not valid java name */
    public static final boolean m539shouldWaitForAeToConverget6FjEyI(Lock3ABehavior lock3ABehavior) {
        if (lock3ABehavior != null) {
            return !Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), Lock3ABehavior.Companion.m295getIMMEDIATEhRqSH3k());
        }
        return false;
    }

    /* JADX INFO: renamed from: shouldWaitForAwbToConverge-t6FjEyI, reason: not valid java name */
    public static final boolean m541shouldWaitForAwbToConverget6FjEyI(Lock3ABehavior lock3ABehavior) {
        if (lock3ABehavior != null) {
            return !Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), Lock3ABehavior.Companion.m295getIMMEDIATEhRqSH3k());
        }
        return false;
    }

    /* JADX INFO: renamed from: shouldWaitForAfToConverge-t6FjEyI, reason: not valid java name */
    public static final boolean m540shouldWaitForAfToConverget6FjEyI(Lock3ABehavior lock3ABehavior) {
        if (lock3ABehavior != null) {
            return !Lock3ABehavior.m289equalsimpl0(lock3ABehavior.m292unboximpl(), Lock3ABehavior.Companion.m295getIMMEDIATEhRqSH3k());
        }
        return false;
    }
}
