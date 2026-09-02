package com.radolyn.ayugram.utils.remote;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class RemoteHttp implements RemoteFetcher {
    @Override // com.radolyn.ayugram.utils.remote.RemoteFetcher
    public void fetch(final Utilities.Callback2 callback2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.remote.RemoteHttp$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RemoteHttpImpl.getConfig(new RequestDelegate() { // from class: com.radolyn.ayugram.utils.remote.RemoteHttp$$ExternalSyntheticLambda1
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        RemoteHttp.$r8$lambda$B1LUTuFEhruKTuJLMkr950V5sJI(callback2, tLObject, tL_error);
                    }
                });
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$B1LUTuFEhruKTuJLMkr950V5sJI(Utilities.Callback2 callback2, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null || tLObject == null) {
            callback2.run(null, tL_error);
        } else {
            callback2.run((TLRPC.messages_Messages) tLObject, null);
        }
    }
}
