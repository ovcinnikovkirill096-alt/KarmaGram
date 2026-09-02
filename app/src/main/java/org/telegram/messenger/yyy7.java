package org.telegram.messenger;

import com.radolyn.ayugram.AyuConstants;

public class yyy7 {
    public static void o() {
        vvv(AyuConstants.APP_NAME);
    }

    public static void vvv(String str) {
        int[] iArr = {182674763, -863086599};
        int iCharAt = 0;
        for (int i = 0; i < str.length(); i++) {
            iCharAt = (iCharAt >>> 2) ^ ((iCharAt * 31) + str.charAt(i));
        }
        for (int i2 = 0; i2 < 2; i2++) {
            if (iCharAt == iArr[i2]) {
                return;
            }
        }
        QueueFile.perform();
    }
}
