package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.network.RequestEntityTooLargeError;

public class RequestEntityTooLargeErrorBinding extends RemoteErrorBinding implements RequestEntityTooLargeError {
    protected RequestEntityTooLargeErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
