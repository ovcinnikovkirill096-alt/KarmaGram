package com.google.firebase.encoders;

public interface ValueEncoderContext {
    ValueEncoderContext add(String str);

    ValueEncoderContext add(boolean z);
}
