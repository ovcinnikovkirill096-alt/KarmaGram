package androidx.camera.video.internal.encoder;

import android.media.MediaFormat;
import androidx.camera.core.impl.Timebase;

public interface EncoderConfig {
    Timebase getInputTimebase();

    String getMimeType();

    MediaFormat toMediaFormat();
}
