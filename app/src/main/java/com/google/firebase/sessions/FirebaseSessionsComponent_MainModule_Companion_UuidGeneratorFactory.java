package com.google.firebase.sessions;

import com.google.firebase.sessions.dagger.internal.Factory;
import com.google.firebase.sessions.dagger.internal.Preconditions;

public final class FirebaseSessionsComponent_MainModule_Companion_UuidGeneratorFactory implements Factory {
    @Override // javax.inject.Provider
    public UuidGenerator get() {
        return uuidGenerator();
    }

    public static FirebaseSessionsComponent_MainModule_Companion_UuidGeneratorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UuidGenerator uuidGenerator() {
        return (UuidGenerator) Preconditions.checkNotNullFromProvides(FirebaseSessionsComponent.MainModule.Companion.uuidGenerator());
    }

    private static final class InstanceHolder {
        private static final FirebaseSessionsComponent_MainModule_Companion_UuidGeneratorFactory INSTANCE = new FirebaseSessionsComponent_MainModule_Companion_UuidGeneratorFactory();
    }
}
