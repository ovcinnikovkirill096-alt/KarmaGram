package com.google.firebase.encoders.config;

import com.google.firebase.encoders.ObjectEncoder;

public interface EncoderConfig {
    EncoderConfig registerEncoder(Class cls, ObjectEncoder objectEncoder);
}
