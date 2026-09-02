package com.yandex.runtime.internal;

import com.yandex.runtime.Error;
import com.yandex.runtime.NativeObject;

public class ErrorBinding implements Error {
    private final NativeObject nativeObject;

    @Override // com.yandex.runtime.Error
    public native boolean isValid();

    protected ErrorBinding(NativeObject nativeObject) {
        this.nativeObject = nativeObject;
    }
}
