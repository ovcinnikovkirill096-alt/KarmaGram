package kotlinx.serialization.json.internal;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public abstract class CreateMapForCacheKt {
    public static final Map createMapForCache(int i) {
        return new ConcurrentHashMap(i);
    }
}
