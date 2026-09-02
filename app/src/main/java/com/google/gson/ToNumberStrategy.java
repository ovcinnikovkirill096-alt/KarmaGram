package com.google.gson;

import com.google.gson.stream.JsonReader;

public interface ToNumberStrategy {
    Number readNumber(JsonReader jsonReader);
}
