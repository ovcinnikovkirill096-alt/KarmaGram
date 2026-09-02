package com.radolyn.ayugram.utils.filters;

import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.radolyn.ayugram.AyuForward$ForwardChunk$$ExternalSyntheticRecord0;
import j$.util.Objects;
import java.util.regex.Pattern;

public final class ReversiblePattern extends RecordTag {
    private final Pattern pattern;
    private final boolean reversed;

    private /* synthetic */ boolean $record$equals(Object obj) {
        if (!(obj instanceof ReversiblePattern)) {
            return false;
        }
        ReversiblePattern reversiblePattern = (ReversiblePattern) obj;
        return this.reversed == reversiblePattern.reversed && Objects.equals(this.pattern, reversiblePattern.pattern);
    }

    private /* synthetic */ Object[] $record$getFieldsAsObjects() {
        return new Object[]{this.pattern, Boolean.valueOf(this.reversed)};
    }

    public ReversiblePattern(Pattern pattern, boolean z) {
        this.pattern = pattern;
        this.reversed = z;
    }

    public final boolean equals(Object obj) {
        return $record$equals(obj);
    }

    public final int hashCode() {
        return AyuForward$ForwardChunk$$ExternalSyntheticRecord0.m(this.reversed, this.pattern);
    }

    public Pattern pattern() {
        return this.pattern;
    }

    public boolean reversed() {
        return this.reversed;
    }

    public final String toString() {
        return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ReversiblePattern.class, "pattern;reversed");
    }
}
