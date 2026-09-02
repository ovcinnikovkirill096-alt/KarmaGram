package okhttp3.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.internal.url._UrlKt;

public final class _CacheControlCommonKt {
    public static final int commonClampToInt(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) j;
    }

    public static final String commonToString(CacheControl cacheControl) {
        Intrinsics.checkNotNullParameter(cacheControl, "<this>");
        String headerValue$okhttp = cacheControl.getHeaderValue$okhttp();
        if (headerValue$okhttp != null) {
            return headerValue$okhttp;
        }
        StringBuilder sb = new StringBuilder();
        if (cacheControl.noCache()) {
            sb.append("no-cache, ");
        }
        if (cacheControl.noStore()) {
            sb.append("no-store, ");
        }
        if (cacheControl.maxAgeSeconds() != -1) {
            sb.append("max-age=");
            sb.append(cacheControl.maxAgeSeconds());
            sb.append(", ");
        }
        if (cacheControl.sMaxAgeSeconds() != -1) {
            sb.append("s-maxage=");
            sb.append(cacheControl.sMaxAgeSeconds());
            sb.append(", ");
        }
        if (cacheControl.isPrivate()) {
            sb.append("private, ");
        }
        if (cacheControl.isPublic()) {
            sb.append("public, ");
        }
        if (cacheControl.mustRevalidate()) {
            sb.append("must-revalidate, ");
        }
        if (cacheControl.maxStaleSeconds() != -1) {
            sb.append("max-stale=");
            sb.append(cacheControl.maxStaleSeconds());
            sb.append(", ");
        }
        if (cacheControl.minFreshSeconds() != -1) {
            sb.append("min-fresh=");
            sb.append(cacheControl.minFreshSeconds());
            sb.append(", ");
        }
        if (cacheControl.onlyIfCached()) {
            sb.append("only-if-cached, ");
        }
        if (cacheControl.noTransform()) {
            sb.append("no-transform, ");
        }
        if (cacheControl.immutable()) {
            sb.append("immutable, ");
        }
        if (sb.length() == 0) {
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
        Intrinsics.checkNotNullExpressionValue(sb.delete(sb.length() - 2, sb.length()), "delete(...)");
        String string = sb.toString();
        cacheControl.setHeaderValue$okhttp(string);
        return string;
    }

    public static final CacheControl commonForceNetwork(CacheControl.Companion companion) {
        Intrinsics.checkNotNullParameter(companion, "<this>");
        return new CacheControl.Builder().noCache().build();
    }

    public static final CacheControl commonForceCache(CacheControl.Companion companion) {
        Intrinsics.checkNotNullParameter(companion, "<this>");
        CacheControl.Builder builderOnlyIfCached = new CacheControl.Builder().onlyIfCached();
        Duration.Companion companion2 = Duration.Companion;
        return builderOnlyIfCached.m2591maxStaleLRDsOJo(DurationKt.toDuration(Integer.MAX_VALUE, DurationUnit.SECONDS)).build();
    }

    public static final CacheControl commonBuild(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        return new CacheControl(builder.getNoCache$okhttp(), builder.getNoStore$okhttp(), builder.getMaxAgeSeconds$okhttp(), -1, false, false, false, builder.getMaxStaleSeconds$okhttp(), builder.getMinFreshSeconds$okhttp(), builder.getOnlyIfCached$okhttp(), builder.getNoTransform$okhttp(), builder.getImmutable$okhttp(), null);
    }

    public static final CacheControl.Builder commonNoCache(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        builder.setNoCache$okhttp(true);
        return builder;
    }

    public static final CacheControl.Builder commonNoStore(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        builder.setNoStore$okhttp(true);
        return builder;
    }

    public static final CacheControl.Builder commonOnlyIfCached(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        builder.setOnlyIfCached$okhttp(true);
        return builder;
    }

    public static final CacheControl.Builder commonNoTransform(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        builder.setNoTransform$okhttp(true);
        return builder;
    }

    public static final CacheControl.Builder commonImmutable(CacheControl.Builder builder) {
        Intrinsics.checkNotNullParameter(builder, "<this>");
        builder.setImmutable$okhttp(true);
        return builder;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004f  */
    /* JADX WARN: Code duplicated, block: B:17:0x006f  */
    /* JADX WARN: Code duplicated, block: B:28:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:32:0x00e2  */
    /* JADX WARN: Code duplicated, block: B:34:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:36:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:37:0x00fb  */
    /* JADX WARN: Code duplicated, block: B:39:0x0104  */
    /* JADX WARN: Code duplicated, block: B:41:0x010e  */
    /* JADX WARN: Code duplicated, block: B:43:0x0117  */
    /* JADX WARN: Code duplicated, block: B:44:0x011d  */
    /* JADX WARN: Code duplicated, block: B:46:0x0126  */
    /* JADX WARN: Code duplicated, block: B:47:0x012c  */
    /* JADX WARN: Code duplicated, block: B:49:0x0135  */
    /* JADX WARN: Code duplicated, block: B:50:0x013b  */
    /* JADX WARN: Code duplicated, block: B:52:0x0144  */
    /* JADX WARN: Code duplicated, block: B:53:0x014a  */
    /* JADX WARN: Code duplicated, block: B:55:0x0153  */
    /* JADX WARN: Code duplicated, block: B:56:0x015b  */
    /* JADX WARN: Code duplicated, block: B:58:0x0164  */
    /* JADX WARN: Code duplicated, block: B:59:0x016a  */
    /* JADX WARN: Code duplicated, block: B:61:0x0174  */
    /* JADX WARN: Code duplicated, block: B:62:0x017c  */
    /* JADX WARN: Code duplicated, block: B:64:0x0185  */
    /* JADX WARN: Code duplicated, block: B:65:0x018d  */
    /* JADX WARN: Code duplicated, block: B:67:0x0196  */
    public static final CacheControl commonParse(CacheControl.Companion companion, Headers headers) {
        int iIndexOfElement;
        int iIndexOfElement2;
        boolean z;
        String string;
        int i;
        String str;
        String string2;
        Headers headers2 = headers;
        Intrinsics.checkNotNullParameter(companion, "<this>");
        Intrinsics.checkNotNullParameter(headers2, "headers");
        int size = headers2.size();
        boolean z2 = true;
        boolean z3 = true;
        int i2 = 0;
        String str2 = null;
        boolean z4 = false;
        boolean z5 = false;
        int nonNegativeInt = -1;
        int nonNegativeInt2 = -1;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = false;
        int nonNegativeInt3 = -1;
        int nonNegativeInt4 = -1;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
        while (i2 < size) {
            String strName = headers2.name(i2);
            String strValue = headers2.value(i2);
            if (!StringsKt.equals(strName, "Cache-Control", z2)) {
                if (StringsKt.equals(strName, "Pragma", z2)) {
                }
                i2++;
                headers2 = headers;
                z2 = z2;
                size = size;
            } else {
                if (str2 == null) {
                    str2 = strValue;
                }
                iIndexOfElement = 0;
                while (iIndexOfElement < strValue.length()) {
                    iIndexOfElement2 = indexOfElement(strValue, "=,;", iIndexOfElement);
                    String strSubstring = strValue.substring(iIndexOfElement, iIndexOfElement2);
                    z = z2;
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                    string = StringsKt.trim(strSubstring).toString();
                    if (iIndexOfElement2 != strValue.length()) {
                        i = size;
                        if (strValue.charAt(iIndexOfElement2) == ',' && strValue.charAt(iIndexOfElement2) != ';') {
                            int iIndexOfNonWhitespace = _UtilCommonKt.indexOfNonWhitespace(strValue, iIndexOfElement2 + 1);
                            if (iIndexOfNonWhitespace < strValue.length() && strValue.charAt(iIndexOfNonWhitespace) == '\"') {
                                int i3 = iIndexOfNonWhitespace + 1;
                                String str3 = strValue;
                                int iIndexOf$default = StringsKt.indexOf$default((CharSequence) str3, '\"', i3, false, 4, (Object) null);
                                str = str3;
                                String strSubstring2 = str.substring(i3, iIndexOf$default);
                                Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                                iIndexOfElement = iIndexOf$default + 1;
                                string2 = strSubstring2;
                            } else {
                                str = strValue;
                                iIndexOfElement = indexOfElement(str, ",;", iIndexOfNonWhitespace);
                                String strSubstring3 = str.substring(iIndexOfNonWhitespace, iIndexOfElement);
                                Intrinsics.checkNotNullExpressionValue(strSubstring3, "substring(...)");
                                string2 = StringsKt.trim(strSubstring3).toString();
                            }
                        }
                        if (StringsKt.equals("no-cache", string, z)) {
                            z2 = z;
                            z4 = z2;
                        } else if (StringsKt.equals("no-store", string, z)) {
                            z2 = z;
                            z5 = z2;
                        } else {
                            if (StringsKt.equals("max-age", string, z)) {
                                nonNegativeInt = _UtilCommonKt.toNonNegativeInt(string2, -1);
                            } else if (StringsKt.equals("s-maxage", string, z)) {
                                nonNegativeInt2 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                            } else if (StringsKt.equals("private", string, z)) {
                                z2 = z;
                                z6 = z2;
                            } else if (StringsKt.equals("public", string, z)) {
                                z2 = z;
                                z7 = z2;
                            } else if (StringsKt.equals("must-revalidate", string, z)) {
                                z2 = z;
                                z8 = z2;
                            } else if (StringsKt.equals("max-stale", string, z)) {
                                nonNegativeInt3 = _UtilCommonKt.toNonNegativeInt(string2, Integer.MAX_VALUE);
                            } else if (StringsKt.equals("min-fresh", string, z)) {
                                nonNegativeInt4 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                            } else if (StringsKt.equals("only-if-cached", string, z)) {
                                z2 = z;
                                z9 = z2;
                            } else if (StringsKt.equals("no-transform", string, z)) {
                                z2 = z;
                                z10 = z2;
                            } else if (StringsKt.equals("immutable", string, z)) {
                                z2 = z;
                                z11 = z2;
                            }
                            z2 = z;
                        }
                        strValue = str;
                        size = i;
                    } else {
                        i = size;
                    }
                    str = strValue;
                    iIndexOfElement = iIndexOfElement2 + 1;
                    string2 = null;
                    if (StringsKt.equals("no-cache", string, z)) {
                        z2 = z;
                        z4 = z2;
                    } else if (StringsKt.equals("no-store", string, z)) {
                        z2 = z;
                        z5 = z2;
                    } else {
                        if (StringsKt.equals("max-age", string, z)) {
                            nonNegativeInt = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("s-maxage", string, z)) {
                            nonNegativeInt2 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("private", string, z)) {
                            z2 = z;
                            z6 = z2;
                        } else if (StringsKt.equals("public", string, z)) {
                            z2 = z;
                            z7 = z2;
                        } else if (StringsKt.equals("must-revalidate", string, z)) {
                            z2 = z;
                            z8 = z2;
                        } else if (StringsKt.equals("max-stale", string, z)) {
                            nonNegativeInt3 = _UtilCommonKt.toNonNegativeInt(string2, Integer.MAX_VALUE);
                        } else if (StringsKt.equals("min-fresh", string, z)) {
                            nonNegativeInt4 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("only-if-cached", string, z)) {
                            z2 = z;
                            z9 = z2;
                        } else if (StringsKt.equals("no-transform", string, z)) {
                            z2 = z;
                            z10 = z2;
                        } else if (StringsKt.equals("immutable", string, z)) {
                            z2 = z;
                            z11 = z2;
                        }
                        z2 = z;
                    }
                    strValue = str;
                    size = i;
                }
                i2++;
                headers2 = headers;
                z2 = z2;
                size = size;
            }
            z3 = false;
            iIndexOfElement = 0;
            while (iIndexOfElement < strValue.length()) {
                iIndexOfElement2 = indexOfElement(strValue, "=,;", iIndexOfElement);
                String strSubstring4 = strValue.substring(iIndexOfElement, iIndexOfElement2);
                z = z2;
                Intrinsics.checkNotNullExpressionValue(strSubstring4, "substring(...)");
                string = StringsKt.trim(strSubstring4).toString();
                if (iIndexOfElement2 != strValue.length()) {
                    i = size;
                    if (strValue.charAt(iIndexOfElement2) == ',') {
                    }
                    if (StringsKt.equals("no-cache", string, z)) {
                        z2 = z;
                        z4 = z2;
                    } else if (StringsKt.equals("no-store", string, z)) {
                        z2 = z;
                        z5 = z2;
                    } else {
                        if (StringsKt.equals("max-age", string, z)) {
                            nonNegativeInt = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("s-maxage", string, z)) {
                            nonNegativeInt2 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("private", string, z)) {
                            z2 = z;
                            z6 = z2;
                        } else if (StringsKt.equals("public", string, z)) {
                            z2 = z;
                            z7 = z2;
                        } else if (StringsKt.equals("must-revalidate", string, z)) {
                            z2 = z;
                            z8 = z2;
                        } else if (StringsKt.equals("max-stale", string, z)) {
                            nonNegativeInt3 = _UtilCommonKt.toNonNegativeInt(string2, Integer.MAX_VALUE);
                        } else if (StringsKt.equals("min-fresh", string, z)) {
                            nonNegativeInt4 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                        } else if (StringsKt.equals("only-if-cached", string, z)) {
                            z2 = z;
                            z9 = z2;
                        } else if (StringsKt.equals("no-transform", string, z)) {
                            z2 = z;
                            z10 = z2;
                        } else if (StringsKt.equals("immutable", string, z)) {
                            z2 = z;
                            z11 = z2;
                        }
                        z2 = z;
                    }
                    strValue = str;
                    size = i;
                } else {
                    i = size;
                }
                str = strValue;
                iIndexOfElement = iIndexOfElement2 + 1;
                string2 = null;
                if (StringsKt.equals("no-cache", string, z)) {
                    z2 = z;
                    z4 = z2;
                } else if (StringsKt.equals("no-store", string, z)) {
                    z2 = z;
                    z5 = z2;
                } else {
                    if (StringsKt.equals("max-age", string, z)) {
                        nonNegativeInt = _UtilCommonKt.toNonNegativeInt(string2, -1);
                    } else if (StringsKt.equals("s-maxage", string, z)) {
                        nonNegativeInt2 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                    } else if (StringsKt.equals("private", string, z)) {
                        z2 = z;
                        z6 = z2;
                    } else if (StringsKt.equals("public", string, z)) {
                        z2 = z;
                        z7 = z2;
                    } else if (StringsKt.equals("must-revalidate", string, z)) {
                        z2 = z;
                        z8 = z2;
                    } else if (StringsKt.equals("max-stale", string, z)) {
                        nonNegativeInt3 = _UtilCommonKt.toNonNegativeInt(string2, Integer.MAX_VALUE);
                    } else if (StringsKt.equals("min-fresh", string, z)) {
                        nonNegativeInt4 = _UtilCommonKt.toNonNegativeInt(string2, -1);
                    } else if (StringsKt.equals("only-if-cached", string, z)) {
                        z2 = z;
                        z9 = z2;
                    } else if (StringsKt.equals("no-transform", string, z)) {
                        z2 = z;
                        z10 = z2;
                    } else if (StringsKt.equals("immutable", string, z)) {
                        z2 = z;
                        z11 = z2;
                    }
                    z2 = z;
                }
                strValue = str;
                size = i;
            }
            i2++;
            headers2 = headers;
            z2 = z2;
            size = size;
        }
        return new CacheControl(z4, z5, nonNegativeInt, nonNegativeInt2, z6, z7, z8, nonNegativeInt3, nonNegativeInt4, z9, z10, z11, !z3 ? null : str2);
    }

    static /* synthetic */ int indexOfElement$default(String str, String str2, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return indexOfElement(str, str2, i);
    }

    private static final int indexOfElement(String str, String str2, int i) {
        int length = str.length();
        while (i < length) {
            if (StringsKt.contains$default((CharSequence) str2, str.charAt(i), false, 2, (Object) null)) {
                return i;
            }
            i++;
        }
        return str.length();
    }
}
