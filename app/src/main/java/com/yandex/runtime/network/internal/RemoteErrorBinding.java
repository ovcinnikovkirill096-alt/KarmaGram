package com.yandex.runtime.network.internal;

import com.yandex.runtime.NativeObject;
import com.yandex.runtime.internal.ErrorBinding;
import com.yandex.runtime.network.RemoteError;

public class RemoteErrorBinding extends ErrorBinding implements RemoteError {
    protected RemoteErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
