package com.radolyn.ayugram.controllers.messages;

import com.radolyn.ayugram.controllers.AyuMessagesController;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;

public class MessageDeleteWrapper {
    private final AyuMessagesController controller;
    private final DeletedMessageService deletedMessageService;
    private boolean deletedSmth = false;

    public MessageDeleteWrapper(AyuMessagesController ayuMessagesController, DeletedMessageService deletedMessageService) {
        this.controller = ayuMessagesController;
        this.deletedMessageService = deletedMessageService;
    }

    public MessageDeleteWrapper deleteMessage(long j, int i) {
        this.deletedSmth = this.deletedMessageService.deleteMessage(j, i) | this.deletedSmth;
        return this;
    }

    public void commit() {
        if (this.deletedSmth) {
            this.controller.executeAsync(new Runnable() { // from class: com.radolyn.ayugram.controllers.messages.MessageDeleteWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$commit$0();
                }
            }, "loadLastMessages");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commit$0() {
        this.controller.getDeletedDialogServiceInternal().loadLastMessages();
    }
}
