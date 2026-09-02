package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import okhttp3.internal.url._UrlKt;

class InstallerPackageNameProvider {
    private String installerPackageName;

    InstallerPackageNameProvider() {
    }

    synchronized String getInstallerPackageName(Context context) {
        try {
            if (this.installerPackageName == null) {
                this.installerPackageName = loadInstallerPackageName(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return _UrlKt.FRAGMENT_ENCODE_SET.equals(this.installerPackageName) ? null : this.installerPackageName;
    }

    private static String loadInstallerPackageName(Context context) {
        String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installerPackageName == null ? _UrlKt.FRAGMENT_ENCODE_SET : installerPackageName;
    }
}
