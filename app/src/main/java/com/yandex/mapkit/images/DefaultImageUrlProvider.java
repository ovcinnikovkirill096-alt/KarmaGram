package com.yandex.mapkit.images;

import com.yandex.runtime.NativeObject;

public class DefaultImageUrlProvider implements ImageUrlProvider {
    private final NativeObject nativeObject = createNative();

    private static native NativeObject createNative();

    @Override // com.yandex.mapkit.images.ImageUrlProvider
    public native String formatUrl(ImageDataDescriptor imageDataDescriptor);

    public native void setUrlBase(String str);
}
