package com.android.dex;

import okhttp3.internal.url._UrlKt;

public abstract class DexFormat {
    public static int magicToApi(byte[] bArr) {
        if (bArr.length == 8 && bArr[0] == 100 && bArr[1] == 101 && bArr[2] == 120 && bArr[3] == 10 && bArr[7] == 0) {
            String str = _UrlKt.FRAGMENT_ENCODE_SET + ((char) bArr[4]) + ((char) bArr[5]) + ((char) bArr[6]);
            if (str.equals("035")) {
                return 13;
            }
            if (str.equals("037")) {
                return 24;
            }
            if (str.equals("038")) {
                return 26;
            }
            if (str.equals("039") || str.equals("039")) {
                return 28;
            }
        }
        return -1;
    }

    public static String apiToMagic(int i) {
        String str = "039";
        if (i < 28 && i < 28) {
            if (i >= 26) {
                str = "038";
            } else {
                str = i >= 24 ? "037" : "035";
            }
        }
        return "dex\n" + str + "\u0000";
    }

    public static boolean isSupportedDexMagic(byte[] bArr) {
        return magicToApi(bArr) > 0;
    }
}
