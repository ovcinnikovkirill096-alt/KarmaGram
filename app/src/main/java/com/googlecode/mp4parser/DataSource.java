package com.googlecode.mp4parser;

import java.io.Closeable;

public interface DataSource extends Closeable {
    long position();

    void position(long j);
}
