package j$.time;

import j$.time.format.E;
import j$.time.format.TextStyle;
import j$.time.zone.ZoneRules;
import j$.util.Objects;
import java.io.DataOutput;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public abstract class ZoneId implements Serializable {
    public static final Map a;
    private static final long serialVersionUID = 8352817235686L;

    public abstract void T(DataOutput dataOutput);

    public abstract String getId();

    public abstract ZoneRules getRules();

    static {
        Map.Entry[] entryArr = {j$.com.android.tools.r8.a.R("ACT", "Australia/Darwin"), j$.com.android.tools.r8.a.R("AET", "Australia/Sydney"), j$.com.android.tools.r8.a.R("AGT", "America/Argentina/Buenos_Aires"), j$.com.android.tools.r8.a.R("ART", "Africa/Cairo"), j$.com.android.tools.r8.a.R("AST", "America/Anchorage"), j$.com.android.tools.r8.a.R("BET", "America/Sao_Paulo"), j$.com.android.tools.r8.a.R("BST", "Asia/Dhaka"), j$.com.android.tools.r8.a.R("CAT", "Africa/Harare"), j$.com.android.tools.r8.a.R("CNT", "America/St_Johns"), j$.com.android.tools.r8.a.R("CST", "America/Chicago"), j$.com.android.tools.r8.a.R("CTT", "Asia/Shanghai"), j$.com.android.tools.r8.a.R("EAT", "Africa/Addis_Ababa"), j$.com.android.tools.r8.a.R("ECT", "Europe/Paris"), j$.com.android.tools.r8.a.R("IET", "America/Indiana/Indianapolis"), j$.com.android.tools.r8.a.R("IST", "Asia/Kolkata"), j$.com.android.tools.r8.a.R("JST", "Asia/Tokyo"), j$.com.android.tools.r8.a.R("MIT", "Pacific/Apia"), j$.com.android.tools.r8.a.R("NET", "Asia/Yerevan"), j$.com.android.tools.r8.a.R("NST", "Pacific/Auckland"), j$.com.android.tools.r8.a.R("PLT", "Asia/Karachi"), j$.com.android.tools.r8.a.R("PNT", "America/Phoenix"), j$.com.android.tools.r8.a.R("PRT", "America/Puerto_Rico"), j$.com.android.tools.r8.a.R("PST", "America/Los_Angeles"), j$.com.android.tools.r8.a.R("SST", "Pacific/Guadalcanal"), j$.com.android.tools.r8.a.R("VST", "Asia/Ho_Chi_Minh"), j$.com.android.tools.r8.a.R("EST", "-05:00"), j$.com.android.tools.r8.a.R("MST", "-07:00"), j$.com.android.tools.r8.a.R("HST", "-10:00")};
        HashMap map = new HashMap(28);
        for (int i = 0; i < 28; i++) {
            Map.Entry entry = entryArr[i];
            Object objRequireNonNull = Objects.requireNonNull(entry.getKey());
            if (map.put(objRequireNonNull, Objects.requireNonNull(entry.getValue())) != null) {
                throw new IllegalArgumentException("duplicate key: " + objRequireNonNull);
            }
        }
        a = Collections.unmodifiableMap(map);
    }

    public static ZoneId systemDefault() {
        String id = TimeZone.getDefault().getID();
        Map map = a;
        Objects.requireNonNull(id, "zoneId");
        Objects.requireNonNull(map, "aliasMap");
        return of((String) Objects.requireNonNullElse((String) map.get(id), id));
    }

    public static ZoneId of(String str) {
        return Q(str, true);
    }

    public static ZoneId R(String str, ZoneOffset zoneOffset) {
        Objects.requireNonNull(str, "prefix");
        Objects.requireNonNull(zoneOffset, "offset");
        if (str.isEmpty()) {
            return zoneOffset;
        }
        if (!str.equals("GMT") && !str.equals("UTC") && !str.equals("UT")) {
            throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: ".concat(str));
        }
        if (zoneOffset.getTotalSeconds() != 0) {
            str = str.concat(zoneOffset.c);
        }
        return new u(str, zoneOffset.getRules());
    }

    public static ZoneId Q(String str, boolean z) {
        Objects.requireNonNull(str, "zoneId");
        if (str.length() <= 1 || str.startsWith("+") || str.startsWith("-")) {
            return ZoneOffset.U(str);
        }
        if (str.startsWith("UTC") || str.startsWith("GMT")) {
            return S(str, 3, z);
        }
        if (str.startsWith("UT")) {
            return S(str, 2, z);
        }
        return u.U(str, z);
    }

    public static ZoneId S(String str, int i, boolean z) {
        String strSubstring = str.substring(0, i);
        if (str.length() == i) {
            return R(strSubstring, ZoneOffset.UTC);
        }
        if (str.charAt(i) != '+' && str.charAt(i) != '-') {
            return u.U(str, z);
        }
        try {
            ZoneOffset zoneOffsetU = ZoneOffset.U(str.substring(i));
            if (zoneOffsetU == ZoneOffset.UTC) {
                return R(strSubstring, zoneOffsetU);
            }
            return R(strSubstring, zoneOffsetU);
        } catch (b e) {
            throw new b("Invalid ID for offset-based ZoneId: ".concat(str), e);
        }
    }

    public ZoneId() {
        if (getClass() != ZoneOffset.class && getClass() != u.class) {
            throw new AssertionError("Invalid subclass");
        }
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        j$.time.format.u uVar = new j$.time.format.u();
        uVar.c(new j$.time.format.t(textStyle, false));
        return uVar.q(locale, E.SMART, null).a(new t(0, this));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneId) {
            return getId().equals(((ZoneId) obj).getId());
        }
        return false;
    }

    public int hashCode() {
        return getId().hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public String toString() {
        return getId();
    }

    private Object writeReplace() {
        return new p((byte) 7, this);
    }
}
