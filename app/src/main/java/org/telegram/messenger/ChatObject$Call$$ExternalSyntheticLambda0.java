package org.telegram.messenger;

public final /* synthetic */ class ChatObject$Call$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatObject.Call f$0;

    public /* synthetic */ ChatObject$Call$$ExternalSyntheticLambda0(ChatObject.Call call) {
        this.f$0 = call;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.checkQueue();
    }
}
