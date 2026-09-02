package j$.util.stream;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/* JADX INFO: renamed from: j$.util.stream.o, reason: case insensitive filesystem */
public final class C0266o extends AbstractC0259m2 {
    public final /* synthetic */ int b;
    public Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0266o(AbstractC0201b abstractC0201b, InterfaceC0279q2 interfaceC0279q2, int i) {
        super(interfaceC0279q2);
        this.b = i;
        this.c = abstractC0201b;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0266o(InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.b = 0;
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public void end() {
        switch (this.b) {
            case 0:
                this.c = null;
                this.a.end();
                break;
            default:
                super.end();
                break;
        }
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public void h(long j) {
        switch (this.b) {
            case 0:
                this.c = new HashSet();
                this.a.h(-1L);
                break;
            case 1:
            default:
                super.h(j);
                break;
            case 2:
                this.a.h(-1L);
                break;
        }
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.b) {
            case 0:
                if (!((Set) this.c).contains(obj)) {
                    ((Set) this.c).add(obj);
                    this.a.accept(obj);
                }
                break;
            case 1:
                ((Consumer) ((C0290t) this.c).t).accept(obj);
                this.a.accept(obj);
                break;
            case 2:
                if (((Predicate) ((C0290t) this.c).t).test(obj)) {
                    this.a.accept(obj);
                }
                break;
            case 3:
                this.a.accept(((Function) ((C0290t) this.c).t).apply(obj));
                break;
            case 4:
                this.a.accept(((ToIntFunction) ((X) this.c).t).applyAsInt(obj));
                break;
            case 5:
                this.a.accept(((ToLongFunction) ((C0237i0) this.c).t).applyAsLong(obj));
                break;
            default:
                this.a.accept(((ToDoubleFunction) ((C0315y) this.c).t).applyAsDouble(obj));
                break;
        }
    }
}
