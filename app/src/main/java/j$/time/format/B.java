package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
import java.text.DateFormatSymbols;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import okhttp3.internal.url._UrlKt;

public class B {
    public static final ConcurrentHashMap a = new ConcurrentHashMap(16, 0.75f, 2);
    public static final z b = new z();
    public static final B c = new B();

    public String c(j$.time.temporal.r rVar, long j, TextStyle textStyle, Locale locale) {
        Object objA = a(rVar, locale);
        if (objA instanceof A) {
            return ((A) objA).a(j, textStyle);
        }
        return null;
    }

    public String b(j$.time.chrono.k kVar, j$.time.temporal.r rVar, long j, TextStyle textStyle, Locale locale) {
        if (kVar == j$.time.chrono.r.c || !(rVar instanceof j$.time.temporal.a)) {
            return c(rVar, j, textStyle, locale);
        }
        return null;
    }

    public Iterator e(j$.time.temporal.r rVar, TextStyle textStyle, Locale locale) {
        List list;
        Object objA = a(rVar, locale);
        if (!(objA instanceof A) || (list = (List) ((HashMap) ((A) objA).b).get(textStyle)) == null) {
            return null;
        }
        return list.iterator();
    }

    public Iterator d(j$.time.chrono.k kVar, j$.time.temporal.r rVar, TextStyle textStyle, Locale locale) {
        if (kVar == j$.time.chrono.r.c || !(rVar instanceof j$.time.temporal.a)) {
            return e(rVar, textStyle, locale);
        }
        return null;
    }

    public static Object a(j$.time.temporal.r rVar, Locale locale) {
        Object a2;
        String strSubstring;
        AbstractMap.SimpleImmutableEntry simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry(rVar, locale);
        ConcurrentHashMap concurrentHashMap = a;
        V v = concurrentHashMap.get(simpleImmutableEntry);
        if (v != 0) {
            return v;
        }
        HashMap map = new HashMap();
        if (rVar == j$.time.temporal.a.ERA) {
            DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
            HashMap map2 = new HashMap();
            HashMap map3 = new HashMap();
            String[] eras = dateFormatSymbols.getEras();
            for (int i = 0; i < eras.length; i++) {
                if (!eras[i].isEmpty()) {
                    long j = i;
                    map2.put(Long.valueOf(j), eras[i]);
                    Long lValueOf = Long.valueOf(j);
                    String str = eras[i];
                    map3.put(lValueOf, str.substring(0, Character.charCount(str.codePointAt(0))));
                }
            }
            if (!map2.isEmpty()) {
                map.put(TextStyle.FULL, map2);
                map.put(TextStyle.SHORT, map2);
                map.put(TextStyle.NARROW, map3);
            }
            a2 = new A(map);
        } else {
            long j2 = 1;
            if (rVar == j$.time.temporal.a.MONTH_OF_YEAR) {
                int length = DateFormatSymbols.getInstance(locale).getMonths().length;
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                LinkedHashMap linkedHashMap3 = new LinkedHashMap();
                for (long j3 = 1; j3 <= length; j3++) {
                    String strD = j$.com.android.tools.r8.a.D(j3, "LLLL", locale);
                    linkedHashMap.put(Long.valueOf(j3), strD);
                    linkedHashMap2.put(Long.valueOf(j3), strD.substring(0, Character.charCount(strD.codePointAt(0))));
                    linkedHashMap3.put(Long.valueOf(j3), j$.com.android.tools.r8.a.D(j3, "LLL", locale));
                }
                if (length > 0) {
                    map.put(TextStyle.FULL_STANDALONE, linkedHashMap);
                    map.put(TextStyle.NARROW_STANDALONE, linkedHashMap2);
                    map.put(TextStyle.SHORT_STANDALONE, linkedHashMap3);
                    map.put(TextStyle.FULL, linkedHashMap);
                    map.put(TextStyle.NARROW, linkedHashMap2);
                    map.put(TextStyle.SHORT, linkedHashMap3);
                }
                a2 = new A(map);
            } else if (rVar == j$.time.temporal.a.DAY_OF_WEEK) {
                int length2 = DateFormatSymbols.getInstance(locale).getWeekdays().length;
                LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                LinkedHashMap linkedHashMap5 = new LinkedHashMap();
                LinkedHashMap linkedHashMap6 = new LinkedHashMap();
                boolean z = locale == Locale.SIMPLIFIED_CHINESE || locale == Locale.TRADITIONAL_CHINESE;
                long j4 = 1;
                while (j4 <= length2) {
                    String strC = j$.com.android.tools.r8.a.C(j4, "cccc", locale);
                    linkedHashMap4.put(Long.valueOf(j4), strC);
                    Long lValueOf2 = Long.valueOf(j4);
                    if (!z) {
                        strSubstring = strC.substring(0, Character.charCount(strC.codePointAt(0)));
                    } else {
                        strSubstring = new StringBuilder().appendCodePoint(strC.codePointBefore(strC.length())).toString();
                    }
                    linkedHashMap5.put(lValueOf2, strSubstring);
                    linkedHashMap6.put(Long.valueOf(j4), j$.com.android.tools.r8.a.C(j4, "ccc", locale));
                    j4 += j2;
                    j2 = j2;
                }
                if (length2 > 0) {
                    map.put(TextStyle.FULL_STANDALONE, linkedHashMap4);
                    map.put(TextStyle.NARROW_STANDALONE, linkedHashMap5);
                    map.put(TextStyle.SHORT_STANDALONE, linkedHashMap6);
                    map.put(TextStyle.FULL, linkedHashMap4);
                    map.put(TextStyle.NARROW, linkedHashMap5);
                    map.put(TextStyle.SHORT, linkedHashMap6);
                }
                a2 = new A(map);
            } else if (rVar == j$.time.temporal.a.AMPM_OF_DAY) {
                DateFormatSymbols dateFormatSymbols2 = DateFormatSymbols.getInstance(locale);
                HashMap map4 = new HashMap();
                HashMap map5 = new HashMap();
                String[] amPmStrings = dateFormatSymbols2.getAmPmStrings();
                for (int i2 = 0; i2 < amPmStrings.length; i2++) {
                    if (!amPmStrings[i2].isEmpty()) {
                        long j5 = i2;
                        map4.put(Long.valueOf(j5), amPmStrings[i2]);
                        Long lValueOf3 = Long.valueOf(j5);
                        String str2 = amPmStrings[i2];
                        map5.put(lValueOf3, str2.substring(0, Character.charCount(str2.codePointAt(0))));
                    }
                }
                if (!map4.isEmpty()) {
                    map.put(TextStyle.FULL, map4);
                    map.put(TextStyle.SHORT, map4);
                    map.put(TextStyle.NARROW, map5);
                }
                a2 = new A(map);
            } else {
                a2 = _UrlKt.FRAGMENT_ENCODE_SET;
            }
        }
        concurrentHashMap.putIfAbsent(simpleImmutableEntry, a2);
        return concurrentHashMap.get(simpleImmutableEntry);
    }
}
