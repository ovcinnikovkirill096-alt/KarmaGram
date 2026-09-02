package j$.util;

import java.io.Serializable;

public final /* synthetic */ class A implements java.util.Comparator, Serializable {
    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((Comparable) ((java.util.Map.Entry) obj).getValue()).compareTo(((java.util.Map.Entry) obj2).getValue());
    }
}
