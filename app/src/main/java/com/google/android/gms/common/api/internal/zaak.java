package com.google.android.gms.common.api.internal;

final class zaak implements Runnable {
    final /* synthetic */ zaaw zaa;

    zaak(zaaw zaawVar) {
        this.zaa = zaawVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zaaw zaawVar = this.zaa;
        zaawVar.zad.cancelAvailabilityErrorNotifications(zaawVar.zac);
    }
}
