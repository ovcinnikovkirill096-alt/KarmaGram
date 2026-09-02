package org.telegram.ui.Components.voip;

import android.view.View;
import androidx.annotation.Keep;

public abstract class CallSwipeView extends View {
    /* JADX INFO: renamed from: -$$Nest$fgetarrowAlphas, reason: not valid java name */
    static /* bridge */ /* synthetic */ int[] m12159$$Nest$fgetarrowAlphas(CallSwipeView callSwipeView) {
        throw null;
    }

    private class ArrowAnimWrapper {
        private int index;

        @Keep
        public int getArrowAlpha() {
            return CallSwipeView.m12159$$Nest$fgetarrowAlphas(null)[this.index];
        }

        @Keep
        public void setArrowAlpha(int i) {
            CallSwipeView.m12159$$Nest$fgetarrowAlphas(null)[this.index] = i;
        }
    }
}
