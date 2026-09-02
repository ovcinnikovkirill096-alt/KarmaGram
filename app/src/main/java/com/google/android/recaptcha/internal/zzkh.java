package com.google.android.recaptcha.internal;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import sun.misc.Unsafe;

final class zzkh<T> implements zzkr<T> {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzlv.zzg();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzke zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final int[] zzj;
    private final int zzk;
    private final int zzl;
    private final zzjs zzm;
    private final zzll zzn;
    private final zzif zzo;
    private final zzkk zzp;
    private final zzjz zzq;

    private zzkh(int[] iArr, Object[] objArr, int i, int i2, zzke zzkeVar, int i3, boolean z, int[] iArr2, int i4, int i5, zzkk zzkkVar, zzjs zzjsVar, zzll zzllVar, zzif zzifVar, zzjz zzjzVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = zzkeVar instanceof zzit;
        boolean z2 = false;
        if (zzifVar != null && zzifVar.zzj(zzkeVar)) {
            z2 = true;
        }
        this.zzh = z2;
        this.zzj = iArr2;
        this.zzk = i4;
        this.zzl = i5;
        this.zzp = zzkkVar;
        this.zzm = zzjsVar;
        this.zzn = zzllVar;
        this.zzo = zzifVar;
        this.zzg = zzkeVar;
        this.zzq = zzjzVar;
    }

    private final Object zzA(Object obj, int i) {
        zzkr zzkrVarZzx = zzx(i);
        int iZzu = zzu(i) & 1048575;
        if (!zzN(obj, i)) {
            return zzkrVarZzx.zze();
        }
        Object object = zzb.getObject(obj, iZzu);
        if (zzQ(object)) {
            return object;
        }
        Object objZze = zzkrVarZzx.zze();
        if (object != null) {
            zzkrVarZzx.zzg(objZze, object);
        }
        return objZze;
    }

    private final Object zzB(Object obj, int i, int i2) {
        zzkr zzkrVarZzx = zzx(i2);
        if (!zzR(obj, i, i2)) {
            return zzkrVarZzx.zze();
        }
        Object object = zzb.getObject(obj, zzu(i2) & 1048575);
        if (zzQ(object)) {
            return object;
        }
        Object objZze = zzkrVarZzx.zze();
        if (object != null) {
            zzkrVarZzx.zzg(objZze, object);
        }
        return objZze;
    }

