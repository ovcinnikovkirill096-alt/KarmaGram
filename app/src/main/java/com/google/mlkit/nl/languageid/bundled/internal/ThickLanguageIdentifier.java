package com.google.mlkit.nl.languageid.bundled.internal;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.nl.languageid.IdentifiedLanguage;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.internal.LanguageIdentifierDelegate;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ThickLanguageIdentifier implements LanguageIdentifierDelegate {
    private static boolean zba;
    private final Context zbb;
    private long zbc;

    ThickLanguageIdentifier(Context context, LanguageIdentificationOptions languageIdentificationOptions) {
        this.zbb = context;
    }

    private native void nativeDestroy(long j);

    private native IdentifiedLanguage[] nativeIdentifyPossibleLanguages(long j, byte[] bArr, float f);

    private native long nativeInitFromBuffer(MappedByteBuffer mappedByteBuffer, long j);

    public static synchronized void zba() {
        if (zba) {
            return;
        }
        try {
            System.loadLibrary("language_id_l2c_jni");
            zba = true;
        } catch (UnsatisfiedLinkError e) {
            throw new MlKitException("Couldn't load language identification library.", 13, e);
        }
    }

    @Override // com.google.mlkit.nl.languageid.internal.LanguageIdentifierDelegate
    public final List identifyPossibleLanguages(String str, float f) {
        Preconditions.checkState(this.zbc != 0);
        IdentifiedLanguage[] identifiedLanguageArrNativeIdentifyPossibleLanguages = nativeIdentifyPossibleLanguages(this.zbc, str.getBytes(StandardCharsets.UTF_8), f);
        ArrayList arrayList = new ArrayList();
        for (IdentifiedLanguage identifiedLanguage : identifiedLanguageArrNativeIdentifyPossibleLanguages) {
            arrayList.add(new IdentifiedLanguage(identifiedLanguage.getLanguageTag(), identifiedLanguage.getConfidence()));
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:37:0x006c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:47:? A[Catch: IOException -> 0x0050, SYNTHETIC, TRY_LEAVE, TryCatch #3 {IOException -> 0x0050, blocks: (B:7:0x0013, B:13:0x004c, B:34:0x0074, B:33:0x0071, B:30:0x006c, B:8:0x0020, B:12:0x0049, B:28:0x0069, B:27:0x0066, B:24:0x0061, B:9:0x002d, B:19:0x0055, B:20:0x005c), top: B:42:0x0013, inners: #0, #1 }] */
    @Override // com.google.mlkit.nl.languageid.internal.LanguageIdentifierDelegate
    public final void init() throws MlKitException {
        Preconditions.checkState(this.zbc == 0);
        zba();
        try {
            AssetFileDescriptor assetFileDescriptorOpenFd = this.zbb.getAssets().openFd("tflite_langid.tflite.jpg");
            try {
                FileChannel channel = new FileInputStream(assetFileDescriptorOpenFd.getFileDescriptor()).getChannel();
                try {
                    long jNativeInitFromBuffer = nativeInitFromBuffer(channel.map(FileChannel.MapMode.READ_ONLY, assetFileDescriptorOpenFd.getStartOffset(), assetFileDescriptorOpenFd.getDeclaredLength()), assetFileDescriptorOpenFd.getDeclaredLength());
                    this.zbc = jNativeInitFromBuffer;
                    if (jNativeInitFromBuffer == 0) {
                        throw new MlKitException("Couldn't load language identification model", 13);
                    }
                    channel.close();
                    assetFileDescriptorOpenFd.close();
                    return;
                } catch (Throwable th) {
                    if (channel == null) {
                        throw th;
                    }
                    try {
                        channel.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                    throw new MlKitException("Couldn't open language identification model file", 13, e);
                }
            } catch (Throwable th3) {
                if (assetFileDescriptorOpenFd != null) {
                    throw th3;
                }
                try {
                    assetFileDescriptorOpenFd.close();
                    throw th3;
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                    throw th3;
                }
            }
            if (assetFileDescriptorOpenFd != null) {
                throw th3;
            }
            assetFileDescriptorOpenFd.close();
            throw th3;
        } catch (IOException e) {
            throw new MlKitException("Couldn't open language identification model file", 13, e);
        }
    }

    @Override // com.google.mlkit.nl.languageid.internal.LanguageIdentifierDelegate
    public final void release() {
        long j = this.zbc;
        if (j == 0) {
            return;
        }
        nativeDestroy(j);
        this.zbc = 0L;
    }
}
