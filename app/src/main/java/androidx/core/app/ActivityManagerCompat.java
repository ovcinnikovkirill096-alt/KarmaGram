package androidx.core.app;

import android.app.ActivityManager;

public abstract class ActivityManagerCompat {
    public static boolean isLowRamDevice(ActivityManager activityManager) {
        return activityManager.isLowRamDevice();
    }
}
