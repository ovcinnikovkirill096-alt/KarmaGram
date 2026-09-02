package com.yandex.mapkit.layers;

public interface TileDataSource extends BaseDataSource {
    void invalidate(String str);
}
