package com.google.firebase.sessions.settings;

import kotlin.coroutines.Continuation;

public interface SettingsCache {
    boolean hasCacheExpired();

    Integer sessionRestartTimeout();

    Double sessionSamplingRate();

    Boolean sessionsEnabled();

    Object updateConfigs(SessionConfigs sessionConfigs, Continuation continuation);
}
