package com.yandex.mapkit.layers.internal;

import com.yandex.mapkit.layers.TileDataSource;
import com.yandex.runtime.NativeObject;

public class TileDataSourceBinding extends BaseDataSourceBinding implements TileDataSource {
    @Override // com.yandex.mapkit.layers.TileDataSource
    public native void invalidate(String str);

    protected TileDataSourceBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
