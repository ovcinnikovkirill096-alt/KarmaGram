package com.radolyn.ayugram.controllers;

import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.messages.AttachmentsCacheManager;
import com.radolyn.ayugram.controllers.messages.DeletedDialogService;
import com.radolyn.ayugram.controllers.messages.DeletedMessageService;
import com.radolyn.ayugram.controllers.messages.EditedMessageService;
import com.radolyn.ayugram.controllers.messages.MessageDeleteWrapper;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.utils.ThreadSafeLongSparseArray;
import j$.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public class AyuMessagesController extends BaseController {
    private static volatile AyuMessagesController[] Instance = new AyuMessagesController[16];
    private final DeletedDialogService deletedDialogService;
    private final DeletedMessageService deletedMessageService;
    private final EditedMessageService editedMessageService;
    private final ExecutorService executor;
    private final ThreadSafeLongSparseArray lastMessages;

    public void onMediaDownloaded(SaveMessageRequest saveMessageRequest) {
    }

    public AyuMessagesController(final int i) {
        super(i);
        this.executor = Executors.newFixedThreadPool(3, new ThreadFactory() { // from class: com.radolyn.ayugram.controllers.AyuMessagesController$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return AyuMessagesController.$r8$lambda$tSgyiFiq_DtF_Dz2KNWw_2fP438(i, runnable);
            }
        });
        this.lastMessages = new ThreadSafeLongSparseArray();
        this.deletedMessageService = new DeletedMessageService(this);
        this.editedMessageService = new EditedMessageService(this);
        final DeletedDialogService deletedDialogService = new DeletedDialogService(this);
        this.deletedDialogService = deletedDialogService;
        Objects.requireNonNull(deletedDialogService);
        executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.AyuMessagesController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                deletedDialogService.loadLastMessages();
            }
        }, "loadLastMessages");
    }

    public static /* synthetic */ Thread $r8$lambda$tSgyiFiq_DtF_Dz2KNWw_2fP438(int i, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("ctrl_" + i);
        return thread;
    }

    public static AyuMessagesController getInstance(int i) {
        AyuMessagesController ayuMessagesController;
        AyuMessagesController ayuMessagesController2 = Instance[i];
        if (ayuMessagesController2 != null) {
            return ayuMessagesController2;
        }
        synchronized (AyuMessagesController.class) {
            try {
                ayuMessagesController = Instance[i];
                if (ayuMessagesController == null) {
                    AyuMessagesController[] ayuMessagesControllerArr = Instance;
                    AyuMessagesController ayuMessagesController3 = new AyuMessagesController(i);
                    ayuMessagesControllerArr[i] = ayuMessagesController3;
                    ayuMessagesController = ayuMessagesController3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuMessagesController;
    }

    public static void clear() {
        AyuData.clearMessageDatabase();
        for (int i = 0; i < UserConfig.getActivatedAccountsCount(); i++) {
            getInstance(i).executor.shutdown();
        }
        Instance = new AyuMessagesController[16];
    }

    public static void clearAttachments() {
        AttachmentsCacheManager.clearAll();
    }

    public static void onAttachmentsCleanUp() {
        getInstance(UserConfig.selectedAccount).executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.AyuMessagesController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AttachmentsCacheManager.cleanUp();
            }
        }, "attachmentsCleanUp");
    }

    public void onMessageEdited(SaveMessageRequest saveMessageRequest, TLRPC.Message message) {
        this.editedMessageService.onMessageEdited(saveMessageRequest, message);
    }

    public void onMessageDeleted(SaveMessageRequest saveMessageRequest) {
        this.deletedMessageService.onMessageDeleted(saveMessageRequest);
    }

    public void onHistoryFlushed(TLRPC.Dialog dialog, Runnable runnable) {
        this.deletedMessageService.onHistoryFlushed(dialog, runnable);
    }

    public void onDialogDeleted(long j) {
        this.deletedDialogService.onDialogDeleted(j);
    }

    public void updateDeletedDialogsFolder(ArrayList arrayList, int i) {
        this.deletedDialogService.updateDeletedDialogsFolder(arrayList, i);
    }

    public void deleteExistingDialogs(ArrayList arrayList) {
        this.deletedDialogService.deleteExistingDialogs(arrayList);
    }

    public boolean hasAnyRevisions(long j, int i) {
        return this.editedMessageService.hasAnyRevisions(j, i);
    }

    public List getRevisions(long j, int i, int i2, int i3) {
        return this.editedMessageService.getRevisions(j, i, i2, i3);
    }

    public DeletedMessageFull getMessage(long j, int i) {
        return this.deletedMessageService.getMessage(j, i);
    }

    public List getMessages(long j, long j2, int i, int i2) {
        return this.deletedMessageService.getMessages(j, j2, i, i2);
    }

    public List getMessagesForScroll(long j, long j2, String str, int i, int i2) {
        return this.deletedMessageService.getMessagesForScroll(j, j2, str, i, i2);
    }

    public int getDeletedCount(long j, long j2, String str) {
        return this.deletedMessageService.getDeletedCount(j, j2, str);
    }

    public void clearDeletedFromDialog(long j, long j2, Long l) {
        this.deletedMessageService.clearDeletedFromDialog(j, j2, l);
    }

    public void deleteDialog(long j) {
        this.deletedDialogService.deleteDialog(j);
    }

    public MessageObject getLastMessageCached(long j) {
        return (MessageObject) this.lastMessages.get(j);
    }

    public int getLastMessagesCount() {
        return this.lastMessages.size();
    }

    public MessageDeleteWrapper wrapDelete() {
        return new MessageDeleteWrapper(this, this.deletedMessageService);
    }

    public ExecutorService getExecutor() {
        return this.executor;
    }

    public void executeAsync(final Runnable runnable, final String str) {
        if (this.executor.isShutdown() || this.executor.isTerminated()) {
            FileLog.w("executor is shut down, cannot execute task: " + str);
            return;
        }
        this.executor.execute(new Runnable() { // from class: com.radolyn.ayugram.controllers.AyuMessagesController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AyuMessagesController.$r8$lambda$HRm6JSScbje8CrjkWP7vMMFiPQQ(runnable, str);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$HRm6JSScbje8CrjkWP7vMMFiPQQ(Runnable runnable, String str) {
        try {
            runnable.run();
        } catch (Throwable th) {
            AyuUtils.logError(str, th);
        }
    }

    public long getUserId() {
        return getUserConfig().getClientUserId();
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public ThreadSafeLongSparseArray getLastMessages() {
        return this.lastMessages;
    }

    public void updateLastMessage(long j, TLRPC.Message message) {
        this.deletedDialogService.updateLastMessage(j, message);
    }

    public AyuMapper getAyuMapperInternal() {
        return getAyuMapper();
    }

    public MessagesController getMessagesControllerInternal() {
        return getMessagesController();
    }

    public MessagesStorage getMessagesStorageInternal() {
        return getMessagesStorage();
    }

    public NotificationCenter getNotificationCenterInternal() {
        return getNotificationCenter();
    }

    public AyuSpyController getAyuSpyControllerInternal() {
        return getAyuSpyController();
    }

    public DeletedDialogService getDeletedDialogServiceInternal() {
        return this.deletedDialogService;
    }
}
