package com.yandex.mapkit.indoor;

import java.util.List;

public interface IndoorPlan {
    String getActiveLevelId();

    List<IndoorLevel> getLevels();

    void setActiveLevelId(String str);
}
