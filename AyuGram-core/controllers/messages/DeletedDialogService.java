package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import android.util.LongSparseArray;
import androidx.core.util.Pair;
import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.utils.AyuHistoryHook;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;

public class DeletedDialogService {
    private final AyuMessagesController controller;

    public DeletedDialogService(AyuMessagesController ayuMessagesController) {
        this.controller = ayuMessagesController;
    }

    public void loadLastMessages() {
        List<DeletedMessageFull> lastMessages = AyuData.getDeletedMessageDao().getLastMessages(this.controller.getUserId());
        LongSparseArray longSparseArray = new LongSparseArray();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        this.controller.getLastMessages().clear();
        for (DeletedMessageFull deletedMessageFull : lastMessages) {
            if (!AyuHistoryHook.isEmpty(deletedMessageFull.message)) {
                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                this.controller.getAyuMapperInternal().map(deletedMessageFull.message, tL_message);
                try {
                    this.controller.getAyuMapperInternal().mapMedia(deletedMessageFull.message, tL_message);
                } catch (Exception e) {
                    FileLog.e("Failed to map media for message " + deletedMessageFull.message.messageId, e);
                }
                MessageObject messageObjectCreateWithEntities = createWithEntities(tL_message, longSparseArray, longSparseArray2);
                if (!TextUtils.isEmpty(messageObjectCreateWithEntities.messageText)) {
                    this.controller.getLastMessages().put(deletedMessageFull.message.dialogId, messageObjectCreateWithEntities);
                }
            }
        }
        final ArrayList arrayListSparseArrayToList = AyuUtils.sparseArrayToList(longSparseArray);
        final ArrayList arrayListSparseArrayToList2 = AyuUtils.sparseArrayToList(longSparseArray2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedDialogService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadLastMessages$0(arrayListSparseArrayToList, arrayListSparseArrayToList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLastMessages$0(ArrayList arrayList, ArrayList arrayList2) {
        if (!arrayList.isEmpty()) {
            this.controller.getMessagesControllerInternal().putUsers(arrayList, true);
        }
        if (!arrayList2.isEmpty()) {
            this.controller.getMessagesControllerInternal().putChats(arrayList2, true);
        }
        this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedDialogService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.loadDeletedDialogs();
            }
        }, "loadDeletedDialogs");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDeletedDialogs() {
        List<DeletedDialog> all = AyuData.getDeletedDialogDao().getAll(this.controller.getUserId());
        if (all.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        collectDialogIds(all, arrayList, arrayList2);
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        loadUsersFromStorage(arrayList, longSparseArray, arrayList3);
        loadChatsFromStorage(arrayList2, longSparseArray, arrayList4);
        restoreDialogsOnUIThread(all, longSparseArray, arrayList3, arrayList4);
    }

    private void collectDialogIds(List list, ArrayList arrayList, ArrayList arrayList2) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            long j = ((DeletedDialog) it.next()).dialogId;
            if (DialogObject.isUserDialog(j)) {
                if (!arrayList.contains(Long.valueOf(j))) {
                    arrayList.add(Long.valueOf(j));
                }
            } else if (DialogObject.isChatDialog(j)) {
                long j2 = -j;
                if (!arrayList2.contains(Long.valueOf(j2))) {
                    arrayList2.add(Long.valueOf(j2));
                }
            }
        }
    }

