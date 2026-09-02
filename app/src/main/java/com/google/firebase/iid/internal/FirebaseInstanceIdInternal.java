package com.google.firebase.iid.internal;

public interface FirebaseInstanceIdInternal {

    public interface NewTokenListener {
    }

    void addNewTokenListener(NewTokenListener newTokenListener);
}
