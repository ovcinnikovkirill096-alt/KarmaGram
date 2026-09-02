package com.yandex.runtime.internal;

import java.io.File;

class AvailableSpace {
    AvailableSpace() {
    }

    public static long getAvailableSpaceOnFilesystem(String str) {
        return new File(str).getUsableSpace();
    }
}
