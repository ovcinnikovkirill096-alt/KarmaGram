package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.EditedMessage;
import java.util.List;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLRPC;

public class EditedMessageService {
    private final AyuMessagesController controller;

    public EditedMessageService(AyuMessagesController ayuMessagesController) {
        this.controller = ayuMessagesController;
    }

    public void onMessageEdited(final SaveMessageRequest saveMessageRequest, TLRPC.Message message) {
        try {
            if (AyuConfig.saveEditedMessageFor(this.controller.getCurrentAccount(), saveMessageRequest.getDialogId()) || saveMessageRequest.isForce()) {
                TLRPC.Message message2 = saveMessageRequest.getMessage();
                final boolean z = !saveMessageRequest.isForce() && isSameMedia(message2, message);
                if (z && TextUtils.equals(message2.message, message.message)) {
                    return;
                }
                if (!saveMessageRequest.isForce() && message.from_id.user_id != 0 && !message.edit_hide) {
                    this.controller.getAyuSpyControllerInternal().saveOnlineActivity(message.from_id.user_id, saveMessageRequest.getRequestCatchTime());
                }
                this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.EditedMessageService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onMessageEdited$0(saveMessageRequest, z);
                    }
                }, "saveEditedMessage");
            }
        } catch (Throwable th) {
            AyuUtils.logError("onMessageEdited", th);
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x003a  */
    /* JADX WARN: Code duplicated, block: B:25:0x003e  */
    /* JADX WARN: Code duplicated, block: B:33:0x0054 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:34:0x0055 A[RETURN] */
    private static boolean isSameMedia(TLRPC.Message message, TLRPC.Message message2) {
        TLRPC.MessageMedia messageMedia;
        TLRPC.Document document;
        TLRPC.Document document2;
        TLRPC.Photo photo;
        TLRPC.MessageMedia messageMedia2 = message.media;
        TLRPC.MessageMedia messageMedia3 = message2.media;
        boolean z = messageMedia2 == messageMedia3 || !(messageMedia2 == null || messageMedia3 == null || messageMedia2.getClass() != message2.media.getClass());
        TLRPC.MessageMedia messageMedia4 = message.media;
        if (messageMedia4 instanceof TLRPC.TL_messageMediaPhoto) {
            TLRPC.MessageMedia messageMedia5 = message2.media;
            if (messageMedia5 instanceof TLRPC.TL_messageMediaPhoto) {
                TLRPC.Photo photo2 = messageMedia4.photo;
                if (photo2 != null && (photo = messageMedia5.photo) != null) {
                    return photo2.id == photo.id;
                }
            } else if (messageMedia4 instanceof TLRPC.TL_messageMediaDocument) {
                messageMedia = message2.media;
                if ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && (document = messageMedia4.document) != null && (document2 = messageMedia.document) != null) {
                    if (document.id == document2.id) {
                        return true;
                    }
                    return false;
                }
            }
        } else if (messageMedia4 instanceof TLRPC.TL_messageMediaDocument) {
            messageMedia = message2.media;
            if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                if (document.id == document2.id) {
                    return true;
                }
                return false;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: saveEditedMessage, reason: merged with bridge method [inline-methods] */
    public void lambda$onMessageEdited$0(SaveMessageRequest saveMessageRequest, boolean z) {
        EditedMessage lastRevision;
        String str;
        EditedMessage editedMessage = new EditedMessage();
        this.controller.getAyuMapperInternal().map(saveMessageRequest, editedMessage);
        try {
            this.controller.getAyuMapperInternal().mapMedia(saveMessageRequest, editedMessage, !z);
        } catch (Exception e) {
            FileLog.e("Failed to map media for edited message " + saveMessageRequest.getMessageId(), e);
        }
        if (!z && !TextUtils.isEmpty(editedMessage.mediaPath) && (lastRevision = AyuData.getEditedMessageDao().getLastRevision(this.controller.getUserId(), saveMessageRequest.getDialogId(), saveMessageRequest.getMessageId())) != null && !TextUtils.equals(editedMessage.mediaPath, lastRevision.mediaPath) && (str = lastRevision.mediaPath) != null && !str.contains(AyuConfig.getSavePathFolder())) {
            AyuData.getEditedMessageDao().updateMediaPathForRevisionsBetweenDates(this.controller.getUserId(), saveMessageRequest.getDialogId(), saveMessageRequest.getMessageId(), lastRevision.mediaPath, editedMessage.mediaPath);
        }
        AyuData.getEditedMessageDao().insert(editedMessage);
    }

    public boolean hasAnyRevisions(long j, int i) {
        return AyuData.getEditedMessageDao().hasAnyRevisions(this.controller.getUserId(), j, i);
    }

    public List getRevisions(long j, int i, int i2, int i3) {
        return AyuData.getEditedMessageDao().getAllRevisions(this.controller.getUserId(), j, i, i2, i3);
    }
}
