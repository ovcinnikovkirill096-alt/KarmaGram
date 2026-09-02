package com.yandex.mapkit.location.internal;

import com.yandex.mapkit.location.LocationUnavailableError;
import com.yandex.runtime.NativeObject;
import com.yandex.runtime.internal.ErrorBinding;

public class LocationUnavailableErrorBinding extends ErrorBinding implements LocationUnavailableError {
    protected LocationUnavailableErrorBinding(NativeObject nativeObject) {
        super(nativeObject);
    }
}
