package androidx.camera.core.internal.compat.quirk;

import android.util.Range;
import androidx.camera.core.impl.Quirk;

public interface AeFpsRangeQuirk extends Quirk {
    Range getTargetAeFpsRange();
}
