package com.google.android.gms.internal.play_billing;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.List;

abstract class zziz {
    public static final /* synthetic */ int $r8$clinit = 0;
    private static final zzjj zzb;

    static {
        int i = zziu.$r8$clinit;
        zzb = new zzjl();
    }

    public static void zzA(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzC(i, list, z);
    }

    public static void zzB(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzE(i, list, z);
    }

    public static void zzC(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzJ(i, list, z);
    }

    public static void zzD(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzL(i, list, z);
    }

    static boolean zzE(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    static int zza(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzhl) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzA = 0;
        for (int i = 0; i < size; i++) {
            iZzA += zzgr.zzA(((Integer) list.get(i)).intValue());
        }
        return iZzA;
    }

    static int zzb(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzgr.zzz(i << 3) + 4);
    }

    static int zzc(List list) {
        return list.size() * 4;
    }

    static int zzd(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzgr.zzz(i << 3) + 8);
    }

    static int zze(List list) {
        return list.size() * 8;
    }

    static int zzf(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzhl) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzA = 0;
        for (int i = 0; i < size; i++) {
            iZzA += zzgr.zzA(((Integer) list.get(i)).intValue());
        }
        return iZzA;
    }

    static int zzg(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzib) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzA = 0;
        for (int i = 0; i < size; i++) {
            iZzA += zzgr.zzA(((Long) list.get(i)).longValue());
        }
        return iZzA;
    }

    static int zzi(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzhl) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzz = 0;
        for (int i = 0; i < size; i++) {
            int iIntValue = ((Integer) list.get(i)).intValue();
            iZzz += zzgr.zzz((iIntValue >> 31) ^ (iIntValue + iIntValue));
        }
        return iZzz;
    }

    static int zzj(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzib) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzA = 0;
        for (int i = 0; i < size; i++) {
            long jLongValue = ((Long) list.get(i)).longValue();
            iZzA += zzgr.zzA((jLongValue >> 63) ^ (jLongValue + jLongValue));
        }
        return iZzA;
    }

    static int zzk(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzhl) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzz = 0;
        for (int i = 0; i < size; i++) {
            iZzz += zzgr.zzz(((Integer) list.get(i)).intValue());
        }
        return iZzz;
    }

    static int zzl(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzib) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int iZzA = 0;
        for (int i = 0; i < size; i++) {
            iZzA += zzgr.zzA(((Long) list.get(i)).longValue());
        }
        return iZzA;
    }

    public static zzjj zzm() {
        return zzb;
    }

    static void zzo(zzgx zzgxVar, Object obj, Object obj2) {
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj2);
        throw null;
    }

    static void zzp(zzjj zzjjVar, Object obj, Object obj2) {
        zzhk zzhkVar = (zzhk) obj;
        zzjk zzjkVarZze = zzhkVar.zzc;
        zzjk zzjkVar = ((zzhk) obj2).zzc;
        if (!zzjk.zzc().equals(zzjkVar)) {
            if (zzjk.zzc().equals(zzjkVarZze)) {
                zzjkVarZze = zzjk.zze(zzjkVarZze, zzjkVar);
            } else {
                zzjkVarZze.zzd(zzjkVar);
            }
        }
        zzhkVar.zzc = zzjkVarZze;
    }

    public static void zzq(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzc(i, list, z);
    }

    public static void zzr(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzg(i, list, z);
    }

    public static void zzs(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzj(i, list, z);
    }

    public static void zzt(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzl(i, list, z);
    }

    public static void zzu(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzn(i, list, z);
    }

    public static void zzv(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzp(i, list, z);
    }

    public static void zzw(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzs(i, list, z);
    }

    public static void zzx(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzu(i, list, z);
    }

    public static void zzy(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzy(i, list, z);
    }

    public static void zzz(int i, List list, zzjw zzjwVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzjwVar.zzA(i, list, z);
    }

    static int zzh(int i, Object obj, zzix zzixVar) {
        return zzgr.zzz(i << 3) + zzgr.zzx((zzim) obj, zzixVar);
    }
}
