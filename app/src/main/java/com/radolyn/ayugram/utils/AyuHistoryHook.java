package com.radolyn.ayugram.utils;

import android.text.TextUtils;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pair;
import com.exteragram.messenger.badges.BadgesController;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuMapper;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.entities.AyuMessageBase;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;

public abstract class AyuHistoryHook {
    private static final SparseArray instances = new SparseArray();

    public static synchronized void setInstance(ChatActivity chatActivity) {
        try {
            for (int size = instances.size() - 1; size >= 0; size--) {
                SparseArray sparseArray = instances;
                int iKeyAt = sparseArray.keyAt(size);
                ChatActivity chatActivity2 = (ChatActivity) ((WeakReference) sparseArray.valueAt(size)).get();
                if (chatActivity2 == null || (chatActivity2 == chatActivity && iKeyAt != chatActivity.getClassGuid())) {
                    sparseArray.remove(size);
                }
            }
            instances.put(chatActivity.getClassGuid(), new WeakReference(chatActivity));
        } catch (Throwable th) {
            throw th;
        }
    }

    public static synchronized void removeInstance(ChatActivity chatActivity) {
        instances.remove(chatActivity.getClassGuid());
    }

    private static boolean needHook(long j, long j2) {
        if (j == -1905581924) {
            return j2 == 1;
        }
        return !BadgesController.INSTANCE.isExtera(Math.abs(j));
    }

