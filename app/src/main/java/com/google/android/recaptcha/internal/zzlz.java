package com.google.android.recaptcha.internal;

final class zzlz extends IllegalArgumentException {
    zzlz(int i, int i2) {
        super("Unpaired surrogate at index " + i + " of " + i2);
    }
}
