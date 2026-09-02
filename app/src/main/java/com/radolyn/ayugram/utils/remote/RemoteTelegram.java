package com.radolyn.ayugram.utils.remote;

import com.exteragram.messenger.utils.chats.ChatUtils;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class RemoteTelegram implements RemoteFetcher {
    public static void getMessages(final long j, String str, final Utilities.Callback2 callback2) {
        try {
            final AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
            final TLRPC.TL_messages_getHistory tL_messages_getHistory = new TLRPC.TL_messages_getHistory();
            tL_messages_getHistory.peer = accountInstance.getMessagesController().getInputPeer(j);
            tL_messages_getHistory.offset_id = 0;
            tL_messages_getHistory.limit = 50;
            final Runnable runnable = new Runnable() { // from class: com.radolyn.ayugram.utils.remote.RemoteTelegram$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    accountInstance.getConnectionsManager().sendRequest(tL_messages_getHistory, new RequestDelegate() { // from class: com.radolyn.ayugram.utils.remote.RemoteTelegram$$ExternalSyntheticLambda2
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            RemoteTelegram.$r8$lambda$sxqn5iTI24w7yvre0fjXrC66_g8(callback2, tLObject, tL_error);
                        }
                    });
                }
            };
            if (tL_messages_getHistory.peer.access_hash != 0) {
                AndroidUtilities.runOnUIThread(runnable);
            } else {
                ChatUtils.getInstance().resolveChannel(str, new Utilities.Callback() { // from class: com.radolyn.ayugram.utils.remote.RemoteTelegram$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        RemoteTelegram.$r8$lambda$AhNYCmhBSTve8WhQmQJFrlkDNas(j, tL_messages_getHistory, runnable, (TLRPC.Chat) obj);
                    }
                });
            }
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void $r8$lambda$sxqn5iTI24w7yvre0fjXrC66_g8(Utilities.Callback2 callback2, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null || tLObject == null) {
            callback2.run(null, tL_error);
        } else {
            callback2.run((TLRPC.messages_Messages) tLObject, null);
        }
    }

    public static /* synthetic */ void $r8$lambda$AhNYCmhBSTve8WhQmQJFrlkDNas(long j, TLRPC.TL_messages_getHistory tL_messages_getHistory, Runnable runnable, TLRPC.Chat chat) {
        if (chat == null || chat.id != j) {
            return;
        }
        TLRPC.TL_inputPeerChannel tL_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
        tL_messages_getHistory.peer = tL_inputPeerChannel;
        tL_inputPeerChannel.channel_id = chat.id;
        tL_inputPeerChannel.access_hash = chat.access_hash;
        AndroidUtilities.runOnUIThread(runnable);
    }

    @Override // com.radolyn.ayugram.utils.remote.RemoteFetcher
    public void fetch(final Utilities.Callback2 callback2) {
        Utilities.Callback2 callback3 = new Utilities.Callback2() { // from class: com.radolyn.ayugram.utils.remote.RemoteTelegram.1
            private int count = 0;
            private boolean errored = false;
            private final TLRPC.messages_Messages result = new TLRPC.TL_messages_messages();

            @Override // org.telegram.messenger.Utilities.Callback2
            public void run(TLRPC.messages_Messages messages_messages, TLRPC.TL_error tL_error) {
                try {
                    if (tL_error != null || messages_messages == null) {
                        this.errored = true;
                        callback2.run(null, tL_error);
                    } else {
                        if (this.errored) {
                            return;
                        }
                        this.result.messages.addAll(messages_messages.messages);
                        int i = this.count + 1;
                        this.count = i;
                        if (i == 2) {
                            callback2.run(this.result, null);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        };
        getMessages(-2227431611L, "XS6GEcz5ZXMu82UvXQc", callback3);
        getMessages(-2180718299L, "UFhsu4hOFEufes73fkEeuw", callback3);
    }
}
