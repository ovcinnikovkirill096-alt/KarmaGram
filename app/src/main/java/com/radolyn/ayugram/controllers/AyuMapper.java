package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import androidx.core.util.Pair;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.database.entities.AyuMessageBase;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import com.radolyn.ayugram.utils.AyuFileLocation;
import com.radolyn.ayugram.utils.AyuFileUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import j$.util.Collection;
import j$.util.function.Function$CC;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class AyuMapper extends BaseController {
    private static final AyuMapper[] Instance = new AyuMapper[16];

    public AyuMapper(int i) {
        super(i);
    }

    public static AyuMapper getInstance(int i) {
        AyuMapper ayuMapper;
        AyuMapper[] ayuMapperArr = Instance;
        AyuMapper ayuMapper2 = ayuMapperArr[i];
        if (ayuMapper2 != null) {
            return ayuMapper2;
        }
        synchronized (AyuMapper.class) {
            try {
                ayuMapper = ayuMapperArr[i];
                if (ayuMapper == null) {
                    ayuMapper = new AyuMapper(i);
                    ayuMapperArr[i] = ayuMapper;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuMapper;
    }

    public static ArrayList deserializeMultiple(byte[] bArr, Function function) {
        if (bArr == null || bArr.length == 0) {
            return new ArrayList();
        }
        NativeByteBuffer nativeByteBuffer = null;
        try {
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(bArr.length);
            try {
                nativeByteBuffer2.buffer.put(bArr);
                nativeByteBuffer2.rewind();
                ArrayList arrayList = new ArrayList();
                while (nativeByteBuffer2.buffer.position() < nativeByteBuffer2.buffer.limit()) {
                    TLObject tLObject = (TLObject) function.apply(nativeByteBuffer2);
                    if (tLObject != null) {
                        arrayList.add(tLObject);
                    }
                }
                nativeByteBuffer2.reuse();
                return arrayList;
            } catch (Throwable unused) {
                nativeByteBuffer = nativeByteBuffer2;
                try {
                    return new ArrayList();
                } finally {
                    if (nativeByteBuffer != null) {
                        nativeByteBuffer.reuse();
                    }
                }
            }
        } catch (Throwable unused2) {
        }
    }

    public static TLObject deserializeSingle(byte[] bArr, Function function) {
        ArrayList arrayListDeserializeMultiple = deserializeMultiple(bArr, function);
        if (arrayListDeserializeMultiple.isEmpty()) {
            return null;
        }
        return (TLObject) arrayListDeserializeMultiple.get(0);
    }

    public static byte[] serializeMultiple(ArrayList arrayList) {
        NativeByteBuffer nativeByteBuffer;
        int i = 0;
        if (arrayList == null || arrayList.isEmpty()) {
            return new byte[0];
        }
        try {
            nativeByteBuffer = new NativeByteBuffer(Collection.EL.stream(arrayList).mapToInt(new ToIntFunction() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda5
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((TLObject) obj).getObjectSize();
                }
            }).sum());
            try {
                int size = arrayList.size();
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    ((TLObject) obj).serializeToStream(nativeByteBuffer);
                }
                nativeByteBuffer.rewind();
                byte[] bArr = new byte[nativeByteBuffer.remaining()];
                nativeByteBuffer.buffer.get(bArr);
                nativeByteBuffer.reuse();
                return bArr;
            } catch (Throwable unused) {
                if (nativeByteBuffer != null) {
                    nativeByteBuffer.reuse();
                }
                return null;
            }
        } catch (Throwable unused2) {
            nativeByteBuffer = null;
        }
    }

    public static byte[] serializeSingle(TLObject tLObject) {
        return serializeMultiple(new ArrayList() { // from class: com.radolyn.ayugram.controllers.AyuMapper.1
            {
                add(this.val$entity);
            }
        });
    }

    public void map(SaveMessageRequest saveMessageRequest, AyuMessageBase ayuMessageBase) {
        TLRPC.Message message = saveMessageRequest.getMessage();
        ayuMessageBase.userId = getUserConfig().getClientUserId();
        ayuMessageBase.dialogId = saveMessageRequest.getDialogId();
        ayuMessageBase.groupedId = message.grouped_id;
        ayuMessageBase.peerId = MessageObject.getPeerId(message.peer_id);
        ayuMessageBase.fromId = MessageObject.getPeerId(message.from_id);
        long topicId = saveMessageRequest.getTopicId();
        ayuMessageBase.topicId = topicId;
        if (topicId == 0 && getMessagesController().isMonoForum(saveMessageRequest.getDialogId())) {
            ayuMessageBase.topicId = saveMessageRequest.getMonoForumTopicId();
        }
        ayuMessageBase.messageId = message.id;
        ayuMessageBase.date = message.date;
        int i = message.flags;
        ayuMessageBase.flags = i;
        if (message.unread) {
            ayuMessageBase.flags = i | 1;
        }
        ayuMessageBase.editDate = message.edit_date;
        ayuMessageBase.views = message.views;
        TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
        if (messageFwdHeader != null) {
            ayuMessageBase.fwdFlags = messageFwdHeader.flags;
            ayuMessageBase.fwdFromId = MessageObject.getPeerId(messageFwdHeader.from_id);
            TLRPC.MessageFwdHeader messageFwdHeader2 = message.fwd_from;
            ayuMessageBase.fwdName = messageFwdHeader2.from_name;
            ayuMessageBase.fwdDate = messageFwdHeader2.date;
            ayuMessageBase.fwdPostAuthor = messageFwdHeader2.post_author;
        }
        TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
        if (messageReplyHeader != null) {
            ayuMessageBase.replySerialized = serializeSingle(messageReplyHeader);
        }
        TLRPC.ReplyMarkup replyMarkup = message.reply_markup;
        if (replyMarkup != null) {
            ayuMessageBase.replyMarkupSerialized = serializeSingle(replyMarkup);
        }
        ayuMessageBase.postAuthor = message.post_author;
        ayuMessageBase.entityCreateDate = saveMessageRequest.getRequestCatchTime();
        ayuMessageBase.text = message.message;
        ayuMessageBase.textEntities = serializeMultiple(message.entities);
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0091 A[Catch: all -> 0x0112, TRY_ENTER, TryCatch #0 {all -> 0x0112, blocks: (B:36:0x0091, B:38:0x00a3, B:40:0x00ab, B:42:0x00b5, B:44:0x00c1, B:46:0x00cd, B:48:0x00d7, B:50:0x00f0, B:52:0x00f6, B:49:0x00de), top: B:65:0x008f }] */
    /* JADX WARN: Code duplicated, block: B:42:0x00b5 A[Catch: all -> 0x0112, TryCatch #0 {all -> 0x0112, blocks: (B:36:0x0091, B:38:0x00a3, B:40:0x00ab, B:42:0x00b5, B:44:0x00c1, B:46:0x00cd, B:48:0x00d7, B:50:0x00f0, B:52:0x00f6, B:49:0x00de), top: B:65:0x008f }] */
    /* JADX WARN: Code duplicated, block: B:49:0x00de A[Catch: all -> 0x0112, TryCatch #0 {all -> 0x0112, blocks: (B:36:0x0091, B:38:0x00a3, B:40:0x00ab, B:42:0x00b5, B:44:0x00c1, B:46:0x00cd, B:48:0x00d7, B:50:0x00f0, B:52:0x00f6, B:49:0x00de), top: B:65:0x008f }] */
    /* JADX WARN: Code duplicated, block: B:52:0x00f6 A[Catch: all -> 0x0112, TRY_LEAVE, TryCatch #0 {all -> 0x0112, blocks: (B:36:0x0091, B:38:0x00a3, B:40:0x00ab, B:42:0x00b5, B:44:0x00c1, B:46:0x00cd, B:48:0x00d7, B:50:0x00f0, B:52:0x00f6, B:49:0x00de), top: B:65:0x008f }] */
    /* JADX WARN: Code duplicated, block: B:54:0x0114  */
    /* JADX WARN: Code duplicated, block: B:55:0x0119  */
    public void mapMedia(SaveMessageRequest saveMessageRequest, AyuMessageBase ayuMessageBase, boolean z) {
        NativeByteBuffer nativeByteBuffer;
        int i;
        File file;
        String absolutePath;
        TLRPC.Document document;
        TLRPC.MessageMedia media;
        ArrayList<TLRPC.PhotoSize> arrayList;
        int size;
        TLRPC.PhotoSize photoSize;
        File fileProcessThumb;
        TLRPC.Message message = saveMessageRequest.getMessage();
        int i2 = 0;
        if (AyuMessageUtils.isMediaDownloadable(new MessageObject(this.currentAccount, message, false, true), false) && getAyuAttachments().shouldSaveMedia(saveMessageRequest)) {
            TLRPC.MessageMedia messageMedia = message.media;
            String str = null;
            if (messageMedia == null) {
                ayuMessageBase.documentType = 0;
            } else if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && messageMedia.photo != null) {
                ayuMessageBase.documentType = 1;
            } else if ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && messageMedia.document != null && (MessageObject.isStickerMessage(message) || "application/x-tgsticker".equals(message.media.document.mime_type))) {
                ayuMessageBase.documentType = 2;
                ayuMessageBase.mimeType = message.media.document.mime_type;
                try {
                    nativeByteBuffer = new NativeByteBuffer(message.media.getObjectSize());
                    try {
                        message.media.serializeToStream(nativeByteBuffer);
                        nativeByteBuffer.rewind();
                        byte[] bArr = new byte[nativeByteBuffer.remaining()];
                        nativeByteBuffer.readBytes(bArr, false);
                        ayuMessageBase.documentSerialized = bArr;
                    } catch (Throwable unused) {
                        if (nativeByteBuffer != null) {
                        }
                        i = ayuMessageBase.documentType;
                        if (i != 1) {
                        }
                        file = new File("/");
                        try {
                            if (z) {
                                file = getAyuAttachments().processAttachment(saveMessageRequest);
                                media = MessageObject.getMedia(saveMessageRequest.getMessage());
                                if (media != null) {
                                    arrayList = media.document.thumbs;
                                    size = arrayList.size();
                                    while (i2 < size) {
                                        TLRPC.PhotoSize photoSize2 = arrayList.get(i2);
                                        i2++;
                                        photoSize = photoSize2;
                                        if (!(photoSize instanceof TLRPC.TL_photoSize)) {
                                        }
                                    }
                                }
                            } else {
                                file = new File(getAyuAttachments().getExistingPath(saveMessageRequest.getMessage(), false));
                            }
                            document = message.media.document;
                            if (document != null) {
                                ayuMessageBase.documentAttributesSerialized = serializeMultiple(document.attributes);
                                ayuMessageBase.thumbsSerialized = serializeMultiple(message.media.document.thumbs);
                                ayuMessageBase.mimeType = message.media.document.mime_type;
                            }
                        } catch (Throwable unused2) {
                        }
                        if (file != null) {
                            absolutePath = file.getAbsolutePath();
                        } else {
                            absolutePath = null;
                        }
                        if (absolutePath != null) {
                            str = absolutePath;
                        }
                        ayuMessageBase.mediaPath = str;
                    }
                } catch (Throwable unused3) {
                    nativeByteBuffer = null;
                }
                nativeByteBuffer.reuse();
            } else {
                ayuMessageBase.documentType = 3;
            }
            i = ayuMessageBase.documentType;
            if (i != 1 || i == 3) {
                file = new File("/");
                if (z) {
                    file = getAyuAttachments().processAttachment(saveMessageRequest);
                    media = MessageObject.getMedia(saveMessageRequest.getMessage());
                    if (media != null && MessageObject.isVideoDocument(media.document)) {
                        arrayList = media.document.thumbs;
                        size = arrayList.size();
                        while (i2 < size) {
                            TLRPC.PhotoSize photoSize3 = arrayList.get(i2);
                            i2++;
                            photoSize = photoSize3;
                            if (!(photoSize instanceof TLRPC.TL_photoSize) && (fileProcessThumb = getAyuAttachments().processThumb((TLRPC.TL_photoSize) photoSize)) != null && !fileProcessThumb.getAbsolutePath().equals("/")) {
                                ayuMessageBase.hqThumbPath = fileProcessThumb.getAbsolutePath();
                                break;
                            }
                        }
                    }
                } else {
                    file = new File(getAyuAttachments().getExistingPath(saveMessageRequest.getMessage(), false));
                }
                document = message.media.document;
                if (document != null) {
                    ayuMessageBase.documentAttributesSerialized = serializeMultiple(document.attributes);
                    ayuMessageBase.thumbsSerialized = serializeMultiple(message.media.document.thumbs);
                    ayuMessageBase.mimeType = message.media.document.mime_type;
                }
                if (file != null) {
                    absolutePath = file.getAbsolutePath();
                } else {
                    absolutePath = null;
                }
                if (absolutePath != null && !absolutePath.equals("/")) {
                    str = absolutePath;
                }
                ayuMessageBase.mediaPath = str;
            }
        }
    }

    public void map(AyuMessageBase ayuMessageBase, TLRPC.Message message) {
        int i = ayuMessageBase.flags;
        message.dialog_id = ayuMessageBase.dialogId;
        message.grouped_id = ayuMessageBase.groupedId;
        message.peer_id = getMessagesController().getPeer(ayuMessageBase.peerId);
        message.from_id = getMessagesController().getPeer(ayuMessageBase.fromId);
        int i2 = ayuMessageBase.messageId;
        message.id = i2;
        message.realId = i2;
        message.date = ayuMessageBase.date;
        int i3 = ayuMessageBase.flags;
        message.flags = i3;
        message.unread = (i & 1) != 0;
        message.out = (i & 2) != 0;
        message.mentioned = (i & 16) != 0;
        message.media_unread = (i & 32) != 0;
        message.silent = (i & 8192) != 0;
        message.post = (i & 16384) != 0;
        message.from_scheduled = (262144 & i) != 0;
        message.legacy = (524288 & i) != 0;
        message.edit_hide = (2097152 & i) != 0;
        message.pinned = (16777216 & i) != 0;
        message.noforwards = false;
        message.ayuNoforwards = (67108864 & i) != 0;
        message.invert_media = (134217728 & i) != 0;
        message.edit_date = ayuMessageBase.editDate;
        message.views = ayuMessageBase.views;
        if ((i3 & 4) != 0) {
            TLRPC.TL_messageFwdHeader tL_messageFwdHeader = new TLRPC.TL_messageFwdHeader();
            message.fwd_from = tL_messageFwdHeader;
            tL_messageFwdHeader.flags = ayuMessageBase.fwdFlags;
            if (ayuMessageBase.fwdFromId != 0) {
                tL_messageFwdHeader.from_id = getMessagesController().getPeer(ayuMessageBase.fwdFromId);
            }
            TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
            messageFwdHeader.from_name = ayuMessageBase.fwdName;
            messageFwdHeader.date = ayuMessageBase.fwdDate;
            messageFwdHeader.post_author = ayuMessageBase.fwdPostAuthor;
        }
        if ((message.flags & 8) != 0) {
            if (ayuMessageBase.replyFlags != 0) {
                TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                message.reply_to = tL_messageReplyHeader;
                tL_messageReplyHeader.flags = ayuMessageBase.replyFlags;
                tL_messageReplyHeader.reply_to_msg_id = ayuMessageBase.replyMessageId;
                tL_messageReplyHeader.reply_to_peer_id = getMessagesController().getPeer(ayuMessageBase.replyPeerId);
                TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
                messageReplyHeader.reply_to_top_id = ayuMessageBase.replyTopId;
                messageReplyHeader.forum_topic = ayuMessageBase.replyForumTopic;
            } else {
                message.reply_to = (TLRPC.MessageReplyHeader) deserializeSingle(ayuMessageBase.replySerialized, new Function() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda0
                    public /* synthetic */ Function andThen(Function function) {
                        return Function$CC.$default$andThen(this, function);
                    }

                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        NativeByteBuffer nativeByteBuffer = (NativeByteBuffer) obj;
                        return TLRPC.MessageReplyHeader.TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                    }

                    public /* synthetic */ Function compose(Function function) {
                        return Function$CC.$default$compose(this, function);
                    }
                });
            }
        }
        if ((i & 64) != 0) {
            message.reply_markup = (TLRPC.ReplyMarkup) deserializeSingle(ayuMessageBase.replyMarkupSerialized, new Function() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda1
                public /* synthetic */ Function andThen(Function function) {
                    return Function$CC.$default$andThen(this, function);
                }

                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    NativeByteBuffer nativeByteBuffer = (NativeByteBuffer) obj;
                    return TLRPC.ReplyMarkup.TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                }

                public /* synthetic */ Function compose(Function function) {
                    return Function$CC.$default$compose(this, function);
                }
            });
        }
        message.post_author = ayuMessageBase.postAuthor;
        message.message = ayuMessageBase.text;
        message.entities = deserializeMultiple(ayuMessageBase.textEntities, new Function() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda2
            public /* synthetic */ Function andThen(Function function) {
                return Function$CC.$default$andThen(this, function);
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                NativeByteBuffer nativeByteBuffer = (NativeByteBuffer) obj;
                return TLRPC.MessageEntity.TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
            }

            public /* synthetic */ Function compose(Function function) {
                return Function$CC.$default$compose(this, function);
            }
        });
    }

    public void mapMedia(AyuMessageBase ayuMessageBase, TLRPC.Message message) {
        byte[] bArr;
        int i = ayuMessageBase.documentType;
        byte[] bArr2 = ayuMessageBase.documentSerialized;
        String str = ayuMessageBase.mediaPath;
        int i2 = ayuMessageBase.date;
        if (i == 0) {
            return;
        }
        if (i != 2 && TextUtils.isEmpty(str)) {
            return;
        }
        int i3 = 0;
        if (i == 2) {
            NativeByteBuffer nativeByteBuffer = null;
            try {
                NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(bArr2.length);
                try {
                    nativeByteBuffer2.put(ByteBuffer.wrap(bArr2));
                    nativeByteBuffer2.rewind();
                    message.media = TLRPC.MessageMedia.TLdeserialize(nativeByteBuffer2, nativeByteBuffer2.readInt32(false), false);
                    message.stickerVerified = 1;
                    nativeByteBuffer2.reuse();
                } catch (Throwable unused) {
                    nativeByteBuffer = nativeByteBuffer2;
                    if (nativeByteBuffer != null) {
                        nativeByteBuffer.reuse();
                    }
                }
            } catch (Throwable unused2) {
            }
        } else {
            message.attachPath = str;
            File file = new File(str);
            if (i == 1) {
                Pair pairExtractImageSizeFromName = AyuFileUtils.extractImageSizeFromName(file.getName());
                if (pairExtractImageSizeFromName == null) {
                    pairExtractImageSizeFromName = AyuFileUtils.extractImageSizeFromFile(file.getAbsolutePath());
                }
                if (pairExtractImageSizeFromName == null) {
                    pairExtractImageSizeFromName = new Pair(500, 302);
                }
                Integer num = (Integer) pairExtractImageSizeFromName.first;
                Integer num2 = (Integer) pairExtractImageSizeFromName.second;
                TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                message.media = tL_messageMediaPhoto;
                tL_messageMediaPhoto.flags = 1;
                tL_messageMediaPhoto.photo = new TLRPC.TL_photo();
                TLRPC.Photo photo = message.media.photo;
                photo.has_stickers = false;
                photo.date = i2;
                TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                tL_photoSize.size = (int) file.length();
                tL_photoSize.w = num.intValue();
                tL_photoSize.h = num2.intValue();
                tL_photoSize.type = "y";
                tL_photoSize.location = new AyuFileLocation(str);
                message.media.photo.sizes.add(tL_photoSize);
                return;
            }
            if (i == 3) {
                TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                message.media = tL_messageMediaDocument;
                tL_messageMediaDocument.flags = 1;
                String readableFilename = AyuFileUtils.getReadableFilename(file.getName());
                message.media.document = new TLRPC.TL_document();
                TLRPC.Document document = message.media.document;
                document.date = i2;
                document.localPath = str;
                document.file_name = readableFilename;
                document.file_name_fixed = readableFilename;
                document.size = file.length();
                TLRPC.Document document2 = message.media.document;
                document2.mime_type = ayuMessageBase.mimeType;
                document2.attributes = deserializeMultiple(ayuMessageBase.documentAttributesSerialized, new Function() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda3
                    public /* synthetic */ Function andThen(Function function) {
                        return Function$CC.$default$andThen(this, function);
                    }

                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        NativeByteBuffer nativeByteBuffer3 = (NativeByteBuffer) obj;
                        return TLRPC.DocumentAttribute.TLdeserialize(nativeByteBuffer3, nativeByteBuffer3.readInt32(false), false);
                    }

                    public /* synthetic */ Function compose(Function function) {
                        return Function$CC.$default$compose(this, function);
                    }
                });
                ArrayList arrayListDeserializeMultiple = deserializeMultiple(ayuMessageBase.thumbsSerialized, new Function() { // from class: com.radolyn.ayugram.controllers.AyuMapper$$ExternalSyntheticLambda4
                    public /* synthetic */ Function andThen(Function function) {
                        return Function$CC.$default$andThen(this, function);
                    }

                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        NativeByteBuffer nativeByteBuffer3 = (NativeByteBuffer) obj;
                        return TLRPC.PhotoSize.TLdeserialize(0L, 0L, 0L, nativeByteBuffer3, nativeByteBuffer3.readInt32(false), false);
                    }

                    public /* synthetic */ Function compose(Function function) {
                        return Function$CC.$default$compose(this, function);
                    }
                });
                int size = arrayListDeserializeMultiple.size();
                while (i3 < size) {
                    Object obj = arrayListDeserializeMultiple.get(i3);
                    i3++;
                    TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) obj;
                    if (photoSize != null) {
                        if ((photoSize instanceof TLRPC.TL_photoSize) && !TextUtils.isEmpty(ayuMessageBase.hqThumbPath) && ((bArr = photoSize.bytes) == null || bArr.length == 0)) {
                            photoSize.location = new AyuFileLocation(ayuMessageBase.hqThumbPath);
                        }
                        byte[] bArr3 = photoSize.bytes;
                        if ((bArr3 != null && bArr3.length != 0) || photoSize.location != null) {
                            message.media.document.thumbs.add(photoSize);
                        }
                    }
                }
            }
        }
    }

    public void map(TLRPC.Dialog dialog, DeletedDialog deletedDialog) {
        deletedDialog.userId = getUserConfig().getClientUserId();
        deletedDialog.dialogId = dialog.id;
        deletedDialog.peerId = MessageObject.getPeerId(dialog.peer);
        deletedDialog.folderId = Integer.valueOf(dialog.folder_id);
        deletedDialog.topMessage = dialog.top_message;
        deletedDialog.lastMessageDate = dialog.last_message_date;
        deletedDialog.flags = dialog.flags;
        deletedDialog.entityCreateDate = getConnectionsManager().getCurrentTime();
    }

    public void map(DeletedDialog deletedDialog, TLRPC.Dialog dialog) {
        dialog.id = deletedDialog.dialogId;
        dialog.peer = getMessagesController().getPeer(deletedDialog.peerId);
        Integer num = deletedDialog.folderId;
        if (num != null) {
            dialog.folder_id = num.intValue();
        }
        dialog.top_message = deletedDialog.topMessage;
        dialog.last_message_date = deletedDialog.lastMessageDate;
        dialog.flags = deletedDialog.flags;
    }
}
