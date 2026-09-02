package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;

abstract class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = {1769172845, 1769172786, 1769172787, 1769172788, 1769172789, 1769172790, 1769172793, 1635148593, 1752589105, 1751479857, 1635135537, 1836069937, 1836069938, 862401121, 862401122, 862417462, 862417718, 862414134, 862414646, 1295275552, 1295270176, 1714714144, 1801741417, 1295275600, 1903435808, 1297305174, 1684175153, 1769172332, 1885955686};

    public static boolean sniffFragmented(ExtractorInput extractorInput) {
        return sniffInternal(extractorInput, true, false);
    }

    public static boolean sniffUnfragmented(ExtractorInput extractorInput, boolean z) {
        return sniffInternal(extractorInput, false, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean sniffInternal(ExtractorInput extractorInput, boolean z, boolean z2) {
        boolean z3;
        int i;
        long length = extractorInput.getLength();
        long j = -1;
        int i2 = (length > (-1L) ? 1 : (length == (-1L) ? 0 : -1));
        long j2 = 4096;
        if (i2 != 0 && length <= 4096) {
            j2 = length;
        }
        int i3 = (int) j2;
        ParsableByteArray parsableByteArray = new ParsableByteArray(64);
        int i4 = 0;
        int i5 = 0;
        boolean z4 = false;
        while (true) {
            if (i5 < i3) {
                parsableByteArray.reset(8);
                if (extractorInput.peekFully(parsableByteArray.getData(), i4, 8, true)) {
                    long unsignedInt = parsableByteArray.readUnsignedInt();
                    int i6 = parsableByteArray.readInt();
                    if (unsignedInt == 1) {
                        extractorInput.peekFully(parsableByteArray.getData(), 8, 8);
                        parsableByteArray.setLimit(16);
                        i = 16;
                        unsignedInt = parsableByteArray.readLong();
                    } else {
                        if (unsignedInt == 0) {
                            long length2 = extractorInput.getLength();
                            if (length2 != j) {
                                unsignedInt = (length2 - extractorInput.getPeekPosition()) + ((long) 8);
                            }
                        }
                        i = 8;
                    }
                    long j3 = i;
                    if (unsignedInt < j3) {
                        return i4;
                    }
                    int i7 = i5 + i;
                    boolean z5 = i4;
                    if (i6 == 1836019574) {
                        i3 += (int) unsignedInt;
                        if (i2 != 0 && i3 > length) {
                            i3 = (int) length;
                        }
                        i5 = i7;
                        i4 = z5 ? 1 : 0;
                        j = -1;
                    } else {
                        if (i6 == 1836019558 || i6 == 1836475768) {
                            z3 = true;
                            return z4 && z == z3;
                        }
                        int i8 = i2;
                        if ((((long) i7) + unsignedInt) - j3 < i3) {
                            int i9 = (int) (unsignedInt - j3);
                            i5 = i7 + i9;
                            if (i6 == 1718909296) {
                                if (i9 < 8) {
                                    return z5;
                                }
                                parsableByteArray.reset(i9);
                                extractorInput.peekFully(parsableByteArray.getData(), z5 ? 1 : 0, i9);
                                int i10 = i9 / 4;
                                for (int i11 = 0; i11 < i10; i11++) {
                                    if (i11 == 1) {
                                        parsableByteArray.skipBytes(4);
                                    } else if (isCompatibleBrand(parsableByteArray.readInt(), z2)) {
                                        z4 = true;
                                        break;
                                    }
                                }
                                if (!z4) {
                                    return false;
                                }
                            } else if (i9 != 0) {
                                extractorInput.advancePeekPosition(i9);
                            }
                            i2 = i8;
                            j = -1;
                            i4 = 0;
                        }
                    }
                }
            }
            z3 = false;
            if (z4) {
                return false;
            }
        }
    }

    private static boolean isCompatibleBrand(int i, boolean z) {
        if ((i >>> 8) == 3368816) {
            return true;
        }
        if (i == 1751476579 && z) {
            return true;
        }
        for (int i2 : COMPATIBLE_BRANDS) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }
}
