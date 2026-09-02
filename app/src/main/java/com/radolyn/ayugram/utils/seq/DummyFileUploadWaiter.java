package com.radolyn.ayugram.utils.seq;

import android.text.TextUtils;
import org.telegram.messenger.NotificationCenter;

public class DummyFileUploadWaiter extends SyncWaiter {
    private int messageId;
    private final String path;

    public DummyFileUploadWaiter(int i, String str) {
        super(i);
        this.path = str;
        this.notifications.add(Integer.valueOf(NotificationCenter.fileUploaded));
        this.notifications.add(Integer.valueOf(NotificationCenter.fileUploadFailed));
        this.notifications.add(Integer.valueOf(NotificationCenter.filePreparingFailed));
        this.notifications.add(Integer.valueOf(NotificationCenter.messageReceivedByServer));
    }

    public void setMessageId(int i) {
        this.messageId = i;
    }

    private void process(String str) {
        if (this.path.contains(str) || (!TextUtils.isEmpty(str) && str.endsWith(this.path))) {
            unsubscribe();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.fileUploaded || i == NotificationCenter.fileUploadFailed) {
            process((String) objArr[0]);
            return;
        }
        if (i == NotificationCenter.filePreparingFailed) {
            process((String) objArr[1]);
            return;
        }
        if (i == NotificationCenter.messageReceivedByServer) {
            if (this.messageId != ((Integer) objArr[0]).intValue() || this.messageId == 0) {
                return;
            }
            unsubscribe();
        }
    }
}
