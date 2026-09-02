package com.yandex.mapkit.layers;

import com.yandex.mapkit.GeoObject;

public interface GeoObjectTapEvent {
    GeoObject getGeoObject();

    boolean isValid();
}
