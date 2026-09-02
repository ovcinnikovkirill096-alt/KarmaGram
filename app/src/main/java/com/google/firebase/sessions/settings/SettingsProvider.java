package com.google.firebase.sessions.settings;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.time.Duration;

public interface SettingsProvider {
    Double getSamplingRate();

    Boolean getSessionEnabled();

    /* JADX INFO: renamed from: getSessionRestartTimeout-FghU774 */
    Duration mo2211getSessionRestartTimeoutFghU774();

    Object updateSettings(Continuation continuation);

    public static final class DefaultImpls {
        public static Object updateSettings(SettingsProvider settingsProvider, Continuation continuation) {
            return Unit.INSTANCE;
        }
    }
}