    private static Field zzC(Class cls, String str) {
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

    private static void zzD(Object obj) {
        if (!zzQ(obj)) {
            throw new IllegalArgumentException("Mutating immutable message: ".concat(String.valueOf(obj)));
        }
    }

    private final void zzE(Object obj, Object obj2, int i) {
        if (zzN(obj2, i)) {
            int iZzu = zzu(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzu;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzkr zzkrVarZzx = zzx(i);
            if (!zzN(obj, i)) {
                if (zzQ(object)) {
                    Object objZze = zzkrVarZzx.zze();
                    zzkrVarZzx.zzg(objZze, object);
                    unsafe.putObject(obj, j, objZze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzH(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzQ(object2)) {
                Object objZze2 = zzkrVarZzx.zze();
                zzkrVarZzx.zzg(objZze2, object2);
                unsafe.putObject(obj, j, objZze2);
                object2 = objZze2;
            }
            zzkrVarZzx.zzg(object2, object);
        }
    }

    private final void zzF(Object obj, Object obj2, int i) {
        int i2 = this.zzc[i];
        if (zzR(obj2, i2, i)) {
            int iZzu = zzu(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = iZzu;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzkr zzkrVarZzx = zzx(i);
            if (!zzR(obj, i2, i)) {
                if (zzQ(object)) {
                    Object objZze = zzkrVarZzx.zze();
                    zzkrVarZzx.zzg(objZze, object);
                    unsafe.putObject(obj, j, objZze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzI(obj, i2, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzQ(object2)) {
                Object objZze2 = zzkrVarZzx.zze();
                zzkrVarZzx.zzg(objZze2, object2);
                unsafe.putObject(obj, j, objZze2);
                object2 = objZze2;
            }
            zzkrVarZzx.zzg(object2, object);
        }
    }

    private final void zzG(Object obj, int i, zzkq zzkqVar) {
        long j = i & 1048575;
        if (zzM(i)) {
            zzlv.zzs(obj, j, zzkqVar.zzs());
        } else if (this.zzi) {
            zzlv.zzs(obj, j, zzkqVar.zzr());
        } else {
            zzlv.zzs(obj, j, zzkqVar.zzp());
        }
    }

    private final void zzH(Object obj, int i) {
        int iZzr = zzr(i);
        long j = 1048575 & iZzr;
        if (j == 1048575) {
            return;
        }
        zzlv.zzq(obj, j, (1 << (iZzr >>> 20)) | zzlv.zzc(obj, j));
    }

    private final void zzI(Object obj, int i, int i2) {
        zzlv.zzq(obj, zzr(i2) & 1048575, i);
    }

    private final void zzJ(Object obj, int i, Object obj2) {
        zzb.putObject(obj, zzu(i) & 1048575, obj2);
        zzH(obj, i);
    }

    private final void zzK(Object obj, int i, int i2, Object obj2) {
        zzb.putObject(obj, zzu(i2) & 1048575, obj2);
        zzI(obj, i, i2);
    }

    private final boolean zzL(Object obj, Object obj2, int i) {
        return zzN(obj, i) == zzN(obj2, i);
    }

    private static boolean zzM(int i) {
        return (i & 536870912) != 0;
    }

    private final boolean zzN(Object obj, int i) {
        int iZzr = zzr(i);
        long j = iZzr & 1048575;
        if (j != 1048575) {
            return (zzlv.zzc(obj, j) & (1 << (iZzr >>> 20))) != 0;
        }
        int iZzu = zzu(i);
        long j2 = iZzu & 1048575;
        switch (zzt(iZzu)) {
            case 0:
                return Double.doubleToRawLongBits(zzlv.zza(obj, j2)) != 0;
            case 1:
                return Float.floatToRawIntBits(zzlv.zzb(obj, j2)) != 0;
            case 2:
                return zzlv.zzd(obj, j2) != 0;
            case 3:
                return zzlv.zzd(obj, j2) != 0;
            case 4:
                return zzlv.zzc(obj, j2) != 0;
            case 5:
                return zzlv.zzd(obj, j2) != 0;
            case 6:
                return zzlv.zzc(obj, j2) != 0;
            case 7:
                return zzlv.zzw(obj, j2);
            case 8:
                Object objZzf = zzlv.zzf(obj, j2);
                if (objZzf instanceof String) {
                    return !((String) objZzf).isEmpty();
                }
                if (objZzf instanceof zzgw) {
                    return !zzgw.zzb.equals(objZzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzlv.zzf(obj, j2) != null;
            case 10:
                return !zzgw.zzb.equals(zzlv.zzf(obj, j2));
            case 11:
                return zzlv.zzc(obj, j2) != 0;
            case 12:
                return zzlv.zzc(obj, j2) != 0;
            case 13:
                return zzlv.zzc(obj, j2) != 0;
            case 14:
                return zzlv.zzd(obj, j2) != 0;
            case 15:
                return zzlv.zzc(obj, j2) != 0;
            case 16:
                return zzlv.zzd(obj, j2) != 0;
            case 17:
                return zzlv.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zzO(Object obj, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zzN(obj, i);
        }
        return (i3 & i4) != 0;
    }

    private static boolean zzP(Object obj, int i, zzkr zzkrVar) {
        return zzkrVar.zzl(zzlv.zzf(obj, i & 1048575));
    }

    private static boolean zzQ(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof zzit) {
            return ((zzit) obj).zzG();
        }
        return true;
    }

    private final boolean zzR(Object obj, int i, int i2) {
        return zzlv.zzc(obj, (long) (zzr(i2) & 1048575)) == i;
    }

    private static boolean zzS(Object obj, long j) {
        return ((Boolean) zzlv.zzf(obj, j)).booleanValue();
    }

    private static final void zzT(int i, Object obj, zzmd zzmdVar) {
        if (obj instanceof String) {
            zzmdVar.zzG(i, (String) obj);
        } else {
            zzmdVar.zzd(i, (zzgw) obj);
        }
    }

    static zzlm zzd(Object obj) {
        zzit zzitVar = (zzit) obj;
        zzlm zzlmVar = zzitVar.zzc;
        if (zzlmVar != zzlm.zzc()) {
            return zzlmVar;
        }
        zzlm zzlmVarZzf = zzlm.zzf();
        zzitVar.zzc = zzlmVarZzf;
        return zzlmVarZzf;
    }

    /* JADX WARN: Code duplicated, block: B:125:0x0268  */
    /* JADX WARN: Code duplicated, block: B:127:0x026e  */
    /* JADX WARN: Code duplicated, block: B:130:0x0284  */
    /* JADX WARN: Code duplicated, block: B:131:0x0287  */
    /* JADX WARN: Code duplicated, block: B:171:0x0351  */
    /* JADX WARN: Code duplicated, block: B:186:0x03a0  */
    /* JADX WARN: Code duplicated, block: B:189:0x03ad  */
    static zzkh zzm(Class cls, zzkb zzkbVar, zzkk zzkkVar, zzjs zzjsVar, zzll zzllVar, zzif zzifVar, zzjz zzjzVar) {
        int i;
        int iCharAt;
        int i2;
        int i3;
        int i4;
        int[] iArr;
        int i5;
        int i6;
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
        int i19;
        int iObjectFieldOffset;
        int i20;
        int iObjectFieldOffset2;
        int i21;
        int iObjectFieldOffset3;
        Field fieldZzC;
        char cCharAt9;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        Object obj;
        Field fieldZzC2;
        int i27;
        Object obj2;
        Field fieldZzC3;
        int i28;
        char cCharAt10;
        int i29;
        char cCharAt11;
        int i30;
        char cCharAt12;
        int i31;
        char cCharAt13;
        if (!(zzkbVar instanceof zzkp)) {
            throw null;
        }
        zzkp zzkpVar = (zzkp) zzkbVar;
        String strZzd = zzkpVar.zzd();
        int length = strZzd.length();
        char cCharAt14 = strZzd.charAt(0);
        char c = CharacterCompat.MIN_HIGH_SURROGATE;
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
            i4 = 0;
            iCharAt = 0;
            i3 = 0;
            i5 = 0;
            i2 = 0;
            i6 = 0;
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
            i2 = iCharAt3 + iCharAt3 + iCharAt4;
            int[] iArr2 = new int[iCharAt9 + iCharAt7 + iCharAt8];
            int i60 = iCharAt7;
            i3 = iCharAt5;
            i4 = i60;
            iArr = iArr2;
            i5 = iCharAt6;
            i6 = iCharAt9;
            i7 = iCharAt3;
            i33 = i57;
        }
        Unsafe unsafe = zzb;
        Object[] objArrZze = zzkpVar.zze();
        Class<?> cls2 = zzkpVar.zza().getClass();
        int i61 = i6 + i4;
        int i62 = iCharAt + iCharAt;
        int[] iArr3 = new int[iCharAt * 3];
        Object[] objArr = new Object[i62];
        int i63 = 0;
        int i64 = 0;
        int i65 = i6;
        int i66 = i61;
        while (i33 < length) {
            int i67 = i33 + 1;
            int iCharAt10 = strZzd.charAt(i33);
            if (iCharAt10 >= c) {
                int i68 = iCharAt10 & 8191;
                int i69 = i67;
                int i70 = 13;
                while (true) {
                    i30 = i69 + 1;
                    cCharAt12 = strZzd.charAt(i69);
                    if (cCharAt12 < c) {
                        break;
                    }
                    i68 |= (cCharAt12 & 8191) << i70;
                    i70 += 13;
                    i69 = i30;
                }
                iCharAt10 = i68 | (cCharAt12 << i70);
                i16 = i30;
            } else {
                i16 = i67;
            }
            int i71 = i16 + 1;
            int iCharAt11 = strZzd.charAt(i16);
            if (iCharAt11 >= c) {
                int i72 = iCharAt11 & 8191;
                int i73 = i71;
                int i74 = 13;
                while (true) {
                    i29 = i73 + 1;
                    cCharAt11 = strZzd.charAt(i73);
                    if (cCharAt11 < c) {
                        break;
                    }
                    i72 |= (cCharAt11 & 8191) << i74;
                    i74 += 13;
                    i73 = i29;
                }
                iCharAt11 = i72 | (cCharAt11 << i74);
                i17 = i29;
            } else {
                i17 = i71;
            }
            if ((iCharAt11 & 1024) != 0) {
                iArr[i63] = i64;
                i63++;
            }
            int i75 = iCharAt11 & 255;
            int i76 = iCharAt11 & 2048;
            zzkp zzkpVar2 = zzkpVar;
            if (i75 >= 51) {
                int i77 = i17 + 1;
                int iCharAt12 = strZzd.charAt(i17);
                char c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                if (iCharAt12 >= 55296) {
                    int i78 = iCharAt12 & 8191;
                    int i79 = i77;
                    int i80 = 13;
                    while (true) {
                        i28 = i79 + 1;
                        cCharAt10 = strZzd.charAt(i79);
                        if (cCharAt10 < c2) {
                            break;
                        }
                        i78 |= (cCharAt10 & 8191) << i80;
                        i80 += 13;
                        i79 = i28;
                        c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                    }
                    iCharAt12 = i78 | (cCharAt10 << i80);
                    i24 = i28;
                } else {
                    i24 = i77;
                }
                int i81 = i24;
                int i82 = i75 - 51;
                if (i82 == 9 || i82 == 17) {
                    i25 = i2 + 1;
                    int i83 = i64 / 3;
                    objArr[i83 + i83 + 1] = objArrZze[i2];
                } else {
                    if (i82 == 12) {
                        if (zzkpVar2.zzc() == 1 || i76 != 0) {
                            i25 = i2 + 1;
                            int i84 = i64 / 3;
                            objArr[i84 + i84 + 1] = objArrZze[i2];
                        } else {
                            i76 = 0;
                        }
                    }
                    i26 = iCharAt12 + iCharAt12;
                    obj = objArrZze[i26];
                    if (obj instanceof Field) {
                        fieldZzC2 = (Field) obj;
                    } else {
                        fieldZzC2 = zzC(cls2, (String) obj);
                        objArrZze[i26] = fieldZzC2;
                    }
                    int i85 = iCharAt10;
                    int i86 = i76;
                    iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldZzC2);
                    i27 = i26 + 1;
                    obj2 = objArrZze[i27];
                    if (obj2 instanceof Field) {
                        fieldZzC3 = (Field) obj2;
                    } else {
                        fieldZzC3 = zzC(cls2, (String) obj2);
                        objArrZze[i27] = fieldZzC3;
                    }
                    iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzC3);
                    strZzd = strZzd;
                    i20 = i81;
                    i76 = i86;
                    i21 = 0;
                    i18 = i85;
                }
                i2 = i25;
                i26 = iCharAt12 + iCharAt12;
                obj = objArrZze[i26];
                if (obj instanceof Field) {
                    fieldZzC2 = (Field) obj;
                } else {
                    fieldZzC2 = zzC(cls2, (String) obj);
                    objArrZze[i26] = fieldZzC2;
                }
                int i87 = iCharAt10;
                int i88 = i76;
                iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldZzC2);
                i27 = i26 + 1;
                obj2 = objArrZze[i27];
                if (obj2 instanceof Field) {
                    fieldZzC3 = (Field) obj2;
                } else {
                    fieldZzC3 = zzC(cls2, (String) obj2);
                    objArrZze[i27] = fieldZzC3;
                }
                iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzC3);
                strZzd = strZzd;
                i20 = i81;
                i76 = i88;
                i21 = 0;
                i18 = i87;
            } else {
                i18 = iCharAt10;
                int i89 = i2 + 1;
                Field fieldZzC4 = zzC(cls2, (String) objArrZze[i2]);
                if (i75 == 9 || i75 == 17) {
                    int i90 = i64 / 3;
                    objArr[i90 + i90 + 1] = fieldZzC4.getType();
                } else {
                    if (i75 != 27) {
                        if (i75 == 49) {
                            i23 = i2 + 2;
                            i22 = 1;
                        } else if (i75 == 12 || i75 == 30 || i75 == 44) {
                            i18 = i18;
                            if (zzkpVar2.zzc() == 1 || i76 != 0) {
                                i23 = i2 + 2;
                                int i91 = i64 / 3;
                                objArr[i91 + i91 + 1] = objArrZze[i89];
                                i19 = i23;
                            } else {
                                i19 = i89;
                                i76 = 0;
                            }
                        } else if (i75 == 50) {
                            i19 = i2 + 2;
                            int i92 = i65 + 1;
                            iArr[i65] = i64;
                            int i93 = i64 / 3;
                            int i94 = i93 + i93;
                            objArr[i94] = objArrZze[i89];
                            if (i76 != 0) {
                                objArr[i94 + 1] = objArrZze[i19];
                                i19 = i2 + 3;
                                i65 = i92;
                                i18 = i18;
                            } else {
                                i65 = i92;
                                i76 = 0;
                                i18 = i18;
                            }
                        }
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzC4);
                        iCharAt11 = iCharAt11;
                        if ((iCharAt11 & 4096) != 0 || i75 > 17) {
                            i20 = i17;
                            iObjectFieldOffset2 = 1048575;
                            i21 = 0;
                        } else {
                            int i95 = i17 + 1;
                            int iCharAt13 = strZzd.charAt(i17);
                            if (iCharAt13 >= 55296) {
                                int i96 = iCharAt13 & 8191;
                                int i97 = 13;
                                while (true) {
                                    i20 = i95 + 1;
                                    cCharAt9 = strZzd.charAt(i95);
                                    if (cCharAt9 < 55296) {
                                        break;
                                    }
                                    i96 |= (cCharAt9 & 8191) << i97;
                                    i97 += 13;
                                    i95 = i20;
                                }
                                iCharAt13 = i96 | (cCharAt9 << i97);
                            } else {
                                i20 = i95;
                            }
                            int i98 = i7 + i7 + (iCharAt13 / 32);
                            Object obj3 = objArrZze[i98];
                            if (obj3 instanceof Field) {
                                fieldZzC = (Field) obj3;
                            } else {
                                fieldZzC = zzC(cls2, (String) obj3);
                                objArrZze[i98] = fieldZzC;
                            }
                            i21 = iCharAt13 % 32;
                            iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZzC);
                        }
                        if (i75 >= 18 && i75 <= 49) {
                            iArr[i66] = iObjectFieldOffset;
                            i66++;
                        }
                        iObjectFieldOffset3 = iObjectFieldOffset;
                        i2 = i19;
                    } else {
                        i22 = 1;
                        i23 = i2 + 2;
                    }
                    int i99 = i64 / 3;
                    objArr[i99 + i99 + i22] = objArrZze[i89];
                    i19 = i23;
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzC4);
                    iCharAt11 = iCharAt11;
                    if ((iCharAt11 & 4096) != 0) {
                        i20 = i17;
                        iObjectFieldOffset2 = 1048575;
                        i21 = 0;
                    } else {
                        i20 = i17;
                        iObjectFieldOffset2 = 1048575;
                        i21 = 0;
                    }
                    if (i75 >= 18) {
                        iArr[i66] = iObjectFieldOffset;
                        i66++;
                    }
                    iObjectFieldOffset3 = iObjectFieldOffset;
                    i2 = i19;
                }
                i19 = i89;
                iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZzC4);
                iCharAt11 = iCharAt11;
                if ((iCharAt11 & 4096) != 0) {
                    i20 = i17;
                    iObjectFieldOffset2 = 1048575;
                    i21 = 0;
                } else {
                    i20 = i17;
                    iObjectFieldOffset2 = 1048575;
                    i21 = 0;
                }
                if (i75 >= 18) {
                    iArr[i66] = iObjectFieldOffset;
                    i66++;
                }
                iObjectFieldOffset3 = iObjectFieldOffset;
                i2 = i19;
            }
            int i100 = i64 + 1;
            iArr3[i64] = i18;
            int i101 = i64 + 2;
            iArr3[i100] = ((iCharAt11 & 512) != 0 ? 536870912 : 0) | ((iCharAt11 & 256) != 0 ? 268435456 : 0) | (i76 != 0 ? Integer.MIN_VALUE : 0) | (i75 << 20) | iObjectFieldOffset3;
            i64 += 3;
            iArr3[i101] = (i21 << 20) | iObjectFieldOffset2;
            i33 = i20;
            zzkpVar = zzkpVar2;
            strZzd = strZzd;
            length = length;
            c = CharacterCompat.MIN_HIGH_SURROGATE;
        }
        zzkp zzkpVar3 = zzkpVar;
        return new zzkh(iArr3, objArr, i3, i5, zzkpVar3.zza(), zzkpVar3.zzc(), false, iArr, i6, i61, zzkkVar, zzjsVar, zzllVar, zzifVar, zzjzVar);
    }

    private static double zzn(Object obj, long j) {
        return ((Double) zzlv.zzf(obj, j)).doubleValue();
    }

    private static float zzo(Object obj, long j) {
        return ((Float) zzlv.zzf(obj, j)).floatValue();
    }

    private static int zzp(Object obj, long j) {
        return ((Integer) zzlv.zzf(obj, j)).intValue();
    }

    private final int zzq(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzs(i, 0);
    }

    private final int zzr(int i) {
        return this.zzc[i + 2];
    }

    private final int zzs(int i, int i2) {
        int length = (this.zzc.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int i5 = this.zzc[i4];
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

    private static int zzt(int i) {
        return (i >>> 20) & 255;
    }

    private final int zzu(int i) {
        return this.zzc[i + 1];
    }

    private static long zzv(Object obj, long j) {
        return ((Long) zzlv.zzf(obj, j)).longValue();
    }

    private final zzix zzw(int i) {
        int i2 = i / 3;
        return (zzix) this.zzd[i2 + i2 + 1];
    }

    private final zzkr zzx(int i) {
        Object[] objArr = this.zzd;
        int i2 = i / 3;
        int i3 = i2 + i2;
        zzkr zzkrVar = (zzkr) objArr[i3];
        if (zzkrVar != null) {
            return zzkrVar;
        }
        zzkr zzkrVarZzb = zzkn.zza().zzb((Class) objArr[i3 + 1]);
        this.zzd[i3] = zzkrVarZzb;
        return zzkrVarZzb;
    }

    private final Object zzy(Object obj, int i, Object obj2, zzll zzllVar, Object obj3) {
        int i2 = this.zzc[i];
        Object objZzf = zzlv.zzf(obj, zzu(i) & 1048575);
        if (objZzf == null || zzw(i) == null) {
            return obj2;
        }
        throw null;
    }

    private final Object zzz(int i) {
        int i2 = i / 3;
        return this.zzd[i2 + i2];
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:142:0x038d  */
    /* JADX WARN: Code duplicated, block: B:179:0x0484  */
    /* JADX WARN: Code duplicated, block: B:282:0x0724 A[PHI: r0 r1
  0x0724: PHI (r0v2 com.google.android.recaptcha.internal.zzkh<T>) = 
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v33 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v42 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
  (r0v1 com.google.android.recaptcha.internal.zzkh<T>)
 binds: [B:18:0x004f, B:280:0x071a, B:250:0x0654, B:234:0x05ec, B:225:0x05b6, B:218:0x0583, B:138:0x0371, B:135:0x0359, B:132:0x0341, B:129:0x0329, B:126:0x0311, B:123:0x02f9, B:120:0x02e1, B:117:0x02c9, B:114:0x02b0, B:111:0x0299, B:108:0x0282, B:105:0x026b, B:102:0x0254, B:97:0x0238, B:83:0x01e4, B:85:0x01f2, B:80:0x01ca, B:77:0x01bc, B:74:0x01a6, B:71:0x0190, B:68:0x017a, B:65:0x016c, B:62:0x015e, B:59:0x014f, B:53:0x0121, B:50:0x010d, B:46:0x00ed, B:43:0x00d8, B:40:0x00c3, B:36:0x00b4, B:32:0x00a5, B:29:0x008b, B:25:0x0070, B:21:0x0058] A[DONT_GENERATE, DONT_INLINE]
  0x0724: PHI (r1v6 java.lang.Object) = 
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v7 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
  (r1v1 java.lang.Object)
 binds: [B:18:0x004f, B:280:0x071a, B:250:0x0654, B:234:0x05ec, B:225:0x05b6, B:218:0x0583, B:138:0x0371, B:135:0x0359, B:132:0x0341, B:129:0x0329, B:126:0x0311, B:123:0x02f9, B:120:0x02e1, B:117:0x02c9, B:114:0x02b0, B:111:0x0299, B:108:0x0282, B:105:0x026b, B:102:0x0254, B:97:0x0238, B:83:0x01e4, B:85:0x01f2, B:80:0x01ca, B:77:0x01bc, B:74:0x01a6, B:71:0x0190, B:68:0x017a, B:65:0x016c, B:62:0x015e, B:59:0x014f, B:53:0x0121, B:50:0x010d, B:46:0x00ed, B:43:0x00d8, B:40:0x00c3, B:36:0x00b4, B:32:0x00a5, B:29:0x008b, B:25:0x0070, B:21:0x0058] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.google.android.recaptcha.internal.zzkr
    public final int zza(Object obj) {
        int i;
        int iZzy;
        int iZzy2;
        int iZzz;
        int iZzy3;
        int iZzy4;
        int iZzy5;
        int iZzy6;
        int iZzy7;
        int iZzh;
        int i2;
        int iZzg;
        int size;
        int iZzl;
        int iZzy8;
        int iZzy9;
        int iZzy10;
        int iZzz2;
        int iZze;
        int iZzy11;
        int iZzy12;
        int iZzt;
        int iZzy13;
        int iZzy14;
        int iZzy15;
        zzkh<T> zzkhVar = this;
        Object obj2 = obj;
        Unsafe unsafe = zzb;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1048575;
        while (i4 < zzkhVar.zzc.length) {
            int iZzu = zzkhVar.zzu(i4);
            int iZzt2 = zzt(iZzu);
            int[] iArr = zzkhVar.zzc;
            int i8 = iArr[i4];
            int i9 = iArr[i4 + 2];
            int i10 = i9 & i3;
            if (iZzt2 <= 17) {
                if (i10 != i7) {
                    i5 = i10 == i3 ? 0 : unsafe.getInt(obj2, i10);
                    i7 = i10;
                }
                i = 1 << (i9 >>> 20);
            } else {
                i = 0;
            }
            int i11 = iZzu & i3;
            if (iZzt2 >= zzik.zzJ.zza()) {
                zzik.zzW.zza();
            }
            int i12 = i6;
            long j = i11;
            switch (iZzt2) {
                case 0:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy = zzhh.zzy(i8 << 3);
                        iZzh = iZzy + 8;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 1:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy2 = zzhh.zzy(i8 << 3);
                        iZzy5 = iZzy2 + 4;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 2:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzz = zzhh.zzz(unsafe.getLong(obj2, j));
                        iZzy3 = zzhh.zzy(i8 << 3);
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 3:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzz = zzhh.zzz(unsafe.getLong(obj2, j));
                        iZzy3 = zzhh.zzy(i8 << 3);
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 4:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzz = zzhh.zzu(unsafe.getInt(obj2, j));
                        iZzy3 = zzhh.zzy(i8 << 3);
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 5:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy4 = zzhh.zzy(i8 << 3);
                        iZzy5 = iZzy4 + 8;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 6:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy2 = zzhh.zzy(i8 << 3);
                        iZzy5 = iZzy2 + 4;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 7:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy5 = zzhh.zzy(i8 << 3) + 1;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 8:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        int i13 = i8 << 3;
                        Object object = unsafe.getObject(obj2, j);
                        if (object instanceof zzgw) {
                            int i14 = zzhh.zzb;
                            int iZzd = ((zzgw) object).zzd();
                            iZzy6 = zzhh.zzy(iZzd) + iZzd;
                            iZzy7 = zzhh.zzy(i13);
                            i2 = iZzy7 + iZzy6;
                            i6 = i12 + i2;
                            zzkhVar = this;
                            i4 += 3;
                            i3 = 1048575;
                        } else {
                            iZzz = zzhh.zzx((String) object);
                            iZzy3 = zzhh.zzy(i13);
                            i2 = iZzy3 + iZzz;
                            i6 = i12 + i2;
                            zzkhVar = this;
                            i4 += 3;
                            i3 = 1048575;
                        }
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 9:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzh = zzkt.zzh(i8, unsafe.getObject(obj2, j), zzkhVar.zzx(i4));
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 10:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        zzgw zzgwVar = (zzgw) unsafe.getObject(obj2, j);
                        int i15 = zzhh.zzb;
                        int iZzd2 = zzgwVar.zzd();
                        iZzy6 = zzhh.zzy(iZzd2) + iZzd2;
                        iZzy7 = zzhh.zzy(i8 << 3);
                        i2 = iZzy7 + iZzy6;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 11:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzz = zzhh.zzy(unsafe.getInt(obj2, j));
                        iZzy3 = zzhh.zzy(i8 << 3);
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 12:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzz = zzhh.zzu(unsafe.getInt(obj2, j));
                        iZzy3 = zzhh.zzy(i8 << 3);
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 13:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy2 = zzhh.zzy(i8 << 3);
                        iZzy5 = iZzy2 + 4;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 14:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzy4 = zzhh.zzy(i8 << 3);
                        iZzy5 = iZzy4 + 8;
                        i6 = i12 + iZzy5;
                        zzkhVar = this;
                        obj2 = obj;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    obj2 = obj;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 15:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        int i16 = unsafe.getInt(obj2, j);
                        iZzy3 = zzhh.zzy(i8 << 3);
                        iZzz = zzhh.zzy((i16 >> 31) ^ (i16 + i16));
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 16:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        long j2 = unsafe.getLong(obj2, j);
                        iZzy3 = zzhh.zzy(i8 << 3);
                        iZzz = zzhh.zzz((j2 >> 63) ^ (j2 + j2));
                        i2 = iZzy3 + iZzz;
                        i6 = i12 + i2;
                        zzkhVar = this;
                        i4 += 3;
                        i3 = 1048575;
                    }
                    zzkhVar = this;
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 17:
                    if (zzkhVar.zzO(obj2, i4, i7, i5, i)) {
                        iZzh = zzhh.zzt(i8, (zzke) unsafe.getObject(obj2, j), zzkhVar.zzx(i4));
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 18:
                    iZzh = zzkt.zzd(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 19:
                    iZzh = zzkt.zzb(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 20:
                    List list = (List) unsafe.getObject(obj2, j);
                    int i17 = zzkt.zza;
                    if (list.size() == 0) {
                        iZzg = 0;
                    } else {
                        iZzg = zzkt.zzg(list) + (list.size() * zzhh.zzy(i8 << 3));
                    }
                    i6 = iZzg + i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 21:
                    List list2 = (List) unsafe.getObject(obj2, j);
                    int i18 = zzkt.zza;
                    size = list2.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zzl(list2);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 22:
                    List list3 = (List) unsafe.getObject(obj2, j);
                    int i19 = zzkt.zza;
                    size = list3.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zzf(list3);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 23:
                    iZzh = zzkt.zzd(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 24:
                    iZzh = zzkt.zzb(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 25:
                    List list4 = (List) unsafe.getObject(obj2, j);
                    int i20 = zzkt.zza;
                    int size2 = list4.size();
                    if (size2 == 0) {
                        iZzh = 0;
                    } else {
                        iZzh = size2 * (zzhh.zzy(i8 << 3) + 1);
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 26:
                    List list5 = (List) unsafe.getObject(obj2, j);
                    int i21 = zzkt.zza;
                    int size3 = list5.size();
                    if (size3 == 0) {
                        iZzg = 0;
                    } else {
                        boolean z = list5 instanceof zzjm;
                        iZzg = zzhh.zzy(i8 << 3) * size3;
                        if (z) {
                            zzjm zzjmVar = (zzjm) list5;
                            for (int i22 = 0; i22 < size3; i22++) {
                                Object objZzf = zzjmVar.zzf(i22);
                                if (objZzf instanceof zzgw) {
                                    int iZzd3 = ((zzgw) objZzf).zzd();
                                    iZzg += zzhh.zzy(iZzd3) + iZzd3;
                                } else {
                                    iZzg += zzhh.zzx((String) objZzf);
                                }
                            }
                        } else {
                            for (int i23 = 0; i23 < size3; i23++) {
                                Object obj3 = list5.get(i23);
                                if (obj3 instanceof zzgw) {
                                    int iZzd4 = ((zzgw) obj3).zzd();
                                    iZzg += zzhh.zzy(iZzd4) + iZzd4;
                                } else {
                                    iZzg += zzhh.zzx((String) obj3);
                                }
                            }
                        }
                    }
                    i6 = iZzg + i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 27:
                    List list6 = (List) unsafe.getObject(obj2, j);
                    zzkr zzkrVarZzx = zzkhVar.zzx(i4);
                    int i24 = zzkt.zza;
                    int size4 = list6.size();
                    if (size4 == 0) {
                        iZzy9 = 0;
                    } else {
                        iZzy9 = zzhh.zzy(i8 << 3) * size4;
                        for (int i25 = 0; i25 < size4; i25++) {
                            Object obj4 = list6.get(i25);
                            if (obj4 instanceof zzjk) {
                                int iZza = ((zzjk) obj4).zza();
                                iZzy9 += zzhh.zzy(iZza) + iZza;
                            } else {
                                iZzy9 += zzhh.zzw((zzke) obj4, zzkrVarZzx);
                            }
                        }
                    }
                    i6 = i12 + iZzy9;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 28:
                    List list7 = (List) unsafe.getObject(obj2, j);
                    int i26 = zzkt.zza;
                    int size5 = list7.size();
                    if (size5 == 0) {
                        iZzy10 = 0;
                    } else {
                        iZzy10 = size5 * zzhh.zzy(i8 << 3);
                        for (int i27 = 0; i27 < list7.size(); i27++) {
                            int iZzd5 = ((zzgw) list7.get(i27)).zzd();
                            iZzy10 += zzhh.zzy(iZzd5) + iZzd5;
                        }
                    }
                    i6 = i12 + iZzy10;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 29:
                    List list8 = (List) unsafe.getObject(obj2, j);
                    int i28 = zzkt.zza;
                    size = list8.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zzk(list8);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 30:
                    List list9 = (List) unsafe.getObject(obj2, j);
                    int i29 = zzkt.zza;
                    size = list9.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zza(list9);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 31:
                    iZzh = zzkt.zzb(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 32:
                    iZzh = zzkt.zzd(i8, (List) unsafe.getObject(obj2, j), false);
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 33:
                    List list10 = (List) unsafe.getObject(obj2, j);
                    int i30 = zzkt.zza;
                    size = list10.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zzi(list10);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 34:
                    List list11 = (List) unsafe.getObject(obj2, j);
                    int i31 = zzkt.zza;
                    size = list11.size();
                    if (size == 0) {
                        iZzh = 0;
                    } else {
                        iZzl = zzkt.zzj(list11);
                        iZzy8 = zzhh.zzy(i8 << 3);
                        iZzz2 = size * iZzy8;
                        iZzh = iZzl + iZzz2;
                    }
                    i6 = i12 + iZzh;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 35:
                    iZze = zzkt.zze((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 36:
                    iZze = zzkt.zzc((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 37:
                    iZze = zzkt.zzg((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 38:
                    iZze = zzkt.zzl((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 39:
                    iZze = zzkt.zzf((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 40:
                    iZze = zzkt.zze((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 41:
                    iZze = zzkt.zzc((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 42:
                    List list12 = (List) unsafe.getObject(obj2, j);
                    int i32 = zzkt.zza;
                    iZze = list12.size();
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 43:
                    iZze = zzkt.zzk((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 44:
                    iZze = zzkt.zza((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 45:
                    iZze = zzkt.zzc((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 46:
                    iZze = zzkt.zze((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 47:
                    iZze = zzkt.zzi((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 48:
                    iZze = zzkt.zzj((List) unsafe.getObject(obj2, j));
                    if (iZze > 0) {
                        iZzy11 = zzhh.zzy(iZze);
                        iZzy12 = zzhh.zzy(i8 << 3);
                        iZzy10 = iZzy12 + iZzy11 + iZze;
                        i6 = i12 + iZzy10;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 49:
                    List list13 = (List) unsafe.getObject(obj2, j);
                    zzkr zzkrVarZzx2 = zzkhVar.zzx(i4);
                    int i33 = zzkt.zza;
                    int size6 = list13.size();
                    if (size6 == 0) {
                        iZzt = 0;
                    } else {
                        iZzt = 0;
                        for (int i34 = 0; i34 < size6; i34++) {
                            iZzt += zzhh.zzt(i8, (zzke) list13.get(i34), zzkrVarZzx2);
                        }
                    }
                    i6 = i12 + iZzt;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 50:
                    zzjy zzjyVar = (zzjy) unsafe.getObject(obj2, j);
                    if (!zzjyVar.isEmpty()) {
                        Iterator it = zzjyVar.entrySet().iterator();
                        if (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            entry.getKey();
                            entry.getValue();
                            throw null;
                        }
                    }
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 51:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy = zzhh.zzy(i8 << 3);
                        iZzh = iZzy + 8;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 52:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy13 = zzhh.zzy(i8 << 3);
                        iZzh = iZzy13 + 4;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 53:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzz2 = zzhh.zzz(zzv(obj2, j));
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 54:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzz2 = zzhh.zzz(zzv(obj2, j));
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 55:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzz2 = zzhh.zzu(zzp(obj2, j));
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 56:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy = zzhh.zzy(i8 << 3);
                        iZzh = iZzy + 8;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 57:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy13 = zzhh.zzy(i8 << 3);
                        iZzh = iZzy13 + 4;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 58:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzh = zzhh.zzy(i8 << 3) + 1;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 59:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        int i35 = i8 << 3;
                        Object object2 = unsafe.getObject(obj2, j);
                        if (object2 instanceof zzgw) {
                            int i36 = zzhh.zzb;
                            int iZzd6 = ((zzgw) object2).zzd();
                            iZzy14 = zzhh.zzy(iZzd6) + iZzd6;
                            iZzy15 = zzhh.zzy(i35);
                            iZzh = iZzy15 + iZzy14;
                            i6 = i12 + iZzh;
                        } else {
                            iZzz2 = zzhh.zzx((String) object2);
                            iZzl = zzhh.zzy(i35);
                            iZzh = iZzl + iZzz2;
                            i6 = i12 + iZzh;
                        }
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 60:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzh = zzkt.zzh(i8, unsafe.getObject(obj2, j), zzkhVar.zzx(i4));
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 61:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        zzgw zzgwVar2 = (zzgw) unsafe.getObject(obj2, j);
                        int i37 = zzhh.zzb;
                        int iZzd7 = zzgwVar2.zzd();
                        iZzy14 = zzhh.zzy(iZzd7) + iZzd7;
                        iZzy15 = zzhh.zzy(i8 << 3);
                        iZzh = iZzy15 + iZzy14;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 62:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzz2 = zzhh.zzy(zzp(obj2, j));
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 63:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzz2 = zzhh.zzu(zzp(obj2, j));
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 64:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy13 = zzhh.zzy(i8 << 3);
                        iZzh = iZzy13 + 4;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 65:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzy = zzhh.zzy(i8 << 3);
                        iZzh = iZzy + 8;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 66:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        int iZzp = zzp(obj2, j);
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzz2 = zzhh.zzy((iZzp >> 31) ^ (iZzp + iZzp));
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 67:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        long jZzv = zzv(obj2, j);
                        iZzl = zzhh.zzy(i8 << 3);
                        iZzz2 = zzhh.zzz((jZzv >> 63) ^ (jZzv + jZzv));
                        iZzh = iZzl + iZzz2;
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                case 68:
                    if (zzkhVar.zzR(obj2, i8, i4)) {
                        iZzh = zzhh.zzt(i8, (zzke) unsafe.getObject(obj2, j), zzkhVar.zzx(i4));
                        i6 = i12 + iZzh;
                    } else {
                        i6 = i12;
                    }
                    i4 += 3;
                    i3 = 1048575;
                    break;
                default:
                    i6 = i12;
                    i4 += 3;
                    i3 = 1048575;
                    break;
            }
        }
        zzll zzllVar = zzkhVar.zzn;
        int iZza2 = i6 + zzllVar.zza(zzllVar.zzd(obj2));
        if (!zzkhVar.zzh) {
            return iZza2;
        }
        zzij zzijVarZzb = zzkhVar.zzo.zzb(obj2);
        int iZza3 = 0;
        for (int i38 = 0; i38 < zzijVarZzb.zza.zzb(); i38++) {
            Map.Entry entryZzg = zzijVarZzb.zza.zzg(i38);
            iZza3 += zzij.zza((zzii) entryZzg.getKey(), entryZzg.getValue());
        }
        for (Map.Entry entry2 : zzijVarZzb.zza.zzc()) {
            iZza3 += zzij.zza((zzii) entry2.getKey(), entry2.getValue());
        }
        return iZza2 + iZza3;
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final int zzb(Object obj) {
        int i;
        long jDoubleToLongBits;
        int iFloatToIntBits;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < this.zzc.length; i4 += 3) {
            int iZzu = zzu(i4);
            int[] iArr = this.zzc;
            int i5 = 1048575 & iZzu;
            int iZzt = zzt(iZzu);
            int i6 = iArr[i4];
            long j = i5;
            int iHashCode = 37;
            switch (iZzt) {
                case 0:
                    i = i3 * 53;
                    jDoubleToLongBits = Double.doubleToLongBits(zzlv.zza(obj, j));
                    byte[] bArr = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 1:
                    i = i3 * 53;
                    iFloatToIntBits = Float.floatToIntBits(zzlv.zzb(obj, j));
                    i3 = i + iFloatToIntBits;
                    break;
                case 2:
                    i = i3 * 53;
                    jDoubleToLongBits = zzlv.zzd(obj, j);
                    byte[] bArr2 = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 3:
                    i = i3 * 53;
                    jDoubleToLongBits = zzlv.zzd(obj, j);
                    byte[] bArr3 = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 4:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 5:
                    i = i3 * 53;
                    jDoubleToLongBits = zzlv.zzd(obj, j);
                    byte[] bArr4 = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 6:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 7:
                    i = i3 * 53;
                    iFloatToIntBits = zzjc.zza(zzlv.zzw(obj, j));
                    i3 = i + iFloatToIntBits;
                    break;
                case 8:
                    i = i3 * 53;
                    iFloatToIntBits = ((String) zzlv.zzf(obj, j)).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 9:
                    i2 = i3 * 53;
                    Object objZzf = zzlv.zzf(obj, j);
                    if (objZzf != null) {
                        iHashCode = objZzf.hashCode();
                    }
                    i3 = i2 + iHashCode;
                    break;
                case 10:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 11:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 12:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 13:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 14:
                    i = i3 * 53;
                    jDoubleToLongBits = zzlv.zzd(obj, j);
                    byte[] bArr5 = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 15:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzc(obj, j);
                    i3 = i + iFloatToIntBits;
                    break;
                case 16:
                    i = i3 * 53;
                    jDoubleToLongBits = zzlv.zzd(obj, j);
                    byte[] bArr6 = zzjc.zzd;
                    iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                    i3 = i + iFloatToIntBits;
                    break;
                case 17:
                    i2 = i3 * 53;
                    Object objZzf2 = zzlv.zzf(obj, j);
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
                    iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 50:
                    i = i3 * 53;
                    iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                    i3 = i + iFloatToIntBits;
                    break;
                case 51:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = Double.doubleToLongBits(zzn(obj, j));
                        byte[] bArr7 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 52:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = Float.floatToIntBits(zzo(obj, j));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 53:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzv(obj, j);
                        byte[] bArr8 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 54:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzv(obj, j);
                        byte[] bArr9 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 55:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 56:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzv(obj, j);
                        byte[] bArr10 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 57:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 58:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzjc.zza(zzS(obj, j));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 59:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = ((String) zzlv.zzf(obj, j)).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 60:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 61:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 62:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 63:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 64:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 65:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzv(obj, j);
                        byte[] bArr11 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 66:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzp(obj, j);
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 67:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        jDoubleToLongBits = zzv(obj, j);
                        byte[] bArr12 = zzjc.zzd;
                        iFloatToIntBits = (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
                        i3 = i + iFloatToIntBits;
                    }
                    break;
                case 68:
                    if (zzR(obj, i6, i4)) {
                        i = i3 * 53;
                        iFloatToIntBits = zzlv.zzf(obj, j).hashCode();
                        i3 = i + iFloatToIntBits;
                    }
                    break;
            }
        }
        int iHashCode2 = (i3 * 53) + this.zzn.zzd(obj).hashCode();
        return this.zzh ? (iHashCode2 * 53) + this.zzo.zzb(obj).zza.hashCode() : iHashCode2;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 36301. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    final int zzc(java.lang.Object r30, byte[] r31, int r32, int r33, int r34, com.google.android.recaptcha.internal.zzgj r35) throws com.google.android.recaptcha.internal.zzje {
        /*
            Method dump skipped, instruction units count: 3630
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.recaptcha.internal.zzkh.zzc(java.lang.Object, byte[], int, int, int, com.google.android.recaptcha.internal.zzgj):int");
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final Object zze() {
        return ((zzit) this.zzg).zzs();
    }

    /* JADX WARN: Code duplicated, block: B:26:0x006d  */
    /* JADX WARN: Code duplicated, block: B:28:0x0073  */
    /* JADX WARN: Code duplicated, block: B:41:0x0080 A[SYNTHETIC] */
    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzf(Object obj) {
        if (zzQ(obj)) {
            if (obj instanceof zzit) {
                zzit zzitVar = (zzit) obj;
                zzitVar.zzE(Integer.MAX_VALUE);
                zzitVar.zza = 0;
                zzitVar.zzC();
            }
            int[] iArr = this.zzc;
            for (int i = 0; i < iArr.length; i += 3) {
                int iZzu = zzu(i);
                int i2 = 1048575 & iZzu;
                int iZzt = zzt(iZzu);
                long j = i2;
                if (iZzt != 9) {
                    if (iZzt != 60 && iZzt != 68) {
                        switch (iZzt) {
                            case 17:
                                if (zzN(obj, i)) {
                                    zzx(i).zzf(zzb.getObject(obj, j));
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
                                this.zzm.zzb(obj, j);
                                break;
                            case 50:
                                Unsafe unsafe = zzb;
                                Object object = unsafe.getObject(obj, j);
                                if (object != null) {
                                    ((zzjy) object).zzc();
                                    unsafe.putObject(obj, j, object);
                                }
                                break;
                        }
                    } else if (zzR(obj, this.zzc[i], i)) {
                        zzx(i).zzf(zzb.getObject(obj, j));
                    }
                } else if (zzN(obj, i)) {
                    zzx(i).zzf(zzb.getObject(obj, j));
                }
            }
            this.zzn.zzm(obj);
            if (this.zzh) {
                this.zzo.zzf(obj);
            }
        }
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzg(Object obj, Object obj2) {
        zzD(obj);
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzu = zzu(i);
            int i2 = 1048575 & iZzu;
            int[] iArr = this.zzc;
            int iZzt = zzt(iZzu);
            int i3 = iArr[i];
            long j = i2;
            switch (iZzt) {
                case 0:
                    if (zzN(obj2, i)) {
                        zzlv.zzo(obj, j, zzlv.zza(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 1:
                    if (zzN(obj2, i)) {
                        zzlv.zzp(obj, j, zzlv.zzb(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 2:
                    if (zzN(obj2, i)) {
                        zzlv.zzr(obj, j, zzlv.zzd(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 3:
                    if (zzN(obj2, i)) {
                        zzlv.zzr(obj, j, zzlv.zzd(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 4:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 5:
                    if (zzN(obj2, i)) {
                        zzlv.zzr(obj, j, zzlv.zzd(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 6:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 7:
                    if (zzN(obj2, i)) {
                        zzlv.zzm(obj, j, zzlv.zzw(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 8:
                    if (zzN(obj2, i)) {
                        zzlv.zzs(obj, j, zzlv.zzf(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 9:
                    zzE(obj, obj2, i);
                    break;
                case 10:
                    if (zzN(obj2, i)) {
                        zzlv.zzs(obj, j, zzlv.zzf(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 11:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 12:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 13:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 14:
                    if (zzN(obj2, i)) {
                        zzlv.zzr(obj, j, zzlv.zzd(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 15:
                    if (zzN(obj2, i)) {
                        zzlv.zzq(obj, j, zzlv.zzc(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 16:
                    if (zzN(obj2, i)) {
                        zzlv.zzr(obj, j, zzlv.zzd(obj2, j));
                        zzH(obj, i);
                    }
                    break;
                case 17:
                    zzE(obj, obj2, i);
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
                    this.zzm.zzc(obj, obj2, j);
                    break;
                case 50:
                    int i4 = zzkt.zza;
                    zzlv.zzs(obj, j, zzjz.zzb(zzlv.zzf(obj, j), zzlv.zzf(obj2, j)));
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
                    if (zzR(obj2, i3, i)) {
                        zzlv.zzs(obj, j, zzlv.zzf(obj2, j));
                        zzI(obj, i3, i);
                    }
                    break;
                case 60:
                    zzF(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (zzR(obj2, i3, i)) {
                        zzlv.zzs(obj, j, zzlv.zzf(obj2, j));
                        zzI(obj, i3, i);
                    }
                    break;
                case 68:
                    zzF(obj, obj2, i);
                    break;
            }
        }
        zzkt.zzr(this.zzn, obj, obj2);
        if (this.zzh) {
            zzkt.zzq(this.zzo, obj, obj2);
        }
    }

    /* JADX WARN: Code duplicated, block: B:195:0x07e4 A[Catch: all -> 0x07ea, TRY_LEAVE, TryCatch #7 {all -> 0x07ea, blocks: (B:193:0x07df, B:195:0x07e4), top: B:234:0x07df }] */
    /* JADX WARN: Code duplicated, block: B:205:0x07fa A[LOOP:1: B:203:0x07f6->B:205:0x07fa, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:208:0x080e  */
    /* JADX WARN: Code duplicated, block: B:210:0x0812  */
    /* JADX WARN: Code duplicated, block: B:219:0x0827 A[LOOP:2: B:217:0x0823->B:219:0x0827, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:222:0x0839  */
    /* JADX WARN: Code duplicated, block: B:260:0x07f4 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:344:? A[RETURN, SYNTHETIC] */
    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzh(Object obj, zzkq zzkqVar, zzie zzieVar) throws Throwable {
        Object obj2;
        Object objZzc;
        int i;
        Object obj3;
        Object obj4;
        Object obj5;
        zzkh<T> zzkhVar;
        Object obj6;
        Object obj7;
        zzll zzllVar;
        zzif zzifVar;
        zzie zzieVar2;
        zzll zzllVar2;
        zzll zzllVar3;
        int i2;
        Object objZzo;
        zzll zzllVar4;
        zzkh<T> zzkhVar2 = this;
        zzie zzieVar3 = zzieVar;
        zzieVar3.getClass();
        zzD(obj);
        zzll zzllVar5 = zzkhVar2.zzn;
        zzif zzifVar2 = zzkhVar2.zzo;
        Object objZzc2 = null;
        zzij zzijVarZzc = null;
        while (true) {
            try {
                int iZzc = zzkqVar.zzc();
                int iZzq = zzkhVar2.zzq(iZzc);
                if (iZzq >= 0) {
                    zzifVar = zzifVar2;
                    zzieVar2 = zzieVar3;
                    zzllVar = zzllVar5;
                    obj5 = objZzc2;
                    obj7 = obj;
                    try {
                        int iZzu = zzkhVar2.zzu(iZzq);
                        try {
                            try {
                                switch (zzt(iZzu)) {
                                    case 0:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzo(obj7, iZzu & 1048575, zzkqVar.zza());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 1:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzp(obj7, iZzu & 1048575, zzkqVar.zzb());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 2:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzr(obj7, iZzu & 1048575, zzkqVar.zzl());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 3:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzr(obj7, iZzu & 1048575, zzkqVar.zzo());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 4:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzq(obj7, iZzu & 1048575, zzkqVar.zzg());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 5:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzr(obj7, iZzu & 1048575, zzkqVar.zzk());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 6:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzq(obj7, iZzu & 1048575, zzkqVar.zzf());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 7:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzm(obj7, iZzu & 1048575, zzkqVar.zzN());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 8:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkhVar.zzG(obj7, iZzu, zzkqVar);
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 9:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzke zzkeVar = (zzke) zzkhVar.zzA(obj7, iZzq);
                                        zzkqVar.zzu(zzkeVar, zzkhVar.zzx(iZzq), zzieVar2);
                                        zzkhVar.zzJ(obj7, iZzq, zzkeVar);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 10:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, zzkqVar.zzp());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 11:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzq(obj7, iZzu & 1048575, zzkqVar.zzj());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 12:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        int iZze = zzkqVar.zze();
                                        zzix zzixVarZzw = zzkhVar.zzw(iZzq);
                                        if (zzixVarZzw == null || zzixVarZzw.zza(iZze)) {
                                            zzlv.zzq(obj7, iZzu & 1048575, iZze);
                                            zzkhVar.zzH(obj7, iZzq);
                                        } else {
                                            objZzc2 = zzkt.zzp(obj7, iZzc, iZze, objZzc2, zzllVar5);
                                        }
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 13:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzq(obj7, iZzu & 1048575, zzkqVar.zzh());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 14:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzr(obj7, iZzu & 1048575, zzkqVar.zzm());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 15:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzq(obj7, iZzu & 1048575, zzkqVar.zzi());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 16:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzlv.zzr(obj7, iZzu & 1048575, zzkqVar.zzn());
                                        zzkhVar.zzH(obj7, iZzq);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 17:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzke zzkeVar2 = (zzke) zzkhVar.zzA(obj7, iZzq);
                                        zzkqVar.zzt(zzkeVar2, zzkhVar.zzx(iZzq), zzieVar2);
                                        zzkhVar.zzJ(obj7, iZzq, zzkeVar2);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 18:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzx(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 19:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzB(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 20:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzE(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 21:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzM(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 22:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzD(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 23:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzA(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 24:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzz(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 25:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzv(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 26:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        if (zzM(iZzu)) {
                                            ((zzhd) zzkqVar).zzK(zzkhVar.zzm.zza(obj7, iZzu & 1048575), true);
                                        } else {
                                            ((zzhd) zzkqVar).zzK(zzkhVar.zzm.zza(obj7, iZzu & 1048575), false);
                                        }
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 27:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzF(zzkhVar.zzm.zza(obj7, iZzu & 1048575), zzkhVar.zzx(iZzq), zzieVar2);
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 28:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzw(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 29:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzL(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 30:
                                        zzkhVar = zzkhVar2;
                                        List listZza = zzkhVar.zzm.zza(obj7, iZzu & 1048575);
                                        zzkqVar.zzy(listZza);
                                        objZzo = zzkt.zzo(obj7, iZzc, listZza, zzkhVar.zzw(iZzq), obj5, zzllVar);
                                        zzllVar5 = zzllVar;
                                        objZzc2 = objZzo;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 31:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzG(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 32:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzH(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 33:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzI(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 34:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzJ(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 35:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzx(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 36:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzB(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 37:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzE(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 38:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzM(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 39:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzD(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 40:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzA(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 41:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzz(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 42:
                                        zzkhVar = zzkhVar2;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        zzkqVar.zzv(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 43:
                                        zzkhVar = zzkhVar2;
                                        obj6 = obj7;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar;
                                        try {
                                            zzkqVar.zzL(zzkhVar.zzm.zza(obj6, iZzu & 1048575));
                                        } catch (zzjd unused) {
                                            try {
                                                zzllVar5.zzs(zzkqVar);
                                                if (objZzc2 == null) {
                                                    objZzc2 = zzllVar5.zzc(obj6);
                                                }
                                                objZzc = objZzc2;
                                                try {
                                                    if (zzllVar5.zzr(objZzc, zzkqVar)) {
                                                        i2 = zzkhVar.zzk;
                                                        while (i2 < zzkhVar.zzl) {
                                                            Object obj8 = obj6;
                                                            zzkhVar.zzy(obj8, zzkhVar.zzj[i2], objZzc, zzllVar5, obj);
                                                            i2++;
                                                            obj6 = obj8;
                                                        }
                                                        obj2 = obj6;
                                                        obj4 = objZzc;
                                                        if (obj4 != null) {
                                                            zzllVar5.zzn(obj2, obj4);
                                                        }
                                                    }
                                                    objZzc2 = objZzc;
                                                } catch (Throwable th) {
                                                    th = th;
                                                    obj2 = obj6;
                                                    zzkhVar2 = zzkhVar;
                                                    i = zzkhVar2.zzk;
                                                    while (i < zzkhVar2.zzl) {
                                                        zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                                                        i++;
                                                        zzkhVar2 = this;
                                                    }
                                                    obj3 = obj2;
                                                    if (objZzc != null) {
                                                        zzllVar5.zzn(obj3, objZzc);
                                                    }
                                                    throw th;
                                                }
                                            } catch (Throwable th2) {
                                                th = th2;
                                                obj2 = obj6;
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                            obj2 = obj6;
                                            zzkhVar2 = zzkhVar;
                                            objZzc = objZzc2;
                                            i = zzkhVar2.zzk;
                                            while (i < zzkhVar2.zzl) {
                                                zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                                                i++;
                                                zzkhVar2 = this;
                                            }
                                            obj3 = obj2;
                                            if (objZzc != null) {
                                                zzllVar5.zzn(obj3, objZzc);
                                            }
                                            throw th;
                                        }
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 44:
                                        zzkhVar = zzkhVar2;
                                        obj6 = obj7;
                                        try {
                                            List listZza2 = zzkhVar.zzm.zza(obj6, iZzu & 1048575);
                                            zzkqVar.zzy(listZza2);
                                            objZzo = zzkt.zzo(obj6, iZzc, listZza2, zzkhVar.zzw(iZzq), obj5, zzllVar);
                                            zzllVar5 = zzllVar;
                                            objZzc2 = objZzo;
                                            zzkhVar2 = zzkhVar;
                                            zzieVar3 = zzieVar2;
                                            zzifVar2 = zzifVar;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            zzllVar2 = zzllVar;
                                            objZzc2 = obj5;
                                            zzllVar5 = zzllVar2;
                                            obj2 = obj6;
                                            zzkhVar2 = zzkhVar;
                                            objZzc = objZzc2;
                                            i = zzkhVar2.zzk;
                                            while (i < zzkhVar2.zzl) {
                                                zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                                                i++;
                                                zzkhVar2 = this;
                                            }
                                            obj3 = obj2;
                                            if (objZzc != null) {
                                                zzllVar5.zzn(obj3, objZzc);
                                            }
                                            throw th;
                                        }
                                        break;
                                    case 45:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkqVar.zzG(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzll zzllVar6 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar6;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 46:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkqVar.zzH(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzll zzllVar7 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar7;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 47:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkqVar.zzI(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzll zzllVar8 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar8;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 48:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkqVar.zzJ(zzkhVar.zzm.zza(obj7, iZzu & 1048575));
                                        zzll zzllVar9 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar9;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 49:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkqVar.zzC(zzkhVar.zzm.zza(obj7, iZzu & 1048575), zzkhVar.zzx(iZzq), zzieVar2);
                                        zzll zzllVar10 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar10;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 50:
                                        zzkhVar = zzkhVar2;
                                        obj6 = obj7;
                                        zzllVar4 = zzllVar;
                                        Object objZzz = zzkhVar.zzz(iZzq);
                                        long jZzu = zzkhVar.zzu(iZzq) & 1048575;
                                        Object objZzf = zzlv.zzf(obj6, jZzu);
                                        if (objZzf == null) {
                                            objZzf = zzjy.zza().zzb();
                                            zzlv.zzs(obj6, jZzu, objZzf);
                                        } else if (zzjz.zza(objZzf)) {
                                            Object objZzb = zzjy.zza().zzb();
                                            zzjz.zzb(objZzb, objZzf);
                                            zzlv.zzs(obj6, jZzu, objZzb);
                                            objZzf = objZzb;
                                        }
                                        throw null;
                                    case 51:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Double.valueOf(zzkqVar.zza()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar11 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar11;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 52:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Float.valueOf(zzkqVar.zzb()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar12 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar12;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 53:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Long.valueOf(zzkqVar.zzl()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar13 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar13;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 54:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Long.valueOf(zzkqVar.zzo()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar14 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar14;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 55:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(zzkqVar.zzg()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar15 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar15;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 56:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Long.valueOf(zzkqVar.zzk()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar16 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar16;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 57:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(zzkqVar.zzf()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar17 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar17;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 58:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Boolean.valueOf(zzkqVar.zzN()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar18 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar18;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 59:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzkhVar.zzG(obj7, iZzu, zzkqVar);
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar19 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar19;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 60:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzke zzkeVar3 = (zzke) zzkhVar.zzB(obj7, iZzc, iZzq);
                                        zzkqVar.zzu(zzkeVar3, zzkhVar.zzx(iZzq), zzieVar2);
                                        zzkhVar.zzK(obj7, iZzc, iZzq, zzkeVar3);
                                        zzll zzllVar110 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar110;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 61:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, zzkqVar.zzp());
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar111 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar111;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 62:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(zzkqVar.zzj()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar112 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar112;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 63:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        int iZze2 = zzkqVar.zze();
                                        zzix zzixVarZzw2 = zzkhVar.zzw(iZzq);
                                        if (zzixVarZzw2 == null || zzixVarZzw2.zza(iZze2)) {
                                            zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(iZze2));
                                            zzkhVar.zzI(obj7, iZzc, iZzq);
                                            zzll zzllVar113 = zzllVar4;
                                            objZzc2 = obj5;
                                            zzllVar5 = zzllVar113;
                                            zzkhVar2 = zzkhVar;
                                            zzieVar3 = zzieVar2;
                                            zzifVar2 = zzifVar;
                                        } else {
                                            Object objZzp = zzkt.zzp(obj7, iZzc, iZze2, obj5, zzllVar4);
                                            zzllVar5 = zzllVar4;
                                            zzkhVar2 = zzkhVar;
                                            zzieVar3 = zzieVar2;
                                            zzifVar2 = zzifVar;
                                            objZzc2 = objZzp;
                                        }
                                        break;
                                    case 64:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(zzkqVar.zzh()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar114 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar114;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 65:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Long.valueOf(zzkqVar.zzm()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar115 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar115;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 66:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Integer.valueOf(zzkqVar.zzi()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar116 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar116;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 67:
                                        zzkhVar = zzkhVar2;
                                        zzllVar4 = zzllVar;
                                        zzlv.zzs(obj7, iZzu & 1048575, Long.valueOf(zzkqVar.zzn()));
                                        zzkhVar.zzI(obj7, iZzc, iZzq);
                                        zzll zzllVar117 = zzllVar4;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar117;
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    case 68:
                                        zzkhVar = zzkhVar2;
                                        obj6 = obj7;
                                        zzllVar4 = zzllVar;
                                        try {
                                            zzke zzkeVar4 = (zzke) zzkhVar.zzB(obj6, iZzc, iZzq);
                                            zzkqVar.zzt(zzkeVar4, zzkhVar.zzx(iZzq), zzieVar2);
                                            zzkhVar.zzK(obj6, iZzc, iZzq, zzkeVar4);
                                            zzll zzllVar118 = zzllVar4;
                                            objZzc2 = obj5;
                                            zzllVar5 = zzllVar118;
                                        } catch (zzjd unused2) {
                                            zzllVar3 = zzllVar4;
                                            objZzc2 = obj5;
                                            zzllVar5 = zzllVar3;
                                            zzllVar5.zzs(zzkqVar);
                                            if (objZzc2 == null) {
                                                objZzc2 = zzllVar5.zzc(obj6);
                                            }
                                            objZzc = objZzc2;
                                            if (zzllVar5.zzr(objZzc, zzkqVar)) {
                                                i2 = zzkhVar.zzk;
                                                while (i2 < zzkhVar.zzl) {
                                                    Object obj9 = obj6;
                                                    zzkhVar.zzy(obj9, zzkhVar.zzj[i2], objZzc, zzllVar5, obj);
                                                    i2++;
                                                    obj6 = obj9;
                                                }
                                                obj2 = obj6;
                                                obj4 = objZzc;
                                                if (obj4 != null) {
                                                    zzllVar5.zzn(obj2, obj4);
                                                }
                                            }
                                            objZzc2 = objZzc;
                                        }
                                        zzkhVar2 = zzkhVar;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                        break;
                                    default:
                                        objZzc = obj5 == null ? zzllVar.zzc(obj7) : obj5;
                                        try {
                                            if (!zzllVar.zzr(objZzc, zzkqVar)) {
                                                int i3 = zzkhVar2.zzk;
                                                while (i3 < zzkhVar2.zzl) {
                                                    zzll zzllVar20 = zzllVar;
                                                    zzkhVar2.zzy(obj, zzkhVar2.zzj[i3], objZzc, zzllVar20, obj);
                                                    i3++;
                                                    obj7 = obj;
                                                    zzkhVar2 = zzkhVar2;
                                                    zzllVar = zzllVar20;
                                                }
                                                zzll zzllVar21 = zzllVar;
                                                zzkhVar = zzkhVar2;
                                                obj2 = obj7;
                                                zzllVar5 = zzllVar21;
                                                obj4 = objZzc;
                                            } else {
                                                zzllVar5 = zzllVar;
                                                zzkhVar2 = zzkhVar2;
                                                zzifVar2 = zzifVar;
                                                objZzc2 = objZzc;
                                                zzieVar3 = zzieVar2;
                                            }
                                        } catch (zzjd unused3) {
                                            zzkhVar = zzkhVar2;
                                            obj6 = obj7;
                                            zzllVar5 = zzllVar;
                                            objZzc2 = objZzc;
                                            zzllVar5.zzs(zzkqVar);
                                            if (objZzc2 == null) {
                                                objZzc2 = zzllVar5.zzc(obj6);
                                            }
                                            objZzc = objZzc2;
                                            if (zzllVar5.zzr(objZzc, zzkqVar)) {
                                                objZzc2 = objZzc;
                                                zzkhVar2 = zzkhVar;
                                                zzieVar3 = zzieVar2;
                                                zzifVar2 = zzifVar;
                                            } else {
                                                i2 = zzkhVar.zzk;
                                                while (i2 < zzkhVar.zzl) {
                                                    Object obj10 = obj6;
                                                    zzkhVar.zzy(obj10, zzkhVar.zzj[i2], objZzc, zzllVar5, obj);
                                                    i2++;
                                                    obj6 = obj10;
                                                }
                                                obj2 = obj6;
                                            }
                                        } catch (Throwable th5) {
                                            th = th5;
                                            zzkhVar = zzkhVar2;
                                            obj2 = obj7;
                                            zzllVar5 = zzllVar;
                                            zzkhVar2 = zzkhVar;
                                            i = zzkhVar2.zzk;
                                            while (i < zzkhVar2.zzl) {
                                                zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                                                i++;
                                                zzkhVar2 = this;
                                            }
                                            obj3 = obj2;
                                            if (objZzc != null) {
                                                zzllVar5.zzn(obj3, objZzc);
                                            }
                                            throw th;
                                        }
                                        break;
                                }
                            } catch (zzjd unused4) {
                                zzllVar3 = zzllVar;
                                zzkhVar = zzkhVar2;
                                obj6 = obj7;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            zzllVar2 = zzllVar;
                            zzkhVar = zzkhVar2;
                            obj6 = obj7;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        obj2 = obj7;
                        objZzc2 = obj5;
                        zzllVar5 = zzllVar;
                        objZzc = objZzc2;
                    }
                } else if (iZzc == Integer.MAX_VALUE) {
                    int i4 = zzkhVar2.zzk;
                    while (i4 < zzkhVar2.zzl) {
                        Object obj11 = objZzc2;
                        zzkhVar2.zzy(obj, zzkhVar2.zzj[i4], obj11, zzllVar5, obj);
                        i4++;
                        objZzc2 = obj11;
                        zzllVar5 = zzllVar5;
                        zzkhVar2 = zzkhVar2;
                    }
                    obj4 = objZzc2;
                    zzllVar5 = zzllVar5;
                    obj2 = obj;
                } else {
                    Object obj12 = objZzc2;
                    zzll zzllVar22 = zzllVar5;
                    obj5 = obj12;
                    zzkhVar = zzkhVar2;
                    obj6 = obj;
                    try {
                        Object objZzd = !zzkhVar.zzh ? null : zzifVar2.zzd(zzieVar3, zzkhVar.zzg, iZzc);
                        if (objZzd != null) {
                            if (zzijVarZzc == null) {
                                try {
                                    zzijVarZzc = zzifVar2.zzc(obj6);
                                } catch (Throwable th8) {
                                    th = th8;
                                    objZzc2 = obj5;
                                    zzllVar5 = zzllVar22;
                                    obj2 = obj6;
                                    zzkhVar2 = zzkhVar;
                                    objZzc = objZzc2;
                                    i = zzkhVar2.zzk;
                                    while (i < zzkhVar2.zzl) {
                                        zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                                        i++;
                                        zzkhVar2 = this;
                                    }
                                    obj3 = obj2;
                                    if (objZzc != null) {
                                        zzllVar5.zzn(obj3, objZzc);
                                    }
                                    throw th;
                                }
                            }
                            zzij zzijVar = zzijVarZzc;
                            try {
                                zzifVar2.zze(obj6, zzkqVar, objZzd, zzieVar3, zzijVar, obj5, zzllVar22);
                                zzijVarZzc = zzijVar;
                                zzifVar = zzifVar2;
                                zzieVar2 = zzieVar3;
                                objZzc2 = obj5;
                                zzllVar5 = zzllVar22;
                                zzkhVar2 = zzkhVar;
                                zzieVar3 = zzieVar2;
                                zzifVar2 = zzifVar;
                            } catch (Throwable th9) {
                                th = th9;
                                obj2 = obj6;
                                obj5 = obj5;
                                zzllVar22 = zzllVar22;
                            }
                        } else {
                            zzifVar = zzifVar2;
                            obj2 = obj6;
                            zzieVar2 = zzieVar3;
                            try {
                                zzllVar22.zzs(zzkqVar);
                                if (obj5 == null) {
                                    try {
                                        objZzc = zzllVar22.zzc(obj2);
                                    } catch (Throwable th10) {
                                        th = th10;
                                        zzll zzllVar23 = zzllVar22;
                                        objZzc2 = obj5;
                                        zzllVar5 = zzllVar23;
                                        zzkhVar2 = zzkhVar;
                                        objZzc = objZzc2;
                                    }
                                } else {
                                    objZzc = obj5;
                                }
                                try {
                                    if (zzllVar22.zzr(objZzc, zzkqVar)) {
                                        zzkhVar2 = zzkhVar;
                                        objZzc2 = objZzc;
                                        zzllVar5 = zzllVar22;
                                        zzieVar3 = zzieVar2;
                                        zzifVar2 = zzifVar;
                                    } else {
                                        int i5 = zzkhVar.zzk;
                                        while (i5 < zzkhVar.zzl) {
                                            zzll zzllVar24 = zzllVar22;
                                            zzkh<T> zzkhVar3 = zzkhVar;
                                            zzkhVar3.zzy(obj2, zzkhVar.zzj[i5], objZzc, zzllVar24, obj);
                                            i5++;
                                            zzllVar22 = zzllVar24;
                                            zzkhVar = zzkhVar3;
                                        }
                                        zzll zzllVar25 = zzllVar22;
                                        obj4 = objZzc;
                                        zzllVar5 = zzllVar25;
                                    }
                                } catch (Throwable th11) {
                                    th = th11;
                                    zzkhVar2 = zzkhVar;
                                    zzllVar5 = zzllVar22;
                                }
                            } catch (Throwable th12) {
                                th = th12;
                                zzkhVar2 = zzkhVar;
                                zzllVar = zzllVar22;
                                objZzc2 = obj5;
                                zzllVar5 = zzllVar;
                                objZzc = objZzc2;
                            }
                        }
                        zzll zzllVar26 = zzllVar22;
                        objZzc2 = obj5;
                        zzllVar5 = zzllVar26;
                    } catch (Throwable th13) {
                        th = th13;
                        obj7 = obj6;
                        zzkhVar2 = zzkhVar;
                        zzllVar = zzllVar22;
                        obj2 = obj7;
                        objZzc2 = obj5;
                        zzllVar5 = zzllVar;
                        objZzc = objZzc2;
                        i = zzkhVar2.zzk;
                        while (i < zzkhVar2.zzl) {
                            zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                            i++;
                            zzkhVar2 = this;
                        }
                        obj3 = obj2;
                        if (objZzc != null) {
                            zzllVar5.zzn(obj3, objZzc);
                        }
                        throw th;
                    }
                    zzkhVar2 = zzkhVar;
                    objZzc = objZzc2;
                }
            } catch (Throwable th14) {
                th = th14;
                obj2 = obj;
            }
            i = zzkhVar2.zzk;
            while (i < zzkhVar2.zzl) {
                zzkhVar2.zzy(obj2, zzkhVar2.zzj[i], objZzc, zzllVar5, obj);
                i++;
                zzkhVar2 = this;
            }
            obj3 = obj2;
            if (objZzc != null) {
                zzllVar5.zzn(obj3, objZzc);
            }
            throw th;
        }
        if (obj4 != null) {
            zzllVar5.zzn(obj2, obj4);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzi(Object obj, byte[] bArr, int i, int i2, zzgj zzgjVar) throws zzje {
        zzc(obj, bArr, i, i2, 0, zzgjVar);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:7:0x0024  */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzj(Object obj, zzmd zzmdVar) throws zzhf {
        Map.Entry entry;
        Iterator it;
        int i;
        int i2;
        int i3;
        int i4;
        zzkh<T> zzkhVar = this;
        if (zzkhVar.zzh) {
            zzij zzijVarZzb = zzkhVar.zzo.zzb(obj);
            if (zzijVarZzb.zza.isEmpty()) {
                entry = null;
                it = null;
            } else {
                Iterator itZzf = zzijVarZzb.zzf();
                entry = (Map.Entry) itZzf.next();
                it = itZzf;
            }
        } else {
            entry = null;
            it = null;
        }
        int[] iArr = zzkhVar.zzc;
        Unsafe unsafe = zzb;
        int i5 = 0;
        int i6 = 1048575;
        int i7 = 0;
        while (i5 < iArr.length) {
            int iZzu = zzkhVar.zzu(i5);
            int[] iArr2 = zzkhVar.zzc;
            int iZzt = zzt(iZzu);
            int i8 = iArr2[i5];
            if (iZzt <= 17) {
                int i9 = iArr2[i5 + 2];
                int i10 = i9 & 1048575;
                if (i10 != i6) {
                    i = 1;
                    i7 = i10 == 1048575 ? 0 : unsafe.getInt(obj, i10);
                    i6 = i10;
                } else {
                    i = 1;
                }
                i2 = i6;
                i3 = i7;
                i4 = i << (i9 >>> 20);
            } else {
                i = 1;
                i2 = i6;
                i3 = i7;
                i4 = 0;
            }
            while (entry != null && zzkhVar.zzo.zza(entry) <= i8) {
                zzkhVar.zzo.zzi(zzmdVar, entry);
                entry = it.hasNext() ? (Map.Entry) it.next() : null;
            }
            long j = iZzu & 1048575;
            switch (iZzt) {
                case 0:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzf(i8, zzlv.zza(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 1:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzo(i8, zzlv.zzb(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 2:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzt(i8, unsafe.getLong(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 3:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzK(i8, unsafe.getLong(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 4:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzr(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 5:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzm(i8, unsafe.getLong(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 6:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzk(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 7:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzb(i8, zzlv.zzw(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 8:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzT(i8, unsafe.getObject(obj, j), zzmdVar);
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 9:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzv(i8, unsafe.getObject(obj, j), zzkhVar.zzx(i5));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 10:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzd(i8, (zzgw) unsafe.getObject(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 11:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzI(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 12:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzi(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 13:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzx(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 14:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzz(i8, unsafe.getLong(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 15:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzB(i8, unsafe.getInt(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 16:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzD(i8, unsafe.getLong(obj, j));
                    }
                    zzkhVar = this;
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 17:
                    if (zzkhVar.zzO(obj, i5, i2, i3, i4)) {
                        zzmdVar.zzq(i8, unsafe.getObject(obj, j), zzkhVar.zzx(i5));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 18:
                    zzkt.zzu(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 19:
                    zzkt.zzy(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 20:
                    zzkt.zzA(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 21:
                    zzkt.zzG(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 22:
                    zzkt.zzz(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 23:
                    zzkt.zzx(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 24:
                    zzkt.zzw(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 25:
                    zzkt.zzt(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 26:
                    int i11 = zzkhVar.zzc[i5];
                    List list = (List) unsafe.getObject(obj, j);
                    int i12 = zzkt.zza;
                    if (list != null && !list.isEmpty()) {
                        zzmdVar.zzH(i11, list);
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 27:
                    int i13 = zzkhVar.zzc[i5];
                    List list2 = (List) unsafe.getObject(obj, j);
                    zzkr zzkrVarZzx = zzkhVar.zzx(i5);
                    int i14 = zzkt.zza;
                    if (list2 != null && !list2.isEmpty()) {
                        for (int i15 = 0; i15 < list2.size(); i15++) {
                            ((zzhi) zzmdVar).zzv(i13, list2.get(i15), zzkrVarZzx);
                        }
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 28:
                    int i16 = zzkhVar.zzc[i5];
                    List list3 = (List) unsafe.getObject(obj, j);
                    int i17 = zzkt.zza;
                    if (list3 != null && !list3.isEmpty()) {
                        zzmdVar.zze(i16, list3);
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 29:
                    zzkt.zzF(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 30:
                    zzkt.zzv(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 31:
                    zzkt.zzB(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 32:
                    zzkt.zzC(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 33:
                    zzkt.zzD(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 34:
                    zzkt.zzE(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, false);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 35:
                    zzkt.zzu(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 36:
                    zzkt.zzy(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 37:
                    zzkt.zzA(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 38:
                    zzkt.zzG(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 39:
                    zzkt.zzz(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 40:
                    zzkt.zzx(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 41:
                    zzkt.zzw(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 42:
                    zzkt.zzt(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 43:
                    zzkt.zzF(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 44:
                    zzkt.zzv(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 45:
                    zzkt.zzB(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 46:
                    zzkt.zzC(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 47:
                    zzkt.zzD(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 48:
                    zzkt.zzE(zzkhVar.zzc[i5], (List) unsafe.getObject(obj, j), zzmdVar, i);
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 49:
                    int i18 = zzkhVar.zzc[i5];
                    List list4 = (List) unsafe.getObject(obj, j);
                    zzkr zzkrVarZzx2 = zzkhVar.zzx(i5);
                    int i19 = zzkt.zza;
                    if (list4 != null && !list4.isEmpty()) {
                        for (int i20 = 0; i20 < list4.size(); i20++) {
                            ((zzhi) zzmdVar).zzq(i18, list4.get(i20), zzkrVarZzx2);
                        }
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 50:
                    if (unsafe.getObject(obj, j) != null) {
                        throw null;
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 51:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzf(i8, zzn(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 52:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzo(i8, zzo(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 53:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzt(i8, zzv(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 54:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzK(i8, zzv(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 55:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzr(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 56:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzm(i8, zzv(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 57:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzk(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 58:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzb(i8, zzS(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 59:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzT(i8, unsafe.getObject(obj, j), zzmdVar);
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 60:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzv(i8, unsafe.getObject(obj, j), zzkhVar.zzx(i5));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 61:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzd(i8, (zzgw) unsafe.getObject(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 62:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzI(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 63:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzi(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 64:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzx(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 65:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzz(i8, zzv(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 66:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzB(i8, zzp(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 67:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzD(i8, zzv(obj, j));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                case 68:
                    if (zzkhVar.zzR(obj, i8, i5)) {
                        zzmdVar.zzq(i8, unsafe.getObject(obj, j), zzkhVar.zzx(i5));
                    }
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
                default:
                    i5 += 3;
                    i7 = i3;
                    i6 = i2;
                    entry = entry;
                    break;
            }
        }
        while (entry != null) {
            zzkhVar.zzo.zzi(zzmdVar, entry);
            entry = it.hasNext() ? (Map.Entry) it.next() : null;
        }
        zzll zzllVar = zzkhVar.zzn;
        zzllVar.zzq(zzllVar.zzd(obj), zzmdVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final boolean zzk(Object obj, Object obj2) {
        boolean zZzH;
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzu = zzu(i);
            long j = iZzu & 1048575;
            switch (zzt(iZzu)) {
                case 0:
                    if (!zzL(obj, obj2, i) || Double.doubleToLongBits(zzlv.zza(obj, j)) != Double.doubleToLongBits(zzlv.zza(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 1:
                    if (!zzL(obj, obj2, i) || Float.floatToIntBits(zzlv.zzb(obj, j)) != Float.floatToIntBits(zzlv.zzb(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 2:
                    if (!zzL(obj, obj2, i) || zzlv.zzd(obj, j) != zzlv.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 3:
                    if (!zzL(obj, obj2, i) || zzlv.zzd(obj, j) != zzlv.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 4:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 5:
                    if (!zzL(obj, obj2, i) || zzlv.zzd(obj, j) != zzlv.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 6:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 7:
                    if (!zzL(obj, obj2, i) || zzlv.zzw(obj, j) != zzlv.zzw(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 8:
                    if (!zzL(obj, obj2, i) || !zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 9:
                    if (!zzL(obj, obj2, i) || !zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 10:
                    if (!zzL(obj, obj2, i) || !zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 11:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 12:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 13:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 14:
                    if (!zzL(obj, obj2, i) || zzlv.zzd(obj, j) != zzlv.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 15:
                    if (!zzL(obj, obj2, i) || zzlv.zzc(obj, j) != zzlv.zzc(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 16:
                    if (!zzL(obj, obj2, i) || zzlv.zzd(obj, j) != zzlv.zzd(obj2, j)) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                case 17:
                    if (!zzL(obj, obj2, i) || !zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j))) {
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
                    zZzH = zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j));
                    break;
                case 50:
                    zZzH = zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j));
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
                    long jZzr = zzr(i) & 1048575;
                    if (zzlv.zzc(obj, jZzr) != zzlv.zzc(obj2, jZzr) || !zzkt.zzH(zzlv.zzf(obj, j), zzlv.zzf(obj2, j))) {
                        return false;
                    }
                    continue;
                    break;
                    break;
                default:
                    continue;
                    break;
            }
            if (!zZzH) {
                return false;
            }
        }
        if (!this.zzn.zzd(obj).equals(this.zzn.zzd(obj2))) {
            return false;
        }
        if (this.zzh) {
            return this.zzo.zzb(obj).equals(this.zzo.zzb(obj2));
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:42:0x008d  */
    /* JADX WARN: Code duplicated, block: B:44:0x009c  */
    /* JADX WARN: Code duplicated, block: B:47:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:50:0x00b2 A[LOOP:1: B:45:0x00a1->B:50:0x00b2, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:67:0x00b1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x00c6 A[SYNTHETIC] */
    @Override // com.google.android.recaptcha.internal.zzkr
    public final boolean zzl(Object obj) {
        int i;
        int i2;
        List list;
        zzkr zzkrVarZzx;
        int i3;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1048575;
        while (i4 < this.zzk) {
            int[] iArr = this.zzj;
            int[] iArr2 = this.zzc;
            int i7 = iArr[i4];
            int i8 = iArr2[i7];
            int iZzu = zzu(i7);
            int i9 = this.zzc[i7 + 2];
            int i10 = i9 & 1048575;
            int i11 = 1 << (i9 >>> 20);
            if (i10 != i6) {
                if (i10 != 1048575) {
                    i5 = zzb.getInt(obj, i10);
                }
                i2 = i5;
                i = i10;
            } else {
                i = i6;
                i2 = i5;
            }
            Object obj2 = obj;
            if ((268435456 & iZzu) != 0 && !zzO(obj2, i7, i, i2, i11)) {
                return false;
            }
            int iZzt = zzt(iZzu);
            if (iZzt == 9 || iZzt == 17) {
                if (zzO(obj2, i7, i, i2, i11) && !zzP(obj2, iZzu, zzx(i7))) {
                    return false;
                }
            } else if (iZzt == 27) {
                list = (List) zzlv.zzf(obj2, iZzu & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzkrVarZzx = zzx(i7);
                    for (i3 = 0; i3 < list.size(); i3++) {
                        if (!zzkrVarZzx.zzl(list.get(i3))) {
                            return false;
                        }
                    }
                }
            } else if (iZzt == 60 || iZzt == 68) {
                if (zzR(obj2, i8, i7) && !zzP(obj2, iZzu, zzx(i7))) {
                    return false;
                }
            } else if (iZzt == 49) {
                list = (List) zzlv.zzf(obj2, iZzu & 1048575);
                if (list.isEmpty()) {
                    zzkrVarZzx = zzx(i7);
                    while (i3 < list.size()) {
                        if (!zzkrVarZzx.zzl(list.get(i3))) {
                            return false;
                        }
                    }
                } else {
                    continue;
                }
            } else if (iZzt == 50 && !((zzjy) zzlv.zzf(obj2, iZzu & 1048575)).isEmpty()) {
                throw null;
            }
            i4++;
            obj = obj2;
            i6 = i;
            i5 = i2;
        }
        return !this.zzh || this.zzo.zzb(obj).zzk();
    }
}
