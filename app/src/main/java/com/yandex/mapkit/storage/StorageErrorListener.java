package com.yandex.mapkit.storage;

import com.yandex.runtime.LocalError;

public interface StorageErrorListener {
    void onStorageError(LocalError localError);
}
