package com.google.android.gms.internal.clearcut;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.internal.url._UrlKt;
import sun.misc.Unsafe;

final class zzds implements zzef {
    private static final Unsafe zzmh = zzfd.zzef();
    private final int[] zzmi;
    private final Object[] zzmj;
    private final int zzmk;
    private final int zzml;
    private final int zzmm;
    private final zzdo zzmn;
    private final boolean zzmo;
    private final boolean zzmp;
    private final boolean zzmq;
    private final boolean zzmr;
    private final int[] zzms;
    private final int[] zzmt;
    private final int[] zzmu;
    private final zzdw zzmv;
    private final zzcy zzmw;
    private final zzex zzmx;
    private final zzbu zzmy;
    private final zzdj zzmz;

    private zzds(int[] iArr, Object[] objArr, int i, int i2, int i3, zzdo zzdoVar, boolean z, boolean z2, int[] iArr2, int[] iArr3, int[] iArr4, zzdw zzdwVar, zzcy zzcyVar, zzex zzexVar, zzbu zzbuVar, zzdj zzdjVar) {
        this.zzmi = iArr;
        this.zzmj = objArr;
        this.zzmk = i;
        this.zzml = i2;
        this.zzmm = i3;
        this.zzmp = zzdoVar instanceof zzcg;
        this.zzmq = z;
        this.zzmo = zzbuVar != null && zzbuVar.zze(zzdoVar);
        this.zzmr = false;
        this.zzms = iArr2;
        this.zzmt = iArr3;
        this.zzmu = iArr4;
        this.zzmv = zzdwVar;
        this.zzmw = zzcyVar;
        this.zzmx = zzexVar;
        this.zzmy = zzbuVar;
        this.zzmn = zzdoVar;
        this.zzmz = zzdjVar;
    }

    private static int zza(int i, byte[] bArr, int i2, int i3, Object obj, zzay zzayVar) {
        return zzax.zza(i, bArr, i2, i3, zzn(obj), zzayVar);
    }

