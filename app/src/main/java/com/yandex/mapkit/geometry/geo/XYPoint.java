package com.yandex.mapkit.geometry.geo;

import com.yandex.runtime.bindings.Archive;
import com.yandex.runtime.bindings.Serializable;

public class XYPoint implements Serializable {
    private double x;
    private double y;

    public XYPoint(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public XYPoint() {
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override // com.yandex.runtime.bindings.Serializable
    public void serialize(Archive archive) {
        this.x = archive.add(this.x);
        this.y = archive.add(this.y);
    }
}
