package com.google.firebase.crashlytics.internal.network;

import java.util.Map;

public class HttpRequestFactory {
    public HttpGetRequest buildHttpGetRequest(String str, Map map) {
        return new HttpGetRequest(str, map);
    }
}
