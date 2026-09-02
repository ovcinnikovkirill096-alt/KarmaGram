package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.utils.AyuFileUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.seq.AyuSequentialUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.secretmedia.EncryptedFileInputStream;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class AyuAttachments extends BaseController {
    private static final AyuAttachments[] Instance = new AyuAttachments[16];

    public AyuAttachments(int i) {
        super(i);
    }

    public static AyuAttachments getInstance(int i) {
        AyuAttachments ayuAttachments;
        AyuAttachments[] ayuAttachmentsArr = Instance;
        AyuAttachments ayuAttachments2 = ayuAttachmentsArr[i];
        if (ayuAttachments2 != null) {
            return ayuAttachments2;
        }
        synchronized (AyuAttachments.class) {
            try {
                ayuAttachments = ayuAttachmentsArr[i];
                if (ayuAttachments == null) {
                    ayuAttachments = new AyuAttachments(i);
                    ayuAttachmentsArr[i] = ayuAttachments;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuAttachments;
    }

    public String getExistingPath(TLRPC.Message message, boolean z) {
        return getExistingPath(new MessageObject(this.currentAccount, message, false, true), z);
    }

    public String getExistingPath(MessageObject messageObject, boolean z) {
        TLObject media;
        TLObject media2;
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia messageMedia2;
        FileLoader fileLoader = FileLoader.getInstance(messageObject.currentAccount);
        long messageSize = AyuMessageUtils.getMessageSize(messageObject);
        String string = messageObject.messageOwner.attachPath;
        if (!TextUtils.isEmpty(string)) {
            File file = new File(string);
            if (!file.exists() || file.length() != messageSize || file.isDirectory()) {
                string = null;
            }
        }
        if (TextUtils.isEmpty(string)) {
            String string2 = fileLoader.getPathToMessage(messageObject.messageOwner).toString();
            File file2 = new File(string2);
            if (!file2.exists() || file2.length() != messageSize || file2.isDirectory()) {
                string = null;
            }
            if (!TextUtils.isEmpty(string2)) {
                File fileTryEncrypted = tryEncrypted(file2, new File(string2), !z);
                if (!fileTryEncrypted.getAbsolutePath().equals("/")) {
                    return fileTryEncrypted.getAbsolutePath();
                }
            }
        }
        if (TextUtils.isEmpty(string)) {
            string = fileLoader.getPathToAttach(messageObject.getDocument(), null, false).toString();
            File file3 = new File(string);
            if (!file3.exists() || file3.length() != messageSize || file3.isDirectory()) {
                string = null;
            }
        }
        if (TextUtils.isEmpty(string)) {
            string = fileLoader.getPathToAttach(messageObject.getDocument(), null, true).toString();
            File file4 = new File(string);
            if (!file4.exists() || file4.length() != messageSize || file4.isDirectory()) {
                string = null;
            }
        }
        if (TextUtils.isEmpty(string) && (messageMedia2 = messageObject.messageOwner.media) != null) {
            string = fileLoader.getPathToAttach(messageMedia2.photo, null, false).toString();
            File file5 = new File(string);
            if (!file5.exists() || file5.isDirectory()) {
                string = null;
            }
        }
        if (TextUtils.isEmpty(string) && (messageMedia = messageObject.messageOwner.media) != null) {
            string = fileLoader.getPathToAttach(messageMedia.photo, null, true).toString();
            File file6 = new File(string);
            if (!file6.exists() || file6.isDirectory()) {
                string = null;
            }
        }
        if (TextUtils.isEmpty(string) && (media2 = AyuMessageUtils.getMedia(messageObject.messageOwner)) != null) {
            string = fileLoader.getPathToAttach(media2, null, false).toString();
            File file7 = new File(string);
            if (!file7.exists() || file7.isDirectory()) {
                string = null;
            }
        }
        if (z && TextUtils.isEmpty(string)) {
            if (Thread.currentThread() == ApplicationLoader.applicationHandler.getLooper().getThread()) {
                return "/";
            }
            if (ApplicationLoader.isConnectedToWiFi()) {
                if (messageSize > AyuConfig.saveMediaOnWiFiLimit) {
                    return "/";
                }
            } else if (messageSize > AyuConfig.saveMediaOnCellularDataLimit) {
                return "/";
            }
            AyuSequentialUtils.loadDocumentsSync(messageObject.currentAccount, new ArrayList(messageObject) { // from class: com.radolyn.ayugram.controllers.AyuAttachments.1
                final /* synthetic */ MessageObject val$messageObject;

                {
                    this.val$messageObject = messageObject;
                    add(messageObject);
                }
            });
            return getExistingPath(messageObject, false);
        }
        if (TextUtils.isEmpty(string) || new File(string).isDirectory()) {
            string = fileLoader.getPathToMessage(messageObject.messageOwner).toString();
        }
        if (TextUtils.isEmpty(string) || new File(string).isDirectory()) {
            string = fileLoader.getPathToAttach(messageObject.getDocument(), null, false).toString();
        }
        if (TextUtils.isEmpty(string) || new File(string).isDirectory()) {
            string = fileLoader.getPathToAttach(messageObject.getDocument(), null, true).toString();
        }
        if ((TextUtils.isEmpty(string) || new File(string).isDirectory()) && (media = AyuMessageUtils.getMedia(messageObject.messageOwner)) != null) {
            string = fileLoader.getPathToAttach(media, null, false).toString();
        }
        return (TextUtils.isEmpty(string) || new File(string).isDirectory()) ? "/" : string;
    }

    public String getExistingPath(MessageObject messageObject) {
        return getExistingPath(messageObject, true);
    }

    public String getExistingPathPhoto(TLRPC.TL_photoSize tL_photoSize) {
        FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
        String string = "";
        if (TextUtils.isEmpty(string)) {
            string = fileLoader.getPathToAttach(tL_photoSize, null, false, false).toString();
            if (!new File(string).exists()) {
                string = null;
            }
        }
        return TextUtils.isEmpty(string) ? fileLoader.getPathToAttach(tL_photoSize, null, true, false).toString() : string;
    }

    public File processThumb(TLRPC.TL_photoSize tL_photoSize) {
        return copyFile(new File(getExistingPathPhoto(tL_photoSize)), new File(AyuConfig.getSavePathJava(), AyuFileUtils.getFilename(tL_photoSize)), false);
    }

    public File processAttachment(SaveMessageRequest saveMessageRequest) {
        TLRPC.Message message = saveMessageRequest.getMessage();
        return copyFile(new File(getExistingPath(new MessageObject(this.currentAccount, message, false, true))), new File(AyuConfig.getSavePathJava(), AyuFileUtils.getFilename(message)), saveMessageRequest.isForce());
    }

    private File copyFile(File file, File file2, boolean z) {
        File file3 = new File("/");
        boolean zMoveOrCopyFile = false;
        if (file.exists() && !file.isDirectory()) {
            try {
                zMoveOrCopyFile = AyuFileUtils.moveOrCopyFile(file, file2, z);
            } catch (Throwable unused) {
            }
            return zMoveOrCopyFile ? new File(file2.getAbsolutePath()) : file3;
        }
        return tryEncrypted(file, file2, false);
    }

    private File tryEncrypted(File file, File file2, boolean z) {
        File file3 = new File(FileLoader.getDirectory(4), file.getName() + ".enc");
        if (!file3.exists() || (z && file3.length() > 8388608)) {
            return new File("/");
        }
        File file4 = new File(FileLoader.getInternalCacheDir(), file3.getName() + ".key");
        if (file4.exists() && file3.length() > 0 && file4.length() > 0) {
            try {
                EncryptedFileInputStream encryptedFileInputStream = new EncryptedFileInputStream(file3, file4);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int i = encryptedFileInputStream.read(bArr);
                            if (i != -1) {
                                fileOutputStream.write(bArr, 0, i);
                            } else {
                                fileOutputStream.close();
                                encryptedFileInputStream.close();
                                return file2;
                            }
                            try {
                                encryptedFileInputStream.close();
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                } catch (Throwable th4) {
                    encryptedFileInputStream.close();
                    throw th4;
                }
            } catch (Exception unused) {
            }
        }
        return new File("/");
    }

    public boolean shouldSaveMedia(SaveMessageRequest saveMessageRequest) {
        if (!AyuConfig.saveMedia || saveMessageRequest.getMessage().media == null) {
            return false;
        }
        if (DialogObject.isUserDialog(saveMessageRequest.getDialogId())) {
            return AyuConfig.saveMediaInPrivateChats;
        }
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(Math.abs(saveMessageRequest.getDialogId())));
        if (chat == null) {
            return true;
        }
        boolean zIsPublic = ChatObject.isPublic(chat);
        if (ChatObject.isChannel(chat)) {
            if (zIsPublic && AyuConfig.saveMediaInPublicChannels) {
                return true;
            }
            return !zIsPublic && AyuConfig.saveMediaInPrivateChannels;
        }
        if (zIsPublic && AyuConfig.saveMediaInPublicGroups) {
            return true;
        }
        return !zIsPublic && AyuConfig.saveMediaInPrivateGroups;
    }
}
