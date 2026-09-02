package com.yandex.mapkit.geometry;

public class SubpolylineHelper {
    public static native Polyline subpolyline(Polyline polyline, Subpolyline subpolyline);

    public static native double subpolylineLength(Polyline polyline, Subpolyline subpolyline);
}
