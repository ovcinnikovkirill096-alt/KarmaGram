package com.google.android.recaptcha.internal;

import j$.util.DesugarCollections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class zzjo extends zzjs {
    private static final Class zza = DesugarCollections.unmodifiableList(Collections.EMPTY_LIST).getClass();

    private zzjo() {
        super(null);
    }

    /* synthetic */ zzjo(zzjn zzjnVar) {
        super(null);
    }

    private static List zzf(Object obj, long j, int i) {
        List listZzd;
        List list = (List) zzlv.zzf(obj, j);
        if (list.isEmpty()) {
            if (list instanceof zzjm) {
                listZzd = new zzjl(i);
            } else {
                listZzd = ((list instanceof zzkm) && (list instanceof zzjb)) ? ((zzjb) list).zzd(i) : new ArrayList(i);
            }
            zzlv.zzs(obj, j, listZzd);
            return listZzd;
        }
        if (zza.isAssignableFrom(list.getClass())) {
            ArrayList arrayList = new ArrayList(list.size() + i);
            arrayList.addAll(list);
            zzlv.zzs(obj, j, arrayList);
            return arrayList;
        }
        if (list instanceof zzlq) {
            zzjl zzjlVar = new zzjl(list.size() + i);
            zzjlVar.addAll(zzjlVar.size(), (zzlq) list);
            zzlv.zzs(obj, j, zzjlVar);
            return zzjlVar;
        }
        if ((list instanceof zzkm) && (list instanceof zzjb)) {
            zzjb zzjbVar = (zzjb) list;
            if (!zzjbVar.zzc()) {
                zzjb zzjbVarZzd = zzjbVar.zzd(list.size() + i);
                zzlv.zzs(obj, j, zzjbVarZzd);
                return zzjbVarZzd;
            }
        }
        return list;
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final List zza(Object obj, long j) {
        return zzf(obj, j, 10);
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzb(Object obj, long j) {
        Object objUnmodifiableList;
        List list = (List) zzlv.zzf(obj, j);
        if (list instanceof zzjm) {
            objUnmodifiableList = ((zzjm) list).zze();
        } else {
            if (zza.isAssignableFrom(list.getClass())) {
                return;
            }
            if ((list instanceof zzkm) && (list instanceof zzjb)) {
                zzjb zzjbVar = (zzjb) list;
                if (zzjbVar.zzc()) {
                    zzjbVar.zzb();
                    return;
                }
                return;
            }
            objUnmodifiableList = DesugarCollections.unmodifiableList(list);
        }
        zzlv.zzs(obj, j, objUnmodifiableList);
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzc(Object obj, Object obj2, long j) {
        List list = (List) zzlv.zzf(obj2, j);
        List listZzf = zzf(obj, j, list.size());
        int size = listZzf.size();
        int size2 = list.size();
        if (size > 0 && size2 > 0) {
            listZzf.addAll(list);
        }
        if (size > 0) {
            list = listZzf;
        }
        zzlv.zzs(obj, j, list);
    }
}
