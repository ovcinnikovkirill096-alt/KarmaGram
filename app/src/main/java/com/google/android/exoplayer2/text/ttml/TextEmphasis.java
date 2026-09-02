package com.google.android.exoplayer2.text.ttml;

import android.text.TextUtils;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.regex.Pattern;

final class TextEmphasis {
    public final int markFill;
    public final int markShape;
    public final int position;
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final ImmutableSet SINGLE_STYLE_VALUES = ImmutableSet.of((Object) "auto", (Object) "none");
    private static final ImmutableSet MARK_SHAPE_VALUES = ImmutableSet.of((Object) "dot", (Object) "sesame", (Object) "circle");
    private static final ImmutableSet MARK_FILL_VALUES = ImmutableSet.of((Object) "filled", (Object) "open");
    private static final ImmutableSet POSITION_VALUES = ImmutableSet.of((Object) "after", (Object) "before", (Object) "outside");

    private TextEmphasis(int i, int i2, int i3) {
        this.markShape = i;
        this.markFill = i2;
        this.position = i3;
    }

    public static TextEmphasis parse(String str) {
        if (str == null) {
            return null;
        }
        String lowerCase = Ascii.toLowerCase(str.trim());
        if (lowerCase.isEmpty()) {
            return null;
        }
        return parseWords(ImmutableSet.copyOf(TextUtils.split(lowerCase, WHITESPACE_PATTERN)));
    }

    /* JADX WARN: Code duplicated, block: B:19:0x004b  */
    /* JADX WARN: Code duplicated, block: B:21:0x005e  */
    /* JADX WARN: Code duplicated, block: B:27:0x006e  */
    /* JADX WARN: Code duplicated, block: B:30:0x007a  */
    /* JADX WARN: Code duplicated, block: B:38:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:44:0x00b9  */
    /* JADX WARN: Code duplicated, block: B:48:0x00cf  */
    /* JADX WARN: Code duplicated, block: B:50:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:53:0x00da  */
    /* JADX WARN: Code duplicated, block: B:56:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:58:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:59:0x00ee  */
    private static TextEmphasis parseWords(ImmutableSet immutableSet) {
        int i;
        Sets.SetView setViewIntersection;
        int i2;
        Sets.SetView setViewIntersection2;
        Sets.SetView setViewIntersection3;
        String str;
        int iHashCode;
        int i3;
        String str2;
        int iHashCode2;
        String str3;
        int iHashCode3;
        String str4 = (String) Iterables.getFirst(Sets.intersection(POSITION_VALUES, immutableSet), "outside");
        int iHashCode4 = str4.hashCode();
        int i4 = 1;
        if (iHashCode4 != -1392885889) {
            if (iHashCode4 != -1106037339) {
                if (iHashCode4 == 92734940 && str4.equals("after")) {
                    i = 2;
                }
            } else if (str4.equals("outside")) {
                i = -2;
            }
            setViewIntersection = Sets.intersection(SINGLE_STYLE_VALUES, immutableSet);
            i2 = -1;
            if (!setViewIntersection.isEmpty()) {
                str3 = (String) setViewIntersection.iterator().next();
                iHashCode3 = str3.hashCode();
                if (iHashCode3 != 3005871) {
                    str3.equals("auto");
                } else if (iHashCode3 == 3387192 && str3.equals("none")) {
                    i2 = 0;
                }
                return new TextEmphasis(i2, 0, i);
            }
            setViewIntersection2 = Sets.intersection(MARK_FILL_VALUES, immutableSet);
            setViewIntersection3 = Sets.intersection(MARK_SHAPE_VALUES, immutableSet);
            if (!setViewIntersection2.isEmpty() && setViewIntersection3.isEmpty()) {
                return new TextEmphasis(-1, 0, i);
            }
            str = (String) Iterables.getFirst(setViewIntersection2, "filled");
            iHashCode = str.hashCode();
            if (iHashCode != -1274499742) {
                str.equals("filled");
            } else {
                if (iHashCode == 3417674 && str.equals("open")) {
                    i3 = 2;
                }
                str2 = (String) Iterables.getFirst(setViewIntersection3, "circle");
                iHashCode2 = str2.hashCode();
                if (iHashCode2 != -1360216880) {
                    str2.equals("circle");
                } else if (iHashCode2 != -905816648) {
                    if (iHashCode2 == 99657 && str2.equals("dot")) {
                        i4 = 2;
                    }
                } else if (str2.equals("sesame")) {
                    i4 = 3;
                }
                return new TextEmphasis(i4, i3, i);
            }
            i3 = 1;
            str2 = (String) Iterables.getFirst(setViewIntersection3, "circle");
            iHashCode2 = str2.hashCode();
            if (iHashCode2 != -1360216880) {
                str2.equals("circle");
            } else if (iHashCode2 != -905816648) {
                if (iHashCode2 == 99657) {
                    i4 = 2;
                }
            } else if (str2.equals("sesame")) {
                i4 = 3;
            }
            return new TextEmphasis(i4, i3, i);
        }
        str4.equals("before");
        i = 1;
        setViewIntersection = Sets.intersection(SINGLE_STYLE_VALUES, immutableSet);
        i2 = -1;
        if (!setViewIntersection.isEmpty()) {
            str3 = (String) setViewIntersection.iterator().next();
            iHashCode3 = str3.hashCode();
            if (iHashCode3 != 3005871) {
                str3.equals("auto");
            } else if (iHashCode3 == 3387192) {
                i2 = 0;
            }
            return new TextEmphasis(i2, 0, i);
        }
        setViewIntersection2 = Sets.intersection(MARK_FILL_VALUES, immutableSet);
        setViewIntersection3 = Sets.intersection(MARK_SHAPE_VALUES, immutableSet);
        if (!setViewIntersection2.isEmpty()) {
        }
        str = (String) Iterables.getFirst(setViewIntersection2, "filled");
        iHashCode = str.hashCode();
        if (iHashCode != -1274499742) {
            str.equals("filled");
        } else {
            if (iHashCode == 3417674) {
                i3 = 2;
            }
            str2 = (String) Iterables.getFirst(setViewIntersection3, "circle");
            iHashCode2 = str2.hashCode();
            if (iHashCode2 != -1360216880) {
                str2.equals("circle");
            } else if (iHashCode2 != -905816648) {
                if (iHashCode2 == 99657) {
                    i4 = 2;
                }
            } else if (str2.equals("sesame")) {
                i4 = 3;
            }
            return new TextEmphasis(i4, i3, i);
        }
        i3 = 1;
        str2 = (String) Iterables.getFirst(setViewIntersection3, "circle");
        iHashCode2 = str2.hashCode();
        if (iHashCode2 != -1360216880) {
            str2.equals("circle");
        } else if (iHashCode2 != -905816648) {
            if (iHashCode2 == 99657) {
                i4 = 2;
            }
        } else if (str2.equals("sesame")) {
            i4 = 3;
        }
        return new TextEmphasis(i4, i3, i);
    }
}
