package com.exteragram.messenger.nowplaying;

import java.util.Iterator;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt;

public enum ServiceEmoji {
    MUSIC(5271627010681108586L),
    SPOTIFY(5271857023359681001L),
    TELEGRAM(5325674462522144646L);

    private final long documentId;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    public static final Companion Companion = new Companion(null);

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }

    ServiceEmoji(long j) {
        this.documentId = j;
    }

    public final long getDocumentId() {
        return this.documentId;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ServiceEmoji fromString(String str) {
            Object next;
            Iterator<E> it = ServiceEmoji.getEntries().iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!StringsKt.equals(((ServiceEmoji) next).name(), str, true));
            ServiceEmoji serviceEmoji = (ServiceEmoji) next;
            return serviceEmoji == null ? ServiceEmoji.MUSIC : serviceEmoji;
        }
    }
}
