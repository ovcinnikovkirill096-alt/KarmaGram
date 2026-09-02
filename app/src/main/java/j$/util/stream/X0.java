package j$.util.stream;

import j$.util.Spliterator;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;

public final class X0 extends U2 implements D0, InterfaceC0316y0 {
    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
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
        v((Double) obj);
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.InterfaceC0316y0, j$.util.stream.B0
    public final D0 build() {
        return this;
    }

    @Override // j$.util.stream.B0
    public final J0 build() {
        return this;
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.P(this, j, j2);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void end() {
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ Object[] g(IntFunction intFunction) {
        return AbstractC0322z1.I(this, intFunction);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ int i() {
        return 0;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    @Override // j$.util.stream.InterfaceC0264n2
    public final /* synthetic */ void v(Double d) {
        AbstractC0322z1.A(this, d);
    }

    @Override // j$.util.stream.J0
    public final /* bridge */ /* synthetic */ J0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.I0, j$.util.stream.J0
    public final I0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void f(Object[] objArr, int i) {
        AbstractC0322z1.J(this, (Double[]) objArr, i);
    }

    @Override // j$.util.stream.AbstractC0200a3, j$.util.stream.I0
    public final void c(int i, Object obj) {
        super.c(i, (double[]) obj);
    }

    @Override // j$.util.stream.AbstractC0200a3, j$.util.stream.I0
    public final void d(Object obj) {
        super.d((DoubleConsumer) obj);
    }

    @Override // j$.util.stream.U2, j$.util.stream.AbstractC0200a3, java.lang.Iterable
    public final Spliterator spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.U2, j$.util.stream.AbstractC0200a3, java.lang.Iterable
    public final j$.util.c0 spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        clear();
        p(j);
    }

    @Override // j$.util.stream.AbstractC0200a3, j$.util.stream.I0
    public final Object b() {
        return (double[]) super.b();
    }
}
