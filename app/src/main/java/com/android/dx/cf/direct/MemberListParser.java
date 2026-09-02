package com.android.dx.cf.direct;

import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.cf.iface.StdAttributeList;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;

abstract class MemberListParser {
    private final AttributeFactory attributeFactory;
    private final DirectClassFile cf;
    private final CstType definer;
    private int endOffset;
    private ParseObserver observer;
    private final int offset;

    protected abstract int getAttributeContext();

    protected abstract String humanAccessFlags(int i);

    protected abstract String humanName();

    protected abstract Member set(int i, int i2, CstNat cstNat, AttributeList attributeList);

    public MemberListParser(DirectClassFile directClassFile, CstType cstType, int i, AttributeFactory attributeFactory) {
        if (directClassFile == null) {
            throw new NullPointerException("cf == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (attributeFactory == null) {
            throw new NullPointerException("attributeFactory == null");
        }
        this.cf = directClassFile;
        this.definer = cstType;
        this.offset = i;
        this.attributeFactory = attributeFactory;
        this.endOffset = -1;
    }

    public int getEndOffset() {
        parseIfNecessary();
        return this.endOffset;
    }

    public final void setObserver(ParseObserver parseObserver) {
        this.observer = parseObserver;
    }

    protected final void parseIfNecessary() {
        if (this.endOffset < 0) {
            parse();
        }
    }

    protected final int getCount() {
        return this.cf.getBytes().getUnsignedShort(this.offset);
    }

    protected final CstType getDefiner() {
        return this.definer;
    }

    private void parse() {
        int i;
        char c;
        int i2;
        int attributeContext = getAttributeContext();
        int count = getCount();
        int i3 = this.offset + 2;
        ByteArray bytes = this.cf.getBytes();
        ConstantPool constantPool = this.cf.getConstantPool();
        ParseObserver parseObserver = this.observer;
        if (parseObserver != null) {
            parseObserver.parsed(bytes, this.offset, 2, humanName() + "s_count: " + Hex.u2(count));
        }
        int i4 = 0;
        while (i4 < count) {
            try {
                int unsignedShort = bytes.getUnsignedShort(i3);
                int i5 = i3 + 2;
                int unsignedShort2 = bytes.getUnsignedShort(i5);
                int i6 = i3 + 4;
                int unsignedShort3 = bytes.getUnsignedShort(i6);
                CstString cstString = (CstString) constantPool.get(unsignedShort2);
                CstString cstString2 = (CstString) constantPool.get(unsignedShort3);
                ParseObserver parseObserver2 = this.observer;
                if (parseObserver2 != null) {
                    parseObserver2.startParsingMember(bytes, i3, cstString.getString(), cstString2.getString());
                    this.observer.parsed(bytes, i3, 0, "\n" + humanName() + "s[" + i4 + "]:\n");
                    this.observer.changeIndent(1);
                    ParseObserver parseObserver3 = this.observer;
                    StringBuilder sb = new StringBuilder();
                    sb.append("access_flags: ");
                    sb.append(humanAccessFlags(unsignedShort));
                    parseObserver3.parsed(bytes, i3, 2, sb.toString());
                    this.observer.parsed(bytes, i5, 2, "name: " + cstString.toHuman());
                    c = 2;
                    this.observer.parsed(bytes, i6, 2, "descriptor: " + cstString2.toHuman());
                } else {
                    c = 2;
                }
                AttributeListParser attributeListParser = new AttributeListParser(this.cf, attributeContext, i3 + 6, this.attributeFactory);
                attributeListParser.setObserver(this.observer);
                int endOffset = attributeListParser.getEndOffset();
                StdAttributeList list = attributeListParser.getList();
                list.setImmutable();
                Member member = set(i4, unsignedShort, new CstNat(cstString, cstString2), list);
                ParseObserver parseObserver4 = this.observer;
                if (parseObserver4 != null) {
                    parseObserver4.changeIndent(-1);
                    this.observer.parsed(bytes, endOffset, 0, "end " + humanName() + "s[" + i4 + "]\n");
                    i = i4;
                    try {
                        i2 = endOffset;
                        this.observer.endParsingMember(bytes, i2, cstString.getString(), cstString2.getString(), member);
                    } catch (ParseException e) {
                        e = e;
                        e.addContext("...while parsing " + humanName() + "s[" + i + "]");
                        throw e;
                    } catch (RuntimeException e2) {
                        e = e2;
                        ParseException parseException = new ParseException(e);
                        parseException.addContext("...while parsing " + humanName() + "s[" + i + "]");
                        throw parseException;
                    }
                } else {
                    i2 = endOffset;
                    i = i4;
                }
                i4 = i + 1;
                i3 = i2;
                count = count;
                constantPool = constantPool;
            } catch (ParseException e3) {
                e = e3;
                i = i4;
            } catch (RuntimeException e4) {
                e = e4;
                i = i4;
            }
        }
        this.endOffset = i3;
    }
}
