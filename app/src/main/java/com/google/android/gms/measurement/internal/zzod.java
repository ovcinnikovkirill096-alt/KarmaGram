package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.common.collect.ImmutableList;
import j$.util.Objects;
import java.util.HashMap;
import java.util.Map;
import org.mvel2.MVEL;

public final class zzod {
    private final Map zza;

    zzod(Map map) {
        HashMap map2 = new HashMap();
        this.zza = map2;
        map2.putAll(map);
    }

    private final Bundle zzf() {
        int iZzg;
        Map map = this.zza;
        if ("1".equals(map.get("GoogleConsent")) && (iZzg = zzg()) >= 0) {
            String str = (String) map.get("PurposeConsents");
            if (!TextUtils.isEmpty(str)) {
                Bundle bundle = new Bundle();
                String str2 = "denied";
                if (str.length() > 0) {
                    bundle.putString(zzjk.AD_STORAGE.zze, str.charAt(0) == '1' ? "granted" : "denied");
                }
                if (str.length() > 3) {
                    bundle.putString(zzjk.AD_PERSONALIZATION.zze, (str.charAt(2) == '1' && str.charAt(3) == '1') ? "granted" : "denied");
                }
                if (str.length() > 6 && iZzg >= 4) {
                    String str3 = zzjk.AD_USER_DATA.zze;
                    if (str.charAt(0) == '1' && str.charAt(6) == '1') {
                        str2 = "granted";
                    }
                    bundle.putString(str3, str2);
                }
                return bundle;
            }
        }
        return Bundle.EMPTY;
    }

