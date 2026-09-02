package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.collection.LongSparseArray;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.utils.AyuLocalDatabaseUtils;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import okhttp3.internal.url._UrlKt;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLClassStore;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.AccountFrozenAlert;
import org.telegram.ui.ActionBar.AlertDialog;

public class SecretChatHelper extends BaseController {
    public static int CURRENT_SECRET_CHAT_LAYER = 151;
    private static volatile SecretChatHelper[] Instance = new SecretChatHelper[16];
    private SparseArray<TLRPC.EncryptedChat> acceptingChats;
    public ArrayList<TLRPC.Update> delayedEncryptedChatUpdates;
    private ArrayList<Long> pendingEncMessagesToDelete;
    private SparseArray<ArrayList<TLRPC.Update>> pendingSecretMessages;
    private SparseArray<SparseIntArray> requestedHoles;
    private SparseArray<ArrayList<TL_decryptedMessageHolder>> secretHolesQueue;
    private ArrayList<Integer> sendingNotifyLayer;
    private boolean startingSecretChat;

    public void sendScreenshotMessage(TLRPC.EncryptedChat encryptedChat, ArrayList<Long> arrayList, TLRPC.Message message) {
    }

    public static class TL_decryptedMessageHolder extends TLObject {
        public static int constructor = 1431655929;
        public int date;
        public int decryptedWithVersion;
        public TLRPC.EncryptedFile file;
        public TLRPC.TL_decryptedMessageLayer layer;
        public boolean new_key_used;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            inputSerializedData.readInt64(z);
            this.date = inputSerializedData.readInt32(z);
            this.layer = TLRPC.TL_decryptedMessageLayer.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if (inputSerializedData.readBool(z)) {
                this.file = TLRPC.EncryptedFile.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
            this.new_key_used = inputSerializedData.readBool(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(0L);
            outputSerializedData.writeInt32(this.date);
            this.layer.serializeToStream(outputSerializedData);
            outputSerializedData.writeBool(this.file != null);
            TLRPC.EncryptedFile encryptedFile = this.file;
            if (encryptedFile != null) {
                encryptedFile.serializeToStream(outputSerializedData);
            }
            outputSerializedData.writeBool(this.new_key_used);
        }
    }

