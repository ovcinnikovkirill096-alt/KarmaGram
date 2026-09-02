package j$.util.stream;

import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

/* JADX INFO: renamed from: j$.util.stream.u0, reason: case insensitive filesystem */
public final class C0296u0 extends AbstractC0301v0 implements InterfaceC0264n2 {
    @Override // java.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        v((Double) obj);
    }

    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return j$.com.android.tools.r8.a.b(this, doubleConsumer);
    }

    @Override // j$.util.stream.InterfaceC0264n2
    public final /* synthetic */ void v(Double d) {
        AbstractC0322z1.A(this, d);
    }

    @Override // j$.util.stream.AbstractC0301v0, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        if (this.a) {
            return;
        }
        DoublePredicate doublePredicate = null;
        doublePredicate.test(d);
        throw null;
    }
}
