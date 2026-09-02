package j$.util;

import j$.time.Instant;
import java.util.Date;
import org.telegram.messenger.MediaDataController;

public final /* synthetic */ class DateRetargetClass {
    public static Instant toInstant(Date date) {
        long time = date.getTime();
        Instant instant = Instant.c;
        long j = MediaDataController.MAX_STYLE_RUNS_COUNT;
        return Instant.Q(j$.com.android.tools.r8.a.U(time, j), ((int) j$.com.android.tools.r8.a.T(time, j)) * 1000000);
    }
}
