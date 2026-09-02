package org.mvel2.optimizers;

import java.lang.reflect.Method;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.AbstractParser;
import org.mvel2.util.ParseTools;

public class AbstractOptimizer extends AbstractParser {
    protected static final int BEAN = 0;
    protected static final int COL = 2;
    protected static final int METH = 1;
    protected static final int WITH = 3;
    protected boolean collection;
    protected Class currType;
    protected boolean nullSafe;
    protected boolean staticAccess;
    protected int tkStart;

    protected AbstractOptimizer() {
        this.collection = false;
        this.nullSafe = false;
        this.currType = null;
        this.staticAccess = false;
    }

    protected AbstractOptimizer(ParserContext parserContext) {
        super(parserContext);
        this.collection = false;
        this.nullSafe = false;
        this.currType = null;
        this.staticAccess = false;
    }

    /* JADX WARN: Code duplicated, block: B:144:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:76:0x00f8 A[Catch: Exception -> 0x0141, TryCatch #3 {Exception -> 0x0141, blocks: (B:3:0x0003, B:4:0x0009, B:6:0x000d, B:17:0x002a, B:20:0x0032, B:27:0x0041, B:28:0x0044, B:29:0x0047, B:31:0x004b, B:33:0x0051, B:35:0x0057, B:37:0x005c, B:39:0x0060, B:41:0x006d, B:42:0x0079, B:44:0x007d, B:46:0x0085, B:49:0x0092, B:51:0x0097, B:53:0x009c, B:54:0x00ba, B:56:0x00bf, B:58:0x00c6, B:61:0x00d3, B:40:0x0065, B:64:0x00db, B:65:0x00de, B:68:0x00e4, B:70:0x00ea, B:81:0x0107, B:72:0x00ee, B:73:0x00f1, B:74:0x00f4, B:76:0x00f8, B:78:0x00fe, B:80:0x0104, B:82:0x010a, B:83:0x0111, B:85:0x0117, B:87:0x011d, B:92:0x0128, B:94:0x012e, B:96:0x0134), top: B:109:0x0003, inners: #0, #1, #2 }] */
    protected Object tryStaticAccess() {
        int i;
        char[] cArr;
        int i2 = this.cursor;
        try {
            int i3 = this.end;
            int i4 = i3 - 1;
            boolean z = false;
            while (i4 > this.start) {
                char c = this.expr[i4];
                if (c != '\"') {
                    if (c != '\'') {
                        int i5 = 1;
                        if (c == ')') {
                            int i6 = i4 - 1;
                            int i7 = 1;
                            while (i6 > this.start && i7 != 0) {
                                char c2 = this.expr[i6];
                                if (c2 != '\"') {
                                    switch (c2) {
                                        case '\'':
                                            while (i6 > this.start) {
                                                cArr = this.expr;
                                                if (cArr[i6] == c2 || cArr[i6 - 1] == '\\') {
                                                }
                                                i6--;
                                                break;
                                            }
                                            break;
                                        case '(':
                                            i7--;
                                            break;
                                        case ')':
                                            i7++;
                                            break;
                                    }
                                } else {
                                    while (i6 > this.start) {
                                        cArr = this.expr;
                                        if (cArr[i6] == c2) {
                                            break;
                                        }
                                        break;
                                    }
                                }
                                i6--;
                            }
                            int i8 = i6;
                            i4 = i6 + 1;
                            i3 = i8;
                            z = true;
                        } else if (c == '.') {
                            if (!z) {
                                ParserContext parserContext = this.pCtx;
                                ClassLoader classLoader = parserContext != null ? parserContext.getClassLoader() : Thread.currentThread().getContextClassLoader();
                                char[] cArr2 = this.expr;
                                int i9 = this.start;
                                this.cursor = i3;
                                String str = new String(cArr2, i9, i3 - i9);
                                try {
                                    if (MVEL.COMPILER_OPT_SUPPORT_JAVA_STYLE_CLASS_LITERALS && str.endsWith(".class")) {
                                        str = str.substring(0, str.length() - 6);
                                    }
                                    return Class.forName(str, true, classLoader);
                                } catch (ClassNotFoundException e) {
                                    try {
                                        return ParseTools.findInnerClass(str, classLoader, e);
                                    } catch (ClassNotFoundException unused) {
                                        Class clsForNameWithInner = ParseTools.forNameWithInner(new String(this.expr, this.start, i4 - this.start), classLoader);
                                        String str2 = new String(this.expr, i4 + 1, (this.end - i4) - 1);
                                        try {
                                            return clsForNameWithInner.getField(str2);
                                        } catch (NoSuchFieldException unused2) {
                                            for (Method method : clsForNameWithInner.getMethods()) {
                                                if (str2.equals(method.getName())) {
                                                    return method;
                                                }
                                            }
                                            return null;
                                        }
                                    }
                                }
                            }
                            i3 = i4;
                            z = false;
                        } else if (c == '}') {
                            while (true) {
                                i4--;
                                if (i4 <= this.start || i5 == 0) {
                                    break;
                                }
                                char c3 = this.expr[i4];
                                if (c3 == '\"' || c3 == '\'') {
                                    while (i4 > this.start) {
                                        char[] cArr3 = this.expr;
                                        if (cArr3[i4] == c3 || cArr3[i4 - 1] == '\\') {
                                            break;
                                        }
                                        i4--;
                                    }
                                } else if (c3 == '{') {
                                    i5--;
                                } else if (c3 == '}') {
                                    i5++;
                                }
                            }
                        }
                    } else {
                        while (true) {
                            i = i4 - 1;
                            if (i > this.start) {
                                char[] cArr4 = this.expr;
                                if (cArr4[i] != '\'' || cArr4[i4 - 2] == '\\') {
                                    i4 = i;
                                }
                            }
                        }
                    }
                    i4--;
                } else {
                    while (true) {
                        i = i4 - 1;
                        if (i > this.start) {
                            char[] cArr5 = this.expr;
                            if (cArr5[i] != '\"' || cArr5[i4 - 2] == '\\') {
                                i4 = i;
                            }
                        }
                    }
                }
                i4 = i;
                i4--;
            }
        } catch (Exception unused3) {
            this.cursor = i2;
        }
        return null;
    }

