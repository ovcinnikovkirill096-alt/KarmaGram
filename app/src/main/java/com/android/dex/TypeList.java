package com.android.dex;

import com.android.dex.util.Unsigned;
import java.io.Serializable;

public final class TypeList implements Comparable {
    public static final TypeList EMPTY = new TypeList(null, Dex.EMPTY_SHORT_ARRAY);
    private final Dex dex;
    private final short[] types;

    public TypeList(Dex dex, short[] sArr) {
        this.dex = dex;
        this.types = sArr;
    }

    public short[] getTypes() {
        return this.types;
    }

    @Override // java.lang.Comparable
    public int compareTo(TypeList typeList) {
        short[] sArr;
        int i = 0;
        while (true) {
            sArr = this.types;
            if (i >= sArr.length) {
                break;
            }
            short[] sArr2 = typeList.types;
            if (i >= sArr2.length) {
                break;
            }
            short s = sArr[i];
            short s2 = sArr2[i];
            if (s != s2) {
                return Unsigned.compare(s, s2);
            }
            i++;
        }
        return Unsigned.compare(sArr.length, typeList.types.length);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int length = this.types.length;
        for (int i = 0; i < length; i++) {
            Dex dex = this.dex;
            sb.append(dex != null ? (Serializable) dex.typeNames().get(this.types[i]) : Short.valueOf(this.types[i]));
        }
        sb.append(")");
        return sb.toString();
    }
}
