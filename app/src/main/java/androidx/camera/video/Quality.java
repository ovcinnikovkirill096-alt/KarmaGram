package androidx.camera.video;

import android.util.Size;
import j$.util.DesugarCollections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Quality {
    public static final Quality FHD;
    public static final Quality HD;
    public static final Quality HIGHEST;
    public static final Quality LOWEST;
    static final Quality NONE;
    private static final Set QUALITIES;
    private static final List QUALITIES_ORDER_BY_SIZE;
    public static final Quality SD;
    public static final Quality UHD;

    private Quality() {
    }

    static {
        ConstantQuality constantQualityOf = ConstantQuality.of(4, 2002, "SD", DesugarCollections.unmodifiableList(Arrays.asList(new Size(720, 480), new Size(640, 480))));
        SD = constantQualityOf;
        ConstantQuality constantQualityOf2 = ConstantQuality.of(5, 2003, "HD", Collections.singletonList(new Size(1280, 720)));
        HD = constantQualityOf2;
        ConstantQuality constantQualityOf3 = ConstantQuality.of(6, 2004, "FHD", Collections.singletonList(new Size(1920, 1080)));
        FHD = constantQualityOf3;
        ConstantQuality constantQualityOf4 = ConstantQuality.of(8, 2005, "UHD", Collections.singletonList(new Size(3840, 2160)));
        UHD = constantQualityOf4;
        List list = Collections.EMPTY_LIST;
        ConstantQuality constantQualityOf5 = ConstantQuality.of(0, 2000, "LOWEST", list);
        LOWEST = constantQualityOf5;
        ConstantQuality constantQualityOf6 = ConstantQuality.of(1, 2001, "HIGHEST", list);
        HIGHEST = constantQualityOf6;
        NONE = ConstantQuality.of(-1, -1, "NONE", list);
        QUALITIES = new HashSet(Arrays.asList(constantQualityOf5, constantQualityOf6, constantQualityOf, constantQualityOf2, constantQualityOf3, constantQualityOf4));
        QUALITIES_ORDER_BY_SIZE = Arrays.asList(constantQualityOf4, constantQualityOf3, constantQualityOf2, constantQualityOf);
    }

    static boolean containsQuality(Quality quality) {
        return QUALITIES.contains(quality);
    }

    public static List getSortedQualities() {
        return new ArrayList(QUALITIES_ORDER_BY_SIZE);
    }

    public static abstract class ConstantQuality extends Quality {
        abstract int getHighSpeedValue();

        public abstract String getName();

        public abstract List getTypicalSizes();

        abstract int getValue();

        public ConstantQuality() {
            super();
        }

        static ConstantQuality of(int i, int i2, String str, List list) {
            return new AutoValue_Quality_ConstantQuality(i, i2, str, list);
        }

        public int getQualityValue(int i) {
            if (i == 1) {
                return getValue();
            }
            if (i == 2) {
                return getHighSpeedValue();
            }
            throw new AssertionError("Unknown quality source: " + i);
        }
    }
}
