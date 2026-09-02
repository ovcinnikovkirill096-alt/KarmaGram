package com.google.android.gms.internal.play_billing;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.CharacterCompat;
import sun.misc.Unsafe;

final class zzip implements zzix {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzjq.zzg();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzim zzg;
    private final boolean zzh = false;
    private final int[] zzi;
    private final int zzj;
    private final int zzk;
    private final zzjj zzl;
    private final zzgx zzm;

    private zzip(int[] iArr, Object[] objArr, int i, int i2, zzim zzimVar, boolean z, int[] iArr2, int i3, int i4, zzir zzirVar, zzhz zzhzVar, zzjj zzjjVar, zzgx zzgxVar, zzih zzihVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = iArr2;
        this.zzj = i3;
        this.zzk = i4;
        this.zzl = zzjjVar;
        this.zzm = zzgxVar;
        this.zzg = zzimVar;
    }

    private static void zzA(Object obj) {
        if (!zzL(obj)) {
            throw new IllegalArgumentException("Mutating immutable message: ".concat(String.valueOf(obj)));
        }
    }

    private final void zzB(Object obj, Object obj2, int i) {
        if (zzI(obj2, i)) {
            int iZzs = zzs(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzs;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzix zzixVarZzv = zzv(i);
            if (!zzI(obj, i)) {
                if (zzL(object)) {
                    Object objZze = zzixVarZzv.zze();
                    zzixVarZzv.zzg(objZze, object);
                    unsafe.putObject(obj, j, objZze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzD(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzL(object2)) {
                Object objZze2 = zzixVarZzv.zze();
                zzixVarZzv.zzg(objZze2, object2);
                unsafe.putObject(obj, j, objZze2);
                object2 = objZze2;
            }
            zzixVarZzv.zzg(object2, object);
        }
    }

    private final void zzC(Object obj, Object obj2, int i) {
        int i2 = this.zzc[i];
        if (zzM(obj2, i2, i)) {
            int iZzs = zzs(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzs;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzix zzixVarZzv = zzv(i);
            if (!zzM(obj, i2, i)) {
                if (zzL(object)) {
                    Object objZze = zzixVarZzv.zze();
                    zzixVarZzv.zzg(objZze, object);
                    unsafe.putObject(obj, j, objZze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzE(obj, i2, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzL(object2)) {
                Object objZze2 = zzixVarZzv.zze();
                zzixVarZzv.zzg(objZze2, object2);
                unsafe.putObject(obj, j, objZze2);
                object2 = objZze2;
            }
            zzixVarZzv.zzg(object2, object);
        }
    }

    private final void zzD(Object obj, int i) {
        int iZzp = zzp(i);
        long j = 1048575 & iZzp;
        if (j == 1048575) {
            return;
        }
        zzjq.zzq(obj, j, (1 << (iZzp >>> 20)) | zzjq.zzc(obj, j));
    }

    private final void zzE(Object obj, int i, int i2) {
        zzjq.zzq(obj, zzp(i2) & 1048575, i);
    }

    private final boolean zzH(Object obj, Object obj2, int i) {
        return zzI(obj, i) == zzI(obj2, i);
    }

    private final boolean zzI(Object obj, int i) {
        int iZzp = zzp(i);
        long j = iZzp & 1048575;
        if (j != 1048575) {
            return (zzjq.zzc(obj, j) & (1 << (iZzp >>> 20))) != 0;
        }
        int iZzs = zzs(i);
        long j2 = iZzs & 1048575;
        switch (zzr(iZzs)) {
            case 0:
                return Double.doubleToRawLongBits(zzjq.zza(obj, j2)) != 0;
            case 1:
                return Float.floatToRawIntBits(zzjq.zzb(obj, j2)) != 0;
            case 2:
                return zzjq.zzd(obj, j2) != 0;
            case 3:
                return zzjq.zzd(obj, j2) != 0;
            case 4:
                return zzjq.zzc(obj, j2) != 0;
            case 5:
                return zzjq.zzd(obj, j2) != 0;
            case 6:
                return zzjq.zzc(obj, j2) != 0;
            case 7:
                return zzjq.zzw(obj, j2);
            case 8:
                Object objZzf = zzjq.zzf(obj, j2);
                if (objZzf instanceof String) {
                    return !((String) objZzf).isEmpty();
                }
                if (objZzf instanceof zzgk) {
                    return !zzgk.zzb.equals(objZzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzjq.zzf(obj, j2) != null;
            case 10:
                return !zzgk.zzb.equals(zzjq.zzf(obj, j2));
            case 11:
                return zzjq.zzc(obj, j2) != 0;
            case 12:
                return zzjq.zzc(obj, j2) != 0;
            case 13:
                return zzjq.zzc(obj, j2) != 0;
            case 14:
                return zzjq.zzd(obj, j2) != 0;
            case 15:
                return zzjq.zzc(obj, j2) != 0;
            case 16:
                return zzjq.zzd(obj, j2) != 0;
            case 17:
                return zzjq.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zzJ(Object obj, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zzI(obj, i);
        }
        return (i3 & i4) != 0;
    }

    private static boolean zzK(Object obj, int i, zzix zzixVar) {
        return zzixVar.zzk(zzjq.zzf(obj, i & 1048575));
    }

    private static boolean zzL(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof zzhk) {
            return ((zzhk) obj).zzA();
        }
        return true;
    }

    private final boolean zzM(Object obj, int i, int i2) {
        return zzjq.zzc(obj, (long) (zzp(i2) & 1048575)) == i;
    }

    private static boolean zzN(Object obj, long j) {
        return ((Boolean) zzjq.zzf(obj, j)).booleanValue();
    }

    private static final void zzO(int i, Object obj, zzjw zzjwVar) {
        if (obj instanceof String) {
            zzjwVar.zzG(i, (String) obj);
        } else {
            zzjwVar.zzd(i, (zzgk) obj);
        }
    }

    /* JADX WARN: Code duplicated, block: B:126:0x026e  */
    /* JADX WARN: Code duplicated, block: B:128:0x0274  */
    /* JADX WARN: Code duplicated, block: B:131:0x028c  */
    /* JADX WARN: Code duplicated, block: B:132:0x028f  */
    /* JADX WARN: Code duplicated, block: B:171:0x0350  */
    /* JADX WARN: Code duplicated, block: B:187:0x03a6  */
    /* JADX WARN: Code duplicated, block: B:190:0x03b0  */
    static zzip zzl(Class cls, zzij zzijVar, zzir zzirVar, zzhz zzhzVar, zzjj zzjjVar, zzgx zzgxVar, zzih zzihVar) {
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
        Field fieldZzz;
        char cCharAt9;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        Object obj;
        Field fieldZzz2;
        int i27;
        Object obj2;
        Field fieldZzz3;
        int i28;
        char cCharAt10;
        int i29;
        char cCharAt11;
        int i30;
        char cCharAt12;
        int i31;
        char cCharAt13;
        if (!(zzijVar instanceof zziw)) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzijVar);
            throw null;
        }
        zziw zziwVar = (zziw) zzijVar;
        String strZzd = zziwVar.zzd();
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
        Object[] objArrZze = zziwVar.zze();
        Class<?> cls2 = zziwVar.zza().getClass();
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
            zziw zziwVar2 = zziwVar;
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
                    } else if (zziwVar2.zzc() == 1 || i77 != 0) {
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
                        fieldZzz2 = (Field) obj;
                    } else {
                        fieldZzz2 = zzz(cls2, (String) obj);
                        objArrZze[i26] = fieldZzz2;
                    }
                    int i87 = i7;
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzz2);
                    i27 = i26 + 1;
                    obj2 = objArrZze[i27];
                    i18 = i87;
                    if (obj2 instanceof Field) {
                        fieldZzz3 = (Field) obj2;
                    } else {
                        fieldZzz3 = zzz(cls2, (String) obj2);
                        objArrZze[i27] = fieldZzz3;
                    }
                    iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzz3);
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
                    fieldZzz2 = (Field) obj;
                } else {
                    fieldZzz2 = zzz(cls2, (String) obj);
                    objArrZze[i26] = fieldZzz2;
                }
                int i89 = i7;
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzz2);
                i27 = i26 + 1;
                obj2 = objArrZze[i27];
                i18 = i89;
                if (obj2 instanceof Field) {
                    fieldZzz3 = (Field) obj2;
                } else {
                    fieldZzz3 = zzz(cls2, (String) obj2);
                    objArrZze[i27] = fieldZzz3;
                }
                iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzz3);
                strZzd = strZzd;
                i20 = i88;
                i17 = i82;
                i19 = 0;
                c = CharacterCompat.MIN_HIGH_SURROGATE;
            } else {
                i18 = i7;
                int i90 = i6 + 1;
                Field fieldZzz4 = zzz(cls2, (String) objArrZze[i6]);
                if (i76 == 9 || i76 == 17) {
                    int i91 = i67 / 3;
                    objArr[i91 + i91 + 1] = fieldZzz4.getType();
                } else {
                    if (i76 != 27) {
                        if (i76 == 49) {
                            i6 += 2;
                            i22 = 1;
                        } else if (i76 == 12 || i76 == 30 || i76 == 44) {
                            if (zziwVar2.zzc() == 1 || i77 != 0) {
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
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzz4);
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
                                fieldZzz = (Field) obj3;
                            } else {
                                fieldZzz = zzz(cls2, (String) obj3);
                                objArrZze[i100] = fieldZzz;
                            }
                            int i101 = iCharAt13;
                            int iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldZzz);
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
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzz4);
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
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzz4);
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
            zziwVar = zziwVar2;
            length = length;
            i7 = i18;
        }
        return new zzip(iArr3, objArr, i2, i4, zziwVar.zza(), false, iArr, i5, i62, zzirVar, zzhzVar, zzjjVar, zzgxVar, zzihVar);
    }

