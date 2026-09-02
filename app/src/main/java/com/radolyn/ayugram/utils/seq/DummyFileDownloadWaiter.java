package com.radolyn.ayugram.utils.seq;

import android.text.TextUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import java.util.ArrayList;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;

public class DummyFileDownloadWaiter extends SyncWaiter {
    private final ArrayList messages;
    public final Object messagesLock;

    public DummyFileDownloadWaiter(int i, ArrayList arrayList) {
        super(i);
        this.messagesLock = new Object();
        this.messages = arrayList;
        this.notifications.add(Integer.valueOf(NotificationCenter.fileLoaded));
        this.notifications.add(Integer.valueOf(NotificationCenter.fileLoadFailed));
        this.notifications.add(Integer.valueOf(NotificationCenter.httpFileDidLoad));
        this.notifications.add(Integer.valueOf(NotificationCenter.httpFileDidFailedLoad));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: process, reason: merged with bridge method [inline-methods] */
    public void lambda$didReceivedNotification$0(String str) {
        synchronized (this.messagesLock) {
            try {
                ArrayList arrayList = this.messages;
                int size = arrayList.size();
                int i = 0;
                loop0: while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    MessageObject messageObject = (MessageObject) obj;
                    String[] strArr = {messageObject.getDocumentName(), FileLoader.getAttachFileName(messageObject.getDocument()), FileLoader.getAttachFileName(MessageObject.getPhoto(messageObject.messageOwner)), FileLoader.getInstance(this.currentAccount).getPathToAttach(messageObject.getDocument()).toString(), FileLoader.getInstance(this.currentAccount).getPathToAttach(messageObject.getDocument(), true).toString(), FileLoader.getInstance(this.currentAccount).getPathToAttach(messageObject.messageOwner.media.photo).toString(), FileLoader.getInstance(this.currentAccount).getPathToAttach(messageObject.messageOwner.media.photo, true).toString(), FileLoader.getInstance(this.currentAccount).getPathToAttach(AyuMessageUtils.getMedia(messageObject.messageOwner)).toString(), messageObject.messageOwner.attachPath};
                    for (int i2 = 0; i2 < 9; i2++) {
                        String str2 = strArr[i2];
                        if (!TextUtils.isEmpty(str2) && !str2.endsWith("/cache")) {
                            if (str2.contains(str)) {
                                this.messages.remove(messageObject);
                                break loop0;
                            }
                        }
                    }
                }
                if (this.messages.isEmpty()) {
                    unsubscribe();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.fileLoaded || i == NotificationCenter.httpFileDidLoad || i == NotificationCenter.fileLoadFailed || i == NotificationCenter.httpFileDidFailedLoad) {
            final String str = (String) objArr[0];
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: com.radolyn.ayugram.utils.seq.DummyFileDownloadWaiter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$didReceivedNotification$0(str);
                }
            });
        }
    }
}
