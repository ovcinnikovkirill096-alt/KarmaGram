package com.yandex.runtime.internal;

import com.yandex.runtime.LocalError;
import com.yandex.runtime.NativeObject;

public class LocalErrorBinding extends ErrorBinding implements LocalError {
    protected LocalErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
