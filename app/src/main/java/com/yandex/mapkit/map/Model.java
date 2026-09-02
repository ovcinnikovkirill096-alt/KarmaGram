package com.yandex.mapkit.map;

import com.yandex.runtime.DataProviderWithId;

public interface Model {
    ModelStyle getModelStyle();

    boolean isValid();

    void setData(DataProviderWithId dataProviderWithId, Callback callback);

    void setModelStyle(ModelStyle modelStyle);
}
