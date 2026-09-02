package androidx.datastore.preferences.core;

import j$.util.DesugarCollections;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public abstract class Actual_jvmAndroidKt {
    public static final Map immutableMap(Map map) {
        Intrinsics.checkNotNullParameter(map, "map");
        Map mapUnmodifiableMap = DesugarCollections.unmodifiableMap(map);
        Intrinsics.checkNotNullExpressionValue(mapUnmodifiableMap, "unmodifiableMap(map)");
        return mapUnmodifiableMap;
    }

    public static final Set immutableCopyOfSet(Set set) {
        Intrinsics.checkNotNullParameter(set, "set");
        Set setUnmodifiableSet = DesugarCollections.unmodifiableSet(CollectionsKt.toSet(set));
        Intrinsics.checkNotNullExpressionValue(setUnmodifiableSet, "unmodifiableSet(set.toSet())");
        return setUnmodifiableSet;
    }
}
