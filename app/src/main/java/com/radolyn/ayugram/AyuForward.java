package com.radolyn.ayugram;

import android.text.TextUtils;
import android.util.LongSparseArray;
import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.radolyn.ayugram.controllers.AyuAttachments;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.seq.AyuSequentialUtils;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;

public abstract class AyuForward {
    private static final DispatchQueue queue = new DispatchQueue("AyuForward");
    private static final ConcurrentMap forwardStates = new ConcurrentHashMap();

    public static DispatchQueue getQueue() {
        return queue;
    }

    public static boolean isForwarding(long j) {
        int i;
        ForwardState forwardState = (ForwardState) forwardStates.get(Long.valueOf(j));
        if (j == 0 || forwardState == null || (i = forwardState.state) == 3) {
            return false;
        }
        int i2 = forwardState.currentChunk;
        int i3 = forwardState.totalChunks;
        if (i2 >= i3 || forwardState.stopRequested) {
            return false;
        }
        return !(i3 == 0 || forwardState.totalMessages == 0) || i == 1;
    }

    public static boolean isFullAyuForwardsNeeded(int i, ArrayList arrayList) {
        return isFullAyuForwardsNeeded(i, (MessageObject) arrayList.get(0));
    }

    public static boolean isFullAyuForwardsNeeded(int i, MessageObject messageObject) {
        return AyuMessageUtils.isChatNoForwards(i, messageObject.getDialogId());
    }

