package com.google.firebase.sessions;

import android.content.Context;
import androidx.datastore.core.DataStore;
import com.google.firebase.sessions.dagger.internal.Factory;
import com.google.firebase.sessions.dagger.internal.Preconditions;
import javax.inject.Provider;
import kotlin.coroutines.CoroutineContext;

public final class FirebaseSessionsComponent_MainModule_Companion_SessionConfigsDataStoreFactory implements Factory {
    private final Provider appContextProvider;
    private final Provider blockingDispatcherProvider;

    public FirebaseSessionsComponent_MainModule_Companion_SessionConfigsDataStoreFactory(Provider provider, Provider provider2) {
        this.appContextProvider = provider;
        this.blockingDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    public DataStore get() {
        return sessionConfigsDataStore((Context) this.appContextProvider.get(), (CoroutineContext) this.blockingDispatcherProvider.get());
    }

    public static FirebaseSessionsComponent_MainModule_Companion_SessionConfigsDataStoreFactory create(Provider provider, Provider provider2) {
        return new FirebaseSessionsComponent_MainModule_Companion_SessionConfigsDataStoreFactory(provider, provider2);
    }

    public static DataStore sessionConfigsDataStore(Context context, CoroutineContext coroutineContext) {
        return (DataStore) Preconditions.checkNotNullFromProvides(FirebaseSessionsComponent.MainModule.Companion.sessionConfigsDataStore(context, coroutineContext));
    }
}
