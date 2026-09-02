package j$.util.stream;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/* JADX INFO: renamed from: j$.util.stream.s0, reason: case insensitive filesystem */
public final class C0286s0 extends AbstractC0301v0 implements InterfaceC0269o2 {
    public final /* synthetic */ EnumC0306w0 c;
    public final /* synthetic */ IntPredicate d;

    @Override // java.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0286s0(EnumC0306w0 enumC0306w0, IntPredicate intPredicate) {
        super(enumC0306w0);
        this.c = enumC0306w0;
        this.d = intPredicate;
    }

    @Override // j$.util.stream.AbstractC0301v0, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        if (this.a) {
            return;
        }
        boolean zTest = this.d.test(i);
        EnumC0306w0 enumC0306w0 = this.c;
        if (zTest == enumC0306w0.a) {
            this.a = true;
            this.b = enumC0306w0.b;
        }
    }
}
