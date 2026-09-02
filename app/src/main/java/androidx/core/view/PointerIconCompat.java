package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.view.PointerIcon;

public final class PointerIconCompat {
    private final PointerIcon mPointerIcon;

    private PointerIconCompat(PointerIcon pointerIcon) {
        this.mPointerIcon = pointerIcon;
    }

    public Object getPointerIcon() {
        return this.mPointerIcon;
    }

    public static PointerIconCompat getSystemIcon(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new PointerIconCompat(Api24Impl.getSystemIcon(context, i));
        }
        return new PointerIconCompat(null);
    }

    static class Api24Impl {
        static PointerIcon getSystemIcon(Context context, int i) {
            return PointerIcon.getSystemIcon(context, i);
        }
    }
}
