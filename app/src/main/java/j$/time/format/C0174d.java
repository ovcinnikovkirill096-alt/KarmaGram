package j$.time.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: renamed from: j$.time.format.d, reason: case insensitive filesystem */
public final class C0174d implements InterfaceC0175e {
    public final InterfaceC0175e[] a;
    public final boolean b;

    /* JADX WARN: Illegal instructions before constructor call */
    public C0174d(List list, boolean z) {
        ArrayList arrayList = (ArrayList) list;
        this((InterfaceC0175e[]) arrayList.toArray(new InterfaceC0175e[arrayList.size()]), z);
    }

    public C0174d(InterfaceC0175e[] interfaceC0175eArr, boolean z) {
        this.a = interfaceC0175eArr;
        this.b = z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
    
        if (r2 != false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
    
        r8.c--;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002c, code lost:
    
        if (r2 != false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x002f, code lost:
    
        return true;
     */
    /* JADX WARN: Undo finally extract visitor
    java.lang.NullPointerException: Cannot invoke "Object.hashCode()" because "this.second" is null
    	at jadx.core.utils.Pair.hashCode(Pair.java:35)
    	at java.base/java.util.HashMap.hash(HashMap.java:338)
    	at java.base/java.util.HashMap.getNode(HashMap.java:576)
    	at java.base/java.util.HashMap.containsKey(HashMap.java:602)
    	at jadx.core.dex.visitors.finaly.traverser.state.TraverserGlobalCommonState.hasBlocksBeenCached(TraverserGlobalCommonState.java:35)
    	at jadx.core.dex.visitors.finaly.traverser.handlers.MergePathActivePathTraverserHandler.handle(MergePathActivePathTraverserHandler.java:174)
    	at jadx.core.dex.visitors.finaly.traverser.handlers.AbstractActivePathTraverserHandler.process(AbstractActivePathTraverserHandler.java:19)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.processHandlerImplementations(TraverserController.java:43)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.advance(TraverserController.java:156)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.process(TraverserController.java:79)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.findCommonInsns(MarkFinallyVisitor.java:404)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.extractFinally(MarkFinallyVisitor.java:284)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.processTryBlock(MarkFinallyVisitor.java:202)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.visit(MarkFinallyVisitor.java:135)
     */
    @Override // j$.time.format.InterfaceC0175e
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean i(y yVar, StringBuilder sb) {
        int length = sb.length();
        boolean z = this.b;
        if (z) {
            yVar.c++;
        }
        try {
            for (InterfaceC0175e interfaceC0175e : this.a) {
                if (!interfaceC0175e.i(yVar, sb)) {
                    sb.setLength(length);
                }
            }
        } catch (Throwable th) {
            if (z) {
                yVar.c--;
            }
            throw th;
        }
    }

    @Override // j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        boolean z = this.b;
        InterfaceC0175e[] interfaceC0175eArr = this.a;
        int i2 = 0;
        if (z) {
            ArrayList arrayList = vVar.d;
            D dC = vVar.c();
            dC.getClass();
            D d = new D();
            ((HashMap) d.a).putAll(dC.a);
            d.b = dC.b;
            d.c = dC.c;
            d.d = dC.d;
            arrayList.add(d);
            int length = interfaceC0175eArr.length;
            int iJ = i;
            while (i2 < length) {
                iJ = interfaceC0175eArr[i2].j(vVar, charSequence, iJ);
                if (iJ < 0) {
                    ArrayList arrayList2 = vVar.d;
                    arrayList2.remove(arrayList2.size() - 1);
                    return i;
                }
                i2++;
            }
            ArrayList arrayList3 = vVar.d;
            arrayList3.remove(arrayList3.size() - 2);
            return iJ;
        }
        int length2 = interfaceC0175eArr.length;
        while (i2 < length2) {
            i = interfaceC0175eArr[i2].j(vVar, charSequence, i);
            if (i < 0) {
                return i;
            }
            i2++;
        }
        return i;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        InterfaceC0175e[] interfaceC0175eArr = this.a;
        if (interfaceC0175eArr != null) {
            boolean z = this.b;
            sb.append(z ? "[" : "(");
            for (InterfaceC0175e interfaceC0175e : interfaceC0175eArr) {
                sb.append(interfaceC0175e);
            }
            sb.append(z ? "]" : ")");
        }
        return sb.toString();
    }
}
