package com.google.firebase.remoteconfig;

import java.util.Set;

public abstract class ConfigUpdate {
    public abstract Set getUpdatedKeys();

    public static ConfigUpdate create(Set set) {
        return new AutoValue_ConfigUpdate(set);
    }
}
