package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.network.UnauthorizedError;

public class UnauthorizedErrorBinding extends RemoteErrorBinding implements UnauthorizedError {
    protected UnauthorizedErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
