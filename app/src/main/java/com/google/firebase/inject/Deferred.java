package com.google.firebase.inject;

public interface Deferred {

    public interface DeferredHandler {
        void handle(Provider provider);
    }

    void whenAvailable(DeferredHandler deferredHandler);
}
