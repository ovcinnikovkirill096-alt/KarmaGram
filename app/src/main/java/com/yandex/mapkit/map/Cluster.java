package com.yandex.mapkit.map;

import java.util.List;

public interface Cluster {
    void addClusterTapListener(ClusterTapListener clusterTapListener);

    PlacemarkMapObject getAppearance();

    List<PlacemarkMapObject> getPlacemarks();

    int getSize();

    boolean isValid();

    void removeClusterTapListener(ClusterTapListener clusterTapListener);
}
