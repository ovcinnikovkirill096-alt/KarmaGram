package com.google.android.gms.common.moduleinstall;

import android.content.Context;
import com.google.android.gms.common.moduleinstall.internal.zay;

public abstract class ModuleInstall {
    public static ModuleInstallClient getClient(Context context) {
        return new zay(context);
    }
}
