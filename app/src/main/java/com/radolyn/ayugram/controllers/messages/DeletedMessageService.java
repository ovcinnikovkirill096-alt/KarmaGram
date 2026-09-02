package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import com.radolyn.ayugram.utils.AyuLocalDatabaseUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLRPC;

public class DeletedMessageService {
    private final AyuMessagesController controller;

    public DeletedMessageService(AyuMessagesController ayuMessagesController) {
        this.controller = ayuMessagesController;
    }

    public void onMessageDeleted(final SaveMessageRequest saveMessageRequest) {
        if (validateForSave(saveMessageRequest)) {
            this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedMessageService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onMessageDeleted$0(saveMessageRequest);
                }
            }, "saveDeletedMessage");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMessageDeleted$0(SaveMessageRequest saveMessageRequest) {
        if (messageExists(saveMessageRequest)) {
            FileLog.e("onMessageDeleted: message exists");
        } else {
            saveDeletedMessage(saveMessageRequest);
        }
    }

    private boolean validateForSave(SaveMessageRequest saveMessageRequest) {
        try {
            if (saveMessageRequest.getMessage() == null) {
                FileLog.e("onMessageDeleted: message is null");
                return false;
            }
            TLRPC.Message message = saveMessageRequest.getMessage();
            if (message.send_state == 1 && message.id < 0) {
                FileLog.e("onMessageDeleted: message is sending");
                return false;
            }
            if (!AyuConfig.saveDeletedMessageFor(this.controller.getCurrentAccount(), saveMessageRequest.getDialogId())) {
                FileLog.e("onMessageDeleted: saveDeletedMessageFor is false");
                return false;
            }
            if (!(message instanceof TLRPC.TL_messageService) && !(message instanceof TLRPC.TL_messageEmpty)) {
                return true;
            }
            FileLog.e("onMessageDeleted: message is empty");
            return false;
        } catch (Throwable th) {
            AyuUtils.logError("onMessageDeleted", th);
            return false;
        }
    }

    private boolean messageExists(SaveMessageRequest saveMessageRequest) {
        return AyuData.getDeletedMessageDao().exists(this.controller.getUserId(), saveMessageRequest.getDialogId(), saveMessageRequest.getTopicId(), saveMessageRequest.getMessageId());
    }

    void saveDeletedMessage(SaveMessageRequest saveMessageRequest) {
        DeletedMessage deletedMessage = new DeletedMessage();
        deletedMessage.userId = this.controller.getUserId();
        deletedMessage.dialogId = saveMessageRequest.getDialogId();
        deletedMessage.messageId = saveMessageRequest.getMessageId();
        deletedMessage.entityCreateDate = saveMessageRequest.getRequestCatchTime();
        TLRPC.Message message = saveMessageRequest.getMessage();
        this.controller.getAyuMapperInternal().map(saveMessageRequest, deletedMessage);
        try {
            this.controller.getAyuMapperInternal().mapMedia(saveMessageRequest, deletedMessage, true);
        } catch (Throwable th) {
            AyuUtils.logError("saveDeletedMessage.mapMedia", th);
        }
        long jInsert = AyuData.getDeletedMessageDao().insert(deletedMessage);
        this.controller.updateLastMessage(saveMessageRequest.getDialogId(), message);
        TLRPC.TL_messageReactions tL_messageReactions = message.reactions;
        if (tL_messageReactions != null) {
            processDeletedReactions(jInsert, tL_messageReactions);
        }
    }

    private void processDeletedReactions(long j, TLRPC.TL_messageReactions tL_messageReactions) {
        ArrayList arrayList = tL_messageReactions.results;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            TLRPC.ReactionCount reactionCount = (TLRPC.ReactionCount) obj;
            if (!(reactionCount.reaction instanceof TLRPC.TL_reactionEmpty)) {
                DeletedMessageReaction deletedMessageReaction = new DeletedMessageReaction();
                deletedMessageReaction.deletedMessageId = j;
                deletedMessageReaction.count = reactionCount.count;
                deletedMessageReaction.selfSelected = reactionCount.chosen;
                TLRPC.Reaction reaction = reactionCount.reaction;
                if (reaction instanceof TLRPC.TL_reactionEmoji) {
                    deletedMessageReaction.emoticon = ((TLRPC.TL_reactionEmoji) reaction).emoticon;
                } else if (reaction instanceof TLRPC.TL_reactionCustomEmoji) {
                    deletedMessageReaction.documentId = ((TLRPC.TL_reactionCustomEmoji) reaction).document_id;
                    deletedMessageReaction.isCustom = true;
                } else if (reaction instanceof TLRPC.TL_reactionPaid) {
                    deletedMessageReaction.isPaid = true;
                }
                AyuData.getDeletedMessageDao().insertReaction(deletedMessageReaction);
            }
        }
    }

    public void onHistoryFlushed(final TLRPC.Dialog dialog, final Runnable runnable) {
        if (dialog == null) {
            return;
        }
        this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedMessageService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onHistoryFlushed$1(dialog, runnable);
            }
        }, "saveHistory");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHistoryFlushed$1(TLRPC.Dialog dialog, Runnable runnable) {
        saveHistory(dialog);
        runnable.run();
    }

    private void saveHistory(TLRPC.Dialog dialog) {
        Iterator itIterateThroughMessages = AyuLocalDatabaseUtils.iterateThroughMessages(this.controller.getCurrentAccount(), dialog.id);
        while (itIterateThroughMessages.hasNext()) {
            SaveMessageRequest saveMessageRequest = new SaveMessageRequest(this.controller.getCurrentAccount(), (TLRPC.Message) itIterateThroughMessages.next());
            saveMessageRequest.setDialogId(dialog.id);
            try {
                saveDeletedMessage(saveMessageRequest);
            } catch (Throwable th) {
                AyuUtils.logError("saveHistory", th);
            }
        }
    }

    public DeletedMessageFull getMessage(long j, int i) {
        return AyuData.getDeletedMessageDao().getMessage(this.controller.getUserId(), j, i);
    }

    public List getMessages(long j, long j2, int i, int i2) {
        if (j2 == 0) {
            return AyuData.getDeletedMessageDao().getMessagesTopicless(this.controller.getUserId(), j, i, i2);
        }
        return AyuData.getDeletedMessageDao().getMessagesForTopic(this.controller.getUserId(), j, j2, i, i2);
    }

    public List getMessagesForScroll(long j, long j2, String str, int i, int i2) {
        return AyuData.getDeletedMessageDao().getMessagesForScroll(this.controller.getUserId(), j, j2, TextUtils.isEmpty(str) ? _UrlKt.FRAGMENT_ENCODE_SET : str, i, i2);
    }

    public int getDeletedCount(long j, long j2, String str) {
        return AyuData.getDeletedMessageDao().getDeletedCount(this.controller.getUserId(), j, j2, str);
    }

    boolean deleteMessage(long j, int i) {
        DeletedMessageFull message = getMessage(j, i);
        if (message == null) {
            return false;
        }
        AyuData.getDeletedMessageDao().delete(this.controller.getUserId(), j, i);
        if (TextUtils.isEmpty(message.message.mediaPath) || !message.message.mediaPath.contains(AyuConfig.getSavePath())) {
            return true;
        }
        File file = new File(message.message.mediaPath);
        if (!file.exists()) {
            return true;
        }
        try {
            if (file.delete()) {
                return true;
            }
            file.deleteOnExit();
            return true;
        } catch (Throwable unused) {
            file.deleteOnExit();
            return true;
        }
    }

    public void clearDeletedFromDialog(long j, long j2, Long l) {
        AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), j, l);
        this.controller.getLastMessages().remove(j);
        if (j2 == 0 || j2 == j) {
            return;
        }
        AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), j2, l);
        AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), j2, null);
        this.controller.getLastMessages().remove(j2);
    }
}
