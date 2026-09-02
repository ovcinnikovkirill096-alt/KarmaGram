package androidx.collection;

public abstract class FloatSetKt {
    private static final MutableFloatSet EmptyFloatSet = new MutableFloatSet(0);
    private static final float[] EmptyFloatArray = new float[0];

    public static final float[] getEmptyFloatArray() {
        return EmptyFloatArray;
    }
}
