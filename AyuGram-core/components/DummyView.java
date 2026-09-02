package com.radolyn.ayugram.components;

import android.content.Context;
import android.view.View;
import org.telegram.messenger.MessageObject;

public class DummyView extends View {
    private MessageObject messageObject;

    public DummyView(Context context) {
        super(context);
        setMinimumHeight(1);
    }

    public MessageObject getMessageObject() {
        return this.messageObject;
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
    }
}
