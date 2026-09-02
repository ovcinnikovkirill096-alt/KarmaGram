package com.exteragram.messenger.export.api;

import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.export.output.html.HtmlWriter;
import j$.util.Objects;

public final class ApiWrap$ActionChatEditPhoto extends RecordTag {
    private final HtmlWriter.Photo photo;

    private /* synthetic */ boolean $record$equals(Object obj) {
        return (obj instanceof ApiWrap$ActionChatEditPhoto) && Objects.equals(this.photo, ((ApiWrap$ActionChatEditPhoto) obj).photo);
    }

    private /* synthetic */ Object[] $record$getFieldsAsObjects() {
        return new Object[]{this.photo};
    }

    public ApiWrap$ActionChatEditPhoto(HtmlWriter.Photo photo) {
        this.photo = photo;
    }

    public final boolean equals(Object obj) {
        return $record$equals(obj);
    }

    public final int hashCode() {
        return Objects.hashCode(this.photo);
    }

    public HtmlWriter.Photo photo() {
        return this.photo;
    }

    public final String toString() {
        return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ApiWrap$ActionChatEditPhoto.class, "photo");
    }
}
