package com.yandex.mapkit.map;

import com.yandex.mapkit.ConflictResolutionMode;

public interface RootMapObjectCollection extends MapObjectCollection {
    ConflictResolutionMode getConflictResolutionMode();

    void setConflictResolutionMode(ConflictResolutionMode conflictResolutionMode);
}
