package com.radolyn.ayugram.controllers.messages;

import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public class SaveMessageRequest {
    private long dialogId;
    private boolean force = false;
    private TLRPC.Message message;
    private int messageId;
    private long monoForumTopicId;
    private int requestCatchTime;
    private long topicId;

    public SaveMessageRequest(TLRPC.Message message, long j, long j2, int i, int i2) {
        this.monoForumTopicId = -1L;
        this.requestCatchTime = -1;
        this.message = message;
        this.dialogId = j;
        this.topicId = j2;
        this.messageId = i;
        this.requestCatchTime = i2 < 1397411401 ? (int) (System.currentTimeMillis() / 1000) : i2;
        this.monoForumTopicId = MessageObject.getMonoForumTopicId(message);
    }

    public SaveMessageRequest(int i, TLRPC.Message message) {
        this.dialogId = -1L;
        this.topicId = -1L;
        this.monoForumTopicId = -1L;
        this.messageId = -1;
        this.requestCatchTime = -1;
        if (message == null) {
            return;
        }
        this.message = message;
        this.dialogId = MessageObject.getDialogId(message);
        this.topicId = MessageObject.getDialogId(this.message) == UserConfig.getInstance(i).getClientUserId() ? 0L : MessageObject.getTopicId(i, message, MessagesController.getInstance(i).isForum(message));
        this.messageId = message.id;
        this.requestCatchTime = (int) (System.currentTimeMillis() / 1000);
        this.monoForumTopicId = MessageObject.getMonoForumTopicId(message);
    }

    public TLRPC.Message getMessage() {
        return this.message;
    }

    public long getDialogId() {
        return this.dialogId;
    }

    public void setDialogId(long j) {
        if (j == 0) {
            return;
        }
        this.dialogId = j;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long j) {
        this.topicId = j;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public int getRequestCatchTime() {
        return this.requestCatchTime;
    }

    public long getMonoForumTopicId() {
        return this.monoForumTopicId;
    }

    public boolean isForce() {
        return this.force;
    }

    public void forceSaveEdited() {
        this.force = true;
    }
}
