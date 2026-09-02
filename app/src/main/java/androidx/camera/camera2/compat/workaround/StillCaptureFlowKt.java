package androidx.camera.camera2.compat.workaround;

import android.hardware.camera2.CaptureRequest;
import androidx.camera.camera2.compat.quirk.DeviceQuirks;
import androidx.camera.camera2.compat.quirk.StillCaptureFlashStopRepeatingQuirk;
import androidx.camera.camera2.pipe.Request;
import androidx.camera.camera2.pipe.RequestTemplate;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

public abstract class StillCaptureFlowKt {
    public static final boolean shouldStopRepeatingBeforeCapture(List list) {
        Intrinsics.checkNotNullParameter(list, "<this>");
        if (((StillCaptureFlashStopRepeatingQuirk) DeviceQuirks.INSTANCE.get(StillCaptureFlashStopRepeatingQuirk.class)) == null) {
            return false;
        }
        Iterator it = list.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            Request request = (Request) it.next();
            RequestTemplate requestTemplateM368getTemplateejQnlcg = request.m368getTemplateejQnlcg();
            if (requestTemplateM368getTemplateejQnlcg != null && requestTemplateM368getTemplateejQnlcg.m391unboximpl() == 2) {
                z = true;
            }
            CaptureRequest.Key CONTROL_AE_MODE = CaptureRequest.CONTROL_AE_MODE;
            Intrinsics.checkNotNullExpressionValue(CONTROL_AE_MODE, "CONTROL_AE_MODE");
            Integer num = (Integer) request.get(CONTROL_AE_MODE);
            if ((num != null && num.intValue() == 2) || (num != null && num.intValue() == 3)) {
                z2 = true;
            }
        }
        return z && z2;
    }
}
