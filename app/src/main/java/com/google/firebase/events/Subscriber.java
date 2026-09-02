package com.google.firebase.events;

import java.util.concurrent.Executor;

public interface Subscriber {
    void subscribe(Class cls, EventHandler eventHandler);

    void subscribe(Class cls, Executor executor, EventHandler eventHandler);
}