    private final int zzg() {
        try {
            String str = (String) this.zza.get("PolicyVersion");
            if (TextUtils.isEmpty(str)) {
                return -1;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public final boolean equals(Object obj) {
        if (obj instanceof zzod) {
            return zza().equalsIgnoreCase(((zzod) obj).zza());
        }
        return false;
    }

    public final int hashCode() {
        return zza().hashCode();
    }

    public final String toString() {
        return zza();
    }

    final String zza() {
        StringBuilder sb = new StringBuilder();
        ImmutableList immutableList = zzof.zza;
        int size = immutableList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) immutableList.get(i);
            Map map = this.zza;
            if (map.containsKey(str)) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(str);
                sb.append("=");
                sb.append((String) map.get(str));
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004e  */
    /* JADX WARN: Code duplicated, block: B:17:0x005a  */
    /* JADX WARN: Code duplicated, block: B:19:0x0064  */
    /* JADX WARN: Code duplicated, block: B:21:0x0069  */
    /* JADX WARN: Code duplicated, block: B:24:0x0071  */
    /* JADX WARN: Code duplicated, block: B:26:0x008b  */
    /* JADX WARN: Code duplicated, block: B:27:0x008d  */
    /* JADX WARN: Code duplicated, block: B:33:0x00af  */
    /* JADX WARN: Code duplicated, block: B:36:0x00ba  */
    /* JADX WARN: Code duplicated, block: B:41:0x00d5  */
    /* JADX WARN: Code duplicated, block: B:44:0x00da  */
    /* JADX WARN: Instruction removed from duplicated block: B:24:0x0071, please report this as an issue */
    public final Bundle zzb() {
        Map map;
        Bundle bundle;
        String str;
        String str2;
        zzfx zzfxVar = zzfy.zzaZ;
        if (((Boolean) zzfxVar.zzb(null)).booleanValue()) {
            Map map2 = this.zza;
            if ("1".equals(map2.get("gdprApplies")) && "1".equals(map2.get("EnableAdvertiserConsentMode"))) {
                if (((Boolean) zzfxVar.zzb(null)).booleanValue()) {
                    return zzf();
                }
                map = this.zza;
                if (map.get("Version") == null) {
                    return zzf();
                }
                if (zzg() >= 0) {
                    bundle = new Bundle();
                    String str3 = zzjk.AD_STORAGE.zze;
                    if (true != Objects.equals(map.get("AuthorizePurpose1"), "1")) {
                        str = "denied";
                    } else {
                        str = "granted";
                    }
                    bundle.putString(str3, str);
                    String str4 = zzjk.AD_PERSONALIZATION.zze;
                    if (Objects.equals(map.get("AuthorizePurpose3"), "1") || !Objects.equals(map.get("AuthorizePurpose4"), "1")) {
                        str2 = "denied";
                    } else {
                        str2 = "granted";
                    }
                    bundle.putString(str4, str2);
                    if (zzg() >= 4) {
                        bundle.putString(zzjk.AD_USER_DATA.zze, (Objects.equals(map.get("AuthorizePurpose1"), "1") || !Objects.equals(map.get("AuthorizePurpose7"), "1")) ? "denied" : "granted");
                    }
                    return bundle;
                }
            }
        } else {
            Map map3 = this.zza;
            if ("1".equals(map3.get("GoogleConsent")) && "1".equals(map3.get("gdprApplies")) && "1".equals(map3.get("EnableAdvertiserConsentMode"))) {
                if (((Boolean) zzfxVar.zzb(null)).booleanValue()) {
                    return zzf();
                }
                map = this.zza;
                if (map.get("Version") == null) {
                    return zzf();
                }
                if (zzg() >= 0) {
                    bundle = new Bundle();
                    String str5 = zzjk.AD_STORAGE.zze;
                    if (true != Objects.equals(map.get("AuthorizePurpose1"), "1")) {
                        str = "denied";
                    } else {
                        str = "granted";
                    }
                    bundle.putString(str5, str);
                    String str6 = zzjk.AD_PERSONALIZATION.zze;
                    if (Objects.equals(map.get("AuthorizePurpose3"), "1")) {
                        str2 = "denied";
                    } else {
                        str2 = "denied";
                    }
                    bundle.putString(str6, str2);
                    if (zzg() >= 4) {
                        bundle.putString(zzjk.AD_USER_DATA.zze, (Objects.equals(map.get("AuthorizePurpose1"), "1") || !Objects.equals(map.get("AuthorizePurpose7"), "1")) ? "denied" : "granted");
                    }
                    return bundle;
                }
            }
        }
        return Bundle.EMPTY;
    }

    public final String zzc() {
        String str = (String) this.zza.get("PurposeDiagnostics");
        return TextUtils.isEmpty(str) ? "200000" : str;
    }

    public final String zzd(zzod zzodVar) {
        Map map = zzodVar.zza;
        boolean zIsEmpty = map.isEmpty();
        String str = MVEL.VERSION_SUB;
        String str2 = (zIsEmpty || ((String) map.get("Version")) != null) ? MVEL.VERSION_SUB : "1";
        Bundle bundleZzb = zzb();
        Bundle bundleZzb2 = zzodVar.zzb();
        if (bundleZzb.size() != bundleZzb2.size() || !Objects.equals(bundleZzb.getString("ad_storage"), bundleZzb2.getString("ad_storage")) || !Objects.equals(bundleZzb.getString("ad_personalization"), bundleZzb2.getString("ad_personalization")) || !Objects.equals(bundleZzb.getString("ad_user_data"), bundleZzb2.getString("ad_user_data"))) {
            str = "1";
        }
        return str2.concat(str);
    }

    public final String zze() {
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        int i = -1;
        try {
            String str = (String) this.zza.get("CmpSdkID");
            if (!TextUtils.isEmpty(str)) {
                i = Integer.parseInt(str);
            }
        } catch (NumberFormatException unused) {
        }
        if (i < 0 || i > 4095) {
            sb.append("00");
        } else {
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i >> 6));
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i & 63));
        }
        int iZzg = zzg();
        if (iZzg < 0 || iZzg > 63) {
            sb.append(MVEL.VERSION_SUB);
        } else {
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(iZzg));
        }
        Preconditions.checkArgument(true);
        Map map = this.zza;
        int i2 = true != "1".equals(map.get("gdprApplies")) ? 0 : 2;
        boolean zEquals = "1".equals(map.get("EnableAdvertiserConsentMode"));
        int i3 = i2 | 4;
        if (zEquals) {
            i3 = i2 | 12;
        }
        sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i3));
        return sb.toString();
    }
}
