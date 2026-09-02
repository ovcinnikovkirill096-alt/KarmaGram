package com.android.dx.io.instructions;

public interface CodeInput extends CodeCursor {
    boolean hasMore();

    int read();

    int readInt();

    long readLong();
}
