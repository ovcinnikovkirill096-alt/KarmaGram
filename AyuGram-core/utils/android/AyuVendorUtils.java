package com.radolyn.ayugram.utils.android;

import android.os.Build;
import org.telegram.messenger.XiaomiUtilities;

public class AyuVendorUtils {
    public static boolean isMIUI() {
        return XiaomiUtilities.isMIUI();
    }

    public static boolean isHyperOS() {
        return XiaomiUtilities.isHyperOS();
    }

    public static boolean isXiaomi() {
        String lowerCase = Build.MANUFACTURER.toLowerCase();
        return lowerCase.startsWith("xiaomi") || lowerCase.startsWith("redmi") || lowerCase.startsWith("poco");
    }
}
