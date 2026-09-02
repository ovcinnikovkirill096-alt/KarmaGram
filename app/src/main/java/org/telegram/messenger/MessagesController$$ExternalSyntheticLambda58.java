package org.telegram.messenger;

public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda58 implements Runnable {
    public final /* synthetic */ MessagesController f$0;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda58(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.removePromoDialog();
    }
}
