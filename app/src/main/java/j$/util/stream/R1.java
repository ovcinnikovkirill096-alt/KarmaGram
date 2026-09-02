package j$.util.stream;

import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

public final class R1 extends V1 implements U1, InterfaceC0269o2 {
    public final /* synthetic */ Supplier b;
    public final /* synthetic */ ObjIntConsumer c;
    public final /* synthetic */ r d;

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final /* synthetic */ void accept(long j) {
        AbstractC0322z1.H();
        throw null;
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        this.a = this.d.apply(this.a, ((R1) u1).a);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        this.c.accept(this.a, i);
    }

    public R1(Supplier supplier, ObjIntConsumer objIntConsumer, r rVar) {
        this.b = supplier;
        this.c = objIntConsumer;
        this.d = rVar;
    }
}
