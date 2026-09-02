package j$.util.stream;

import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;

public final class T1 implements U1, InterfaceC0274p2 {
    public boolean a;
    public long b;
    public final /* synthetic */ LongBinaryOperator c;

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        u((Long) obj);
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
    }

    public T1(LongBinaryOperator longBinaryOperator) {
        this.c = longBinaryOperator;
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        T1 t1 = (T1) u1;
        if (t1.a) {
            return;
        }
        accept(t1.b);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a = true;
        this.b = 0L;
    }

    @Override // j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        if (this.a) {
            this.a = false;
            this.b = j;
        } else {
            this.b = this.c.applyAsLong(this.b, j);
        }
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return this.a ? j$.util.C.c : new j$.util.C(this.b);
    }
}
