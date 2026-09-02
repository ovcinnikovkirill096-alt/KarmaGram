package com.yandex.mapkit.layers.internal;

import com.yandex.mapkit.layers.BaseDataSource;
import com.yandex.runtime.NativeObject;

public class BaseDataSourceBinding implements BaseDataSource {
    private final NativeObject nativeObject;

    @Override // com.yandex.mapkit.layers.BaseDataSource
    public native String getId();

    @Override // com.yandex.mapkit.layers.BaseDataSource
    public native boolean isValid();

    protected BaseDataSourceBinding(NativeObject nativeObject) {
        this.nativeObject = nativeObject;
    }
}
