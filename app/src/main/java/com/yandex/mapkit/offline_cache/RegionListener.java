package com.yandex.mapkit.offline_cache;

public interface RegionListener {
    void onRegionProgress(int i);

    void onRegionStateChanged(int i);
}
