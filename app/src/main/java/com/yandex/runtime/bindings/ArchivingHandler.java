package com.yandex.runtime.bindings;

public interface ArchivingHandler<T> {
    T add(T t, Archive archive);
}
