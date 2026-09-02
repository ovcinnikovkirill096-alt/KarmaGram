package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.util.Map;

public interface DataSource extends DataReader {

    public interface Factory {
        DataSource createDataSource();
    }

    void addTransferListener(TransferListener transferListener);

    void close();

    Map getResponseHeaders();

    Uri getUri();

    long open(DataSpec dataSpec);

    /* JADX INFO: renamed from: com.google.android.exoplayer2.upstream.DataSource$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
    }
}
