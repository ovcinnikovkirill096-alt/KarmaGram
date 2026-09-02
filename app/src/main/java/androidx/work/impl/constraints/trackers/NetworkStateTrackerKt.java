package androidx.work.impl.constraints.trackers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.core.net.ConnectivityManagerCompat;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import kotlin.jvm.internal.Intrinsics;

public abstract class NetworkStateTrackerKt {
    private static final String TAG;

    public static final ConstraintTracker NetworkStateTracker(Context context, TaskExecutor taskExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(taskExecutor, "taskExecutor");
        if (Build.VERSION.SDK_INT >= 24) {
            return new NetworkStateTracker24(context, taskExecutor);
        }
        return new NetworkStateTrackerPre24(context, taskExecutor);
    }

    static {
        String strTagWithPrefix = Logger.tagWithPrefix("NetworkStateTracker");
        Intrinsics.checkNotNullExpressionValue(strTagWithPrefix, "tagWithPrefix(...)");
        TAG = strTagWithPrefix;
    }

    public static final boolean isActiveNetworkValidated(ConnectivityManager connectivityManager) {
        Intrinsics.checkNotNullParameter(connectivityManager, "<this>");
        try {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(16);
            }
            return false;
        } catch (SecurityException e) {
            Logger.get().error(TAG, "Unable to validate active network", e);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x001b  */
    /* JADX WARN: Code duplicated, block: B:17:0x002e  */
    public static final NetworkState getActiveNetworkState(ConnectivityManager connectivityManager, boolean z) {
        boolean z2;
        SecurityException securityException;
        boolean z3;
        Intrinsics.checkNotNullParameter(connectivityManager, "connectivityManager");
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean z4 = false;
            if (activeNetworkInfo != null) {
                try {
                    if (activeNetworkInfo.isConnected()) {
                        z4 = true;
                        z3 = true;
                    } else {
                        z3 = true;
                    }
                    boolean zIsActiveNetworkValidated = isActiveNetworkValidated(connectivityManager);
                    boolean zIsActiveNetworkMetered = ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager);
                    if (activeNetworkInfo != null || activeNetworkInfo.isRoaming()) {
                        z3 = false;
                    }
                    z2 = z;
                    try {
                        return new NetworkState(z4, zIsActiveNetworkValidated, zIsActiveNetworkMetered, z3, z2);
                    } catch (SecurityException e) {
                        e = e;
                        securityException = e;
                        Logger.get().error(TAG, "Unable to get active network state", securityException);
                        return new NetworkState(false, false, false, true, z2);
                    }
                } catch (SecurityException e2) {
                    securityException = e2;
                    z2 = z;
                }
            } else {
                z3 = true;
                boolean zIsActiveNetworkValidated2 = isActiveNetworkValidated(connectivityManager);
                boolean zIsActiveNetworkMetered2 = ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager);
                if (activeNetworkInfo != null) {
                    z3 = false;
                } else {
                    z3 = false;
                }
                z2 = z;
                return new NetworkState(z4, zIsActiveNetworkValidated2, zIsActiveNetworkMetered2, z3, z2);
            }
        } catch (SecurityException e3) {
            e = e3;
            z2 = z;
        }
        Logger.get().error(TAG, "Unable to get active network state", securityException);
        return new NetworkState(false, false, false, true, z2);
    }

    public static final NetworkState getActiveNetworkState(NetworkCapabilities capabilities, boolean z) {
        Intrinsics.checkNotNullParameter(capabilities, "capabilities");
        return new NetworkState(capabilities.hasCapability(12), capabilities.hasCapability(16), !capabilities.hasCapability(11), capabilities.hasCapability(18), z);
    }
}
