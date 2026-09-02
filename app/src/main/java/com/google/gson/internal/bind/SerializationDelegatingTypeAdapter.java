package com.google.gson.internal.bind;

import com.google.gson.TypeAdapter;

public abstract class SerializationDelegatingTypeAdapter extends TypeAdapter {
    public abstract TypeAdapter getSerializationDelegate();
}