    private void loadUsersFromStorage(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Long l = (Long) obj;
            TLRPC.User user = this.controller.getMessagesStorageInternal().getUser(l.longValue());
            longSparseArray.put(l.longValue(), Boolean.valueOf(user != null));
            if (user != null) {
                arrayList2.add(user);
            }
        }
    }

    private void loadChatsFromStorage(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Long l = (Long) obj;
            TLRPC.Chat chat = this.controller.getMessagesStorageInternal().getChat(l.longValue());
            longSparseArray.put(l.longValue(), Boolean.valueOf(chat != null));
            if (chat != null) {
                arrayList2.add(chat);
            }
        }
    }

    private void restoreDialogsOnUIThread(final List list, final LongSparseArray longSparseArray, final ArrayList arrayList, final ArrayList arrayList2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedDialogService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$restoreDialogsOnUIThread$1(list, longSparseArray, arrayList, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreDialogsOnUIThread$1(List list, LongSparseArray longSparseArray, ArrayList arrayList, ArrayList arrayList2) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            DeletedDialog deletedDialog = (DeletedDialog) it.next();
            long j = deletedDialog.dialogId;
            try {
                if (DialogObject.isUserDialog(j)) {
                    if (((Boolean) longSparseArray.get(j, Boolean.FALSE)).booleanValue()) {
                        putDialog(deletedDialog);
                    }
                } else if (DialogObject.isChatDialog(j) && ((Boolean) longSparseArray.get(-j, Boolean.FALSE)).booleanValue()) {
                    putDialog(deletedDialog);
                }
            } catch (Throwable th) {
                FileLog.e("Failed to put deleted dialog " + j, th);
            }
        }
        this.controller.getMessagesControllerInternal().sortDialogs(null);
        try {
            if (!arrayList.isEmpty()) {
                this.controller.getMessagesControllerInternal().putUsers(arrayList, true);
            }
            if (!arrayList2.isEmpty()) {
                this.controller.getMessagesControllerInternal().putChats(arrayList2, true);
            }
            this.controller.getNotificationCenterInternal().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        } catch (Throwable th2) {
            FileLog.e("Failed to reload dialogs after loading deleted dialogs", th2);
        }
    }

    private void putDialog(DeletedDialog deletedDialog) {
        if (this.controller.getMessagesControllerInternal().getDialog(deletedDialog.dialogId) != null) {
            return;
        }
        TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
        this.controller.getAyuMapperInternal().map(deletedDialog, tL_dialog);
        this.controller.getMessagesControllerInternal().dialogs_dict.put(tL_dialog.id, tL_dialog);
        this.controller.getMessagesControllerInternal().getAllDialogs().add(tL_dialog);
        MessageObject lastMessageCached = this.controller.getLastMessageCached(deletedDialog.dialogId);
        if (lastMessageCached != null) {
            ArrayList arrayList = (ArrayList) this.controller.getMessagesControllerInternal().dialogMessage.get(tL_dialog.id);
            if (arrayList == null || arrayList.isEmpty() || ((MessageObject) arrayList.get(0)).messageOwner.id < lastMessageCached.messageOwner.id) {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(lastMessageCached);
                this.controller.getMessagesControllerInternal().dialogMessage.put(tL_dialog.id, arrayList2);
            }
        }
    }

    public void onDialogDeleted(final long j) {
        try {
            final TLRPC.Dialog dialog = this.controller.getMessagesControllerInternal().getDialog(j);
            if (dialog == null) {
                return;
            }
            this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedDialogService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDialogDeleted$2(j, dialog);
                }
            }, "saveDeletedDialog");
        } catch (Throwable th) {
            AyuUtils.logError("onDialogDeleted", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: saveDeletedDialog, reason: merged with bridge method [inline-methods] */
    public void lambda$onDialogDeleted$2(long j, TLRPC.Dialog dialog) {
        DeletedDialog deletedDialog = AyuData.getDeletedDialogDao().get(this.controller.getUserId(), j);
        if (deletedDialog != null) {
            AyuData.getDeletedDialogDao().delete(deletedDialog);
        }
        final DeletedDialog deletedDialog2 = new DeletedDialog();
        this.controller.getAyuMapperInternal().map(dialog, deletedDialog2);
        AyuData.getDeletedDialogDao().insert(deletedDialog2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.DeletedDialogService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveDeletedDialog$3(deletedDialog2);
            }
        }, 1500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveDeletedDialog$3(DeletedDialog deletedDialog) {
        putDialog(deletedDialog);
        this.controller.getMessagesControllerInternal().sortDialogs(null);
        this.controller.getNotificationCenterInternal().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
    }

    public void updateDeletedDialogsFolder(ArrayList arrayList, int i) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        AyuData.getDeletedDialogDao().updateDialogsFolder(this.controller.getUserId(), arrayList, i);
    }

    public void deleteExistingDialogs(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        AyuData.getDeletedDialogDao().deleteExisting(this.controller.getUserId(), arrayList);
    }

    public void deleteDialog(long j) {
        AyuData.getDeletedDialogDao().delete(this.controller.getUserId(), j);
    }

    public void updateLastMessage(long j, TLRPC.Message message) {
        MessageObject messageObject = (MessageObject) this.controller.getLastMessages().get(j);
        if (messageObject == null || AyuMessageUtils.compareMessages(messageObject.messageOwner, message) > 0) {
            this.controller.getLastMessages().put(j, createWithEntities(message, null, null));
        }
    }

    private MessageObject createWithEntities(TLRPC.Message message, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        MessagesStorage.addUsersAndChatsFromMessage(message, arrayList, arrayList2, null);
        Pair dicts = AyuHistoryHook.getEntities(this.controller.getMessagesStorageInternal(), arrayList, arrayList2).getDicts();
        if (longSparseArray != null) {
            for (int i = 0; i < ((androidx.collection.LongSparseArray) dicts.first).size(); i++) {
                TLRPC.User user = (TLRPC.User) ((androidx.collection.LongSparseArray) dicts.first).valueAt(i);
                longSparseArray.put(user.id, user);
            }
        }
        if (longSparseArray2 != null) {
            for (int i2 = 0; i2 < ((androidx.collection.LongSparseArray) dicts.second).size(); i2++) {
                TLRPC.Chat chat = (TLRPC.Chat) ((androidx.collection.LongSparseArray) dicts.second).valueAt(i2);
                longSparseArray2.put(chat.id, chat);
            }
        }
        return new MessageObject(this.controller.getCurrentAccount(), message, (androidx.collection.LongSparseArray) dicts.first, (androidx.collection.LongSparseArray) dicts.second, false, false);
    }
}
