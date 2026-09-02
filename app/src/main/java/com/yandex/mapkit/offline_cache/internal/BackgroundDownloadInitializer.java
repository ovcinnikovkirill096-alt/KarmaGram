package com.yandex.mapkit.offline_cache.internal;

public interface BackgroundDownloadInitializer {
    void initializeMapkit();

    void setListener(BackgroundWorkerListener backgroundWorkerListener);
}
