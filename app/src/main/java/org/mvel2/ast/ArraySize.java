package org.mvel2.ast;

import java.io.Serializable;

public class ArraySize implements Serializable {
    public char[] value;

    public ArraySize(char[] cArr) {
        this.value = cArr;
    }
}