    /* JADX WARN: Code duplicated, block: B:139:0x0222  */
    public static synchronized int doHook(int i, ArrayList arrayList, TLRPC.messages_Messages messages_messages, int i2, long j, long j2, int i3, int i4, int i5, boolean z, int i6, int i7, int i8, int i9, int i10, int i11, boolean z2, int i12, long j3, int i13, boolean z3, int i14, boolean z4, boolean z5) {
        SparseArray[] sparseArrayArr;
        boolean z6;
        int iIntValue;
        int minRealId;
        int size;
        int i15;
        TLRPC.TL_forumTopic tL_forumTopicFindTopic;
        long jAbs;
        try {
            if (AyuConfig.disableHook) {
                return -100;
            }
            if (i12 == 1) {
                return -100;
            }
            WeakReference weakReference = (WeakReference) instances.get(i6);
            if (weakReference == null) {
                return -100;
            }
            ChatActivity chatActivity = (ChatActivity) weakReference.get();
            if (chatActivity == null) {
                return -100;
            }
            if (chatActivity.getChatMode() == 0 || chatActivity.getChatMode() == 8) {
                if (!chatActivity.isComments && (z5 || !chatActivity.isThreadChat())) {
                    long dialogId = chatActivity.getDialogId();
                    long topicId = chatActivity.getTopicId();
                    if (!needHook(dialogId, topicId)) {
                        return -100;
                    }
                    SparseArray[] sparseArrayArr2 = chatActivity.messagesDict;
                    boolean zIsSecretChat = chatActivity.isSecretChat();
                    int i16 = zIsSecretChat ? Integer.MAX_VALUE : 0;
                    int i17 = zIsSecretChat ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                    Pair minAndMaxIds = getMinAndMaxIds(arrayList, true);
                    if (!DialogObject.isEncryptedDialog(dialogId)) {
                        if (!arrayList.isEmpty()) {
                            int iIntValue2 = ((Integer) minAndMaxIds.first).intValue();
                            int iIntValue3 = ((Integer) minAndMaxIds.second).intValue();
                            int iMin = Math.min(iIntValue2, iIntValue3);
                            sparseArrayArr = sparseArrayArr2;
                            int iMax = Math.max(iIntValue2, iIntValue3);
                            z6 = zIsSecretChat;
                            TLRPC.Dialog dialog = chatActivity.getMessagesController().getDialog(dialogId);
                            if (z5) {
                                try {
                                    i15 = iIntValue3;
                                    try {
                                        jAbs = chatActivity.getCurrentChatInfo().id;
                                    } catch (Exception unused) {
                                        jAbs = Math.abs(dialogId);
                                    }
                                } catch (Exception unused2) {
                                    i15 = iIntValue3;
                                }
                                tL_forumTopicFindTopic = chatActivity.getMessagesController().getTopicsController().findTopic(jAbs, topicId);
                            } else {
                                i15 = iIntValue3;
                                tL_forumTopicFindTopic = null;
                            }
                            android.util.Pair minAndMaxForDialog = AyuLocalDatabaseUtils.getMinAndMaxForDialog(i, dialogId);
                            if ((dialog == null || (dialog.top_message != iMax && (((Integer) minAndMaxForDialog.second).intValue() != iMax || dialog.top_message > ((Integer) minAndMaxForDialog.second).intValue()))) && (tL_forumTopicFindTopic == null || tL_forumTopicFindTopic.top_message != iMax)) {
                                if (arrayList.size() == 1 && (((MessageObject) arrayList.get(0)).messageOwner instanceof TLRPC.TL_messageService) && chatActivity.messages.isEmpty()) {
                                    minRealId = i17;
                                } else if (arrayList.size() == 1 && (((MessageObject) arrayList.get(0)).messageOwner instanceof TLRPC.TL_messageService)) {
                                    minRealId = AyuMessageUtils.getMinRealId(chatActivity.messages);
                                } else if (arrayList.size() >= i3 || z || (!(i11 == 2 || i11 == 1) || arrayList.isEmpty())) {
                                    minRealId = iMax;
                                } else {
                                    minRealId = Math.min(iIntValue2, i15);
                                }
                                iMin = i16;
                            } else {
                                minRealId = i17;
                            }
                            iIntValue = iMin;
                        } else {
                            sparseArrayArr = sparseArrayArr2;
                            z6 = zIsSecretChat;
                            int i18 = i17;
                            i16 = i16;
                            if (!chatActivity.messages.isEmpty() && i11 == 0) {
                                minRealId = AyuMessageUtils.getMinRealId(chatActivity.messages);
                            } else {
                                minRealId = (i11 == 2 || i11 == 3 || (i11 == 0 && chatActivity.messages.isEmpty())) ? i18 : i16;
                            }
                            iIntValue = i16;
                            if (z) {
                                minRealId = iIntValue;
                            }
                        }
                    } else {
                        sparseArrayArr = sparseArrayArr2;
                        z6 = zIsSecretChat;
                        int i19 = i17;
                        i16 = i16;
                        android.util.Pair minAndMaxForDialog2 = AyuLocalDatabaseUtils.getMinAndMaxForDialog(i, dialogId);
                        int iIntValue4 = ((Integer) minAndMaxForDialog2.second).intValue();
                        int iIntValue5 = ((Integer) minAndMaxForDialog2.first).intValue();
                        iIntValue = ((Integer) minAndMaxIds.second).intValue();
                        int iIntValue6 = ((Integer) minAndMaxIds.first).intValue();
                        Pair minAndMaxIds2 = getMinAndMaxIds(arrayList, true);
                        int iIntValue7 = ((Integer) minAndMaxIds2.second).intValue();
                        int iIntValue8 = ((Integer) minAndMaxIds2.first).intValue();
                        if (Math.abs(iIntValue4 - iIntValue5) == 1 || ((iIntValue4 == iIntValue && iIntValue5 == iIntValue6) || ((iIntValue4 == iIntValue7 && iIntValue5 == iIntValue8) || ((iIntValue4 == iIntValue && iIntValue5 == iIntValue8) || (iIntValue4 == iIntValue7 && iIntValue5 == iIntValue6))))) {
                            minRealId = i19;
                            iIntValue = i16;
                        } else {
                            if (iIntValue4 == iIntValue) {
                                iIntValue = i16;
                            } else if (iIntValue5 == iIntValue6) {
                                minRealId = i19;
                            }
                            minRealId = iIntValue6;
                        }
                    }
                    if (iIntValue <= minRealId) {
                        int i20 = minRealId;
                        minRealId = iIntValue;
                        iIntValue = i20;
                    }
                    int i21 = i16;
                    if (minRealId == i21 && iIntValue == i21) {
                        size = i3;
                    } else {
                        doHook(i, arrayList, sparseArrayArr, minRealId, iIntValue, dialogId, topicId, z6, true);
                        if (arrayList.size() > i3) {
                            size = arrayList.size();
                        } else {
                            size = i3;
                        }
                    }
                    return size;
                }
            }
            return -100;
        } catch (Throwable th) {
            throw th;
        }
    }

