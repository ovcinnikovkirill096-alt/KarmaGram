package com.radolyn.ayugram;

import com.radolyn.ayugram.controllers.AyuGhostController;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;

public class AyuWorker {
    private static final long LAST_SEEN_FETCH_DELAY_MS = 100;
    private static ScheduledFuture<?> scheduledTask;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<Integer, AtomicBoolean> needOffline = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < 16; i++) {
            needOffline.put(Integer.valueOf(i), new AtomicBoolean(false));
        }
    }

    public static synchronized void run() {
        try {
            ScheduledFuture<?> scheduledFuture = scheduledTask;
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledTask.cancel(false);
            }
            scheduledTask = scheduler.scheduleWithFixedDelay(new Runnable() { // from class: com.radolyn.ayugram.AyuWorker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AyuWorker.runOnce();
                }
            }, 1500L, 3000L, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runOnce() {
        for (final int i = 0; i < 16; i++) {
            if (UserConfig.getInstance(i).isClientActivated() && shouldSendOffline(i)) {
                sendOffline(i, new Runnable() { // from class: com.radolyn.ayugram.AyuWorker$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AyuWorker.notifyLastSeenPillFetch(i);
                    }
                });
            }
        }
    }

    private static boolean shouldSendOffline(int i) {
        AtomicBoolean atomicBoolean;
        return AyuGhostController.getInstance(i).isSendOfflinePacketAfterOnline() && (atomicBoolean = needOffline.get(Integer.valueOf(i))) != null && atomicBoolean.getAndSet(false);
    }

    private static void sendOffline(int i, final Runnable runnable) {
        TL_account.updateStatus updatestatus = new TL_account.updateStatus();
        updatestatus.offline = true;
        ConnectionsManager.getInstance(i).sendRequest(updatestatus, new RequestDelegate() { // from class: com.radolyn.ayugram.AyuWorker$$ExternalSyntheticLambda1
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                AyuWorker.$r8$lambda$ckDDaQDF5ZjKbomkUbzXF31v2sk(runnable, tLObject, tL_error);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$ckDDaQDF5ZjKbomkUbzXF31v2sk(Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private static void notifyLastSeenPillUpdate(int i) {
        NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(AyuConstants.LAST_SEEN_PILL_UPDATE, Integer.valueOf(i), Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyLastSeenPillFetch(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuWorker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(AyuConstants.LAST_SEEN_PILL_FETCH, Integer.valueOf(i));
            }
        }, LAST_SEEN_FETCH_DELAY_MS);
    }

    public static void requestLastSeenUpdate(final int i) {
        if (!AyuGhostController.getInstance(i).isSendOfflinePacketAfterOnline()) {
            notifyLastSeenPillFetch(i);
        } else if (shouldSendOffline(i)) {
            sendOffline(i, new Runnable() { // from class: com.radolyn.ayugram.AyuWorker$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AyuWorker.notifyLastSeenPillFetch(i);
                }
            });
        } else {
            notifyLastSeenPillFetch(i);
        }
    }

    public static synchronized void setOnline(int i, boolean z) {
        try {
            AtomicBoolean atomicBoolean = needOffline.get(Integer.valueOf(i));
            if (atomicBoolean != null) {
                atomicBoolean.set(z);
            }
            if (AyuGhostController.getInstance(i).isSendOfflinePacketAfterOnline()) {
                run();
            }
            notifyLastSeenPillUpdate(i);
        } catch (Throwable th) {
            throw th;
        }
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}
