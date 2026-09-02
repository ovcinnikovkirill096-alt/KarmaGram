package com.google.android.gms.internal.measurement;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import sun.misc.Unsafe;

final class zznp implements zznx {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzop.zzq();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zznm zzg;
    private final boolean zzh = false;
    private final int[] zzi;
    private final int zzj;
    private final int zzk;
    private final zzoi zzl;
    private final zzls zzm;

    private zznp(int[] iArr, Object[] objArr, int i, int i2, zznm zznmVar, boolean z, int[] iArr2, int i3, int i4, zznr zznrVar, zzmy zzmyVar, zzoi zzoiVar, zzls zzlsVar, zznh zznhVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = iArr2;
        this.zzj = i3;
        this.zzk = i4;
        this.zzl = zzoiVar;
        this.zzm = zzlsVar;
        this.zzg = zznmVar;
    }

    private static boolean zzA(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof zzmf) {
            return ((zzmf) obj).zzcf();
        }
        return true;
    }

    private static void zzB(Object obj) {
        if (!zzA(obj)) {
            throw new IllegalArgumentException("Mutating immutable message: ".concat(String.valueOf(obj)));
        }
    }

    private static double zzC(Object obj, long j) {
        return ((Double) zzop.zzn(obj, j)).doubleValue();
    }

    private static float zzD(Object obj, long j) {
        return ((Float) zzop.zzn(obj, j)).floatValue();
    }

    private static int zzE(Object obj, long j) {
        return ((Integer) zzop.zzn(obj, j)).intValue();
    }

    private static long zzF(Object obj, long j) {
        return ((Long) zzop.zzn(obj, j)).longValue();
    }

    private static boolean zzG(Object obj, long j) {
        return ((Boolean) zzop.zzn(obj, j)).booleanValue();
    }

    private final boolean zzH(Object obj, Object obj2, int i) {
        return zzJ(obj, i) == zzJ(obj2, i);
    }

    private final boolean zzI(Object obj, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zzJ(obj, i);
        }
        return (i3 & i4) != 0;
    }

    private final boolean zzJ(Object obj, int i) {
        int iZzy = zzy(i);
        long j = iZzy & 1048575;
        if (j != 1048575) {
            return (zzop.zzd(obj, j) & (1 << (iZzy >>> 20))) != 0;
        }
        int iZzx = zzx(i);
        long j2 = iZzx & 1048575;
        switch (zzz(iZzx)) {
            case 0:
                return Double.doubleToRawLongBits(zzop.zzl(obj, j2)) != 0;
            case 1:
                return Float.floatToRawIntBits(zzop.zzj(obj, j2)) != 0;
            case 2:
                return zzop.zzf(obj, j2) != 0;
            case 3:
                return zzop.zzf(obj, j2) != 0;
            case 4:
                return zzop.zzd(obj, j2) != 0;
            case 5:
                return zzop.zzf(obj, j2) != 0;
            case 6:
                return zzop.zzd(obj, j2) != 0;
            case 7:
                return zzop.zzh(obj, j2);
            case 8:
                Object objZzn = zzop.zzn(obj, j2);
                if (objZzn instanceof String) {
                    return !((String) objZzn).isEmpty();
                }
                if (objZzn instanceof zzlh) {
                    return !zzlh.zzb.equals(objZzn);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzop.zzn(obj, j2) != null;
            case 10:
                return !zzlh.zzb.equals(zzop.zzn(obj, j2));
            case 11:
                return zzop.zzd(obj, j2) != 0;
            case 12:
                return zzop.zzd(obj, j2) != 0;
            case 13:
                return zzop.zzd(obj, j2) != 0;
            case 14:
                return zzop.zzf(obj, j2) != 0;
            case 15:
                return zzop.zzd(obj, j2) != 0;
            case 16:
                return zzop.zzf(obj, j2) != 0;
            case 17:
                return zzop.zzn(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final void zzK(Object obj, int i) {
        int iZzy = zzy(i);
        long j = 1048575 & iZzy;
        if (j == 1048575) {
            return;
        }
        zzop.zze(obj, j, (1 << (iZzy >>> 20)) | zzop.zzd(obj, j));
    }

    private final boolean zzL(Object obj, int i, int i2) {
        return zzop.zzd(obj, (long) (zzy(i2) & 1048575)) == i;
    }

    private final void zzM(Object obj, int i, int i2) {
        zzop.zze(obj, zzy(i2) & 1048575, i);
    }

    private final int zzN(int i, int i2) {
        int[] iArr = this.zzc;
        int length = (iArr.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int i5 = iArr[i4];
            if (i == i5) {
                return i4;
            }
            if (i < i5) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }

    private static final int zzO(byte[] bArr, int i, int i2, zzot zzotVar, Class cls, zzkw zzkwVar) {
        zzot zzotVar2 = zzot.zza;
        switch (zzotVar.ordinal()) {
            case 0:
                int i3 = i + 8;
                zzkwVar.zzc = Double.valueOf(Double.longBitsToDouble(zzkx.zze(bArr, i)));
                return i3;
            case 1:
                int i4 = i + 4;
                zzkwVar.zzc = Float.valueOf(Float.intBitsToFloat(zzkx.zzd(bArr, i)));
                return i4;
            case 2:
            case 3:
                int iZzc = zzkx.zzc(bArr, i, zzkwVar);
                zzkwVar.zzc = Long.valueOf(zzkwVar.zzb);
                return iZzc;
            case 4:
            case 12:
            case 13:
                int iZza = zzkx.zza(bArr, i, zzkwVar);
                zzkwVar.zzc = Integer.valueOf(zzkwVar.zza);
                return iZza;
            case 5:
            case 15:
                int i5 = i + 8;
                zzkwVar.zzc = Long.valueOf(zzkx.zze(bArr, i));
                return i5;
            case 6:
            case 14:
                int i6 = i + 4;
                zzkwVar.zzc = Integer.valueOf(zzkx.zzd(bArr, i));
                return i6;
            case 7:
                int iZzc2 = zzkx.zzc(bArr, i, zzkwVar);
                zzkwVar.zzc = Boolean.valueOf(zzkwVar.zzb != 0);
                return iZzc2;
            case 8:
                return zzkx.zzf(bArr, i, zzkwVar);
            case 9:
            default:
                throw new RuntimeException("unsupported field type.");
            case 10:
                return zzkx.zzh(zznu.zza().zzb(cls), bArr, i, i2, zzkwVar);
            case 11:
                return zzkx.zzg(bArr, i, zzkwVar);
            case 16:
                int iZza2 = zzkx.zza(bArr, i, zzkwVar);
                zzkwVar.zzc = Integer.valueOf(zzlj.zzb(zzkwVar.zza));
                return iZza2;
            case 17:
                int iZzc3 = zzkx.zzc(bArr, i, zzkwVar);
                zzkwVar.zzc = Long.valueOf(zzlj.zzc(zzkwVar.zzb));
                return iZzc3;
        }
    }

    private static final void zzP(int i, Object obj, zzov zzovVar) {
        if (obj instanceof String) {
            zzovVar.zzm(i, (String) obj);
        } else {
            zzovVar.zzn(i, (zzlh) obj);
        }
    }

    static zzoj zzg(Object obj) {
        zzmf zzmfVar = (zzmf) obj;
        zzoj zzojVar = zzmfVar.zzc;
        if (zzojVar != zzoj.zza()) {
            return zzojVar;
        }
        zzoj zzojVarZzb = zzoj.zzb();
        zzmfVar.zzc = zzojVarZzb;
        return zzojVarZzb;
    }

    /* JADX WARN: Code duplicated, block: B:126:0x026e  */
    /* JADX WARN: Code duplicated, block: B:128:0x0274  */
    /* JADX WARN: Code duplicated, block: B:131:0x028c  */
    /* JADX WARN: Code duplicated, block: B:132:0x028f  */
    /* JADX WARN: Code duplicated, block: B:171:0x0350  */
    /* JADX WARN: Code duplicated, block: B:187:0x03a6  */
    /* JADX WARN: Code duplicated, block: B:190:0x03b0  */
    static zznp zzl(Class cls, zznj zznjVar, zznr zznrVar, zzmy zzmyVar, zzoi zzoiVar, zzls zzlsVar, zznh zznhVar) {
        int i;
        int iCharAt;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int[] iArr;
        int i7;
        int i8;
        char cCharAt;
        int i9;
        char cCharAt2;
        int i10;
        char cCharAt3;
        int i11;
        char cCharAt4;
        int i12;
        char cCharAt5;
        int i13;
        char cCharAt6;
        int i14;
        char cCharAt7;
        int i15;
        char cCharAt8;
        int i16;
        int i17;
        int i18;
        int iObjectFieldOffset;
        char c;
        int iObjectFieldOffset2;
        int i19;
        int i20;
        int i21;
        Field fieldZzm;
        char cCharAt9;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        Object obj;
        Field fieldZzm2;
        int i27;
        Object obj2;
        Field fieldZzm3;
        int i28;
        char cCharAt10;
        int i29;
        char cCharAt11;
        int i30;
        char cCharAt12;
        int i31;
        char cCharAt13;
        if (!(zznjVar instanceof zznw)) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zznjVar);
            throw null;
        }
        zznw zznwVar = (zznw) zznjVar;
        String strZzd = zznwVar.zzd();
        int length = strZzd.length();
        char cCharAt14 = strZzd.charAt(0);
        char c2 = CharacterCompat.MIN_HIGH_SURROGATE;
        if (cCharAt14 >= 55296) {
            int i32 = 1;
            while (true) {
                i = i32 + 1;
                if (strZzd.charAt(i32) < 55296) {
                    break;
                }
                i32 = i;
            }
        } else {
            i = 1;
        }
        int i33 = i + 1;
        int iCharAt2 = strZzd.charAt(i);
        if (iCharAt2 >= 55296) {
            int i34 = iCharAt2 & 8191;
            int i35 = 13;
            while (true) {
                i31 = i33 + 1;
                cCharAt13 = strZzd.charAt(i33);
                if (cCharAt13 < 55296) {
                    break;
                }
                i34 |= (cCharAt13 & 8191) << i35;
                i35 += 13;
                i33 = i31;
            }
            iCharAt2 = i34 | (cCharAt13 << i35);
            i33 = i31;
        }
        if (iCharAt2 == 0) {
            i3 = 0;
            i6 = 0;
            iCharAt = 0;
            i2 = 0;
            i4 = 0;
            i5 = 0;
            iArr = zza;
            i7 = 0;
        } else {
            int i36 = i33 + 1;
            int iCharAt3 = strZzd.charAt(i33);
            if (iCharAt3 >= 55296) {
                int i37 = iCharAt3 & 8191;
                int i38 = 13;
                while (true) {
                    i15 = i36 + 1;
                    cCharAt8 = strZzd.charAt(i36);
                    if (cCharAt8 < 55296) {
                        break;
                    }
                    i37 |= (cCharAt8 & 8191) << i38;
                    i38 += 13;
                    i36 = i15;
                }
                iCharAt3 = i37 | (cCharAt8 << i38);
                i36 = i15;
            }
            int i39 = i36 + 1;
            int iCharAt4 = strZzd.charAt(i36);
            if (iCharAt4 >= 55296) {
                int i40 = iCharAt4 & 8191;
                int i41 = 13;
                while (true) {
                    i14 = i39 + 1;
                    cCharAt7 = strZzd.charAt(i39);
                    if (cCharAt7 < 55296) {
                        break;
                    }
                    i40 |= (cCharAt7 & 8191) << i41;
                    i41 += 13;
                    i39 = i14;
                }
                iCharAt4 = i40 | (cCharAt7 << i41);
                i39 = i14;
            }
            int i42 = i39 + 1;
            int iCharAt5 = strZzd.charAt(i39);
            if (iCharAt5 >= 55296) {
                int i43 = iCharAt5 & 8191;
                int i44 = 13;
                while (true) {
                    i13 = i42 + 1;
                    cCharAt6 = strZzd.charAt(i42);
                    if (cCharAt6 < 55296) {
                        break;
                    }
                    i43 |= (cCharAt6 & 8191) << i44;
                    i44 += 13;
                    i42 = i13;
                }
                iCharAt5 = i43 | (cCharAt6 << i44);
                i42 = i13;
            }
            int i45 = i42 + 1;
            int iCharAt6 = strZzd.charAt(i42);
            if (iCharAt6 >= 55296) {
                int i46 = iCharAt6 & 8191;
                int i47 = 13;
                while (true) {
                    i12 = i45 + 1;
                    cCharAt5 = strZzd.charAt(i45);
                    if (cCharAt5 < 55296) {
                        break;
                    }
                    i46 |= (cCharAt5 & 8191) << i47;
                    i47 += 13;
                    i45 = i12;
                }
                iCharAt6 = i46 | (cCharAt5 << i47);
                i45 = i12;
            }
            int i48 = i45 + 1;
            iCharAt = strZzd.charAt(i45);
            if (iCharAt >= 55296) {
                int i49 = iCharAt & 8191;
                int i50 = 13;
                while (true) {
                    i11 = i48 + 1;
                    cCharAt4 = strZzd.charAt(i48);
                    if (cCharAt4 < 55296) {
                        break;
                    }
                    i49 |= (cCharAt4 & 8191) << i50;
                    i50 += 13;
                    i48 = i11;
                }
                iCharAt = i49 | (cCharAt4 << i50);
                i48 = i11;
            }
            int i51 = i48 + 1;
            int iCharAt7 = strZzd.charAt(i48);
            if (iCharAt7 >= 55296) {
                int i52 = iCharAt7 & 8191;
                int i53 = 13;
                while (true) {
                    i10 = i51 + 1;
                    cCharAt3 = strZzd.charAt(i51);
                    if (cCharAt3 < 55296) {
                        break;
                    }
                    i52 |= (cCharAt3 & 8191) << i53;
                    i53 += 13;
                    i51 = i10;
                }
                iCharAt7 = i52 | (cCharAt3 << i53);
                i51 = i10;
            }
            int i54 = i51 + 1;
            int iCharAt8 = strZzd.charAt(i51);
            if (iCharAt8 >= 55296) {
                int i55 = iCharAt8 & 8191;
                int i56 = 13;
                while (true) {
                    i9 = i54 + 1;
                    cCharAt2 = strZzd.charAt(i54);
                    if (cCharAt2 < 55296) {
                        break;
                    }
                    i55 |= (cCharAt2 & 8191) << i56;
                    i56 += 13;
                    i54 = i9;
                }
                iCharAt8 = i55 | (cCharAt2 << i56);
                i54 = i9;
            }
            int i57 = i54 + 1;
            int iCharAt9 = strZzd.charAt(i54);
            if (iCharAt9 >= 55296) {
                int i58 = iCharAt9 & 8191;
                int i59 = 13;
                while (true) {
                    i8 = i57 + 1;
                    cCharAt = strZzd.charAt(i57);
                    if (cCharAt < 55296) {
                        break;
                    }
                    i58 |= (cCharAt & 8191) << i59;
                    i59 += 13;
                    i57 = i8;
                }
                iCharAt9 = i58 | (cCharAt << i59);
                i57 = i8;
            }
            int i60 = iCharAt3 + iCharAt3 + iCharAt4;
            int[] iArr2 = new int[iCharAt9 + iCharAt7 + iCharAt8];
            int i61 = iCharAt7;
            i2 = iCharAt5;
            i3 = i61;
            i4 = iCharAt6;
            i5 = iCharAt9;
            i6 = i60;
            iArr = iArr2;
            i7 = iCharAt3;
            i33 = i57;
        }
        Unsafe unsafe = zzb;
        Object[] objArrZze = zznwVar.zze();
        Class<?> cls2 = zznwVar.zzb().getClass();
        int i62 = i5 + i3;
        int i63 = iCharAt + iCharAt;
        int[] iArr3 = new int[iCharAt * 3];
        Object[] objArr = new Object[i63];
        int i64 = i5;
        int i65 = i62;
        int i66 = 0;
        int i67 = 0;
        while (i33 < length) {
            int i68 = i33 + 1;
            int iCharAt10 = strZzd.charAt(i33);
            if (iCharAt10 >= c2) {
                int i69 = iCharAt10 & 8191;
                int i70 = i68;
                int i71 = 13;
                while (true) {
                    i30 = i70 + 1;
                    cCharAt12 = strZzd.charAt(i70);
                    if (cCharAt12 < c2) {
                        break;
                    }
                    i69 |= (cCharAt12 & 8191) << i71;
                    i71 += 13;
                    i70 = i30;
                }
                iCharAt10 = i69 | (cCharAt12 << i71);
                i16 = i30;
            } else {
                i16 = i68;
            }
            int i72 = i16 + 1;
            int iCharAt11 = strZzd.charAt(i16);
            if (iCharAt11 >= c2) {
                int i73 = iCharAt11 & 8191;
                int i74 = i72;
                int i75 = 13;
                while (true) {
                    i29 = i74 + 1;
                    cCharAt11 = strZzd.charAt(i74);
                    if (cCharAt11 < c2) {
                        break;
                    }
                    i73 |= (cCharAt11 & 8191) << i75;
                    i75 += 13;
                    i74 = i29;
                }
                iCharAt11 = i73 | (cCharAt11 << i75);
                i17 = i29;
            } else {
                i17 = i72;
            }
            if ((iCharAt11 & 1024) != 0) {
                iArr[i66] = i67;
                i66++;
            }
            int i76 = iCharAt11 & 255;
            zznw zznwVar2 = zznwVar;
            int i77 = iCharAt11 & 2048;
            if (i76 >= 51) {
                int i78 = i17 + 1;
                int iCharAt12 = strZzd.charAt(i17);
                char c3 = CharacterCompat.MIN_HIGH_SURROGATE;
                if (iCharAt12 >= 55296) {
                    int i79 = iCharAt12 & 8191;
                    int i80 = i78;
                    int i81 = 13;
                    while (true) {
                        i28 = i80 + 1;
                        cCharAt10 = strZzd.charAt(i80);
                        if (cCharAt10 < c3) {
                            break;
                        }
                        i79 |= (cCharAt10 & 8191) << i81;
                        i81 += 13;
                        i80 = i28;
                        c3 = CharacterCompat.MIN_HIGH_SURROGATE;
                    }
                    iCharAt12 = i79 | (cCharAt10 << i81);
                    i23 = i28;
                } else {
                    i23 = i78;
                }
                int i82 = i23;
                int i83 = i76 - 51;
                if (i83 == 9 || i83 == 17) {
                    i24 = i6 + 1;
                    int i84 = i67 / 3;
                    objArr[i84 + i84 + 1] = objArrZze[i6];
                } else {
                    if (i83 != 12) {
                        i25 = i77;
                    } else if (zznwVar2.zzc() == 1 || i77 != 0) {
                        i24 = i6 + 1;
                        int i85 = i67 / 3;
                        objArr[i85 + i85 + 1] = objArrZze[i6];
                    } else {
                        i25 = 0;
                    }
                    i26 = iCharAt12 + iCharAt12;
                    obj = objArrZze[i26];
                    int i86 = i25;
                    if (obj instanceof Field) {
                        fieldZzm2 = (Field) obj;
                    } else {
                        fieldZzm2 = zzm(cls2, (String) obj);
                        objArrZze[i26] = fieldZzm2;
                    }
                    int i87 = i7;
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzm2);
                    i27 = i26 + 1;
                    obj2 = objArrZze[i27];
                    i18 = i87;
                    if (obj2 instanceof Field) {
                        fieldZzm3 = (Field) obj2;
                    } else {
                        fieldZzm3 = zzm(cls2, (String) obj2);
                        objArrZze[i27] = fieldZzm3;
                    }
                    iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzm3);
                    strZzd = strZzd;
                    i20 = i86;
                    i17 = i82;
                    i19 = 0;
                    c = CharacterCompat.MIN_HIGH_SURROGATE;
                }
                i6 = i24;
                i25 = i77;
                i26 = iCharAt12 + iCharAt12;
                obj = objArrZze[i26];
                int i88 = i25;
                if (obj instanceof Field) {
                    fieldZzm2 = (Field) obj;
                } else {
                    fieldZzm2 = zzm(cls2, (String) obj);
                    objArrZze[i26] = fieldZzm2;
                }
                int i89 = i7;
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzm2);
                i27 = i26 + 1;
                obj2 = objArrZze[i27];
                i18 = i89;
                if (obj2 instanceof Field) {
                    fieldZzm3 = (Field) obj2;
                } else {
                    fieldZzm3 = zzm(cls2, (String) obj2);
                    objArrZze[i27] = fieldZzm3;
                }
                iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzm3);
                strZzd = strZzd;
                i20 = i88;
                i17 = i82;
                i19 = 0;
                c = CharacterCompat.MIN_HIGH_SURROGATE;
            } else {
                i18 = i7;
                int i90 = i6 + 1;
                Field fieldZzm4 = zzm(cls2, (String) objArrZze[i6]);
                if (i76 == 9 || i76 == 17) {
                    int i91 = i67 / 3;
                    objArr[i91 + i91 + 1] = fieldZzm4.getType();
                } else {
                    if (i76 != 27) {
                        if (i76 == 49) {
                            i6 += 2;
                            i22 = 1;
                        } else if (i76 == 12 || i76 == 30 || i76 == 44) {
                            if (zznwVar2.zzc() == 1 || i77 != 0) {
                                i6 += 2;
                                int i92 = i67 / 3;
                                objArr[i92 + i92 + 1] = objArrZze[i90];
                            } else {
                                i6 = i90;
                                i77 = 0;
                            }
                        } else if (i76 == 50) {
                            int i93 = i6 + 2;
                            int i94 = i64 + 1;
                            iArr[i64] = i67;
                            int i95 = i67 / 3;
                            int i96 = i95 + i95;
                            objArr[i96] = objArrZze[i90];
                            if (i77 != 0) {
                                objArr[i96 + 1] = objArrZze[i93];
                                i6 += 3;
                                i64 = i94;
                            } else {
                                i6 = i93;
                                i64 = i94;
                                i77 = 0;
                            }
                        }
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzm4);
                        if ((iCharAt11 & 4096) != 0 || i76 > 17) {
                            c = CharacterCompat.MIN_HIGH_SURROGATE;
                            iObjectFieldOffset2 = 1048575;
                            i19 = 0;
                        } else {
                            int i97 = i17 + 1;
                            int iCharAt13 = strZzd.charAt(i17);
                            if (iCharAt13 >= 55296) {
                                int i98 = iCharAt13 & 8191;
                                int i99 = 13;
                                while (true) {
                                    i21 = i97 + 1;
                                    cCharAt9 = strZzd.charAt(i97);
                                    if (cCharAt9 < 55296) {
                                        break;
                                    }
                                    i98 |= (cCharAt9 & 8191) << i99;
                                    i99 += 13;
                                    i97 = i21;
                                }
                                iCharAt13 = i98 | (cCharAt9 << i99);
                            } else {
                                i21 = i97;
                            }
                            int i100 = i18 + i18 + (iCharAt13 / 32);
                            Object obj3 = objArrZze[i100];
                            if (obj3 instanceof Field) {
                                fieldZzm = (Field) obj3;
                            } else {
                                fieldZzm = zzm(cls2, (String) obj3);
                                objArrZze[i100] = fieldZzm;
                            }
                            int i101 = iCharAt13;
                            int iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldZzm);
                            i19 = i101 % 32;
                            i17 = i21;
                            c = CharacterCompat.MIN_HIGH_SURROGATE;
                            iObjectFieldOffset2 = iObjectFieldOffset3;
                        }
                        if (i76 >= 18 && i76 <= 49) {
                            iArr[i65] = iObjectFieldOffset;
                            i65++;
                        }
                        i20 = i77;
                    } else {
                        i22 = 1;
                        i6 += 2;
                    }
                    int i102 = i67 / 3;
                    objArr[i102 + i102 + i22] = objArrZze[i90];
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzm4);
                    if ((iCharAt11 & 4096) != 0) {
                        c = CharacterCompat.MIN_HIGH_SURROGATE;
                        iObjectFieldOffset2 = 1048575;
                        i19 = 0;
                    } else {
                        c = CharacterCompat.MIN_HIGH_SURROGATE;
                        iObjectFieldOffset2 = 1048575;
                        i19 = 0;
                    }
                    if (i76 >= 18) {
                        iArr[i65] = iObjectFieldOffset;
                        i65++;
                    }
                    i20 = i77;
                }
                i6 = i90;
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzm4);
                if ((iCharAt11 & 4096) != 0) {
                    c = CharacterCompat.MIN_HIGH_SURROGATE;
                    iObjectFieldOffset2 = 1048575;
                    i19 = 0;
                } else {
                    c = CharacterCompat.MIN_HIGH_SURROGATE;
                    iObjectFieldOffset2 = 1048575;
                    i19 = 0;
                }
                if (i76 >= 18) {
                    iArr[i65] = iObjectFieldOffset;
                    i65++;
                }
                i20 = i77;
            }
            int i103 = i67 + 1;
            iArr3[i67] = iCharAt10;
            int i104 = i67 + 2;
            iArr3[i103] = ((iCharAt11 & 512) != 0 ? 536870912 : 0) | ((iCharAt11 & 256) != 0 ? 268435456 : 0) | (i20 != 0 ? Integer.MIN_VALUE : 0) | (i76 << 20) | iObjectFieldOffset;
            i67 += 3;
            iArr3[i104] = (i19 << 20) | iObjectFieldOffset2;
            i33 = i17;
            strZzd = strZzd;
            c2 = c;
            zznwVar = zznwVar2;
            length = length;
            i7 = i18;
        }
        return new zznp(iArr3, objArr, i2, i4, zznwVar.zzb(), false, iArr, i5, i62, zznrVar, zzmyVar, zzoiVar, zzlsVar, zznhVar);
    }

    private static Field zzm(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException e) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String string = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 11 + name.length() + 29 + String.valueOf(string).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(string);
            throw new RuntimeException(sb.toString(), e);
        }
    }

    private final void zzn(Object obj, Object obj2, int i) {
        if (zzJ(obj2, i)) {
            int iZzx = zzx(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzx;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                int i2 = this.zzc[i];
                String string = obj2.toString();
                StringBuilder sb = new StringBuilder(String.valueOf(i2).length() + 38 + string.length());
                sb.append("Source subfield ");
                sb.append(i2);
                sb.append(" is present but null: ");
                sb.append(string);
                throw new IllegalStateException(sb.toString());
            }
            zznx zznxVarZzp = zzp(i);
            if (!zzJ(obj, i)) {
                if (zzA(object)) {
                    Object objZza = zznxVarZzp.zza();
                    zznxVarZzp.zzd(objZza, object);
                    unsafe.putObject(obj, j, objZza);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzK(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzA(object2)) {
                Object objZza2 = zznxVarZzp.zza();
                zznxVarZzp.zzd(objZza2, object2);
                unsafe.putObject(obj, j, objZza2);
                object2 = objZza2;
            }
            zznxVarZzp.zzd(object2, object);
        }
    }

    private final void zzo(Object obj, Object obj2, int i) {
        int[] iArr = this.zzc;
        int i2 = iArr[i];
        if (zzL(obj2, i2, i)) {
            int iZzx = zzx(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzx;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                int i3 = iArr[i];
                String string = obj2.toString();
                StringBuilder sb = new StringBuilder(String.valueOf(i3).length() + 38 + string.length());
                sb.append("Source subfield ");
                sb.append(i3);
                sb.append(" is present but null: ");
                sb.append(string);
                throw new IllegalStateException(sb.toString());
            }
            zznx zznxVarZzp = zzp(i);
            if (!zzL(obj, i2, i)) {
                if (zzA(object)) {
                    Object objZza = zznxVarZzp.zza();
                    zznxVarZzp.zzd(objZza, object);
                    unsafe.putObject(obj, j, objZza);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzM(obj, i2, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzA(object2)) {
                Object objZza2 = zznxVarZzp.zza();
                zznxVarZzp.zzd(objZza2, object2);
                unsafe.putObject(obj, j, objZza2);
                object2 = objZza2;
            }
            zznxVarZzp.zzd(object2, object);
        }
    }

    private final zznx zzp(int i) {
        Object[] objArr = this.zzd;
        int i2 = i / 3;
        int i3 = i2 + i2;
        zznx zznxVar = (zznx) objArr[i3];
        if (zznxVar != null) {
            return zznxVar;
        }
        zznx zznxVarZzb = zznu.zza().zzb((Class) objArr[i3 + 1]);
        objArr[i3] = zznxVarZzb;
        return zznxVarZzb;
    }

    private final Object zzq(int i) {
        int i2 = i / 3;
        return this.zzd[i2 + i2];
    }

    private final zzmk zzr(int i) {
        int i2 = i / 3;
        return (zzmk) this.zzd[i2 + i2 + 1];
    }

    private final Object zzs(Object obj, int i) {
        zznx zznxVarZzp = zzp(i);
        int iZzx = zzx(i) & 1048575;
        if (!zzJ(obj, i)) {
            return zznxVarZzp.zza();
        }
        Object object = zzb.getObject(obj, iZzx);
        if (zzA(object)) {
            return object;
        }
        Object objZza = zznxVarZzp.zza();
        if (object != null) {
            zznxVarZzp.zzd(objZza, object);
        }
        return objZza;
    }

    private final void zzt(Object obj, int i, Object obj2) {
        zzb.putObject(obj, zzx(i) & 1048575, obj2);
        zzK(obj, i);
    }

    private final Object zzu(Object obj, int i, int i2) {
        zznx zznxVarZzp = zzp(i2);
        if (!zzL(obj, i, i2)) {
            return zznxVarZzp.zza();
        }
        Object object = zzb.getObject(obj, zzx(i2) & 1048575);
        if (zzA(object)) {
            return object;
        }
        Object objZza = zznxVarZzp.zza();
        if (object != null) {
            zznxVarZzp.zzd(objZza, object);
        }
        return objZza;
    }

    private final void zzv(Object obj, int i, int i2, Object obj2) {
        zzb.putObject(obj, zzx(i2) & 1048575, obj2);
        zzM(obj, i, i2);
    }

    private static boolean zzw(Object obj, int i, zznx zznxVar) {
        return zznxVar.zzk(zzop.zzn(obj, i & 1048575));
    }

    private final int zzx(int i) {
        return this.zzc[i + 1];
    }

    private final int zzy(int i) {
        return this.zzc[i + 2];
    }

    private static int zzz(int i) {
        return (i >>> 20) & 255;
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final Object zza() {
        return ((zzmf) this.zzg).zzch();
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final boolean zzb(Object obj, Object obj2) {
        boolean zZzB;
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzx = zzx(i);
            long j = iZzx & 1048575;
            switch (zzz(iZzx)) {
                case 0:
                    if (!zzH(obj, obj2, i) || Double.doubleToLongBits(zzop.zzl(obj, j)) != Double.doubleToLongBits(zzop.zzl(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 1:
                    if (!zzH(obj, obj2, i) || Float.floatToIntBits(zzop.zzj(obj, j)) != Float.floatToIntBits(zzop.zzj(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 2:
                    if (!zzH(obj, obj2, i) || zzop.zzf(obj, j) != zzop.zzf(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 3:
                    if (!zzH(obj, obj2, i) || zzop.zzf(obj, j) != zzop.zzf(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 4:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 5:
                    if (!zzH(obj, obj2, i) || zzop.zzf(obj, j) != zzop.zzf(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 6:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 7:
                    if (!zzH(obj, obj2, i) || zzop.zzh(obj, j) != zzop.zzh(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 8:
                    if (!zzH(obj, obj2, i) || !zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 9:
                    if (!zzH(obj, obj2, i) || !zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 10:
                    if (!zzH(obj, obj2, i) || !zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 11:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 12:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 13:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 14:
                    if (!zzH(obj, obj2, i) || zzop.zzf(obj, j) != zzop.zzf(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 15:
                    if (!zzH(obj, obj2, i) || zzop.zzd(obj, j) != zzop.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 16:
                    if (!zzH(obj, obj2, i) || zzop.zzf(obj, j) != zzop.zzf(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 17:
                    if (!zzH(obj, obj2, i) || !zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
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
                    zZzB = zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j));
                    break;
                case 50:
                    zZzB = zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j));
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
                    long jZzy = zzy(i) & 1048575;
                    if (zzop.zzd(obj, jZzy) != zzop.zzd(obj2, jZzy) || !zznz.zzB(zzop.zzn(obj, j), zzop.zzn(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                default:
                    continue;
                    break;
            }
            if (!zZzB) {
                return false;
            }
        }
        if (!((zzmf) obj).zzc.equals(((zzmf) obj2).zzc)) {
            return false;
        }
        if (!this.zzh) {
            return true;
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final int zzc(Object obj) {
        int i;
        long jDoubleToLongBits;
        int iFloatToIntBits;
        int i2;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            int[] iArr = this.zzc;
            if (i3 >= iArr.length) {
                int iHashCode = (i4 * 53) + ((zzmf) obj).zzc.hashCode();
                if (!this.zzh) {
                    return iHashCode;
                }
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
                throw null;
            }
            int iZzx = zzx(i3);
            int i5 = 1048575 & iZzx;
            int iZzz = zzz(iZzx);
            int i6 = iArr[i3];
            long j = i5;
            int iHashCode2 = 37;
            switch (iZzz) {
                case 0:
                    i = i4 * 53;
                    jDoubleToLongBits = Double.doubleToLongBits(zzop.zzl(obj, j));
                    byte[] bArr = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 1:
                    i = i4 * 53;
                    iFloatToIntBits = Float.floatToIntBits(zzop.zzj(obj, j));
                    i4 = i + iFloatToIntBits;
                    break;
                case 2:
                    i = i4 * 53;
                    jDoubleToLongBits = zzop.zzf(obj, j);
                    byte[] bArr2 = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 3:
                    i = i4 * 53;
                    jDoubleToLongBits = zzop.zzf(obj, j);
                    byte[] bArr3 = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 4:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 5:
                    i = i4 * 53;
                    jDoubleToLongBits = zzop.zzf(obj, j);
                    byte[] bArr4 = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 6:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 7:
                    i = i4 * 53;
                    iFloatToIntBits = zzmp.zzb(zzop.zzh(obj, j));
                    i4 = i + iFloatToIntBits;
                    break;
                case 8:
                    i = i4 * 53;
                    iFloatToIntBits = ((String) zzop.zzn(obj, j)).hashCode();
                    i4 = i + iFloatToIntBits;
                    break;
                case 9:
                    i2 = i4 * 53;
                    Object objZzn = zzop.zzn(obj, j);
                    if (objZzn != null) {
                        iHashCode2 = objZzn.hashCode();
                    }
                    i4 = i2 + iHashCode2;
                    break;
                case 10:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                    i4 = i + iFloatToIntBits;
                    break;
                case 11:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 12:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 13:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 14:
                    i = i4 * 53;
                    jDoubleToLongBits = zzop.zzf(obj, j);
                    byte[] bArr5 = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 15:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzd(obj, j);
                    i4 = i + iFloatToIntBits;
                    break;
                case 16:
                    i = i4 * 53;
                    jDoubleToLongBits = zzop.zzf(obj, j);
                    byte[] bArr6 = zzmp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i4 = i + iFloatToIntBits;
                    break;
                case 17:
                    i2 = i4 * 53;
                    Object objZzn2 = zzop.zzn(obj, j);
                    if (objZzn2 != null) {
                        iHashCode2 = objZzn2.hashCode();
                    }
                    i4 = i2 + iHashCode2;
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
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                    i4 = i + iFloatToIntBits;
                    break;
                case 50:
                    i = i4 * 53;
                    iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                    i4 = i + iFloatToIntBits;
                    break;
                case 51:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = Double.doubleToLongBits(zzC(obj, j));
                        byte[] bArr7 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 52:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = Float.floatToIntBits(zzD(obj, j));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 53:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = zzF(obj, j);
                        byte[] bArr8 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 54:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = zzF(obj, j);
                        byte[] bArr9 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 55:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 56:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = zzF(obj, j);
                        byte[] bArr10 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 57:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 58:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzmp.zzb(zzG(obj, j));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 59:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = ((String) zzop.zzn(obj, j)).hashCode();
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 60:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 61:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 62:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 63:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 64:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 65:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = zzF(obj, j);
                        byte[] bArr11 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 66:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzE(obj, j);
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 67:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        jDoubleToLongBits = zzF(obj, j);
                        byte[] bArr12 = zzmp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i4 = i + iFloatToIntBits;
                    }
                    break;
                case 68:
                    if (zzL(obj, i6, i3)) {
                        i = i4 * 53;
                        iFloatToIntBits = zzop.zzn(obj, j).hashCode();
                        i4 = i + iFloatToIntBits;
                    }
                    break;
            }
            i3 += 3;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final void zzd(Object obj, Object obj2) {
        zzB(obj);
        obj2.getClass();
        int i = 0;
        while (true) {
            int[] iArr = this.zzc;
            if (i >= iArr.length) {
                zznz.zzD(this.zzl, obj, obj2);
                if (this.zzh) {
                    zznz.zzC(this.zzm, obj, obj2);
                    return;
                }
                return;
            }
            int iZzx = zzx(i);
            int i2 = 1048575 & iZzx;
            int iZzz = zzz(iZzx);
            int i3 = iArr[i];
            long j = i2;
            switch (iZzz) {
                case 0:
                    if (zzJ(obj2, i)) {
                        zzop.zzm(obj, j, zzop.zzl(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 1:
                    if (zzJ(obj2, i)) {
                        zzop.zzk(obj, j, zzop.zzj(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 2:
                    if (zzJ(obj2, i)) {
                        zzop.zzg(obj, j, zzop.zzf(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 3:
                    if (zzJ(obj2, i)) {
                        zzop.zzg(obj, j, zzop.zzf(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 4:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 5:
                    if (zzJ(obj2, i)) {
                        zzop.zzg(obj, j, zzop.zzf(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 6:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 7:
                    if (zzJ(obj2, i)) {
                        zzop.zzi(obj, j, zzop.zzh(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 8:
                    if (zzJ(obj2, i)) {
                        zzop.zzo(obj, j, zzop.zzn(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 9:
                    zzn(obj, obj2, i);
                    break;
                case 10:
                    if (zzJ(obj2, i)) {
                        zzop.zzo(obj, j, zzop.zzn(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 11:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 12:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 13:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 14:
                    if (zzJ(obj2, i)) {
                        zzop.zzg(obj, j, zzop.zzf(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 15:
                    if (zzJ(obj2, i)) {
                        zzop.zze(obj, j, zzop.zzd(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 16:
                    if (zzJ(obj2, i)) {
                        zzop.zzg(obj, j, zzop.zzf(obj2, j));
                        zzK(obj, i);
                    }
                    break;
                case 17:
                    zzn(obj, obj2, i);
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
                    zzmo zzmoVarZzg = (zzmo) zzop.zzn(obj, j);
                    zzmo zzmoVar = (zzmo) zzop.zzn(obj2, j);
                    int size = zzmoVarZzg.size();
                    int size2 = zzmoVar.size();
                    if (size > 0 && size2 > 0) {
                        if (!zzmoVarZzg.zza()) {
                            zzmoVarZzg = zzmoVarZzg.zzg(size2 + size);
                        }
                        zzmoVarZzg.addAll(zzmoVar);
                    }
                    if (size > 0) {
                        zzmoVar = zzmoVarZzg;
                    }
                    zzop.zzo(obj, j, zzmoVar);
                    break;
                case 50:
                    int i4 = zznz.$r8$clinit;
                    zzop.zzo(obj, j, zznh.zza(zzop.zzn(obj, j), zzop.zzn(obj2, j)));
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
                    if (zzL(obj2, i3, i)) {
                        zzop.zzo(obj, j, zzop.zzn(obj2, j));
                        zzM(obj, i3, i);
                    }
                    break;
                case 60:
                    zzo(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (zzL(obj2, i3, i)) {
                        zzop.zzo(obj, j, zzop.zzn(obj2, j));
                        zzM(obj, i3, i);
                    }
                    break;
                case 68:
                    zzo(obj, obj2, i);
                    break;
            }
            i += 3;
        }
    }

    /* JADX WARN: Code duplicated, block: B:143:0x038e  */
    /* JADX WARN: Code duplicated, block: B:84:0x01df  */
    @Override // com.google.android.gms.internal.measurement.zznx
    public final int zze(Object obj) {
        int i;
        int iZzz;
        int iZzz2;
        int iZzA;
        int iZzz3;
        int iZzz4;
        int iZzz5;
        int iZzc;
        int iZzz6;
        int iZzz7;
        int iZzo;
        int size;
        int iZzp;
        int iZzz8;
        int iZzz9;
        int iZzz10;
        int iZzA2;
        int iZzx;
        int iZzz11;
        int iZzz12;
        int iZzG;
        int iZzz13;
        int iZzz14;
        int iZzz15;
        int iZzc2;
        int iZzz16;
        zznp zznpVar = this;
        Unsafe unsafe = zzb;
        int i2 = 0;
        int i3 = 0;
        int iZzz17 = 0;
        int i4 = 1048575;
        while (true) {
            int[] iArr = zznpVar.zzc;
            if (i2 >= iArr.length) {
                int iZzi = iZzz17 + ((zzmf) obj).zzc.zzi();
                if (!zznpVar.zzh) {
                    return iZzi;
                }
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
                throw null;
            }
            int iZzx2 = zznpVar.zzx(i2);
            int iZzz18 = zzz(iZzx2);
            int i5 = iArr[i2];
            int i6 = iArr[i2 + 2];
            int i7 = i6 & 1048575;
            if (iZzz18 <= 17) {
                if (i7 != i4) {
                    i3 = i7 == 1048575 ? 0 : unsafe.getInt(obj, i7);
                    i4 = i7;
                }
                i = 1 << (i6 >>> 20);
            } else {
                i = 0;
            }
            int i8 = iZzx2 & 1048575;
            if (iZzz18 >= zzlx.zzJ.zza()) {
                zzlx.zzW.zza();
            }
            long j = i8;
            switch (iZzz18) {
                case 0:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz17 += zzlm.zzz(i5 << 3) + 8;
                    }
                    break;
                case 1:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz = zzlm.zzz(i5 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 2:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        long j2 = unsafe.getLong(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzA(j2);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 3:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        long j3 = unsafe.getLong(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzA(j3);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 4:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        long j4 = unsafe.getInt(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzA(j4);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 5:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz3 = zzlm.zzz(i5 << 3);
                        iZzz4 = iZzz3 + 8;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 6:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz = zzlm.zzz(i5 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 7:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz4 = zzlm.zzz(i5 << 3) + 1;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 8:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        int i9 = i5 << 3;
                        Object object = unsafe.getObject(obj, j);
                        if (object instanceof zzlh) {
                            iZzz5 = zzlm.zzz(i9);
                            iZzc = ((zzlh) object).zzc();
                            iZzz6 = zzlm.zzz(iZzc);
                            iZzz4 = iZzz5 + iZzz6 + iZzc;
                            iZzz17 += iZzz4;
                        } else {
                            iZzz2 = zzlm.zzz(i9);
                            iZzA = zzlm.zzB((String) object);
                            iZzz4 = iZzz2 + iZzA;
                            iZzz17 += iZzz4;
                        }
                    }
                    zznpVar = this;
                    break;
                case 9:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz7 = zznz.zzz(i5, unsafe.getObject(obj, j), zznpVar.zzp(i2));
                        iZzz17 += iZzz7;
                    }
                    break;
                case 10:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        zzlh zzlhVar = (zzlh) unsafe.getObject(obj, j);
                        iZzz5 = zzlm.zzz(i5 << 3);
                        iZzc = zzlhVar.zzc();
                        iZzz6 = zzlm.zzz(iZzc);
                        iZzz4 = iZzz5 + iZzz6 + iZzc;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 11:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        int i10 = unsafe.getInt(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzz(i10);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 12:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        long j5 = unsafe.getInt(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzA(j5);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 13:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz = zzlm.zzz(i5 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 14:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz3 = zzlm.zzz(i5 << 3);
                        iZzz4 = iZzz3 + 8;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 15:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        int i11 = unsafe.getInt(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzz((i11 >> 31) ^ (i11 + i11));
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 16:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        long j6 = unsafe.getLong(obj, j);
                        iZzz2 = zzlm.zzz(i5 << 3);
                        iZzA = zzlm.zzA((j6 >> 63) ^ (j6 + j6));
                        iZzz4 = iZzz2 + iZzA;
                        iZzz17 += iZzz4;
                    }
                    zznpVar = this;
                    break;
                case 17:
                    if (zznpVar.zzI(obj, i2, i4, i3, i)) {
                        iZzz7 = zzlm.zzG(i5, (zznm) unsafe.getObject(obj, j), zznpVar.zzp(i2));
                        iZzz17 += iZzz7;
                    }
                    break;
                case 18:
                    iZzz7 = zznz.zzy(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 19:
                    iZzz7 = zznz.zzw(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 20:
                    List list = (List) unsafe.getObject(obj, j);
                    int i12 = zznz.$r8$clinit;
                    if (list.size() == 0) {
                        iZzo = 0;
                    } else {
                        iZzo = zznz.zzo(list) + (list.size() * zzlm.zzz(i5 << 3));
                    }
                    iZzz17 += iZzo;
                    break;
                case 21:
                    List list2 = (List) unsafe.getObject(obj, j);
                    int i13 = zznz.$r8$clinit;
                    size = list2.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzp(list2);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 22:
                    List list3 = (List) unsafe.getObject(obj, j);
                    int i14 = zznz.$r8$clinit;
                    size = list3.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzs(list3);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 23:
                    iZzz7 = zznz.zzy(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 24:
                    iZzz7 = zznz.zzw(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 25:
                    List list4 = (List) unsafe.getObject(obj, j);
                    int i15 = zznz.$r8$clinit;
                    int size2 = list4.size();
                    if (size2 == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzz7 = size2 * (zzlm.zzz(i5 << 3) + 1);
                    }
                    iZzz17 += iZzz7;
                    break;
                case 26:
                    List list5 = (List) unsafe.getObject(obj, j);
                    int i16 = zznz.$r8$clinit;
                    int size3 = list5.size();
                    if (size3 == 0) {
                        iZzo = 0;
                    } else {
                        iZzo = zzlm.zzz(i5 << 3) * size3;
                        if (list5 instanceof zzmx) {
                            zzmx zzmxVar = (zzmx) list5;
                            for (int i17 = 0; i17 < size3; i17++) {
                                Object objZzc = zzmxVar.zzc();
                                if (objZzc instanceof zzlh) {
                                    int iZzc3 = ((zzlh) objZzc).zzc();
                                    iZzo += zzlm.zzz(iZzc3) + iZzc3;
                                } else {
                                    iZzo += zzlm.zzB((String) objZzc);
                                }
                            }
                        } else {
                            for (int i18 = 0; i18 < size3; i18++) {
                                Object obj2 = list5.get(i18);
                                if (obj2 instanceof zzlh) {
                                    int iZzc4 = ((zzlh) obj2).zzc();
                                    iZzo += zzlm.zzz(iZzc4) + iZzc4;
                                } else {
                                    iZzo += zzlm.zzB((String) obj2);
                                }
                            }
                        }
                    }
                    iZzz17 += iZzo;
                    break;
                case 27:
                    List list6 = (List) unsafe.getObject(obj, j);
                    zznx zznxVarZzp = zznpVar.zzp(i2);
                    int i19 = zznz.$r8$clinit;
                    int size4 = list6.size();
                    if (size4 == 0) {
                        iZzz9 = 0;
                    } else {
                        iZzz9 = zzlm.zzz(i5 << 3) * size4;
                        for (int i20 = 0; i20 < size4; i20++) {
                            iZzz9 += zzlm.zzD((zznm) list6.get(i20), zznxVarZzp);
                        }
                    }
                    iZzz17 += iZzz9;
                    break;
                case 28:
                    List list7 = (List) unsafe.getObject(obj, j);
                    int i21 = zznz.$r8$clinit;
                    int size5 = list7.size();
                    if (size5 == 0) {
                        iZzz10 = 0;
                    } else {
                        iZzz10 = size5 * zzlm.zzz(i5 << 3);
                        for (int i22 = 0; i22 < list7.size(); i22++) {
                            int iZzc5 = ((zzlh) list7.get(i22)).zzc();
                            iZzz10 += zzlm.zzz(iZzc5) + iZzc5;
                        }
                    }
                    iZzz17 += iZzz10;
                    break;
                case 29:
                    List list8 = (List) unsafe.getObject(obj, j);
                    int i23 = zznz.$r8$clinit;
                    size = list8.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzt(list8);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 30:
                    List list9 = (List) unsafe.getObject(obj, j);
                    int i24 = zznz.$r8$clinit;
                    size = list9.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzr(list9);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 31:
                    iZzz7 = zznz.zzw(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 32:
                    iZzz7 = zznz.zzy(i5, (List) unsafe.getObject(obj, j), false);
                    iZzz17 += iZzz7;
                    break;
                case 33:
                    List list10 = (List) unsafe.getObject(obj, j);
                    int i25 = zznz.$r8$clinit;
                    size = list10.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzu(list10);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 34:
                    List list11 = (List) unsafe.getObject(obj, j);
                    int i26 = zznz.$r8$clinit;
                    size = list11.size();
                    if (size == 0) {
                        iZzz7 = 0;
                    } else {
                        iZzp = zznz.zzq(list11);
                        iZzz8 = zzlm.zzz(i5 << 3);
                        iZzA2 = size * iZzz8;
                        iZzz7 = iZzp + iZzA2;
                    }
                    iZzz17 += iZzz7;
                    break;
                case 35:
                    iZzx = zznz.zzx((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 36:
                    iZzx = zznz.zzv((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 37:
                    iZzx = zznz.zzo((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 38:
                    iZzx = zznz.zzp((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 39:
                    iZzx = zznz.zzs((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 40:
                    iZzx = zznz.zzx((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 41:
                    iZzx = zznz.zzv((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 42:
                    List list12 = (List) unsafe.getObject(obj, j);
                    int i27 = zznz.$r8$clinit;
                    iZzx = list12.size();
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 43:
                    iZzx = zznz.zzt((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 44:
                    iZzx = zznz.zzr((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 45:
                    iZzx = zznz.zzv((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 46:
                    iZzx = zznz.zzx((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 47:
                    iZzx = zznz.zzu((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 48:
                    iZzx = zznz.zzq((List) unsafe.getObject(obj, j));
                    if (iZzx > 0) {
                        iZzz11 = zzlm.zzz(i5 << 3);
                        iZzz12 = zzlm.zzz(iZzx);
                        iZzz10 = iZzz11 + iZzz12 + iZzx;
                        iZzz17 += iZzz10;
                    }
                    break;
                case 49:
                    List list13 = (List) unsafe.getObject(obj, j);
                    zznx zznxVarZzp2 = zznpVar.zzp(i2);
                    int i28 = zznz.$r8$clinit;
                    int size6 = list13.size();
                    if (size6 == 0) {
                        iZzG = 0;
                    } else {
                        iZzG = 0;
                        for (int i29 = 0; i29 < size6; i29++) {
                            iZzG += zzlm.zzG(i5, (zznm) list13.get(i29), zznxVarZzp2);
                        }
                    }
                    iZzz17 += iZzG;
                    break;
                case 50:
                    zzng zzngVar = (zzng) unsafe.getObject(obj, j);
                    zznf zznfVar = (zznf) zznpVar.zzq(i2);
                    if (zzngVar.isEmpty()) {
                        iZzo = 0;
                    } else {
                        iZzo = 0;
                        for (Map.Entry entry : zzngVar.entrySet()) {
                            iZzo += zznfVar.zzd(i5, entry.getKey(), entry.getValue());
                        }
                    }
                    iZzz17 += iZzo;
                    break;
                case 51:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz13 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz13 + 8;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 52:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz14 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz14 + 4;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 53:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        long jZzF = zzF(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzA(jZzF);
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 54:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        long jZzF2 = zzF(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzA(jZzF2);
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 55:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        long jZzE = zzE(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzA(jZzE);
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 56:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz13 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz13 + 8;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 57:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz14 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz14 + 4;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 58:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz7 = zzlm.zzz(i5 << 3) + 1;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 59:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        int i30 = i5 << 3;
                        Object object2 = unsafe.getObject(obj, j);
                        if (object2 instanceof zzlh) {
                            iZzz15 = zzlm.zzz(i30);
                            iZzc2 = ((zzlh) object2).zzc();
                            iZzz16 = zzlm.zzz(iZzc2);
                            iZzz7 = iZzz15 + iZzz16 + iZzc2;
                            iZzz17 += iZzz7;
                        } else {
                            iZzp = zzlm.zzz(i30);
                            iZzA2 = zzlm.zzB((String) object2);
                            iZzz7 = iZzp + iZzA2;
                            iZzz17 += iZzz7;
                        }
                    }
                    break;
                case 60:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz7 = zznz.zzz(i5, unsafe.getObject(obj, j), zznpVar.zzp(i2));
                        iZzz17 += iZzz7;
                    }
                    break;
                case 61:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        zzlh zzlhVar2 = (zzlh) unsafe.getObject(obj, j);
                        iZzz15 = zzlm.zzz(i5 << 3);
                        iZzc2 = zzlhVar2.zzc();
                        iZzz16 = zzlm.zzz(iZzc2);
                        iZzz7 = iZzz15 + iZzz16 + iZzc2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 62:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        int iZzE = zzE(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzz(iZzE);
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 63:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        long jZzE2 = zzE(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzA(jZzE2);
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 64:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz14 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz14 + 4;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 65:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz13 = zzlm.zzz(i5 << 3);
                        iZzz7 = iZzz13 + 8;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 66:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        int iZzE2 = zzE(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzz((iZzE2 >> 31) ^ (iZzE2 + iZzE2));
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 67:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        long jZzF3 = zzF(obj, j);
                        iZzp = zzlm.zzz(i5 << 3);
                        iZzA2 = zzlm.zzA((jZzF3 >> 63) ^ (jZzF3 + jZzF3));
                        iZzz7 = iZzp + iZzA2;
                        iZzz17 += iZzz7;
                    }
                    break;
                case 68:
                    if (zznpVar.zzL(obj, i5, i2)) {
                        iZzz7 = zzlm.zzG(i5, (zznm) unsafe.getObject(obj, j), zznpVar.zzp(i2));
                        iZzz17 += iZzz7;
                    }
                    break;
            }
            i2 += 3;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final void zzf(Object obj, zzov zzovVar) {
        int i;
        zznp zznpVar = this;
        if (zznpVar.zzh) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
            throw null;
        }
        int[] iArr = zznpVar.zzc;
        Unsafe unsafe = zzb;
        int i2 = 1048575;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i4 < iArr.length) {
            int iZzx = zznpVar.zzx(i4);
            int iZzz = zzz(iZzx);
            int i6 = iArr[i4];
            if (iZzz <= 17) {
                int i7 = iArr[i4 + 2];
                int i8 = i7 & i2;
                if (i8 != i3) {
                    i5 = i8 == i2 ? 0 : unsafe.getInt(obj, i8);
                    i3 = i8;
                }
                i = 1 << (i7 >>> 20);
            } else {
                i = 0;
            }
            long j = iZzx & i2;
            switch (iZzz) {
                case 0:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzf(i6, zzop.zzl(obj, j));
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 1:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zze(i6, zzop.zzj(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 2:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzc(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 3:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzh(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 4:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzi(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 5:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzj(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 6:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzk(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 7:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzl(i6, zzop.zzh(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 8:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzP(i6, unsafe.getObject(obj, j), zzovVar);
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 9:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzr(i6, unsafe.getObject(obj, j), zznpVar.zzp(i4));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 10:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzn(i6, (zzlh) unsafe.getObject(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 11:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzo(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 12:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzg(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 13:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzb(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 14:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzd(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 15:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzp(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 16:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzq(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 17:
                    if (zznpVar.zzI(obj, i4, i3, i5, i)) {
                        zzovVar.zzs(i6, unsafe.getObject(obj, j), zznpVar.zzp(i4));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 18:
                    zznz.zza(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 19:
                    zznz.zzb(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 20:
                    zznz.zzc(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 21:
                    zznz.zzd(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 22:
                    zznz.zzh(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 23:
                    zznz.zzf(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 24:
                    zznz.zzk(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 25:
                    zznz.zzn(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 26:
                    int i9 = iArr[i4];
                    List list = (List) unsafe.getObject(obj, j);
                    int i10 = zznz.$r8$clinit;
                    if (list != null && !list.isEmpty()) {
                        zzovVar.zzF(i9, list);
                    }
                    break;
                case 27:
                    int i11 = iArr[i4];
                    List list2 = (List) unsafe.getObject(obj, j);
                    zznx zznxVarZzp = zznpVar.zzp(i4);
                    int i12 = zznz.$r8$clinit;
                    if (list2 != null && !list2.isEmpty()) {
                        for (int i13 = 0; i13 < list2.size(); i13++) {
                            ((zzln) zzovVar).zzr(i11, list2.get(i13), zznxVarZzp);
                        }
                    }
                    break;
                case 28:
                    int i14 = iArr[i4];
                    List list3 = (List) unsafe.getObject(obj, j);
                    int i15 = zznz.$r8$clinit;
                    if (list3 != null && !list3.isEmpty()) {
                        zzovVar.zzG(i14, list3);
                    }
                    break;
                case 29:
                    zznz.zzi(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 30:
                    zznz.zzm(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 31:
                    zznz.zzl(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 32:
                    zznz.zzg(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 33:
                    zznz.zzj(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 34:
                    zznz.zze(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zznpVar = this;
                    break;
                case 35:
                    zznz.zza(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 36:
                    zznz.zzb(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 37:
                    zznz.zzc(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 38:
                    zznz.zzd(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 39:
                    zznz.zzh(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 40:
                    zznz.zzf(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 41:
                    zznz.zzk(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 42:
                    zznz.zzn(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 43:
                    zznz.zzi(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 44:
                    zznz.zzm(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 45:
                    zznz.zzl(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 46:
                    zznz.zzg(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 47:
                    zznz.zzj(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 48:
                    zznz.zze(iArr[i4], (List) unsafe.getObject(obj, j), zzovVar, true);
                    break;
                case 49:
                    int i16 = iArr[i4];
                    List list4 = (List) unsafe.getObject(obj, j);
                    zznx zznxVarZzp2 = zznpVar.zzp(i4);
                    int i17 = zznz.$r8$clinit;
                    if (list4 != null && !list4.isEmpty()) {
                        for (int i18 = 0; i18 < list4.size(); i18++) {
                            ((zzln) zzovVar).zzs(i16, list4.get(i18), zznxVarZzp2);
                        }
                    }
                    break;
                case 50:
                    Object object = unsafe.getObject(obj, j);
                    if (object != null) {
                        zzovVar.zzM(i6, ((zznf) zznpVar.zzq(i4)).zze(), (zzng) object);
                    }
                    break;
                case 51:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzf(i6, zzC(obj, j));
                    }
                    break;
                case 52:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zze(i6, zzD(obj, j));
                    }
                    break;
                case 53:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzc(i6, zzF(obj, j));
                    }
                    break;
                case 54:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzh(i6, zzF(obj, j));
                    }
                    break;
                case 55:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzi(i6, zzE(obj, j));
                    }
                    break;
                case 56:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzj(i6, zzF(obj, j));
                    }
                    break;
                case 57:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzk(i6, zzE(obj, j));
                    }
                    break;
                case 58:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzl(i6, zzG(obj, j));
                    }
                    break;
                case 59:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzP(i6, unsafe.getObject(obj, j), zzovVar);
                    }
                    break;
                case 60:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzr(i6, unsafe.getObject(obj, j), zznpVar.zzp(i4));
                    }
                    break;
                case 61:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzn(i6, (zzlh) unsafe.getObject(obj, j));
                    }
                    break;
                case 62:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzo(i6, zzE(obj, j));
                    }
                    break;
                case 63:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzg(i6, zzE(obj, j));
                    }
                    break;
                case 64:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzb(i6, zzE(obj, j));
                    }
                    break;
                case 65:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzd(i6, zzF(obj, j));
                    }
                    break;
                case 66:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzp(i6, zzE(obj, j));
                    }
                    break;
                case 67:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzq(i6, zzF(obj, j));
                    }
                    break;
                case 68:
                    if (zznpVar.zzL(obj, i6, i4)) {
                        zzovVar.zzs(i6, unsafe.getObject(obj, j), zznpVar.zzp(i4));
                    }
                    break;
            }
            i4 += 3;
            i2 = 1048575;
            zznpVar = this;
        }
        ((zzmf) obj).zzc.zzg(zzovVar);
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 39661. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    final int zzh(java.lang.Object r36, byte[] r37, int r38, int r39, int r40, com.google.android.gms.internal.measurement.zzkw r41) {
        /*
            Method dump skipped, instruction units count: 3966
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zznp.zzh(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.measurement.zzkw):int");
    }

    @Override // com.google.android.gms.internal.measurement.zznx
    public final void zzi(Object obj, byte[] bArr, int i, int i2, zzkw zzkwVar) {
        zzh(obj, bArr, i, i2, 0, zzkwVar);
    }

    /* JADX WARN: Code duplicated, block: B:26:0x006f  */
    /* JADX WARN: Code duplicated, block: B:28:0x0075  */
    /* JADX WARN: Code duplicated, block: B:41:0x0082 A[SYNTHETIC] */
    @Override // com.google.android.gms.internal.measurement.zznx
    public final void zzj(Object obj) {
        if (zzA(obj)) {
            if (obj instanceof zzmf) {
                zzmf zzmfVar = (zzmf) obj;
                zzmfVar.zzcm(Integer.MAX_VALUE);
                zzmfVar.zza = 0;
                zzmfVar.zzcg();
            }
            int[] iArr = this.zzc;
            for (int i = 0; i < iArr.length; i += 3) {
                int iZzx = zzx(i);
                int i2 = 1048575 & iZzx;
                int iZzz = zzz(iZzx);
                long j = i2;
                if (iZzz != 9) {
                    if (iZzz != 60 && iZzz != 68) {
                        switch (iZzz) {
                            case 17:
                                if (zzJ(obj, i)) {
                                    zzp(i).zzj(zzb.getObject(obj, j));
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
                                ((zzmo) zzop.zzn(obj, j)).zzb();
                                break;
                            case 50:
                                Unsafe unsafe = zzb;
                                Object object = unsafe.getObject(obj, j);
                                if (object != null) {
                                    ((zzng) object).zzd();
                                    unsafe.putObject(obj, j, object);
                                }
                                break;
                        }
                    } else if (zzL(obj, iArr[i], i)) {
                        zzp(i).zzj(zzb.getObject(obj, j));
                    }
                } else if (zzJ(obj, i)) {
                    zzp(i).zzj(zzb.getObject(obj, j));
                }
            }
            this.zzl.zzb(obj);
            if (this.zzh) {
                this.zzm.zza(obj);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:51:0x00c3  */
    /* JADX WARN: Code duplicated, block: B:53:0x00d2  */
    /* JADX WARN: Code duplicated, block: B:56:0x00dd  */
    /* JADX WARN: Code duplicated, block: B:59:0x00e8 A[LOOP:2: B:54:0x00d7->B:59:0x00e8, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:75:0x00e7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:82:0x00fd A[SYNTHETIC] */
    @Override // com.google.android.gms.internal.measurement.zznx
    public final boolean zzk(Object obj) {
        int i;
        int i2;
        int i3;
        List list;
        zznx zznxVarZzp;
        int i4;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1048575;
        while (true) {
            zznx zznxVarZzb = null;
            if (i6 >= this.zzj) {
                if (!this.zzh) {
                    return true;
                }
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
                throw null;
            }
            int[] iArr = this.zzi;
            int[] iArr2 = this.zzc;
            int i8 = iArr[i6];
            int i9 = iArr2[i8];
            int iZzx = zzx(i8);
            int i10 = iArr2[i8 + 2];
            int i11 = i10 & 1048575;
            int i12 = 1 << (i10 >>> 20);
            if (i11 != i7) {
                if (i11 != 1048575) {
                    i5 = zzb.getInt(obj, i11);
                }
                i = i5;
                i7 = i11;
            } else {
                i = i5;
            }
            if ((268435456 & iZzx) != 0) {
                i2 = i8;
                i3 = i7;
                if (!zzI(obj, i2, i3, i, i12)) {
                    return false;
                }
            } else {
                i2 = i8;
                i3 = i7;
            }
            int iZzz = zzz(iZzx);
            if (iZzz == 9 || iZzz == 17) {
                if (zzI(obj, i2, i3, i, i12) && !zzw(obj, iZzx, zzp(i2))) {
                    return false;
                }
            } else if (iZzz == 27) {
                list = (List) zzop.zzn(obj, iZzx & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zznxVarZzp = zzp(i2);
                    for (i4 = 0; i4 < list.size(); i4++) {
                        if (!zznxVarZzp.zzk(list.get(i4))) {
                            return false;
                        }
                    }
                }
            } else if (iZzz == 60 || iZzz == 68) {
                if (zzL(obj, i9, i2) && !zzw(obj, iZzx, zzp(i2))) {
                    return false;
                }
            } else if (iZzz == 49) {
                list = (List) zzop.zzn(obj, iZzx & 1048575);
                if (list.isEmpty()) {
                    zznxVarZzp = zzp(i2);
                    while (i4 < list.size()) {
                        if (!zznxVarZzp.zzk(list.get(i4))) {
                            return false;
                        }
                    }
                } else {
                    continue;
                }
            } else if (iZzz != 50) {
                continue;
            } else {
                zzng zzngVar = (zzng) zzop.zzn(obj, iZzx & 1048575);
                if (!zzngVar.isEmpty() && ((zznf) zzq(i2)).zze().zzc.zza() == zzou.MESSAGE) {
                    for (Object obj2 : zzngVar.values()) {
                        if (zznxVarZzb == null) {
                            zznxVarZzb = zznu.zza().zzb(obj2.getClass());
                        }
                        if (!zznxVarZzb.zzk(obj2)) {
                            return false;
                        }
                    }
                }
            }
            i6++;
            i7 = i3;
            i5 = i;
        }
    }
}
