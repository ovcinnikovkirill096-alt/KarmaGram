package com.google.android.play.integrity.internal;

public final class aj implements ai {
    private static final aj a = new aj(null);
    private final Object b;

    private aj(Object obj) {
        this.b = obj;
    }

    public static ai b(Object obj) {
        if (obj != null) {
            return new aj(obj);
        }
        throw new NullPointerException("instance cannot be null");
    }

    @Override // com.google.android.play.integrity.internal.al
    public final Object a() {
        return this.b;
    }
}