    public static boolean isAyuForwardNeeded(ArrayList arrayList) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            if (isAyuForwardNeeded((MessageObject) obj)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAyuForwardNeeded(MessageObject messageObject) {
        return AyuMessageUtils.isUnforwardable(messageObject);
    }

    public static void intelligentForward(int i, ArrayList arrayList, long j, boolean z, boolean z2, boolean z3, int i2, MessageObject messageObject) {
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ForwardChunk forwardChunk = new ForwardChunk(isAyuForwardNeeded((MessageObject) arrayList.get(0)), arrayList3);
        int size = arrayList.size();
        int i3 = 0;
        while (i3 < size) {
            Object obj = arrayList.get(i3);
            i3++;
            MessageObject messageObject2 = (MessageObject) obj;
            if (isAyuForwardNeeded(messageObject2) != forwardChunk.isAyuForwardNeeded) {
                arrayList2.add(forwardChunk);
                arrayList3 = new ArrayList();
                forwardChunk = new ForwardChunk(isAyuForwardNeeded(messageObject2), arrayList3);
            }
            arrayList3.add(messageObject2);
        }
        arrayList2.add(forwardChunk);
        ForwardState forwardState = new ForwardState(arrayList2.size());
        forwardStates.put(Long.valueOf(j), forwardState);
        int size2 = arrayList2.size();
        int i4 = 0;
        while (i4 < size2) {
            Object obj2 = arrayList2.get(i4);
            i4++;
            ForwardChunk forwardChunk2 = (ForwardChunk) obj2;
            if (forwardChunk2.isAyuForwardNeeded) {
                forwardMessages(i, forwardChunk2.messages, j, z2, z3, messageObject, forwardState);
            } else {
                forwardState.totalMessages = forwardChunk2.messages.size();
                forwardState.sentMessages = 0;
                forwardState.setState(2);
                AyuSequentialUtils.forwardMessagesSync(i, forwardChunk2.messages, j, z, z2, z3, messageObject);
                forwardState.sentMessages = forwardState.totalMessages;
                forwardState.setState(3);
            }
            forwardState.increaseChunk();
        }
        forwardState.setState(3);
    }

    /* JADX WARN: Code duplicated, block: B:44:0x0117  */
    public static void forwardMessages(int i, ArrayList arrayList, long j, boolean z, boolean z2, MessageObject messageObject, ForwardState forwardState) {
        boolean z3;
        int i2;
        boolean z4;
        LongSparseArray longSparseArray;
        LongSparseArray longSparseArray2;
        ForwardState forwardState2;
        ArrayList arrayList2;
        ArrayList arrayList3;
        Long l;
        int i3 = i;
        boolean z5 = true;
        ForwardState forwardState3 = forwardState == null ? new ForwardState(1) : forwardState;
        forwardStates.put(Long.valueOf(j), forwardState3);
        ArrayList arrayList4 = new ArrayList();
        LongSparseArray longSparseArray3 = new LongSparseArray();
        LongSparseArray longSparseArray4 = new LongSparseArray();
        boolean zIsFullAyuForwardsNeeded = isFullAyuForwardsNeeded(i, arrayList);
        int size = arrayList.size();
        boolean z6 = false;
        int i4 = 0;
        while (true) {
            if (i4 >= size) {
                break;
            }
            Object obj = arrayList.get(i4);
            i4++;
            MessageObject messageObject2 = (MessageObject) obj;
            if ((zIsFullAyuForwardsNeeded || AyuMessageUtils.isUnforwardable(messageObject2)) && AyuMessageUtils.isMediaDownloadable(messageObject2, false)) {
                arrayList4.add(messageObject2);
            }
            if (messageObject2.getGroupId() != 0) {
                Long l2 = (Long) longSparseArray3.get(messageObject2.getGroupId());
                longSparseArray3.put(messageObject2.getGroupId(), Long.valueOf(l2 != null ? l2.longValue() + 1 : 1L));
                if (((Long) longSparseArray4.get(messageObject2.getGroupId())) == null) {
                    longSparseArray4.put(messageObject2.getGroupId(), Long.valueOf(Utilities.random.nextLong()));
                }
            }
        }
        if (!arrayList4.isEmpty()) {
            forwardState3.setState(1);
            AyuSequentialUtils.loadDocumentsSync(i3, arrayList4);
        }
        forwardState3.totalMessages = arrayList.size();
        forwardState3.sentMessages = 0;
        forwardState3.setState(2);
        int size2 = arrayList.size();
        int i5 = 0;
        while (i5 < size2) {
            i5++;
            MessageObject messageObject3 = (MessageObject) arrayList.get(i5);
            if (forwardState3.stopRequested) {
                forwardState3.setState(3);
                return;
            }
            CharSequence messageText = ChatUtils.getInstance().getMessageText(messageObject3, null);
            String string = messageText == null ? "" : messageText.toString();
            boolean zIsMediaDownloadable = AyuMessageUtils.isMediaDownloadable(messageObject3, z6);
            long groupId = messageObject3.getGroupId();
            Long l3 = (Long) longSparseArray4.get(groupId, 0L);
            if (groupId != 0) {
                longSparseArray3.put(groupId, Long.valueOf(((Long) longSparseArray3.get(groupId)).longValue() - 1));
                if (((Long) longSparseArray3.get(groupId)).longValue() == 0) {
                    z3 = z5;
                } else {
                    z3 = z6;
                }
            } else {
                z3 = z6;
            }
            File file = new File(AyuAttachments.getInstance(i3).getExistingPath(messageObject3, z6));
            if (TextUtils.isEmpty(string) && !zIsMediaDownloadable) {
                forwardState3.decreaseTotalMessages();
                i5 = i5;
            } else {
                boolean z7 = (!messageObject3.hasMediaSpoilers() || messageObject3.isHiddenSensitive()) ? z6 : z5;
                TLRPC.Message message = messageObject3.messageOwner;
                boolean z8 = message.invert_media;
                TLRPC.Document document = AyuMessageUtils.getDocument(message);
                TLRPC.Photo photo = AyuMessageUtils.getPhoto(messageObject3.messageOwner);
                if (messageObject3.isAnimatedEmoji() || !(messageObject3.isSticker() || messageObject3.isAnimatedSticker())) {
                    i2 = size2;
                    if (!zIsMediaDownloadable) {
                        AyuSequentialUtils.sendTextMessageSync(i, string, j, messageObject, messageObject, null, false, messageObject3.messageOwner.entities, z2);
                    } else {
                        i3 = i;
                        if (document == null) {
                            ForwardState forwardState4 = forwardState3;
                            z4 = z6;
                            longSparseArray = longSparseArray4;
                            longSparseArray2 = longSparseArray3;
                            boolean z9 = z3;
                            if (photo != null) {
                                if (!file.exists()) {
                                    forwardState4.decreaseTotalMessages();
                                    forwardState3 = forwardState4;
                                } else {
                                    TLRPC.TL_photo tL_photoMapPhoto = mapPhoto(i3, photo, file);
                                    if (!TextUtils.isEmpty(photo.caption)) {
                                        string = photo.caption;
                                    }
                                    ArrayList arrayList5 = messageObject3.messageOwner.entities;
                                    if (z) {
                                        string = null;
                                        arrayList2 = null;
                                    } else {
                                        arrayList2 = arrayList5;
                                    }
                                    forwardState2 = forwardState4;
                                    AyuSequentialUtils.sendPhotoMessageSync(i3, tL_photoMapPhoto, file.getAbsolutePath(), j, messageObject, messageObject, string, arrayList2, z2, l3, z9, z7, z8);
                                }
                                longSparseArray3 = longSparseArray2;
                                size2 = i2;
                                longSparseArray4 = longSparseArray;
                                z6 = z4;
                            } else {
                                forwardState2 = forwardState4;
                            }
                        } else if (!file.exists()) {
                            forwardState3.decreaseTotalMessages();
                            longSparseArray4 = longSparseArray4;
                            size2 = i2;
                        } else {
                            TLRPC.TL_document tL_documentMapDocument = mapDocument(i3, messageObject3, document, file, z6);
                            ArrayList arrayList6 = messageObject3.messageOwner.entities;
                            if (z) {
                                string = null;
                                l = l3;
                                arrayList3 = null;
                            } else {
                                arrayList3 = arrayList6;
                                l = l3;
                            }
                            forwardState2 = forwardState3;
                            z4 = z6;
                            longSparseArray = longSparseArray4;
                            longSparseArray2 = longSparseArray3;
                            AyuSequentialUtils.sendDocumentMessageSync(i3, tL_documentMapDocument, file.getAbsolutePath(), j, messageObject, messageObject, string, arrayList3, z2, l, z3, z7, z8);
                        }
                        forwardState2.increaseSentMessages();
                        i3 = i;
                        forwardState3 = forwardState2;
                        longSparseArray3 = longSparseArray2;
                        size2 = i2;
                        longSparseArray4 = longSparseArray;
                        z6 = z4;
                    }
                    z5 = true;
                } else {
                    i2 = size2;
                    AyuSequentialUtils.sendStickerMessageSync(i3, document, null, j, messageObject, messageObject, z2, 0, false);
                }
                forwardState2 = forwardState3;
                z4 = z6;
                longSparseArray = longSparseArray4;
                longSparseArray2 = longSparseArray3;
                forwardState2.increaseSentMessages();
                i3 = i;
                forwardState3 = forwardState2;
                longSparseArray3 = longSparseArray2;
                size2 = i2;
                longSparseArray4 = longSparseArray;
                z6 = z4;
                z5 = true;
            }
        }
        forwardState3.setState(3);
    }

    private static TLRPC.TL_document mapDocument(int i, MessageObject messageObject, TLRPC.Document document, File file, boolean z) {
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        tL_document.flags = document.flags;
        tL_document.file_reference = z ? document.file_reference : new byte[0];
        tL_document.dc_id = z ? document.dc_id : Integer.MIN_VALUE;
        tL_document.user_id = document.user_id;
        tL_document.version = document.version;
        tL_document.mime_type = document.mime_type;
        tL_document.file_name = document.file_name;
        tL_document.file_name_fixed = document.file_name_fixed;
        tL_document.date = AccountInstance.getInstance(i).getConnectionsManager().getCurrentTime();
        if (file != null) {
            tL_document.size = (int) file.length();
        }
        tL_document.thumbs = document.thumbs;
        tL_document.video_thumbs = document.video_thumbs;
        tL_document.localThumbPath = document.localThumbPath;
        tL_document.attributes = document.attributes;
        if (file != null) {
            tL_document.localPath = file.getAbsolutePath();
        }
        if (messageObject.isGif()) {
            tL_document.mime_type = "image/gif";
        }
        return tL_document;
    }

    private static TLRPC.TL_photo mapPhoto(int i, TLRPC.Photo photo, File file) {
        TLRPC.TL_photo tL_photoGeneratePhotoSizes = SendMessagesHelper.getInstance(i).generatePhotoSizes(file.getAbsolutePath(), null);
        tL_photoGeneratePhotoSizes.flags = photo.flags;
        tL_photoGeneratePhotoSizes.has_stickers = photo.has_stickers;
        tL_photoGeneratePhotoSizes.date = AccountInstance.getInstance(i).getConnectionsManager().getCurrentTime();
        tL_photoGeneratePhotoSizes.geo = photo.geo;
        tL_photoGeneratePhotoSizes.caption = photo.caption;
        return tL_photoGeneratePhotoSizes;
    }

    public static String getForwardingStatus(long j) {
        String string;
        ForwardState forwardState = (ForwardState) forwardStates.get(Long.valueOf(j));
        if (forwardState == null) {
            return null;
        }
        String string2 = LocaleController.formatString(org.telegram.messenger.R.string.AyuForwardStatusSentCount, Integer.valueOf(forwardState.sentMessages), Integer.valueOf(forwardState.totalMessages));
        String string3 = LocaleController.formatString(org.telegram.messenger.R.string.AyuForwardStatusChunkCount, Integer.valueOf(forwardState.currentChunk + 1), Integer.valueOf(forwardState.totalChunks));
        if (forwardState.totalChunks > 1) {
            string2 = string2 + " â€¢ " + string3;
        }
        int i = forwardState.state;
        if (i == 0) {
            string = LocaleController.getString(org.telegram.messenger.R.string.AyuForwardStatusPreparing);
        } else {
            if (i == 1) {
                return LocaleController.getString(org.telegram.messenger.R.string.AyuForwardStatusLoadingMedia);
            }
            if (i == 2) {
                string = LocaleController.getString(org.telegram.messenger.R.string.AyuForwardStatusForwarding);
            } else {
                string = LocaleController.getString(org.telegram.messenger.R.string.AyuForwardStatusFinished);
            }
        }
        return string + "\n" + string2;
    }

    public static boolean stop(long j) {
        ForwardState forwardState = (ForwardState) forwardStates.get(Long.valueOf(j));
        if (forwardState == null) {
            return false;
        }
        forwardState.requestStop();
        return true;
    }

    public static void forceStop() {
        forwardStates.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
            }
        });
    }

    private static final class ForwardChunk extends RecordTag {
        private final boolean isAyuForwardNeeded;
        private final ArrayList messages;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof ForwardChunk)) {
                return false;
            }
            ForwardChunk forwardChunk = (ForwardChunk) obj;
            return this.isAyuForwardNeeded == forwardChunk.isAyuForwardNeeded && Objects.equals(this.messages, forwardChunk.messages);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Boolean.valueOf(this.isAyuForwardNeeded), this.messages};
        }

        private ForwardChunk(boolean z, ArrayList arrayList) {
            this.isAyuForwardNeeded = z;
            this.messages = arrayList;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return AyuForward$ForwardChunk$$ExternalSyntheticRecord0.m(this.isAyuForwardNeeded, this.messages);
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ForwardChunk.class, "isAyuForwardNeeded;messages");
        }
    }

    public static class ForwardState {
        public final int totalChunks;
        public int currentChunk = 0;
        public int totalMessages = 0;
        public int sentMessages = 0;
        public int state = 0;
        public boolean stopRequested = false;

        public ForwardState(int i) {
            this.totalChunks = i;
        }

        public void increaseChunk() {
            this.currentChunk++;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$ForwardState$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
                }
            });
        }

        public void increaseSentMessages() {
            this.sentMessages++;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$ForwardState$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
                }
            });
        }

        public void decreaseTotalMessages() {
            this.totalMessages--;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$ForwardState$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
                }
            });
        }

        public void requestStop() {
            this.stopRequested = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$ForwardState$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
                }
            });
        }

        public void setState(int i) {
            this.state = i;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuForward$ForwardState$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(AyuConstants.UPDATE_CHAT_RESTRICTION, new Object[0]);
                }
            });
        }
    }
}