    private static double zzm(Object obj, long j) {
        return ((Double) zzjq.zzf(obj, j)).doubleValue();
    }

    private static float zzn(Object obj, long j) {
        return ((Float) zzjq.zzf(obj, j)).floatValue();
    }

    private static int zzo(Object obj, long j) {
        return ((Integer) zzjq.zzf(obj, j)).intValue();
    }

    private final int zzp(int i) {
        return this.zzc[i + 2];
    }

    private static int zzr(int i) {
        return (i >>> 20) & 255;
    }

    private final int zzs(int i) {
        return this.zzc[i + 1];
    }

    private static long zzt(Object obj, long j) {
        return ((Long) zzjq.zzf(obj, j)).longValue();
    }

    private final zzix zzv(int i) {
        Object[] objArr = this.zzd;
        int i2 = i / 3;
        int i3 = i2 + i2;
        zzix zzixVar = (zzix) objArr[i3];
        if (zzixVar != null) {
            return zzixVar;
        }
        zzix zzixVarZzb = zziu.zza().zzb((Class) objArr[i3 + 1]);
        this.zzd[i3] = zzixVarZzb;
        return zzixVarZzb;
    }

    private final Object zzw(int i) {
        int i2 = i / 3;
        return this.zzd[i2 + i2];
    }

