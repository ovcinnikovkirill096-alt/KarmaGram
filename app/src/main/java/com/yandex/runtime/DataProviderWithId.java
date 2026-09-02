package com.yandex.runtime;

public interface DataProviderWithId {
    byte[] load();

    String providerId();
}
