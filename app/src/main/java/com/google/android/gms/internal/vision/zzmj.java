package com.google.android.gms.internal.vision;

final class zzmj extends zzme {
    zzmj() {
    }

    @Override // com.google.android.gms.internal.vision.zzme
    final int zza(int i, byte[] bArr, int i2, int i3) {
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
                if (zzma.zza(bArr, j4) < 0) {
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
                byte bZza = zzma.zza(bArr, j6);
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
                    if (zzma.zza(bArr, j6) <= -65) {
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
                byte bZza2 = zzma.zza(bArr, j6);
                if (bZza2 <= -65 && (((b2 << 28) + (bZza2 + 112)) >> 30) == 0) {
                    long j10 = j6 + 2;
                    if (zzma.zza(bArr, j9) <= -65) {
                        j6 += 3;
                        if (zzma.zza(bArr, j10) > -65) {
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
            byte bZza3 = zzma.zza(bArr, j6);
            if (bZza3 <= -65) {
                j = j3;
                if ((b2 != -32 || bZza3 >= -96) && (b2 != -19 || bZza3 < -96)) {
                    j6 += 2;
                    if (zzma.zza(bArr, j11) > -65) {
                    }
                }
            }
            return -1;
            i5 = i5;
            j3 = j;
            b = b;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzme
    final String zzb(byte[] bArr, int i, int i2) throws zzjk {
        if ((i | i2 | ((bArr.length - i) - i2)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
        }
        int i3 = i + i2;
        char[] cArr = new char[i2];
        int i4 = 0;
        while (i < i3) {
            byte bZza = zzma.zza(bArr, i);
            if (!zzmf.zzd(bZza)) {
                break;
            }
            i++;
            zzmf.zzb(bZza, cArr, i4);
            i4++;
        }
        int i5 = i4;
        while (i < i3) {
            int i6 = i + 1;
            byte bZza2 = zzma.zza(bArr, i);
            if (zzmf.zzd(bZza2)) {
                int i7 = i5 + 1;
                zzmf.zzb(bZza2, cArr, i5);
                while (i6 < i3) {
                    byte bZza3 = zzma.zza(bArr, i6);
                    if (!zzmf.zzd(bZza3)) {
                        break;
                    }
                    i6++;
                    zzmf.zzb(bZza3, cArr, i7);
                    i7++;
                }
                i5 = i7;
                i = i6;
            } else if (zzmf.zze(bZza2)) {
                if (i6 < i3) {
                    i += 2;
                    zzmf.zzb(bZza2, zzma.zza(bArr, i6), cArr, i5);
                    i5++;
                } else {
                    throw zzjk.zzh();
                }
            } else if (zzmf.zzf(bZza2)) {
                if (i6 < i3 - 1) {
                    int i8 = i + 2;
                    i += 3;
                    zzmf.zzb(bZza2, zzma.zza(bArr, i6), zzma.zza(bArr, i8), cArr, i5);
                    i5++;
                } else {
                    throw zzjk.zzh();
                }
            } else {
                if (i6 >= i3 - 2) {
                    throw zzjk.zzh();
                }
                byte bZza4 = zzma.zza(bArr, i6);
                int i9 = i + 3;
                byte bZza5 = zzma.zza(bArr, i + 2);
                i += 4;
                zzmf.zzb(bZza2, bZza4, bZza5, zzma.zza(bArr, i9), cArr, i5);
                i5 += 2;
            }
        }
        return new String(cArr, 0, i5);
    }

    @Override // com.google.android.gms.internal.vision.zzme
    final int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
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
            zzma.zza(bArr, j4, (byte) cCharAt);
            i4++;
            j4 = 1 + j4;
        }
        if (i4 == length) {
            return (int) j4;
        }
        while (i4 < length) {
            char cCharAt3 = charSequence.charAt(i4);
            if (cCharAt3 < 128 && j4 < j5) {
                zzma.zza(bArr, j4, (byte) cCharAt3);
                j3 = j5;
                j2 = j;
                j4 += j;
            } else if (cCharAt3 >= 2048 || j4 > j5 - 2) {
                j2 = j;
                if ((cCharAt3 >= 55296 && 57343 >= cCharAt3) || j4 > j5 - 3) {
                    j3 = j5;
                    if (j4 <= j3 - 4) {
                        int i5 = i4 + 1;
                        if (i5 != length) {
                            char cCharAt4 = charSequence.charAt(i5);
                            if (Character.isSurrogatePair(cCharAt3, cCharAt4)) {
                                int codePoint = Character.toCodePoint(cCharAt3, cCharAt4);
                                zzma.zza(bArr, j4, (byte) ((codePoint >>> 18) | 240));
                                zzma.zza(bArr, j4 + j2, (byte) (((codePoint >>> 12) & 63) | 128));
                                long j6 = j4 + 3;
                                zzma.zza(bArr, j4 + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                                j4 += 4;
                                zzma.zza(bArr, j6, (byte) ((codePoint & 63) | 128));
                                i4 = i5;
                            } else {
                                i4 = i5;
                            }
                        }
                        throw new zzmg(i4 - 1, length);
                    }
                    if (55296 > cCharAt3 || cCharAt3 > 57343 || ((i3 = i4 + 1) != length && Character.isSurrogatePair(cCharAt3, charSequence.charAt(i3)))) {
                        StringBuilder sb2 = new StringBuilder(46);
                        sb2.append("Failed writing ");
                        sb2.append(cCharAt3);
                        sb2.append(" at index ");
                        sb2.append(j4);
                        throw new ArrayIndexOutOfBoundsException(sb2.toString());
                    }
                    throw new zzmg(i4, length);
                }
                zzma.zza(bArr, j4, (byte) ((cCharAt3 >>> '\f') | 480));
                j3 = j5;
                long j7 = j4 + 2;
                zzma.zza(bArr, j4 + j2, (byte) (((cCharAt3 >>> 6) & 63) | 128));
                j4 += 3;
                zzma.zza(bArr, j7, (byte) ((cCharAt3 & '?') | 128));
            } else {
                j2 = j;
                long j8 = j4 + j2;
                zzma.zza(bArr, j4, (byte) ((cCharAt3 >>> 6) | 960));
                j4 += 2;
                zzma.zza(bArr, j8, (byte) ((cCharAt3 & '?') | 128));
                j3 = j5;
            }
            i4++;
            j = j2;
            j5 = j3;
        }
        return (int) j4;
    }

    private static int zza(byte[] bArr, int i, long j, int i2) {
        if (i2 == 0) {
            return zzmd.zzb(i);
        }
        if (i2 == 1) {
            return zzmd.zzb(i, zzma.zza(bArr, j));
        }
        if (i2 == 2) {
            return zzmd.zzb(i, zzma.zza(bArr, j), zzma.zza(bArr, j + 1));
        }
        throw new AssertionError();
    }
}
