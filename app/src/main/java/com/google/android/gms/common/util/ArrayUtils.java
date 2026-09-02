package com.google.android.gms.common.util;

import com.google.android.gms.common.internal.Objects;
import java.util.ArrayList;

public abstract class ArrayUtils {
    public static boolean contains(int[] iArr, int i) {
        if (iArr != null) {
            for (int i2 : iArr) {
                if (i2 == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList newArrayList() {
        return new ArrayList();
    }

    public static boolean contains(Object[] objArr, Object obj) {
        int length = objArr != null ? objArr.length : 0;
        for (int i = 0; i < length; i++) {
            if (Objects.equal(objArr[i], obj)) {
                if (i >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
