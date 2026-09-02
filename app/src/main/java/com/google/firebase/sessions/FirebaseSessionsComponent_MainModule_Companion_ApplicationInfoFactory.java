package com.google.firebase.sessions;

import com.google.firebase.FirebaseApp;
import com.google.firebase.sessions.dagger.internal.Factory;
import com.google.firebase.sessions.dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FirebaseSessionsComponent_MainModule_Companion_ApplicationInfoFactory implements Factory {
    private final Provider firebaseAppProvider;

    public FirebaseSessionsComponent_MainModule_Companion_ApplicationInfoFactory(Provider provider) {
        this.firebaseAppProvider = provider;
    }

    @Override // javax.inject.Provider
    public ApplicationInfo get() {
        return applicationInfo((FirebaseApp) this.firebaseAppProvider.get());
    }

    public static FirebaseSessionsComponent_MainModule_Companion_ApplicationInfoFactory create(Provider provider) {
        return new FirebaseSessionsComponent_MainModule_Companion_ApplicationInfoFactory(provider);
    }

    public static ApplicationInfo applicationInfo(FirebaseApp firebaseApp) {
        return (ApplicationInfo) Preconditions.checkNotNullFromProvides(FirebaseSessionsComponent.MainModule.Companion.applicationInfo(firebaseApp));
    }
}
