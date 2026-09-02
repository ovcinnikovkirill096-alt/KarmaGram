package com.google.android.gms.common.util;

import j$.util.DesugarCollections;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CollectionUtils {
    public static List listOf(Object obj) {
        return Collections.singletonList(obj);
    }

    public static List listOf(Object... objArr) {
        int length = objArr.length;
        if (length == 0) {
            return Collections.EMPTY_LIST;
        }
        if (length == 1) {
            return Collections.singletonList(objArr[0]);
        }
        return DesugarCollections.unmodifiableList(Arrays.asList(objArr));
    }
}
