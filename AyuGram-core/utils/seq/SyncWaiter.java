package com.radolyn.ayugram.utils.seq;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;

public abstract class SyncWaiter implements NotificationCenter.NotificationCenterDelegate {
    protected final int currentAccount;
    protected final ArrayList notifications = new ArrayList();
    protected final CountDownLatch latch = new CountDownLatch(1);

    public SyncWaiter(int i) {
        this.currentAccount = i;
    }

    public void subscribe() {
        if (this.notifications.isEmpty()) {
            throw new IllegalStateException("NO NOTIFICATIONS SPECIFIED");
        }
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.seq.SyncWaiter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$subscribe$0(countDownLatch);
                }
            });
            try {
                countDownLatch.await();
                return;
            } catch (InterruptedException unused) {
                return;
            }
        }
        ArrayList arrayList = this.notifications;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, ((Integer) obj).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$subscribe$0(CountDownLatch countDownLatch) {
        ArrayList arrayList = this.notifications;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, ((Integer) obj).intValue());
        }
        countDownLatch.countDown();
    }

    protected void unsubscribe() {
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.seq.SyncWaiter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$unsubscribe$1(countDownLatch);
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException unused) {
            }
        } else {
            ArrayList arrayList = this.notifications;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, ((Integer) obj).intValue());
            }
        }
        this.latch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unsubscribe$1(CountDownLatch countDownLatch) {
        ArrayList arrayList = this.notifications;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, ((Integer) obj).intValue());
        }
        countDownLatch.countDown();
    }

    public void await() {
        try {
            this.latch.await(300L, TimeUnit.SECONDS);
        } catch (InterruptedException unused) {
        }
    }
}
