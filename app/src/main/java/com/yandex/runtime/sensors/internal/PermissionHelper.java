package com.yandex.runtime.sensors.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import com.yandex.runtime.Runtime;

public class PermissionHelper {
    private static final String TAG = "com.yandex.runtime.sensors.internal.PermissionHelper";

    public static boolean checkPermissions(String[] strArr) {
        if (strArr == null) {
            return true;
        }
        try {
            Context applicationContext = Runtime.getApplicationContext();
            PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 4096);
            if (packageInfo.requestedPermissions != null && packageInfo.requestedPermissionsFlags != null) {
                for (String str : strArr) {
                    int i = 0;
                    while (true) {
                        String[] strArr2 = packageInfo.requestedPermissions;
                        if (i >= strArr2.length) {
                            return false;
                        }
                        if ((packageInfo.requestedPermissionsFlags[i] & 2) != 0 && strArr2[i].equals(str)) {
                            break;
                        }
                        i++;
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Exception of calling getPackageInfo", e);
            return false;
        }
    }
}
