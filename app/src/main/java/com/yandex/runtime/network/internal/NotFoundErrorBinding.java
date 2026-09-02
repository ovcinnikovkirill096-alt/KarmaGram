package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.network.NotFoundError;

public class NotFoundErrorBinding extends RemoteErrorBinding implements NotFoundError {
    protected NotFoundErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
