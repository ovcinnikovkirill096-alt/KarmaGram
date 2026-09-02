package com.google.android.gms.internal.clearcut;

import java.nio.ByteBuffer;

final class zzfj extends zzfg {
    zzfj() {
    }

    private static int zza(byte[] bArr, int i, long j, int i2) {
        if (i2 == 0) {
            return zzff.zzam(i);
        }
        if (i2 == 1) {
            return zzff.zzp(i, zzfd.zza(bArr, j));
        }
        if (i2 == 2) {
            return zzff.zzd(i, zzfd.zza(bArr, j), zzfd.zza(bArr, j + 1));
        }
        throw new AssertionError();
    }

    @Override // com.google.android.gms.internal.clearcut.zzfg
    final int zzb(int i, byte[] bArr, int i2, int i3) {
        int i4;
        long j;
        int i5 = 2;
        byte b = 0;
        if ((i2 | i3 | (bArr.length - i3)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
        }
        long j2 = i2;
        int i6 = (int) (((long) i3) - j2);
        long j3 = 1;
        if (i6 >= 16) {
            i4 = 0;
            long j4 = j2;
            while (true) {
                if (i4 >= i6) {
                    i4 = i6;
                    break;
                }
                long j5 = j4 + 1;
                if (zzfd.zza(bArr, j4) < 0) {
                    break;
                }
                i4++;
                j4 = j5;
            }
        } else {
            i4 = 0;
        }
        int i7 = i6 - i4;
        long j6 = j2 + ((long) i4);
        while (true) {
            byte b2 = b;
            while (i7 > 0) {
                long j7 = j6 + j3;
                byte bZza = zzfd.zza(bArr, j6);
                if (bZza < 0) {
                    b2 = bZza;
                    j6 = j7;
                    break;
                }
                i7--;
                b2 = bZza;
                j6 = j7;
            }
            if (i7 == 0) {
                return b;
            }
            int i8 = i7 - 1;
            if (b2 < -32) {
                if (i8 == 0) {
                    return b2;
                }
                i7 -= 2;
                if (b2 >= -62) {
                    long j8 = j6 + j3;
                    if (zzfd.zza(bArr, j6) <= -65) {
                        j6 = j8;
                        j = j3;
                    }
                }
                return -1;
            }
            if (b2 >= -16) {
                j = j3;
                if (i8 < 3) {
                    return zza(bArr, b2, j6, i8);
                }
                i7 -= 4;
                long j9 = j6 + j;
                byte bZza2 = zzfd.zza(bArr, j6);
                if (bZza2 <= -65 && (((b2 << 28) + (bZza2 + 112)) >> 30) == 0) {
                    long j10 = j6 + 2;
                    if (zzfd.zza(bArr, j9) <= -65) {
                        j6 += 3;
                        if (zzfd.zza(bArr, j10) > -65) {
                        }
                    }
                }
                return -1;
            }
            if (i8 < i5) {
                return zza(bArr, b2, j6, i8);
            }
            i7 -= 3;
            long j11 = j6 + j3;
            byte bZza3 = zzfd.zza(bArr, j6);
            if (bZza3 <= -65) {
                j = j3;
                if ((b2 != -32 || bZza3 >= -96) && (b2 != -19 || bZza3 < -96)) {
                    j6 += 2;
                    if (zzfd.zza(bArr, j11) > -65) {
                    }
                }
            }
            return -1;
            i5 = i5;
            j3 = j;
            b = b;
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzfg
    final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
        long j;
        long j2;
        long j3;
        int i3;
        char cCharAt;
        long j4 = i;
        long j5 = ((long) i2) + j4;
        int length = charSequence.length();
        if (length > i2 || bArr.length - i2 < i) {
            char cCharAt2 = charSequence.charAt(length - 1);
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(cCharAt2);
            sb.append(" at index ");
            sb.append(i + i2);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i4 = 0;
        while (true) {
            j = 1;
            if (i4 >= length || (cCharAt = charSequence.charAt(i4)) >= 128) {
                break;
            }
            zzfd.zza(bArr, j4, (byte) cCharAt);
            i4++;
            j4 = 1 + j4;
        }
        if (i4 == length) {
            return (int) j4;
        }
        while (i4 < length) {
            char cCharAt3 = charSequence.charAt(i4);
            if (cCharAt3 < 128 && j4 < j5) {
                zzfd.zza(bArr, j4, (byte) cCharAt3);
                j3 = j5;
                j2 = j;
                j4 += j;
            } else if (cCharAt3 >= 2048 || j4 > j5 - 2) {
                j2 = j;
                if ((cCharAt3 >= 55296 && 57343 >= cCharAt3) || j4 > j5 - 3) {
                    j3 = j5;
                    if (j4 > j3 - 4) {
                        if (55296 <= cCharAt3 && cCharAt3 <= 57343 && ((i3 = i4 + 1) == length || !Character.isSurrogatePair(cCharAt3, charSequence.charAt(i3)))) {
                            throw new zzfi(i4, length);
                        }
                        StringBuilder sb2 = new StringBuilder(46);
                        sb2.append("Failed writing ");
                        sb2.append(cCharAt3);
                        sb2.append(" at index ");
                        sb2.append(j4);
                        throw new ArrayIndexOutOfBoundsException(sb2.toString());
                    }
                    int i5 = i4 + 1;
                    if (i5 != length) {
                        char cCharAt4 = charSequence.charAt(i5);
                        if (Character.isSurrogatePair(cCharAt3, cCharAt4)) {
                            int codePoint = Character.toCodePoint(cCharAt3, cCharAt4);
                            zzfd.zza(bArr, j4, (byte) ((codePoint >>> 18) | 240));
                            zzfd.zza(bArr, j4 + j2, (byte) (((codePoint >>> 12) & 63) | 128));
                            long j6 = j4 + 3;
                            zzfd.zza(bArr, j4 + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                            j4 += 4;
                            zzfd.zza(bArr, j6, (byte) ((codePoint & 63) | 128));
                            i4 = i5;
                        } else {
                            i4 = i5;
                        }
                    }
                    throw new zzfi(i4 - 1, length);
                }
                zzfd.zza(bArr, j4, (byte) ((cCharAt3 >>> '\f') | 480));
                j3 = j5;
                long j7 = j4 + 2;
                zzfd.zza(bArr, j4 + j2, (byte) (((cCharAt3 >>> 6) & 63) | 128));
                j4 += 3;
                zzfd.zza(bArr, j7, (byte) ((cCharAt3 & '?') | 128));
            } else {
                j2 = j;
                long j8 = j4 + j2;
                zzfd.zza(bArr, j4, (byte) ((cCharAt3 >>> 6) | 960));
                j4 += 2;
                zzfd.zza(bArr, j8, (byte) ((cCharAt3 & '?') | 128));
                j3 = j5;
            }
            i4++;
            j = j2;
            j5 = j3;
        }
        return (int) j4;
    }

    @Override // com.google.android.gms.internal.clearcut.zzfg
    final void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        long j;
        char c;
        long j2;
        long j3;
        int i;
        char c2;
        char cCharAt;
        long jZzb = zzfd.zzb(byteBuffer);
        long jPosition = ((long) byteBuffer.position()) + jZzb;
        long jLimit = ((long) byteBuffer.limit()) + jZzb;
        int length = charSequence.length();
        if (length > jLimit - jPosition) {
            char cCharAt2 = charSequence.charAt(length - 1);
            int iLimit = byteBuffer.limit();
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(cCharAt2);
            sb.append(" at index ");
            sb.append(iLimit);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i2 = 0;
        while (true) {
            j = 1;
            c = 128;
            if (i2 >= length || (cCharAt = charSequence.charAt(i2)) >= 128) {
                break;
            }
            zzfd.zza(jPosition, (byte) cCharAt);
            i2++;
            jPosition = 1 + jPosition;
        }
        if (i2 == length) {
            j2 = jPosition - jZzb;
        } else {
            while (i2 < length) {
                char cCharAt3 = charSequence.charAt(i2);
                if (cCharAt3 >= c || jPosition >= jLimit) {
                    j3 = j;
                    if (cCharAt3 < 2048 && jPosition <= jLimit - 2) {
                        long j4 = jPosition + j3;
                        zzfd.zza(jPosition, (byte) ((cCharAt3 >>> 6) | 960));
                        jPosition += 2;
                        zzfd.zza(j4, (byte) ((cCharAt3 & '?') | 128));
                    } else {
                        if ((cCharAt3 >= 55296 && 57343 >= cCharAt3) || jPosition > jLimit - 3) {
                            jZzb = jZzb;
                            jLimit = jLimit;
                            if (jPosition > jLimit - 4) {
                                if (55296 <= cCharAt3 && cCharAt3 <= 57343 && ((i = i2 + 1) == length || !Character.isSurrogatePair(cCharAt3, charSequence.charAt(i)))) {
                                    throw new zzfi(i2, length);
                                }
                                StringBuilder sb2 = new StringBuilder(46);
                                sb2.append("Failed writing ");
                                sb2.append(cCharAt3);
                                sb2.append(" at index ");
                                sb2.append(jPosition);
                                throw new ArrayIndexOutOfBoundsException(sb2.toString());
                            }
                            int i3 = i2 + 1;
                            if (i3 != length) {
                                char cCharAt4 = charSequence.charAt(i3);
                                if (Character.isSurrogatePair(cCharAt3, cCharAt4)) {
                                    int codePoint = Character.toCodePoint(cCharAt3, cCharAt4);
                                    zzfd.zza(jPosition, (byte) ((codePoint >>> 18) | 240));
                                    c2 = 128;
                                    zzfd.zza(jPosition + j3, (byte) (((codePoint >>> 12) & 63) | 128));
                                    long j5 = jPosition + 3;
                                    zzfd.zza(jPosition + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                                    jPosition += 4;
                                    zzfd.zza(j5, (byte) ((codePoint & 63) | 128));
                                    i2 = i3;
                                } else {
                                    i2 = i3;
                                }
                            }
                            throw new zzfi(i2 - 1, length);
                        }
                        zzfd.zza(jPosition, (byte) ((cCharAt3 >>> '\f') | 480));
                        long j6 = jPosition + 2;
                        zzfd.zza(jPosition + j3, (byte) (((cCharAt3 >>> 6) & 63) | 128));
                        jPosition += 3;
                        zzfd.zza(j6, (byte) ((cCharAt3 & '?') | 128));
                    }
                    c2 = 128;
                } else {
                    zzfd.zza(jPosition, (byte) cCharAt3);
                    jZzb = jZzb;
                    jLimit = jLimit;
                    c2 = c;
                    jPosition += j;
                    j3 = j;
                }
                i2++;
                c = c2;
                j = j3;
                jZzb = jZzb;
                jLimit = jLimit;
            }
            j2 = jPosition - jZzb;
        }
        byteBuffer.position((int) j2);
    }
}
