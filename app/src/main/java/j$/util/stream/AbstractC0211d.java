package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.d, reason: case insensitive filesystem */
public abstract class AbstractC0211d {
    public final int a;
    public int b;
    public int c;
    public long[] d;

    public abstract void clear();

    public AbstractC0211d() {
        this.a = 4;
    }

    public AbstractC0211d(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i);
        }
        this.a = Math.max(4, 32 - Integer.numberOfLeadingZeros(i - 1));
    }

    public final long count() {
        int i = this.c;
        if (i == 0) {
            return this.b;
        }
        return this.d[i] + ((long) this.b);
    }
}
