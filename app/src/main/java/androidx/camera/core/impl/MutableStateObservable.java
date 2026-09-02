package androidx.camera.core.impl;

public class MutableStateObservable extends StateObservable {
    private MutableStateObservable(Object obj, boolean z) {
        super(obj, z);
    }

    public static MutableStateObservable withInitialState(Object obj) {
        return new MutableStateObservable(obj, false);
    }

    public void setState(Object obj) {
        updateState(obj);
    }
}
