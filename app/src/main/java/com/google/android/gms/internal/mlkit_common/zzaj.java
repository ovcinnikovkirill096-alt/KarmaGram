package com.google.android.gms.internal.mlkit_common;

import java.util.Set;

public abstract class zzaj extends zzab implements Set, j$.util.Set {
    private transient zzaf zza;

    zzaj() {
    }

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this || obj == this) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            try {
                return size() == set.size() && containsAll(set);
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        return zzar.zza(this);
    }

    public final zzaf zzf() {
        zzaf zzafVar = this.zza;
        if (zzafVar != null) {
            return zzafVar;
        }
        zzaf zzafVarZzg = zzg();
        this.zza = zzafVarZzg;
        return zzafVarZzg;
    }

    zzaf zzg() {
        Object[] array = toArray();
        int i = zzaf.$r8$clinit;
        return zzaf.zzg(array, array.length);
    }
}
