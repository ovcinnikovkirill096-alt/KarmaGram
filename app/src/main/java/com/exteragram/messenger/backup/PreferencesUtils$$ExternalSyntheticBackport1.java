package com.exteragram.messenger.backup;

import j$.util.DesugarCollections;
import j$.util.Objects;
import java.util.HashSet;
import java.util.Set;

public abstract /* synthetic */ class PreferencesUtils$$ExternalSyntheticBackport1 {
    public static /* synthetic */ Set m(Object[] objArr) {
        HashSet hashSet = new HashSet(objArr.length);
        for (Object obj : objArr) {
            Objects.requireNonNull(obj);
            if (!hashSet.add(obj)) {
                throw new IllegalArgumentException("duplicate element: " + obj);
            }
        }
        return DesugarCollections.unmodifiableSet(hashSet);
    }
}
