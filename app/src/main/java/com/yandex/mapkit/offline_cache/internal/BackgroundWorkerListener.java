package com.yandex.mapkit.offline_cache.internal;

import androidx.work.ForegroundInfo;

public interface BackgroundWorkerListener {
    void updateForegroundInfo(ForegroundInfo foregroundInfo);
}