    protected int nextSubToken() {
        int i;
        skipWhitespace();
        this.nullSafe = false;
        char[] cArr = this.expr;
        int i2 = this.cursor;
        this.tkStart = i2;
        char c = cArr[i2];
        if (c != '.') {
            if (c != '?') {
                if (c == '[') {
                    return 2;
                }
                if (c == '{' && cArr[i2 - 1] == '.') {
                    return 3;
                }
            } else if (this.start == i2) {
                this.tkStart = i2 + 1;
                this.cursor = i2 + 1;
                this.nullSafe = true;
            }
        } else if (this.start + 1 != this.end) {
            int i3 = i2 + 1;
            this.tkStart = i3;
            this.cursor = i3;
            char c2 = cArr[i3];
            if (c2 == '?') {
                skipWhitespace();
                int i4 = this.tkStart + 1;
                this.tkStart = i4;
                this.cursor = i4;
                if (i4 == this.end) {
                    throw new CompileException("unexpected end of statement", this.expr, this.start);
                }
                this.nullSafe = true;
                this.fields = -1;
            } else {
                if (c2 == '{') {
                    return 3;
                }
                if (ParseTools.isWhitespace(c2)) {
                    skipWhitespace();
                    this.tkStart = this.cursor;
                }
            }
        } else {
            throw new CompileException("unexpected end of statement", this.expr, this.start);
        }
        do {
            i = this.cursor + 1;
            this.cursor = i;
            if (i >= this.end) {
                break;
            }
        } while (ParseTools.isIdentifierPart(this.expr[i]));
        skipWhitespace();
        int i5 = this.cursor;
        if (i5 >= this.end) {
            return 0;
        }
        char c3 = this.expr[i5];
        if (c3 != '(') {
            return c3 != '[' ? 0 : 2;
        }
        return 1;
    }

    protected String capture() {
        char[] cArr = this.expr;
        int iTrimRight = trimRight(this.tkStart);
        this.tkStart = iTrimRight;
        return new String(cArr, iTrimRight, trimLeft(this.cursor) - this.tkStart);
    }

    protected void whiteSpaceSkip() {
        if (this.cursor < this.length) {
            while (ParseTools.isWhitespace(this.expr[this.cursor])) {
                int i = this.cursor + 1;
                this.cursor = i;
                if (i == this.length) {
                    return;
                }
            }
        }
    }

    protected boolean scanTo(char c) {
        while (true) {
            int i = this.cursor;
            int i2 = this.end;
            if (i >= i2) {
                return true;
            }
            char[] cArr = this.expr;
            char c2 = cArr[i];
            if (c2 == '\"' || c2 == '\'') {
                this.cursor = ParseTools.captureStringLiteral(c2, cArr, i, i2);
            }
            char[] cArr2 = this.expr;
            int i3 = this.cursor;
            if (cArr2[i3] == c) {
                return false;
            }
            this.cursor = i3 + 1;
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0027  */
    /* JADX WARN: Code duplicated, block: B:17:0x002a  */
    /* JADX WARN: Code duplicated, block: B:19:0x002e  */
    protected int findLastUnion() {
        int i = 0;
        int i2 = -1;
        for (int i3 = (this.start + this.length) - 1; i3 != this.start; i3--) {
            char c = this.expr[i3];
            if (c != '.') {
                if (c == '[') {
                    i--;
                    if (i == 0) {
                        this.collection = true;
                        i2 = i3;
                    }
                } else if (c == ']') {
                    i++;
                } else if (c == '{') {
                    i--;
                    if (i == 0) {
                        this.collection = true;
                        i2 = i3;
                    }
                } else if (c == '}') {
                    i++;
                }
            } else if (i == 0) {
                i2 = i3;
            }
            if (i2 != -1) {
                return i2;
            }
        }
        return i2;
    }
}