    private static void doHook(int i, ArrayList arrayList, SparseArray[] sparseArrayArr, int i2, int i3, long j, long j2, boolean z, boolean z2) {
        AyuMessagesController ayuMessagesController = AyuMessagesController.getInstance(i);
        List<DeletedMessageFull> messages = ayuMessagesController.getMessages(j, j2, i2, i3);
        if (messages.isEmpty()) {
            return;
        }
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (DeletedMessageFull deletedMessageFull : messages) {
            if (!isEmpty(deletedMessageFull.message)) {
                TLRPC.TL_message map = map(deletedMessageFull, i);
                longSparseArray.put(map.id, map);
                MessagesStorage.addUsersAndChatsFromMessage(map, arrayList2, arrayList3, null);
            }
        }
        if (longSparseArray.isEmpty()) {
            return;
        }
        MessagesStorage messagesStorage = MessagesStorage.getInstance(i);
        MessagesController messagesController = MessagesController.getInstance(i);
        QuadroResult entities = getEntities(messagesStorage, arrayList2, arrayList3);
        Pair dicts = entities.getDicts();
        ArrayList users = entities.getUsers();
        ArrayList chats = entities.getChats();
        if (!users.isEmpty()) {
            messagesController.putUsers(users, true);
        }
        if (!chats.isEmpty()) {
            messagesController.putChats(chats, true);
        }
        ArrayList arrayList4 = new ArrayList();
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            try {
                arrayList4.add(new MessageObject(i, (TLRPC.Message) longSparseArray.get(longSparseArray.keyAt(i4)), (LongSparseArray) dicts.first, (LongSparseArray) dicts.second, false, true));
            } catch (Exception unused) {
            }
        }
        if (arrayList4.isEmpty()) {
            return;
        }
        merge(arrayList4, arrayList, z, z2);
        fixReplies(i, arrayList, sparseArrayArr, ayuMessagesController, dicts, messagesStorage);
    }

    public static void fixReplies(int i, List list, SparseArray[] sparseArrayArr, AyuMessagesController ayuMessagesController, Pair pair, MessagesStorage messagesStorage) {
        MessageObject messageObject;
        DeletedMessageFull message;
        TLRPC.Message message2;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            MessageObject messageObject2 = (MessageObject) it.next();
            TLRPC.Message message3 = messageObject2.messageOwner;
            if (message3.reply_to != null && ((message3.replyMessage == null || messageObject2.replyMessageObject == null) && !messageObject2.isReplyToStory())) {
                if (sparseArrayArr != null) {
                    messageObject = (MessageObject) sparseArrayArr[0].get(messageObject2.messageOwner.reply_to.reply_to_msg_id);
                    if (messageObject == null || messageObject.getId() != messageObject2.messageOwner.reply_to.reply_to_msg_id) {
                        messageObject = (MessageObject) sparseArrayArr[1].get(messageObject2.messageOwner.reply_to.reply_to_msg_id);
                    }
                } else {
                    messageObject = null;
                }
                if (messageObject == null || messageObject.getId() != messageObject2.messageOwner.reply_to.reply_to_msg_id) {
                    Iterator it2 = list.iterator();
                    while (it2.hasNext()) {
                        MessageObject messageObject3 = (MessageObject) it2.next();
                        if (messageObject3.getId() == messageObject2.messageOwner.reply_to.reply_to_msg_id) {
                            messageObject = messageObject3;
                            break;
                        }
                    }
                }
                if ((messageObject == null || messageObject.getId() != messageObject2.messageOwner.reply_to.reply_to_msg_id) && (message = ayuMessagesController.getMessage(MessageObject.getDialogId(messageObject2.messageOwner), messageObject2.messageOwner.reply_to.reply_to_msg_id)) != null) {
                    try {
                        messageObject = new MessageObject(i, (TLRPC.Message) map(message, i), (LongSparseArray) pair.first, (LongSparseArray) pair.second, false, false);
                    } catch (Exception unused) {
                    }
                }
                if (messageObject == null && (message2 = messagesStorage.getMessage(MessageObject.getDialogId(messageObject2.messageOwner), messageObject2.messageOwner.reply_to.reply_to_msg_id)) != null) {
                    try {
                        messageObject = new MessageObject(i, message2, (LongSparseArray) pair.first, (LongSparseArray) pair.second, false, false);
                    } catch (Exception unused2) {
                    }
                }
                if (messageObject != null) {
                    TLRPC.Message message4 = messageObject2.messageOwner;
                    TLRPC.Message message5 = messageObject.messageOwner;
                    message4.replyMessage = message5;
                    message4.reply_to.reply_to_peer_id = message5.peer_id;
                    messageObject2.replyMessageObject = messageObject;
                }
            }
        }
    }

    private static void merge(ArrayList arrayList, ArrayList arrayList2, boolean z, boolean z2) {
        boolean z3 = (z && z2) || !(z || z2);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            MessageObject messageObject = (MessageObject) obj;
            if (!arrayList2.isEmpty()) {
                int i2 = 0;
                while (true) {
                    if (i2 < arrayList2.size()) {
                        MessageObject messageObject2 = (MessageObject) arrayList2.get(i2);
                        if (!z && ((messageObject.getId() < 0 && messageObject2.getId() > 0) || (messageObject.getId() > 0 && messageObject2.getId() < 0))) {
                            int i3 = messageObject.messageOwner.date;
                            int i4 = messageObject2.messageOwner.date;
                            if (z3) {
                                if (i3 < i4) {
                                    arrayList2.add(i2, messageObject);
                                    break;
                                    break;
                                }
                                i2++;
                            } else {
                                if (i3 > i4) {
                                    arrayList2.add(i2, messageObject);
                                    break;
                                    break;
                                }
                                i2++;
                            }
                        } else {
                            int id = messageObject.getId();
                            int id2 = messageObject2.getId();
                            if (z3) {
                                if (id < id2) {
                                    arrayList2.add(i2, messageObject);
                                    break;
                                }
                                i2++;
                            } else {
                                if (id > id2) {
                                    arrayList2.add(i2, messageObject);
                                    break;
                                    break;
                                }
                                i2++;
                            }
                        }
                    } else {
                        MessageObject messageObject3 = (MessageObject) arrayList2.get(z3 ? arrayList2.size() - 1 : 0);
                        if (!z && ((messageObject.getId() < 0 && messageObject3.getId() > 0) || (messageObject.getId() > 0 && messageObject3.getId() < 0))) {
                            int i5 = messageObject.messageOwner.date;
                            int i6 = messageObject3.messageOwner.date;
                            if (!z3) {
                                if (i5 <= i6) {
                                    arrayList2.add(messageObject);
                                    break;
                                    break;
                                } else {
                                    arrayList2.add(0, messageObject);
                                    break;
                                    break;
                                }
                            }
                            if (i5 >= i6) {
                                arrayList2.add(messageObject);
                                break;
                                break;
                            } else {
                                arrayList2.add(0, messageObject);
                                break;
                                break;
                            }
                        }
                        int id3 = messageObject.getId();
                        int id4 = messageObject3.getId();
                        if (!z3) {
                            if (id3 <= id4) {
                                arrayList2.add(messageObject);
                                break;
                                break;
                            } else {
                                arrayList2.add(0, messageObject);
                                break;
                                break;
                            }
                        }
                        if (id3 >= id4) {
                            arrayList2.add(messageObject);
                            break;
                        } else {
                            arrayList2.add(0, messageObject);
                            break;
                        }
                    }
                }
            } else {
                arrayList2.add(messageObject);
            }
        }
    }

    public static QuadroResult getEntities(MessagesStorage messagesStorage, ArrayList arrayList, ArrayList arrayList2) {
        ArrayList<TLRPC.User> arrayList3 = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList4 = new ArrayList<>();
        try {
            if (!arrayList.isEmpty()) {
                messagesStorage.getUsersInternal((ArrayList<Long>) arrayList, arrayList3);
            }
        } catch (Exception unused) {
        }
        try {
            if (!arrayList2.isEmpty()) {
                messagesStorage.getChatsInternal(TextUtils.join(",", arrayList2), arrayList4);
            }
        } catch (Exception unused2) {
        }
        return new QuadroResult(arrayList3, arrayList4);
    }

    public static boolean isEmpty(AyuMessageBase ayuMessageBase) {
        if (ayuMessageBase != null) {
            return TextUtils.isEmpty(ayuMessageBase.text) && TextUtils.isEmpty(ayuMessageBase.mediaPath) && ayuMessageBase.documentSerialized == null;
        }
        return true;
    }

    public static TLRPC.TL_message map(DeletedMessageFull deletedMessageFull, int i) {
        TLRPC.TL_message tL_message = new TLRPC.TL_message();
        AyuMapper.getInstance(i).map(deletedMessageFull.message, tL_message);
        AyuMapper.getInstance(i).mapMedia(deletedMessageFull.message, tL_message);
        List<DeletedMessageReaction> list = deletedMessageFull.reactions;
        if (list != null && !list.isEmpty()) {
            tL_message.reactions = new TLRPC.TL_messageReactions();
            int i2 = 0;
            for (DeletedMessageReaction deletedMessageReaction : deletedMessageFull.reactions) {
                TLRPC.TL_reactionCount tL_reactionCount = new TLRPC.TL_reactionCount();
                tL_reactionCount.count = deletedMessageReaction.count;
                tL_reactionCount.chosen = deletedMessageReaction.selfSelected;
                i2++;
                tL_reactionCount.chosen_order = i2;
                if (deletedMessageReaction.isCustom) {
                    TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                    tL_reactionCustomEmoji.document_id = deletedMessageReaction.documentId;
                    tL_reactionCount.reaction = tL_reactionCustomEmoji;
                } else if (deletedMessageReaction.isPaid) {
                    tL_reactionCount.reaction = new TLRPC.TL_reactionPaid();
                } else {
                    TLRPC.TL_reactionEmoji tL_reactionEmoji = new TLRPC.TL_reactionEmoji();
                    tL_reactionEmoji.emoticon = deletedMessageReaction.emoticon;
                    tL_reactionCount.reaction = tL_reactionEmoji;
                }
                tL_message.reactions.results.add(tL_reactionCount);
            }
        }
        tL_message.ayuDeleted = true;
        return tL_message;
    }

    public static Pair getMinAndMaxIds(ArrayList arrayList, boolean z) {
        int size = arrayList.size();
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        int i3 = 0;
        while (i3 < size) {
            Object obj = arrayList.get(i3);
            i3++;
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.isSent() && messageObject.getId() != 0) {
                TLRPC.Message message = messageObject.messageOwner;
                if (!(message instanceof TLRPC.TL_messageEmpty) && (z || !(message instanceof TLRPC.TL_messageService))) {
                    int id = messageObject.getId();
                    if (id < i) {
                        i = id;
                    }
                    if (id > i2) {
                        i2 = id;
                    }
                }
            }
        }
        return new Pair(Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static class QuadroResult {
        private final ArrayList chats;
        private LongSparseArray chatsDict;
        private final ArrayList users;
        private LongSparseArray usersDict;

        public QuadroResult(ArrayList arrayList, ArrayList arrayList2) {
            this.users = arrayList;
            this.chats = arrayList2;
        }

        public Pair getDicts() {
            if (this.usersDict == null && this.chatsDict == null) {
                this.usersDict = new LongSparseArray();
                this.chatsDict = new LongSparseArray();
                ArrayList arrayList = this.users;
                int size = arrayList.size();
                int i = 0;
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList.get(i2);
                    i2++;
                    TLRPC.User user = (TLRPC.User) obj;
                    this.usersDict.put(user.id, user);
                }
                ArrayList arrayList2 = this.chats;
                int size2 = arrayList2.size();
                while (i < size2) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    TLRPC.Chat chat = (TLRPC.Chat) obj2;
                    this.chatsDict.put(chat.id, chat);
                }
            }
            return new Pair(this.usersDict, this.chatsDict);
        }

        public ArrayList getUsers() {
            return this.users;
        }

        public ArrayList getChats() {
            return this.chats;
        }
    }
}
