package com.google.android.gms.internal.cast;

import j$.util.DesugarCollections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class zzuh extends zzul {
    private static final Class zza = DesugarCollections.unmodifiableList(Collections.EMPTY_LIST).getClass();

    /* synthetic */ zzuh(zzug zzugVar) {
        super(null);
    }

    @Override // com.google.android.gms.internal.cast.zzul
    final void zza(Object obj, long j) {
        Object objUnmodifiableList;
        List list = (List) zzwj.zzf(obj, j);
        if (list instanceof zzuf) {
            objUnmodifiableList = ((zzuf) list).zzd();
        } else {
            if (zza.isAssignableFrom(list.getClass())) {
                return;
            }
            if ((list instanceof zzve) && (list instanceof zztx)) {
                zztx zztxVar = (zztx) list;
                if (zztxVar.zzc()) {
                    zztxVar.zzb();
                    return;
                }
                return;
            }
            objUnmodifiableList = DesugarCollections.unmodifiableList(list);
        }
        zzwj.zzs(obj, j, objUnmodifiableList);
    }

    @Override // com.google.android.gms.internal.cast.zzul
    final void zzb(Object obj, Object obj2, long j) {
        List list;
        List list2;
        List listZzg;
        List list3 = (List) zzwj.zzf(obj2, j);
        int size = list3.size();
        List list4 = (List) zzwj.zzf(obj, j);
        if (list4.isEmpty()) {
            if (list4 instanceof zzuf) {
                listZzg = new zzue(size);
            } else {
                listZzg = ((list4 instanceof zzve) && (list4 instanceof zztx)) ? ((zztx) list4).zzg(size) : new ArrayList(size);
            }
            zzwj.zzs(obj, j, listZzg);
            list2 = listZzg;
        } else {
            if (zza.isAssignableFrom(list4.getClass())) {
                ArrayList arrayList = new ArrayList(list4.size() + size);
                arrayList.addAll(list4);
                zzwj.zzs(obj, j, arrayList);
                list = arrayList;
            } else if (list4 instanceof zzwe) {
                zzue zzueVar = new zzue(list4.size() + size);
                zzueVar.addAll(zzueVar.size(), (zzwe) list4);
                zzwj.zzs(obj, j, zzueVar);
                list = zzueVar;
            } else if ((list4 instanceof zzve) && (list4 instanceof zztx)) {
                zztx zztxVar = (zztx) list4;
                if (!zztxVar.zzc()) {
                    list2 = list4;
                    list2 = list4;
                    list2 = list4;
                    zztx zztxVarZzg = zztxVar.zzg(list4.size() + size);
                    zzwj.zzs(obj, j, zztxVarZzg);
                    list2 = zztxVarZzg;
                }
            }
            list2 = list;
        }
        list2 = list4;
        list2 = list4;
        list2 = list4;
        list2 = list4;
        list2 = list4;
        list2 = list4;
        int size2 = list2.size();
        int size3 = list3.size();
        if (size2 > 0 && size3 > 0) {
            list2.addAll(list3);
        }
        if (size2 > 0) {
            list3 = list2;
        }
        zzwj.zzs(obj, j, list3);
    }
}
