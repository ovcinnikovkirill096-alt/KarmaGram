package com.yandex.mapkit.map;

import com.yandex.mapkit.images.ImageUrlProvider;

public interface BaseDataSourceBuilder {
    boolean isValid();

    void setImageUrlProvider(ImageUrlProvider imageUrlProvider);
}
