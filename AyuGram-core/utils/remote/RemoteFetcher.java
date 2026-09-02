package com.radolyn.ayugram.utils.remote;

import org.telegram.messenger.Utilities;

public interface RemoteFetcher {
    void fetch(Utilities.Callback2 callback2);
}
