package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.internal.url._UrlKt;

public final class zzas implements Iterable, zzao {
    private final String zza;

    public zzas(String str) {
        if (str == null) {
            throw new IllegalArgumentException("StringValue cannot be null.");
        }
        this.zza = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzas) {
            return this.zza.equals(((zzas) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return new zzar(this);
    }

    public final String toString() {
        String str = this.zza;
        StringBuilder sb = new StringBuilder(str.length() + 2);
        sb.append("\"");
        sb.append(str);
        sb.append("\"");
        return sb.toString();
    }

    final /* synthetic */ String zzb() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzao
    public final String zzc() {
        return this.zza;
    }

    /* JADX WARN: Code duplicated, block: B:100:0x02c3  */
    /* JADX WARN: Code duplicated, block: B:102:0x02c9  */
    /* JADX WARN: Code duplicated, block: B:108:0x02f3  */
    /* JADX WARN: Code duplicated, block: B:111:0x02fa  */
    /* JADX WARN: Code duplicated, block: B:113:0x02fe A[LOOP:0: B:112:0x02fc->B:113:0x02fe, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:116:0x0311  */
    /* JADX WARN: Code duplicated, block: B:118:0x031d  */
    /* JADX WARN: Code duplicated, block: B:120:0x0328  */
    /* JADX WARN: Code duplicated, block: B:121:0x033c  */
    /* JADX WARN: Code duplicated, block: B:124:0x0346  */
    /* JADX WARN: Code duplicated, block: B:125:0x0353  */
    /* JADX WARN: Code duplicated, block: B:128:0x0363  */
    /* JADX WARN: Code duplicated, block: B:129:0x0376  */
    /* JADX WARN: Code duplicated, block: B:132:0x0385  */
    /* JADX WARN: Code duplicated, block: B:133:0x0390  */
    /* JADX WARN: Code duplicated, block: B:136:0x03ac  */
    /* JADX WARN: Code duplicated, block: B:138:0x03b8  */
    /* JADX WARN: Code duplicated, block: B:140:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:141:0x03c7  */
    /* JADX WARN: Code duplicated, block: B:144:0x03e4  */
    /* JADX WARN: Code duplicated, block: B:146:0x03fd  */
    /* JADX WARN: Code duplicated, block: B:148:0x0400  */
    /* JADX WARN: Code duplicated, block: B:150:0x040c  */
    /* JADX WARN: Code duplicated, block: B:152:0x041b  */
    /* JADX WARN: Code duplicated, block: B:154:0x0427  */
    /* JADX WARN: Code duplicated, block: B:156:0x0438  */
    /* JADX WARN: Code duplicated, block: B:158:0x0444  */
    /* JADX WARN: Code duplicated, block: B:161:0x0452  */
    /* JADX WARN: Code duplicated, block: B:164:0x0468  */
    /* JADX WARN: Code duplicated, block: B:165:0x046b  */
    /* JADX WARN: Code duplicated, block: B:168:0x0484  */
    /* JADX WARN: Code duplicated, block: B:169:0x0487  */
    /* JADX WARN: Code duplicated, block: B:172:0x049b  */
    /* JADX WARN: Code duplicated, block: B:174:0x04a5  */
    /* JADX WARN: Code duplicated, block: B:176:0x04b5  */
    /* JADX WARN: Code duplicated, block: B:178:0x04c1  */
    /* JADX WARN: Code duplicated, block: B:180:0x04cb  */
    /* JADX WARN: Code duplicated, block: B:183:0x04ea  */
    /* JADX WARN: Code duplicated, block: B:185:0x04f9  */
    /* JADX WARN: Code duplicated, block: B:187:0x0505  */
    /* JADX WARN: Code duplicated, block: B:189:0x050f  */
    /* JADX WARN: Code duplicated, block: B:191:0x0521  */
    /* JADX WARN: Code duplicated, block: B:193:0x052d  */
    /* JADX WARN: Code duplicated, block: B:195:0x0533  */
    /* JADX WARN: Code duplicated, block: B:198:0x0541 A[LOOP:1: B:196:0x053b->B:198:0x0541, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:202:0x0560  */
    /* JADX WARN: Code duplicated, block: B:204:0x056e  */
    /* JADX WARN: Code duplicated, block: B:206:0x0578  */
    /* JADX WARN: Code duplicated, block: B:207:0x0591  */
    /* JADX WARN: Code duplicated, block: B:217:0x05ae  */
    /* JADX WARN: Code duplicated, block: B:219:0x05ba  */
    /* JADX WARN: Code duplicated, block: B:221:0x05ca  */
    /* JADX WARN: Code duplicated, block: B:223:0x05d8  */
    /* JADX WARN: Code duplicated, block: B:225:0x05dc  */
    /* JADX WARN: Code duplicated, block: B:227:0x05ec  */
    /* JADX WARN: Code duplicated, block: B:229:0x0608  */
    /* JADX WARN: Code duplicated, block: B:231:0x060b  */
    /* JADX WARN: Code duplicated, block: B:43:0x00c6 A[FALL_THROUGH] */
    /* JADX WARN: Code duplicated, block: B:44:0x00ca  */
    /* JADX WARN: Code duplicated, block: B:46:0x00d0  */
    /* JADX WARN: Code duplicated, block: B:48:0x00df  */
    /* JADX WARN: Code duplicated, block: B:50:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:53:0x00fd  */
    /* JADX WARN: Code duplicated, block: B:54:0x0100  */
    /* JADX WARN: Code duplicated, block: B:57:0x0127  */
    /* JADX WARN: Code duplicated, block: B:59:0x0133  */
    /* JADX WARN: Code duplicated, block: B:61:0x013e  */
    /* JADX WARN: Code duplicated, block: B:63:0x0154  */
    /* JADX WARN: Code duplicated, block: B:66:0x0168  */
    /* JADX WARN: Code duplicated, block: B:68:0x016c  */
    /* JADX WARN: Code duplicated, block: B:69:0x0192  */
    /* JADX WARN: Code duplicated, block: B:72:0x01d6  */
    /* JADX WARN: Code duplicated, block: B:74:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:76:0x01ed  */
    /* JADX WARN: Code duplicated, block: B:77:0x0206  */
    /* JADX WARN: Code duplicated, block: B:80:0x020e  */
    /* JADX WARN: Code duplicated, block: B:82:0x0227  */
    /* JADX WARN: Code duplicated, block: B:85:0x0256  */
    /* JADX WARN: Code duplicated, block: B:87:0x0262  */
    /* JADX WARN: Code duplicated, block: B:89:0x026d  */
    /* JADX WARN: Code duplicated, block: B:91:0x027d  */
    /* JADX WARN: Code duplicated, block: B:93:0x0289  */
    /* JADX WARN: Code duplicated, block: B:94:0x028e  */
    /* JADX WARN: Code duplicated, block: B:96:0x02a3  */
    /* JADX WARN: Code duplicated, block: B:97:0x02ba  */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.zzao
    public final zzao zzcA(String str, zzg zzgVar, List list) {
        String str2;
        int i;
        String str3;
        String strZzc;
        int i2;
        String str4;
        zzao zzaoVarZza;
        int i3;
        int i4;
        int iZzi;
        zzas zzasVar;
        StringBuilder sb;
        int i5;
        double dDoubleValue;
        double dZzi;
        int i6;
        int i7;
        String strZzc2;
        String str5;
        double dDoubleValue2;
        double dZzi2;
        double dMin;
        double length;
        double dZzi3;
        double dMin2;
        String str6;
        ArrayList arrayList;
        String strZzc3;
        long jZzh;
        String[] strArrSplit;
        int length2;
        int i8;
        int i9;
        boolean zIsEmpty;
        String str7;
        int iZzi2;
        int length3;
        zzao zzaoVarZza2;
        String str8;
        String str9;
        int iIndexOf;
        int i10;
        zzg zzgVar2;
        double dDoubleValue3;
        if ("charAt".equals(str) || "concat".equals(str) || "hasOwnProperty".equals(str) || "indexOf".equals(str) || "lastIndexOf".equals(str) || "match".equals(str) || "replace".equals(str) || "search".equals(str) || "slice".equals(str) || "split".equals(str) || "substring".equals(str) || "toLowerCase".equals(str) || "toLocaleLowerCase".equals(str) || "toString".equals(str) || "toUpperCase".equals(str)) {
            str2 = "toLocaleUpperCase";
            i = 0;
        } else {
            str2 = "toLocaleUpperCase";
            i = 0;
            if (!str2.equals(str)) {
                str3 = "trim";
                if (!str3.equals(str)) {
                    throw new IllegalArgumentException(String.format("%s is not a String function", str));
                }
            }
            strZzc = "undefined";
            switch (str.hashCode()) {
                case -1789698943:
                    i2 = i;
                    if (str.equals("hasOwnProperty")) {
                        zzh.zza("hasOwnProperty", 1, list);
                        str4 = this.zza;
                        zzaoVarZza = zzgVar.zza((zzao) list.get(i2));
                        if ("length".equals(zzaoVarZza.zzc())) {
                            return zzao.zzk;
                        }
                        double dDoubleValue4 = zzaoVarZza.zzd().doubleValue();
                        return (dDoubleValue4 == Math.floor(dDoubleValue4) || (i3 = (int) dDoubleValue4) < 0 || i3 >= str4.length()) ? zzao.zzl : zzao.zzk;
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -1776922004:
                    i4 = i;
                    if (str.equals("toString")) {
                        zzh.zza("toString", i4, list);
                        return this;
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -1464939364:
                    if (str.equals("toLocaleLowerCase")) {
                        zzh.zza("toLocaleLowerCase", 0, list);
                        return new zzas(this.zza.toLowerCase());
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -1361633751:
                    if (str.equals("charAt")) {
                        zzh.zzc("charAt", 1, list);
                        if (list.isEmpty()) {
                            iZzi = 0;
                        } else {
                            iZzi = (int) zzh.zzi(zzgVar.zza((zzao) list.get(0)).zzd().doubleValue());
                        }
                        String str10 = this.zza;
                        return (iZzi >= 0 || iZzi >= str10.length()) ? zzao.zzm : new zzas(String.valueOf(str10.charAt(iZzi)));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -1354795244:
                    zzasVar = this;
                    if (str.equals("concat")) {
                        if (!list.isEmpty()) {
                            sb = new StringBuilder(zzasVar.zza);
                            for (i5 = 0; i5 < list.size(); i5++) {
                                sb.append(zzgVar.zza((zzao) list.get(i5)).zzc());
                            }
                            return new zzas(sb.toString());
                        }
                        return zzasVar;
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -1137582698:
                    if (str.equals("toLowerCase")) {
                        zzh.zza("toLowerCase", 0, list);
                        return new zzas(this.zza.toLowerCase(Locale.ENGLISH));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -906336856:
                    if (str.equals("search")) {
                        zzh.zzc("search", 1, list);
                        Matcher matcher = Pattern.compile(list.isEmpty() ? "undefined" : zzgVar.zza((zzao) list.get(0)).zzc()).matcher(this.zza);
                        return matcher.find() ? new zzah(Double.valueOf(matcher.start())) : new zzah(Double.valueOf(-1.0d));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -726908483:
                    if (str.equals(str2)) {
                        zzh.zza(str2, 0, list);
                        return new zzas(this.zza.toUpperCase());
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -467511597:
                    if (str.equals("lastIndexOf")) {
                        zzh.zzc("lastIndexOf", 2, list);
                        String str11 = this.zza;
                        String strZzc4 = list.size() > 0 ? zzgVar.zza((zzao) list.get(0)).zzc() : "undefined";
                        if (list.size() < 2) {
                            dDoubleValue = Double.NaN;
                        } else {
                            dDoubleValue = zzgVar.zza((zzao) list.get(1)).zzd().doubleValue();
                        }
                        if (Double.isNaN(dDoubleValue)) {
                            dZzi = Double.POSITIVE_INFINITY;
                        } else {
                            dZzi = zzh.zzi(dDoubleValue);
                        }
                        return new zzah(Double.valueOf(str11.lastIndexOf(strZzc4, (int) dZzi)));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case -399551817:
                    i6 = i;
                    if (str.equals("toUpperCase")) {
                        zzh.zza("toUpperCase", i6, list);
                        return new zzas(this.zza.toUpperCase(Locale.ENGLISH));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 3568674:
                    i7 = i;
                    if (str.equals(str3)) {
                        zzh.zza("toUpperCase", i7, list);
                        return new zzas(this.zza.trim());
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 103668165:
                    if (str.equals("match")) {
                        zzh.zzc("match", 1, list);
                        String str12 = this.zza;
                        if (list.size() <= 0) {
                            strZzc2 = _UrlKt.FRAGMENT_ENCODE_SET;
                        } else {
                            strZzc2 = zzgVar.zza((zzao) list.get(0)).zzc();
                        }
                        Matcher matcher2 = Pattern.compile(strZzc2).matcher(str12);
                        return matcher2.find() ? new zzae(Arrays.asList(new zzas(matcher2.group()))) : zzao.zzg;
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 109526418:
                    if (str.equals("slice")) {
                        zzh.zzc("slice", 2, list);
                        str5 = this.zza;
                        if (list.isEmpty()) {
                            dDoubleValue2 = 0.0d;
                        } else {
                            dDoubleValue2 = zzgVar.zza((zzao) list.get(0)).zzd().doubleValue();
                        }
                        dZzi2 = zzh.zzi(dDoubleValue2);
                        if (dZzi2 < 0.0d) {
                            dMin = Math.max(((double) str5.length()) + dZzi2, 0.0d);
                        } else {
                            dMin = Math.min(dZzi2, str5.length());
                        }
                        if (list.size() > 1) {
                            length = zzgVar.zza((zzao) list.get(1)).zzd().doubleValue();
                        } else {
                            length = str5.length();
                        }
                        dZzi3 = zzh.zzi(length);
                        if (dZzi3 < 0.0d) {
                            dMin2 = Math.max(((double) str5.length()) + dZzi3, 0.0d);
                        } else {
                            dMin2 = Math.min(dZzi3, str5.length());
                        }
                        int i11 = (int) dMin;
                        return new zzas(str5.substring(i11, Math.max(0, ((int) dMin2) - i11) + i11));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 109648666:
                    if (str.equals("split")) {
                        zzh.zzc("split", 2, list);
                        str6 = this.zza;
                        if (str6.length() == 0) {
                            return new zzae(Arrays.asList(this));
                        }
                        arrayList = new ArrayList();
                        if (list.isEmpty()) {
                            arrayList.add(this);
                        } else {
                            strZzc3 = zzgVar.zza((zzao) list.get(0)).zzc();
                            if (list.size() > 1) {
                                jZzh = zzh.zzh(zzgVar.zza((zzao) list.get(1)).zzd().doubleValue());
                            } else {
                                jZzh = 2147483647L;
                            }
                            if (jZzh == 0) {
                                return new zzae();
                            }
                            strArrSplit = str6.split(Pattern.quote(strZzc3), ((int) jZzh) + 1);
                            length2 = strArrSplit.length;
                            if (strZzc3.isEmpty() || length2 <= 0) {
                                i8 = length2;
                                i9 = 0;
                            } else {
                                zIsEmpty = strArrSplit[0].isEmpty();
                                i8 = length2 - 1;
                                if (!strArrSplit[i8].isEmpty()) {
                                    i9 = zIsEmpty;
                                    i8 = length2;
                                    i9 = zIsEmpty;
                                }
                            }
                            i9 = zIsEmpty;
                            if (length2 > jZzh) {
                                i8--;
                            }
                            while (i9 < i8) {
                                arrayList.add(new zzas(strArrSplit[i9]));
                                i9++;
                            }
                        }
                        return new zzae(arrayList);
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 530542161:
                    if (str.equals("substring")) {
                        zzh.zzc("substring", 2, list);
                        str7 = this.zza;
                        if (list.isEmpty()) {
                            iZzi2 = 0;
                        } else {
                            iZzi2 = (int) zzh.zzi(zzgVar.zza((zzao) list.get(0)).zzd().doubleValue());
                        }
                        if (list.size() > 1) {
                            length3 = (int) zzh.zzi(zzgVar.zza((zzao) list.get(1)).zzd().doubleValue());
                        } else {
                            length3 = str7.length();
                        }
                        int iMin = Math.min(Math.max(iZzi2, 0), str7.length());
                        int iMin2 = Math.min(Math.max(length3, 0), str7.length());
                        return new zzas(str7.substring(Math.min(iMin, iMin2), Math.max(iMin, iMin2)));
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 1094496948:
                    zzasVar = this;
                    if (str.equals("replace")) {
                        zzh.zzc("replace", 2, list);
                        zzaoVarZza2 = zzao.zzf;
                        if (!list.isEmpty()) {
                            strZzc = zzgVar.zza((zzao) list.get(0)).zzc();
                            if (list.size() > 1) {
                                zzaoVarZza2 = zzgVar.zza((zzao) list.get(1));
                            }
                        }
                        str8 = strZzc;
                        str9 = zzasVar.zza;
                        iIndexOf = str9.indexOf(str8);
                        if (iIndexOf >= 0) {
                            if (zzaoVarZza2 instanceof zzai) {
                                i10 = 0;
                                zzaoVarZza2 = ((zzai) zzaoVarZza2).zza(zzgVar, Arrays.asList(new zzas(str8), new zzah(Double.valueOf(iIndexOf)), zzasVar));
                            } else {
                                i10 = 0;
                            }
                            String strSubstring = str9.substring(i10, iIndexOf);
                            String strZzc5 = zzaoVarZza2.zzc();
                            String strSubstring2 = str9.substring(iIndexOf + str8.length());
                            StringBuilder sb2 = new StringBuilder(String.valueOf(strSubstring).length() + String.valueOf(strZzc5).length() + String.valueOf(strSubstring2).length());
                            sb2.append(strSubstring);
                            sb2.append(strZzc5);
                            sb2.append(strSubstring2);
                            return new zzas(sb2.toString());
                        }
                        return zzasVar;
                    }
                    throw new IllegalArgumentException("Command not supported");
                case 1943291465:
                    if (str.equals("indexOf")) {
                        zzh.zzc("indexOf", 2, list);
                        String str13 = this.zza;
                        if (list.size() <= 0) {
                            zzgVar2 = zzgVar;
                        } else {
                            zzgVar2 = zzgVar;
                            strZzc = zzgVar2.zza((zzao) list.get(i)).zzc();
                        }
                        String str14 = strZzc;
                        if (list.size() < 2) {
                            dDoubleValue3 = 0.0d;
                        } else {
                            dDoubleValue3 = zzgVar2.zza((zzao) list.get(1)).zzd().doubleValue();
                        }
                        return new zzah(Double.valueOf(str13.indexOf(str14, (int) zzh.zzi(dDoubleValue3))));
                    }
                default:
                    throw new IllegalArgumentException("Command not supported");
            }
        }
        str3 = "trim";
        strZzc = "undefined";
        switch (str.hashCode()) {
            case -1789698943:
                i2 = i;
                if (str.equals("hasOwnProperty")) {
                    zzh.zza("hasOwnProperty", 1, list);
                    str4 = this.zza;
                    zzaoVarZza = zzgVar.zza((zzao) list.get(i2));
                    if ("length".equals(zzaoVarZza.zzc())) {
                        return zzao.zzk;
                    }
                    double dDoubleValue5 = zzaoVarZza.zzd().doubleValue();
                    if (dDoubleValue5 == Math.floor(dDoubleValue5)) {
                    }
                    break;
                }
                throw new IllegalArgumentException("Command not supported");
            case -1776922004:
                i4 = i;
                if (str.equals("toString")) {
                    zzh.zza("toString", i4, list);
                    return this;
                }
                throw new IllegalArgumentException("Command not supported");
            case -1464939364:
                if (str.equals("toLocaleLowerCase")) {
                    zzh.zza("toLocaleLowerCase", 0, list);
                    return new zzas(this.zza.toLowerCase());
                }
                throw new IllegalArgumentException("Command not supported");
            case -1361633751:
                if (str.equals("charAt")) {
                    zzh.zzc("charAt", 1, list);
                    if (list.isEmpty()) {
                        iZzi = (int) zzh.zzi(zzgVar.zza((zzao) list.get(0)).zzd().doubleValue());
                    } else {
                        iZzi = 0;
                    }
                    String str15 = this.zza;
                    if (iZzi >= 0) {
                    }
                    break;
                }
                throw new IllegalArgumentException("Command not supported");
            case -1354795244:
                zzasVar = this;
                if (str.equals("concat")) {
                    if (!list.isEmpty()) {
                        sb = new StringBuilder(zzasVar.zza);
                        while (i5 < list.size()) {
                            sb.append(zzgVar.zza((zzao) list.get(i5)).zzc());
                        }
                        return new zzas(sb.toString());
                    }
                    return zzasVar;
                }
                throw new IllegalArgumentException("Command not supported");
            case -1137582698:
                if (str.equals("toLowerCase")) {
                    zzh.zza("toLowerCase", 0, list);
                    return new zzas(this.zza.toLowerCase(Locale.ENGLISH));
                }
                throw new IllegalArgumentException("Command not supported");
            case -906336856:
                if (str.equals("search")) {
                    zzh.zzc("search", 1, list);
                    Matcher matcher3 = Pattern.compile(list.isEmpty() ? "undefined" : zzgVar.zza((zzao) list.get(0)).zzc()).matcher(this.zza);
                    if (matcher3.find()) {
                    }
                }
                throw new IllegalArgumentException("Command not supported");
            case -726908483:
                if (str.equals(str2)) {
                    zzh.zza(str2, 0, list);
                    return new zzas(this.zza.toUpperCase());
                }
                throw new IllegalArgumentException("Command not supported");
            case -467511597:
                if (str.equals("lastIndexOf")) {
                    zzh.zzc("lastIndexOf", 2, list);
                    String str16 = this.zza;
                    String strZzc6 = list.size() > 0 ? zzgVar.zza((zzao) list.get(0)).zzc() : "undefined";
                    if (list.size() < 2) {
                        dDoubleValue = Double.NaN;
                    } else {
                        dDoubleValue = zzgVar.zza((zzao) list.get(1)).zzd().doubleValue();
                    }
                    if (Double.isNaN(dDoubleValue)) {
                        dZzi = Double.POSITIVE_INFINITY;
                    } else {
                        dZzi = zzh.zzi(dDoubleValue);
                    }
                    return new zzah(Double.valueOf(str16.lastIndexOf(strZzc6, (int) dZzi)));
                }
                throw new IllegalArgumentException("Command not supported");
            case -399551817:
                i6 = i;
                if (str.equals("toUpperCase")) {
                    zzh.zza("toUpperCase", i6, list);
                    return new zzas(this.zza.toUpperCase(Locale.ENGLISH));
                }
                throw new IllegalArgumentException("Command not supported");
            case 3568674:
                i7 = i;
                if (str.equals(str3)) {
                    zzh.zza("toUpperCase", i7, list);
                    return new zzas(this.zza.trim());
                }
                throw new IllegalArgumentException("Command not supported");
            case 103668165:
                if (str.equals("match")) {
                    zzh.zzc("match", 1, list);
                    String str17 = this.zza;
                    if (list.size() <= 0) {
                        strZzc2 = _UrlKt.FRAGMENT_ENCODE_SET;
                    } else {
                        strZzc2 = zzgVar.zza((zzao) list.get(0)).zzc();
                    }
                    Matcher matcher4 = Pattern.compile(strZzc2).matcher(str17);
                    if (matcher4.find()) {
                    }
                }
                throw new IllegalArgumentException("Command not supported");
            case 109526418:
                if (str.equals("slice")) {
                    zzh.zzc("slice", 2, list);
                    str5 = this.zza;
                    if (list.isEmpty()) {
                        dDoubleValue2 = zzgVar.zza((zzao) list.get(0)).zzd().doubleValue();
                    } else {
                        dDoubleValue2 = 0.0d;
                    }
                    dZzi2 = zzh.zzi(dDoubleValue2);
                    if (dZzi2 < 0.0d) {
                        dMin = Math.max(((double) str5.length()) + dZzi2, 0.0d);
                    } else {
                        dMin = Math.min(dZzi2, str5.length());
                    }
                    if (list.size() > 1) {
                        length = zzgVar.zza((zzao) list.get(1)).zzd().doubleValue();
                    } else {
                        length = str5.length();
                    }
                    dZzi3 = zzh.zzi(length);
                    if (dZzi3 < 0.0d) {
                        dMin2 = Math.max(((double) str5.length()) + dZzi3, 0.0d);
                    } else {
                        dMin2 = Math.min(dZzi3, str5.length());
                    }
                    int i12 = (int) dMin;
                    return new zzas(str5.substring(i12, Math.max(0, ((int) dMin2) - i12) + i12));
                }
                throw new IllegalArgumentException("Command not supported");
            case 109648666:
                if (str.equals("split")) {
                    zzh.zzc("split", 2, list);
                    str6 = this.zza;
                    if (str6.length() == 0) {
                        return new zzae(Arrays.asList(this));
                    }
                    arrayList = new ArrayList();
                    if (list.isEmpty()) {
                        arrayList.add(this);
                    } else {
                        strZzc3 = zzgVar.zza((zzao) list.get(0)).zzc();
                        if (list.size() > 1) {
                            jZzh = zzh.zzh(zzgVar.zza((zzao) list.get(1)).zzd().doubleValue());
                        } else {
                            jZzh = 2147483647L;
                        }
                        if (jZzh == 0) {
                            return new zzae();
                        }
                        strArrSplit = str6.split(Pattern.quote(strZzc3), ((int) jZzh) + 1);
                        length2 = strArrSplit.length;
                        if (strZzc3.isEmpty()) {
                            i8 = length2;
                            i9 = 0;
                        } else {
                            i8 = length2;
                            i9 = 0;
                        }
                        i9 = zIsEmpty;
                        if (length2 > jZzh) {
                            i8--;
                        }
                        while (i9 < i8) {
                            arrayList.add(new zzas(strArrSplit[i9]));
                            i9++;
                        }
                    }
                    return new zzae(arrayList);
                }
                throw new IllegalArgumentException("Command not supported");
            case 530542161:
                if (str.equals("substring")) {
                    zzh.zzc("substring", 2, list);
                    str7 = this.zza;
                    if (list.isEmpty()) {
                        iZzi2 = (int) zzh.zzi(zzgVar.zza((zzao) list.get(0)).zzd().doubleValue());
                    } else {
                        iZzi2 = 0;
                    }
                    if (list.size() > 1) {
                        length3 = (int) zzh.zzi(zzgVar.zza((zzao) list.get(1)).zzd().doubleValue());
                    } else {
                        length3 = str7.length();
                    }
                    int iMin3 = Math.min(Math.max(iZzi2, 0), str7.length());
                    int iMin4 = Math.min(Math.max(length3, 0), str7.length());
                    return new zzas(str7.substring(Math.min(iMin3, iMin4), Math.max(iMin3, iMin4)));
                }
                throw new IllegalArgumentException("Command not supported");
            case 1094496948:
                zzasVar = this;
                if (str.equals("replace")) {
                    zzh.zzc("replace", 2, list);
                    zzaoVarZza2 = zzao.zzf;
                    if (!list.isEmpty()) {
                        strZzc = zzgVar.zza((zzao) list.get(0)).zzc();
                        if (list.size() > 1) {
                            zzaoVarZza2 = zzgVar.zza((zzao) list.get(1));
                        }
                    }
                    str8 = strZzc;
                    str9 = zzasVar.zza;
                    iIndexOf = str9.indexOf(str8);
                    if (iIndexOf >= 0) {
                        if (zzaoVarZza2 instanceof zzai) {
                            i10 = 0;
                            zzaoVarZza2 = ((zzai) zzaoVarZza2).zza(zzgVar, Arrays.asList(new zzas(str8), new zzah(Double.valueOf(iIndexOf)), zzasVar));
                        } else {
                            i10 = 0;
                        }
                        String strSubstring3 = str9.substring(i10, iIndexOf);
                        String strZzc7 = zzaoVarZza2.zzc();
                        String strSubstring4 = str9.substring(iIndexOf + str8.length());
                        StringBuilder sb3 = new StringBuilder(String.valueOf(strSubstring3).length() + String.valueOf(strZzc7).length() + String.valueOf(strSubstring4).length());
                        sb3.append(strSubstring3);
                        sb3.append(strZzc7);
                        sb3.append(strSubstring4);
                        return new zzas(sb3.toString());
                    }
                    return zzasVar;
                }
                throw new IllegalArgumentException("Command not supported");
            case 1943291465:
                if (str.equals("indexOf")) {
                    zzh.zzc("indexOf", 2, list);
                    String str18 = this.zza;
                    if (list.size() <= 0) {
                        zzgVar2 = zzgVar;
                    } else {
                        zzgVar2 = zzgVar;
                        strZzc = zzgVar2.zza((zzao) list.get(i)).zzc();
                    }
                    String str19 = strZzc;
                    if (list.size() < 2) {
                        dDoubleValue3 = 0.0d;
                    } else {
                        dDoubleValue3 = zzgVar2.zza((zzao) list.get(1)).zzd().doubleValue();
                    }
                    return new zzah(Double.valueOf(str18.indexOf(str19, (int) zzh.zzi(dDoubleValue3))));
                }
            default:
                throw new IllegalArgumentException("Command not supported");
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzao
    public final Double zzd() {
        String str = this.zza;
        if (str.isEmpty()) {
            return Double.valueOf(0.0d);
        }
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException unused) {
            return Double.valueOf(Double.NaN);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzao
    public final Boolean zze() {
        return Boolean.valueOf(!this.zza.isEmpty());
    }

    @Override // com.google.android.gms.internal.measurement.zzao
    public final Iterator zzf() {
        return new zzaq(this);
    }

    @Override // com.google.android.gms.internal.measurement.zzao
    public final zzao zzt() {
        return new zzas(this.zza);
    }
}
