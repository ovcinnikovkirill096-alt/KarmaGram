package com.coremedia.iso.boxes;

import java.nio.channels.WritableByteChannel;

public interface Box {
    void getBox(WritableByteChannel writableByteChannel);

    Container getParent();

    long getSize();

    String getType();

    void setParent(Container container);
}
