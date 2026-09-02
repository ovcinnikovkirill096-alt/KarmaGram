package j$.time.format;

public final class k implements InterfaceC0175e {
    public final InterfaceC0175e a;
    public final int b;
    public final char c;

    @Override // j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        boolean z = vVar.c;
        if (i > charSequence.length()) {
            throw new IndexOutOfBoundsException();
        }
        if (i == charSequence.length()) {
            return ~i;
        }
        int length = this.b + i;
        if (length > charSequence.length()) {
            if (z) {
                return ~i;
            }
            length = charSequence.length();
        }
        int i2 = i;
        while (i2 < length && vVar.a(charSequence.charAt(i2), this.c)) {
            i2++;
        }
        int iJ = this.a.j(vVar, charSequence.subSequence(0, length), i2);
        return (iJ == length || !z) ? iJ : ~(i + i2);
    }

    public k(InterfaceC0175e interfaceC0175e, int i, char c) {
        this.a = interfaceC0175e;
        this.b = i;
        this.c = c;
    }

    @Override // j$.time.format.InterfaceC0175e
    public final boolean i(y yVar, StringBuilder sb) {
        int length = sb.length();
        if (!this.a.i(yVar, sb)) {
            return false;
        }
        int length2 = sb.length() - length;
        int i = this.b;
        if (length2 <= i) {
            for (int i2 = 0; i2 < i - length2; i2++) {
                sb.insert(length, this.c);
            }
            return true;
        }
        throw new j$.time.b("Cannot print as output of " + length2 + " characters exceeds pad width of " + i);
    }

    public final String toString() {
        String str;
        char c = this.c;
        if (c == ' ') {
            str = ")";
        } else {
            str = ",'" + c + "')";
        }
        return "Pad(" + this.a + "," + this.b + str;
    }
}
