package j$.util.stream;

import j$.util.Spliterator;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.p1, reason: case insensitive filesystem */
public final class C0273p1 extends Y2 implements H0, A0 {
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

    @Override // j$.util.stream.A0, j$.util.stream.B0
    public final H0 build() {
        return this;
    }

    @Override // j$.util.stream.B0
    public final J0 build() {
        return this;
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.R(this, j, j2);
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

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
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
        AbstractC0322z1.L(this, (Long[]) objArr, i);
    }

    @Override // j$.util.stream.AbstractC0200a3, j$.util.stream.I0
    public final void c(int i, Object obj) {
        super.c(i, (long[]) obj);
    }

    @Override // j$.util.stream.AbstractC0200a3, j$.util.stream.I0
    public final void d(Object obj) {
        super.d((LongConsumer) obj);
    }

    @Override // j$.util.stream.Y2, j$.util.stream.AbstractC0200a3, java.lang.Iterable
    public final Spliterator spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.Y2, j$.util.stream.AbstractC0200a3, java.lang.Iterable
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
        return (long[]) super.b();
    }
}
