package com.google.android.gms.internal.vision;

import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.CharacterCompat;
import sun.misc.Unsafe;

final class zzko implements zzlc {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzma.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzkk zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final boolean zzj;
    private final boolean zzk;
    private final int[] zzl;
    private final int zzm;
    private final int zzn;
    private final zzks zzo;
    private final zzju zzp;
    private final zzlu zzq;
    private final zziq zzr;
    private final zzkh zzs;

    private zzko(int[] iArr, Object[] objArr, int i, int i2, zzkk zzkkVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzks zzksVar, zzju zzjuVar, zzlu zzluVar, zziq zziqVar, zzkh zzkhVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = zzkkVar instanceof zzjb;
        this.zzj = z;
        this.zzh = zziqVar != null && zziqVar.zza(zzkkVar);
        this.zzk = false;
        this.zzl = iArr2;
        this.zzm = i3;
        this.zzn = i4;
        this.zzo = zzksVar;
        this.zzp = zzjuVar;
        this.zzq = zzluVar;
        this.zzr = zziqVar;
        this.zzg = zzkkVar;
        this.zzs = zzkhVar;
    }

    /* JADX WARN: Code duplicated, block: B:125:0x0274  */
    /* JADX WARN: Code duplicated, block: B:127:0x0278  */
    /* JADX WARN: Code duplicated, block: B:130:0x0292  */
    /* JADX WARN: Code duplicated, block: B:131:0x0295  */
    /* JADX WARN: Code duplicated, block: B:179:0x0381  */
    static zzko zza(Class cls, zzki zzkiVar, zzks zzksVar, zzju zzjuVar, zzlu zzluVar, zziq zziqVar, zzkh zzkhVar) {
        int i;
        int iCharAt;
        int iCharAt2;
        int iCharAt3;
        int iCharAt4;
        int i2;
        int i3;
        int[] iArr;
        int i4;
        char cCharAt;
        int i5;
        char cCharAt2;
        int i6;
        char cCharAt3;
        int i7;
        char cCharAt4;
        int i8;
        char cCharAt5;
        int i9;
        char cCharAt6;
        int i10;
        char cCharAt7;
        int i11;
        char cCharAt8;
        int i12;
        int i13;
        int i14;
        int iObjectFieldOffset;
        int iObjectFieldOffset2;
        int i15;
        int i16;
        int i17;
        Field fieldZza;
        char cCharAt9;
        int i18;
        int i19;
        Object obj;
        Field fieldZza2;
        int i20;
        Object obj2;
        Field fieldZza3;
        int i21;
        char cCharAt10;
        int i22;
        char cCharAt11;
        int i23;
        int i24;
        char cCharAt12;
        int i25;
        char cCharAt13;
        if (zzkiVar instanceof zzla) {
            zzla zzlaVar = (zzla) zzkiVar;
            int i26 = 0;
            boolean z = zzlaVar.zza() == zzkz.zzb;
            String strZzd = zzlaVar.zzd();
            int length = strZzd.length();
            if (strZzd.charAt(0) >= 55296) {
                int i27 = 1;
                while (true) {
                    i = i27 + 1;
                    if (strZzd.charAt(i27) < 55296) {
                        break;
                    }
                    i27 = i;
                }
            } else {
                i = 1;
            }
            int i28 = i + 1;
            int iCharAt5 = strZzd.charAt(i);
            if (iCharAt5 >= 55296) {
                int i29 = iCharAt5 & 8191;
                int i30 = 13;
                while (true) {
                    i25 = i28 + 1;
                    cCharAt13 = strZzd.charAt(i28);
                    if (cCharAt13 < 55296) {
                        break;
                    }
                    i29 |= (cCharAt13 & 8191) << i30;
                    i30 += 13;
                    i28 = i25;
                }
                iCharAt5 = i29 | (cCharAt13 << i30);
                i28 = i25;
            }
            if (iCharAt5 == 0) {
                iCharAt = 0;
                iCharAt2 = 0;
                iCharAt3 = 0;
                i2 = 0;
                iCharAt4 = 0;
                iArr = zza;
                i3 = 0;
            } else {
                int i31 = i28 + 1;
                int iCharAt6 = strZzd.charAt(i28);
                if (iCharAt6 >= 55296) {
                    int i32 = iCharAt6 & 8191;
                    int i33 = 13;
                    while (true) {
                        i11 = i31 + 1;
                        cCharAt8 = strZzd.charAt(i31);
                        if (cCharAt8 < 55296) {
                            break;
                        }
                        i32 |= (cCharAt8 & 8191) << i33;
                        i33 += 13;
                        i31 = i11;
                    }
                    iCharAt6 = i32 | (cCharAt8 << i33);
                    i31 = i11;
                }
                int i34 = i31 + 1;
                int iCharAt7 = strZzd.charAt(i31);
                if (iCharAt7 >= 55296) {
                    int i35 = iCharAt7 & 8191;
                    int i36 = 13;
                    while (true) {
                        i10 = i34 + 1;
                        cCharAt7 = strZzd.charAt(i34);
                        if (cCharAt7 < 55296) {
                            break;
                        }
                        i35 |= (cCharAt7 & 8191) << i36;
                        i36 += 13;
                        i34 = i10;
                    }
                    iCharAt7 = i35 | (cCharAt7 << i36);
                    i34 = i10;
                }
                int i37 = i34 + 1;
                iCharAt = strZzd.charAt(i34);
                if (iCharAt >= 55296) {
                    int i38 = iCharAt & 8191;
                    int i39 = 13;
                    while (true) {
                        i9 = i37 + 1;
                        cCharAt6 = strZzd.charAt(i37);
                        if (cCharAt6 < 55296) {
                            break;
                        }
                        i38 |= (cCharAt6 & 8191) << i39;
                        i39 += 13;
                        i37 = i9;
                    }
                    iCharAt = i38 | (cCharAt6 << i39);
                    i37 = i9;
                }
                int i40 = i37 + 1;
                iCharAt2 = strZzd.charAt(i37);
                if (iCharAt2 >= 55296) {
                    int i41 = iCharAt2 & 8191;
                    int i42 = 13;
                    while (true) {
                        i8 = i40 + 1;
                        cCharAt5 = strZzd.charAt(i40);
                        if (cCharAt5 < 55296) {
                            break;
                        }
                        i41 |= (cCharAt5 & 8191) << i42;
                        i42 += 13;
                        i40 = i8;
                    }
                    iCharAt2 = i41 | (cCharAt5 << i42);
                    i40 = i8;
                }
                int i43 = i40 + 1;
                iCharAt3 = strZzd.charAt(i40);
                if (iCharAt3 >= 55296) {
                    int i44 = iCharAt3 & 8191;
                    int i45 = 13;
                    while (true) {
                        i7 = i43 + 1;
                        cCharAt4 = strZzd.charAt(i43);
                        if (cCharAt4 < 55296) {
                            break;
                        }
                        i44 |= (cCharAt4 & 8191) << i45;
                        i45 += 13;
                        i43 = i7;
                    }
                    iCharAt3 = i44 | (cCharAt4 << i45);
                    i43 = i7;
                }
                int i46 = i43 + 1;
                int iCharAt8 = strZzd.charAt(i43);
                if (iCharAt8 >= 55296) {
                    int i47 = iCharAt8 & 8191;
                    int i48 = 13;
                    while (true) {
                        i6 = i46 + 1;
                        cCharAt3 = strZzd.charAt(i46);
                        if (cCharAt3 < 55296) {
                            break;
                        }
                        i47 |= (cCharAt3 & 8191) << i48;
                        i48 += 13;
                        i46 = i6;
                    }
                    iCharAt8 = i47 | (cCharAt3 << i48);
                    i46 = i6;
                }
                int i49 = i46 + 1;
                int iCharAt9 = strZzd.charAt(i46);
                if (iCharAt9 >= 55296) {
                    int i50 = iCharAt9 & 8191;
                    int i51 = 13;
                    while (true) {
                        i5 = i49 + 1;
                        cCharAt2 = strZzd.charAt(i49);
                        if (cCharAt2 < 55296) {
                            break;
                        }
                        i50 |= (cCharAt2 & 8191) << i51;
                        i51 += 13;
                        i49 = i5;
                    }
                    iCharAt9 = i50 | (cCharAt2 << i51);
                    i49 = i5;
                }
                int i52 = i49 + 1;
                iCharAt4 = strZzd.charAt(i49);
                if (iCharAt4 >= 55296) {
                    int i53 = iCharAt4 & 8191;
                    int i54 = i52;
                    int i55 = 13;
                    while (true) {
                        i4 = i54 + 1;
                        cCharAt = strZzd.charAt(i54);
                        if (cCharAt < 55296) {
                            break;
                        }
                        i53 |= (cCharAt & 8191) << i55;
                        i55 += 13;
                        i54 = i4;
                    }
                    iCharAt4 = i53 | (cCharAt << i55);
                    i52 = i4;
                }
                int[] iArr2 = new int[iCharAt4 + iCharAt8 + iCharAt9];
                i2 = (iCharAt6 << 1) + iCharAt7;
                i3 = iCharAt8;
                iArr = iArr2;
                i26 = iCharAt6;
                i28 = i52;
            }
            Unsafe unsafe = zzb;
            Object[] objArrZze = zzlaVar.zze();
            Class<?> cls2 = zzlaVar.zzc().getClass();
            int[] iArr3 = new int[iCharAt3 * 3];
            Object[] objArr = new Object[iCharAt3 << 1];
            int i56 = i3 + iCharAt4;
            int i57 = i56;
            int i58 = iCharAt4;
            int i59 = 0;
            int i60 = 0;
            while (i28 < length) {
                int i61 = i28 + 1;
                int iCharAt10 = strZzd.charAt(i28);
                zzla zzlaVar2 = zzlaVar;
                if (iCharAt10 >= 55296) {
                    int i62 = iCharAt10 & 8191;
                    int i63 = i61;
                    int i64 = 13;
                    while (true) {
                        i24 = i63 + 1;
                        cCharAt12 = strZzd.charAt(i63);
                        i12 = length;
                        if (cCharAt12 < 55296) {
                            break;
                        }
                        i62 |= (cCharAt12 & 8191) << i64;
                        i64 += 13;
                        i63 = i24;
                        length = i12;
                    }
                    iCharAt10 = i62 | (cCharAt12 << i64);
                    i13 = i24;
                } else {
                    i12 = length;
                    i13 = i61;
                }
                int i65 = i13 + 1;
                int iCharAt11 = strZzd.charAt(i13);
                if (iCharAt11 >= 55296) {
                    int i66 = iCharAt11 & 8191;
                    int i67 = i65;
                    int i68 = 13;
                    while (true) {
                        i22 = i67 + 1;
                        cCharAt11 = strZzd.charAt(i67);
                        i23 = i66;
                        if (cCharAt11 < 55296) {
                            break;
                        }
                        i66 = i23 | ((cCharAt11 & 8191) << i68);
                        i68 += 13;
                        i67 = i22;
                    }
                    iCharAt11 = i23 | (cCharAt11 << i68);
                    i14 = i22;
                } else {
                    i14 = i65;
                }
                int i69 = i26;
                int i70 = iCharAt11 & 255;
                int i71 = iCharAt10;
                if ((iCharAt11 & 1024) != 0) {
                    iArr[i59] = i60;
                    i59++;
                }
                int[] iArr4 = iArr3;
                if (i70 >= 51) {
                    int i72 = i14 + 1;
                    int iCharAt12 = strZzd.charAt(i14);
                    char c = CharacterCompat.MIN_HIGH_SURROGATE;
                    if (iCharAt12 >= 55296) {
                        int i73 = iCharAt12 & 8191;
                        int i74 = 13;
                        while (true) {
                            i21 = i72 + 1;
                            cCharAt10 = strZzd.charAt(i72);
                            if (cCharAt10 < c) {
                                break;
                            }
                            i73 |= (cCharAt10 & 8191) << i74;
                            i74 += 13;
                            i72 = i21;
                            c = CharacterCompat.MIN_HIGH_SURROGATE;
                        }
                        iCharAt12 = i73 | (cCharAt10 << i74);
                        i72 = i21;
                    }
                    int i75 = i70 - 51;
                    int i76 = iCharAt12;
                    if (i75 == 9 || i75 == 17) {
                        i18 = i2 + 1;
                        objArr[((i60 / 3) << 1) + 1] = objArrZze[i2];
                    } else {
                        if (i75 == 12 && !z) {
                            i18 = i2 + 1;
                            objArr[((i60 / 3) << 1) + 1] = objArrZze[i2];
                        }
                        i19 = i76 << 1;
                        obj = objArrZze[i19];
                        if (obj instanceof Field) {
                            fieldZza2 = (Field) obj;
                        } else {
                            fieldZza2 = zza((Class) cls2, (String) obj);
                            objArrZze[i19] = fieldZza2;
                        }
                        int i77 = i72;
                        int iObjectFieldOffset3 = (int) unsafe.objectFieldOffset(fieldZza2);
                        i20 = i19 + 1;
                        obj2 = objArrZze[i20];
                        if (obj2 instanceof Field) {
                            fieldZza3 = (Field) obj2;
                        } else {
                            fieldZza3 = zza((Class) cls2, (String) obj2);
                            objArrZze[i20] = fieldZza3;
                        }
                        strZzd = strZzd;
                        iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZza3);
                        i17 = iObjectFieldOffset3;
                        i16 = 0;
                        i15 = i77;
                    }
                    i2 = i18;
                    i19 = i76 << 1;
                    obj = objArrZze[i19];
                    if (obj instanceof Field) {
                        fieldZza2 = (Field) obj;
                    } else {
                        fieldZza2 = zza((Class) cls2, (String) obj);
                        objArrZze[i19] = fieldZza2;
                    }
                    int i78 = i72;
                    int iObjectFieldOffset4 = (int) unsafe.objectFieldOffset(fieldZza2);
                    i20 = i19 + 1;
                    obj2 = objArrZze[i20];
                    if (obj2 instanceof Field) {
                        fieldZza3 = (Field) obj2;
                    } else {
                        fieldZza3 = zza((Class) cls2, (String) obj2);
                        objArrZze[i20] = fieldZza3;
                    }
                    strZzd = strZzd;
                    iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZza3);
                    i17 = iObjectFieldOffset4;
                    i16 = 0;
                    i15 = i78;
                } else {
                    int i79 = i2 + 1;
                    Field fieldZza4 = zza((Class) cls2, (String) objArrZze[i2]);
                    if (i70 == 9 || i70 == 17) {
                        objArr[((i60 / 3) << 1) + 1] = fieldZza4.getType();
                    } else {
                        if (i70 == 27 || i70 == 49) {
                            i2 += 2;
                            objArr[((i60 / 3) << 1) + 1] = objArrZze[i79];
                        } else if (i70 == 12 || i70 == 30 || i70 == 44) {
                            if (!z) {
                                i2 += 2;
                                objArr[((i60 / 3) << 1) + 1] = objArrZze[i79];
                            }
                        } else if (i70 == 50) {
                            int i80 = i58 + 1;
                            iArr[i58] = i60;
                            int i81 = (i60 / 3) << 1;
                            int i82 = i2 + 2;
                            objArr[i81] = objArrZze[i79];
                            if ((iCharAt11 & 2048) != 0) {
                                objArr[i81 + 1] = objArrZze[i82];
                                i2 += 3;
                            } else {
                                i2 = i82;
                            }
                            i58 = i80;
                        }
                        iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZza4);
                        if ((iCharAt11 & 4096) == 4096 || i70 > 17) {
                            iObjectFieldOffset2 = 1048575;
                            i15 = i14;
                            i16 = 0;
                        } else {
                            int i83 = i14 + 1;
                            int iCharAt13 = strZzd.charAt(i14);
                            if (iCharAt13 >= 55296) {
                                int i84 = iCharAt13 & 8191;
                                int i85 = 13;
                                while (true) {
                                    i15 = i83 + 1;
                                    cCharAt9 = strZzd.charAt(i83);
                                    if (cCharAt9 < 55296) {
                                        break;
                                    }
                                    i84 |= (cCharAt9 & 8191) << i85;
                                    i85 += 13;
                                    i83 = i15;
                                }
                                iCharAt13 = i84 | (cCharAt9 << i85);
                            } else {
                                i15 = i83;
                            }
                            int i86 = (i69 << 1) + (iCharAt13 / 32);
                            Object obj3 = objArrZze[i86];
                            if (obj3 instanceof Field) {
                                fieldZza = (Field) obj3;
                            } else {
                                fieldZza = zza((Class) cls2, (String) obj3);
                                objArrZze[i86] = fieldZza;
                            }
                            iObjectFieldOffset2 = (int) unsafe.objectFieldOffset(fieldZza);
                            i16 = iCharAt13 % 32;
                        }
                        if (i70 >= 18 && i70 <= 49) {
                            iArr[i57] = iObjectFieldOffset;
                            i57++;
                        }
                        i17 = iObjectFieldOffset;
                    }
                    i2 = i79;
                    iObjectFieldOffset = (int) unsafe.objectFieldOffset(fieldZza4);
                    if ((iCharAt11 & 4096) == 4096) {
                        iObjectFieldOffset2 = 1048575;
                        i15 = i14;
                        i16 = 0;
                    } else {
                        iObjectFieldOffset2 = 1048575;
                        i15 = i14;
                        i16 = 0;
                    }
                    if (i70 >= 18) {
                        iArr[i57] = iObjectFieldOffset;
                        i57++;
                    }
                    i17 = iObjectFieldOffset;
                }
                int i87 = i60 + 1;
                iArr4[i60] = i71;
                int i88 = i60 + 2;
                int i89 = iObjectFieldOffset2;
                iArr4[i87] = ((iCharAt11 & 256) != 0 ? 268435456 : 0) | ((iCharAt11 & 512) != 0 ? 536870912 : 0) | (i70 << 20) | i17;
                i60 += 3;
                iArr4[i88] = (i16 << 20) | i89;
                i26 = i69;
                zzlaVar = zzlaVar2;
                length = i12;
                i28 = i15;
                iArr3 = iArr4;
                strZzd = strZzd;
            }
            return new zzko(iArr3, objArr, iCharAt, iCharAt2, zzlaVar.zzc(), z, false, iArr, iCharAt4, i56, zzksVar, zzjuVar, zzluVar, zziqVar, zzkhVar);
        }
        WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(zzkiVar);
        throw null;
    }

    private static Field zza(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String string = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + name.length() + String.valueOf(string).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(string);
            throw new RuntimeException(sb.toString());
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final Object zza() {
        return this.zzo.zza(this.zzg);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x003a  */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zza(Object obj, Object obj2) {
        int length = this.zzc.length;
        int i = 0;
        while (true) {
            boolean zZza = true;
            if (i < length) {
                int iZzd = zzd(i);
                long j = iZzd & 1048575;
                switch ((iZzd & 267386880) >>> 20) {
                    case 0:
                        if (!zzc(obj, obj2, i) || Double.doubleToLongBits(zzma.zze(obj, j)) != Double.doubleToLongBits(zzma.zze(obj2, j))) {
                            zZza = false;
                        }
                        break;
                    case 1:
                        if (!zzc(obj, obj2, i) || Float.floatToIntBits(zzma.zzd(obj, j)) != Float.floatToIntBits(zzma.zzd(obj2, j))) {
                            zZza = false;
                        }
                        break;
                    case 2:
                        if (!zzc(obj, obj2, i) || zzma.zzb(obj, j) != zzma.zzb(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 3:
                        if (!zzc(obj, obj2, i) || zzma.zzb(obj, j) != zzma.zzb(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 4:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 5:
                        if (!zzc(obj, obj2, i) || zzma.zzb(obj, j) != zzma.zzb(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 6:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 7:
                        if (!zzc(obj, obj2, i) || zzma.zzc(obj, j) != zzma.zzc(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 8:
                        if (!zzc(obj, obj2, i) || !zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j))) {
                            zZza = false;
                        }
                        break;
                    case 9:
                        if (!zzc(obj, obj2, i) || !zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j))) {
                            zZza = false;
                        }
                        break;
                    case 10:
                        if (!zzc(obj, obj2, i) || !zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j))) {
                            zZza = false;
                        }
                        break;
                    case 11:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 12:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 13:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 14:
                        if (!zzc(obj, obj2, i) || zzma.zzb(obj, j) != zzma.zzb(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 15:
                        if (!zzc(obj, obj2, i) || zzma.zza(obj, j) != zzma.zza(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 16:
                        if (!zzc(obj, obj2, i) || zzma.zzb(obj, j) != zzma.zzb(obj2, j)) {
                            zZza = false;
                        }
                        break;
                    case 17:
                        if (!zzc(obj, obj2, i) || !zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j))) {
                            zZza = false;
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
                        zZza = zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j));
                        break;
                    case 50:
                        zZza = zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j));
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
                        long jZze = zze(i) & 1048575;
                        if (zzma.zza(obj, jZze) != zzma.zza(obj2, jZze) || !zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j))) {
                            zZza = false;
                        }
                        break;
                }
                if (!zZza) {
                    return false;
                }
                i += 3;
            } else {
                if (!this.zzq.zzb(obj).equals(this.zzq.zzb(obj2))) {
                    return false;
                }
                if (this.zzh) {
                    return this.zzr.zza(obj).equals(this.zzr.zza(obj2));
                }
                return true;
            }
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zza(Object obj) {
        int i;
        int iZza;
        int length = this.zzc.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 3) {
            int iZzd = zzd(i3);
            int i4 = this.zzc[i3];
            long j = 1048575 & iZzd;
            int iHashCode = 37;
            switch ((iZzd & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    iZza = zzjf.zza(Double.doubleToLongBits(zzma.zze(obj, j)));
                    i2 = i + iZza;
                    break;
                case 1:
                    i = i2 * 53;
                    iZza = Float.floatToIntBits(zzma.zzd(obj, j));
                    i2 = i + iZza;
                    break;
                case 2:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzb(obj, j));
                    i2 = i + iZza;
                    break;
                case 3:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzb(obj, j));
                    i2 = i + iZza;
                    break;
                case 4:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 5:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzb(obj, j));
                    i2 = i + iZza;
                    break;
                case 6:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 7:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzc(obj, j));
                    i2 = i + iZza;
                    break;
                case 8:
                    i = i2 * 53;
                    iZza = ((String) zzma.zzf(obj, j)).hashCode();
                    i2 = i + iZza;
                    break;
                case 9:
                    Object objZzf = zzma.zzf(obj, j);
                    if (objZzf != null) {
                        iHashCode = objZzf.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
                    break;
                case 10:
                    i = i2 * 53;
                    iZza = zzma.zzf(obj, j).hashCode();
                    i2 = i + iZza;
                    break;
                case 11:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 12:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 13:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 14:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzb(obj, j));
                    i2 = i + iZza;
                    break;
                case 15:
                    i = i2 * 53;
                    iZza = zzma.zza(obj, j);
                    i2 = i + iZza;
                    break;
                case 16:
                    i = i2 * 53;
                    iZza = zzjf.zza(zzma.zzb(obj, j));
                    i2 = i + iZza;
                    break;
                case 17:
                    Object objZzf2 = zzma.zzf(obj, j);
                    if (objZzf2 != null) {
                        iHashCode = objZzf2.hashCode();
                    }
                    i2 = (i2 * 53) + iHashCode;
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
                    i = i2 * 53;
                    iZza = zzma.zzf(obj, j).hashCode();
                    i2 = i + iZza;
                    break;
                case 50:
                    i = i2 * 53;
                    iZza = zzma.zzf(obj, j).hashCode();
                    i2 = i + iZza;
                    break;
                case 51:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(Double.doubleToLongBits(zzb(obj, j)));
                        i2 = i + iZza;
                    }
                    break;
                case 52:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = Float.floatToIntBits(zzc(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 53:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zze(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 54:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zze(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 55:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 56:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zze(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 57:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 58:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zzf(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 59:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = ((String) zzma.zzf(obj, j)).hashCode();
                        i2 = i + iZza;
                    }
                    break;
                case 60:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzma.zzf(obj, j).hashCode();
                        i2 = i + iZza;
                    }
                    break;
                case 61:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzma.zzf(obj, j).hashCode();
                        i2 = i + iZza;
                    }
                    break;
                case 62:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 63:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 64:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 65:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zze(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 66:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzd(obj, j);
                        i2 = i + iZza;
                    }
                    break;
                case 67:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzjf.zza(zze(obj, j));
                        i2 = i + iZza;
                    }
                    break;
                case 68:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        iZza = zzma.zzf(obj, j).hashCode();
                        i2 = i + iZza;
                    }
                    break;
            }
        }
        int iHashCode2 = (i2 * 53) + this.zzq.zzb(obj).hashCode();
        return this.zzh ? (iHashCode2 * 53) + this.zzr.zza(obj).hashCode() : iHashCode2;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(Object obj, Object obj2) {
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int iZzd = zzd(i);
            long j = 1048575 & iZzd;
            int i2 = this.zzc[i];
            switch ((iZzd & 267386880) >>> 20) {
                case 0:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zze(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 1:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzd(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 2:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzb(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 3:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzb(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 4:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 5:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzb(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 6:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 7:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzc(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 8:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzf(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 9:
                    zza(obj, obj2, i);
                    break;
                case 10:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzf(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 11:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 12:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 13:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 14:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzb(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 15:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zza(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 16:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzb(obj2, j));
                        zzb(obj, i);
                    }
                    break;
                case 17:
                    zza(obj, obj2, i);
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
                    this.zzp.zza(obj, obj2, j);
                    break;
                case 50:
                    zzle.zza(this.zzs, obj, obj2, j);
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
                        zzma.zza(obj, j, zzma.zzf(obj2, j));
                        zzb(obj, i2, i);
                    }
                    break;
                case 60:
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
                        zzma.zza(obj, j, zzma.zzf(obj2, j));
                        zzb(obj, i2, i);
                    }
                    break;
                case 68:
                    zzb(obj, obj2, i);
                    break;
            }
        }
        zzle.zza(this.zzq, obj, obj2);
        if (this.zzh) {
            zzle.zza(this.zzr, obj, obj2);
        }
    }

    private final void zza(Object obj, Object obj2, int i) {
        long jZzd = zzd(i) & 1048575;
        if (zza(obj2, i)) {
            Object objZzf = zzma.zzf(obj, jZzd);
            Object objZzf2 = zzma.zzf(obj2, jZzd);
            if (objZzf != null && objZzf2 != null) {
                zzma.zza(obj, jZzd, zzjf.zza(objZzf, objZzf2));
                zzb(obj, i);
            } else if (objZzf2 != null) {
                zzma.zza(obj, jZzd, objZzf2);
                zzb(obj, i);
            }
        }
    }

    private final void zzb(Object obj, Object obj2, int i) {
        int iZzd = zzd(i);
        int i2 = this.zzc[i];
        long j = iZzd & 1048575;
        if (zza(obj2, i2, i)) {
            Object objZzf = zza(obj, i2, i) ? zzma.zzf(obj, j) : null;
            Object objZzf2 = zzma.zzf(obj2, j);
            if (objZzf != null && objZzf2 != null) {
                zzma.zza(obj, j, zzjf.zza(objZzf, objZzf2));
                zzb(obj, i2, i);
            } else if (objZzf2 != null) {
                zzma.zza(obj, j, objZzf2);
                zzb(obj, i2, i);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:210:0x04d9 A[PHI: r4
  0x04d9: PHI (r4v4 int) = 
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v11 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v12 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v1 int)
  (r4v13 int)
  (r4v1 int)
 binds: [B:204:0x04c0, B:368:0x08ae, B:365:0x08a5, B:359:0x088a, B:356:0x0879, B:353:0x086a, B:350:0x085d, B:347:0x0850, B:344:0x0846, B:341:0x083d, B:338:0x0830, B:335:0x0823, B:332:0x0810, B:311:0x072a, B:308:0x0714, B:305:0x06fe, B:302:0x06e8, B:299:0x06d2, B:296:0x06bc, B:293:0x06a6, B:290:0x0690, B:287:0x067b, B:284:0x0666, B:281:0x0651, B:278:0x063c, B:275:0x0627, B:271:0x060f, B:266:0x05da, B:267:0x05dc, B:263:0x05cd, B:260:0x05bd, B:257:0x05ad, B:254:0x059d, B:251:0x0591, B:248:0x0585, B:245:0x0579, B:239:0x055b, B:236:0x0548, B:233:0x0537, B:230:0x0528, B:227:0x0519, B:225:0x0513, B:223:0x050c, B:220:0x0501, B:217:0x04f2, B:214:0x04e3, B:209:0x04d8, B:207:0x04c8] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zzb(Object obj) {
        int i;
        int i2;
        boolean z;
        int iZzd;
        int iZzb;
        int iZzj;
        int iZzi;
        int iZze;
        int iZzg;
        int iZzb2;
        int iZzi2;
        int iZze2;
        int iZzg2;
        int i3 = 267386880;
        int i4 = 1048575;
        int i5 = 0;
        if (this.zzj) {
            Unsafe unsafe = zzb;
            int i6 = 0;
            int i7 = 0;
            while (i6 < this.zzc.length) {
                int iZzd2 = zzd(i6);
                int i8 = (iZzd2 & i3) >>> 20;
                int i9 = i3;
                int i10 = this.zzc[i6];
                long j = iZzd2 & 1048575;
                if (i8 >= zziv.zza.zza() && i8 <= zziv.zzb.zza()) {
                    int i11 = this.zzc[i6 + 2];
                }
                switch (i8) {
                    case 0:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzb(i10, 0.0d);
                            i7 += iZzb2;
                        }
                        break;
                    case 1:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzb(i10, 0.0f);
                            i7 += iZzb2;
                        }
                        break;
                    case 2:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzd(i10, zzma.zzb(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 3:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zze(i10, zzma.zzb(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 4:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzf(i10, zzma.zza(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 5:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzg(i10, 0L);
                            i7 += iZzb2;
                        }
                        break;
                    case 6:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzi(i10, 0);
                            i7 += iZzb2;
                        }
                        break;
                    case 7:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzb(i10, true);
                            i7 += iZzb2;
                        }
                        break;
                    case 8:
                        if (zza(obj, i6)) {
                            Object objZzf = zzma.zzf(obj, j);
                            if (objZzf instanceof zzht) {
                                iZzb2 = zzii.zzc(i10, (zzht) objZzf);
                            } else {
                                iZzb2 = zzii.zzb(i10, (String) objZzf);
                            }
                            i7 += iZzb2;
                        }
                        break;
                    case 9:
                        if (zza(obj, i6)) {
                            iZzb2 = zzle.zza(i10, zzma.zzf(obj, j), zza(i6));
                            i7 += iZzb2;
                        }
                        break;
                    case 10:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzc(i10, (zzht) zzma.zzf(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 11:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzg(i10, zzma.zza(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 12:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzk(i10, zzma.zza(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 13:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzj(i10, 0);
                            i7 += iZzb2;
                        }
                        break;
                    case 14:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzh(i10, 0L);
                            i7 += iZzb2;
                        }
                        break;
                    case 15:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzh(i10, zzma.zza(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 16:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzf(i10, zzma.zzb(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 17:
                        if (zza(obj, i6)) {
                            iZzb2 = zzii.zzc(i10, (zzkk) zzma.zzf(obj, j), zza(i6));
                            i7 += iZzb2;
                        }
                        break;
                    case 18:
                        iZzb2 = zzle.zzi(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 19:
                        iZzb2 = zzle.zzh(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 20:
                        iZzb2 = zzle.zza(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 21:
                        iZzb2 = zzle.zzb(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 22:
                        iZzb2 = zzle.zze(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 23:
                        iZzb2 = zzle.zzi(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 24:
                        iZzb2 = zzle.zzh(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 25:
                        iZzb2 = zzle.zzj(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 26:
                        iZzb2 = zzle.zza(i10, zza(obj, j));
                        i7 += iZzb2;
                        break;
                    case 27:
                        iZzb2 = zzle.zza(i10, zza(obj, j), zza(i6));
                        i7 += iZzb2;
                        break;
                    case 28:
                        iZzb2 = zzle.zzb(i10, zza(obj, j));
                        i7 += iZzb2;
                        break;
                    case 29:
                        iZzb2 = zzle.zzf(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 30:
                        iZzb2 = zzle.zzd(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 31:
                        iZzb2 = zzle.zzh(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 32:
                        iZzb2 = zzle.zzi(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 33:
                        iZzb2 = zzle.zzg(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 34:
                        iZzb2 = zzle.zzc(i10, zza(obj, j), false);
                        i7 += iZzb2;
                        break;
                    case 35:
                        iZzi2 = zzle.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 36:
                        iZzi2 = zzle.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 37:
                        iZzi2 = zzle.zza((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 38:
                        iZzi2 = zzle.zzb((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 39:
                        iZzi2 = zzle.zze((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 40:
                        iZzi2 = zzle.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 41:
                        iZzi2 = zzle.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 42:
                        iZzi2 = zzle.zzj((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 43:
                        iZzi2 = zzle.zzf((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 44:
                        iZzi2 = zzle.zzd((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 45:
                        iZzi2 = zzle.zzh((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 46:
                        iZzi2 = zzle.zzi((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 47:
                        iZzi2 = zzle.zzg((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 48:
                        iZzi2 = zzle.zzc((List) unsafe.getObject(obj, j));
                        if (iZzi2 > 0) {
                            iZze2 = zzii.zze(i10);
                            iZzg2 = zzii.zzg(iZzi2);
                            iZzb2 = iZze2 + iZzg2 + iZzi2;
                            i7 += iZzb2;
                        }
                        break;
                    case 49:
                        iZzb2 = zzle.zzb(i10, zza(obj, j), zza(i6));
                        i7 += iZzb2;
                        break;
                    case 50:
                        iZzb2 = this.zzs.zza(i10, zzma.zzf(obj, j), zzb(i6));
                        i7 += iZzb2;
                        break;
                    case 51:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzb(i10, 0.0d);
                            i7 += iZzb2;
                        }
                        break;
                    case 52:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzb(i10, 0.0f);
                            i7 += iZzb2;
                        }
                        break;
                    case 53:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzd(i10, zze(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 54:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zze(i10, zze(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 55:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzf(i10, zzd(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 56:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzg(i10, 0L);
                            i7 += iZzb2;
                        }
                        break;
                    case 57:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzi(i10, 0);
                            i7 += iZzb2;
                        }
                        break;
                    case 58:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzb(i10, true);
                            i7 += iZzb2;
                        }
                        break;
                    case 59:
                        if (zza(obj, i10, i6)) {
                            Object objZzf2 = zzma.zzf(obj, j);
                            if (objZzf2 instanceof zzht) {
                                iZzb2 = zzii.zzc(i10, (zzht) objZzf2);
                            } else {
                                iZzb2 = zzii.zzb(i10, (String) objZzf2);
                            }
                            i7 += iZzb2;
                        }
                        break;
                    case 60:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzle.zza(i10, zzma.zzf(obj, j), zza(i6));
                            i7 += iZzb2;
                        }
                        break;
                    case 61:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzc(i10, (zzht) zzma.zzf(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 62:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzg(i10, zzd(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 63:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzk(i10, zzd(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 64:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzj(i10, 0);
                            i7 += iZzb2;
                        }
                        break;
                    case 65:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzh(i10, 0L);
                            i7 += iZzb2;
                        }
                        break;
                    case 66:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzh(i10, zzd(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 67:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzf(i10, zze(obj, j));
                            i7 += iZzb2;
                        }
                        break;
                    case 68:
                        if (zza(obj, i10, i6)) {
                            iZzb2 = zzii.zzc(i10, (zzkk) zzma.zzf(obj, j), zza(i6));
                            i7 += iZzb2;
                        }
                        break;
                }
                i6 += 3;
                i3 = i9;
            }
            return i7 + zza(this.zzq, obj);
        }
        Unsafe unsafe2 = zzb;
        int i12 = 1048575;
        int i13 = 0;
        int iZzb3 = 0;
        int i14 = 0;
        while (i13 < this.zzc.length) {
            int iZzd3 = zzd(i13);
            int[] iArr = this.zzc;
            int i15 = iArr[i13];
            int i16 = i4;
            int i17 = (iZzd3 & 267386880) >>> 20;
            if (i17 <= 17) {
                int i18 = iArr[i13 + 2];
                int i19 = i18 & i16;
                i = 1 << (i18 >>> 20);
                if (i19 != i12) {
                    i14 = unsafe2.getInt(obj, i19);
                    i12 = i19;
                }
            } else {
                i = 0;
            }
            long j2 = iZzd3 & i16;
            switch (i17) {
                case 0:
                    i2 = 0;
                    z = false;
                    if ((i & i14) != 0) {
                        iZzb3 += zzii.zzb(i15, 0.0d);
                    }
                    break;
                case 1:
                    i2 = 0;
                    if ((i & i14) != 0) {
                        z = false;
                        iZzb3 += zzii.zzb(i15, 0.0f);
                    } else {
                        z = false;
                    }
                    break;
                case 2:
                    i2 = 0;
                    if ((i & i14) != 0) {
                        iZzd = zzii.zzd(i15, unsafe2.getLong(obj, j2));
                        iZzb3 += iZzd;
                    }
                    z = false;
                    break;
                case 3:
                    i2 = 0;
                    if ((i & i14) != 0) {
                        iZzd = zzii.zze(i15, unsafe2.getLong(obj, j2));
                        iZzb3 += iZzd;
                    }
                    z = false;
                    break;
                case 4:
                    i2 = 0;
                    if ((i & i14) != 0) {
                        iZzd = zzii.zzf(i15, unsafe2.getInt(obj, j2));
                        iZzb3 += iZzd;
                    }
                    z = false;
                    break;
                case 5:
                    i2 = 0;
                    if ((i & i14) != 0) {
                        iZzd = zzii.zzg(i15, 0L);
                        iZzb3 += iZzd;
                    }
                    z = false;
                    break;
                case 6:
                    if ((i & i14) != 0) {
                        i2 = 0;
                        iZzd = zzii.zzi(i15, 0);
                        iZzb3 += iZzd;
                    } else {
                        i2 = 0;
                    }
                    z = false;
                    break;
                case 7:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzb(i15, true);
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 8:
                    if ((i & i14) != 0) {
                        Object object = unsafe2.getObject(obj, j2);
                        if (object instanceof zzht) {
                            iZzb = zzii.zzc(i15, (zzht) object);
                        } else {
                            iZzb = zzii.zzb(i15, (String) object);
                        }
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 9:
                    if ((i & i14) != 0) {
                        iZzb = zzle.zza(i15, unsafe2.getObject(obj, j2), zza(i13));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 10:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzc(i15, (zzht) unsafe2.getObject(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 11:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzg(i15, unsafe2.getInt(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 12:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzk(i15, unsafe2.getInt(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 13:
                    if ((i & i14) != 0) {
                        iZzj = zzii.zzj(i15, 0);
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 14:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzh(i15, 0L);
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 15:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzh(i15, unsafe2.getInt(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 16:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzf(i15, unsafe2.getLong(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 17:
                    if ((i & i14) != 0) {
                        iZzb = zzii.zzc(i15, (zzkk) unsafe2.getObject(obj, j2), zza(i13));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 18:
                    iZzb = zzle.zzi(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 19:
                    i2 = 0;
                    iZzd = zzle.zzh(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 20:
                    i2 = 0;
                    iZzd = zzle.zza(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 21:
                    i2 = 0;
                    iZzd = zzle.zzb(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 22:
                    i2 = 0;
                    iZzd = zzle.zze(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 23:
                    i2 = 0;
                    iZzd = zzle.zzi(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 24:
                    i2 = 0;
                    iZzd = zzle.zzh(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 25:
                    i2 = 0;
                    iZzd = zzle.zzj(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 26:
                    iZzb = zzle.zza(i15, (List) unsafe2.getObject(obj, j2));
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 27:
                    iZzb = zzle.zza(i15, (List) unsafe2.getObject(obj, j2), zza(i13));
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 28:
                    iZzb = zzle.zzb(i15, (List) unsafe2.getObject(obj, j2));
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 29:
                    iZzb = zzle.zzf(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 30:
                    i2 = 0;
                    iZzd = zzle.zzd(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 31:
                    i2 = 0;
                    iZzd = zzle.zzh(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 32:
                    i2 = 0;
                    iZzd = zzle.zzi(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 33:
                    i2 = 0;
                    iZzd = zzle.zzg(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 34:
                    i2 = 0;
                    iZzd = zzle.zzc(i15, (List) unsafe2.getObject(obj, j2), false);
                    iZzb3 += iZzd;
                    z = false;
                    break;
                case 35:
                    iZzi = zzle.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 36:
                    iZzi = zzle.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 37:
                    iZzi = zzle.zza((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 38:
                    iZzi = zzle.zzb((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 39:
                    iZzi = zzle.zze((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 40:
                    iZzi = zzle.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 41:
                    iZzi = zzle.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 42:
                    iZzi = zzle.zzj((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 43:
                    iZzi = zzle.zzf((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 44:
                    iZzi = zzle.zzd((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 45:
                    iZzi = zzle.zzh((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 46:
                    iZzi = zzle.zzi((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 47:
                    iZzi = zzle.zzg((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 48:
                    iZzi = zzle.zzc((List) unsafe2.getObject(obj, j2));
                    if (iZzi > 0) {
                        iZze = zzii.zze(i15);
                        iZzg = zzii.zzg(iZzi);
                        iZzj = iZze + iZzg + iZzi;
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 49:
                    iZzb = zzle.zzb(i15, (List) unsafe2.getObject(obj, j2), zza(i13));
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 50:
                    iZzb = this.zzs.zza(i15, unsafe2.getObject(obj, j2), zzb(i13));
                    iZzb3 += iZzb;
                    i2 = 0;
                    z = false;
                    break;
                case 51:
                    if (zza(obj, i15, i13)) {
                        iZzb3 += zzii.zzb(i15, 0.0d);
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 52:
                    if (zza(obj, i15, i13)) {
                        iZzj = zzii.zzb(i15, 0.0f);
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 53:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzd(i15, zze(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 54:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zze(i15, zze(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 55:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzf(i15, zzd(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 56:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzg(i15, 0L);
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 57:
                    if (zza(obj, i15, i13)) {
                        iZzj = zzii.zzi(i15, 0);
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 58:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzb(i15, true);
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 59:
                    if (zza(obj, i15, i13)) {
                        Object object2 = unsafe2.getObject(obj, j2);
                        if (object2 instanceof zzht) {
                            iZzb = zzii.zzc(i15, (zzht) object2);
                        } else {
                            iZzb = zzii.zzb(i15, (String) object2);
                        }
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 60:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzle.zza(i15, unsafe2.getObject(obj, j2), zza(i13));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 61:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzc(i15, (zzht) unsafe2.getObject(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 62:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzg(i15, zzd(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 63:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzk(i15, zzd(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 64:
                    if (zza(obj, i15, i13)) {
                        iZzj = zzii.zzj(i15, 0);
                        iZzb3 += iZzj;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 65:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzh(i15, 0L);
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 66:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzh(i15, zzd(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 67:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzf(i15, zze(obj, j2));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                case 68:
                    if (zza(obj, i15, i13)) {
                        iZzb = zzii.zzc(i15, (zzkk) unsafe2.getObject(obj, j2), zza(i13));
                        iZzb3 += iZzb;
                    }
                    i2 = 0;
                    z = false;
                    break;
                default:
                    i2 = 0;
                    z = false;
                    break;
            }
            i13 += 3;
            i5 = i2;
            i4 = i16;
        }
        int iZzc = i5;
        int iZza = iZzb3 + zza(this.zzq, obj);
        if (!this.zzh) {
            return iZza;
        }
        zziu zziuVarZza = this.zzr.zza(obj);
        for (int i20 = iZzc; i20 < zziuVarZza.zza.zzc(); i20++) {
            Map.Entry entryZzb = zziuVarZza.zza.zzb(i20);
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(entryZzb.getKey());
            iZzc += zziu.zzc(null, entryZzb.getValue());
        }
        for (Map.Entry entry : zziuVarZza.zza.zzd()) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
            iZzc += zziu.zzc(null, entry.getValue());
        }
        return iZza + iZzc;
    }

    private static int zza(zzlu zzluVar, Object obj) {
        return zzluVar.zzf(zzluVar.zzb(obj));
    }

    private static List zza(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }

    /* JADX WARN: Code duplicated, block: B:178:0x054a  */
    /* JADX WARN: Code duplicated, block: B:9:0x0032  */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(Object obj, zzmr zzmrVar) {
        Iterator itZzd;
        Map.Entry entry;
        Iterator itZze;
        Map.Entry entry2;
        if (zzmrVar.zza() == zzmq.zzb) {
            zza(this.zzq, obj, zzmrVar);
            if (this.zzh) {
                zziu zziuVarZza = this.zzr.zza(obj);
                if (zziuVarZza.zza.isEmpty()) {
                    itZze = null;
                    entry2 = null;
                } else {
                    itZze = zziuVarZza.zze();
                    entry2 = (Map.Entry) itZze.next();
                }
            } else {
                itZze = null;
                entry2 = null;
            }
            for (int length = this.zzc.length - 3; length >= 0; length -= 3) {
                int iZzd = zzd(length);
                int i = this.zzc[length];
                while (entry2 != null && this.zzr.zza(entry2) > i) {
                    this.zzr.zza(zzmrVar, entry2);
                    entry2 = itZze.hasNext() ? (Map.Entry) itZze.next() : null;
                }
                switch ((iZzd & 267386880) >>> 20) {
                    case 0:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 1:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 2:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 3:
                        if (zza(obj, length)) {
                            zzmrVar.zzc(i, zzma.zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 4:
                        if (zza(obj, length)) {
                            zzmrVar.zzc(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 5:
                        if (zza(obj, length)) {
                            zzmrVar.zzd(i, zzma.zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 6:
                        if (zza(obj, length)) {
                            zzmrVar.zzd(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 7:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zzc(obj, iZzd & 1048575));
                        }
                        break;
                    case 8:
                        if (zza(obj, length)) {
                            zza(i, zzma.zzf(obj, iZzd & 1048575), zzmrVar);
                        }
                        break;
                    case 9:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zzf(obj, iZzd & 1048575), zza(length));
                        }
                        break;
                    case 10:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, (zzht) zzma.zzf(obj, iZzd & 1048575));
                        }
                        break;
                    case 11:
                        if (zza(obj, length)) {
                            zzmrVar.zze(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 12:
                        if (zza(obj, length)) {
                            zzmrVar.zzb(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 13:
                        if (zza(obj, length)) {
                            zzmrVar.zza(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 14:
                        if (zza(obj, length)) {
                            zzmrVar.zzb(i, zzma.zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 15:
                        if (zza(obj, length)) {
                            zzmrVar.zzf(i, zzma.zza(obj, iZzd & 1048575));
                        }
                        break;
                    case 16:
                        if (zza(obj, length)) {
                            zzmrVar.zze(i, zzma.zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 17:
                        if (zza(obj, length)) {
                            zzmrVar.zzb(i, zzma.zzf(obj, iZzd & 1048575), zza(length));
                        }
                        break;
                    case 18:
                        zzle.zza(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 19:
                        zzle.zzb(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 20:
                        zzle.zzc(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 21:
                        zzle.zzd(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 22:
                        zzle.zzh(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 23:
                        zzle.zzf(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 24:
                        zzle.zzk(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 25:
                        zzle.zzn(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 26:
                        zzle.zza(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar);
                        break;
                    case 27:
                        zzle.zza(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, zza(length));
                        break;
                    case 28:
                        zzle.zzb(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar);
                        break;
                    case 29:
                        zzle.zzi(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 30:
                        zzle.zzm(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 31:
                        zzle.zzl(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 32:
                        zzle.zzg(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 33:
                        zzle.zzj(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 34:
                        zzle.zze(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, false);
                        break;
                    case 35:
                        zzle.zza(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 36:
                        zzle.zzb(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 37:
                        zzle.zzc(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 38:
                        zzle.zzd(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 39:
                        zzle.zzh(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 40:
                        zzle.zzf(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 41:
                        zzle.zzk(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 42:
                        zzle.zzn(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 43:
                        zzle.zzi(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 44:
                        zzle.zzm(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 45:
                        zzle.zzl(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 46:
                        zzle.zzg(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 47:
                        zzle.zzj(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 48:
                        zzle.zze(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, true);
                        break;
                    case 49:
                        zzle.zzb(this.zzc[length], (List) zzma.zzf(obj, iZzd & 1048575), zzmrVar, zza(length));
                        break;
                    case 50:
                        zza(zzmrVar, i, zzma.zzf(obj, iZzd & 1048575), length);
                        break;
                    case 51:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zzb(obj, iZzd & 1048575));
                        }
                        break;
                    case 52:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zzc(obj, iZzd & 1048575));
                        }
                        break;
                    case 53:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 54:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzc(i, zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 55:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzc(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 56:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzd(i, zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 57:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzd(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 58:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zzf(obj, iZzd & 1048575));
                        }
                        break;
                    case 59:
                        if (zza(obj, i, length)) {
                            zza(i, zzma.zzf(obj, iZzd & 1048575), zzmrVar);
                        }
                        break;
                    case 60:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zzma.zzf(obj, iZzd & 1048575), zza(length));
                        }
                        break;
                    case 61:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, (zzht) zzma.zzf(obj, iZzd & 1048575));
                        }
                        break;
                    case 62:
                        if (zza(obj, i, length)) {
                            zzmrVar.zze(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 63:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzb(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 64:
                        if (zza(obj, i, length)) {
                            zzmrVar.zza(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 65:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzb(i, zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 66:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzf(i, zzd(obj, iZzd & 1048575));
                        }
                        break;
                    case 67:
                        if (zza(obj, i, length)) {
                            zzmrVar.zze(i, zze(obj, iZzd & 1048575));
                        }
                        break;
                    case 68:
                        if (zza(obj, i, length)) {
                            zzmrVar.zzb(i, zzma.zzf(obj, iZzd & 1048575), zza(length));
                        }
                        break;
                }
            }
            while (entry2 != null) {
                this.zzr.zza(zzmrVar, entry2);
                entry2 = itZze.hasNext() ? (Map.Entry) itZze.next() : null;
            }
            return;
        }
        if (this.zzj) {
            if (this.zzh) {
                zziu zziuVarZza2 = this.zzr.zza(obj);
                if (zziuVarZza2.zza.isEmpty()) {
                    itZzd = null;
                    entry = null;
                } else {
                    itZzd = zziuVarZza2.zzd();
                    entry = (Map.Entry) itZzd.next();
                }
            } else {
                itZzd = null;
                entry = null;
            }
            int length2 = this.zzc.length;
            for (int i2 = 0; i2 < length2; i2 += 3) {
                int iZzd2 = zzd(i2);
                int i3 = this.zzc[i2];
                while (entry != null && this.zzr.zza(entry) <= i3) {
                    this.zzr.zza(zzmrVar, entry);
                    entry = itZzd.hasNext() ? (Map.Entry) itZzd.next() : null;
                }
                switch ((iZzd2 & 267386880) >>> 20) {
                    case 0:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 1:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 2:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 3:
                        if (zza(obj, i2)) {
                            zzmrVar.zzc(i3, zzma.zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 4:
                        if (zza(obj, i2)) {
                            zzmrVar.zzc(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 5:
                        if (zza(obj, i2)) {
                            zzmrVar.zzd(i3, zzma.zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 6:
                        if (zza(obj, i2)) {
                            zzmrVar.zzd(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 7:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zzc(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 8:
                        if (zza(obj, i2)) {
                            zza(i3, zzma.zzf(obj, iZzd2 & 1048575), zzmrVar);
                        }
                        break;
                    case 9:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zzf(obj, iZzd2 & 1048575), zza(i2));
                        }
                        break;
                    case 10:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, (zzht) zzma.zzf(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 11:
                        if (zza(obj, i2)) {
                            zzmrVar.zze(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 12:
                        if (zza(obj, i2)) {
                            zzmrVar.zzb(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 13:
                        if (zza(obj, i2)) {
                            zzmrVar.zza(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 14:
                        if (zza(obj, i2)) {
                            zzmrVar.zzb(i3, zzma.zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 15:
                        if (zza(obj, i2)) {
                            zzmrVar.zzf(i3, zzma.zza(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 16:
                        if (zza(obj, i2)) {
                            zzmrVar.zze(i3, zzma.zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 17:
                        if (zza(obj, i2)) {
                            zzmrVar.zzb(i3, zzma.zzf(obj, iZzd2 & 1048575), zza(i2));
                        }
                        break;
                    case 18:
                        zzle.zza(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 19:
                        zzle.zzb(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 20:
                        zzle.zzc(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 21:
                        zzle.zzd(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 22:
                        zzle.zzh(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 23:
                        zzle.zzf(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 24:
                        zzle.zzk(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 25:
                        zzle.zzn(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 26:
                        zzle.zza(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar);
                        break;
                    case 27:
                        zzle.zza(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, zza(i2));
                        break;
                    case 28:
                        zzle.zzb(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar);
                        break;
                    case 29:
                        zzle.zzi(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 30:
                        zzle.zzm(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 31:
                        zzle.zzl(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 32:
                        zzle.zzg(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 33:
                        zzle.zzj(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 34:
                        zzle.zze(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, false);
                        break;
                    case 35:
                        zzle.zza(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 36:
                        zzle.zzb(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 37:
                        zzle.zzc(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 38:
                        zzle.zzd(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 39:
                        zzle.zzh(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 40:
                        zzle.zzf(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 41:
                        zzle.zzk(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 42:
                        zzle.zzn(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 43:
                        zzle.zzi(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 44:
                        zzle.zzm(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 45:
                        zzle.zzl(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 46:
                        zzle.zzg(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 47:
                        zzle.zzj(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 48:
                        zzle.zze(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, true);
                        break;
                    case 49:
                        zzle.zzb(this.zzc[i2], (List) zzma.zzf(obj, iZzd2 & 1048575), zzmrVar, zza(i2));
                        break;
                    case 50:
                        zza(zzmrVar, i3, zzma.zzf(obj, iZzd2 & 1048575), i2);
                        break;
                    case 51:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zzb(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 52:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zzc(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 53:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 54:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzc(i3, zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 55:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzc(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 56:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzd(i3, zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 57:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzd(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 58:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zzf(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 59:
                        if (zza(obj, i3, i2)) {
                            zza(i3, zzma.zzf(obj, iZzd2 & 1048575), zzmrVar);
                        }
                        break;
                    case 60:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zzma.zzf(obj, iZzd2 & 1048575), zza(i2));
                        }
                        break;
                    case 61:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, (zzht) zzma.zzf(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 62:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zze(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 63:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzb(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 64:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zza(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 65:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzb(i3, zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 66:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzf(i3, zzd(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 67:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zze(i3, zze(obj, iZzd2 & 1048575));
                        }
                        break;
                    case 68:
                        if (zza(obj, i3, i2)) {
                            zzmrVar.zzb(i3, zzma.zzf(obj, iZzd2 & 1048575), zza(i2));
                        }
                        break;
                }
            }
            while (entry != null) {
                this.zzr.zza(zzmrVar, entry);
                entry = itZzd.hasNext() ? (Map.Entry) itZzd.next() : null;
            }
            zza(this.zzq, obj, zzmrVar);
            return;
        }
        zzb(obj, zzmrVar);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0023  */
    private final void zzb(Object obj, zzmr zzmrVar) {
        Iterator itZzd;
        Map.Entry entry;
        int i;
        if (this.zzh) {
            zziu zziuVarZza = this.zzr.zza(obj);
            if (zziuVarZza.zza.isEmpty()) {
                itZzd = null;
                entry = null;
            } else {
                itZzd = zziuVarZza.zzd();
                entry = (Map.Entry) itZzd.next();
            }
        } else {
            itZzd = null;
            entry = null;
        }
        int length = this.zzc.length;
        Unsafe unsafe = zzb;
        int i2 = 1048575;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 3) {
            int iZzd = zzd(i4);
            int[] iArr = this.zzc;
            int i5 = iArr[i4];
            int i6 = (iZzd & 267386880) >>> 20;
            if (i6 <= 17) {
                int i7 = iArr[i4 + 2];
                int i8 = i7 & 1048575;
                if (i8 != i2) {
                    i3 = unsafe.getInt(obj, i8);
                    i2 = i8;
                }
                i = 1 << (i7 >>> 20);
            } else {
                i = 0;
            }
            while (entry != null && this.zzr.zza(entry) <= i5) {
                this.zzr.zza(zzmrVar, entry);
                entry = itZzd.hasNext() ? (Map.Entry) itZzd.next() : null;
            }
            long j = iZzd & 1048575;
            switch (i6) {
                case 0:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, zzma.zze(obj, j));
                        continue;
                    }
                    break;
                case 1:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, zzma.zzd(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 2:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 3:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzc(i5, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 4:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzc(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 5:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzd(i5, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 6:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzd(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 7:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, zzma.zzc(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 8:
                    if ((i3 & i) != 0) {
                        zza(i5, unsafe.getObject(obj, j), zzmrVar);
                    } else {
                        continue;
                    }
                    break;
                case 9:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, unsafe.getObject(obj, j), zza(i4));
                    } else {
                        continue;
                    }
                    break;
                case 10:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, (zzht) unsafe.getObject(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 11:
                    if ((i3 & i) != 0) {
                        zzmrVar.zze(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 12:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzb(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 13:
                    if ((i3 & i) != 0) {
                        zzmrVar.zza(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 14:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzb(i5, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 15:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzf(i5, unsafe.getInt(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 16:
                    if ((i3 & i) != 0) {
                        zzmrVar.zze(i5, unsafe.getLong(obj, j));
                    } else {
                        continue;
                    }
                    break;
                case 17:
                    if ((i3 & i) != 0) {
                        zzmrVar.zzb(i5, unsafe.getObject(obj, j), zza(i4));
                    } else {
                        continue;
                    }
                    break;
                case 18:
                    zzle.zza(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 19:
                    zzle.zzb(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 20:
                    zzle.zzc(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 21:
                    zzle.zzd(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 22:
                    zzle.zzh(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 23:
                    zzle.zzf(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 24:
                    zzle.zzk(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 25:
                    zzle.zzn(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 26:
                    zzle.zza(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar);
                    break;
                case 27:
                    zzle.zza(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, zza(i4));
                    break;
                case 28:
                    zzle.zzb(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar);
                    break;
                case 29:
                    zzle.zzi(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 30:
                    zzle.zzm(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 31:
                    zzle.zzl(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 32:
                    zzle.zzg(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 33:
                    zzle.zzj(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 34:
                    zzle.zze(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, false);
                    continue;
                    break;
                case 35:
                    zzle.zza(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 36:
                    zzle.zzb(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 37:
                    zzle.zzc(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 38:
                    zzle.zzd(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 39:
                    zzle.zzh(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 40:
                    zzle.zzf(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 41:
                    zzle.zzk(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 42:
                    zzle.zzn(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 43:
                    zzle.zzi(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 44:
                    zzle.zzm(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 45:
                    zzle.zzl(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 46:
                    zzle.zzg(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 47:
                    zzle.zzj(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 48:
                    zzle.zze(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, true);
                    break;
                case 49:
                    zzle.zzb(this.zzc[i4], (List) unsafe.getObject(obj, j), zzmrVar, zza(i4));
                    break;
                case 50:
                    zza(zzmrVar, i5, unsafe.getObject(obj, j), i4);
                    break;
                case 51:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, zzb(obj, j));
                    }
                    break;
                case 52:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, zzc(obj, j));
                    }
                    break;
                case 53:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, zze(obj, j));
                    }
                    break;
                case 54:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzc(i5, zze(obj, j));
                    }
                    break;
                case 55:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzc(i5, zzd(obj, j));
                    }
                    break;
                case 56:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzd(i5, zze(obj, j));
                    }
                    break;
                case 57:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzd(i5, zzd(obj, j));
                    }
                    break;
                case 58:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, zzf(obj, j));
                    }
                    break;
                case 59:
                    if (zza(obj, i5, i4)) {
                        zza(i5, unsafe.getObject(obj, j), zzmrVar);
                    }
                    break;
                case 60:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, unsafe.getObject(obj, j), zza(i4));
                    }
                    break;
                case 61:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, (zzht) unsafe.getObject(obj, j));
                    }
                    break;
                case 62:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zze(i5, zzd(obj, j));
                    }
                    break;
                case 63:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzb(i5, zzd(obj, j));
                    }
                    break;
                case 64:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zza(i5, zzd(obj, j));
                    }
                    break;
                case 65:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzb(i5, zze(obj, j));
                    }
                    break;
                case 66:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzf(i5, zzd(obj, j));
                    }
                    break;
                case 67:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zze(i5, zze(obj, j));
                    }
                    break;
                case 68:
                    if (zza(obj, i5, i4)) {
                        zzmrVar.zzb(i5, unsafe.getObject(obj, j), zza(i4));
                    }
                    break;
            }
        }
        while (entry != null) {
            this.zzr.zza(zzmrVar, entry);
            entry = itZzd.hasNext() ? (Map.Entry) itZzd.next() : null;
        }
        zza(this.zzq, obj, zzmrVar);
    }

    private final void zza(zzmr zzmrVar, int i, Object obj, int i2) {
        if (obj != null) {
            this.zzs.zzb(zzb(i2));
            zzmrVar.zza(i, (zzkf) null, this.zzs.zzc(obj));
        }
    }

    private static void zza(zzlu zzluVar, Object obj, zzmr zzmrVar) {
        zzluVar.zza(zzluVar.zzb(obj), zzmrVar);
    }

    private static zzlx zze(Object obj) {
        zzjb zzjbVar = (zzjb) obj;
        zzlx zzlxVar = zzjbVar.zzb;
        if (zzlxVar != zzlx.zza()) {
            return zzlxVar;
        }
        zzlx zzlxVarZzb = zzlx.zzb();
        zzjbVar.zzb = zzlxVarZzb;
        return zzlxVarZzb;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzhn zzhnVar) throws zzjk {
        int iZza;
        Unsafe unsafe = zzb;
        zzjl zzjlVarZza = (zzjl) unsafe.getObject(obj, j2);
        if (!zzjlVarZza.zza()) {
            int size = zzjlVarZza.size();
            zzjlVarZza = zzjlVarZza.zza(size == 0 ? 10 : size << 1);
            unsafe.putObject(obj, j2, zzjlVarZza);
        }
        zzjl zzjlVar = zzjlVarZza;
        switch (i7) {
            case 18:
            case 35:
                if (i5 != 2) {
                    if (i5 == 1) {
                        zzhl.zzc(bArr, i);
                        throw null;
                    }
                    return i;
                }
                int iZza2 = zzhl.zza(bArr, i, zzhnVar);
                int i8 = zzhnVar.zza + iZza2;
                if (iZza2 < i8) {
                    zzhl.zzc(bArr, iZza2);
                    throw null;
                }
                if (iZza2 == i8) {
                    return iZza2;
                }
                throw zzjk.zza();
            case 19:
            case 36:
                if (i5 != 2) {
                    if (i5 == 5) {
                        zzhl.zzd(bArr, i);
                        throw null;
                    }
                    return i;
                }
                int iZza3 = zzhl.zza(bArr, i, zzhnVar);
                int i9 = zzhnVar.zza + iZza3;
                if (iZza3 < i9) {
                    zzhl.zzd(bArr, iZza3);
                    throw null;
                }
                if (iZza3 == i9) {
                    return iZza3;
                }
                throw zzjk.zza();
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zzhl.zzb(bArr, i, zzhnVar);
                        long j3 = zzhnVar.zzb;
                        throw null;
                    }
                    return i;
                }
                int iZza4 = zzhl.zza(bArr, i, zzhnVar);
                int i10 = zzhnVar.zza + iZza4;
                if (iZza4 < i10) {
                    zzhl.zzb(bArr, iZza4, zzhnVar);
                    throw null;
                }
                if (iZza4 == i10) {
                    return iZza4;
                }
                throw zzjk.zza();
            case 22:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzhl.zza(bArr, i, zzjlVar, zzhnVar);
                }
                if (i5 == 0) {
                    return zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                return i;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 != 2) {
                    if (i5 == 1) {
                        zzhl.zzb(bArr, i);
                        throw null;
                    }
                    return i;
                }
                int iZza5 = zzhl.zza(bArr, i, zzhnVar);
                int i11 = zzhnVar.zza + iZza5;
                if (iZza5 < i11) {
                    zzhl.zzb(bArr, iZza5);
                    throw null;
                }
                if (iZza5 == i11) {
                    return iZza5;
                }
                throw zzjk.zza();
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzjd zzjdVar = (zzjd) zzjlVar;
                    int iZza6 = zzhl.zza(bArr, i, zzhnVar);
                    int i12 = zzhnVar.zza + iZza6;
                    while (iZza6 < i12) {
                        zzjdVar.zzc(zzhl.zza(bArr, iZza6));
                        iZza6 += 4;
                    }
                    if (iZza6 == i12) {
                        return iZza6;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 5) {
                    zzjd zzjdVar2 = (zzjd) zzjlVar;
                    zzjdVar2.zzc(zzhl.zza(bArr, i));
                    int i13 = i + 4;
                    while (i13 < i2) {
                        int iZza7 = zzhl.zza(bArr, i13, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i13;
                        }
                        zzjdVar2.zzc(zzhl.zza(bArr, iZza7));
                        i13 = iZza7 + 4;
                    }
                    return i13;
                }
                return i;
            case 25:
            case 42:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zzhl.zzb(bArr, i, zzhnVar);
                        long j4 = zzhnVar.zzb;
                        throw null;
                    }
                    return i;
                }
                int iZza8 = zzhl.zza(bArr, i, zzhnVar);
                int i14 = zzhnVar.zza + iZza8;
                if (iZza8 < i14) {
                    zzhl.zzb(bArr, iZza8, zzhnVar);
                    throw null;
                }
                if (iZza8 == i14) {
                    return iZza8;
                }
                throw zzjk.zza();
            case 26:
                if (i5 == 2) {
                    if ((j & 536870912) == 0) {
                        int iZza9 = zzhl.zza(bArr, i, zzhnVar);
                        int i15 = zzhnVar.zza;
                        if (i15 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i15 == 0) {
                            zzjlVar.add(_UrlKt.FRAGMENT_ENCODE_SET);
                        } else {
                            zzjlVar.add(new String(bArr, iZza9, i15, zzjf.zza));
                            iZza9 += i15;
                        }
                        while (iZza9 < i2) {
                            int iZza10 = zzhl.zza(bArr, iZza9, zzhnVar);
                            if (i3 != zzhnVar.zza) {
                                return iZza9;
                            }
                            iZza9 = zzhl.zza(bArr, iZza10, zzhnVar);
                            int i16 = zzhnVar.zza;
                            if (i16 < 0) {
                                throw zzjk.zzb();
                            }
                            if (i16 == 0) {
                                zzjlVar.add(_UrlKt.FRAGMENT_ENCODE_SET);
                            } else {
                                zzjlVar.add(new String(bArr, iZza9, i16, zzjf.zza));
                                iZza9 += i16;
                            }
                        }
                        return iZza9;
                    }
                    int iZza11 = zzhl.zza(bArr, i, zzhnVar);
                    int i17 = zzhnVar.zza;
                    if (i17 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i17 == 0) {
                        zzjlVar.add(_UrlKt.FRAGMENT_ENCODE_SET);
                    } else {
                        int i18 = iZza11 + i17;
                        if (!zzmd.zza(bArr, iZza11, i18)) {
                            throw zzjk.zzh();
                        }
                        zzjlVar.add(new String(bArr, iZza11, i17, zzjf.zza));
                        iZza11 = i18;
                    }
                    while (iZza11 < i2) {
                        int iZza12 = zzhl.zza(bArr, iZza11, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return iZza11;
                        }
                        iZza11 = zzhl.zza(bArr, iZza12, zzhnVar);
                        int i19 = zzhnVar.zza;
                        if (i19 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i19 == 0) {
                            zzjlVar.add(_UrlKt.FRAGMENT_ENCODE_SET);
                        } else {
                            int i20 = iZza11 + i19;
                            if (!zzmd.zza(bArr, iZza11, i20)) {
                                throw zzjk.zzh();
                            }
                            zzjlVar.add(new String(bArr, iZza11, i19, zzjf.zza));
                            iZza11 = i20;
                        }
                    }
                    return iZza11;
                }
                return i;
            case 27:
                if (i5 == 2) {
                    return zzhl.zza(zza(i6), i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                return i;
            case 28:
                if (i5 == 2) {
                    int iZza13 = zzhl.zza(bArr, i, zzhnVar);
                    int i21 = zzhnVar.zza;
                    if (i21 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i21 > bArr.length - iZza13) {
                        throw zzjk.zza();
                    }
                    if (i21 == 0) {
                        zzjlVar.add(zzht.zza);
                    } else {
                        zzjlVar.add(zzht.zza(bArr, iZza13, i21));
                        iZza13 += i21;
                    }
                    while (iZza13 < i2) {
                        int iZza14 = zzhl.zza(bArr, iZza13, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return iZza13;
                        }
                        iZza13 = zzhl.zza(bArr, iZza14, zzhnVar);
                        int i22 = zzhnVar.zza;
                        if (i22 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i22 > bArr.length - iZza13) {
                            throw zzjk.zza();
                        }
                        if (i22 == 0) {
                            zzjlVar.add(zzht.zza);
                        } else {
                            zzjlVar.add(zzht.zza(bArr, iZza13, i22));
                            iZza13 += i22;
                        }
                    }
                    return iZza13;
                }
                return i;
            case 30:
            case 44:
                if (i5 != 2) {
                    if (i5 == 0) {
                        iZza = zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                    }
                    return i;
                }
                iZza = zzhl.zza(bArr, i, zzjlVar, zzhnVar);
                zzjb zzjbVar = (zzjb) obj;
                zzlx zzlxVar = zzjbVar.zzb;
                zzlx zzlxVar2 = (zzlx) zzle.zza(i4, zzjlVar, zzc(i6), zzlxVar != zzlx.zza() ? zzlxVar : null, this.zzq);
                if (zzlxVar2 != null) {
                    zzjbVar.zzb = zzlxVar2;
                }
                return iZza;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzjd zzjdVar3 = (zzjd) zzjlVar;
                    int iZza15 = zzhl.zza(bArr, i, zzhnVar);
                    int i23 = zzhnVar.zza + iZza15;
                    while (iZza15 < i23) {
                        iZza15 = zzhl.zza(bArr, iZza15, zzhnVar);
                        zzjdVar3.zzc(zzif.zze(zzhnVar.zza));
                    }
                    if (iZza15 == i23) {
                        return iZza15;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 0) {
                    zzjd zzjdVar4 = (zzjd) zzjlVar;
                    int iZza16 = zzhl.zza(bArr, i, zzhnVar);
                    zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    while (iZza16 < i2) {
                        int iZza17 = zzhl.zza(bArr, iZza16, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return iZza16;
                        }
                        iZza16 = zzhl.zza(bArr, iZza17, zzhnVar);
                        zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    }
                    return iZza16;
                }
                return i;
            case 34:
            case 48:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zzhl.zzb(bArr, i, zzhnVar);
                        zzif.zza(zzhnVar.zzb);
                        throw null;
                    }
                    return i;
                }
                int iZza18 = zzhl.zza(bArr, i, zzhnVar);
                int i24 = zzhnVar.zza + iZza18;
                if (iZza18 >= i24) {
                    if (iZza18 == i24) {
                        return iZza18;
                    }
                    throw zzjk.zza();
                }
                zzhl.zzb(bArr, iZza18, zzhnVar);
                zzif.zza(zzhnVar.zzb);
                throw null;
            case 49:
                if (i5 == 3) {
                    zzlc zzlcVarZza = zza(i6);
                    int i25 = (i3 & (-8)) | 4;
                    int iZza19 = zzhl.zza(zzlcVarZza, bArr, i, i2, i25, zzhnVar);
                    int i26 = i25;
                    zzhn zzhnVar2 = zzhnVar;
                    zzjlVar.add(zzhnVar2.zzc);
                    while (iZza19 < i2) {
                        int iZza20 = zzhl.zza(bArr, iZza19, zzhnVar2);
                        if (i3 != zzhnVar2.zza) {
                            return iZza19;
                        }
                        int i27 = i26;
                        zzhn zzhnVar3 = zzhnVar2;
                        iZza19 = zzhl.zza(zzlcVarZza, bArr, iZza20, i2, i27, zzhnVar3);
                        zzjlVar.add(zzhnVar3.zzc);
                        i26 = i27;
                        zzhnVar2 = zzhnVar3;
                    }
                    return iZza19;
                }
                return i;
            default:
                return i;
        }
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, long j, zzhn zzhnVar) throws zzjk {
        Unsafe unsafe = zzb;
        Object objZzb = zzb(i3);
        Object object = unsafe.getObject(obj, j);
        if (this.zzs.zzd(object)) {
            Object objZzf = this.zzs.zzf(objZzb);
            this.zzs.zza(objZzf, object);
            unsafe.putObject(obj, j, objZzf);
            object = objZzf;
        }
        this.zzs.zzb(objZzb);
        this.zzs.zza(object);
        int iZza = zzhl.zza(bArr, i, zzhnVar);
        int i4 = zzhnVar.zza;
        if (i4 < 0 || i4 > i2 - iZza) {
            throw zzjk.zza();
        }
        throw null;
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzhn zzhnVar) throws zzjk {
        int i9;
        int i10;
        int iZzb;
        Object object;
        Unsafe unsafe = zzb;
        long j2 = this.zzc[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                i9 = i;
                if (i5 != 1) {
                    return i9;
                }
                unsafe.putObject(obj, j, Double.valueOf(zzhl.zzc(bArr, i)));
                iZzb = i9 + 8;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 52:
                i10 = i;
                if (i5 != 5) {
                    return i10;
                }
                unsafe.putObject(obj, j, Float.valueOf(zzhl.zzd(bArr, i)));
                iZzb = i10 + 4;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 53:
            case 54:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzhl.zzb(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, Long.valueOf(zzhnVar.zzb));
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 55:
            case 62:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzhl.zza(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, Integer.valueOf(zzhnVar.zza));
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 56:
            case 65:
                i9 = i;
                if (i5 != 1) {
                    return i9;
                }
                unsafe.putObject(obj, j, Long.valueOf(zzhl.zzb(bArr, i)));
                iZzb = i9 + 8;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 57:
            case 64:
                i10 = i;
                if (i5 != 5) {
                    return i10;
                }
                unsafe.putObject(obj, j, Integer.valueOf(zzhl.zza(bArr, i)));
                iZzb = i10 + 4;
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 58:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzhl.zzb(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, Boolean.valueOf(zzhnVar.zzb != 0));
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 59:
                if (i5 != 2) {
                    return i;
                }
                int iZza = zzhl.zza(bArr, i, zzhnVar);
                int i11 = zzhnVar.zza;
                if (i11 == 0) {
                    unsafe.putObject(obj, j, _UrlKt.FRAGMENT_ENCODE_SET);
                } else {
                    if ((i6 & 536870912) != 0 && !zzmd.zza(bArr, iZza, iZza + i11)) {
                        throw zzjk.zzh();
                    }
                    unsafe.putObject(obj, j, new String(bArr, iZza, i11, zzjf.zza));
                    iZza += i11;
                }
                unsafe.putInt(obj, j2, i4);
                return iZza;
            case 60:
                if (i5 != 2) {
                    return i;
                }
                int iZza2 = zzhl.zza(zza(i8), bArr, i, i2, zzhnVar);
                object = unsafe.getInt(obj, j2) == i4 ? unsafe.getObject(obj, j) : null;
                if (object == null) {
                    unsafe.putObject(obj, j, zzhnVar.zzc);
                } else {
                    unsafe.putObject(obj, j, zzjf.zza(object, zzhnVar.zzc));
                }
                unsafe.putInt(obj, j2, i4);
                return iZza2;
            case 61:
                if (i5 != 2) {
                    return i;
                }
                iZzb = zzhl.zze(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, zzhnVar.zzc);
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 63:
                if (i5 != 0) {
                    return i;
                }
                int iZza3 = zzhl.zza(bArr, i, zzhnVar);
                int i12 = zzhnVar.zza;
                zzjg zzjgVarZzc = zzc(i8);
                if (zzjgVarZzc == null || zzjgVarZzc.zza(i12)) {
                    unsafe.putObject(obj, j, Integer.valueOf(i12));
                    iZzb = iZza3;
                    unsafe.putInt(obj, j2, i4);
                    return iZzb;
                }
                zze(obj).zza(i3, Long.valueOf(i12));
                return iZza3;
            case 66:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzhl.zza(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, Integer.valueOf(zzif.zze(zzhnVar.zza)));
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 67:
                if (i5 != 0) {
                    return i;
                }
                iZzb = zzhl.zzb(bArr, i, zzhnVar);
                unsafe.putObject(obj, j, Long.valueOf(zzif.zza(zzhnVar.zzb)));
                unsafe.putInt(obj, j2, i4);
                return iZzb;
            case 68:
                if (i5 == 3) {
                    iZzb = zzhl.zza(zza(i8), bArr, i, i2, (i3 & (-8)) | 4, zzhnVar);
                    object = unsafe.getInt(obj, j2) == i4 ? unsafe.getObject(obj, j) : null;
                    if (object == null) {
                        unsafe.putObject(obj, j, zzhnVar.zzc);
                    } else {
                        unsafe.putObject(obj, j, zzjf.zza(object, zzhnVar.zzc));
                    }
                    unsafe.putInt(obj, j2, i4);
                    return iZzb;
                }
            default:
                return i;
        }
    }

    private final zzlc zza(int i) {
        int i2 = (i / 3) << 1;
        zzlc zzlcVar = (zzlc) this.zzd[i2];
        if (zzlcVar != null) {
            return zzlcVar;
        }
        zzlc zzlcVarZza = zzky.zza().zza((Class) this.zzd[i2 + 1]);
        this.zzd[i2] = zzlcVarZza;
        return zzlcVarZza;
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    private final zzjg zzc(int i) {
        return (zzjg) this.zzd[((i / 3) << 1) + 1];
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 12061. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    final int zza(java.lang.Object r27, byte[] r28, int r29, int r30, int r31, com.google.android.gms.internal.vision.zzhn r32) {
        /*
            Method dump skipped, instruction units count: 1206
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.vision.zzko.zza(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.vision.zzhn):int");
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 8701. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(java.lang.Object r27, byte[] r28, int r29, int r30, com.google.android.gms.internal.vision.zzhn r31) {
        /*
            Method dump skipped, instruction units count: 870
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.vision.zzko.zza(java.lang.Object, byte[], int, int, com.google.android.gms.internal.vision.zzhn):void");
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzc(Object obj) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long jZzd = zzd(this.zzl[i2]) & 1048575;
            Object objZzf = zzma.zzf(obj, jZzd);
            if (objZzf != null) {
                zzma.zza(obj, jZzd, this.zzs.zze(objZzf));
            }
            i2++;
        }
        int length = this.zzl.length;
        while (i < length) {
            this.zzp.zzb(obj, this.zzl[i]);
            i++;
        }
        this.zzq.zzd(obj);
        if (this.zzh) {
            this.zzr.zzc(obj);
        }
    }

    private final Object zza(Object obj, int i, Object obj2, zzlu zzluVar) {
        zzjg zzjgVarZzc;
        int i2 = this.zzc[i];
        Object objZzf = zzma.zzf(obj, zzd(i) & 1048575);
        return (objZzf == null || (zzjgVarZzc = zzc(i)) == null) ? obj2 : zza(i, i2, this.zzs.zza(objZzf), zzjgVarZzc, obj2, zzluVar);
    }

    private final Object zza(int i, int i2, Map map, zzjg zzjgVar, Object obj, zzlu zzluVar) {
        this.zzs.zzb(zzb(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!zzjgVar.zza(((Integer) entry.getValue()).intValue())) {
                if (obj == null) {
                    obj = zzluVar.zza();
                }
                zzib zzibVarZzc = zzht.zzc(zzkc.zza(null, entry.getKey(), entry.getValue()));
                try {
                    zzkc.zza(zzibVarZzc.zzb(), null, entry.getKey(), entry.getValue());
                    zzluVar.zza(obj, i2, zzibVarZzc.zza());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    /* JADX WARN: Code duplicated, block: B:42:0x0096  */
    /* JADX WARN: Code duplicated, block: B:44:0x00a5  */
    /* JADX WARN: Code duplicated, block: B:47:0x00b0  */
    /* JADX WARN: Code duplicated, block: B:50:0x00bb A[LOOP:1: B:45:0x00aa->B:50:0x00bb, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:67:0x00ba A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x00cf A[SYNTHETIC] */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zzd(Object obj) {
        int i;
        int i2;
        zzko zzkoVar;
        Object obj2;
        List list;
        zzlc zzlcVarZza;
        int i3;
        int i4 = 1048575;
        int i5 = 0;
        int i6 = 0;
        while (i5 < this.zzm) {
            int i7 = this.zzl[i5];
            int i8 = this.zzc[i7];
            int iZzd = zzd(i7);
            int i9 = this.zzc[i7 + 2];
            int i10 = i9 & 1048575;
            int i11 = 1 << (i9 >>> 20);
            if (i10 != i4) {
                if (i10 != 1048575) {
                    i6 = zzb.getInt(obj, i10);
                }
                i2 = i6;
                i = i10;
            } else {
                i = i4;
                i2 = i6;
            }
            if ((268435456 & iZzd) != 0) {
                zzkoVar = this;
                obj2 = obj;
                if (!zzkoVar.zza(obj2, i7, i, i2, i11)) {
                    return false;
                }
            } else {
                zzkoVar = this;
                obj2 = obj;
            }
            int i12 = (267386880 & iZzd) >>> 20;
            if (i12 == 9 || i12 == 17) {
                if (zzkoVar.zza(obj2, i7, i, i2, i11) && !zza(obj2, iZzd, zza(i7))) {
                    return false;
                }
            } else if (i12 == 27) {
                list = (List) zzma.zzf(obj2, iZzd & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzlcVarZza = zza(i7);
                    for (i3 = 0; i3 < list.size(); i3++) {
                        if (!zzlcVarZza.zzd(list.get(i3))) {
                            return false;
                        }
                    }
                }
            } else if (i12 == 60 || i12 == 68) {
                if (zza(obj2, i8, i7) && !zza(obj2, iZzd, zza(i7))) {
                    return false;
                }
            } else if (i12 == 49) {
                list = (List) zzma.zzf(obj2, iZzd & 1048575);
                if (list.isEmpty()) {
                    zzlcVarZza = zza(i7);
                    while (i3 < list.size()) {
                        if (!zzlcVarZza.zzd(list.get(i3))) {
                            return false;
                        }
                    }
                } else {
                    continue;
                }
            } else if (i12 == 50 && !zzkoVar.zzs.zzc(zzma.zzf(obj2, iZzd & 1048575)).isEmpty()) {
                zzkoVar.zzs.zzb(zzb(i7));
                throw null;
            }
            i5++;
            obj = obj2;
            i4 = i;
            i6 = i2;
        }
        return !this.zzh || this.zzr.zza(obj).zzf();
    }

    private static boolean zza(Object obj, int i, zzlc zzlcVar) {
        return zzlcVar.zzd(zzma.zzf(obj, i & 1048575));
    }

    private static void zza(int i, Object obj, zzmr zzmrVar) {
        if (obj instanceof String) {
            zzmrVar.zza(i, (String) obj);
        } else {
            zzmrVar.zza(i, (zzht) obj);
        }
    }

    private final int zzd(int i) {
        return this.zzc[i + 1];
    }

    private final int zze(int i) {
        return this.zzc[i + 2];
    }

    private static double zzb(Object obj, long j) {
        return ((Double) zzma.zzf(obj, j)).doubleValue();
    }

    private static float zzc(Object obj, long j) {
        return ((Float) zzma.zzf(obj, j)).floatValue();
    }

    private static int zzd(Object obj, long j) {
        return ((Integer) zzma.zzf(obj, j)).intValue();
    }

    private static long zze(Object obj, long j) {
        return ((Long) zzma.zzf(obj, j)).longValue();
    }

    private static boolean zzf(Object obj, long j) {
        return ((Boolean) zzma.zzf(obj, j)).booleanValue();
    }

    private final boolean zzc(Object obj, Object obj2, int i) {
        return zza(obj, i) == zza(obj2, i);
    }

    private final boolean zza(Object obj, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zza(obj, i);
        }
        return (i3 & i4) != 0;
    }

    private final boolean zza(Object obj, int i) {
        int iZze = zze(i);
        long j = iZze & 1048575;
        if (j != 1048575) {
            return (zzma.zza(obj, j) & (1 << (iZze >>> 20))) != 0;
        }
        int iZzd = zzd(i);
        long j2 = iZzd & 1048575;
        switch ((iZzd & 267386880) >>> 20) {
            case 0:
                return zzma.zze(obj, j2) != 0.0d;
            case 1:
                return zzma.zzd(obj, j2) != 0.0f;
            case 2:
                return zzma.zzb(obj, j2) != 0;
            case 3:
                return zzma.zzb(obj, j2) != 0;
            case 4:
                return zzma.zza(obj, j2) != 0;
            case 5:
                return zzma.zzb(obj, j2) != 0;
            case 6:
                return zzma.zza(obj, j2) != 0;
            case 7:
                return zzma.zzc(obj, j2);
            case 8:
                Object objZzf = zzma.zzf(obj, j2);
                if (objZzf instanceof String) {
                    return !((String) objZzf).isEmpty();
                }
                if (objZzf instanceof zzht) {
                    return !zzht.zza.equals(objZzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzma.zzf(obj, j2) != null;
            case 10:
                return !zzht.zza.equals(zzma.zzf(obj, j2));
            case 11:
                return zzma.zza(obj, j2) != 0;
            case 12:
                return zzma.zza(obj, j2) != 0;
            case 13:
                return zzma.zza(obj, j2) != 0;
            case 14:
                return zzma.zzb(obj, j2) != 0;
            case 15:
                return zzma.zza(obj, j2) != 0;
            case 16:
                return zzma.zzb(obj, j2) != 0;
            case 17:
                return zzma.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final void zzb(Object obj, int i) {
        int iZze = zze(i);
        long j = 1048575 & iZze;
        if (j == 1048575) {
            return;
        }
        zzma.zza(obj, j, (1 << (iZze >>> 20)) | zzma.zza(obj, j));
    }

    private final boolean zza(Object obj, int i, int i2) {
        return zzma.zza(obj, (long) (zze(i2) & 1048575)) == i;
    }

    private final void zzb(Object obj, int i, int i2) {
        zzma.zza(obj, zze(i2) & 1048575, i);
    }

    private final int zzg(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, 0);
    }

    private final int zza(int i, int i2) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, i2);
    }

    private final int zzb(int i, int i2) {
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
}
