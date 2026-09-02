package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.network.ForbiddenError;

public class ForbiddenErrorBinding extends RemoteErrorBinding implements ForbiddenError {
    protected ForbiddenErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