    private static Field zzz(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:137:0x036c  */
    /* JADX WARN: Code duplicated, block: B:170:0x0452  */
    /* JADX WARN: Code duplicated, block: B:271:0x06e7 A[PHI: r0
  0x06e7: PHI (r0v2 com.google.android.gms.internal.play_billing.zzip) = 
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v39 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
  (r0v1 com.google.android.gms.internal.play_billing.zzip)
 binds: [B:18:0x0051, B:269:0x06da, B:239:0x0615, B:216:0x0585, B:209:0x0552, B:133:0x0350, B:130:0x0338, B:127:0x0320, B:124:0x0308, B:121:0x02f0, B:118:0x02d8, B:115:0x02c0, B:112:0x02a8, B:109:0x028f, B:106:0x0278, B:103:0x0261, B:100:0x024a, B:97:0x0233, B:92:0x0217, B:80:0x01ca, B:77:0x01bc, B:74:0x01a6, B:71:0x0190, B:68:0x0179, B:65:0x016b, B:62:0x015d, B:59:0x014d, B:53:0x0122, B:50:0x010e, B:46:0x00f0, B:43:0x00db, B:40:0x00c5, B:36:0x00b6, B:32:0x00a7, B:29:0x008d, B:25:0x0072, B:21:0x005a] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.google.android.gms.internal.play_billing.zzix
    public final int zza(Object obj) {
        int i;
        int iZzz;
        int iZzz2;
        int iZzA;
        int iZzz3;
        int iZzz4;
        int iZzz5;
        int iZzd;
        int iZzz6;
        int iZzh;
        int iZzg;
        int size;
        int iZzl;
        int iZzz7;
        int iZzz8;
        int iZzz9;
        int iZzA2;
        int iZze;
        int iZzz10;
        int iZzz11;
        int iZzw;
        int iZzz12;
        int iZzz13;
        int iZzz14;
        int iZzd2;
        int iZzz15;
        zzip zzipVar = this;
        Unsafe unsafe = zzb;
        int i2 = 1048575;
        int i3 = 0;
        int i4 = 0;
        int iZzz16 = 0;
        int i5 = 1048575;
        while (i3 < zzipVar.zzc.length) {
            int iZzs = zzipVar.zzs(i3);
            int iZzr = zzr(iZzs);
            int[] iArr = zzipVar.zzc;
            int i6 = iArr[i3];
            int i7 = iArr[i3 + 2];
            int i8 = i7 & i2;
            if (iZzr <= 17) {
                if (i8 != i5) {
                    i4 = i8 == i2 ? 0 : unsafe.getInt(obj, i8);
                    i5 = i8;
                }
                i = 1 << (i7 >>> 20);
            } else {
                i = 0;
            }
            int i9 = iZzs & i2;
            if (iZzr >= zzhc.zzJ.zza()) {
                zzhc.zzW.zza();
            }
            int i10 = iZzz16;
            long j = i9;
            switch (iZzr) {
                case 0:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz16 = i10 + zzgr.zzz(i6 << 3) + 8;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 1:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz = zzgr.zzz(i6 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 2:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        long j2 = unsafe.getLong(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzA(j2);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 3:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        long j3 = unsafe.getLong(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzA(j3);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 4:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        long j4 = unsafe.getInt(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzA(j4);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 5:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz3 = zzgr.zzz(i6 << 3);
                        iZzz4 = iZzz3 + 8;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 6:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz = zzgr.zzz(i6 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 7:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz4 = zzgr.zzz(i6 << 3) + 1;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 8:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        int i11 = i6 << 3;
                        Object object = unsafe.getObject(obj, j);
                        if (object instanceof zzgk) {
                            iZzz5 = zzgr.zzz(i11);
                            iZzd = ((zzgk) object).zzd();
                            iZzz6 = zzgr.zzz(iZzd);
                            iZzz4 = iZzz5 + iZzz6 + iZzd;
                            iZzz16 = i10 + iZzz4;
                            zzipVar = this;
                            i3 += 3;
                            i2 = 1048575;
                        } else {
                            iZzz2 = zzgr.zzz(i11);
                            iZzA = zzgr.zzy((String) object);
                            iZzz4 = iZzz2 + iZzA;
                            iZzz16 = i10 + iZzz4;
                            zzipVar = this;
                            i3 += 3;
                            i2 = 1048575;
                        }
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 9:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzh = zziz.zzh(i6, unsafe.getObject(obj, j), zzipVar.zzv(i3));
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 10:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        zzgk zzgkVar = (zzgk) unsafe.getObject(obj, j);
                        iZzz5 = zzgr.zzz(i6 << 3);
                        iZzd = zzgkVar.zzd();
                        iZzz6 = zzgr.zzz(iZzd);
                        iZzz4 = iZzz5 + iZzz6 + iZzd;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 11:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        int i12 = unsafe.getInt(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzz(i12);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 12:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        long j5 = unsafe.getInt(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzA(j5);
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 13:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz = zzgr.zzz(i6 << 3);
                        iZzz4 = iZzz + 4;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 14:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzz3 = zzgr.zzz(i6 << 3);
                        iZzz4 = iZzz3 + 8;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 15:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        int i13 = unsafe.getInt(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzz((i13 >> 31) ^ (i13 + i13));
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 16:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        long j6 = unsafe.getLong(obj, j);
                        iZzz2 = zzgr.zzz(i6 << 3);
                        iZzA = zzgr.zzA((j6 >> 63) ^ (j6 + j6));
                        iZzz4 = iZzz2 + iZzA;
                        iZzz16 = i10 + iZzz4;
                        zzipVar = this;
                        i3 += 3;
                        i2 = 1048575;
                    }
                    zzipVar = this;
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 17:
                    if (zzipVar.zzJ(obj, i3, i5, i4, i)) {
                        iZzh = zzgr.zzw(i6, (zzim) unsafe.getObject(obj, j), zzipVar.zzv(i3));
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 18:
                    iZzh = zziz.zzd(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 19:
                    iZzh = zziz.zzb(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 20:
                    List list = (List) unsafe.getObject(obj, j);
                    int i14 = zziz.$r8$clinit;
                    if (list.size() == 0) {
                        iZzg = 0;
                    } else {
                        iZzg = zziz.zzg(list) + (list.size() * zzgr.zzz(i6 << 3));
                    }
                    iZzz16 = iZzg + i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 21:
                    List list2 = (List) unsafe.getObject(obj, j);
                    int i15 = zziz.$r8$clinit;
                    size = list2.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zzl(list2);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 22:
                    List list3 = (List) unsafe.getObject(obj, j);
                    int i16 = zziz.$r8$clinit;
                    size = list3.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zzf(list3);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 23:
                    iZzh = zziz.zzd(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 24:
                    iZzh = zziz.zzb(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 25:
                    List list4 = (List) unsafe.getObject(obj, j);
                    int i17 = zziz.$r8$clinit;
                    int size2 = list4.size();
                    if (size2 == 0) {
                        iZzh = 0;
                    } else {
                        iZzh = size2 * (zzgr.zzz(i6 << 3) + 1);
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 26:
                    List list5 = (List) unsafe.getObject(obj, j);
                    int i18 = zziz.$r8$clinit;
                    int size3 = list5.size();
                    if (size3 == 0) {
                        iZzg = 0;
                    } else {
                        iZzg = zzgr.zzz(i6 << 3) * size3;
                        if (list5 instanceof zzhy) {
                            zzhy zzhyVar = (zzhy) list5;
                            for (int i19 = 0; i19 < size3; i19++) {
                                Object objZzc = zzhyVar.zzc();
                                if (objZzc instanceof zzgk) {
                                    int iZzd3 = ((zzgk) objZzc).zzd();
                                    iZzg += zzgr.zzz(iZzd3) + iZzd3;
                                } else {
                                    iZzg += zzgr.zzy((String) objZzc);
                                }
                            }
                        } else {
                            for (int i20 = 0; i20 < size3; i20++) {
                                Object obj2 = list5.get(i20);
                                if (obj2 instanceof zzgk) {
                                    int iZzd4 = ((zzgk) obj2).zzd();
                                    iZzg += zzgr.zzz(iZzd4) + iZzd4;
                                } else {
                                    iZzg += zzgr.zzy((String) obj2);
                                }
                            }
                        }
                    }
                    iZzz16 = iZzg + i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 27:
                    List list6 = (List) unsafe.getObject(obj, j);
                    zzix zzixVarZzv = zzipVar.zzv(i3);
                    int i21 = zziz.$r8$clinit;
                    int size4 = list6.size();
                    if (size4 == 0) {
                        iZzz8 = 0;
                    } else {
                        iZzz8 = zzgr.zzz(i6 << 3) * size4;
                        for (int i22 = 0; i22 < size4; i22++) {
                            iZzz8 += zzgr.zzx((zzim) list6.get(i22), zzixVarZzv);
                        }
                    }
                    iZzz16 = i10 + iZzz8;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 28:
                    List list7 = (List) unsafe.getObject(obj, j);
                    int i23 = zziz.$r8$clinit;
                    int size5 = list7.size();
                    if (size5 == 0) {
                        iZzz9 = 0;
                    } else {
                        iZzz9 = size5 * zzgr.zzz(i6 << 3);
                        for (int i24 = 0; i24 < list7.size(); i24++) {
                            int iZzd5 = ((zzgk) list7.get(i24)).zzd();
                            iZzz9 += zzgr.zzz(iZzd5) + iZzd5;
                        }
                    }
                    iZzz16 = i10 + iZzz9;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 29:
                    List list8 = (List) unsafe.getObject(obj, j);
                    int i25 = zziz.$r8$clinit;
                    size = list8.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zzk(list8);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 30:
                    List list9 = (List) unsafe.getObject(obj, j);
                    int i26 = zziz.$r8$clinit;
                    size = list9.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zza(list9);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 31:
                    iZzh = zziz.zzb(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 32:
                    iZzh = zziz.zzd(i6, (List) unsafe.getObject(obj, j), false);
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 33:
                    List list10 = (List) unsafe.getObject(obj, j);
                    int i27 = zziz.$r8$clinit;
                    size = list10.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zzi(list10);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 34:
                    List list11 = (List) unsafe.getObject(obj, j);
                    int i28 = zziz.$r8$clinit;
                    size = list11.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zziz.zzj(list11);
                        iZzz7 = zzgr.zzz(i6 << 3);
                        iZzA2 = size * iZzz7;
                        iZzh = iZzl + iZzA2;
                    }
                    iZzz16 = i10 + iZzh;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 35:
                    iZze = zziz.zze((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 36:
                    iZze = zziz.zzc((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 37:
                    iZze = zziz.zzg((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 38:
                    iZze = zziz.zzl((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 39:
                    iZze = zziz.zzf((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 40:
                    iZze = zziz.zze((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 41:
                    iZze = zziz.zzc((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 42:
                    List list12 = (List) unsafe.getObject(obj, j);
                    int i29 = zziz.$r8$clinit;
                    iZze = list12.size();
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 43:
                    iZze = zziz.zzk((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 44:
                    iZze = zziz.zza((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 45:
                    iZze = zziz.zzc((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 46:
                    iZze = zziz.zze((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 47:
                    iZze = zziz.zzi((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 48:
                    iZze = zziz.zzj((List) unsafe.getObject(obj, j));
                    if (iZze > 0) {
                        iZzz10 = zzgr.zzz(i6 << 3);
                        iZzz11 = zzgr.zzz(iZze);
                        iZzz9 = iZzz10 + iZzz11 + iZze;
                        iZzz16 = i10 + iZzz9;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 49:
                    List list13 = (List) unsafe.getObject(obj, j);
                    zzix zzixVarZzv2 = zzipVar.zzv(i3);
                    int i30 = zziz.$r8$clinit;
                    int size6 = list13.size();
                    if (size6 == 0) {
                        iZzw = 0;
                    } else {
                        iZzw = 0;
                        for (int i31 = 0; i31 < size6; i31++) {
                            iZzw += zzgr.zzw(i6, (zzim) list13.get(i31), zzixVarZzv2);
                        }
                    }
                    iZzz16 = i10 + iZzw;
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 50:
                    Object object2 = unsafe.getObject(obj, j);
                    Object objZzw = zzipVar.zzw(i3);
                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(object2);
                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(objZzw);
                    throw null;
                case 51:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz12 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz12 + 8;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 52:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz13 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz13 + 4;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 53:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        long jZzt = zzt(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzA(jZzt);
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 54:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        long jZzt2 = zzt(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzA(jZzt2);
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 55:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        long jZzo = zzo(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzA(jZzo);
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 56:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz12 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz12 + 8;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 57:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz13 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz13 + 4;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 58:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzh = zzgr.zzz(i6 << 3) + 1;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 59:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        int i32 = i6 << 3;
                        Object object3 = unsafe.getObject(obj, j);
                        if (object3 instanceof zzgk) {
                            iZzz14 = zzgr.zzz(i32);
                            iZzd2 = ((zzgk) object3).zzd();
                            iZzz15 = zzgr.zzz(iZzd2);
                            iZzh = iZzz14 + iZzz15 + iZzd2;
                            iZzz16 = i10 + iZzh;
                        } else {
                            iZzl = zzgr.zzz(i32);
                            iZzA2 = zzgr.zzy((String) object3);
                            iZzh = iZzl + iZzA2;
                            iZzz16 = i10 + iZzh;
                        }
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 60:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzh = zziz.zzh(i6, unsafe.getObject(obj, j), zzipVar.zzv(i3));
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 61:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        zzgk zzgkVar2 = (zzgk) unsafe.getObject(obj, j);
                        iZzz14 = zzgr.zzz(i6 << 3);
                        iZzd2 = zzgkVar2.zzd();
                        iZzz15 = zzgr.zzz(iZzd2);
                        iZzh = iZzz14 + iZzz15 + iZzd2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 62:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        int iZzo = zzo(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzz(iZzo);
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 63:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        long jZzo2 = zzo(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzA(jZzo2);
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 64:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz13 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz13 + 4;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 65:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzz12 = zzgr.zzz(i6 << 3);
                        iZzh = iZzz12 + 8;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 66:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        int iZzo2 = zzo(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzz((iZzo2 >> 31) ^ (iZzo2 + iZzo2));
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 67:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        long jZzt3 = zzt(obj, j);
                        iZzl = zzgr.zzz(i6 << 3);
                        iZzA2 = zzgr.zzA((jZzt3 >> 63) ^ (jZzt3 + jZzt3));
                        iZzh = iZzl + iZzA2;
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                case 68:
                    if (zzipVar.zzM(obj, i6, i3)) {
                        iZzh = zzgr.zzw(i6, (zzim) unsafe.getObject(obj, j), zzipVar.zzv(i3));
                        iZzz16 = i10 + iZzh;
                    } else {
                        iZzz16 = i10;
                    }
                    i3 += 3;
                    i2 = 1048575;
                    break;
                default:
                    iZzz16 = i10;
                    i3 += 3;
                    i2 = 1048575;
                    break;
            }
        }
        int iZza = iZzz16 + ((zzhk) obj).zzc.zza();
        if (!zzipVar.zzh) {
            return iZza;
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.play_billing.zzix
    public final int zzb(Object obj) {
        int i;
        long jDoubleToLongBits;
        int iFloatToIntBits;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < this.zzc.length; i4 += 3) {
            int iZzs = zzs(i4);
            int[] iArr = this.zzc;
            int i5 = 1048575 & iZzs;
            int iZzr = zzr(iZzs);
            int i6 = iArr[i4];
            long j = i5;
            int iHashCode = 37;
            switch (iZzr) {
                case 0:
                    i = i3 * 53;
                    jDoubleToLongBits = Double.doubleToLongBits(zzjq.zza(obj, j));
                    byte[] bArr = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 1:
                    i = i3 * 53;
                    iFloatToIntBits = Float.floatToIntBits(zzjq.zzb(obj, j));
                    i3 = i + iFloatToIntBits;
                    break;
                case 2:
                    i = i3 * 53;
                    jDoubleToLongBits = zzjq.zzd(obj, j);
                    byte[] bArr2 = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 3:
                    i = i3 * 53;
                    jDoubleToLongBits = zzjq.zzd(obj, j);
                    byte[] bArr3 = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 4:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 5:
                    i = i3 * 53;
                    jDoubleToLongBits = zzjq.zzd(obj, j);
                    byte[] bArr4 = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 6:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 7:
                    i = i3 * 53;
                    iFloatToIntBits = zzhp.zza(zzjq.zzw(obj, j));
                    i3 = i + iFloatToIntBits;
                    break;
                case 8:
                    i = i3 * 53;
                    iFloatToIntBits = ((String) zzjq.zzf(obj, j)).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 9:
                    i2 = i3 * 53;
                    Object objZzf = zzjq.zzf(obj, j);
                    if (objZzf != null) {
                        iHashCode = objZzf.hashCode();
                    }
                    i3 = i2 + iHashCode;
                    break;
                case 10:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 11:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 12:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 13:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 14:
                    i = i3 * 53;
                    jDoubleToLongBits = zzjq.zzd(obj, j);
                    byte[] bArr5 = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 15:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 16:
                    i = i3 * 53;
                    jDoubleToLongBits = zzjq.zzd(obj, j);
                    byte[] bArr6 = zzhp.zzb;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 17:
                    i2 = i3 * 53;
                    Object objZzf2 = zzjq.zzf(obj, j);
                    if (objZzf2 != null) {
                        iHashCode = objZzf2.hashCode();
                    }
                    i3 = i2 + iHashCode;
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
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 50:
                    i = i3 * 53;
                    iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 51:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = Double.doubleToLongBits(zzm(obj, j));
                        byte[] bArr7 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 52:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = Float.floatToIntBits(zzn(obj, j));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 53:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzt(obj, j);
                        byte[] bArr8 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 54:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzt(obj, j);
                        byte[] bArr9 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 55:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 56:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzt(obj, j);
                        byte[] bArr10 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 57:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 58:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzhp.zza(zzN(obj, j));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 59:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = ((String) zzjq.zzf(obj, j)).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 60:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 61:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 62:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 63:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 64:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 65:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzt(obj, j);
                        byte[] bArr11 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 66:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzo(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 67:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzt(obj, j);
                        byte[] bArr12 = zzhp.zzb;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 68:
                    if (zzM(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzjq.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
            }
        }
        int iHashCode2 = (i3 * 53) + ((zzhk) obj).zzc.hashCode();
        if (!this.zzh) {
            return iHashCode2;
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.play_billing.zzix
    public final Object zze() {
        return ((zzhk) this.zzg).zzp();
    }

    /* JADX WARN: Code duplicated, block: B:30:0x006b  */
    /* JADX WARN: Code duplicated, block: B:32:0x0071  */
    /* JADX WARN: Code duplicated, block: B:44:0x007e A[SYNTHETIC] */
    @Override // com.google.android.gms.internal.play_billing.zzix
    public final void zzf(Object obj) {
        if (zzL(obj)) {
            if (obj instanceof zzhk) {
                zzhk zzhkVar = (zzhk) obj;
                zzhkVar.zzy(Integer.MAX_VALUE);
                zzhkVar.zza = 0;
                zzhkVar.zzw();
            }
            int[] iArr = this.zzc;
            for (int i = 0; i < iArr.length; i += 3) {
                int iZzs = zzs(i);
                int i2 = 1048575 & iZzs;
                int iZzr = zzr(iZzs);
                long j = i2;
                if (iZzr != 9) {
                    if (iZzr != 60 && iZzr != 68) {
                        switch (iZzr) {
                            case 17:
                                if (zzI(obj, i)) {
                                    zzv(i).zzf(zzb.getObject(obj, j));
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
                                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzjq.zzf(obj, j));
                                throw null;
                            case 50:
                                Object object = zzb.getObject(obj, j);
                                if (object != null) {
                                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(object);
                                    throw null;
                                }
                                break;
                                break;
                        }
                    } else if (zzM(obj, this.zzc[i], i)) {
                        zzv(i).zzf(zzb.getObject(obj, j));
                    }
                } else if (zzI(obj, i)) {
                    zzv(i).zzf(zzb.getObject(obj, j));
                }
            }
            this.zzl.zza(obj);
            if (this.zzh) {
                this.zzm.zza(obj);
            }
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzix
    public final void zzg(Object obj, Object obj2) {
        zzA(obj);
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzs = zzs(i);
            int i2 = 1048575 & iZzs;
            int[] iArr = this.zzc;
            int iZzr = zzr(iZzs);
            int i3 = iArr[i];
            long j = i2;
            switch (iZzr) {
                case 0:
                    if (zzI(obj2, i)) {
                        zzjq.zzo(obj, j, zzjq.zza(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 1:
                    if (zzI(obj2, i)) {
                        zzjq.zzp(obj, j, zzjq.zzb(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 2:
                    if (zzI(obj2, i)) {
                        zzjq.zzr(obj, j, zzjq.zzd(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 3:
                    if (zzI(obj2, i)) {
                        zzjq.zzr(obj, j, zzjq.zzd(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 4:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 5:
                    if (zzI(obj2, i)) {
                        zzjq.zzr(obj, j, zzjq.zzd(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 6:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 7:
                    if (zzI(obj2, i)) {
                        zzjq.zzm(obj, j, zzjq.zzw(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 8:
                    if (zzI(obj2, i)) {
                        zzjq.zzs(obj, j, zzjq.zzf(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 9:
                    zzB(obj, obj2, i);
                    break;
                case 10:
                    if (zzI(obj2, i)) {
                        zzjq.zzs(obj, j, zzjq.zzf(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 11:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 12:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 13:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 14:
                    if (zzI(obj2, i)) {
                        zzjq.zzr(obj, j, zzjq.zzd(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 15:
                    if (zzI(obj2, i)) {
                        zzjq.zzq(obj, j, zzjq.zzc(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 16:
                    if (zzI(obj2, i)) {
                        zzjq.zzr(obj, j, zzjq.zzd(obj2, j));
                        zzD(obj, i);
                    }
                    break;
                case 17:
                    zzB(obj, obj2, i);
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
                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzjq.zzf(obj, j));
                    WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzjq.zzf(obj2, j));
                    throw null;
                case 50:
                    int i4 = zziz.$r8$clinit;
                    zzjq.zzs(obj, j, zzih.zza(zzjq.zzf(obj, j), zzjq.zzf(obj2, j)));
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
                    if (zzM(obj2, i3, i)) {
                        zzjq.zzs(obj, j, zzjq.zzf(obj2, j));
                        zzE(obj, i3, i);
                    }
                    break;
                case 60:
                    zzC(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (zzM(obj2, i3, i)) {
                        zzjq.zzs(obj, j, zzjq.zzf(obj2, j));
                        zzE(obj, i3, i);
                    }
                    break;
                case 68:
                    zzC(obj, obj2, i);
                    break;
            }
        }
        zziz.zzp(this.zzl, obj, obj2);
        if (this.zzh) {
            zziz.zzo(this.zzm, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzix
    public final void zzi(Object obj, zzjw zzjwVar) {
        int i;
        zzip zzipVar = this;
        if (zzipVar.zzh) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
            throw null;
        }
        int[] iArr = zzipVar.zzc;
        Unsafe unsafe = zzb;
        int i2 = 1048575;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i4 < iArr.length) {
            int iZzs = zzipVar.zzs(i4);
            int[] iArr2 = zzipVar.zzc;
            int iZzr = zzr(iZzs);
            int i6 = iArr2[i4];
            if (iZzr <= 17) {
                int i7 = iArr2[i4 + 2];
                int i8 = i7 & i2;
                if (i8 != i3) {
                    i5 = i8 == i2 ? 0 : unsafe.getInt(obj, i8);
                    i3 = i8;
                }
                i = 1 << (i7 >>> 20);
            } else {
                i = 0;
            }
            long j = iZzs & i2;
            switch (iZzr) {
                case 0:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzf(i6, zzjq.zza(obj, j));
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 1:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzo(i6, zzjq.zzb(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 2:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzt(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 3:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzK(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 4:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzr(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 5:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzm(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 6:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzk(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 7:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzb(i6, zzjq.zzw(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 8:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzO(i6, unsafe.getObject(obj, j), zzjwVar);
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 9:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzv(i6, unsafe.getObject(obj, j), zzipVar.zzv(i4));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 10:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzd(i6, (zzgk) unsafe.getObject(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 11:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzI(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 12:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzi(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 13:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzx(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 14:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzz(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 15:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzB(i6, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 16:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzD(i6, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 17:
                    if (zzipVar.zzJ(obj, i4, i3, i5, i)) {
                        zzjwVar.zzq(i6, unsafe.getObject(obj, j), zzipVar.zzv(i4));
                    } else {
                        continue;
                    }
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 18:
                    zziz.zzr(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 19:
                    zziz.zzv(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 20:
                    zziz.zzx(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 21:
                    zziz.zzD(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 22:
                    zziz.zzw(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 23:
                    zziz.zzu(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 24:
                    zziz.zzt(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 25:
                    zziz.zzq(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 26:
                    int i9 = zzipVar.zzc[i4];
                    List list = (List) unsafe.getObject(obj, j);
                    int i10 = zziz.$r8$clinit;
                    if (list != null && !list.isEmpty()) {
                        zzjwVar.zzH(i9, list);
                    }
                    break;
                case 27:
                    int i11 = zzipVar.zzc[i4];
                    List list2 = (List) unsafe.getObject(obj, j);
                    zzix zzixVarZzv = zzipVar.zzv(i4);
                    int i12 = zziz.$r8$clinit;
                    if (list2 != null && !list2.isEmpty()) {
                        for (int i13 = 0; i13 < list2.size(); i13++) {
                            ((zzgs) zzjwVar).zzv(i11, list2.get(i13), zzixVarZzv);
                        }
                    }
                    break;
                case 28:
                    int i14 = zzipVar.zzc[i4];
                    List list3 = (List) unsafe.getObject(obj, j);
                    int i15 = zziz.$r8$clinit;
                    if (list3 != null && !list3.isEmpty()) {
                        zzjwVar.zze(i14, list3);
                    }
                    break;
                case 29:
                    zziz.zzC(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 30:
                    zziz.zzs(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 31:
                    zziz.zzy(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 32:
                    zziz.zzz(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 33:
                    zziz.zzA(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 34:
                    zziz.zzB(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, false);
                    continue;
                    i4 += 3;
                    i2 = 1048575;
                    zzipVar = this;
                    break;
                case 35:
                    zziz.zzr(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 36:
                    zziz.zzv(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 37:
                    zziz.zzx(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 38:
                    zziz.zzD(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 39:
                    zziz.zzw(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 40:
                    zziz.zzu(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 41:
                    zziz.zzt(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 42:
                    zziz.zzq(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 43:
                    zziz.zzC(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 44:
                    zziz.zzs(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 45:
                    zziz.zzy(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 46:
                    zziz.zzz(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 47:
                    zziz.zzA(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 48:
                    zziz.zzB(zzipVar.zzc[i4], (List) unsafe.getObject(obj, j), zzjwVar, true);
                    break;
                case 49:
                    int i16 = zzipVar.zzc[i4];
                    List list4 = (List) unsafe.getObject(obj, j);
                    zzix zzixVarZzv2 = zzipVar.zzv(i4);
                    int i17 = zziz.$r8$clinit;
                    if (list4 != null && !list4.isEmpty()) {
                        for (int i18 = 0; i18 < list4.size(); i18++) {
                            ((zzgs) zzjwVar).zzq(i16, list4.get(i18), zzixVarZzv2);
                        }
                    }
                    break;
                case 50:
                    if (unsafe.getObject(obj, j) != null) {
                        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzipVar.zzw(i4));
                        throw null;
                    }
                    break;
                case 51:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzf(i6, zzm(obj, j));
                    }
                    break;
                case 52:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzo(i6, zzn(obj, j));
                    }
                    break;
                case 53:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzt(i6, zzt(obj, j));
                    }
                    break;
                case 54:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzK(i6, zzt(obj, j));
                    }
                    break;
                case 55:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzr(i6, zzo(obj, j));
                    }
                    break;
                case 56:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzm(i6, zzt(obj, j));
                    }
                    break;
                case 57:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzk(i6, zzo(obj, j));
                    }
                    break;
                case 58:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzb(i6, zzN(obj, j));
                    }
                    break;
                case 59:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzO(i6, unsafe.getObject(obj, j), zzjwVar);
                    }
                    break;
                case 60:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzv(i6, unsafe.getObject(obj, j), zzipVar.zzv(i4));
                    }
                    break;
                case 61:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzd(i6, (zzgk) unsafe.getObject(obj, j));
                    }
                    break;
                case 62:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzI(i6, zzo(obj, j));
                    }
                    break;
                case 63:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzi(i6, zzo(obj, j));
                    }
                    break;
                case 64:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzx(i6, zzo(obj, j));
                    }
                    break;
                case 65:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzz(i6, zzt(obj, j));
                    }
                    break;
                case 66:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzB(i6, zzo(obj, j));
                    }
                    break;
                case 67:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzD(i6, zzt(obj, j));
                    }
                    break;
                case 68:
                    if (zzipVar.zzM(obj, i6, i4)) {
                        zzjwVar.zzq(i6, unsafe.getObject(obj, j), zzipVar.zzv(i4));
                    }
                    break;
            }
            i4 += 3;
            i2 = 1048575;
            zzipVar = this;
        }
        ((zzhk) obj).zzc.zzl(zzjwVar);
    }

    @Override // com.google.android.gms.internal.play_billing.zzix
    public final boolean zzj(Object obj, Object obj2) {
        boolean zZzE;
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzs = zzs(i);
            long j = iZzs & 1048575;
            switch (zzr(iZzs)) {
                case 0:
                    if (!zzH(obj, obj2, i) || Double.doubleToLongBits(zzjq.zza(obj, j)) != Double.doubleToLongBits(zzjq.zza(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 1:
                    if (!zzH(obj, obj2, i) || Float.floatToIntBits(zzjq.zzb(obj, j)) != Float.floatToIntBits(zzjq.zzb(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 2:
                    if (!zzH(obj, obj2, i) || zzjq.zzd(obj, j) != zzjq.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 3:
                    if (!zzH(obj, obj2, i) || zzjq.zzd(obj, j) != zzjq.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 4:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 5:
                    if (!zzH(obj, obj2, i) || zzjq.zzd(obj, j) != zzjq.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 6:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 7:
                    if (!zzH(obj, obj2, i) || zzjq.zzw(obj, j) != zzjq.zzw(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 8:
                    if (!zzH(obj, obj2, i) || !zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 9:
                    if (!zzH(obj, obj2, i) || !zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 10:
                    if (!zzH(obj, obj2, i) || !zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 11:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 12:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 13:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 14:
                    if (!zzH(obj, obj2, i) || zzjq.zzd(obj, j) != zzjq.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 15:
                    if (!zzH(obj, obj2, i) || zzjq.zzc(obj, j) != zzjq.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 16:
                    if (!zzH(obj, obj2, i) || zzjq.zzd(obj, j) != zzjq.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 17:
                    if (!zzH(obj, obj2, i) || !zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j))) {
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
                    zZzE = zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j));
                    break;
                case 50:
                    zZzE = zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j));
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
                    long jZzp = zzp(i) & 1048575;
                    if (zzjq.zzc(obj, jZzp) != zzjq.zzc(obj2, jZzp) || !zziz.zzE(zzjq.zzf(obj, j), zzjq.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                default:
                    continue;
                    break;
            }
            if (!zZzE) {
                return false;
            }
        }
        if (!((zzhk) obj).zzc.equals(((zzhk) obj2).zzc)) {
            return false;
        }
        if (!this.zzh) {
            return true;
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    /* JADX WARN: Code duplicated, block: B:40:0x0085  */
    /* JADX WARN: Code duplicated, block: B:42:0x0094  */
    /* JADX WARN: Code duplicated, block: B:45:0x009f  */
    /* JADX WARN: Code duplicated, block: B:48:0x00aa A[LOOP:1: B:43:0x0099->B:48:0x00aa, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:64:0x00a9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:68:0x00bf A[SYNTHETIC] */
    @Override // com.google.android.gms.internal.play_billing.zzix
    public final boolean zzk(Object obj) {
        int i;
        int i2;
        int i3;
        List list;
        zzix zzixVarZzv;
        int i4;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1048575;
        while (i6 < this.zzj) {
            int[] iArr = this.zzi;
            int[] iArr2 = this.zzc;
            int i8 = iArr[i6];
            int i9 = iArr2[i8];
            int iZzs = zzs(i8);
            int i10 = this.zzc[i8 + 2];
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
            if ((268435456 & iZzs) != 0) {
                i2 = i8;
                i3 = i7;
                if (!zzJ(obj, i2, i3, i, i12)) {
                    return false;
                }
            } else {
                i2 = i8;
                i3 = i7;
            }
            int iZzr = zzr(iZzs);
            if (iZzr == 9 || iZzr == 17) {
                if (zzJ(obj, i2, i3, i, i12) && !zzK(obj, iZzs, zzv(i2))) {
                    return false;
                }
            } else if (iZzr == 27) {
                list = (List) zzjq.zzf(obj, iZzs & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzixVarZzv = zzv(i2);
                    for (i4 = 0; i4 < list.size(); i4++) {
                        if (!zzixVarZzv.zzk(list.get(i4))) {
                            return false;
                        }
                    }
                }
            } else if (iZzr == 60 || iZzr == 68) {
                if (zzM(obj, i9, i2) && !zzK(obj, iZzs, zzv(i2))) {
                    return false;
                }
            } else if (iZzr == 49) {
                list = (List) zzjq.zzf(obj, iZzs & 1048575);
                if (list.isEmpty()) {
                    zzixVarZzv = zzv(i2);
                    while (i4 < list.size()) {
                        if (!zzixVarZzv.zzk(list.get(i4))) {
                            return false;
                        }
                    }
                } else {
                    continue;
                }
            } else if (iZzr == 50) {
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzjq.zzf(obj, iZzs & 1048575));
                throw null;
            }
            i6++;
            i7 = i3;
            i5 = i;
        }
        if (!this.zzh) {
            return true;
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
