package j$.time.chrono;

/* JADX INFO: renamed from: j$.time.chrono.b, reason: case insensitive filesystem */
public interface InterfaceC0163b extends j$.time.temporal.m, j$.time.temporal.o, Comparable {
    long E();

    ChronoLocalDateTime F(j$.time.i iVar);

    l G();

    InterfaceC0163b J(j$.time.temporal.q qVar);

    int M();

    /* JADX INFO: renamed from: N */
    int compareTo(InterfaceC0163b interfaceC0163b);

    k a();

    @Override // j$.time.temporal.m
    InterfaceC0163b c(long j, j$.time.temporal.r rVar);

    @Override // j$.time.temporal.m
    InterfaceC0163b d(long j, j$.time.temporal.t tVar);

    @Override // j$.time.temporal.n
    boolean e(j$.time.temporal.r rVar);

    boolean equals(Object obj);

    int hashCode();

    boolean p();

    /* JADX INFO: renamed from: r */
    InterfaceC0163b y(long j, j$.time.temporal.t tVar);

    String toString();

    InterfaceC0163b x(j$.time.temporal.o oVar);
}
