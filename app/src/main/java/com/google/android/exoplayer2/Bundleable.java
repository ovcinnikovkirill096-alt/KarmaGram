package com.google.android.exoplayer2;

import android.os.Bundle;

public interface Bundleable {

    public interface Creator {
        Bundleable fromBundle(Bundle bundle);
    }

    Bundle toBundle();
}
