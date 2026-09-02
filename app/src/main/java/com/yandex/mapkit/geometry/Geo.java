package com.yandex.mapkit.geometry;

public class Geo {
    public static native Point closestPoint(Point point, Segment segment);

    public static native double course(Point point, Point point2);

    public static native double distance(Point point, Point point2);

    public static native Point pointOnSegmentByFactor(Segment segment, double d);
}
