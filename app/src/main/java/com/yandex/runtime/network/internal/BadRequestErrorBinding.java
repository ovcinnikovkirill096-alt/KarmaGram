package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.network.BadRequestError;

public class BadRequestErrorBinding extends RemoteErrorBinding implements BadRequestError {
    protected BadRequestErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
