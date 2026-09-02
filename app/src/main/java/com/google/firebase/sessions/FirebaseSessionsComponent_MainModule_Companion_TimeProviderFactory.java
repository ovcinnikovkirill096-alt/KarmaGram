package com.google.firebase.sessions;

import com.google.firebase.sessions.dagger.internal.Factory;
import com.google.firebase.sessions.dagger.internal.Preconditions;

public final class FirebaseSessionsComponent_MainModule_Companion_TimeProviderFactory implements Factory {
    @Override // javax.inject.Provider
    public TimeProvider get() {
        return timeProvider();
    }

    public static FirebaseSessionsComponent_MainModule_Companion_TimeProviderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TimeProvider timeProvider() {
        return (TimeProvider) Preconditions.checkNotNullFromProvides(FirebaseSessionsComponent.MainModule.Companion.timeProvider());
    }

    private static final class InstanceHolder {
        private static final FirebaseSessionsComponent_MainModule_Companion_TimeProviderFactory INSTANCE = new FirebaseSessionsComponent_MainModule_Companion_TimeProviderFactory();
    }
}
