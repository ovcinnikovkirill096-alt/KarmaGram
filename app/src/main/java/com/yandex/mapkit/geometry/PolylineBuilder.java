package com.yandex.mapkit.geometry;

public interface PolylineBuilder {
    void append(Point point);

    void append(Polyline polyline);

    Polyline build();
}
