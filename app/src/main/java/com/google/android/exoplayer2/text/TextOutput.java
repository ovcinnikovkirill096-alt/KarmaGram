package com.google.android.exoplayer2.text;

import java.util.List;

public interface TextOutput {
    void onCues(CueGroup cueGroup);

    void onCues(List list);
}
