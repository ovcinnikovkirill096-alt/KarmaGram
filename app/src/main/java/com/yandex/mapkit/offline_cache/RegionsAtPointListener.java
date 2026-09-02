package com.yandex.mapkit.offline_cache;

import com.yandex.runtime.Error;
import java.util.List;

public interface RegionsAtPointListener {
    void onError(Error error);

    void onRegions(List<Integer> list);
}
