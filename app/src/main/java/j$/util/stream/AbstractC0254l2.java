package j$.util.stream;

import j$.util.Objects;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.l2, reason: case insensitive filesystem */
public abstract class AbstractC0254l2 implements InterfaceC0274p2 {
    public final InterfaceC0279q2 a;

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
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        u((Long) obj);
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
    }

    public AbstractC0254l2(InterfaceC0279q2 interfaceC0279q2) {
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
