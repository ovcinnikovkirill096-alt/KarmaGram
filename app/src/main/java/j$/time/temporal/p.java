package j$.time.temporal;

public final /* synthetic */ class p implements o {
    public final /* synthetic */ int a;
    public final /* synthetic */ int b;

    public /* synthetic */ p(int i, int i2) {
        this.a = i2;
        this.b = i;
    }

    @Override // j$.time.temporal.o
    public final m n(m mVar) {
        switch (this.a) {
            case 0:
                int i = mVar.i(a.DAY_OF_WEEK);
                int i2 = this.b;
                if (i == i2) {
                    return mVar;
                }
                int i3 = i - i2;
                return mVar.d(i3 >= 0 ? 7 - i3 : -i3, b.DAYS);
            default:
                int i4 = mVar.i(a.DAY_OF_WEEK);
                int i5 = this.b;
                if (i4 == i5) {
                    return mVar;
                }
                int i6 = i5 - i4;
                return mVar.y(i6 >= 0 ? 7 - i6 : -i6, b.DAYS);
        }
    }
}
