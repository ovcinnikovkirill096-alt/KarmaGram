package androidx.activity.result;

import androidx.activity.result.contract.ActivityResultContracts$PickVisualMedia;
import kotlin.jvm.internal.Intrinsics;

public abstract class PickVisualMediaRequestKt {
    public static final PickVisualMediaRequest PickVisualMediaRequest(ActivityResultContracts$PickVisualMedia.VisualMediaType mediaType) {
        Intrinsics.checkNotNullParameter(mediaType, "mediaType");
        return new PickVisualMediaRequest.Builder().setMediaType(mediaType).build();
    }
}
