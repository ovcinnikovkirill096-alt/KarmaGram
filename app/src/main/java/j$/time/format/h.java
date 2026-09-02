package j$.time.format;

public final class h implements InterfaceC0175e {
    public final /* synthetic */ int a;
    public final Object b;

    public /* synthetic */ h(int i, Object obj) {
        this.a = i;
        this.b = obj;
    }

    @Override // j$.time.format.InterfaceC0175e
    public final boolean i(y yVar, StringBuilder sb) {
        switch (this.a) {
            case 0:
                Long lA = yVar.a(j$.time.temporal.a.OFFSET_SECONDS);
                if (lA == null) {
                    return false;
                }
                sb.append("GMT");
                int iO = j$.com.android.tools.r8.a.O(lA.longValue());
                if (iO != 0) {
                    int iAbs = Math.abs((iO / 3600) % 100);
                    int iAbs2 = Math.abs((iO / 60) % 60);
                    int iAbs3 = Math.abs(iO % 60);
                    sb.append(iO < 0 ? "-" : "+");
                    if (((TextStyle) this.b) == TextStyle.FULL) {
                        a(sb, iAbs);
                        sb.append(':');
                        a(sb, iAbs2);
                        if (iAbs3 != 0) {
                            sb.append(':');
                            a(sb, iAbs3);
                        }
                    } else {
                        if (iAbs >= 10) {
                            sb.append((char) ((iAbs / 10) + 48));
                        }
                        sb.append((char) ((iAbs % 10) + 48));
                        if (iAbs2 != 0 || iAbs3 != 0) {
                            sb.append(':');
                            a(sb, iAbs2);
                            if (iAbs3 != 0) {
                                sb.append(':');
                                a(sb, iAbs3);
                            }
                        }
                    }
                }
                return true;
            default:
                sb.append((String) this.b);
                return true;
        }
    }

    @Override // j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        int i2;
        int iB;
        int i3;
        int i4;
        int i5;
        int i6;
        switch (this.a) {
            case 0:
                int length = charSequence.length();
                if (vVar.g(charSequence, i, "GMT", 0, 3)) {
                    int i7 = i + 3;
                    if (i7 == length) {
                        return vVar.f(j$.time.temporal.a.OFFSET_SECONDS, 0L, i, i7);
                    }
                    char cCharAt = charSequence.charAt(i7);
                    if (cCharAt == '+') {
                        i2 = 1;
                    } else {
                        if (cCharAt != '-') {
                            return vVar.f(j$.time.temporal.a.OFFSET_SECONDS, 0L, i, i7);
                        }
                        i2 = -1;
                    }
                    int i8 = i + 4;
                    int i9 = 0;
                    if (((TextStyle) this.b) == TextStyle.FULL) {
                        int i10 = i + 5;
                        int iB2 = b(charSequence, i8);
                        int i11 = i + 6;
                        int iB3 = b(charSequence, i10);
                        if (iB2 >= 0 && iB3 >= 0) {
                            int i12 = i + 7;
                            if (charSequence.charAt(i11) == ':') {
                                iB = (iB2 * 10) + iB3;
                                int iB4 = b(charSequence, i12);
                                i6 = i + 9;
                                int iB5 = b(charSequence, i + 8);
                                if (iB4 >= 0 && iB5 >= 0) {
                                    i5 = (iB4 * 10) + iB5;
                                    int i13 = i + 11;
                                    if (i13 < length && charSequence.charAt(i6) == ':') {
                                        int iB6 = b(charSequence, i + 10);
                                        int iB7 = b(charSequence, i13);
                                        if (iB6 >= 0 && iB7 >= 0) {
                                            i9 = (iB6 * 10) + iB7;
                                            i6 = i + 12;
                                        }
                                    }
                                    i3 = i9;
                                    i4 = i6;
                                }
                            }
                        }
                    } else {
                        int i14 = i + 5;
                        iB = b(charSequence, i8);
                        if (iB >= 0) {
                            if (i14 < length) {
                                int iB8 = b(charSequence, i14);
                                if (iB8 >= 0) {
                                    iB = (iB * 10) + iB8;
                                    i14 = i + 6;
                                }
                                int i15 = i14 + 2;
                                if (i15 < length && charSequence.charAt(i14) == ':' && i15 < length && charSequence.charAt(i14) == ':') {
                                    int iB9 = b(charSequence, i14 + 1);
                                    int iB10 = b(charSequence, i15);
                                    if (iB9 >= 0 && iB10 >= 0) {
                                        i5 = (iB9 * 10) + iB10;
                                        int i16 = i14 + 3;
                                        int i17 = i14 + 5;
                                        if (i17 < length && charSequence.charAt(i16) == ':') {
                                            int iB11 = b(charSequence, i14 + 4);
                                            int iB12 = b(charSequence, i17);
                                            if (iB11 >= 0 && iB12 >= 0) {
                                                i9 = (iB11 * 10) + iB12;
                                                i6 = i14 + 6;
                                                i3 = i9;
                                                i4 = i6;
                                            }
                                        }
                                        i4 = i16;
                                        i3 = 0;
                                    }
                                    return vVar.f(j$.time.temporal.a.OFFSET_SECONDS, ((((long) i9) * 60) + (((long) iB) * 3600) + ((long) i3)) * ((long) i2), i, i4);
                                }
                            }
                            i3 = 0;
                            i4 = i14;
                            return vVar.f(j$.time.temporal.a.OFFSET_SECONDS, ((((long) i9) * 60) + (((long) iB) * 3600) + ((long) i3)) * ((long) i2), i, i4);
                        }
                    }
                    i9 = i5;
                    return vVar.f(j$.time.temporal.a.OFFSET_SECONDS, ((((long) i9) * 60) + (((long) iB) * 3600) + ((long) i3)) * ((long) i2), i, i4);
                }
                return ~i;
            default:
                String str = (String) this.b;
                if (i > charSequence.length() || i < 0) {
                    throw new IndexOutOfBoundsException();
                }
                return !vVar.g(charSequence, i, str, 0, str.length()) ? ~i : str.length() + i;
        }
    }

    public final String toString() {
        switch (this.a) {
            case 0:
                return "LocalizedOffset(" + ((TextStyle) this.b) + ")";
            default:
                return "'" + ((String) this.b).replace("'", "''") + "'";
        }
    }

    public static void a(StringBuilder sb, int i) {
        sb.append((char) ((i / 10) + 48));
        sb.append((char) ((i % 10) + 48));
    }

    public static int b(CharSequence charSequence, int i) {
        char cCharAt = charSequence.charAt(i);
        if (cCharAt < '0' || cCharAt > '9') {
            return -1;
        }
        return cCharAt - '0';
    }
}
