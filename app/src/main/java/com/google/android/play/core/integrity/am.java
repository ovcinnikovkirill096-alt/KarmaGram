package com.google.android.play.core.integrity;

public final class am implements com.google.android.play.integrity.internal.ai {
    private final com.google.android.play.integrity.internal.al a;
    private final com.google.android.play.integrity.internal.al b;

    public am(com.google.android.play.integrity.internal.al alVar, com.google.android.play.integrity.internal.al alVar2) {
        this.a = alVar;
        this.b = alVar2;
    }

    @Override // com.google.android.play.integrity.internal.al
    public final /* bridge */ /* synthetic */ Object a() {
        return new al((ax) this.a.a(), (bd) this.b.a());
    }
}