    private static int zza(zzef zzefVar, int i, byte[] bArr, int i2, int i3, zzcn zzcnVar, zzay zzayVar) throws zzco {
        int iZza = zza(zzefVar, bArr, i2, i3, zzayVar);
        while (true) {
            zzcnVar.add(zzayVar.zzff);
            if (iZza >= i3) {
                break;
            }
            int iZza2 = zzax.zza(bArr, iZza, zzayVar);
            if (i != zzayVar.zzfd) {
                break;
            }
            iZza = zza(zzefVar, bArr, iZza2, i3, zzayVar);
        }
        return iZza;
    }

    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, int i3, zzay zzayVar) throws zzco {
        zzds zzdsVar = (zzds) zzefVar;
        Object objNewInstance = zzdsVar.newInstance();
        int iZza = zzdsVar.zza(objNewInstance, bArr, i, i2, i3, zzayVar);
        zzdsVar.zzc(objNewInstance);
        zzayVar.zzff = objNewInstance;
        return iZza;
    }

    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, zzay zzayVar) throws zzco {
        int iZza = i + 1;
        int i3 = bArr[i];
        if (i3 < 0) {
            iZza = zzax.zza(i3, bArr, iZza, zzayVar);
            i3 = zzayVar.zzfd;
        }
        int i4 = iZza;
        if (i3 < 0 || i3 > i2 - i4) {
            throw zzco.zzbl();
        }
        Object objNewInstance = zzefVar.newInstance();
        int i5 = i4 + i3;
        zzefVar.zza(objNewInstance, bArr, i4, i5, zzayVar);
        zzefVar.zzc(objNewInstance);
        zzayVar.zzff = objNewInstance;
        return i5;
    }

    private static int zza(zzex zzexVar, Object obj) {
        return zzexVar.zzm(zzexVar.zzq(obj));
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzay zzayVar) throws zzco {
        int i9;
        Object objValueOf;
        int i10;
        Object objValueOf2;
        int iZzb;
        long jZza;
        int iZzm;
        Object objValueOf3;
        Object object;
        Unsafe unsafe = zzmh;
        long j2 = this.zzmi[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                i9 = i;
                if (i5 != 1) {
                    return i9;
                }
                objValueOf = Double.valueOf(zzax.zze(bArr, i));
                unsafe.putObject(obj, j, objValueOf);
                iZzb = i9 + 8;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 52:
                i10 = i;
                if (i5 != 5) {
                    return i10;
                }
                objValueOf2 = Float.valueOf(zzax.zzf(bArr, i));
                unsafe.putObject(obj, j, objValueOf2);
                iZzb = i10 + 4;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 53:
            case 54:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzax.zzb(bArr, i, zzayVar);
                jZza = zzayVar.zzfe;
                objValueOf3 = Long.valueOf(jZza);
                unsafe.putObject(obj, j, objValueOf3);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 55:
            case 62:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzax.zza(bArr, i, zzayVar);
                iZzm = zzayVar.zzfd;
                objValueOf3 = Integer.valueOf(iZzm);
                unsafe.putObject(obj, j, objValueOf3);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 56:
            case 65:
                i9 = i;
                if (i5 != 1) {
                    return i9;
                }
                objValueOf = Long.valueOf(zzax.zzd(bArr, i));
                unsafe.putObject(obj, j, objValueOf);
                iZzb = i9 + 8;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 57:
            case 64:
                i10 = i;
                if (i5 != 5) {
                    return i10;
                }
                objValueOf2 = Integer.valueOf(zzax.zzc(bArr, i));
                unsafe.putObject(obj, j, objValueOf2);
                iZzb = i10 + 4;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 58:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzax.zzb(bArr, i, zzayVar);
                objValueOf3 = Boolean.valueOf(zzayVar.zzfe != 0);
                unsafe.putObject(obj, j, objValueOf3);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 59:
                if (i5 != 2) {
                    return i;
                }
                int iZza = zzax.zza(bArr, i, zzayVar);
                int i11 = zzayVar.zzfd;
                if (i11 == 0) {
                    unsafe.putObject(obj, j, _UrlKt.FRAGMENT_ENCODE_SET);
                } else {
                    if ((i6 & 536870912) != 0 && !zzff.zze(bArr, iZza, iZza + i11)) {
                        throw zzco.zzbp();
                    }
                    unsafe.putObject(obj, j, new String(bArr, iZza, i11, zzci.UTF_8));
                    iZza += i11;
                }
                unsafe.putInt(obj, j2, i4);
                return iZza;
            case 60:
                if (i5 != 2) {
                    return i;
                }
                int iZza2 = zza(zzad(i8), bArr, i, i2, zzayVar);
                object = unsafe.getInt(obj, j2) == i4 ? unsafe.getObject(obj, j) : null;
                Object objZza = zzayVar.zzff;
                if (object != null) {
                    objZza = zzci.zza(object, objZza);
                }
                unsafe.putObject(obj, j, objZza);
                unsafe.putInt(obj, j2, i4);
                return iZza2;
            case 61:
                if (i5 != 2) {
                    return i;
                }
                int iZza3 = zzax.zza(bArr, i, zzayVar);
                int i12 = zzayVar.zzfd;
                if (i12 == 0) {
                    unsafe.putObject(obj, j, zzbb.zzfi);
                } else {
                    unsafe.putObject(obj, j, zzbb.zzb(bArr, iZza3, i12));
                    iZza3 += i12;
                }
                unsafe.putInt(obj, j2, i4);
                return iZza3;
            case 63:
                if (i5 != 0) {
                    return i;
                }
                int iZza4 = zzax.zza(bArr, i, zzayVar);
                int i13 = zzayVar.zzfd;
                zzck zzckVarZzaf = zzaf(i8);
                if (zzckVarZzaf != null && zzckVarZzaf.zzb(i13) == null) {
                    zzn(obj).zzb(i3, Long.valueOf(i13));
                    return iZza4;
                }
                unsafe.putObject(obj, j, Integer.valueOf(i13));
                iZzb = iZza4;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 66:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzax.zza(bArr, i, zzayVar);
                iZzm = zzbk.zzm(zzayVar.zzfd);
                objValueOf3 = Integer.valueOf(iZzm);
                unsafe.putObject(obj, j, objValueOf3);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 67:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzax.zzb(bArr, i, zzayVar);
                jZza = zzbk.zza(zzayVar.zzfe);
                objValueOf3 = Long.valueOf(jZza);
                unsafe.putObject(obj, j, objValueOf3);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 68:
                if (i5 == 3) {
                    iZzb = zza(zzad(i8), bArr, i, i2, (i3 & (-8)) | 4, zzayVar);
                    object = unsafe.getInt(obj, j2) == i4 ? unsafe.getObject(obj, j) : null;
                    objValueOf3 = zzayVar.zzff;
                    if (object != null) {
                        objValueOf3 = zzci.zza(object, objValueOf3);
                    }
                    unsafe.putObject(obj, j, objValueOf3);
                    unsafe.putInt(obj, j2, i4);
                    return iZzb;
                }
            default:
                return i;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:216:0x0174 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:87:0x0185  */
    /* JADX WARN: Code duplicated, block: B:89:0x018d  */
    /* JADX WARN: Code duplicated, block: B:92:0x0196  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:104:0x01c8 -> B:95:0x01a1). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:71:0x0139 -> B:65:0x011b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:90:0x0193 -> B:83:0x0174). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    private final int zza(java.lang.Object r12, byte[] r13, int r14, int r15, int r16, int r17, int r18, int r19, long r20, int r22, long r23, com.google.android.gms.internal.clearcut.zzay r25) throws com.google.android.gms.internal.clearcut.zzco {
        /*
            Method dump skipped, instruction units count: 822
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.clearcut.zzds.zza(java.lang.Object, byte[], int, int, int, int, int, int, long, int, long, com.google.android.gms.internal.clearcut.zzay):int");
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, long j, zzay zzayVar) throws zzco {
        Unsafe unsafe = zzmh;
        Object objZzae = zzae(i3);
        Object object = unsafe.getObject(obj, j);
        if (this.zzmz.zzi(object)) {
            Object objZzk = this.zzmz.zzk(objZzae);
            this.zzmz.zzb(objZzk, object);
            unsafe.putObject(obj, j, objZzk);
            object = objZzk;
        }
        this.zzmz.zzl(objZzae);
        this.zzmz.zzg(object);
        int iZza = zzax.zza(bArr, i, zzayVar);
        int i5 = zzayVar.zzfd;
        if (i5 < 0 || i5 > i2 - iZza) {
            throw zzco.zzbl();
        }
        throw null;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 11581. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    private final int zza(java.lang.Object r23, byte[] r24, int r25, int r26, int r8, com.google.android.gms.internal.clearcut.zzay r28) throws com.google.android.gms.internal.clearcut.zzco {
        /*
            Method dump skipped, instruction units count: 1158
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.clearcut.zzds.zza(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.clearcut.zzay):int");
    }

    static zzds zza(Class cls, zzdm zzdmVar, zzdw zzdwVar, zzcy zzcyVar, zzex zzexVar, zzbu zzbuVar, zzdj zzdjVar) {
        int iZzcu;
        int i;
        int i2;
        int iZza;
        int iZza2;
        int iZzdg;
        if (!(zzdmVar instanceof zzec)) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzdmVar);
            throw null;
        }
        zzec zzecVar = (zzec) zzdmVar;
        boolean z = zzecVar.zzcf() == zzcg.zzg.zzkm;
        if (zzecVar.getFieldCount() == 0) {
            iZzcu = 0;
            i = 0;
            i2 = 0;
        } else {
            int iZzcp = zzecVar.zzcp();
            int iZzcq = zzecVar.zzcq();
            iZzcu = zzecVar.zzcu();
            i = iZzcp;
            i2 = iZzcq;
        }
        int[] iArr = new int[iZzcu << 2];
        Object[] objArr = new Object[iZzcu << 1];
        int[] iArr2 = zzecVar.zzcr() > 0 ? new int[zzecVar.zzcr()] : null;
        int[] iArr3 = zzecVar.zzcs() > 0 ? new int[zzecVar.zzcs()] : null;
        zzed zzedVarZzco = zzecVar.zzco();
        if (zzedVarZzco.next()) {
            int iZzcx = zzedVarZzco.zzcx();
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                if (iZzcx >= zzecVar.zzcv() || i3 >= ((iZzcx - i) << 2)) {
                    if (zzedVarZzco.zzda()) {
                        iZza = (int) zzfd.zza(zzedVarZzco.zzdb());
                        iZza2 = (int) zzfd.zza(zzedVarZzco.zzdc());
                        iZzdg = 0;
                    } else {
                        iZza = (int) zzfd.zza(zzedVarZzco.zzdd());
                        if (zzedVarZzco.zzde()) {
                            iZza2 = (int) zzfd.zza(zzedVarZzco.zzdf());
                            iZzdg = zzedVarZzco.zzdg();
                        } else {
                            iZza2 = 0;
                            iZzdg = 0;
                        }
                    }
                    iArr[i3] = zzedVarZzco.zzcx();
                    int i6 = i3 + 1;
                    iArr[i6] = (zzedVarZzco.zzdi() ? 536870912 : 0) | (zzedVarZzco.zzdh() ? 268435456 : 0) | (zzedVarZzco.zzcy() << 20) | iZza;
                    iArr[i3 + 2] = iZza2 | (iZzdg << 20);
                    if (zzedVarZzco.zzdl() != null) {
                        int i7 = (i3 / 4) << 1;
                        objArr[i7] = zzedVarZzco.zzdl();
                        if (zzedVarZzco.zzdj() != null) {
                            objArr[i7 + 1] = zzedVarZzco.zzdj();
                        } else if (zzedVarZzco.zzdk() != null) {
                            objArr[i7 + 1] = zzedVarZzco.zzdk();
                        }
                    } else if (zzedVarZzco.zzdj() != null) {
                        objArr[((i3 / 4) << 1) + 1] = zzedVarZzco.zzdj();
                    } else if (zzedVarZzco.zzdk() != null) {
                        objArr[((i3 / 4) << 1) + 1] = zzedVarZzco.zzdk();
                    }
                    int iZzcy = zzedVarZzco.zzcy();
                    if (iZzcy == zzcb.zziw.ordinal()) {
                        iArr2[i4] = i3;
                        i4++;
                    } else if (iZzcy >= 18 && iZzcy <= 49) {
                        iArr3[i5] = iArr[i6] & 1048575;
                        i5++;
                    }
                    if (!zzedVarZzco.next()) {
                        break;
                    }
                    iZzcx = zzedVarZzco.zzcx();
                } else {
                    for (int i8 = 0; i8 < 4; i8++) {
                        iArr[i3 + i8] = -1;
                    }
                }
                i3 += 4;
            }
        }
        return new zzds(iArr, objArr, i, i2, zzecVar.zzcv(), zzecVar.zzch(), z, false, zzecVar.zzct(), iArr2, iArr3, zzdwVar, zzcyVar, zzexVar, zzbuVar, zzdjVar);
    }

    private final Object zza(int i, int i2, Map map, zzck zzckVar, Object obj, zzex zzexVar) {
        this.zzmz.zzl(zzae(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (zzckVar.zzb(((Integer) entry.getValue()).intValue()) == null) {
                if (obj == null) {
                    obj = zzexVar.zzdz();
                }
                zzbg zzbgVarZzk = zzbb.zzk(zzdg.zza(null, entry.getKey(), entry.getValue()));
                try {
                    zzdg.zza(zzbgVarZzk.zzae(), null, entry.getKey(), entry.getValue());
                    zzexVar.zza(obj, i2, zzbgVarZzk.zzad());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    private static void zza(int i, Object obj, zzfr zzfrVar) {
        if (obj instanceof String) {
            zzfrVar.zza(i, (String) obj);
        } else {
            zzfrVar.zza(i, (zzbb) obj);
        }
    }

    private static void zza(zzex zzexVar, Object obj, zzfr zzfrVar) {
        zzexVar.zza(zzexVar.zzq(obj), zzfrVar);
    }

    private final void zza(zzfr zzfrVar, int i, Object obj, int i2) {
        if (obj != null) {
            this.zzmz.zzl(zzae(i2));
            zzfrVar.zza(i, (zzdh) null, this.zzmz.zzh(obj));
        }
    }

    private final void zza(Object obj, Object obj2, int i) {
        long jZzag = zzag(i) & 1048575;
        if (zza(obj2, i)) {
            Object objZzo = zzfd.zzo(obj, jZzag);
            Object objZzo2 = zzfd.zzo(obj2, jZzag);
            if (objZzo != null && objZzo2 != null) {
                objZzo2 = zzci.zza(objZzo, objZzo2);
            } else if (objZzo2 == null) {
                return;
            }
            zzfd.zza(obj, jZzag, objZzo2);
            zzb(obj, i);
        }
    }

    private final boolean zza(Object obj, int i) {
        if (!this.zzmq) {
            int iZzah = zzah(i);
            return (zzfd.zzj(obj, (long) (iZzah & 1048575)) & (1 << (iZzah >>> 20))) != 0;
        }
        int iZzag = zzag(i);
        long j = iZzag & 1048575;
        switch ((iZzag & 267386880) >>> 20) {
            case 0:
                return zzfd.zzn(obj, j) != 0.0d;
            case 1:
                return zzfd.zzm(obj, j) != 0.0f;
            case 2:
                return zzfd.zzk(obj, j) != 0;
            case 3:
                return zzfd.zzk(obj, j) != 0;
            case 4:
                return zzfd.zzj(obj, j) != 0;
            case 5:
                return zzfd.zzk(obj, j) != 0;
            case 6:
                return zzfd.zzj(obj, j) != 0;
            case 7:
                return zzfd.zzl(obj, j);
            case 8:
                Object objZzo = zzfd.zzo(obj, j);
                if (objZzo instanceof String) {
                    return !((String) objZzo).isEmpty();
                }
                if (objZzo instanceof zzbb) {
                    return !zzbb.zzfi.equals(objZzo);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzfd.zzo(obj, j) != null;
            case 10:
                return !zzbb.zzfi.equals(zzfd.zzo(obj, j));
            case 11:
                return zzfd.zzj(obj, j) != 0;
            case 12:
                return zzfd.zzj(obj, j) != 0;
            case 13:
                return zzfd.zzj(obj, j) != 0;
            case 14:
                return zzfd.zzk(obj, j) != 0;
            case 15:
                return zzfd.zzj(obj, j) != 0;
            case 16:
                return zzfd.zzk(obj, j) != 0;
            case 17:
                return zzfd.zzo(obj, j) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zza(Object obj, int i, int i2) {
        return zzfd.zzj(obj, (long) (zzah(i2) & 1048575)) == i;
    }

    private final boolean zza(Object obj, int i, int i2, int i3) {
        if (this.zzmq) {
            return zza(obj, i);
        }
        return (i2 & i3) != 0;
    }

    private static boolean zza(Object obj, int i, zzef zzefVar) {
        return zzefVar.zzo(zzfd.zzo(obj, i & 1048575));
    }

    private final zzef zzad(int i) {
        int i2 = (i / 4) << 1;
        zzef zzefVar = (zzef) this.zzmj[i2];
        if (zzefVar != null) {
            return zzefVar;
        }
        zzef zzefVarZze = zzea.zzcm().zze((Class) this.zzmj[i2 + 1]);
        this.zzmj[i2] = zzefVarZze;
        return zzefVarZze;
    }

    private final Object zzae(int i) {
        return this.zzmj[(i / 4) << 1];
    }

    private final zzck zzaf(int i) {
        return (zzck) this.zzmj[((i / 4) << 1) + 1];
    }

    private final int zzag(int i) {
        return this.zzmi[i + 1];
    }

    private final int zzah(int i) {
        return this.zzmi[i + 2];
    }

    private final int zzai(int i) {
        int i2 = this.zzmk;
        if (i >= i2) {
            int i3 = this.zzmm;
            if (i < i3) {
                int i4 = (i - i2) << 2;
                if (this.zzmi[i4] == i) {
                    return i4;
                }
                return -1;
            }
            if (i <= this.zzml) {
                int i5 = i3 - i2;
                int length = (this.zzmi.length / 4) - 1;
                while (i5 <= length) {
                    int i6 = (length + i5) >>> 1;
                    int i7 = i6 << 2;
                    int i8 = this.zzmi[i7];
                    if (i == i8) {
                        return i7;
                    }
                    if (i < i8) {
                        length = i6 - 1;
                    } else {
                        i5 = i6 + 1;
                    }
                }
            }
        }
        return -1;
    }

    private final void zzb(Object obj, int i) {
        if (this.zzmq) {
            return;
        }
        int iZzah = zzah(i);
        long j = iZzah & 1048575;
        zzfd.zza(obj, j, zzfd.zzj(obj, j) | (1 << (iZzah >>> 20)));
    }

    private final void zzb(Object obj, int i, int i2) {
        zzfd.zza(obj, zzah(i2) & 1048575, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:7:0x0021  */
    private final void zzb(Object obj, zzfr zzfrVar) {
        Iterator it;
        Map.Entry entry;
        boolean z;
        int i;
        boolean z2;
        if (this.zzmo) {
            zzby zzbyVarZza = this.zzmy.zza(obj);
            if (zzbyVarZza.isEmpty()) {
                it = null;
                entry = null;
            } else {
                it = zzbyVarZza.iterator();
                entry = (Map.Entry) it.next();
            }
        } else {
            it = null;
            entry = null;
        }
        int length = this.zzmi.length;
        Unsafe unsafe = zzmh;
        int i2 = -1;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 4) {
            int iZzag = zzag(i4);
            int[] iArr = this.zzmi;
            int i5 = iArr[i4];
            int i6 = (267386880 & iZzag) >>> 20;
            if (this.zzmq || i6 > 17) {
                z = true;
                i = 0;
            } else {
                int i7 = iArr[i4 + 2];
                int i8 = i7 & 1048575;
                z = true;
                if (i8 != i2) {
                    i3 = unsafe.getInt(obj, i8);
                    i2 = i8;
                }
                i = 1 << (i7 >>> 20);
            }
            while (entry != null && this.zzmy.zza(entry) <= i5) {
                this.zzmy.zza(zzfrVar, entry);
                entry = it.hasNext() ? (Map.Entry) it.next() : null;
            }
            long j = iZzag & 1048575;
            switch (i6) {
                case 0:
                    if ((i & i3) != 0) {
                        zzfrVar.zza(i5, zzfd.zzn(obj, j));
                    }
                    break;
                case 1:
                    if ((i & i3) != 0) {
                        zzfrVar.zza(i5, zzfd.zzm(obj, j));
                    }
                    break;
                case 2:
                    if ((i & i3) != 0) {
                        zzfrVar.zzi(i5, unsafe.getLong(obj, j));
                    }
                    break;
                case 3:
                    if ((i & i3) != 0) {
                        zzfrVar.zza(i5, unsafe.getLong(obj, j));
                    }
                    break;
                case 4:
                    if ((i & i3) != 0) {
                        zzfrVar.zzc(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 5:
                    if ((i & i3) != 0) {
                        zzfrVar.zzc(i5, unsafe.getLong(obj, j));
                    }
                    break;
                case 6:
                    if ((i & i3) != 0) {
                        zzfrVar.zzf(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 7:
                    if ((i & i3) != 0) {
                        zzfrVar.zzb(i5, zzfd.zzl(obj, j));
                    }
                    break;
                case 8:
                    if ((i & i3) != 0) {
                        zza(i5, unsafe.getObject(obj, j), zzfrVar);
                    }
                    break;
                case 9:
                    if ((i & i3) != 0) {
                        zzfrVar.zza(i5, unsafe.getObject(obj, j), zzad(i4));
                    }
                    break;
                case 10:
                    if ((i & i3) != 0) {
                        zzfrVar.zza(i5, (zzbb) unsafe.getObject(obj, j));
                    }
                    break;
                case 11:
                    if ((i & i3) != 0) {
                        zzfrVar.zzd(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 12:
                    if ((i & i3) != 0) {
                        zzfrVar.zzn(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 13:
                    if ((i & i3) != 0) {
                        zzfrVar.zzm(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 14:
                    if ((i & i3) != 0) {
                        zzfrVar.zzj(i5, unsafe.getLong(obj, j));
                    }
                    break;
                case 15:
                    if ((i & i3) != 0) {
                        zzfrVar.zze(i5, unsafe.getInt(obj, j));
                    }
                    break;
                case 16:
                    if ((i & i3) != 0) {
                        zzfrVar.zzb(i5, unsafe.getLong(obj, j));
                    }
                    break;
                case 17:
                    if ((i & i3) != 0) {
                        zzfrVar.zzb(i5, unsafe.getObject(obj, j), zzad(i4));
                    }
                    break;
                case 18:
                    zzeh.zza(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 19:
                    zzeh.zzb(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 20:
                    zzeh.zzc(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 21:
                    zzeh.zzd(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 22:
                    zzeh.zzh(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 23:
                    zzeh.zzf(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 24:
                    zzeh.zzk(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 25:
                    zzeh.zzn(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 26:
                    zzeh.zza(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar);
                    break;
                case 27:
                    zzeh.zza(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, zzad(i4));
                    break;
                case 28:
                    zzeh.zzb(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar);
                    break;
                case 29:
                    z2 = false;
                    zzeh.zzi(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 30:
                    z2 = false;
                    zzeh.zzm(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 31:
                    z2 = false;
                    zzeh.zzl(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 32:
                    z2 = false;
                    zzeh.zzg(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 33:
                    z2 = false;
                    zzeh.zzj(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 34:
                    z2 = false;
                    zzeh.zze(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, false);
                    break;
                case 35:
                    zzeh.zza(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 36:
                    zzeh.zzb(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 37:
                    zzeh.zzc(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 38:
                    zzeh.zzd(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 39:
                    zzeh.zzh(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 40:
                    zzeh.zzf(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 41:
                    zzeh.zzk(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 42:
                    zzeh.zzn(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 43:
                    zzeh.zzi(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 44:
                    zzeh.zzm(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 45:
                    zzeh.zzl(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 46:
                    zzeh.zzg(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 47:
                    zzeh.zzj(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 48:
                    zzeh.zze(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, z);
                    break;
                case 49:
                    zzeh.zzb(this.zzmi[i4], (List) unsafe.getObject(obj, j), zzfrVar, zzad(i4));
                    break;
                case 50:
                    zza(zzfrVar, i5, unsafe.getObject(obj, j), i4);
                    break;
                case 51:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zza(i5, zze(obj, j));
                    }
                    break;
                case 52:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zza(i5, zzf(obj, j));
                    }
                    break;
                case 53:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzi(i5, zzh(obj, j));
                    }
                    break;
                case 54:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zza(i5, zzh(obj, j));
                    }
                    break;
                case 55:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzc(i5, zzg(obj, j));
                    }
                    break;
                case 56:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzc(i5, zzh(obj, j));
                    }
                    break;
                case 57:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzf(i5, zzg(obj, j));
                    }
                    break;
                case 58:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzb(i5, zzi(obj, j));
                    }
                    break;
                case 59:
                    if (zza(obj, i5, i4)) {
                        zza(i5, unsafe.getObject(obj, j), zzfrVar);
                    }
                    break;
                case 60:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zza(i5, unsafe.getObject(obj, j), zzad(i4));
                    }
                    break;
                case 61:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zza(i5, (zzbb) unsafe.getObject(obj, j));
                    }
                    break;
                case 62:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzd(i5, zzg(obj, j));
                    }
                    break;
                case 63:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzn(i5, zzg(obj, j));
                    }
                    break;
                case 64:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzm(i5, zzg(obj, j));
                    }
                    break;
                case 65:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzj(i5, zzh(obj, j));
                    }
                    break;
                case 66:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zze(i5, zzg(obj, j));
                    }
                    break;
                case 67:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzb(i5, zzh(obj, j));
                    }
                    break;
                case 68:
                    if (zza(obj, i5, i4)) {
                        zzfrVar.zzb(i5, unsafe.getObject(obj, j), zzad(i4));
                    }
                    break;
                default:
                    break;
            }
        }
        while (entry != null) {
            this.zzmy.zza(zzfrVar, entry);
            entry = it.hasNext() ? (Map.Entry) it.next() : null;
        }
        zza(this.zzmx, obj, zzfrVar);
    }

    private final void zzb(Object obj, Object obj2, int i) {
        int iZzag = zzag(i);
        int i2 = this.zzmi[i];
        long j = iZzag & 1048575;
        if (zza(obj2, i2, i)) {
            Object objZzo = zzfd.zzo(obj, j);
            Object objZzo2 = zzfd.zzo(obj2, j);
            if (objZzo != null && objZzo2 != null) {
                objZzo2 = zzci.zza(objZzo, objZzo2);
            } else if (objZzo2 == null) {
                return;
            }
            zzfd.zza(obj, j, objZzo2);
            zzb(obj, i2, i);
        }
    }

    private final boolean zzc(Object obj, Object obj2, int i) {
        return zza(obj, i) == zza(obj2, i);
    }

    private static List zzd(Object obj, long j) {
        return (List) zzfd.zzo(obj, j);
    }

    private static double zze(Object obj, long j) {
        return ((Double) zzfd.zzo(obj, j)).doubleValue();
    }

    private static float zzf(Object obj, long j) {
        return ((Float) zzfd.zzo(obj, j)).floatValue();
    }

    private static int zzg(Object obj, long j) {
        return ((Integer) zzfd.zzo(obj, j)).intValue();
    }

    private static long zzh(Object obj, long j) {
        return ((Long) zzfd.zzo(obj, j)).longValue();
    }

    private static boolean zzi(Object obj, long j) {
        return ((Boolean) zzfd.zzo(obj, j)).booleanValue();
    }

    private static zzey zzn(Object obj) {
        zzcg zzcgVar = (zzcg) obj;
        zzey zzeyVar = zzcgVar.zzjp;
        if (zzeyVar != zzey.zzea()) {
            return zzeyVar;
        }
        zzey zzeyVarZzeb = zzey.zzeb();
        zzcgVar.zzjp = zzeyVarZzeb;
        return zzeyVarZzeb;
    }

    /* JADX WARN: Code duplicated, block: B:12:0x003a  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean equals(Object obj, Object obj2) {
        int length = this.zzmi.length;
        int i = 0;
        while (true) {
            boolean zZzd = true;
            if (i >= length) {
                if (!this.zzmx.zzq(obj).equals(this.zzmx.zzq(obj2))) {
                    return false;
                }
                if (this.zzmo) {
                    return this.zzmy.zza(obj).equals(this.zzmy.zza(obj2));
                }
                return true;
            }
            int iZzag = zzag(i);
            long j = iZzag & 1048575;
            switch ((iZzag & 267386880) >>> 20) {
                case 0:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 1:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 2:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 3:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 4:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 5:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 6:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 7:
                    if (!zzc(obj, obj2, i) || zzfd.zzl(obj, j) != zzfd.zzl(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 8:
                    if (!zzc(obj, obj2, i) || !zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j))) {
                        zZzd = false;
                    }
                    break;
                case 9:
                    if (!zzc(obj, obj2, i) || !zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j))) {
                        zZzd = false;
                    }
                    break;
                case 10:
                    if (!zzc(obj, obj2, i) || !zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j))) {
                        zZzd = false;
                    }
                    break;
                case 11:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 12:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 13:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 14:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 15:
                    if (!zzc(obj, obj2, i) || zzfd.zzj(obj, j) != zzfd.zzj(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 16:
                    if (!zzc(obj, obj2, i) || zzfd.zzk(obj, j) != zzfd.zzk(obj2, j)) {
                        zZzd = false;
                    }
                    break;
                case 17:
                    if (!zzc(obj, obj2, i) || !zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j))) {
                        zZzd = false;
                    }
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    zZzd = zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j));
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                    long jZzah = zzah(i) & 1048575;
                    if (zzfd.zzj(obj, jZzah) != zzfd.zzj(obj2, jZzah) || !zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j))) {
                        zZzd = false;
                    }
                    break;
            }
            if (!zZzd) {
                return false;
            }
            i += 4;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:14:0x0039  */
    /* JADX WARN: Code duplicated, block: B:18:0x004a  */
    /* JADX WARN: Code duplicated, block: B:33:0x0073  */
    /* JADX WARN: Code duplicated, block: B:39:0x0087  */
    /* JADX WARN: Code duplicated, block: B:69:0x00f4 A[PHI: r3
  0x00f4: PHI (r3v9 java.lang.Object) = (r3v6 java.lang.Object), (r3v10 java.lang.Object) binds: [B:74:0x0110, B:68:0x00f2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:9:0x0026  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final int hashCode(Object obj) {
        int i;
        double dZzn;
        float fZzm;
        boolean zZzl;
        Object objZzo;
        int iZzj;
        long jZzk;
        Object objZzo2;
        int length = this.zzmi.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 4) {
            int iZzag = zzag(i3);
            int i4 = this.zzmi[i3];
            long j = 1048575 & iZzag;
            int iHashCode = 37;
            switch ((iZzag & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    dZzn = zzfd.zzn(obj, j);
                    jZzk = Double.doubleToLongBits(dZzn);
                    iZzj = zzci.zzl(jZzk);
                    i2 = i + iZzj;
                    break;
                case 1:
                    i = i2 * 53;
                    fZzm = zzfd.zzm(obj, j);
                    iZzj = Float.floatToIntBits(fZzm);
                    i2 = i + iZzj;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i2 * 53;
                    jZzk = zzfd.zzk(obj, j);
                    iZzj = zzci.zzl(jZzk);
                    i2 = i + iZzj;
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i = i2 * 53;
                    iZzj = zzfd.zzj(obj, j);
                    i2 = i + iZzj;
                    break;
                case 7:
                    i = i2 * 53;
                    zZzl = zzfd.zzl(obj, j);
                    iZzj = zzci.zzc(zZzl);
                    i2 = i + iZzj;
                    break;
                case 8:
                    i = i2 * 53;
                    iZzj = ((String) zzfd.zzo(obj, j)).hashCode();
                    i2 = i + iZzj;
                    break;
                case 9:
                    objZzo = zzfd.zzo(obj, j);
                    if (objZzo != null) {
                        iHashCode = objZzo.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
                    break;
                case 10:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    i = i2 * 53;
                    objZzo2 = zzfd.zzo(obj, j);
                    iZzj = objZzo2.hashCode();
                    i2 = i + iZzj;
                    break;
                case 17:
                    objZzo = zzfd.zzo(obj, j);
                    if (objZzo != null) {
                        iHashCode = objZzo.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
                    break;
                case 51:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        dZzn = zze(obj, j);
                        jZzk = Double.doubleToLongBits(dZzn);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 52:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        fZzm = zzf(obj, j);
                        iZzj = Float.floatToIntBits(fZzm);
                        i2 = i + iZzj;
                    }
                    break;
                case 53:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        jZzk = zzh(obj, j);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 54:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        jZzk = zzh(obj, j);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 55:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 56:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        jZzk = zzh(obj, j);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 57:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 58:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zZzl = zzi(obj, j);
                        iZzj = zzci.zzc(zZzl);
                        i2 = i + iZzj;
                    }
                    break;
                case 59:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = ((String) zzfd.zzo(obj, j)).hashCode();
                        i2 = i + iZzj;
                    }
                    break;
                case 60:
                    if (zza(obj, i4, i3)) {
                        objZzo2 = zzfd.zzo(obj, j);
                        i = i2 * 53;
                        iZzj = objZzo2.hashCode();
                        i2 = i + iZzj;
                    }
                    break;
                case 61:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        objZzo2 = zzfd.zzo(obj, j);
                        iZzj = objZzo2.hashCode();
                        i2 = i + iZzj;
                    }
                    break;
                case 62:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 63:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 64:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 65:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        jZzk = zzh(obj, j);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 66:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZzj = zzg(obj, j);
                        i2 = i + iZzj;
                    }
                    break;
                case 67:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        jZzk = zzh(obj, j);
                        iZzj = zzci.zzl(jZzk);
                        i2 = i + iZzj;
                    }
                    break;
                case 68:
                    if (zza(obj, i4, i3)) {
                        objZzo2 = zzfd.zzo(obj, j);
                        i = i2 * 53;
                        iZzj = objZzo2.hashCode();
                        i2 = i + iZzj;
                    }
                    break;
            }
        }
        int iHashCode2 = (i2 * 53) + this.zzmx.zzq(obj).hashCode();
        return this.zzmo ? (iHashCode2 * 53) + this.zzmy.zza(obj).hashCode() : iHashCode2;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final Object newInstance() {
        return this.zzmv.newInstance(this.zzmn);
    }

    /* JADX WARN: Code duplicated, block: B:192:0x04f6  */
    /* JADX WARN: Code duplicated, block: B:208:0x0533  */
    /* JADX WARN: Code duplicated, block: B:235:0x05ae  */
    /* JADX WARN: Code duplicated, block: B:238:0x05c1  */
    /* JADX WARN: Code duplicated, block: B:241:0x05d6  */
    /* JADX WARN: Code duplicated, block: B:25:0x006e  */
    /* JADX WARN: Code duplicated, block: B:52:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:55:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:58:0x0111  */
    /* JADX WARN: Code duplicated, block: B:9:0x0030  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zza(Object obj, zzfr zzfrVar) {
        Iterator it;
        Map.Entry entry;
        double dZzn;
        float fZzm;
        long jZzk;
        long jZzk2;
        int iZzj;
        long jZzk3;
        int iZzj2;
        boolean zZzl;
        int iZzj3;
        int iZzj4;
        int iZzj5;
        long jZzk4;
        int iZzj6;
        long jZzk5;
        Iterator itDescendingIterator;
        Map.Entry entry2;
        double dZzn2;
        float fZzm2;
        long jZzk6;
        long jZzk7;
        int iZzj7;
        long jZzk8;
        int iZzj8;
        boolean zZzl2;
        int iZzj9;
        int iZzj10;
        int iZzj11;
        long jZzk9;
        int iZzj12;
        long jZzk10;
        if (zzfrVar.zzaj() == zzcg.zzg.zzkp) {
            zza(this.zzmx, obj, zzfrVar);
            if (this.zzmo) {
                zzby zzbyVarZza = this.zzmy.zza(obj);
                if (zzbyVarZza.isEmpty()) {
                    itDescendingIterator = null;
                    entry2 = null;
                } else {
                    itDescendingIterator = zzbyVarZza.descendingIterator();
                    entry2 = (Map.Entry) itDescendingIterator.next();
                }
            } else {
                itDescendingIterator = null;
                entry2 = null;
            }
            for (int length = this.zzmi.length - 4; length >= 0; length -= 4) {
                int iZzag = zzag(length);
                int i = this.zzmi[length];
                while (entry2 != null && this.zzmy.zza(entry2) > i) {
                    this.zzmy.zza(zzfrVar, entry2);
                    entry2 = itDescendingIterator.hasNext() ? (Map.Entry) itDescendingIterator.next() : null;
                }
                switch ((iZzag & 267386880) >>> 20) {
                    case 0:
                        if (zza(obj, length)) {
                            dZzn2 = zzfd.zzn(obj, iZzag & 1048575);
                            zzfrVar.zza(i, dZzn2);
                        }
                        break;
                    case 1:
                        if (zza(obj, length)) {
                            fZzm2 = zzfd.zzm(obj, iZzag & 1048575);
                            zzfrVar.zza(i, fZzm2);
                        }
                        break;
                    case 2:
                        if (zza(obj, length)) {
                            jZzk6 = zzfd.zzk(obj, iZzag & 1048575);
                            zzfrVar.zzi(i, jZzk6);
                        }
                        break;
                    case 3:
                        if (zza(obj, length)) {
                            jZzk7 = zzfd.zzk(obj, iZzag & 1048575);
                            zzfrVar.zza(i, jZzk7);
                        }
                        break;
                    case 4:
                        if (zza(obj, length)) {
                            iZzj7 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zzc(i, iZzj7);
                        }
                        break;
                    case 5:
                        if (zza(obj, length)) {
                            jZzk8 = zzfd.zzk(obj, iZzag & 1048575);
                            zzfrVar.zzc(i, jZzk8);
                        }
                        break;
                    case 6:
                        if (zza(obj, length)) {
                            iZzj8 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zzf(i, iZzj8);
                        }
                        break;
                    case 7:
                        if (zza(obj, length)) {
                            zZzl2 = zzfd.zzl(obj, iZzag & 1048575);
                            zzfrVar.zzb(i, zZzl2);
                        }
                        break;
                    case 8:
                        if (zza(obj, length)) {
                            zza(i, zzfd.zzo(obj, iZzag & 1048575), zzfrVar);
                        }
                        break;
                    case 9:
                        if (zza(obj, length)) {
                            zzfrVar.zza(i, zzfd.zzo(obj, iZzag & 1048575), zzad(length));
                        }
                        break;
                    case 10:
                        if (zza(obj, length)) {
                            zzfrVar.zza(i, (zzbb) zzfd.zzo(obj, iZzag & 1048575));
                        }
                        break;
                    case 11:
                        if (zza(obj, length)) {
                            iZzj9 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zzd(i, iZzj9);
                        }
                        break;
                    case 12:
                        if (zza(obj, length)) {
                            iZzj10 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zzn(i, iZzj10);
                        }
                        break;
                    case 13:
                        if (zza(obj, length)) {
                            iZzj11 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zzm(i, iZzj11);
                        }
                        break;
                    case 14:
                        if (zza(obj, length)) {
                            jZzk9 = zzfd.zzk(obj, iZzag & 1048575);
                            zzfrVar.zzj(i, jZzk9);
                        }
                        break;
                    case 15:
                        if (zza(obj, length)) {
                            iZzj12 = zzfd.zzj(obj, iZzag & 1048575);
                            zzfrVar.zze(i, iZzj12);
                        }
                        break;
                    case 16:
                        if (zza(obj, length)) {
                            jZzk10 = zzfd.zzk(obj, iZzag & 1048575);
                            zzfrVar.zzb(i, jZzk10);
                        }
                        break;
                    case 17:
                        if (zza(obj, length)) {
                            zzfrVar.zzb(i, zzfd.zzo(obj, iZzag & 1048575), zzad(length));
                        }
                        break;
                    case 18:
                        zzeh.zza(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 19:
                        zzeh.zzb(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 20:
                        zzeh.zzc(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 21:
                        zzeh.zzd(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 22:
                        zzeh.zzh(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 23:
                        zzeh.zzf(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 24:
                        zzeh.zzk(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 25:
                        zzeh.zzn(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 26:
                        zzeh.zza(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar);
                        break;
                    case 27:
                        zzeh.zza(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, zzad(length));
                        break;
                    case 28:
                        zzeh.zzb(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar);
                        break;
                    case 29:
                        zzeh.zzi(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 30:
                        zzeh.zzm(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 31:
                        zzeh.zzl(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 32:
                        zzeh.zzg(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 33:
                        zzeh.zzj(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 34:
                        zzeh.zze(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, false);
                        break;
                    case 35:
                        zzeh.zza(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 36:
                        zzeh.zzb(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 37:
                        zzeh.zzc(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 38:
                        zzeh.zzd(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 39:
                        zzeh.zzh(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 40:
                        zzeh.zzf(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 41:
                        zzeh.zzk(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 42:
                        zzeh.zzn(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 43:
                        zzeh.zzi(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 44:
                        zzeh.zzm(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 45:
                        zzeh.zzl(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 46:
                        zzeh.zzg(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 47:
                        zzeh.zzj(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 48:
                        zzeh.zze(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, true);
                        break;
                    case 49:
                        zzeh.zzb(this.zzmi[length], (List) zzfd.zzo(obj, iZzag & 1048575), zzfrVar, zzad(length));
                        break;
                    case 50:
                        zza(zzfrVar, i, zzfd.zzo(obj, iZzag & 1048575), length);
                        break;
                    case 51:
                        if (zza(obj, i, length)) {
                            dZzn2 = zze(obj, iZzag & 1048575);
                            zzfrVar.zza(i, dZzn2);
                        }
                        break;
                    case 52:
                        if (zza(obj, i, length)) {
                            fZzm2 = zzf(obj, iZzag & 1048575);
                            zzfrVar.zza(i, fZzm2);
                        }
                        break;
                    case 53:
                        if (zza(obj, i, length)) {
                            jZzk6 = zzh(obj, iZzag & 1048575);
                            zzfrVar.zzi(i, jZzk6);
                        }
                        break;
                    case 54:
                        if (zza(obj, i, length)) {
                            jZzk7 = zzh(obj, iZzag & 1048575);
                            zzfrVar.zza(i, jZzk7);
                        }
                        break;
                    case 55:
                        if (zza(obj, i, length)) {
                            iZzj7 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zzc(i, iZzj7);
                        }
                        break;
                    case 56:
                        if (zza(obj, i, length)) {
                            jZzk8 = zzh(obj, iZzag & 1048575);
                            zzfrVar.zzc(i, jZzk8);
                        }
                        break;
                    case 57:
                        if (zza(obj, i, length)) {
                            iZzj8 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zzf(i, iZzj8);
                        }
                        break;
                    case 58:
                        if (zza(obj, i, length)) {
                            zZzl2 = zzi(obj, iZzag & 1048575);
                            zzfrVar.zzb(i, zZzl2);
                        }
                        break;
                    case 59:
                        if (zza(obj, i, length)) {
                            zza(i, zzfd.zzo(obj, iZzag & 1048575), zzfrVar);
                        }
                        break;
                    case 60:
                        if (zza(obj, i, length)) {
                            zzfrVar.zza(i, zzfd.zzo(obj, iZzag & 1048575), zzad(length));
                        }
                        break;
                    case 61:
                        if (zza(obj, i, length)) {
                            zzfrVar.zza(i, (zzbb) zzfd.zzo(obj, iZzag & 1048575));
                        }
                        break;
                    case 62:
                        if (zza(obj, i, length)) {
                            iZzj9 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zzd(i, iZzj9);
                        }
                        break;
                    case 63:
                        if (zza(obj, i, length)) {
                            iZzj10 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zzn(i, iZzj10);
                        }
                        break;
                    case 64:
                        if (zza(obj, i, length)) {
                            iZzj11 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zzm(i, iZzj11);
                        }
                        break;
                    case 65:
                        if (zza(obj, i, length)) {
                            jZzk9 = zzh(obj, iZzag & 1048575);
                            zzfrVar.zzj(i, jZzk9);
                        }
                        break;
                    case 66:
                        if (zza(obj, i, length)) {
                            iZzj12 = zzg(obj, iZzag & 1048575);
                            zzfrVar.zze(i, iZzj12);
                        }
                        break;
                    case 67:
                        if (zza(obj, i, length)) {
                            jZzk10 = zzh(obj, iZzag & 1048575);
                            zzfrVar.zzb(i, jZzk10);
                        }
                        break;
                    case 68:
                        if (zza(obj, i, length)) {
                            zzfrVar.zzb(i, zzfd.zzo(obj, iZzag & 1048575), zzad(length));
                        }
                        break;
                }
            }
            while (entry2 != null) {
                this.zzmy.zza(zzfrVar, entry2);
                entry2 = itDescendingIterator.hasNext() ? (Map.Entry) itDescendingIterator.next() : null;
            }
            return;
        }
        if (!this.zzmq) {
            zzb(obj, zzfrVar);
            return;
        }
        if (this.zzmo) {
            zzby zzbyVarZza2 = this.zzmy.zza(obj);
            if (zzbyVarZza2.isEmpty()) {
                it = null;
                entry = null;
            } else {
                it = zzbyVarZza2.iterator();
                entry = (Map.Entry) it.next();
            }
        } else {
            it = null;
            entry = null;
        }
        int length2 = this.zzmi.length;
        for (int i2 = 0; i2 < length2; i2 += 4) {
            int iZzag2 = zzag(i2);
            int i3 = this.zzmi[i2];
            while (entry != null && this.zzmy.zza(entry) <= i3) {
                this.zzmy.zza(zzfrVar, entry);
                entry = it.hasNext() ? (Map.Entry) it.next() : null;
            }
            switch ((iZzag2 & 267386880) >>> 20) {
                case 0:
                    if (zza(obj, i2)) {
                        dZzn = zzfd.zzn(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, dZzn);
                    }
                    break;
                case 1:
                    if (zza(obj, i2)) {
                        fZzm = zzfd.zzm(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, fZzm);
                    }
                    break;
                case 2:
                    if (zza(obj, i2)) {
                        jZzk = zzfd.zzk(obj, iZzag2 & 1048575);
                        zzfrVar.zzi(i3, jZzk);
                    }
                    break;
                case 3:
                    if (zza(obj, i2)) {
                        jZzk2 = zzfd.zzk(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, jZzk2);
                    }
                    break;
                case 4:
                    if (zza(obj, i2)) {
                        iZzj = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zzc(i3, iZzj);
                    }
                    break;
                case 5:
                    if (zza(obj, i2)) {
                        jZzk3 = zzfd.zzk(obj, iZzag2 & 1048575);
                        zzfrVar.zzc(i3, jZzk3);
                    }
                    break;
                case 6:
                    if (zza(obj, i2)) {
                        iZzj2 = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zzf(i3, iZzj2);
                    }
                    break;
                case 7:
                    if (zza(obj, i2)) {
                        zZzl = zzfd.zzl(obj, iZzag2 & 1048575);
                        zzfrVar.zzb(i3, zZzl);
                    }
                    break;
                case 8:
                    if (zza(obj, i2)) {
                        zza(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar);
                    }
                    break;
                case 9:
                    if (zza(obj, i2)) {
                        zzfrVar.zza(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzad(i2));
                    }
                    break;
                case 10:
                    if (zza(obj, i2)) {
                        zzfrVar.zza(i3, (zzbb) zzfd.zzo(obj, iZzag2 & 1048575));
                    }
                    break;
                case 11:
                    if (zza(obj, i2)) {
                        iZzj3 = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zzd(i3, iZzj3);
                    }
                    break;
                case 12:
                    if (zza(obj, i2)) {
                        iZzj4 = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zzn(i3, iZzj4);
                    }
                    break;
                case 13:
                    if (zza(obj, i2)) {
                        iZzj5 = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zzm(i3, iZzj5);
                    }
                    break;
                case 14:
                    if (zza(obj, i2)) {
                        jZzk4 = zzfd.zzk(obj, iZzag2 & 1048575);
                        zzfrVar.zzj(i3, jZzk4);
                    }
                    break;
                case 15:
                    if (zza(obj, i2)) {
                        iZzj6 = zzfd.zzj(obj, iZzag2 & 1048575);
                        zzfrVar.zze(i3, iZzj6);
                    }
                    break;
                case 16:
                    if (zza(obj, i2)) {
                        jZzk5 = zzfd.zzk(obj, iZzag2 & 1048575);
                        zzfrVar.zzb(i3, jZzk5);
                    }
                    break;
                case 17:
                    if (zza(obj, i2)) {
                        zzfrVar.zzb(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzad(i2));
                    }
                    break;
                case 18:
                    zzeh.zza(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 19:
                    zzeh.zzb(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 20:
                    zzeh.zzc(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 21:
                    zzeh.zzd(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 22:
                    zzeh.zzh(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 23:
                    zzeh.zzf(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 24:
                    zzeh.zzk(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 25:
                    zzeh.zzn(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 26:
                    zzeh.zza(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar);
                    break;
                case 27:
                    zzeh.zza(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, zzad(i2));
                    break;
                case 28:
                    zzeh.zzb(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar);
                    break;
                case 29:
                    zzeh.zzi(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 30:
                    zzeh.zzm(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 31:
                    zzeh.zzl(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 32:
                    zzeh.zzg(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 33:
                    zzeh.zzj(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 34:
                    zzeh.zze(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, false);
                    break;
                case 35:
                    zzeh.zza(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 36:
                    zzeh.zzb(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 37:
                    zzeh.zzc(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 38:
                    zzeh.zzd(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 39:
                    zzeh.zzh(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 40:
                    zzeh.zzf(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 41:
                    zzeh.zzk(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 42:
                    zzeh.zzn(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 43:
                    zzeh.zzi(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 44:
                    zzeh.zzm(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 45:
                    zzeh.zzl(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 46:
                    zzeh.zzg(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 47:
                    zzeh.zzj(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 48:
                    zzeh.zze(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, true);
                    break;
                case 49:
                    zzeh.zzb(this.zzmi[i2], (List) zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar, zzad(i2));
                    break;
                case 50:
                    zza(zzfrVar, i3, zzfd.zzo(obj, iZzag2 & 1048575), i2);
                    break;
                case 51:
                    if (zza(obj, i3, i2)) {
                        dZzn = zze(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, dZzn);
                    }
                    break;
                case 52:
                    if (zza(obj, i3, i2)) {
                        fZzm = zzf(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, fZzm);
                    }
                    break;
                case 53:
                    if (zza(obj, i3, i2)) {
                        jZzk = zzh(obj, iZzag2 & 1048575);
                        zzfrVar.zzi(i3, jZzk);
                    }
                    break;
                case 54:
                    if (zza(obj, i3, i2)) {
                        jZzk2 = zzh(obj, iZzag2 & 1048575);
                        zzfrVar.zza(i3, jZzk2);
                    }
                    break;
                case 55:
                    if (zza(obj, i3, i2)) {
                        iZzj = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zzc(i3, iZzj);
                    }
                    break;
                case 56:
                    if (zza(obj, i3, i2)) {
                        jZzk3 = zzh(obj, iZzag2 & 1048575);
                        zzfrVar.zzc(i3, jZzk3);
                    }
                    break;
                case 57:
                    if (zza(obj, i3, i2)) {
                        iZzj2 = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zzf(i3, iZzj2);
                    }
                    break;
                case 58:
                    if (zza(obj, i3, i2)) {
                        zZzl = zzi(obj, iZzag2 & 1048575);
                        zzfrVar.zzb(i3, zZzl);
                    }
                    break;
                case 59:
                    if (zza(obj, i3, i2)) {
                        zza(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzfrVar);
                    }
                    break;
                case 60:
                    if (zza(obj, i3, i2)) {
                        zzfrVar.zza(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzad(i2));
                    }
                    break;
                case 61:
                    if (zza(obj, i3, i2)) {
                        zzfrVar.zza(i3, (zzbb) zzfd.zzo(obj, iZzag2 & 1048575));
                    }
                    break;
                case 62:
                    if (zza(obj, i3, i2)) {
                        iZzj3 = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zzd(i3, iZzj3);
                    }
                    break;
                case 63:
                    if (zza(obj, i3, i2)) {
                        iZzj4 = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zzn(i3, iZzj4);
                    }
                    break;
                case 64:
                    if (zza(obj, i3, i2)) {
                        iZzj5 = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zzm(i3, iZzj5);
                    }
                    break;
                case 65:
                    if (zza(obj, i3, i2)) {
                        jZzk4 = zzh(obj, iZzag2 & 1048575);
                        zzfrVar.zzj(i3, jZzk4);
                    }
                    break;
                case 66:
                    if (zza(obj, i3, i2)) {
                        iZzj6 = zzg(obj, iZzag2 & 1048575);
                        zzfrVar.zze(i3, iZzj6);
                    }
                    break;
                case 67:
                    if (zza(obj, i3, i2)) {
                        jZzk5 = zzh(obj, iZzag2 & 1048575);
                        zzfrVar.zzb(i3, jZzk5);
                    }
                    break;
                case 68:
                    if (zza(obj, i3, i2)) {
                        zzfrVar.zzb(i3, zzfd.zzo(obj, iZzag2 & 1048575), zzad(i2));
                    }
                    break;
            }
        }
        while (entry != null) {
            this.zzmy.zza(zzfrVar, entry);
            entry = it.hasNext() ? (Map.Entry) it.next() : null;
        }
        zza(this.zzmx, obj, zzfrVar);
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0045 A[PHI: r5 r15
  0x0045: PHI (r5v5 int) = (r5v1 int), (r5v12 int), (r5v1 int), (r5v1 int), (r5v1 int) binds: [B:15:0x0044, B:84:0x018c, B:71:0x0140, B:67:0x012e, B:65:0x0126] A[DONT_GENERATE, DONT_INLINE]
  0x0045: PHI (r15v4 sun.misc.Unsafe) = 
  (r15v5 sun.misc.Unsafe)
  (r15v6 sun.misc.Unsafe)
  (r15v8 sun.misc.Unsafe)
  (r15v9 sun.misc.Unsafe)
  (r15v10 sun.misc.Unsafe)
 binds: [B:15:0x0044, B:84:0x018c, B:71:0x0140, B:67:0x012e, B:65:0x0126] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zza(Object obj, byte[] bArr, int i, int i2, zzay zzayVar) throws zzco {
        Unsafe unsafe;
        int i3;
        int i4;
        int i5;
        int iZza;
        Unsafe unsafe2;
        Object obj2;
        Object objZza;
        int iZzm;
        zzds zzdsVar = this;
        byte[] bArr2 = bArr;
        int i6 = i2;
        zzay zzayVar2 = zzayVar;
        if (!zzdsVar.zzmq) {
            zza(obj, bArr, i, i6, 0, zzayVar);
            return;
        }
        Unsafe unsafe3 = zzmh;
        int iZza2 = i;
        while (iZza2 < i6) {
            int iZza3 = iZza2 + 1;
            int i7 = bArr2[iZza2];
            if (i7 < 0) {
                iZza3 = zzax.zza(i7, bArr2, iZza3, zzayVar2);
                i7 = zzayVar2.zzfd;
            }
            int i8 = i7;
            int i9 = iZza3;
            int i10 = (i8 == true ? 1 : 0) >>> 3;
            int i11 = (i8 == true ? 1 : 0) & 7;
            int iZzai = zzdsVar.zzai(i10);
            if (iZzai >= 0) {
                int i12 = zzdsVar.zzmi[iZzai + 1];
                int i13 = (267386880 & i12) >>> 20;
                long j = 1048575 & i12;
                if (i13 <= 17) {
                    switch (i13) {
                        case 0:
                            unsafe = unsafe3;
                            if (i11 == 1) {
                                zzfd.zza(obj, j, zzax.zze(bArr2, i9));
                                iZza2 = i9 + 8;
                            } else {
                                i5 = i9;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                            }
                            unsafe3 = unsafe;
                            break;
                        case 1:
                            unsafe = unsafe3;
                            if (i11 == 5) {
                                zzfd.zza(obj, j, zzax.zzf(bArr2, i9));
                                iZza2 = i9 + 4;
                            } else {
                                i5 = i9;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                            }
                            unsafe3 = unsafe;
                            break;
                        case 2:
                        case 3:
                            Unsafe unsafe4 = unsafe3;
                            if (i11 != 0) {
                                unsafe = unsafe4;
                                i5 = i9;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                int iZzb = zzax.zzb(bArr2, i9, zzayVar2);
                                unsafe3 = unsafe4;
                                unsafe3.putLong(obj, j, zzayVar2.zzfe);
                                iZza2 = iZzb;
                            }
                            break;
                        case 4:
                        case 11:
                            unsafe2 = unsafe3;
                            if (i11 != 0) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zzax.zza(bArr2, i9, zzayVar2);
                                unsafe2.putInt(obj, j, zzayVar2.zzfd);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 5:
                        case 14:
                            unsafe2 = unsafe3;
                            if (i11 != 1) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                unsafe2.putLong(obj, j, zzax.zzd(bArr2, i9));
                                iZza2 = i9 + 8;
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 6:
                        case 13:
                            unsafe2 = unsafe3;
                            if (i11 != 5) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                unsafe2.putInt(obj, j, zzax.zzc(bArr2, i9));
                                iZza2 = i9 + 4;
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 7:
                            unsafe2 = unsafe3;
                            if (i11 != 0) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zzax.zzb(bArr2, i9, zzayVar2);
                                zzfd.zza(obj, j, zzayVar2.zzfe != 0);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 8:
                            unsafe2 = unsafe3;
                            obj2 = obj;
                            if (i11 != 2) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = (536870912 & i12) == 0 ? zzax.zzc(bArr2, i9, zzayVar2) : zzax.zzd(bArr2, i9, zzayVar2);
                                objZza = zzayVar2.zzff;
                                unsafe2.putObject(obj2, j, objZza);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 9:
                            unsafe2 = unsafe3;
                            obj2 = obj;
                            if (i11 != 2) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zza(zzdsVar.zzad(iZzai), bArr2, i9, i6, zzayVar2);
                                Object object = unsafe2.getObject(obj2, j);
                                objZza = object == null ? zzayVar2.zzff : zzci.zza(object, zzayVar2.zzff);
                                unsafe2.putObject(obj2, j, objZza);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 10:
                            unsafe2 = unsafe3;
                            obj2 = obj;
                            if (i11 != 2) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zzax.zze(bArr2, i9, zzayVar2);
                                objZza = zzayVar2.zzff;
                                unsafe2.putObject(obj2, j, objZza);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 12:
                            unsafe2 = unsafe3;
                            if (i11 != 0) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zzax.zza(bArr2, i9, zzayVar2);
                                iZzm = zzayVar2.zzfd;
                                unsafe2.putInt(obj, j, iZzm);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 15:
                            unsafe2 = unsafe3;
                            if (i11 != 0) {
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                iZza2 = zzax.zza(bArr2, i9, zzayVar2);
                                iZzm = zzbk.zzm(zzayVar2.zzfd);
                                unsafe2.putInt(obj, j, iZzm);
                                unsafe3 = unsafe2;
                            }
                            break;
                        case 16:
                            if (i11 != 0) {
                                unsafe2 = unsafe3;
                                i5 = i9;
                                unsafe = unsafe2;
                                i3 = i5;
                                i4 = i8;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                                unsafe3 = unsafe;
                            } else {
                                int iZzb2 = zzax.zzb(bArr2, i9, zzayVar2);
                                unsafe3.putLong(obj, j, zzbk.zza(zzayVar2.zzfe));
                                unsafe2 = unsafe3;
                                iZza2 = iZzb2;
                                unsafe3 = unsafe2;
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    unsafe = unsafe3;
                    if (i13 != 27) {
                        if (i13 <= 49) {
                            iZza = zzdsVar.zza(obj, bArr, i9, i2, i8 == true ? 1 : 0, i10, i11, iZzai, i12, i13, j, zzayVar);
                            if (iZza == i9) {
                                obj = obj;
                                bArr = bArr;
                                i2 = i2;
                                i3 = iZza;
                                i4 = i8 == true ? 1 : 0;
                                iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                                zzdsVar = this;
                                bArr2 = bArr;
                                zzayVar2 = zzayVar;
                                i6 = i2;
                            }
                        } else {
                            if (i13 != 50) {
                                iZza = zza(obj, bArr, i9, i2, i8 == true ? 1 : 0, i10, i11, i12, i13, j, iZzai, zzayVar);
                                if (iZza == i9) {
                                    i4 = i8 == true ? 1 : 0;
                                    i3 = iZza;
                                }
                            } else if (i11 == 2) {
                                int iZza4 = zza(obj, bArr, i9, i2, iZzai, i10, j, zzayVar);
                                if (iZza4 == i9) {
                                    i3 = iZza4;
                                    i4 = i8 == true ? 1 : 0;
                                } else {
                                    zzdsVar = this;
                                    bArr2 = bArr;
                                    i6 = i2;
                                    zzayVar2 = zzayVar;
                                    iZza2 = iZza4;
                                }
                            } else {
                                i5 = i9;
                                i8 = i8 == true ? 1 : 0;
                                i3 = i5;
                                i4 = i8;
                            }
                            iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                            zzdsVar = this;
                            bArr2 = bArr;
                            zzayVar2 = zzayVar;
                            i6 = i2;
                        }
                        zzdsVar = this;
                        bArr2 = bArr;
                        i6 = i2;
                        zzayVar2 = zzayVar;
                        iZza2 = iZza;
                    } else if (i11 == 2) {
                        zzcn zzcnVarZzi = (zzcn) unsafe.getObject(obj, j);
                        if (!zzcnVarZzi.zzu()) {
                            int size = zzcnVarZzi.size();
                            zzcnVarZzi = zzcnVarZzi.zzi(size == 0 ? 10 : size << 1);
                            unsafe.putObject(obj, j, zzcnVarZzi);
                        }
                        iZza2 = zza(zzdsVar.zzad(iZzai), i8 == true ? 1 : 0, bArr2, i9, i6, zzcnVarZzi, zzayVar2);
                        bArr2 = bArr;
                        i6 = i2;
                        zzayVar2 = zzayVar;
                    } else {
                        i8 = i8 == true ? 1 : 0;
                        i5 = i9;
                        i3 = i5;
                        i4 = i8;
                        iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
                        zzdsVar = this;
                        bArr2 = bArr;
                        zzayVar2 = zzayVar;
                        i6 = i2;
                    }
                    unsafe3 = unsafe;
                }
            }
            unsafe = unsafe3;
            i5 = i9;
            i3 = i5;
            i4 = i8;
            iZza2 = zza(i4, bArr, i3, i2, obj, zzayVar);
            zzdsVar = this;
            bArr2 = bArr;
            zzayVar2 = zzayVar;
            i6 = i2;
            unsafe3 = unsafe;
        }
        if (iZza2 != i6) {
            throw zzco.zzbo();
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj) {
        int[] iArr = this.zzmt;
        if (iArr != null) {
            for (int i : iArr) {
                long jZzag = zzag(i) & 1048575;
                Object objZzo = zzfd.zzo(obj, jZzag);
                if (objZzo != null) {
                    zzfd.zza(obj, jZzag, this.zzmz.zzj(objZzo));
                }
            }
        }
        int[] iArr2 = this.zzmu;
        if (iArr2 != null) {
            for (int i2 : iArr2) {
                this.zzmw.zza(obj, i2);
            }
        }
        this.zzmx.zzc(obj);
        if (this.zzmo) {
            this.zzmy.zzc(obj);
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x002b  */
    /* JADX WARN: Code duplicated, block: B:20:0x0057  */
    /* JADX WARN: Code duplicated, block: B:24:0x0069  */
    /* JADX WARN: Code duplicated, block: B:39:0x0093  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj, Object obj2) {
        obj2.getClass();
        for (int i = 0; i < this.zzmi.length; i += 4) {
            int iZzag = zzag(i);
            long j = 1048575 & iZzag;
            int i2 = this.zzmi[i];
            switch ((iZzag & 267386880) >>> 20) {
                case 0:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzn(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 1:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzm(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 2:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 3:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 4:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 5:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 6:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 7:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzl(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 8:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 9:
                case 17:
                    zza(obj, obj2, i);
                    break;
                case 10:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 11:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 12:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 13:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 14:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 15:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 16:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    this.zzmw.zza(obj, obj2, j);
                    break;
                case 50:
                    zzeh.zza(this.zzmz, obj, obj2, j);
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                    if (zza(obj2, i2, i)) {
                        zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                        zzb(obj, i2, i);
                    }
                    break;
                case 60:
                case 68:
                    zzb(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (zza(obj2, i2, i)) {
                        zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                        zzb(obj, i2, i);
                    }
                    break;
            }
        }
        if (this.zzmq) {
            return;
        }
        zzeh.zza(this.zzmx, obj, obj2);
        if (this.zzmo) {
            zzeh.zza(this.zzmy, obj, obj2);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:17:0x004f  */
    /* JADX WARN: Code duplicated, block: B:250:0x0436 A[PHI: r5
  0x0436: PHI (r5v4 int) = 
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v12 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v1 int)
  (r5v13 int)
  (r5v1 int)
 binds: [B:244:0x041d, B:437:0x0770, B:434:0x076a, B:429:0x075c, B:426:0x0756, B:423:0x0750, B:420:0x0746, B:417:0x073c, B:414:0x0736, B:411:0x0730, B:408:0x0726, B:405:0x071c, B:402:0x0716, B:382:0x0658, B:377:0x0646, B:372:0x0634, B:367:0x0622, B:362:0x0610, B:357:0x05fe, B:352:0x05ec, B:347:0x05db, B:342:0x05ca, B:337:0x05b9, B:332:0x05a8, B:327:0x0597, B:322:0x0586, B:316:0x0566, B:311:0x0532, B:308:0x0525, B:305:0x0515, B:302:0x0505, B:299:0x04f5, B:296:0x04e9, B:293:0x04dd, B:290:0x04d1, B:284:0x04b8, B:281:0x04a5, B:277:0x0494, B:273:0x0485, B:269:0x0476, B:267:0x0470, B:265:0x0469, B:262:0x045e, B:258:0x044f, B:254:0x0440, B:249:0x0435, B:247:0x0425] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:287:0x04c2 A[PHI: r6
  0x04c2: PHI (r6v114 java.lang.Object) = (r6v25 java.lang.Object), (r6v117 java.lang.Object) binds: [B:431:0x0764, B:286:0x04c0] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:288:0x04c5 A[PHI: r6
  0x04c5: PHI (r6v111 java.lang.Object) = (r6v25 java.lang.Object), (r6v117 java.lang.Object) binds: [B:431:0x0764, B:286:0x04c0] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:29:0x0084  */
    /* JADX WARN: Code duplicated, block: B:319:0x056c A[PHI: r8
  0x056c: PHI (r8v75 int) = 
  (r8v37 int)
  (r8v40 int)
  (r8v43 int)
  (r8v46 int)
  (r8v49 int)
  (r8v52 int)
  (r8v55 int)
  (r8v58 int)
  (r8v61 int)
  (r8v64 int)
  (r8v67 int)
  (r8v70 int)
  (r8v73 int)
  (r8v78 int)
 binds: [B:384:0x065c, B:379:0x064a, B:374:0x0638, B:369:0x0626, B:364:0x0614, B:359:0x0602, B:354:0x05f0, B:349:0x05df, B:344:0x05ce, B:339:0x05bd, B:334:0x05ac, B:329:0x059b, B:324:0x058a, B:318:0x056a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:32:0x008f  */
    /* JADX WARN: Code duplicated, block: B:43:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:47:0x00c9  */
    /* JADX WARN: Code duplicated, block: B:52:0x00e4 A[PHI: r4
  0x00e4: PHI (r4v92 java.lang.Object) = (r4v12 java.lang.Object), (r4v94 java.lang.Object) binds: [B:197:0x0369, B:51:0x00e2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:53:0x00e7 A[PHI: r4
  0x00e7: PHI (r4v90 java.lang.Object) = (r4v12 java.lang.Object), (r4v94 java.lang.Object) binds: [B:197:0x0369, B:51:0x00e2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:56:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:59:0x0101  */
    /* JADX WARN: Code duplicated, block: B:62:0x010d  */
    /* JADX WARN: Code duplicated, block: B:77:0x0149  */
    /* JADX WARN: Code duplicated, block: B:80:0x0155  */
    /* JADX WARN: Code duplicated, block: B:87:0x018b A[PHI: r4
  0x018b: PHI (r4v72 int) = 
  (r4v34 int)
  (r4v37 int)
  (r4v40 int)
  (r4v43 int)
  (r4v46 int)
  (r4v49 int)
  (r4v52 int)
  (r4v55 int)
  (r4v58 int)
  (r4v61 int)
  (r4v64 int)
  (r4v67 int)
  (r4v70 int)
  (r4v75 int)
 binds: [B:152:0x027b, B:147:0x0269, B:142:0x0257, B:137:0x0245, B:132:0x0233, B:127:0x0221, B:122:0x020f, B:117:0x01fe, B:112:0x01ed, B:107:0x01dc, B:102:0x01cb, B:97:0x01ba, B:92:0x01a9, B:86:0x0189] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0425, code lost:
    
        if (zza(r21, r15, r4) != false) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x0427, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzdo) r2.getObject(r21, r13), zzad(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x045e, code lost:
    
        if (zza(r21, r15, r4) != false) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0460, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0469, code lost:
    
        if (zza(r21, r15, r4) != false) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x046b, code lost:
    
        r8 = com.google.android.gms.internal.clearcut.zzbn.zzk(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x0494, code lost:
    
        if (zza(r21, r15, r4) != false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0496, code lost:
    
        r6 = (com.google.android.gms.internal.clearcut.zzbb) r2.getObject(r21, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x04a5, code lost:
    
        if (zza(r21, r15, r4) != false) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x04a7, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzeh.zzc(r15, r2.getObject(r21, r13), zzad(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x04d1, code lost:
    
        if (zza(r21, r15, r4) != false) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x04d3, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x0716, code lost:
    
        if ((r12 & r19) != 0) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0730, code lost:
    
        if ((r12 & r19) != 0) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0736, code lost:
    
        if ((r12 & r19) != 0) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0750, code lost:
    
        if ((r12 & r19) != 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0756, code lost:
    
        if ((r12 & r19) != 0) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x076a, code lost:
    
        if ((r12 & r19) != 0) goto L291;
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzm(Object obj) {
        int i;
        int i2;
        boolean z;
        boolean z2;
        int iZzd;
        Object object;
        int iZzg;
        int iZzg2;
        int iZzg3;
        long jZzh;
        int iZzw;
        boolean z3;
        int iZzo;
        int iZzi;
        int iZzb;
        long jZzk;
        long jZzk2;
        int iZzj;
        Object objZzo;
        int iZzj2;
        int iZzj3;
        int iZzj4;
        long jZzk3;
        int iZzo2;
        int iZzi2;
        zzbb zzbbVar;
        int i3 = 267386880;
        int i4 = 1048575;
        if (!this.zzmq) {
            Unsafe unsafe = zzmh;
            int i5 = -1;
            int i6 = 0;
            int iZzb2 = 0;
            int i7 = 0;
            while (i6 < this.zzmi.length) {
                int iZzag = zzag(i6);
                int[] iArr = this.zzmi;
                int i8 = iArr[i6];
                int i9 = i4;
                int i10 = (iZzag & 267386880) >>> 20;
                if (i10 <= 17) {
                    i = iArr[i6 + 2];
                    int i11 = i & i9;
                    i2 = 1 << (i >>> 20);
                    if (i11 != i5) {
                        i7 = unsafe.getInt(obj, i11);
                        i5 = i11;
                    }
                } else {
                    i = (!this.zzmr || i10 < zzcb.zzih.id() || i10 > zzcb.zziu.id()) ? 0 : this.zzmi[i6 + 2] & i9;
                    i2 = 0;
                }
                long j = iZzag & i9;
                switch (i10) {
                    case 0:
                        z = false;
                        z2 = false;
                        if ((i7 & i2) != 0) {
                            iZzb2 += zzbn.zzb(i8, 0.0d);
                        }
                        break;
                    case 1:
                        z = false;
                        if ((i7 & i2) != 0) {
                            z2 = false;
                            iZzb2 += zzbn.zzb(i8, 0.0f);
                        } else {
                            z2 = false;
                        }
                        break;
                    case 2:
                        z = false;
                        if ((i7 & i2) != 0) {
                            iZzd = zzbn.zzd(i8, unsafe.getLong(obj, j));
                            iZzb2 += iZzd;
                        }
                        z2 = false;
                        break;
                    case 3:
                        z = false;
                        if ((i7 & i2) != 0) {
                            iZzd = zzbn.zze(i8, unsafe.getLong(obj, j));
                            iZzb2 += iZzd;
                        }
                        z2 = false;
                        break;
                    case 4:
                        z = false;
                        if ((i7 & i2) != 0) {
                            iZzd = zzbn.zzg(i8, unsafe.getInt(obj, j));
                            iZzb2 += iZzd;
                        }
                        z2 = false;
                        break;
                    case 5:
                        z = false;
                        if ((i7 & i2) != 0) {
                            iZzd = zzbn.zzg(i8, 0L);
                            iZzb2 += iZzd;
                        }
                        z2 = false;
                        break;
                    case 6:
                        if ((i7 & i2) != 0) {
                            z = false;
                            iZzd = zzbn.zzj(i8, 0);
                            iZzb2 += iZzd;
                        } else {
                            z = false;
                        }
                        z2 = false;
                        break;
                    case 7:
                        break;
                    case 8:
                        if ((i7 & i2) != 0) {
                            object = unsafe.getObject(obj, j);
                            if (object instanceof zzbb) {
                                zzbb zzbbVar2 = (zzbb) object;
                                iZzw = zzbn.zzc(i8, zzbbVar2);
                            } else {
                                iZzw = zzbn.zzb(i8, (String) object);
                            }
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        if ((i7 & i2) != 0) {
                            iZzg = unsafe.getInt(obj, j);
                            iZzw = zzbn.zzh(i8, iZzg);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 12:
                        if ((i7 & i2) != 0) {
                            iZzg2 = unsafe.getInt(obj, j);
                            iZzw = zzbn.zzl(i8, iZzg2);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        if ((i7 & i2) != 0) {
                            iZzg3 = unsafe.getInt(obj, j);
                            iZzw = zzbn.zzi(i8, iZzg3);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 16:
                        if ((i7 & i2) != 0) {
                            jZzh = unsafe.getLong(obj, j);
                            iZzw = zzbn.zzf(i8, jZzh);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 17:
                        break;
                    case 18:
                        iZzw = zzeh.zzw(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 19:
                    case 24:
                    case 31:
                        z3 = false;
                        iZzo = zzeh.zzv(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 20:
                        z3 = false;
                        iZzo = zzeh.zzo(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 21:
                        z3 = false;
                        iZzo = zzeh.zzp(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 22:
                        z3 = false;
                        iZzo = zzeh.zzs(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 23:
                    case 32:
                        z3 = false;
                        iZzo = zzeh.zzw(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 25:
                        z3 = false;
                        iZzo = zzeh.zzx(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 26:
                        iZzw = zzeh.zzc(i8, (List) unsafe.getObject(obj, j));
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 27:
                        iZzw = zzeh.zzc(i8, (List) unsafe.getObject(obj, j), zzad(i6));
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 28:
                        iZzw = zzeh.zzd(i8, (List) unsafe.getObject(obj, j));
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 29:
                        iZzw = zzeh.zzt(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 30:
                        z3 = false;
                        iZzo = zzeh.zzr(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 33:
                        z3 = false;
                        iZzo = zzeh.zzu(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 34:
                        z3 = false;
                        iZzo = zzeh.zzq(i8, (List) unsafe.getObject(obj, j), false);
                        iZzb2 += iZzo;
                        z = z3;
                        z2 = false;
                        break;
                    case 35:
                        iZzi = zzeh.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 36:
                        iZzi = zzeh.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 37:
                        iZzi = zzeh.zza((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 38:
                        iZzi = zzeh.zzb((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 39:
                        iZzi = zzeh.zze((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 40:
                        iZzi = zzeh.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 41:
                        iZzi = zzeh.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 42:
                        iZzi = zzeh.zzj((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 43:
                        iZzi = zzeh.zzf((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 44:
                        iZzi = zzeh.zzd((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 45:
                        iZzi = zzeh.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 46:
                        iZzi = zzeh.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 47:
                        iZzi = zzeh.zzg((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 48:
                        iZzi = zzeh.zzc((List) unsafe.getObject(obj, j));
                        if (iZzi > 0) {
                            if (this.zzmr) {
                                unsafe.putInt(obj, i, iZzi);
                            }
                            iZzw = zzbn.zzr(i8) + zzbn.zzt(iZzi) + iZzi;
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 49:
                        iZzw = zzeh.zzd(i8, (List) unsafe.getObject(obj, j), zzad(i6));
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 50:
                        iZzw = this.zzmz.zzb(i8, unsafe.getObject(obj, j), zzae(i6));
                        iZzb2 += iZzw;
                        z = false;
                        z2 = false;
                        break;
                    case 51:
                        if (zza(obj, i8, i6)) {
                            iZzw = zzbn.zzb(i8, 0.0d);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 52:
                        if (zza(obj, i8, i6)) {
                            iZzb = zzbn.zzb(i8, 0.0f);
                            iZzb2 += iZzb;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 53:
                        if (zza(obj, i8, i6)) {
                            iZzw = zzbn.zzd(i8, zzh(obj, j));
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 54:
                        if (zza(obj, i8, i6)) {
                            iZzw = zzbn.zze(i8, zzh(obj, j));
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 55:
                        if (zza(obj, i8, i6)) {
                            iZzw = zzbn.zzg(i8, zzg(obj, j));
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 56:
                        if (zza(obj, i8, i6)) {
                            iZzw = zzbn.zzg(i8, 0L);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 57:
                        if (zza(obj, i8, i6)) {
                            iZzb = zzbn.zzj(i8, 0);
                            iZzb2 += iZzb;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 58:
                        break;
                    case 59:
                        if (zza(obj, i8, i6)) {
                            object = unsafe.getObject(obj, j);
                            if (object instanceof zzbb) {
                                zzbb zzbbVar3 = (zzbb) object;
                                iZzw = zzbn.zzc(i8, zzbbVar3);
                            } else {
                                iZzw = zzbn.zzb(i8, (String) object);
                            }
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 60:
                        break;
                    case 61:
                        break;
                    case 62:
                        if (zza(obj, i8, i6)) {
                            iZzg = zzg(obj, j);
                            iZzw = zzbn.zzh(i8, iZzg);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 63:
                        if (zza(obj, i8, i6)) {
                            iZzg2 = zzg(obj, j);
                            iZzw = zzbn.zzl(i8, iZzg2);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 64:
                        break;
                    case 65:
                        break;
                    case 66:
                        if (zza(obj, i8, i6)) {
                            iZzg3 = zzg(obj, j);
                            iZzw = zzbn.zzi(i8, iZzg3);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 67:
                        if (zza(obj, i8, i6)) {
                            jZzh = zzh(obj, j);
                            iZzw = zzbn.zzf(i8, jZzh);
                            iZzb2 += iZzw;
                        }
                        z = false;
                        z2 = false;
                        break;
                    case 68:
                        break;
                    default:
                        z = false;
                        z2 = false;
                        break;
                }
                i6 += 4;
                i4 = i9;
            }
            int iZza = iZzb2 + zza(this.zzmx, obj);
            return this.zzmo ? iZza + this.zzmy.zza(obj).zzas() : iZza;
        }
        Unsafe unsafe2 = zzmh;
        int i12 = 0;
        int i13 = 0;
        while (i12 < this.zzmi.length) {
            int iZzag2 = zzag(i12);
            int i14 = (iZzag2 & i3) >>> 20;
            int i15 = i3;
            int i16 = this.zzmi[i12];
            long j2 = iZzag2 & 1048575;
            int i17 = (i14 < zzcb.zzih.id() || i14 > zzcb.zziu.id()) ? 0 : this.zzmi[i12 + 2] & 1048575;
            switch (i14) {
                case 0:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzb(i16, 0.0d);
                        i13 += iZzo2;
                    }
                    break;
                case 1:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzb(i16, 0.0f);
                        i13 += iZzo2;
                    }
                    break;
                case 2:
                    if (zza(obj, i12)) {
                        jZzk = zzfd.zzk(obj, j2);
                        iZzo2 = zzbn.zzd(i16, jZzk);
                        i13 += iZzo2;
                    }
                    break;
                case 3:
                    if (zza(obj, i12)) {
                        jZzk2 = zzfd.zzk(obj, j2);
                        iZzo2 = zzbn.zze(i16, jZzk2);
                        i13 += iZzo2;
                    }
                    break;
                case 4:
                    if (zza(obj, i12)) {
                        iZzj = zzfd.zzj(obj, j2);
                        iZzo2 = zzbn.zzg(i16, iZzj);
                        i13 += iZzo2;
                    }
                    break;
                case 5:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzg(i16, 0L);
                        i13 += iZzo2;
                    }
                    break;
                case 6:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzj(i16, 0);
                        i13 += iZzo2;
                    }
                    break;
                case 7:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzc(i16, true);
                        i13 += iZzo2;
                    }
                    break;
                case 8:
                    if (zza(obj, i12)) {
                        objZzo = zzfd.zzo(obj, j2);
                        if (objZzo instanceof zzbb) {
                            zzbbVar = (zzbb) objZzo;
                            iZzo2 = zzbn.zzc(i16, zzbbVar);
                        } else {
                            iZzo2 = zzbn.zzb(i16, (String) objZzo);
                        }
                        i13 += iZzo2;
                    }
                    break;
                case 9:
                    if (zza(obj, i12)) {
                        iZzo2 = zzeh.zzc(i16, zzfd.zzo(obj, j2), zzad(i12));
                        i13 += iZzo2;
                    }
                    break;
                case 10:
                    if (zza(obj, i12)) {
                        zzbbVar = (zzbb) zzfd.zzo(obj, j2);
                        iZzo2 = zzbn.zzc(i16, zzbbVar);
                        i13 += iZzo2;
                    }
                    break;
                case 11:
                    if (zza(obj, i12)) {
                        iZzj2 = zzfd.zzj(obj, j2);
                        iZzo2 = zzbn.zzh(i16, iZzj2);
                        i13 += iZzo2;
                    }
                    break;
                case 12:
                    if (zza(obj, i12)) {
                        iZzj3 = zzfd.zzj(obj, j2);
                        iZzo2 = zzbn.zzl(i16, iZzj3);
                        i13 += iZzo2;
                    }
                    break;
                case 13:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzk(i16, 0);
                        i13 += iZzo2;
                    }
                    break;
                case 14:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzh(i16, 0L);
                        i13 += iZzo2;
                    }
                    break;
                case 15:
                    if (zza(obj, i12)) {
                        iZzj4 = zzfd.zzj(obj, j2);
                        iZzo2 = zzbn.zzi(i16, iZzj4);
                        i13 += iZzo2;
                    }
                    break;
                case 16:
                    if (zza(obj, i12)) {
                        jZzk3 = zzfd.zzk(obj, j2);
                        iZzo2 = zzbn.zzf(i16, jZzk3);
                        i13 += iZzo2;
                    }
                    break;
                case 17:
                    if (zza(obj, i12)) {
                        iZzo2 = zzbn.zzc(i16, (zzdo) zzfd.zzo(obj, j2), zzad(i12));
                        i13 += iZzo2;
                    }
                    break;
                case 18:
                case 23:
                case 32:
                    iZzo2 = zzeh.zzw(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 19:
                case 24:
                case 31:
                    iZzo2 = zzeh.zzv(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 20:
                    iZzo2 = zzeh.zzo(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 21:
                    iZzo2 = zzeh.zzp(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 22:
                    iZzo2 = zzeh.zzs(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 25:
                    iZzo2 = zzeh.zzx(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 26:
                    iZzo2 = zzeh.zzc(i16, zzd(obj, j2));
                    i13 += iZzo2;
                    break;
                case 27:
                    iZzo2 = zzeh.zzc(i16, zzd(obj, j2), zzad(i12));
                    i13 += iZzo2;
                    break;
                case 28:
                    iZzo2 = zzeh.zzd(i16, zzd(obj, j2));
                    i13 += iZzo2;
                    break;
                case 29:
                    iZzo2 = zzeh.zzt(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 30:
                    iZzo2 = zzeh.zzr(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 33:
                    iZzo2 = zzeh.zzu(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 34:
                    iZzo2 = zzeh.zzq(i16, zzd(obj, j2), false);
                    i13 += iZzo2;
                    break;
                case 35:
                    iZzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 36:
                    iZzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 37:
                    iZzi2 = zzeh.zza((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 38:
                    iZzi2 = zzeh.zzb((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 39:
                    iZzi2 = zzeh.zze((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 40:
                    iZzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 41:
                    iZzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 42:
                    iZzi2 = zzeh.zzj((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 43:
                    iZzi2 = zzeh.zzf((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 44:
                    iZzi2 = zzeh.zzd((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 45:
                    iZzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 46:
                    iZzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 47:
                    iZzi2 = zzeh.zzg((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 48:
                    iZzi2 = zzeh.zzc((List) unsafe2.getObject(obj, j2));
                    if (iZzi2 > 0) {
                        if (this.zzmr) {
                            unsafe2.putInt(obj, i17, iZzi2);
                        }
                        iZzo2 = zzbn.zzr(i16) + zzbn.zzt(iZzi2) + iZzi2;
                        i13 += iZzo2;
                    }
                    break;
                case 49:
                    iZzo2 = zzeh.zzd(i16, zzd(obj, j2), zzad(i12));
                    i13 += iZzo2;
                    break;
                case 50:
                    iZzo2 = this.zzmz.zzb(i16, zzfd.zzo(obj, j2), zzae(i12));
                    i13 += iZzo2;
                    break;
                case 51:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzb(i16, 0.0d);
                        i13 += iZzo2;
                    }
                    break;
                case 52:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzb(i16, 0.0f);
                        i13 += iZzo2;
                    }
                    break;
                case 53:
                    if (zza(obj, i16, i12)) {
                        jZzk = zzh(obj, j2);
                        iZzo2 = zzbn.zzd(i16, jZzk);
                        i13 += iZzo2;
                    }
                    break;
                case 54:
                    if (zza(obj, i16, i12)) {
                        jZzk2 = zzh(obj, j2);
                        iZzo2 = zzbn.zze(i16, jZzk2);
                        i13 += iZzo2;
                    }
                    break;
                case 55:
                    if (zza(obj, i16, i12)) {
                        iZzj = zzg(obj, j2);
                        iZzo2 = zzbn.zzg(i16, iZzj);
                        i13 += iZzo2;
                    }
                    break;
                case 56:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzg(i16, 0L);
                        i13 += iZzo2;
                    }
                    break;
                case 57:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzj(i16, 0);
                        i13 += iZzo2;
                    }
                    break;
                case 58:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzc(i16, true);
                        i13 += iZzo2;
                    }
                    break;
                case 59:
                    if (zza(obj, i16, i12)) {
                        objZzo = zzfd.zzo(obj, j2);
                        if (objZzo instanceof zzbb) {
                            zzbbVar = (zzbb) objZzo;
                            iZzo2 = zzbn.zzc(i16, zzbbVar);
                        } else {
                            iZzo2 = zzbn.zzb(i16, (String) objZzo);
                        }
                        i13 += iZzo2;
                    }
                    break;
                case 60:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzeh.zzc(i16, zzfd.zzo(obj, j2), zzad(i12));
                        i13 += iZzo2;
                    }
                    break;
                case 61:
                    if (zza(obj, i16, i12)) {
                        zzbbVar = (zzbb) zzfd.zzo(obj, j2);
                        iZzo2 = zzbn.zzc(i16, zzbbVar);
                        i13 += iZzo2;
                    }
                    break;
                case 62:
                    if (zza(obj, i16, i12)) {
                        iZzj2 = zzg(obj, j2);
                        iZzo2 = zzbn.zzh(i16, iZzj2);
                        i13 += iZzo2;
                    }
                    break;
                case 63:
                    if (zza(obj, i16, i12)) {
                        iZzj3 = zzg(obj, j2);
                        iZzo2 = zzbn.zzl(i16, iZzj3);
                        i13 += iZzo2;
                    }
                    break;
                case 64:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzk(i16, 0);
                        i13 += iZzo2;
                    }
                    break;
                case 65:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzh(i16, 0L);
                        i13 += iZzo2;
                    }
                    break;
                case 66:
                    if (zza(obj, i16, i12)) {
                        iZzj4 = zzg(obj, j2);
                        iZzo2 = zzbn.zzi(i16, iZzj4);
                        i13 += iZzo2;
                    }
                    break;
                case 67:
                    if (zza(obj, i16, i12)) {
                        jZzk3 = zzh(obj, j2);
                        iZzo2 = zzbn.zzf(i16, jZzk3);
                        i13 += iZzo2;
                    }
                    break;
                case 68:
                    if (zza(obj, i16, i12)) {
                        iZzo2 = zzbn.zzc(i16, (zzdo) zzfd.zzo(obj, j2), zzad(i12));
                        i13 += iZzo2;
                    }
                    break;
            }
            i12 += 4;
            i3 = i15;
        }
        return i13 + zza(this.zzmx, obj);
    }

    /* JADX WARN: Code duplicated, block: B:46:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:48:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:51:0x00c1  */
    /* JADX WARN: Code duplicated, block: B:54:0x00cc A[LOOP:1: B:49:0x00bb->B:54:0x00cc, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:71:0x00cb A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:75:0x00e0 A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean zzo(Object obj) {
        int i;
        List list;
        zzef zzefVarZzad;
        int i2;
        int[] iArr = this.zzms;
        int i3 = 1;
        if (iArr == null || iArr.length == 0) {
            return true;
        }
        int i4 = -1;
        int i5 = 0;
        int i6 = 0;
        for (int length = iArr.length; i5 < length; length = length) {
            int i7 = iArr[i5];
            int iZzai = zzai(i7);
            int iZzag = zzag(iZzai);
            if (this.zzmq) {
                i = 0;
            } else {
                int i8 = this.zzmi[iZzai + 2];
                int i9 = i8 & 1048575;
                i = i3 << (i8 >>> 20);
                if (i9 != i4) {
                    i6 = zzmh.getInt(obj, i9);
                    i4 = i9;
                }
            }
            if ((268435456 & iZzag) != 0 && !zza(obj, iZzai, i6, i)) {
                return false;
            }
            int i10 = (267386880 & iZzag) >>> 20;
            if (i10 == 9 || i10 == 17) {
                if (zza(obj, iZzai, i6, i) && !zza(obj, iZzag, zzad(iZzai))) {
                    return false;
                }
            } else if (i10 == 27) {
                list = (List) zzfd.zzo(obj, iZzag & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzefVarZzad = zzad(iZzai);
                    for (i2 = 0; i2 < list.size(); i2++) {
                        if (!zzefVarZzad.zzo(list.get(i2))) {
                            return false;
                        }
                    }
                }
            } else if (i10 == 60 || i10 == 68) {
                if (zza(obj, i7, iZzai) && !zza(obj, iZzag, zzad(iZzai))) {
                    return false;
                }
            } else if (i10 == 49) {
                list = (List) zzfd.zzo(obj, iZzag & 1048575);
                if (list.isEmpty()) {
                    zzefVarZzad = zzad(iZzai);
                    while (i2 < list.size()) {
                        if (!zzefVarZzad.zzo(list.get(i2))) {
                            return false;
                        }
                    }
                } else {
                    continue;
                }
            } else if (i10 == 50 && !this.zzmz.zzh(zzfd.zzo(obj, iZzag & 1048575)).isEmpty()) {
                this.zzmz.zzl(zzae(iZzai));
                throw null;
            }
            i5++;
            i3 = i3;
        }
        boolean z = i3;
        if (!this.zzmo || this.zzmy.zza(obj).isInitialized()) {
            return z;
        }
        return false;
    }
}
