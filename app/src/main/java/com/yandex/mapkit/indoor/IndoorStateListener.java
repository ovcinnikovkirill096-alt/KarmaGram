package com.yandex.mapkit.indoor;

public interface IndoorStateListener {
    void onActiveLevelChanged(String str);

    void onActivePlanFocused(IndoorPlan indoorPlan);

    void onActivePlanLeft();
}
