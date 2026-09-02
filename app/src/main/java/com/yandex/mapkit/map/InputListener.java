package com.yandex.mapkit.map;

import com.yandex.mapkit.geometry.Point;

public interface InputListener {
    void onMapLongTap(Map map, Point point);

    void onMapTap(Map map, Point point);
}
