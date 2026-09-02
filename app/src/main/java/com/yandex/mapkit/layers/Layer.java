package com.yandex.mapkit.layers;

public interface Layer {
    DataSourceLayer dataSourceLayer();

    boolean isValid();

    void remove();
}