    public static SecretChatHelper getInstance(int i) {
        SecretChatHelper secretChatHelper;
        SecretChatHelper secretChatHelper2 = Instance[i];
        if (secretChatHelper2 != null) {
            return secretChatHelper2;
        }
        synchronized (SecretChatHelper.class) {
            try {
                secretChatHelper = Instance[i];
                if (secretChatHelper == null) {
                    SecretChatHelper[] secretChatHelperArr = Instance;
                    SecretChatHelper secretChatHelper3 = new SecretChatHelper(i);
                    secretChatHelperArr[i] = secretChatHelper3;
                    secretChatHelper = secretChatHelper3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return secretChatHelper;
    }

    public SecretChatHelper(int i) {
        super(i);
        this.sendingNotifyLayer = new ArrayList<>();
        this.secretHolesQueue = new SparseArray<>();
        this.pendingSecretMessages = new SparseArray<>();
        this.requestedHoles = new SparseArray<>();
        this.acceptingChats = new SparseArray<>();
        this.delayedEncryptedChatUpdates = new ArrayList<>();
        this.pendingEncMessagesToDelete = new ArrayList<>();
        this.startingSecretChat = false;
    }

    public void cleanup() {
        this.sendingNotifyLayer.clear();
        this.acceptingChats.clear();
        this.secretHolesQueue.clear();
        this.pendingSecretMessages.clear();
        this.requestedHoles.clear();
        this.delayedEncryptedChatUpdates.clear();
        this.pendingEncMessagesToDelete.clear();
        this.startingSecretChat = false;
    }

    protected void processPendingEncMessages() {
        if (this.pendingEncMessagesToDelete.isEmpty()) {
            return;
        }
        final ArrayList arrayList = new ArrayList(this.pendingEncMessagesToDelete);
        if (AyuConfig.saveDeletedMessages) {
            final LongSparseArray messageIdsByRandomIds = AyuLocalDatabaseUtils.getMessageIdsByRandomIds(this.currentAccount, arrayList);
            android.util.LongSparseArray longSparseArray = new android.util.LongSparseArray();
            for (int i = 0; i < messageIdsByRandomIds.size(); i++) {
                long jKeyAt = messageIdsByRandomIds.keyAt(i);
                ArrayList arrayList2 = (ArrayList) messageIdsByRandomIds.valueAt(i);
                int size = arrayList2.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList2.get(i2);
                    i2++;
                    TLRPC.Message message = getMessagesStorage().getMessage(jKeyAt, ((Integer) obj).intValue());
                    if (message != null) {
                        ArrayList arrayList3 = (ArrayList) longSparseArray.get(jKeyAt);
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                            longSparseArray.put(jKeyAt, arrayList3);
                        }
                        arrayList3.add(message);
                    }
                }
            }
            for (int i3 = 0; i3 < longSparseArray.size(); i3++) {
                final long jKeyAt2 = longSparseArray.keyAt(i3);
                ArrayList arrayList4 = (ArrayList) longSparseArray.valueAt(i3);
                int size2 = arrayList4.size();
                int i4 = 0;
                while (i4 < size2) {
                    Object obj2 = arrayList4.get(i4);
                    i4++;
                    SaveMessageRequest saveMessageRequest = new SaveMessageRequest(this.currentAccount, (TLRPC.Message) obj2);
                    saveMessageRequest.setDialogId(jKeyAt2);
                    getAyuMessagesController().onMessageDeleted(saveMessageRequest);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda32
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processPendingEncMessages$0(jKeyAt2, messageIdsByRandomIds);
                    }
                });
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processPendingEncMessages$1(arrayList);
            }
        });
        getMessagesStorage().markMessagesAsDeletedByRandoms(new ArrayList<>(this.pendingEncMessagesToDelete));
        this.pendingEncMessagesToDelete.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingEncMessages$0(long j, LongSparseArray longSparseArray) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(AyuConstants.MESSAGES_DELETED_NOTIFICATION, Long.valueOf(j), longSparseArray.get(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingEncMessages$1(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = (MessageObject) getMessagesController().dialogMessagesByRandomIds.get(((Long) arrayList.get(i)).longValue());
            if (messageObject != null) {
                messageObject.deleted = true;
            }
        }
    }

    private TLRPC.TL_messageService createServiceSecretMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.DecryptedMessageAction decryptedMessageAction) {
        TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
        TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction = new TLRPC.TL_messageEncryptedAction();
        tL_messageService.action = tL_messageEncryptedAction;
        tL_messageEncryptedAction.encryptedAction = decryptedMessageAction;
        int newMessageId = getUserConfig().getNewMessageId();
        tL_messageService.id = newMessageId;
        tL_messageService.local_id = newMessageId;
        TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
        tL_messageService.from_id = tL_peerUser;
        tL_peerUser.user_id = getUserConfig().getClientUserId();
        tL_messageService.unread = true;
        tL_messageService.out = true;
        tL_messageService.flags = 256;
        tL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
        tL_messageService.peer_id = new TLRPC.TL_peerUser();
        tL_messageService.send_state = 1;
        if (encryptedChat.participant_id == getUserConfig().getClientUserId()) {
            tL_messageService.peer_id.user_id = encryptedChat.admin_id;
        } else {
            tL_messageService.peer_id.user_id = encryptedChat.participant_id;
        }
        if ((decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
            tL_messageService.date = getConnectionsManager().getCurrentTime();
        } else {
            tL_messageService.date = 0;
        }
        tL_messageService.random_id = getSendMessagesHelper().getNextRandomId();
        getUserConfig().saveConfig(false);
        ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
        arrayList.add(tL_messageService);
        getMessagesStorage().putMessages(arrayList, false, true, true, 0, false, 0, 0L);
        return tL_messageService;
    }

    public void sendMessagesReadMessage(TLRPC.EncryptedChat encryptedChat, ArrayList<Long> arrayList, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionReadMessages tL_decryptedMessageActionReadMessages = new TLRPC.TL_decryptedMessageActionReadMessages();
                tL_decryptedMessageService.action = tL_decryptedMessageActionReadMessages;
                tL_decryptedMessageActionReadMessages.random_ids = arrayList;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionReadMessages);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    protected void processUpdateEncryption(TLRPC.TL_updateEncryption tL_updateEncryption, ConcurrentHashMap<Long, TLRPC.User> concurrentHashMap) {
        byte[] bArr;
        final TLRPC.EncryptedChat encryptedChat = tL_updateEncryption.chat;
        final long jMakeEncryptedDialogId = DialogObject.makeEncryptedDialogId(encryptedChat.id);
        final TLRPC.EncryptedChat encryptedChatDB = getMessagesController().getEncryptedChatDB(encryptedChat.id, false);
        if ((encryptedChat instanceof TLRPC.TL_encryptedChatRequested) && encryptedChatDB == null) {
            long j = encryptedChat.participant_id;
            if (j == getUserConfig().getClientUserId()) {
                j = encryptedChat.admin_id;
            }
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(j));
            if (user == null) {
                user = concurrentHashMap.get(Long.valueOf(j));
            }
            encryptedChat.user_id = j;
            final TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
            tL_dialog.id = jMakeEncryptedDialogId;
            tL_dialog.folder_id = encryptedChat.folder_id;
            tL_dialog.unread_count = 0;
            tL_dialog.top_message = 0;
            tL_dialog.last_message_date = tL_updateEncryption.date;
            getMessagesController().putEncryptedChat(encryptedChat, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processUpdateEncryption$2(tL_dialog, jMakeEncryptedDialogId);
                }
            });
            getMessagesStorage().putEncryptedChat(encryptedChat, user, tL_dialog);
            acceptSecretChat(encryptedChat);
        } else if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            if ((encryptedChatDB instanceof TLRPC.TL_encryptedChatWaiting) && ((bArr = encryptedChatDB.auth_key) == null || bArr.length == 1)) {
                encryptedChat.a_or_b = encryptedChatDB.a_or_b;
                encryptedChat.user_id = encryptedChatDB.user_id;
                processAcceptedSecretChat(encryptedChat);
            } else if (encryptedChatDB == null && this.startingSecretChat) {
                this.delayedEncryptedChatUpdates.add(tL_updateEncryption);
            }
        } else {
            if (encryptedChatDB != null) {
                encryptedChat.user_id = encryptedChatDB.user_id;
                encryptedChat.auth_key = encryptedChatDB.auth_key;
                encryptedChat.key_create_date = encryptedChatDB.key_create_date;
                encryptedChat.key_use_count_in = encryptedChatDB.key_use_count_in;
                encryptedChat.key_use_count_out = encryptedChatDB.key_use_count_out;
                encryptedChat.ttl = encryptedChatDB.ttl;
                encryptedChat.seq_in = encryptedChatDB.seq_in;
                encryptedChat.seq_out = encryptedChatDB.seq_out;
                encryptedChat.admin_id = encryptedChatDB.admin_id;
                encryptedChat.mtproto_seq = encryptedChatDB.mtproto_seq;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processUpdateEncryption$3(encryptedChatDB, encryptedChat);
                }
            });
        }
        if ((encryptedChat instanceof TLRPC.TL_encryptedChatDiscarded) && encryptedChat.history_deleted) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processUpdateEncryption$4(jMakeEncryptedDialogId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$2(TLRPC.Dialog dialog, long j) {
        if (dialog.folder_id == 1) {
            SharedPreferences.Editor editorEdit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            editorEdit.putBoolean("dialog_bar_archived" + j, true);
            editorEdit.apply();
        }
        getMessagesController().dialogs_dict.put(dialog.id, dialog);
        getMessagesController().allDialogs.add(dialog);
        getMessagesController().sortDialogs(null);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$3(TLRPC.EncryptedChat encryptedChat, TLRPC.EncryptedChat encryptedChat2) {
        if (encryptedChat != null) {
            getMessagesController().putEncryptedChat(encryptedChat2, false);
        }
        getMessagesStorage().updateEncryptedChat(encryptedChat2);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, encryptedChat2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$4(long j) {
        getMessagesController().deleteDialog(j, 0);
    }

    public void sendMessagesDeleteMessage(TLRPC.EncryptedChat encryptedChat, ArrayList<Long> arrayList, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionDeleteMessages tL_decryptedMessageActionDeleteMessages = new TLRPC.TL_decryptedMessageActionDeleteMessages();
                tL_decryptedMessageService.action = tL_decryptedMessageActionDeleteMessages;
                tL_decryptedMessageActionDeleteMessages.random_ids = arrayList;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionDeleteMessages);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendClearHistoryMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionFlushHistory tL_decryptedMessageActionFlushHistory = new TLRPC.TL_decryptedMessageActionFlushHistory();
                tL_decryptedMessageService.action = tL_decryptedMessageActionFlushHistory;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionFlushHistory);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendNotifyLayerMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if ((encryptedChat instanceof TLRPC.TL_encryptedChat) && !this.sendingNotifyLayer.contains(Integer.valueOf(encryptedChat.id))) {
            this.sendingNotifyLayer.add(Integer.valueOf(encryptedChat.id));
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionNotifyLayer tL_decryptedMessageActionNotifyLayer = new TLRPC.TL_decryptedMessageActionNotifyLayer();
                tL_decryptedMessageService.action = tL_decryptedMessageActionNotifyLayer;
                tL_decryptedMessageActionNotifyLayer.layer = CURRENT_SECRET_CHAT_LAYER;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionNotifyLayer);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendRequestKeyMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionRequestKey tL_decryptedMessageActionRequestKey = new TLRPC.TL_decryptedMessageActionRequestKey();
                tL_decryptedMessageService.action = tL_decryptedMessageActionRequestKey;
                tL_decryptedMessageActionRequestKey.exchange_id = encryptedChat.exchange_id;
                tL_decryptedMessageActionRequestKey.g_a = encryptedChat.g_a;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionRequestKey);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendAcceptKeyMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionAcceptKey tL_decryptedMessageActionAcceptKey = new TLRPC.TL_decryptedMessageActionAcceptKey();
                tL_decryptedMessageService.action = tL_decryptedMessageActionAcceptKey;
                tL_decryptedMessageActionAcceptKey.exchange_id = encryptedChat.exchange_id;
                tL_decryptedMessageActionAcceptKey.key_fingerprint = encryptedChat.future_key_fingerprint;
                tL_decryptedMessageActionAcceptKey.g_b = encryptedChat.g_a_or_b;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionAcceptKey);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendCommitKeyMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionCommitKey tL_decryptedMessageActionCommitKey = new TLRPC.TL_decryptedMessageActionCommitKey();
                tL_decryptedMessageService.action = tL_decryptedMessageActionCommitKey;
                tL_decryptedMessageActionCommitKey.exchange_id = encryptedChat.exchange_id;
                tL_decryptedMessageActionCommitKey.key_fingerprint = encryptedChat.future_key_fingerprint;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionCommitKey);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendAbortKeyMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message, long j) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionAbortKey tL_decryptedMessageActionAbortKey = new TLRPC.TL_decryptedMessageActionAbortKey();
                tL_decryptedMessageService.action = tL_decryptedMessageActionAbortKey;
                tL_decryptedMessageActionAbortKey.exchange_id = j;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionAbortKey);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendNoopMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionNoop tL_decryptedMessageActionNoop = new TLRPC.TL_decryptedMessageActionNoop();
                tL_decryptedMessageService.action = tL_decryptedMessageActionNoop;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionNoop);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    public void sendResendMessage(TLRPC.EncryptedChat encryptedChat, int i, int i2, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            SparseIntArray sparseIntArray = this.requestedHoles.get(encryptedChat.id);
            if (sparseIntArray == null || sparseIntArray.indexOfKey(i) < 0) {
                if (sparseIntArray == null) {
                    sparseIntArray = new SparseIntArray();
                    this.requestedHoles.put(encryptedChat.id, sparseIntArray);
                }
                sparseIntArray.put(i, i2);
                TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
                if (message != null) {
                    tL_decryptedMessageService.action = message.action.encryptedAction;
                } else {
                    TLRPC.TL_decryptedMessageActionResend tL_decryptedMessageActionResend = new TLRPC.TL_decryptedMessageActionResend();
                    tL_decryptedMessageService.action = tL_decryptedMessageActionResend;
                    tL_decryptedMessageActionResend.start_seq_no = i;
                    tL_decryptedMessageActionResend.end_seq_no = i2;
                    message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionResend);
                }
                TLRPC.Message message2 = message;
                tL_decryptedMessageService.random_id = message2.random_id;
                performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
            }
        }
    }

    public void sendTTLMessage(TLRPC.EncryptedChat encryptedChat, TLRPC.Message message) {
        if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_decryptedMessageService tL_decryptedMessageService = new TLRPC.TL_decryptedMessageService();
            if (message != null) {
                tL_decryptedMessageService.action = message.action.encryptedAction;
            } else {
                TLRPC.TL_decryptedMessageActionSetMessageTTL tL_decryptedMessageActionSetMessageTTL = new TLRPC.TL_decryptedMessageActionSetMessageTTL();
                tL_decryptedMessageService.action = tL_decryptedMessageActionSetMessageTTL;
                tL_decryptedMessageActionSetMessageTTL.ttl_seconds = encryptedChat.ttl;
                message = createServiceSecretMessage(encryptedChat, tL_decryptedMessageActionSetMessageTTL);
                MessageObject messageObject = new MessageObject(this.currentAccount, message, false, false);
                messageObject.messageOwner.send_state = 1;
                messageObject.wasJustSent = true;
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                arrayList.add(messageObject);
                getMessagesController().updateInterfaceWithMessages(message.dialog_id, arrayList, 0);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            TLRPC.Message message2 = message;
            tL_decryptedMessageService.random_id = message2.random_id;
            performSendEncryptedRequest(tL_decryptedMessageService, message2, encryptedChat, null, null, null);
        }
    }

    private void updateMediaPaths(MessageObject messageObject, TLRPC.EncryptedFile encryptedFile, TLRPC.DecryptedMessage decryptedMessage, String str) {
        TLRPC.Document document;
        TLRPC.Photo photo;
        TLRPC.Message message = messageObject.messageOwner;
        if (encryptedFile != null) {
            TLRPC.MessageMedia messageMedia = message.media;
            if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (photo = messageMedia.photo) != null) {
                ArrayList arrayList = photo.sizes;
                TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) arrayList.get(arrayList.size() - 1);
                String str2 = photoSize.location.volume_id + "_" + photoSize.location.local_id;
                TLRPC.TL_fileEncryptedLocation tL_fileEncryptedLocation = new TLRPC.TL_fileEncryptedLocation();
                photoSize.location = tL_fileEncryptedLocation;
                TLRPC.DecryptedMessageMedia decryptedMessageMedia = decryptedMessage.media;
                tL_fileEncryptedLocation.key = decryptedMessageMedia.key;
                tL_fileEncryptedLocation.iv = decryptedMessageMedia.iv;
                tL_fileEncryptedLocation.dc_id = encryptedFile.dc_id;
                tL_fileEncryptedLocation.volume_id = encryptedFile.id;
                tL_fileEncryptedLocation.secret = encryptedFile.access_hash;
                tL_fileEncryptedLocation.local_id = encryptedFile.key_fingerprint;
                String str3 = photoSize.location.volume_id + "_" + photoSize.location.local_id;
                new File(FileLoader.getDirectory(4), str2 + ".jpg").renameTo(getFileLoader().getPathToAttach(photoSize));
                ImageLoader.getInstance().replaceImageInCache(str2, str3, ImageLocation.getForPhoto(photoSize, message.media.photo), true);
                ArrayList<TLRPC.Message> arrayList2 = new ArrayList<>();
                arrayList2.add(message);
                getMessagesStorage().putMessages(arrayList2, false, true, false, 0, false, 0, 0L);
                return;
            }
            if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument) || (document = messageMedia.document) == null) {
                return;
            }
            messageMedia.document = new TLRPC.TL_documentEncrypted();
            TLRPC.Document document2 = message.media.document;
            document2.id = encryptedFile.id;
            document2.access_hash = encryptedFile.access_hash;
            document2.date = document.date;
            document2.attributes = document.attributes;
            document2.mime_type = document.mime_type;
            document2.size = encryptedFile.size;
            TLRPC.DecryptedMessageMedia decryptedMessageMedia2 = decryptedMessage.media;
            document2.key = decryptedMessageMedia2.key;
            document2.iv = decryptedMessageMedia2.iv;
            ArrayList<TLRPC.PhotoSize> arrayList3 = document.thumbs;
            document2.thumbs = arrayList3;
            document2.dc_id = encryptedFile.dc_id;
            if (arrayList3.isEmpty()) {
                TLRPC.TL_photoSizeEmpty tL_photoSizeEmpty = new TLRPC.TL_photoSizeEmpty();
                tL_photoSizeEmpty.type = "s";
                message.media.document.thumbs.add(tL_photoSizeEmpty);
            }
            String str4 = message.attachPath;
            if (str4 != null && str4.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && new File(message.attachPath).renameTo(getFileLoader().getPathToAttach(message.media.document))) {
                messageObject.mediaExists = messageObject.attachPathExists;
                messageObject.attachPathExists = false;
                message.attachPath = _UrlKt.FRAGMENT_ENCODE_SET;
            }
            ArrayList<TLRPC.Message> arrayList4 = new ArrayList<>();
            arrayList4.add(message);
            getMessagesStorage().putMessages(arrayList4, false, true, false, 0, 0, 0L);
        }
    }

    public static boolean isSecretVisibleMessage(TLRPC.Message message) {
        TLRPC.MessageAction messageAction = message.action;
        if (!(messageAction instanceof TLRPC.TL_messageEncryptedAction)) {
            return false;
        }
        TLRPC.DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
        return (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL);
    }

    public static boolean isSecretInvisibleMessage(TLRPC.Message message) {
        TLRPC.MessageAction messageAction = message.action;
        if (!(messageAction instanceof TLRPC.TL_messageEncryptedAction)) {
            return false;
        }
        TLRPC.DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
        return ((decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) || (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) ? false : true;
    }

    protected void performSendEncryptedRequest(TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia, SendMessagesHelper.DelayedMessage delayedMessage) {
        for (int i = 0; i < tL_messages_sendEncryptedMultiMedia.files.size(); i++) {
            performSendEncryptedRequest((TLRPC.DecryptedMessage) tL_messages_sendEncryptedMultiMedia.messages.get(i), delayedMessage.messages.get(i), delayedMessage.encryptedChat, (TLRPC.InputEncryptedFile) tL_messages_sendEncryptedMultiMedia.files.get(i), delayedMessage.originalPaths.get(i), delayedMessage.messageObjects.get(i));
        }
    }

    protected void performSendEncryptedRequest(final TLRPC.DecryptedMessage decryptedMessage, final TLRPC.Message message, final TLRPC.EncryptedChat encryptedChat, final TLRPC.InputEncryptedFile inputEncryptedFile, final String str, final MessageObject messageObject) {
        if (decryptedMessage == null || encryptedChat.auth_key == null || (encryptedChat instanceof TLRPC.TL_encryptedChatRequested) || (encryptedChat instanceof TLRPC.TL_encryptedChatWaiting)) {
            return;
        }
        getSendMessagesHelper().putToSendingMessages(message, false);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendEncryptedRequest$9(encryptedChat, decryptedMessage, message, inputEncryptedFile, messageObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$9(final TLRPC.EncryptedChat encryptedChat, final TLRPC.DecryptedMessage decryptedMessage, final TLRPC.Message message, TLRPC.InputEncryptedFile inputEncryptedFile, final MessageObject messageObject, final String str) {
        TLObject tLObject;
        TLObject tLObject2;
        try {
            TLRPC.TL_decryptedMessageLayer tL_decryptedMessageLayer = new TLRPC.TL_decryptedMessageLayer();
            tL_decryptedMessageLayer.layer = Math.min(Math.max(46, AndroidUtilities.getMyLayerVersion(encryptedChat.layer)), Math.max(46, AndroidUtilities.getPeerLayerVersion(encryptedChat.layer)));
            tL_decryptedMessageLayer.message = decryptedMessage;
            byte[] bArr = new byte[15];
            tL_decryptedMessageLayer.random_bytes = bArr;
            Utilities.random.nextBytes(bArr);
            boolean z = true;
            if (encryptedChat.seq_in == 0 && encryptedChat.seq_out == 0) {
                if (encryptedChat.admin_id == getUserConfig().getClientUserId()) {
                    encryptedChat.seq_out = 1;
                    encryptedChat.seq_in = -2;
                } else {
                    encryptedChat.seq_in = -1;
                }
            }
            int i = message.seq_in;
            if (i == 0 && message.seq_out == 0) {
                int i2 = encryptedChat.seq_in;
                if (i2 <= 0) {
                    i2 += 2;
                }
                tL_decryptedMessageLayer.in_seq_no = i2;
                int i3 = encryptedChat.seq_out;
                tL_decryptedMessageLayer.out_seq_no = i3;
                encryptedChat.seq_out = i3 + 2;
                if (encryptedChat.key_create_date == 0) {
                    encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                }
                short s = (short) (encryptedChat.key_use_count_out + 1);
                encryptedChat.key_use_count_out = s;
                if ((s >= 100 || encryptedChat.key_create_date < getConnectionsManager().getCurrentTime() - 604800) && encryptedChat.exchange_id == 0 && encryptedChat.future_key_fingerprint == 0) {
                    requestNewSecretChatKey(encryptedChat);
                }
                getMessagesStorage().updateEncryptedChatSeq(encryptedChat, false);
                message.seq_in = tL_decryptedMessageLayer.in_seq_no;
                message.seq_out = tL_decryptedMessageLayer.out_seq_no;
                getMessagesStorage().setMessageSeq(message.id, message.seq_in, message.seq_out);
            } else {
                tL_decryptedMessageLayer.in_seq_no = i;
                tL_decryptedMessageLayer.out_seq_no = message.seq_out;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d(decryptedMessage + " send message with in_seq = " + tL_decryptedMessageLayer.in_seq_no + " out_seq = " + tL_decryptedMessageLayer.out_seq_no);
            }
            int objectSize = tL_decryptedMessageLayer.getObjectSize();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(objectSize + 4);
            nativeByteBuffer.writeInt32(objectSize);
            tL_decryptedMessageLayer.serializeToStream(nativeByteBuffer);
            int length = nativeByteBuffer.length();
            int iNextInt = (length % 16 != 0 ? 16 - (length % 16) : 0) + ((Utilities.random.nextInt(3) + 2) * 16);
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(length + iNextInt);
            nativeByteBuffer.position(0);
            nativeByteBuffer2.writeBytes(nativeByteBuffer);
            if (iNextInt != 0) {
                byte[] bArr2 = new byte[iNextInt];
                Utilities.random.nextBytes(bArr2);
                nativeByteBuffer2.writeBytes(bArr2);
            }
            byte[] bArr3 = new byte[16];
            if (encryptedChat.admin_id == getUserConfig().getClientUserId()) {
                z = false;
            }
            byte[] bArr4 = encryptedChat.auth_key;
            int i4 = z ? 8 : 0;
            ByteBuffer byteBuffer = nativeByteBuffer2.buffer;
            System.arraycopy(Utilities.computeSHA256(bArr4, i4 + 88, 32, byteBuffer, 0, byteBuffer.limit()), 8, bArr3, 0, 16);
            nativeByteBuffer.reuse();
            MessageKeyData messageKeyDataGenerateMessageKeyData = MessageKeyData.generateMessageKeyData(encryptedChat.auth_key, bArr3, z, 2);
            Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, messageKeyDataGenerateMessageKeyData.aesKey, messageKeyDataGenerateMessageKeyData.aesIv, true, false, 0, nativeByteBuffer2.limit());
            NativeByteBuffer nativeByteBuffer3 = new NativeByteBuffer(24 + nativeByteBuffer2.length());
            nativeByteBuffer2.position(0);
            nativeByteBuffer3.writeInt64(encryptedChat.key_fingerprint);
            nativeByteBuffer3.writeBytes(bArr3);
            nativeByteBuffer3.writeBytes(nativeByteBuffer2);
            nativeByteBuffer2.reuse();
            nativeByteBuffer3.position(0);
            if (inputEncryptedFile == null) {
                if (decryptedMessage instanceof TLRPC.TL_decryptedMessageService) {
                    TLRPC.TL_messages_sendEncryptedService tL_messages_sendEncryptedService = new TLRPC.TL_messages_sendEncryptedService();
                    tL_messages_sendEncryptedService.data = nativeByteBuffer3;
                    tL_messages_sendEncryptedService.random_id = decryptedMessage.random_id;
                    TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat = new TLRPC.TL_inputEncryptedChat();
                    tL_messages_sendEncryptedService.peer = tL_inputEncryptedChat;
                    tL_inputEncryptedChat.chat_id = encryptedChat.id;
                    tL_inputEncryptedChat.access_hash = encryptedChat.access_hash;
                    tLObject2 = tL_messages_sendEncryptedService;
                } else {
                    TLRPC.TL_messages_sendEncrypted tL_messages_sendEncrypted = new TLRPC.TL_messages_sendEncrypted();
                    tL_messages_sendEncrypted.silent = message.silent;
                    tL_messages_sendEncrypted.data = nativeByteBuffer3;
                    tL_messages_sendEncrypted.random_id = decryptedMessage.random_id;
                    TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat2 = new TLRPC.TL_inputEncryptedChat();
                    tL_messages_sendEncrypted.peer = tL_inputEncryptedChat2;
                    tL_inputEncryptedChat2.chat_id = encryptedChat.id;
                    tL_inputEncryptedChat2.access_hash = encryptedChat.access_hash;
                    tLObject2 = tL_messages_sendEncrypted;
                }
                tLObject = tLObject2;
            } else {
                TLRPC.TL_messages_sendEncryptedFile tL_messages_sendEncryptedFile = new TLRPC.TL_messages_sendEncryptedFile();
                tL_messages_sendEncryptedFile.silent = message.silent;
                tL_messages_sendEncryptedFile.data = nativeByteBuffer3;
                tL_messages_sendEncryptedFile.random_id = decryptedMessage.random_id;
                TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat3 = new TLRPC.TL_inputEncryptedChat();
                tL_messages_sendEncryptedFile.peer = tL_inputEncryptedChat3;
                tL_inputEncryptedChat3.chat_id = encryptedChat.id;
                tL_inputEncryptedChat3.access_hash = encryptedChat.access_hash;
                tL_messages_sendEncryptedFile.file = inputEncryptedFile;
                tLObject = tL_messages_sendEncryptedFile;
            }
            getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject3, TLRPC.TL_error tL_error) {
                    this.f$0.lambda$performSendEncryptedRequest$8(decryptedMessage, encryptedChat, message, messageObject, str, tLObject3, tL_error);
                }
            }, 64);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$8(TLRPC.DecryptedMessage decryptedMessage, TLRPC.EncryptedChat encryptedChat, final TLRPC.Message message, MessageObject messageObject, String str, TLObject tLObject, TLRPC.TL_error tL_error) {
        final int mediaExistanceFlags = 0;
        if (tL_error == null && (decryptedMessage.action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer)) {
            TLRPC.EncryptedChat encryptedChat2 = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChat.id));
            if (encryptedChat2 == null) {
                encryptedChat2 = encryptedChat;
            }
            if (encryptedChat2.key_hash == null) {
                encryptedChat2.key_hash = AndroidUtilities.calcAuthKeyHash(encryptedChat2.auth_key);
            }
            if (encryptedChat2.key_hash.length == 16) {
                try {
                    byte[] bArr = encryptedChat.auth_key;
                    byte[] bArrComputeSHA256 = Utilities.computeSHA256(bArr, 0, bArr.length);
                    byte[] bArr2 = new byte[36];
                    System.arraycopy(encryptedChat.key_hash, 0, bArr2, 0, 16);
                    System.arraycopy(bArrComputeSHA256, 0, bArr2, 16, 20);
                    encryptedChat2.key_hash = bArr2;
                    getMessagesStorage().updateEncryptedChat(encryptedChat2);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            this.sendingNotifyLayer.remove(Integer.valueOf(encryptedChat2.id));
            encryptedChat2.layer = AndroidUtilities.setMyLayerVersion(encryptedChat2.layer, CURRENT_SECRET_CHAT_LAYER);
            getMessagesStorage().updateEncryptedChatLayer(encryptedChat2);
        }
        if (tL_error == null) {
            String str2 = message.attachPath;
            final TLRPC.messages_SentEncryptedMessage messages_sentencryptedmessage = (TLRPC.messages_SentEncryptedMessage) tLObject;
            if (isSecretVisibleMessage(message)) {
                message.date = messages_sentencryptedmessage.date;
            }
            if (messageObject != null) {
                TLRPC.EncryptedFile encryptedFile = messages_sentencryptedmessage.file;
                if (encryptedFile instanceof TLRPC.TL_encryptedFile) {
                    updateMediaPaths(messageObject, encryptedFile, decryptedMessage, str);
                    mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                }
            }
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performSendEncryptedRequest$6(message, messages_sentencryptedmessage, mediaExistanceFlags);
                }
            });
            return;
        }
        getMessagesStorage().markMessageAsSendError(message, 0);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendEncryptedRequest$7(message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$6(final TLRPC.Message message, TLRPC.messages_SentEncryptedMessage messages_sentencryptedmessage, final int i) {
        if (isSecretInvisibleMessage(message)) {
            messages_sentencryptedmessage.date = 0;
        }
        getMessagesStorage().updateMessageStateAndId(message.random_id, 0L, Integer.valueOf(message.id), message.id, messages_sentencryptedmessage.date, false, 0, 0);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$performSendEncryptedRequest$5(message, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$5(TLRPC.Message message, int i) {
        message.send_state = 0;
        NotificationCenter notificationCenter = getNotificationCenter();
        int i2 = NotificationCenter.messageReceivedByServer;
        Integer numValueOf = Integer.valueOf(message.id);
        Integer numValueOf2 = Integer.valueOf(message.id);
        Long lValueOf = Long.valueOf(message.dialog_id);
        Integer numValueOf3 = Integer.valueOf(i);
        Boolean bool = Boolean.FALSE;
        notificationCenter.lambda$postNotificationNameOnUIThread$1(i2, numValueOf, numValueOf2, message, lValueOf, 0L, numValueOf3, bool);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(message.id), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i), bool);
        getSendMessagesHelper().processSentMessage(message.id);
        getSendMessagesHelper().removeFromSendingMessages(message.id, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$7(TLRPC.Message message) {
        message.send_state = 2;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
        getSendMessagesHelper().processSentMessage(message.id);
        getSendMessagesHelper().removeFromSendingMessages(message.id, false);
    }

    private void applyPeerLayer(final TLRPC.EncryptedChat encryptedChat, int i) {
        int peerLayerVersion = AndroidUtilities.getPeerLayerVersion(encryptedChat.layer);
        if (i <= peerLayerVersion) {
            return;
        }
        if (encryptedChat.key_hash.length == 16) {
            try {
                byte[] bArr = encryptedChat.auth_key;
                byte[] bArrComputeSHA256 = Utilities.computeSHA256(bArr, 0, bArr.length);
                byte[] bArr2 = new byte[36];
                System.arraycopy(encryptedChat.key_hash, 0, bArr2, 0, 16);
                System.arraycopy(bArrComputeSHA256, 0, bArr2, 16, 20);
                encryptedChat.key_hash = bArr2;
                getMessagesStorage().updateEncryptedChat(encryptedChat);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        encryptedChat.layer = AndroidUtilities.setPeerLayerVersion(encryptedChat.layer, i);
        getMessagesStorage().updateEncryptedChatLayer(encryptedChat);
        if (peerLayerVersion < CURRENT_SECRET_CHAT_LAYER) {
            sendNotifyLayerMessage(encryptedChat, null);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$applyPeerLayer$10(encryptedChat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyPeerLayer$10(TLRPC.EncryptedChat encryptedChat) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, encryptedChat);
    }

    /* JADX WARN: Code duplicated, block: B:224:0x05fd  */
    /* JADX WARN: Code duplicated, block: B:227:0x0608  */
    /* JADX WARN: Code duplicated, block: B:229:0x060e  */
    /* JADX WARN: Code duplicated, block: B:232:0x061a  */
    /* JADX WARN: Code duplicated, block: B:234:0x0623  */
    /* JADX WARN: Code duplicated, block: B:236:0x062c  */
    /* JADX WARN: Code duplicated, block: B:27:0x0087  */
    /* JADX WARN: Code duplicated, block: B:308:0x07e7  */
    /* JADX WARN: Code duplicated, block: B:309:0x07f8  */
    /* JADX WARN: Code duplicated, block: B:342:0x08d8  */
    /* JADX WARN: Code duplicated, block: B:345:0x08ea  */
    /* JADX WARN: Code duplicated, block: B:347:0x08fa  */
    /* JADX WARN: Code duplicated, block: B:34:0x00f2  */
    /* JADX WARN: Code duplicated, block: B:350:0x0942  */
    /* JADX WARN: Code duplicated, block: B:352:0x0946  */
    /* JADX WARN: Code duplicated, block: B:376:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:37:0x010a  */
    /* JADX WARN: Instruction removed from duplicated block: B:352:0x0946, please report this as an issue */
    public TLRPC.Message processDecryptedObject(TLRPC.EncryptedChat encryptedChat, TLRPC.EncryptedFile encryptedFile, int i, TLObject tLObject, boolean z) {
        TLRPC.Message message;
        TLRPC.TL_decryptedMessageService tL_decryptedMessageService;
        TLRPC.DecryptedMessageAction decryptedMessageAction;
        TLRPC.TL_messageService tL_messageService;
        TLRPC.DecryptedMessageAction decryptedMessageAction2;
        int i2;
        int i3;
        int i4;
        int i5;
        byte[] bArr;
        long jBytesToLong;
        byte[] bArr2;
        TLRPC.TL_decryptedMessage tL_decryptedMessage;
        TLRPC.TL_message_secret tL_message_secret;
        String str;
        long j;
        TLRPC.DecryptedMessageMedia decryptedMessageMedia;
        int i6;
        String str2;
        TLRPC.MessageMedia messageMedia;
        byte[] bArr3;
        byte[] bArr4;
        TLRPC.PhotoSize tL_photoSizeEmpty;
        byte[] bArr5;
        TLRPC.PhotoSize tL_photoSizeEmpty2;
        byte[] bArr6;
        if (tLObject != null) {
            long j2 = encryptedChat.admin_id;
            if (j2 == getUserConfig().getClientUserId()) {
                j2 = encryptedChat.participant_id;
            }
            long j3 = 0;
            if (encryptedChat.exchange_id == 0 && encryptedChat.future_key_fingerprint == 0 && encryptedChat.key_use_count_in >= 120) {
                requestNewSecretChatKey(encryptedChat);
            }
            long j4 = encryptedChat.exchange_id;
            long j5 = j2;
            if (j4 == 0) {
                message = null;
                if (encryptedChat.future_key_fingerprint != 0 && !z) {
                    encryptedChat.future_auth_key = new byte[256];
                    encryptedChat.future_key_fingerprint = 0L;
                    getMessagesStorage().updateEncryptedChat(encryptedChat);
                }
                if (tLObject instanceof TLRPC.TL_decryptedMessage) {
                    tL_decryptedMessage = (TLRPC.TL_decryptedMessage) tLObject;
                    tL_message_secret = new TLRPC.TL_message_secret();
                    tL_message_secret.ttl = tL_decryptedMessage.ttl;
                    tL_message_secret.entities = tL_decryptedMessage.entities;
                    tL_message_secret.message = tL_decryptedMessage.message;
                    tL_message_secret.date = i;
                    int newMessageId = getUserConfig().getNewMessageId();
                    tL_message_secret.id = newMessageId;
                    tL_message_secret.local_id = newMessageId;
                    tL_message_secret.silent = tL_decryptedMessage.silent;
                    getUserConfig().saveConfig(false);
                    TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                    tL_message_secret.from_id = tL_peerUser;
                    tL_peerUser.user_id = j5;
                    TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                    tL_message_secret.peer_id = tL_peerUser2;
                    tL_peerUser2.user_id = getUserConfig().getClientUserId();
                    tL_message_secret.random_id = tL_decryptedMessage.random_id;
                    tL_message_secret.unread = true;
                    tL_message_secret.flags = 768;
                    str = tL_decryptedMessage.via_bot_name;
                    if (str != null && str.length() > 0) {
                        tL_message_secret.via_bot_name = tL_decryptedMessage.via_bot_name;
                        tL_message_secret.flags |= 2048;
                    }
                    j = tL_decryptedMessage.grouped_id;
                    if (j != 0) {
                        tL_message_secret.grouped_id = j;
                        tL_message_secret.flags |= 131072;
                    }
                    tL_message_secret.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
                    if (tL_decryptedMessage.reply_to_random_id != 0) {
                        TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                        tL_message_secret.reply_to = tL_messageReplyHeader;
                        tL_messageReplyHeader.reply_to_random_id = tL_decryptedMessage.reply_to_random_id;
                        tL_message_secret.flags |= 8;
                    }
                    decryptedMessageMedia = tL_decryptedMessage.media;
                    if (decryptedMessageMedia != null || (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaEmpty)) {
                        tL_message_secret.media = new TLRPC.TL_messageMediaEmpty();
                    } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaWebPage) {
                        TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
                        tL_message_secret.media = tL_messageMediaWebPage;
                        tL_messageMediaWebPage.webpage = new TLRPC.TL_webPageUrlPending();
                        tL_message_secret.media.webpage.url = tL_decryptedMessage.media.url;
                    } else {
                        boolean z2 = decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaContact;
                        String str3 = _UrlKt.FRAGMENT_ENCODE_SET;
                        if (z2) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact = new TLRPC.TL_messageMediaContact();
                            tL_message_secret.media = tL_messageMediaContact;
                            TLRPC.DecryptedMessageMedia decryptedMessageMedia2 = tL_decryptedMessage.media;
                            tL_messageMediaContact.last_name = decryptedMessageMedia2.last_name;
                            tL_messageMediaContact.first_name = decryptedMessageMedia2.first_name;
                            tL_messageMediaContact.phone_number = decryptedMessageMedia2.phone_number;
                            tL_messageMediaContact.user_id = decryptedMessageMedia2.user_id;
                            tL_messageMediaContact.vcard = _UrlKt.FRAGMENT_ENCODE_SET;
                        } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaGeoPoint) {
                            TLRPC.TL_messageMediaGeo tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
                            tL_message_secret.media = tL_messageMediaGeo;
                            tL_messageMediaGeo.geo = new TLRPC.TL_geoPoint();
                            TLRPC.GeoPoint geoPoint = tL_message_secret.media.geo;
                            TLRPC.DecryptedMessageMedia decryptedMessageMedia3 = tL_decryptedMessage.media;
                            geoPoint.lat = decryptedMessageMedia3.lat;
                            geoPoint._long = decryptedMessageMedia3._long;
                        } else {
                            TLRPC.Message message2 = message;
                            if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaPhoto) {
                                byte[] bArr7 = decryptedMessageMedia.key;
                                if (bArr7 == null || bArr7.length != 32 || (bArr6 = decryptedMessageMedia.iv) == null || bArr6.length != 32) {
                                    return message2;
                                }
                                TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                                tL_message_secret.media = tL_messageMediaPhoto;
                                tL_messageMediaPhoto.flags |= 3;
                                if (TextUtils.isEmpty(tL_message_secret.message)) {
                                    String str4 = tL_decryptedMessage.media.caption;
                                    if (str4 != null) {
                                        str3 = str4;
                                    }
                                    tL_message_secret.message = str3;
                                }
                                tL_message_secret.media.photo = new TLRPC.TL_photo();
                                TLRPC.Photo photo = tL_message_secret.media.photo;
                                photo.file_reference = new byte[0];
                                photo.date = tL_message_secret.date;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia4 = tL_decryptedMessage.media;
                                byte[] bArr8 = ((TLRPC.TL_decryptedMessageMediaPhoto) decryptedMessageMedia4).thumb;
                                if (bArr8 != null && bArr8.length != 0 && bArr8.length <= 6000 && decryptedMessageMedia4.thumb_w <= 100 && decryptedMessageMedia4.thumb_h <= 100) {
                                    TLRPC.TL_photoCachedSize tL_photoCachedSize = new TLRPC.TL_photoCachedSize();
                                    TLRPC.DecryptedMessageMedia decryptedMessageMedia5 = tL_decryptedMessage.media;
                                    tL_photoCachedSize.w = decryptedMessageMedia5.thumb_w;
                                    tL_photoCachedSize.h = decryptedMessageMedia5.thumb_h;
                                    tL_photoCachedSize.bytes = bArr8;
                                    tL_photoCachedSize.type = "s";
                                    tL_photoCachedSize.location = new TLRPC.TL_fileLocationUnavailable();
                                    tL_message_secret.media.photo.sizes.add(tL_photoCachedSize);
                                }
                                int i7 = tL_message_secret.ttl;
                                if (i7 != 0) {
                                    TLRPC.MessageMedia messageMedia2 = tL_message_secret.media;
                                    messageMedia2.ttl_seconds = i7;
                                    messageMedia2.flags |= 4;
                                }
                                TLRPC.TL_photoSize_layer127 tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia6 = tL_decryptedMessage.media;
                                tL_photoSize_layer127.w = decryptedMessageMedia6.w;
                                tL_photoSize_layer127.h = decryptedMessageMedia6.h;
                                tL_photoSize_layer127.type = "x";
                                tL_photoSize_layer127.size = (int) encryptedFile.size;
                                TLRPC.TL_fileEncryptedLocation tL_fileEncryptedLocation = new TLRPC.TL_fileEncryptedLocation();
                                tL_photoSize_layer127.location = tL_fileEncryptedLocation;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia7 = tL_decryptedMessage.media;
                                tL_fileEncryptedLocation.key = decryptedMessageMedia7.key;
                                tL_fileEncryptedLocation.iv = decryptedMessageMedia7.iv;
                                tL_fileEncryptedLocation.dc_id = encryptedFile.dc_id;
                                tL_fileEncryptedLocation.volume_id = encryptedFile.id;
                                tL_fileEncryptedLocation.secret = encryptedFile.access_hash;
                                tL_fileEncryptedLocation.local_id = encryptedFile.key_fingerprint;
                                tL_message_secret.media.photo.sizes.add(tL_photoSize_layer127);
                            } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaVideo) {
                                byte[] bArr9 = decryptedMessageMedia.key;
                                if (bArr9 == null || bArr9.length != 32 || (bArr5 = decryptedMessageMedia.iv) == null || bArr5.length != 32) {
                                    return message2;
                                }
                                TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                                tL_message_secret.media = tL_messageMediaDocument;
                                tL_messageMediaDocument.flags |= 3;
                                tL_messageMediaDocument.document = new TLRPC.TL_documentEncrypted();
                                TLRPC.Document document = tL_message_secret.media.document;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia8 = tL_decryptedMessage.media;
                                document.key = decryptedMessageMedia8.key;
                                document.iv = decryptedMessageMedia8.iv;
                                document.dc_id = encryptedFile.dc_id;
                                if (TextUtils.isEmpty(tL_message_secret.message)) {
                                    String str5 = tL_decryptedMessage.media.caption;
                                    if (str5 != null) {
                                        str3 = str5;
                                    }
                                    tL_message_secret.message = str3;
                                }
                                TLRPC.Document document2 = tL_message_secret.media.document;
                                document2.date = i;
                                document2.size = encryptedFile.size;
                                document2.id = encryptedFile.id;
                                document2.access_hash = encryptedFile.access_hash;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia9 = tL_decryptedMessage.media;
                                String str6 = decryptedMessageMedia9.mime_type;
                                document2.mime_type = str6;
                                if (str6 == null) {
                                    document2.mime_type = "video/mp4";
                                }
                                byte[] bArr10 = ((TLRPC.TL_decryptedMessageMediaVideo) decryptedMessageMedia9).thumb;
                                if (bArr10 != null && bArr10.length != 0 && bArr10.length <= 6000 && decryptedMessageMedia9.thumb_w <= 100 && decryptedMessageMedia9.thumb_h <= 100) {
                                    tL_photoSizeEmpty2 = new TLRPC.TL_photoCachedSize();
                                    tL_photoSizeEmpty2.bytes = bArr10;
                                    TLRPC.DecryptedMessageMedia decryptedMessageMedia10 = tL_decryptedMessage.media;
                                    tL_photoSizeEmpty2.w = decryptedMessageMedia10.thumb_w;
                                    tL_photoSizeEmpty2.h = decryptedMessageMedia10.thumb_h;
                                    tL_photoSizeEmpty2.type = "s";
                                    tL_photoSizeEmpty2.location = new TLRPC.TL_fileLocationUnavailable();
                                } else {
                                    tL_photoSizeEmpty2 = new TLRPC.TL_photoSizeEmpty();
                                    tL_photoSizeEmpty2.type = "s";
                                }
                                tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty2);
                                tL_message_secret.media.document.flags |= 1;
                                TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer159 = new TLRPC.TL_documentAttributeVideo_layer159();
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia11 = tL_decryptedMessage.media;
                                tL_documentAttributeVideo_layer159.w = decryptedMessageMedia11.w;
                                tL_documentAttributeVideo_layer159.h = decryptedMessageMedia11.h;
                                tL_documentAttributeVideo_layer159.duration = decryptedMessageMedia11.duration;
                                tL_documentAttributeVideo_layer159.supports_streaming = false;
                                tL_message_secret.media.document.attributes.add(tL_documentAttributeVideo_layer159);
                                int i8 = tL_message_secret.ttl;
                                if (i8 != 0) {
                                    TLRPC.MessageMedia messageMedia3 = tL_message_secret.media;
                                    messageMedia3.ttl_seconds = i8;
                                    messageMedia3.flags |= 4;
                                }
                                if (i8 != 0) {
                                    tL_message_secret.ttl = Math.max(tL_decryptedMessage.media.duration + 1, i8);
                                }
                            } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaDocument) {
                                byte[] bArr11 = decryptedMessageMedia.key;
                                if (bArr11 == null || bArr11.length != 32 || (bArr4 = decryptedMessageMedia.iv) == null || bArr4.length != 32) {
                                    return message2;
                                }
                                TLRPC.TL_messageMediaDocument tL_messageMediaDocument2 = new TLRPC.TL_messageMediaDocument();
                                tL_message_secret.media = tL_messageMediaDocument2;
                                tL_messageMediaDocument2.flags |= 3;
                                if (TextUtils.isEmpty(tL_message_secret.message)) {
                                    String str7 = tL_decryptedMessage.media.caption;
                                    if (str7 == null) {
                                        str7 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    }
                                    tL_message_secret.message = str7;
                                }
                                tL_message_secret.media.document = new TLRPC.TL_documentEncrypted();
                                TLRPC.Document document3 = tL_message_secret.media.document;
                                document3.id = encryptedFile.id;
                                document3.access_hash = encryptedFile.access_hash;
                                document3.date = i;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia12 = tL_decryptedMessage.media;
                                document3.mime_type = decryptedMessageMedia12.mime_type;
                                if (decryptedMessageMedia12 instanceof TLRPC.TL_decryptedMessageMediaDocument_layer8) {
                                    TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                                    tL_documentAttributeFilename.file_name = tL_decryptedMessage.media.file_name;
                                    tL_message_secret.media.document.attributes.add(tL_documentAttributeFilename);
                                } else {
                                    document3.attributes = decryptedMessageMedia12.attributes;
                                }
                                if (tL_message_secret.ttl > 0) {
                                    int size = tL_message_secret.media.document.attributes.size();
                                    for (int i9 = 0; i9 < size; i9++) {
                                        TLRPC.DocumentAttribute documentAttribute = tL_message_secret.media.document.attributes.get(i9);
                                        if ((documentAttribute instanceof TLRPC.TL_documentAttributeAudio) || (documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                                            tL_message_secret.ttl = (int) Math.max(documentAttribute.duration + 1.0d, tL_message_secret.ttl);
                                            break;
                                        }
                                    }
                                    tL_message_secret.ttl = Math.max(tL_decryptedMessage.media.duration + 1, tL_message_secret.ttl);
                                } else {
                                    j3 = 0;
                                }
                                TLRPC.Document document4 = tL_message_secret.media.document;
                                long j6 = tL_decryptedMessage.media.size;
                                document4.size = j6 != j3 ? Math.min(j6, encryptedFile.size) : encryptedFile.size;
                                TLRPC.Document document5 = tL_message_secret.media.document;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia13 = tL_decryptedMessage.media;
                                document5.key = decryptedMessageMedia13.key;
                                document5.iv = decryptedMessageMedia13.iv;
                                String str8 = document5.mime_type;
                                if (str8 == null) {
                                    document5.mime_type = _UrlKt.FRAGMENT_ENCODE_SET;
                                } else if ("application/x-tgsticker".equals(str8) || "application/x-tgsdice".equals(tL_message_secret.media.document.mime_type)) {
                                    tL_message_secret.media.document.mime_type = "application/x-bad_tgsticker";
                                }
                                byte[] bArr12 = ((TLRPC.TL_decryptedMessageMediaDocument) tL_decryptedMessage.media).thumb;
                                if (bArr12 != null && bArr12.length != 0 && bArr12.length <= 20000) {
                                    tL_photoSizeEmpty = new TLRPC.TL_photoCachedSize();
                                    tL_photoSizeEmpty.bytes = bArr12;
                                    TLRPC.DecryptedMessageMedia decryptedMessageMedia14 = tL_decryptedMessage.media;
                                    tL_photoSizeEmpty.w = decryptedMessageMedia14.thumb_w;
                                    tL_photoSizeEmpty.h = decryptedMessageMedia14.thumb_h;
                                    tL_photoSizeEmpty.type = "s";
                                    tL_photoSizeEmpty.location = new TLRPC.TL_fileLocationUnavailable();
                                } else {
                                    tL_photoSizeEmpty = new TLRPC.TL_photoSizeEmpty();
                                    tL_photoSizeEmpty.type = "s";
                                }
                                tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty);
                                TLRPC.Document document6 = tL_message_secret.media.document;
                                document6.flags |= 1;
                                document6.dc_id = encryptedFile.dc_id;
                                if (MessageObject.isVoiceMessage(tL_message_secret) || MessageObject.isRoundVideoMessage(tL_message_secret)) {
                                    tL_message_secret.media_unread = true;
                                }
                            } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaExternalDocument) {
                                TLRPC.TL_messageMediaDocument tL_messageMediaDocument3 = new TLRPC.TL_messageMediaDocument();
                                tL_message_secret.media = tL_messageMediaDocument3;
                                tL_messageMediaDocument3.flags |= 3;
                                tL_message_secret.message = _UrlKt.FRAGMENT_ENCODE_SET;
                                tL_messageMediaDocument3.document = new TLRPC.TL_document();
                                TLRPC.Document document7 = tL_message_secret.media.document;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia15 = tL_decryptedMessage.media;
                                document7.id = decryptedMessageMedia15.id;
                                document7.access_hash = decryptedMessageMedia15.access_hash;
                                document7.file_reference = new byte[0];
                                document7.date = decryptedMessageMedia15.date;
                                document7.attributes = decryptedMessageMedia15.attributes;
                                document7.mime_type = decryptedMessageMedia15.mime_type;
                                document7.dc_id = decryptedMessageMedia15.dc_id;
                                document7.size = decryptedMessageMedia15.size;
                                document7.thumbs.add(((TLRPC.TL_decryptedMessageMediaExternalDocument) decryptedMessageMedia15).thumb);
                                TLRPC.Document document8 = tL_message_secret.media.document;
                                document8.flags |= 1;
                                if (document8.mime_type == null) {
                                    document8.mime_type = _UrlKt.FRAGMENT_ENCODE_SET;
                                }
                                if (MessageObject.isAnimatedStickerMessage(tL_message_secret)) {
                                    tL_message_secret.stickerVerified = 0;
                                    getMediaDataController().verifyAnimatedStickerMessage(tL_message_secret, true);
                                }
                            } else if (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaAudio) {
                                byte[] bArr13 = decryptedMessageMedia.key;
                                if (bArr13 == null || bArr13.length != 32 || (bArr3 = decryptedMessageMedia.iv) == null || bArr3.length != 32) {
                                    return message2;
                                }
                                TLRPC.TL_messageMediaDocument tL_messageMediaDocument4 = new TLRPC.TL_messageMediaDocument();
                                tL_message_secret.media = tL_messageMediaDocument4;
                                tL_messageMediaDocument4.flags |= 3;
                                tL_messageMediaDocument4.document = new TLRPC.TL_documentEncrypted();
                                TLRPC.Document document9 = tL_message_secret.media.document;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia16 = tL_decryptedMessage.media;
                                document9.key = decryptedMessageMedia16.key;
                                document9.iv = decryptedMessageMedia16.iv;
                                document9.id = encryptedFile.id;
                                document9.access_hash = encryptedFile.access_hash;
                                document9.date = i;
                                document9.size = encryptedFile.size;
                                document9.dc_id = encryptedFile.dc_id;
                                document9.mime_type = decryptedMessageMedia16.mime_type;
                                if (TextUtils.isEmpty(tL_message_secret.message)) {
                                    String str9 = tL_decryptedMessage.media.caption;
                                    if (str9 != null) {
                                        str3 = str9;
                                    }
                                    tL_message_secret.message = str3;
                                }
                                TLRPC.Document document10 = tL_message_secret.media.document;
                                if (document10.mime_type == null) {
                                    document10.mime_type = "audio/ogg";
                                }
                                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                                tL_documentAttributeAudio.duration = tL_decryptedMessage.media.duration;
                                tL_documentAttributeAudio.voice = true;
                                tL_message_secret.media.document.attributes.add(tL_documentAttributeAudio);
                                int i10 = tL_message_secret.ttl;
                                if (i10 != 0) {
                                    tL_message_secret.ttl = Math.max(tL_decryptedMessage.media.duration + 1, i10);
                                }
                                if (tL_message_secret.media.document.thumbs.isEmpty()) {
                                    TLRPC.TL_photoSizeEmpty tL_photoSizeEmpty3 = new TLRPC.TL_photoSizeEmpty();
                                    tL_photoSizeEmpty3.type = "s";
                                    tL_message_secret.media.document.thumbs.add(tL_photoSizeEmpty3);
                                }
                            } else {
                                if (!(decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaVenue)) {
                                    return message2;
                                }
                                TLRPC.TL_messageMediaVenue tL_messageMediaVenue = new TLRPC.TL_messageMediaVenue();
                                tL_message_secret.media = tL_messageMediaVenue;
                                tL_messageMediaVenue.geo = new TLRPC.TL_geoPoint();
                                TLRPC.MessageMedia messageMedia4 = tL_message_secret.media;
                                TLRPC.GeoPoint geoPoint2 = messageMedia4.geo;
                                TLRPC.DecryptedMessageMedia decryptedMessageMedia17 = tL_decryptedMessage.media;
                                geoPoint2.lat = decryptedMessageMedia17.lat;
                                geoPoint2._long = decryptedMessageMedia17._long;
                                messageMedia4.title = decryptedMessageMedia17.title;
                                messageMedia4.address = decryptedMessageMedia17.address;
                                messageMedia4.provider = decryptedMessageMedia17.provider;
                                messageMedia4.venue_id = decryptedMessageMedia17.venue_id;
                                messageMedia4.venue_type = _UrlKt.FRAGMENT_ENCODE_SET;
                            }
                        }
                    }
                    i6 = tL_message_secret.ttl;
                    if (i6 != 0) {
                        messageMedia = tL_message_secret.media;
                        if (messageMedia.ttl_seconds == 0) {
                            messageMedia.ttl_seconds = i6;
                            messageMedia.flags |= 4;
                        }
                    }
                    str2 = tL_message_secret.message;
                    if (str2 != null) {
                        tL_message_secret.message = str2.replace((char) 8238, ' ');
                    }
                    return tL_message_secret;
                }
                TLRPC.Message message3 = message;
                if (tLObject instanceof TLRPC.TL_decryptedMessageService) {
                    tL_decryptedMessageService = (TLRPC.TL_decryptedMessageService) tLObject;
                    decryptedMessageAction = tL_decryptedMessageService.action;
                    if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) || (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages)) {
                        tL_messageService = new TLRPC.TL_messageService();
                        if (tL_decryptedMessageService.action instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                            TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction = new TLRPC.TL_messageEncryptedAction();
                            tL_messageService.action = tL_messageEncryptedAction;
                            decryptedMessageAction2 = tL_decryptedMessageService.action;
                            i2 = decryptedMessageAction2.ttl_seconds;
                            if (i2 >= 0 || i2 > 31536000) {
                                decryptedMessageAction2.ttl_seconds = 31536000;
                            }
                            encryptedChat.ttl = decryptedMessageAction2.ttl_seconds;
                            tL_messageEncryptedAction.encryptedAction = decryptedMessageAction2;
                            getMessagesStorage().updateEncryptedChatTTL(encryptedChat);
                        } else {
                            TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction2 = new TLRPC.TL_messageEncryptedAction();
                            tL_messageService.action = tL_messageEncryptedAction2;
                            tL_messageEncryptedAction2.encryptedAction = tL_decryptedMessageService.action;
                        }
                        int newMessageId2 = getUserConfig().getNewMessageId();
                        tL_messageService.id = newMessageId2;
                        tL_messageService.local_id = newMessageId2;
                        getUserConfig().saveConfig(false);
                        tL_messageService.unread = true;
                        tL_messageService.flags = 256;
                        tL_messageService.date = i;
                        TLRPC.TL_peerUser tL_peerUser3 = new TLRPC.TL_peerUser();
                        tL_messageService.from_id = tL_peerUser3;
                        tL_peerUser3.user_id = j5;
                        TLRPC.TL_peerUser tL_peerUser4 = new TLRPC.TL_peerUser();
                        tL_messageService.peer_id = tL_peerUser4;
                        tL_peerUser4.user_id = getUserConfig().getClientUserId();
                        tL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
                        return tL_messageService;
                    }
                    if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
                        final long jMakeEncryptedDialogId = DialogObject.makeEncryptedDialogId(encryptedChat.id);
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda13
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$processDecryptedObject$17(jMakeEncryptedDialogId);
                            }
                        });
                        return message3;
                    }
                    if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
                        if (!decryptedMessageAction.random_ids.isEmpty()) {
                            this.pendingEncMessagesToDelete.addAll(tL_decryptedMessageService.action.random_ids);
                        }
                        return message3;
                    }
                    if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
                        if (decryptedMessageAction.random_ids.isEmpty()) {
                            return null;
                        }
                        int currentTime = getConnectionsManager().getCurrentTime();
                        getMessagesStorage().createTaskForSecretChat(encryptedChat.id, currentTime, currentTime, 1, tL_decryptedMessageService.action.random_ids);
                        return null;
                    }
                    if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                        applyPeerLayer(encryptedChat, decryptedMessageAction.layer);
                        return null;
                    }
                    if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                        long j7 = encryptedChat.exchange_id;
                        if (j7 != 0) {
                            if (j7 > decryptedMessageAction.exchange_id) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("we already have request key with higher exchange_id");
                                }
                                return message3;
                            }
                            sendAbortKeyMessage(encryptedChat, message3, j7);
                        }
                        byte[] bArr14 = new byte[256];
                        Utilities.random.nextBytes(bArr14);
                        BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
                        BigInteger bigIntegerModPow = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr14), bigInteger);
                        BigInteger bigInteger2 = new BigInteger(1, tL_decryptedMessageService.action.g_a);
                        if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                            sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        byte[] byteArray = bigIntegerModPow.toByteArray();
                        if (byteArray.length > 256) {
                            byte[] bArr15 = new byte[256];
                            System.arraycopy(byteArray, 1, bArr15, 0, 256);
                            byteArray = bArr15;
                        }
                        byte[] byteArray2 = bigInteger2.modPow(new BigInteger(1, bArr14), bigInteger).toByteArray();
                        if (byteArray2.length > 256) {
                            bArr2 = new byte[256];
                            System.arraycopy(byteArray2, byteArray2.length - 256, bArr2, 0, 256);
                        } else {
                            if (byteArray2.length < 256) {
                                bArr2 = new byte[256];
                                System.arraycopy(byteArray2, 0, bArr2, 256 - byteArray2.length, byteArray2.length);
                                for (int i11 = 0; i11 < 256 - byteArray2.length; i11++) {
                                    bArr2[i11] = 0;
                                }
                            }
                            byte[] bArrComputeSHA1 = Utilities.computeSHA1(byteArray2);
                            byte[] bArr16 = new byte[8];
                            System.arraycopy(bArrComputeSHA1, bArrComputeSHA1.length - 8, bArr16, 0, 8);
                            encryptedChat.exchange_id = tL_decryptedMessageService.action.exchange_id;
                            encryptedChat.future_auth_key = byteArray2;
                            encryptedChat.future_key_fingerprint = Utilities.bytesToLong(bArr16);
                            encryptedChat.g_a_or_b = byteArray;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            sendAcceptKeyMessage(encryptedChat, null);
                            return null;
                        }
                        byteArray2 = bArr2;
                        byte[] bArrComputeSHA2 = Utilities.computeSHA1(byteArray2);
                        byte[] bArr17 = new byte[8];
                        System.arraycopy(bArrComputeSHA2, bArrComputeSHA2.length - 8, bArr17, 0, 8);
                        encryptedChat.exchange_id = tL_decryptedMessageService.action.exchange_id;
                        encryptedChat.future_auth_key = byteArray2;
                        encryptedChat.future_key_fingerprint = Utilities.bytesToLong(bArr17);
                        encryptedChat.g_a_or_b = byteArray;
                        getMessagesStorage().updateEncryptedChat(encryptedChat);
                        sendAcceptKeyMessage(encryptedChat, null);
                        return null;
                    }
                    if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAcceptKey)) {
                        if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                            if (encryptedChat.exchange_id == decryptedMessageAction.exchange_id) {
                                long j8 = encryptedChat.future_key_fingerprint;
                                if (j8 == decryptedMessageAction.key_fingerprint) {
                                    long j9 = encryptedChat.key_fingerprint;
                                    byte[] bArr18 = encryptedChat.auth_key;
                                    encryptedChat.key_fingerprint = j8;
                                    encryptedChat.auth_key = encryptedChat.future_auth_key;
                                    encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                                    encryptedChat.future_auth_key = bArr18;
                                    encryptedChat.future_key_fingerprint = j9;
                                    encryptedChat.key_use_count_in = (short) 0;
                                    encryptedChat.key_use_count_out = (short) 0;
                                    encryptedChat.exchange_id = 0L;
                                    getMessagesStorage().updateEncryptedChat(encryptedChat);
                                    sendNoopMessage(encryptedChat, null);
                                    return null;
                                }
                            }
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                            if (encryptedChat.exchange_id != decryptedMessageAction.exchange_id) {
                                return null;
                            }
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            return null;
                        }
                        if ((decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNoop) || !(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionResend) || (i3 = decryptedMessageAction.end_seq_no) < (i4 = encryptedChat.in_seq_no) || i3 < (i5 = decryptedMessageAction.start_seq_no)) {
                            return null;
                        }
                        if (i5 < i4) {
                            decryptedMessageAction.start_seq_no = i4;
                        }
                        resendMessages(decryptedMessageAction.start_seq_no, i3, encryptedChat);
                        return null;
                    }
                    if (encryptedChat.exchange_id == decryptedMessageAction.exchange_id) {
                        BigInteger bigInteger3 = new BigInteger(1, getMessagesStorage().getSecretPBytes());
                        BigInteger bigInteger4 = new BigInteger(1, tL_decryptedMessageService.action.g_b);
                        if (!Utilities.isGoodGaAndGb(bigInteger4, bigInteger3)) {
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        byte[] byteArray3 = bigInteger4.modPow(new BigInteger(1, encryptedChat.a_or_b), bigInteger3).toByteArray();
                        if (byteArray3.length > 256) {
                            bArr = new byte[256];
                            System.arraycopy(byteArray3, byteArray3.length - 256, bArr, 0, 256);
                        } else {
                            if (byteArray3.length < 256) {
                                bArr = new byte[256];
                                System.arraycopy(byteArray3, 0, bArr, 256 - byteArray3.length, byteArray3.length);
                                for (int i12 = 0; i12 < 256 - byteArray3.length; i12++) {
                                    bArr[i12] = 0;
                                }
                            }
                            byte[] bArrComputeSHA3 = Utilities.computeSHA1(byteArray3);
                            byte[] bArr19 = new byte[8];
                            System.arraycopy(bArrComputeSHA3, bArrComputeSHA3.length - 8, bArr19, 0, 8);
                            jBytesToLong = Utilities.bytesToLong(bArr19);
                            if (tL_decryptedMessageService.action.key_fingerprint == jBytesToLong) {
                                encryptedChat.future_auth_key = byteArray3;
                                encryptedChat.future_key_fingerprint = jBytesToLong;
                                getMessagesStorage().updateEncryptedChat(encryptedChat);
                                sendCommitKeyMessage(encryptedChat, null);
                                return null;
                            }
                            encryptedChat.future_auth_key = new byte[256];
                            encryptedChat.future_key_fingerprint = 0L;
                            encryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        byteArray3 = bArr;
                        byte[] bArrComputeSHA4 = Utilities.computeSHA1(byteArray3);
                        byte[] bArr110 = new byte[8];
                        System.arraycopy(bArrComputeSHA4, bArrComputeSHA4.length - 8, bArr110, 0, 8);
                        jBytesToLong = Utilities.bytesToLong(bArr110);
                        if (tL_decryptedMessageService.action.key_fingerprint == jBytesToLong) {
                            encryptedChat.future_auth_key = byteArray3;
                            encryptedChat.future_key_fingerprint = jBytesToLong;
                            getMessagesStorage().updateEncryptedChat(encryptedChat);
                            sendCommitKeyMessage(encryptedChat, null);
                            return null;
                        }
                        encryptedChat.future_auth_key = new byte[256];
                        encryptedChat.future_key_fingerprint = 0L;
                        encryptedChat.exchange_id = 0L;
                        getMessagesStorage().updateEncryptedChat(encryptedChat);
                        sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                        return null;
                    }
                    encryptedChat.future_auth_key = new byte[256];
                    encryptedChat.future_key_fingerprint = 0L;
                    encryptedChat.exchange_id = 0L;
                    getMessagesStorage().updateEncryptedChat(encryptedChat);
                    sendAbortKeyMessage(encryptedChat, null, tL_decryptedMessageService.action.exchange_id);
                    return null;
                }
                if (BuildVars.LOGS_ENABLED) {
                    return null;
                }
                FileLog.e("unknown message " + tLObject);
                return null;
            }
            message = null;
            if (j4 != 0 && z) {
                encryptedChat.key_fingerprint = encryptedChat.future_key_fingerprint;
                encryptedChat.auth_key = encryptedChat.future_auth_key;
                encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                encryptedChat.future_auth_key = new byte[256];
                encryptedChat.future_key_fingerprint = 0L;
                encryptedChat.key_use_count_in = (short) 0;
                encryptedChat.key_use_count_out = (short) 0;
                encryptedChat.exchange_id = 0L;
                getMessagesStorage().updateEncryptedChat(encryptedChat);
            }
            if (tLObject instanceof TLRPC.TL_decryptedMessage) {
                tL_decryptedMessage = (TLRPC.TL_decryptedMessage) tLObject;
                tL_message_secret = new TLRPC.TL_message_secret();
                tL_message_secret.ttl = tL_decryptedMessage.ttl;
                tL_message_secret.entities = tL_decryptedMessage.entities;
                tL_message_secret.message = tL_decryptedMessage.message;
                tL_message_secret.date = i;
                int newMessageId3 = getUserConfig().getNewMessageId();
                tL_message_secret.id = newMessageId3;
                tL_message_secret.local_id = newMessageId3;
                tL_message_secret.silent = tL_decryptedMessage.silent;
                getUserConfig().saveConfig(false);
                TLRPC.TL_peerUser tL_peerUser5 = new TLRPC.TL_peerUser();
                tL_message_secret.from_id = tL_peerUser5;
                tL_peerUser5.user_id = j5;
                TLRPC.TL_peerUser tL_peerUser6 = new TLRPC.TL_peerUser();
                tL_message_secret.peer_id = tL_peerUser6;
                tL_peerUser6.user_id = getUserConfig().getClientUserId();
                tL_message_secret.random_id = tL_decryptedMessage.random_id;
                tL_message_secret.unread = true;
                tL_message_secret.flags = 768;
                str = tL_decryptedMessage.via_bot_name;
                if (str != null) {
                    tL_message_secret.via_bot_name = tL_decryptedMessage.via_bot_name;
                    tL_message_secret.flags |= 2048;
                }
                j = tL_decryptedMessage.grouped_id;
                if (j != 0) {
                    tL_message_secret.grouped_id = j;
                    tL_message_secret.flags |= 131072;
                }
                tL_message_secret.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
                if (tL_decryptedMessage.reply_to_random_id != 0) {
                    TLRPC.TL_messageReplyHeader tL_messageReplyHeader2 = new TLRPC.TL_messageReplyHeader();
                    tL_message_secret.reply_to = tL_messageReplyHeader2;
                    tL_messageReplyHeader2.reply_to_random_id = tL_decryptedMessage.reply_to_random_id;
                    tL_message_secret.flags |= 8;
                }
                decryptedMessageMedia = tL_decryptedMessage.media;
                if (decryptedMessageMedia != null) {
                    tL_message_secret.media = new TLRPC.TL_messageMediaEmpty();
                } else {
                    tL_message_secret.media = new TLRPC.TL_messageMediaEmpty();
                }
                i6 = tL_message_secret.ttl;
                if (i6 != 0) {
                    messageMedia = tL_message_secret.media;
                    if (messageMedia.ttl_seconds == 0) {
                        messageMedia.ttl_seconds = i6;
                        messageMedia.flags |= 4;
                    }
                }
                str2 = tL_message_secret.message;
                if (str2 != null) {
                    tL_message_secret.message = str2.replace((char) 8238, ' ');
                }
                return tL_message_secret;
            }
            TLRPC.Message message4 = message;
            if (tLObject instanceof TLRPC.TL_decryptedMessageService) {
                tL_decryptedMessageService = (TLRPC.TL_decryptedMessageService) tLObject;
                decryptedMessageAction = tL_decryptedMessageService.action;
                if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                }
                tL_messageService = new TLRPC.TL_messageService();
                if (tL_decryptedMessageService.action instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                    TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction3 = new TLRPC.TL_messageEncryptedAction();
                    tL_messageService.action = tL_messageEncryptedAction3;
                    decryptedMessageAction2 = tL_decryptedMessageService.action;
                    i2 = decryptedMessageAction2.ttl_seconds;
                    if (i2 >= 0) {
                        decryptedMessageAction2.ttl_seconds = 31536000;
                    } else {
                        decryptedMessageAction2.ttl_seconds = 31536000;
                    }
                    encryptedChat.ttl = decryptedMessageAction2.ttl_seconds;
                    tL_messageEncryptedAction3.encryptedAction = decryptedMessageAction2;
                    getMessagesStorage().updateEncryptedChatTTL(encryptedChat);
                } else {
                    TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction4 = new TLRPC.TL_messageEncryptedAction();
                    tL_messageService.action = tL_messageEncryptedAction4;
                    tL_messageEncryptedAction4.encryptedAction = tL_decryptedMessageService.action;
                }
                int newMessageId4 = getUserConfig().getNewMessageId();
                tL_messageService.id = newMessageId4;
                tL_messageService.local_id = newMessageId4;
                getUserConfig().saveConfig(false);
                tL_messageService.unread = true;
                tL_messageService.flags = 256;
                tL_messageService.date = i;
                TLRPC.TL_peerUser tL_peerUser7 = new TLRPC.TL_peerUser();
                tL_messageService.from_id = tL_peerUser7;
                tL_peerUser7.user_id = j5;
                TLRPC.TL_peerUser tL_peerUser8 = new TLRPC.TL_peerUser();
                tL_messageService.peer_id = tL_peerUser8;
                tL_peerUser8.user_id = getUserConfig().getClientUserId();
                tL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
                return tL_messageService;
            }
            if (BuildVars.LOGS_ENABLED) {
                return null;
            }
            FileLog.e("unknown message " + tLObject);
            return null;
        }
        if (!BuildVars.LOGS_ENABLED) {
            return null;
        }
        FileLog.e("unknown TLObject");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$17(final long j) {
        TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(j);
        if (dialog != null) {
            dialog.unread_count = 0;
            getMessagesController().dialogMessage.remove(dialog.id);
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDecryptedObject$12(j);
            }
        });
        final Runnable runnable = new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDecryptedObject$14(j);
            }
        };
        if (AyuConfig.saveDeletedMessages) {
            getAyuMessagesController().onHistoryFlushed(dialog, new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processDecryptedObject$16(runnable, j);
                }
            });
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$12(final long j) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDecryptedObject$11(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$11(long j) {
        getNotificationsController().processReadMessages(null, j, 0, Integer.MAX_VALUE, false);
        LongSparseIntArray longSparseIntArray = new LongSparseIntArray(1);
        longSparseIntArray.put(j, 0);
        getNotificationsController().processDialogsUpdateRead(longSparseIntArray);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$14(final long j) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDecryptedObject$13(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$13(long j) {
        getMessagesStorage().deleteDialog(j, 1);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(j), Boolean.FALSE, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$16(Runnable runnable, final long j) {
        runnable.run();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processDecryptedObject$15(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$15(long j) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(AyuConstants.HISTORY_FLUSHED_NOTIFICATION, Long.valueOf(j));
    }

    private TLRPC.Message createDeleteMessage(int i, int i2, int i3, long j, TLRPC.EncryptedChat encryptedChat) {
        TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
        TLRPC.TL_messageEncryptedAction tL_messageEncryptedAction = new TLRPC.TL_messageEncryptedAction();
        tL_messageService.action = tL_messageEncryptedAction;
        tL_messageEncryptedAction.encryptedAction = new TLRPC.TL_decryptedMessageActionDeleteMessages();
        tL_messageService.action.encryptedAction.random_ids.add(Long.valueOf(j));
        tL_messageService.id = i;
        tL_messageService.local_id = i;
        TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
        tL_messageService.from_id = tL_peerUser;
        tL_peerUser.user_id = getUserConfig().getClientUserId();
        tL_messageService.unread = true;
        tL_messageService.out = true;
        tL_messageService.flags = 256;
        tL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
        tL_messageService.send_state = 1;
        tL_messageService.seq_in = i3;
        tL_messageService.seq_out = i2;
        tL_messageService.peer_id = new TLRPC.TL_peerUser();
        if (encryptedChat.participant_id == getUserConfig().getClientUserId()) {
            tL_messageService.peer_id.user_id = encryptedChat.admin_id;
        } else {
            tL_messageService.peer_id.user_id = encryptedChat.participant_id;
        }
        tL_messageService.date = 0;
        tL_messageService.random_id = j;
        return tL_messageService;
    }

    private void resendMessages(final int i, final int i2, final TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat == null || i2 - i < 0) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$resendMessages$20(i, encryptedChat, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r16v1, types: [org.telegram.messenger.SendMessagesHelper] */
    /* JADX WARN: Type inference failed for: r1v17, types: [org.telegram.tgnet.InputSerializedData, org.telegram.tgnet.NativeByteBuffer] */
    /* JADX WARN: Type inference failed for: r2v11, types: [org.telegram.tgnet.TLRPC$Message] */
    /* JADX WARN: Type inference failed for: r2v12, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v14, types: [org.telegram.tgnet.TLRPC$Message] */
    /* JADX WARN: Type inference failed for: r8v3, types: [org.telegram.SQLite.SQLiteCursor] */
    public /* synthetic */ void lambda$resendMessages$20(int i, TLRPC.EncryptedChat encryptedChat, int i2) {
        ?? CreateDeleteMessage;
        TLRPC.EncryptedChat encryptedChat2 = encryptedChat;
        try {
            int i3 = (encryptedChat2.admin_id == getUserConfig().getClientUserId() && i % 2 == 0) ? i + 1 : i;
            int i4 = 5;
            ?? r12 = 0;
            int i5 = 1;
            int i6 = 2;
            SQLiteCursor sQLiteCursorQueryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT uid FROM requested_holes WHERE uid = %d AND ((seq_out_start >= %d AND %d <= seq_out_end) OR (seq_out_start >= %d AND %d <= seq_out_end))", Integer.valueOf(encryptedChat2.id), Integer.valueOf(i3), Integer.valueOf(i3), Integer.valueOf(i2), Integer.valueOf(i2)), new Object[0]);
            boolean next = sQLiteCursorQueryFinalized.next();
            sQLiteCursorQueryFinalized.dispose();
            if (next) {
                return;
            }
            long jMakeEncryptedDialogId = DialogObject.makeEncryptedDialogId(encryptedChat2.id);
            SparseArray sparseArray = new SparseArray();
            final ArrayList arrayList = new ArrayList();
            for (int i7 = i3; i7 <= i2; i7 += 2) {
                sparseArray.put(i7, null);
            }
            ?? QueryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, r.random_id, s.seq_in, s.seq_out, m.ttl, s.mid FROM messages_seq as s LEFT JOIN randoms_v2 as r ON r.mid = s.mid LEFT JOIN messages_v2 as m ON m.mid = s.mid WHERE m.uid = %d AND m.out = 1 AND s.seq_out >= %d AND s.seq_out <= %d ORDER BY seq_out ASC", Long.valueOf(jMakeEncryptedDialogId), Integer.valueOf(i3), Integer.valueOf(i2)), new Object[0]);
            while (QueryFinalized.next()) {
                long jLongValue = QueryFinalized.longValue(i5);
                if (jLongValue == 0) {
                    jLongValue = Utilities.random.nextLong();
                }
                long j = jLongValue;
                int iIntValue = QueryFinalized.intValue(i6);
                int iIntValue2 = QueryFinalized.intValue(3);
                int i8 = i5;
                int i9 = i6;
                long j2 = jMakeEncryptedDialogId;
                int iIntValue3 = QueryFinalized.intValue(i4);
                ?? ByteBufferValue = QueryFinalized.byteBufferValue(r12);
                if (ByteBufferValue != 0) {
                    CreateDeleteMessage = TLRPC.Message.TLdeserialize(ByteBufferValue, ByteBufferValue.readInt32(r12), r12);
                    CreateDeleteMessage.readAttachPath(ByteBufferValue, getUserConfig().clientUserId);
                    ByteBufferValue.reuse();
                    CreateDeleteMessage.random_id = j;
                    j2 = j2;
                    CreateDeleteMessage.dialog_id = j2;
                    CreateDeleteMessage.seq_in = iIntValue;
                    CreateDeleteMessage.seq_out = iIntValue2;
                    CreateDeleteMessage.ttl = QueryFinalized.intValue(4);
                } else {
                    CreateDeleteMessage = createDeleteMessage(iIntValue3, iIntValue2, iIntValue, j, encryptedChat2);
                }
                arrayList.add(CreateDeleteMessage);
                sparseArray.remove(iIntValue2);
                encryptedChat2 = encryptedChat;
                jMakeEncryptedDialogId = j2;
                i5 = i8;
                i6 = i9;
                i4 = 5;
                r12 = 0;
            }
            int i10 = i5;
            int i11 = i6;
            QueryFinalized.dispose();
            if (sparseArray.size() != 0) {
                for (int i12 = 0; i12 < sparseArray.size(); i12++) {
                    int iKeyAt = sparseArray.keyAt(i12);
                    arrayList.add(createDeleteMessage(getUserConfig().getNewMessageId(), iKeyAt, iKeyAt + 1, Utilities.random.nextLong(), encryptedChat));
                }
                getUserConfig().saveConfig(false);
            }
            Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda20
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return AndroidUtilities.compare(((TLRPC.Message) obj).seq_out, ((TLRPC.Message) obj2).seq_out);
                }
            });
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(encryptedChat);
            try {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda21
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$resendMessages$19(arrayList);
                    }
                });
                getSendMessagesHelper().processUnsentMessages(arrayList, null, new ArrayList(), new ArrayList(), arrayList2);
                SQLiteDatabase database = getMessagesStorage().getDatabase();
                Locale locale = Locale.US;
                Integer numValueOf = Integer.valueOf(encryptedChat.id);
                Integer numValueOf2 = Integer.valueOf(i3);
                Integer numValueOf3 = Integer.valueOf(i2);
                Object[] objArr = new Object[3];
                objArr[0] = numValueOf;
                objArr[i10] = numValueOf2;
                objArr[i11] = numValueOf3;
                database.executeFast(String.format(locale, "REPLACE INTO requested_holes VALUES(%d, %d, %d)", objArr)).stepThis().dispose();
                return;
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
        }
        FileLog.e(e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resendMessages$19(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) arrayList.get(i), false, true);
            messageObject.resendAsIs = true;
            getSendMessagesHelper().retrySendMessage(messageObject, true, 0L);
        }
    }

    public void checkSecretHoles(TLRPC.EncryptedChat encryptedChat, ArrayList<TLRPC.Message> arrayList) {
        TL_decryptedMessageHolder tL_decryptedMessageHolder;
        TLRPC.TL_decryptedMessageLayer tL_decryptedMessageLayer;
        int i;
        int i2;
        ArrayList<TL_decryptedMessageHolder> arrayList2 = this.secretHolesQueue.get(encryptedChat.id);
        if (arrayList2 == null) {
            return;
        }
        Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda5
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return SecretChatHelper.$r8$lambda$6qPIqA8Z4xXy06MOXDU74TyVkII((SecretChatHelper.TL_decryptedMessageHolder) obj, (SecretChatHelper.TL_decryptedMessageHolder) obj2);
            }
        });
        boolean z = false;
        while (arrayList2.size() > 0 && ((i = (tL_decryptedMessageLayer = (tL_decryptedMessageHolder = arrayList2.get(0)).layer).out_seq_no) == (i2 = encryptedChat.seq_in) || i2 == i - 2)) {
            applyPeerLayer(encryptedChat, tL_decryptedMessageLayer.layer);
            TLRPC.TL_decryptedMessageLayer tL_decryptedMessageLayer2 = tL_decryptedMessageHolder.layer;
            encryptedChat.seq_in = tL_decryptedMessageLayer2.out_seq_no;
            encryptedChat.in_seq_no = tL_decryptedMessageLayer2.in_seq_no;
            arrayList2.remove(0);
            if (tL_decryptedMessageHolder.decryptedWithVersion == 2) {
                encryptedChat.mtproto_seq = Math.min(encryptedChat.mtproto_seq, encryptedChat.seq_in);
            }
            TLRPC.EncryptedChat encryptedChat2 = encryptedChat;
            TLRPC.Message messageProcessDecryptedObject = processDecryptedObject(encryptedChat2, tL_decryptedMessageHolder.file, tL_decryptedMessageHolder.date, tL_decryptedMessageHolder.layer.message, tL_decryptedMessageHolder.new_key_used);
            if (messageProcessDecryptedObject != null) {
                arrayList.add(messageProcessDecryptedObject);
            }
            z = true;
            encryptedChat = encryptedChat2;
        }
        TLRPC.EncryptedChat encryptedChat3 = encryptedChat;
        if (arrayList2.isEmpty()) {
            this.secretHolesQueue.remove(encryptedChat3.id);
        }
        if (z) {
            getMessagesStorage().updateEncryptedChatSeq(encryptedChat3, true);
        }
    }

    public static /* synthetic */ int $r8$lambda$6qPIqA8Z4xXy06MOXDU74TyVkII(TL_decryptedMessageHolder tL_decryptedMessageHolder, TL_decryptedMessageHolder tL_decryptedMessageHolder2) {
        int i = tL_decryptedMessageHolder.layer.out_seq_no;
        int i2 = tL_decryptedMessageHolder2.layer.out_seq_no;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX WARN: Code duplicated, block: B:39:0x00df  */
    private boolean decryptWithMtProtoVersion(NativeByteBuffer nativeByteBuffer, byte[] bArr, byte[] bArr2, int i, boolean z, boolean z2) {
        boolean z3;
        boolean z4 = false;
        boolean z5 = i == 1 ? false : z;
        MessageKeyData messageKeyDataGenerateMessageKeyData = MessageKeyData.generateMessageKeyData(bArr, bArr2, z5, i);
        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, messageKeyDataGenerateMessageKeyData.aesKey, messageKeyDataGenerateMessageKeyData.aesIv, false, false, 24, nativeByteBuffer.limit() - 24);
        int int32 = nativeByteBuffer.readInt32(false);
        if (i == 2) {
            int i2 = z5 ? 8 : 0;
            ByteBuffer byteBuffer = nativeByteBuffer.buffer;
            z3 = true;
            if (!Utilities.arraysEquals(bArr2, 0, Utilities.computeSHA256(bArr, i2 + 88, 32, byteBuffer, 24, byteBuffer.limit()), 8)) {
                if (z2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, messageKeyDataGenerateMessageKeyData.aesKey, messageKeyDataGenerateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                z4 = z3;
            }
        } else {
            z3 = true;
            int iLimit = int32 + 28;
            if (iLimit < nativeByteBuffer.buffer.limit() - 15 || iLimit > nativeByteBuffer.buffer.limit()) {
                iLimit = nativeByteBuffer.buffer.limit();
            }
            byte[] bArrComputeSHA1 = Utilities.computeSHA1(nativeByteBuffer.buffer, 24, iLimit);
            if (!Utilities.arraysEquals(bArr2, 0, bArrComputeSHA1, bArrComputeSHA1.length - 16)) {
                if (z2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, messageKeyDataGenerateMessageKeyData.aesKey, messageKeyDataGenerateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                z4 = z3;
            }
        }
        if (int32 <= 0) {
            z4 = z3;
        }
        if (int32 > nativeByteBuffer.limit() - 28) {
            z4 = z3;
        }
        int iLimit2 = (nativeByteBuffer.limit() - 28) - int32;
        if (i == 2) {
            if (iLimit2 < 12) {
                z4 = z3;
            }
            if (iLimit2 > 1024) {
                z4 = z3;
            }
        } else if (iLimit2 > 15) {
            z4 = z3;
        }
        return !z4;
    }

    /* JADX WARN: Code duplicated, block: B:101:0x0218 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:105:0x0220 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:107:0x0227 A[Catch: Exception -> 0x0037, TRY_LEAVE, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:27:0x0078 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:29:0x008c  */
    /* JADX WARN: Code duplicated, block: B:30:0x008e  */
    /* JADX WARN: Code duplicated, block: B:33:0x0093  */
    /* JADX WARN: Code duplicated, block: B:35:0x0096  */
    /* JADX WARN: Code duplicated, block: B:38:0x00a0 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:39:0x00a2 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:42:0x00ad  */
    /* JADX WARN: Code duplicated, block: B:43:0x00af  */
    /* JADX WARN: Code duplicated, block: B:45:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:48:0x00c6 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:51:0x00d0 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:57:0x00e8 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:58:0x00ee A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:61:0x00f8 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:63:0x00fc A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:65:0x0102 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:67:0x0106 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:70:0x0148 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:71:0x0149  */
    /* JADX WARN: Code duplicated, block: B:78:0x0155 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:80:0x0159 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:83:0x0173 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:86:0x0186 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:88:0x01c0 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:90:0x01d7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:91:0x01d9 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:94:0x01f9 A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:96:0x01fd A[Catch: Exception -> 0x0037, TryCatch #0 {Exception -> 0x0037, blocks: (B:7:0x001a, B:9:0x001e, B:11:0x002a, B:14:0x003a, B:16:0x0045, B:18:0x0060, B:27:0x0078, B:31:0x008f, B:36:0x0098, B:39:0x00a2, B:46:0x00b5, B:48:0x00c6, B:49:0x00cc, B:51:0x00d0, B:53:0x00d6, B:55:0x00da, B:57:0x00e8, B:58:0x00ee, B:59:0x00f1, B:61:0x00f8, B:63:0x00fc, B:65:0x0102, B:67:0x0106, B:68:0x0142, B:72:0x014b, B:76:0x0152, B:78:0x0155, B:80:0x0159, B:81:0x015e, B:83:0x0173, B:84:0x017f, B:86:0x0186, B:88:0x01c0, B:91:0x01d9, B:92:0x01e1, B:99:0x0207, B:101:0x0218, B:102:0x021b, B:94:0x01f9, B:96:0x01fd, B:105:0x0220, B:107:0x0227, B:20:0x0064, B:24:0x0070), top: B:111:0x001a }] */
    /* JADX WARN: Code duplicated, block: B:98:0x0206  */
    /* JADX WARN: Instruction removed from duplicated block: B:67:0x0106, please report this as an issue */
    protected ArrayList<TLRPC.Message> decryptMessage(TLRPC.EncryptedMessage encryptedMessage) {
        byte[] bArr;
        boolean z;
        byte[] data;
        boolean z2;
        boolean z3;
        byte[] bArr2;
        final SecretChatHelper secretChatHelper;
        int i;
        TLObject tLObjectTLdeserialize;
        ArrayList<TLRPC.Message> arrayList;
        TLRPC.Message messageProcessDecryptedObject;
        TLRPC.TL_decryptedMessageLayer tL_decryptedMessageLayer;
        int i2;
        int i3;
        ArrayList<TL_decryptedMessageHolder> arrayList2;
        int i4;
        TLRPC.EncryptedChat encryptedChatDB = getMessagesController().getEncryptedChatDB(encryptedMessage.chat_id, true);
        if (encryptedChatDB != null && !(encryptedChatDB instanceof TLRPC.TL_encryptedChatDiscarded)) {
            try {
                if (encryptedChatDB instanceof TLRPC.TL_encryptedChatWaiting) {
                    ArrayList<TLRPC.Update> arrayList3 = this.pendingSecretMessages.get(encryptedChatDB.id);
                    if (arrayList3 == null) {
                        arrayList3 = new ArrayList<>();
                        this.pendingSecretMessages.put(encryptedChatDB.id, arrayList3);
                    }
                    TLRPC.TL_updateNewEncryptedMessage tL_updateNewEncryptedMessage = new TLRPC.TL_updateNewEncryptedMessage();
                    tL_updateNewEncryptedMessage.message = encryptedMessage;
                    arrayList3.add(tL_updateNewEncryptedMessage);
                    return null;
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(encryptedMessage.bytes.length);
                nativeByteBuffer.writeBytes(encryptedMessage.bytes);
                nativeByteBuffer.position(0);
                long int64 = nativeByteBuffer.readInt64(false);
                if (encryptedChatDB.key_fingerprint == int64) {
                    bArr = encryptedChatDB.auth_key;
                } else {
                    long j = encryptedChatDB.future_key_fingerprint;
                    if (j == 0 || j != int64) {
                        bArr = null;
                    } else {
                        bArr = encryptedChatDB.future_auth_key;
                        z = true;
                    }
                    if (bArr != null) {
                        data = nativeByteBuffer.readData(16, false);
                        if (encryptedChatDB.admin_id == getUserConfig().getClientUserId()) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (encryptedChatDB.mtproto_seq != 0) {
                            z3 = false;
                        } else {
                            z3 = true;
                        }
                        bArr2 = bArr;
                        if (!decryptWithMtProtoVersion(nativeByteBuffer, bArr2, data, 2, z2, z3)) {
                            if (z3) {
                                secretChatHelper = this;
                                if (!secretChatHelper.decryptWithMtProtoVersion(nativeByteBuffer, bArr2, data, 1, z2, false)) {
                                    i = 1;
                                }
                            }
                            return null;
                        }
                        secretChatHelper = this;
                        i = 2;
                        tLObjectTLdeserialize = TLClassStore.Instance().TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                        nativeByteBuffer.reuse();
                        if (!z) {
                            encryptedChatDB.key_use_count_in = (short) (encryptedChatDB.key_use_count_in + 1);
                        }
                        if (tLObjectTLdeserialize instanceof TLRPC.TL_decryptedMessageLayer) {
                            tL_decryptedMessageLayer = (TLRPC.TL_decryptedMessageLayer) tLObjectTLdeserialize;
                            if (encryptedChatDB.seq_in == 0 && encryptedChatDB.seq_out == 0) {
                                if (encryptedChatDB.admin_id == secretChatHelper.getUserConfig().getClientUserId()) {
                                    encryptedChatDB.seq_out = 1;
                                    encryptedChatDB.seq_in = -2;
                                } else {
                                    encryptedChatDB.seq_in = -1;
                                }
                            }
                            if (tL_decryptedMessageLayer.random_bytes.length < 15) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("got random bytes less than needed");
                                }
                                return null;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("current chat in_seq = " + encryptedChatDB.seq_in + " out_seq = " + encryptedChatDB.seq_out);
                                FileLog.d("got message with in_seq = " + tL_decryptedMessageLayer.in_seq_no + " out_seq = " + tL_decryptedMessageLayer.out_seq_no);
                            }
                            i2 = tL_decryptedMessageLayer.out_seq_no;
                            i3 = encryptedChatDB.seq_in;
                            if (i2 <= i3) {
                                return null;
                            }
                            if (i != 1 && (i4 = encryptedChatDB.mtproto_seq) != 0 && i2 >= i4) {
                                return null;
                            }
                            if (i3 != i2 - 2) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("got hole");
                                }
                                secretChatHelper.sendResendMessage(encryptedChatDB, encryptedChatDB.seq_in + 2, tL_decryptedMessageLayer.out_seq_no - 2, null);
                                arrayList2 = secretChatHelper.secretHolesQueue.get(encryptedChatDB.id);
                                if (arrayList2 == null) {
                                    arrayList2 = new ArrayList<>();
                                    secretChatHelper.secretHolesQueue.put(encryptedChatDB.id, arrayList2);
                                }
                                if (arrayList2.size() >= 4) {
                                    secretChatHelper.secretHolesQueue.remove(encryptedChatDB.id);
                                    final TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded = new TLRPC.TL_encryptedChatDiscarded();
                                    tL_encryptedChatDiscarded.id = encryptedChatDB.id;
                                    tL_encryptedChatDiscarded.user_id = encryptedChatDB.user_id;
                                    tL_encryptedChatDiscarded.auth_key = encryptedChatDB.auth_key;
                                    tL_encryptedChatDiscarded.key_create_date = encryptedChatDB.key_create_date;
                                    tL_encryptedChatDiscarded.key_use_count_in = encryptedChatDB.key_use_count_in;
                                    tL_encryptedChatDiscarded.key_use_count_out = encryptedChatDB.key_use_count_out;
                                    tL_encryptedChatDiscarded.seq_in = encryptedChatDB.seq_in;
                                    tL_encryptedChatDiscarded.seq_out = encryptedChatDB.seq_out;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda36
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            this.f$0.lambda$decryptMessage$22(tL_encryptedChatDiscarded);
                                        }
                                    });
                                    secretChatHelper.declineSecretChat(encryptedChatDB.id, false);
                                    return null;
                                }
                                TL_decryptedMessageHolder tL_decryptedMessageHolder = new TL_decryptedMessageHolder();
                                tL_decryptedMessageHolder.layer = tL_decryptedMessageLayer;
                                tL_decryptedMessageHolder.file = encryptedMessage.file;
                                tL_decryptedMessageHolder.date = encryptedMessage.date;
                                tL_decryptedMessageHolder.new_key_used = z;
                                tL_decryptedMessageHolder.decryptedWithVersion = i;
                                arrayList2.add(tL_decryptedMessageHolder);
                                return null;
                            }
                            if (i == 2) {
                                encryptedChatDB.mtproto_seq = Math.min(encryptedChatDB.mtproto_seq, i3);
                            }
                            secretChatHelper.applyPeerLayer(encryptedChatDB, tL_decryptedMessageLayer.layer);
                            encryptedChatDB.seq_in = tL_decryptedMessageLayer.out_seq_no;
                            encryptedChatDB.in_seq_no = tL_decryptedMessageLayer.in_seq_no;
                            secretChatHelper.getMessagesStorage().updateEncryptedChatSeq(encryptedChatDB, true);
                            tLObjectTLdeserialize = tL_decryptedMessageLayer.message;
                        } else {
                            if (tLObjectTLdeserialize instanceof TLRPC.TL_decryptedMessageService) {
                                if (!(((TLRPC.TL_decryptedMessageService) tLObjectTLdeserialize).action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer)) {
                                }
                            }
                            return null;
                        }
                        TLObject tLObject = tLObjectTLdeserialize;
                        arrayList = new ArrayList<>();
                        messageProcessDecryptedObject = secretChatHelper.processDecryptedObject(encryptedChatDB, encryptedMessage.file, encryptedMessage.date, tLObject, z);
                        if (messageProcessDecryptedObject != null) {
                            arrayList.add(messageProcessDecryptedObject);
                        }
                        secretChatHelper.checkSecretHoles(encryptedChatDB, arrayList);
                        return arrayList;
                    }
                    nativeByteBuffer.reuse();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e(String.format("fingerprint mismatch %x", Long.valueOf(int64)));
                    }
                }
                z = false;
                if (bArr != null) {
                    data = nativeByteBuffer.readData(16, false);
                    if (encryptedChatDB.admin_id == getUserConfig().getClientUserId()) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (encryptedChatDB.mtproto_seq != 0) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    bArr2 = bArr;
                    if (!decryptWithMtProtoVersion(nativeByteBuffer, bArr2, data, 2, z2, z3)) {
                        if (z3) {
                            secretChatHelper = this;
                            if (!secretChatHelper.decryptWithMtProtoVersion(nativeByteBuffer, bArr2, data, 1, z2, false)) {
                                i = 1;
                            }
                        }
                        return null;
                    }
                    secretChatHelper = this;
                    i = 2;
                    tLObjectTLdeserialize = TLClassStore.Instance().TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                    nativeByteBuffer.reuse();
                    if (!z) {
                        encryptedChatDB.key_use_count_in = (short) (encryptedChatDB.key_use_count_in + 1);
                    }
                    if (tLObjectTLdeserialize instanceof TLRPC.TL_decryptedMessageLayer) {
                        tL_decryptedMessageLayer = (TLRPC.TL_decryptedMessageLayer) tLObjectTLdeserialize;
                        if (encryptedChatDB.seq_in == 0) {
                            if (encryptedChatDB.admin_id == secretChatHelper.getUserConfig().getClientUserId()) {
                                encryptedChatDB.seq_out = 1;
                                encryptedChatDB.seq_in = -2;
                            } else {
                                encryptedChatDB.seq_in = -1;
                            }
                        }
                        if (tL_decryptedMessageLayer.random_bytes.length < 15) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.e("got random bytes less than needed");
                            }
                            return null;
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("current chat in_seq = " + encryptedChatDB.seq_in + " out_seq = " + encryptedChatDB.seq_out);
                            FileLog.d("got message with in_seq = " + tL_decryptedMessageLayer.in_seq_no + " out_seq = " + tL_decryptedMessageLayer.out_seq_no);
                        }
                        i2 = tL_decryptedMessageLayer.out_seq_no;
                        i3 = encryptedChatDB.seq_in;
                        if (i2 <= i3) {
                            return null;
                        }
                        if (i != 1) {
                        }
                        if (i3 != i2 - 2) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.e("got hole");
                            }
                            secretChatHelper.sendResendMessage(encryptedChatDB, encryptedChatDB.seq_in + 2, tL_decryptedMessageLayer.out_seq_no - 2, null);
                            arrayList2 = secretChatHelper.secretHolesQueue.get(encryptedChatDB.id);
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList<>();
                                secretChatHelper.secretHolesQueue.put(encryptedChatDB.id, arrayList2);
                            }
                            if (arrayList2.size() >= 4) {
                                secretChatHelper.secretHolesQueue.remove(encryptedChatDB.id);
                                final TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded2 = new TLRPC.TL_encryptedChatDiscarded();
                                tL_encryptedChatDiscarded2.id = encryptedChatDB.id;
                                tL_encryptedChatDiscarded2.user_id = encryptedChatDB.user_id;
                                tL_encryptedChatDiscarded2.auth_key = encryptedChatDB.auth_key;
                                tL_encryptedChatDiscarded2.key_create_date = encryptedChatDB.key_create_date;
                                tL_encryptedChatDiscarded2.key_use_count_in = encryptedChatDB.key_use_count_in;
                                tL_encryptedChatDiscarded2.key_use_count_out = encryptedChatDB.key_use_count_out;
                                tL_encryptedChatDiscarded2.seq_in = encryptedChatDB.seq_in;
                                tL_encryptedChatDiscarded2.seq_out = encryptedChatDB.seq_out;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda36
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$decryptMessage$22(tL_encryptedChatDiscarded2);
                                    }
                                });
                                secretChatHelper.declineSecretChat(encryptedChatDB.id, false);
                                return null;
                            }
                            TL_decryptedMessageHolder tL_decryptedMessageHolder2 = new TL_decryptedMessageHolder();
                            tL_decryptedMessageHolder2.layer = tL_decryptedMessageLayer;
                            tL_decryptedMessageHolder2.file = encryptedMessage.file;
                            tL_decryptedMessageHolder2.date = encryptedMessage.date;
                            tL_decryptedMessageHolder2.new_key_used = z;
                            tL_decryptedMessageHolder2.decryptedWithVersion = i;
                            arrayList2.add(tL_decryptedMessageHolder2);
                            return null;
                        }
                        if (i == 2) {
                            encryptedChatDB.mtproto_seq = Math.min(encryptedChatDB.mtproto_seq, i3);
                        }
                        secretChatHelper.applyPeerLayer(encryptedChatDB, tL_decryptedMessageLayer.layer);
                        encryptedChatDB.seq_in = tL_decryptedMessageLayer.out_seq_no;
                        encryptedChatDB.in_seq_no = tL_decryptedMessageLayer.in_seq_no;
                        secretChatHelper.getMessagesStorage().updateEncryptedChatSeq(encryptedChatDB, true);
                        tLObjectTLdeserialize = tL_decryptedMessageLayer.message;
                    } else {
                        if (tLObjectTLdeserialize instanceof TLRPC.TL_decryptedMessageService) {
                            if (!(((TLRPC.TL_decryptedMessageService) tLObjectTLdeserialize).action instanceof TLRPC.TL_decryptedMessageActionNotifyLayer)) {
                            }
                        }
                        return null;
                    }
                    TLObject tLObject2 = tLObjectTLdeserialize;
                    arrayList = new ArrayList<>();
                    messageProcessDecryptedObject = secretChatHelper.processDecryptedObject(encryptedChatDB, encryptedMessage.file, encryptedMessage.date, tLObject2, z);
                    if (messageProcessDecryptedObject != null) {
                        arrayList.add(messageProcessDecryptedObject);
                    }
                    secretChatHelper.checkSecretHoles(encryptedChatDB, arrayList);
                    return arrayList;
                }
                nativeByteBuffer.reuse();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e(String.format("fingerprint mismatch %x", Long.valueOf(int64)));
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$decryptMessage$22(TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded) {
        getMessagesController().putEncryptedChat(tL_encryptedChatDiscarded, false);
        getMessagesStorage().updateEncryptedChat(tL_encryptedChatDiscarded);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, tL_encryptedChatDiscarded);
    }

    public void requestNewSecretChatKey(TLRPC.EncryptedChat encryptedChat) {
        byte[] bArr = new byte[256];
        Utilities.random.nextBytes(bArr);
        byte[] byteArray = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, getMessagesStorage().getSecretPBytes())).toByteArray();
        if (byteArray.length > 256) {
            byte[] bArr2 = new byte[256];
            System.arraycopy(byteArray, 1, bArr2, 0, 256);
            byteArray = bArr2;
        }
        encryptedChat.exchange_id = getSendMessagesHelper().getNextRandomId();
        encryptedChat.a_or_b = bArr;
        encryptedChat.g_a = byteArray;
        getMessagesStorage().updateEncryptedChat(encryptedChat);
        sendRequestKeyMessage(encryptedChat, null);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x006d  */
    /* JADX WARN: Code duplicated, block: B:20:0x0099  */
    /* JADX WARN: Code duplicated, block: B:23:0x00b4  */
    public void processAcceptedSecretChat(final TLRPC.EncryptedChat encryptedChat) {
        byte[] bArr;
        byte[] bArr2;
        ArrayList<TLRPC.Update> arrayList;
        BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
        BigInteger bigInteger2 = new BigInteger(1, encryptedChat.g_a_or_b);
        if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
            declineSecretChat(encryptedChat.id, false);
            return;
        }
        byte[] byteArray = bigInteger2.modPow(new BigInteger(1, encryptedChat.a_or_b), bigInteger).toByteArray();
        if (byteArray.length > 256) {
            bArr = new byte[256];
            System.arraycopy(byteArray, byteArray.length - 256, bArr, 0, 256);
        } else {
            if (byteArray.length < 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray, 0, bArr, 256 - byteArray.length, byteArray.length);
                for (int i = 0; i < 256 - byteArray.length; i++) {
                    bArr[i] = 0;
                }
            }
            byte[] bArrComputeSHA1 = Utilities.computeSHA1(byteArray);
            bArr2 = new byte[8];
            System.arraycopy(bArrComputeSHA1, bArrComputeSHA1.length - 8, bArr2, 0, 8);
            if (encryptedChat.key_fingerprint == Utilities.bytesToLong(bArr2)) {
                encryptedChat.auth_key = byteArray;
                encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                encryptedChat.seq_in = -2;
                encryptedChat.seq_out = 1;
                getMessagesStorage().updateEncryptedChat(encryptedChat);
                getMessagesController().putEncryptedChat(encryptedChat, false);
                arrayList = this.pendingSecretMessages.get(encryptedChat.id);
                if (arrayList != null) {
                    getMessagesController().processUpdateArray(arrayList, null, null, false, 0);
                    this.pendingSecretMessages.remove(encryptedChat.id);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processAcceptedSecretChat$23(encryptedChat);
                    }
                });
                return;
            }
            final TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded = new TLRPC.TL_encryptedChatDiscarded();
            tL_encryptedChatDiscarded.id = encryptedChat.id;
            tL_encryptedChatDiscarded.user_id = encryptedChat.user_id;
            tL_encryptedChatDiscarded.auth_key = encryptedChat.auth_key;
            tL_encryptedChatDiscarded.key_create_date = encryptedChat.key_create_date;
            tL_encryptedChatDiscarded.key_use_count_in = encryptedChat.key_use_count_in;
            tL_encryptedChatDiscarded.key_use_count_out = encryptedChat.key_use_count_out;
            tL_encryptedChatDiscarded.seq_in = encryptedChat.seq_in;
            tL_encryptedChatDiscarded.seq_out = encryptedChat.seq_out;
            tL_encryptedChatDiscarded.admin_id = encryptedChat.admin_id;
            tL_encryptedChatDiscarded.mtproto_seq = encryptedChat.mtproto_seq;
            getMessagesStorage().updateEncryptedChat(tL_encryptedChatDiscarded);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processAcceptedSecretChat$24(tL_encryptedChatDiscarded);
                }
            });
            declineSecretChat(encryptedChat.id, false);
        }
        byteArray = bArr;
        byte[] bArrComputeSHA2 = Utilities.computeSHA1(byteArray);
        bArr2 = new byte[8];
        System.arraycopy(bArrComputeSHA2, bArrComputeSHA2.length - 8, bArr2, 0, 8);
        if (encryptedChat.key_fingerprint == Utilities.bytesToLong(bArr2)) {
            encryptedChat.auth_key = byteArray;
            encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
            encryptedChat.seq_in = -2;
            encryptedChat.seq_out = 1;
            getMessagesStorage().updateEncryptedChat(encryptedChat);
            getMessagesController().putEncryptedChat(encryptedChat, false);
            arrayList = this.pendingSecretMessages.get(encryptedChat.id);
            if (arrayList != null) {
                getMessagesController().processUpdateArray(arrayList, null, null, false, 0);
                this.pendingSecretMessages.remove(encryptedChat.id);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processAcceptedSecretChat$23(encryptedChat);
                }
            });
            return;
        }
        final TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded2 = new TLRPC.TL_encryptedChatDiscarded();
        tL_encryptedChatDiscarded2.id = encryptedChat.id;
        tL_encryptedChatDiscarded2.user_id = encryptedChat.user_id;
        tL_encryptedChatDiscarded2.auth_key = encryptedChat.auth_key;
        tL_encryptedChatDiscarded2.key_create_date = encryptedChat.key_create_date;
        tL_encryptedChatDiscarded2.key_use_count_in = encryptedChat.key_use_count_in;
        tL_encryptedChatDiscarded2.key_use_count_out = encryptedChat.key_use_count_out;
        tL_encryptedChatDiscarded2.seq_in = encryptedChat.seq_in;
        tL_encryptedChatDiscarded2.seq_out = encryptedChat.seq_out;
        tL_encryptedChatDiscarded2.admin_id = encryptedChat.admin_id;
        tL_encryptedChatDiscarded2.mtproto_seq = encryptedChat.mtproto_seq;
        getMessagesStorage().updateEncryptedChat(tL_encryptedChatDiscarded2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$processAcceptedSecretChat$24(tL_encryptedChatDiscarded2);
            }
        });
        declineSecretChat(encryptedChat.id, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedSecretChat$23(TLRPC.EncryptedChat encryptedChat) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, encryptedChat);
        sendNotifyLayerMessage(encryptedChat, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedSecretChat$24(TLRPC.TL_encryptedChatDiscarded tL_encryptedChatDiscarded) {
        getMessagesController().putEncryptedChat(tL_encryptedChatDiscarded, false);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, tL_encryptedChatDiscarded);
    }

    public void declineSecretChat(int i, boolean z) {
        declineSecretChat(i, z, 0L);
    }

    public void declineSecretChat(int i, boolean z, final long j) {
        NativeByteBuffer nativeByteBuffer;
        Exception e;
        if (j == 0) {
            try {
                nativeByteBuffer = new NativeByteBuffer(12);
                try {
                    nativeByteBuffer.writeInt32(100);
                    nativeByteBuffer.writeInt32(i);
                    nativeByteBuffer.writeBool(z);
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                }
            } catch (Exception e3) {
                nativeByteBuffer = null;
                e = e3;
            }
            j = getMessagesStorage().createPendingTask(nativeByteBuffer);
        }
        TLRPC.TL_messages_discardEncryption tL_messages_discardEncryption = new TLRPC.TL_messages_discardEncryption();
        tL_messages_discardEncryption.chat_id = i;
        tL_messages_discardEncryption.delete_history = z;
        getConnectionsManager().sendRequest(tL_messages_discardEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda34
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$declineSecretChat$25(j, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$declineSecretChat$25(long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (j != 0) {
            getMessagesStorage().removePendingTask(j);
        }
    }

    public void acceptSecretChat(final TLRPC.EncryptedChat encryptedChat) {
        if (this.acceptingChats.get(encryptedChat.id) != null) {
            return;
        }
        this.acceptingChats.put(encryptedChat.id, encryptedChat);
        TLRPC.TL_messages_getDhConfig tL_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tL_messages_getDhConfig.random_length = 256;
        tL_messages_getDhConfig.version = getMessagesStorage().getLastSecretVersion();
        getConnectionsManager().sendRequest(tL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda27
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$acceptSecretChat$28(encryptedChat, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$28(final TLRPC.EncryptedChat encryptedChat, TLObject tLObject, TLRPC.TL_error tL_error) {
        byte[] bArr;
        if (tL_error == null) {
            TLRPC.messages_DhConfig messages_dhconfig = (TLRPC.messages_DhConfig) tLObject;
            if (tLObject instanceof TLRPC.TL_messages_dhConfig) {
                if (!Utilities.isGoodPrime(messages_dhconfig.p, messages_dhconfig.g)) {
                    this.acceptingChats.remove(encryptedChat.id);
                    declineSecretChat(encryptedChat.id, false);
                    return;
                } else {
                    getMessagesStorage().setSecretPBytes(messages_dhconfig.p);
                    getMessagesStorage().setSecretG(messages_dhconfig.g);
                    getMessagesStorage().setLastSecretVersion(messages_dhconfig.version);
                    getMessagesStorage().saveSecretParams(getMessagesStorage().getLastSecretVersion(), getMessagesStorage().getSecretG(), getMessagesStorage().getSecretPBytes());
                }
            }
            byte[] bArr2 = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr2[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ messages_dhconfig.random[i]);
            }
            encryptedChat.a_or_b = bArr2;
            encryptedChat.seq_in = -1;
            encryptedChat.seq_out = 0;
            BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
            BigInteger bigIntegerModPow = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr2), bigInteger);
            BigInteger bigInteger2 = new BigInteger(1, encryptedChat.g_a);
            if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                this.acceptingChats.remove(encryptedChat.id);
                declineSecretChat(encryptedChat.id, false);
                return;
            }
            byte[] byteArray = bigIntegerModPow.toByteArray();
            if (byteArray.length > 256) {
                byte[] bArr3 = new byte[256];
                System.arraycopy(byteArray, 1, bArr3, 0, 256);
                byteArray = bArr3;
            }
            byte[] byteArray2 = bigInteger2.modPow(new BigInteger(1, bArr2), bigInteger).toByteArray();
            if (byteArray2.length > 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray2, byteArray2.length - 256, bArr, 0, 256);
            } else {
                if (byteArray2.length < 256) {
                    bArr = new byte[256];
                    System.arraycopy(byteArray2, 0, bArr, 256 - byteArray2.length, byteArray2.length);
                    for (int i2 = 0; i2 < 256 - byteArray2.length; i2++) {
                        bArr[i2] = 0;
                    }
                }
                byte[] bArrComputeSHA1 = Utilities.computeSHA1(byteArray2);
                byte[] bArr4 = new byte[8];
                System.arraycopy(bArrComputeSHA1, bArrComputeSHA1.length - 8, bArr4, 0, 8);
                encryptedChat.auth_key = byteArray2;
                encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                TLRPC.TL_messages_acceptEncryption tL_messages_acceptEncryption = new TLRPC.TL_messages_acceptEncryption();
                tL_messages_acceptEncryption.g_b = byteArray;
                TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat = new TLRPC.TL_inputEncryptedChat();
                tL_messages_acceptEncryption.peer = tL_inputEncryptedChat;
                tL_inputEncryptedChat.chat_id = encryptedChat.id;
                tL_inputEncryptedChat.access_hash = encryptedChat.access_hash;
                tL_messages_acceptEncryption.key_fingerprint = Utilities.bytesToLong(bArr4);
                getConnectionsManager().sendRequest(tL_messages_acceptEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda0
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                        this.f$0.lambda$acceptSecretChat$27(encryptedChat, tLObject2, tL_error2);
                    }
                }, 64);
                return;
            }
            byteArray2 = bArr;
            byte[] bArrComputeSHA2 = Utilities.computeSHA1(byteArray2);
            byte[] bArr5 = new byte[8];
            System.arraycopy(bArrComputeSHA2, bArrComputeSHA2.length - 8, bArr5, 0, 8);
            encryptedChat.auth_key = byteArray2;
            encryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
            TLRPC.TL_messages_acceptEncryption tL_messages_acceptEncryption2 = new TLRPC.TL_messages_acceptEncryption();
            tL_messages_acceptEncryption2.g_b = byteArray;
            TLRPC.TL_inputEncryptedChat tL_inputEncryptedChat2 = new TLRPC.TL_inputEncryptedChat();
            tL_messages_acceptEncryption2.peer = tL_inputEncryptedChat2;
            tL_inputEncryptedChat2.chat_id = encryptedChat.id;
            tL_inputEncryptedChat2.access_hash = encryptedChat.access_hash;
            tL_messages_acceptEncryption2.key_fingerprint = Utilities.bytesToLong(bArr5);
            getConnectionsManager().sendRequest(tL_messages_acceptEncryption2, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                    this.f$0.lambda$acceptSecretChat$27(encryptedChat, tLObject2, tL_error2);
                }
            }, 64);
            return;
        }
        this.acceptingChats.remove(encryptedChat.id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$27(TLRPC.EncryptedChat encryptedChat, TLObject tLObject, TLRPC.TL_error tL_error) {
        this.acceptingChats.remove(encryptedChat.id);
        if (tL_error == null) {
            final TLRPC.EncryptedChat encryptedChat2 = (TLRPC.EncryptedChat) tLObject;
            encryptedChat2.auth_key = encryptedChat.auth_key;
            encryptedChat2.user_id = encryptedChat.user_id;
            encryptedChat2.seq_in = encryptedChat.seq_in;
            encryptedChat2.seq_out = encryptedChat.seq_out;
            encryptedChat2.key_create_date = encryptedChat.key_create_date;
            encryptedChat2.key_use_count_in = encryptedChat.key_use_count_in;
            encryptedChat2.key_use_count_out = encryptedChat.key_use_count_out;
            getMessagesStorage().updateEncryptedChat(encryptedChat2);
            getMessagesController().putEncryptedChat(encryptedChat2, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$acceptSecretChat$26(encryptedChat2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$26(TLRPC.EncryptedChat encryptedChat) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatUpdated, encryptedChat);
        sendNotifyLayerMessage(encryptedChat, null);
    }

    public void startSecretChat(final Context context, final TLRPC.User user) {
        if (user == null || context == null) {
            return;
        }
        if (getMessagesController().isFrozen()) {
            AccountFrozenAlert.show(this.currentAccount);
            return;
        }
        this.startingSecretChat = true;
        final AlertDialog alertDialog = new AlertDialog(context, 3);
        TLRPC.TL_messages_getDhConfig tL_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tL_messages_getDhConfig.random_length = 256;
        tL_messages_getDhConfig.version = getMessagesStorage().getLastSecretVersion();
        final int iSendRequest = getConnectionsManager().sendRequest(tL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda8
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$startSecretChat$35(context, alertDialog, user, tLObject, tL_error);
            }
        }, 2);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                this.f$0.lambda$startSecretChat$36(iSendRequest, dialogInterface);
            }
        });
        try {
            alertDialog.show();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$35(final Context context, final AlertDialog alertDialog, final TLRPC.User user, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_DhConfig messages_dhconfig = (TLRPC.messages_DhConfig) tLObject;
            if (tLObject instanceof TLRPC.TL_messages_dhConfig) {
                if (!Utilities.isGoodPrime(messages_dhconfig.p, messages_dhconfig.g)) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretChatHelper.$r8$lambda$YmuUgBn1OPuoBzllRepuETIyI40(context, alertDialog);
                        }
                    });
                    return;
                }
                getMessagesStorage().setSecretPBytes(messages_dhconfig.p);
                getMessagesStorage().setSecretG(messages_dhconfig.g);
                getMessagesStorage().setLastSecretVersion(messages_dhconfig.version);
                getMessagesStorage().saveSecretParams(getMessagesStorage().getLastSecretVersion(), getMessagesStorage().getSecretG(), getMessagesStorage().getSecretPBytes());
            }
            final byte[] bArr = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ messages_dhconfig.random[i]);
            }
            byte[] byteArray = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, getMessagesStorage().getSecretPBytes())).toByteArray();
            if (byteArray.length > 256) {
                byte[] bArr2 = new byte[256];
                System.arraycopy(byteArray, 1, bArr2, 0, 256);
                byteArray = bArr2;
            }
            TLRPC.TL_messages_requestEncryption tL_messages_requestEncryption = new TLRPC.TL_messages_requestEncryption();
            tL_messages_requestEncryption.g_a = byteArray;
            tL_messages_requestEncryption.user_id = getMessagesController().getInputUser(user);
            tL_messages_requestEncryption.random_id = Utilities.random.nextInt();
            getConnectionsManager().sendRequest(tL_messages_requestEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda11
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                    this.f$0.lambda$startSecretChat$33(context, alertDialog, bArr, user, tLObject2, tL_error2);
                }
            }, 2);
            return;
        }
        this.delayedEncryptedChatUpdates.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startSecretChat$34(context, alertDialog);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$YmuUgBn1OPuoBzllRepuETIyI40(Context context, AlertDialog alertDialog) {
        try {
            if (((Activity) context).isFinishing()) {
                return;
            }
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$33(final Context context, final AlertDialog alertDialog, final byte[] bArr, final TLRPC.User user, final TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startSecretChat$31(context, alertDialog, tLObject, bArr, user);
                }
            });
        } else {
            this.delayedEncryptedChatUpdates.clear();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startSecretChat$32(context, alertDialog);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$31(Context context, AlertDialog alertDialog, TLObject tLObject, byte[] bArr, TLRPC.User user) {
        this.startingSecretChat = false;
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        TLRPC.EncryptedChat encryptedChat = (TLRPC.EncryptedChat) tLObject;
        encryptedChat.user_id = encryptedChat.participant_id;
        encryptedChat.seq_in = -2;
        encryptedChat.seq_out = 1;
        encryptedChat.a_or_b = bArr;
        getMessagesController().putEncryptedChat(encryptedChat, false);
        TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
        tL_dialog.id = DialogObject.makeEncryptedDialogId(encryptedChat.id);
        tL_dialog.unread_count = 0;
        tL_dialog.top_message = 0;
        tL_dialog.last_message_date = getConnectionsManager().getCurrentTime();
        getMessagesController().dialogs_dict.put(tL_dialog.id, tL_dialog);
        getMessagesController().allDialogs.add(tL_dialog);
        getMessagesController().sortDialogs(null);
        getMessagesStorage().putEncryptedChat(encryptedChat, user, tL_dialog);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.encryptedChatCreated, encryptedChat);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startSecretChat$30();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$30() {
        if (this.delayedEncryptedChatUpdates.isEmpty()) {
            return;
        }
        getMessagesController().processUpdateArray(this.delayedEncryptedChatUpdates, null, null, false, 0);
        this.delayedEncryptedChatUpdates.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$32(Context context, AlertDialog alertDialog) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        this.startingSecretChat = false;
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(LocaleController.getString(R.string.CreateEncryptedChatError));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.show().setCanceledOnTouchOutside(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$34(Context context, AlertDialog alertDialog) {
        this.startingSecretChat = false;
        if (((Activity) context).isFinishing()) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$36(int i, DialogInterface dialogInterface) {
        getConnectionsManager().cancelRequest(i, true);
    }
}
