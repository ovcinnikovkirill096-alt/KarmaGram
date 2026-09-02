package com.radolyn.ayugram.utils.seq;

import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;

public class DummyMessageWaiter extends SyncWaiter {
    private final ArrayList alreadySent;
    private long dialogId;
    public int sendingId;

    public DummyMessageWaiter(int i) {
        super(i);
        this.alreadySent = new ArrayList();
        this.notifications.add(Integer.valueOf(NotificationCenter.messageReceivedByServer));
        this.notifications.add(Integer.valueOf(NotificationCenter.messageSendError));
        this.notifications.add(Integer.valueOf(NotificationCenter.messageReceivedByAck));
        this.notifications.add(Integer.valueOf(NotificationCenter.messagesDeleted));
    }

    public void trySetSendingId(long j) {
        if (j == 0) {
            j = UserConfig.getInstance(this.currentAccount).getClientUserId();
        }
        this.dialogId = j;
        SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(this.currentAccount);
        long jCurrentTimeMillis = System.currentTimeMillis();
        int sendingMessageId = 0;
        while (sendingMessageId >= 0) {
            try {
                sendingMessageId = sendMessagesHelper.getSendingMessageId(j);
            } catch (Exception unused) {
            }
            if (System.currentTimeMillis() - jCurrentTimeMillis > 3500) {
                unsubscribe();
                break;
            }
        }
        if (sendingMessageId != 0) {
            setSendingId(sendingMessageId);
        }
    }

    private void setSendingId(int i) {
        this.sendingId = i;
        if (this.alreadySent.isEmpty() || !this.alreadySent.contains(Integer.valueOf(i))) {
            return;
        }
        unsubscribe();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3 = 0;
        if (i == NotificationCenter.messageReceivedByAck || i == NotificationCenter.messageReceivedByServer || i == NotificationCenter.messageSendError) {
            Integer num = (Integer) objArr[0];
            if (this.sendingId == 0) {
                this.alreadySent.add(num);
                return;
            } else {
                if (num.intValue() == this.sendingId) {
                    unsubscribe();
                    return;
                }
                return;
            }
        }
        if (i == NotificationCenter.messagesDeleted) {
            ArrayList arrayList = (ArrayList) objArr[0];
            Long l = (Long) objArr[1];
            if (Math.abs(l.longValue()) == Math.abs(this.dialogId) || l.longValue() == 0 || this.dialogId == 0) {
                if (this.sendingId == 0) {
                    this.alreadySent.addAll(arrayList);
                    return;
                }
                int size = arrayList.size();
                while (i3 < size) {
                    Object obj = arrayList.get(i3);
                    i3++;
                    if (((Integer) obj).intValue() == this.sendingId) {
                        unsubscribe();
                        return;
                    }
                }
            }
        }
    }
}
