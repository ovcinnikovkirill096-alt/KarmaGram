package j$.util.stream;

import java.util.function.Predicate;

/* JADX INFO: renamed from: j$.util.stream.r0, reason: case insensitive filesystem */
public final class C0281r0 extends AbstractC0301v0 {
    public final /* synthetic */ EnumC0306w0 c;
    public final /* synthetic */ Predicate d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0281r0(EnumC0306w0 enumC0306w0, Predicate predicate) {
        super(enumC0306w0);
        this.c = enumC0306w0;
        this.d = predicate;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        if (this.a) {
            return;
        }
        boolean zTest = this.d.test(obj);
        EnumC0306w0 enumC0306w0 = this.c;
        if (zTest == enumC0306w0.a) {
            this.a = true;
            this.b = enumC0306w0.b;
        }
    }
}
