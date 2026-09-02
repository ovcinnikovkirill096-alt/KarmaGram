package com.yandex.mapkit.map;

import android.graphics.PointF;
import java.util.List;

public interface PlacemarksStyler {
    boolean isValid();

    void setScaleFunction(List<PointF> list);
}
