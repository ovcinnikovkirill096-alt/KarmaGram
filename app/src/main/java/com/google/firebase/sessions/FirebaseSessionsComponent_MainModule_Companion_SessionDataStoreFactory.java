package com.google.firebase.sessions;

import android.content.Context;
import androidx.datastore.core.DataStore;
import com.google.firebase.sessions.dagger.internal.Factory;
import com.google.firebase.sessions.dagger.internal.Preconditions;
import javax.inject.Provider;
import kotlin.coroutines.CoroutineContext;

public final class FirebaseSessionsComponent_MainModule_Companion_SessionDataStoreFactory implements Factory {
    private final Provider appContextProvider;
    private final Provider blockingDispatcherProvider;
    private final Provider sessionDataSerializerProvider;

    public FirebaseSessionsComponent_MainModule_Companion_SessionDataStoreFactory(Provider provider, Provider provider2, Provider provider3) {
        this.appContextProvider = provider;
        this.blockingDispatcherProvider = provider2;
        this.sessionDataSerializerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DataStore get() {
        return sessionDataStore((Context) this.appContextProvider.get(), (CoroutineContext) this.blockingDispatcherProvider.get(), (SessionDataSerializer) this.sessionDataSerializerProvider.get());
    }

    public static FirebaseSessionsComponent_MainModule_Companion_SessionDataStoreFactory create(Provider provider, Provider provider2, Provider provider3) {
        return new FirebaseSessionsComponent_MainModule_Companion_SessionDataStoreFactory(provider, provider2, provider3);
    }

    public static DataStore sessionDataStore(Context context, CoroutineContext coroutineContext, SessionDataSerializer sessionDataSerializer) {
        return (DataStore) Preconditions.checkNotNullFromProvides(FirebaseSessionsComponent.MainModule.Companion.sessionDataStore(context, coroutineContext, sessionDataSerializer));
    }
}
