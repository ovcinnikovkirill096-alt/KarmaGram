package androidx.graphics.shapes;

public interface Measurer {
    float findCubicCutPoint(Cubic cubic, float f);

    float measureCubic(Cubic cubic);
}
