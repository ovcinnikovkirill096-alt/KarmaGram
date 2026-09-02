package j$.util.stream;

import j$.util.Objects;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* JADX INFO: renamed from: j$.util.stream.k2, reason: case insensitive filesystem */
public abstract class AbstractC0249k2 implements InterfaceC0269o2 {
    public final InterfaceC0279q2 a;

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
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
    }

    public AbstractC0249k2(InterfaceC0279q2 interfaceC0279q2) {
        this.a = (InterfaceC0279q2) Objects.requireNonNull(interfaceC0279q2);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public void h(long j) {
        this.a.h(j);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public void end() {
        this.a.end();
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public boolean m() {
        return this.a.m();
    }
}
