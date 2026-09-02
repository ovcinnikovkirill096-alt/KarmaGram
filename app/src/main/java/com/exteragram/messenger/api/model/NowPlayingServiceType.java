package com.exteragram.messenger.api.model;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'NONE' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:485)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:399)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:364)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:349)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInvoke(EnumVisitor.java:315)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:288)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:160)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
public final class NowPlayingServiceType {
    private static final /* synthetic */ EnumEntries $ENTRIES;
    private static final /* synthetic */ NowPlayingServiceType[] $VALUES;
    public static final NowPlayingServiceType LAST_FM;
    public static final NowPlayingServiceType NONE;
    public static final NowPlayingServiceType STATS_FM;
    private final String displayName;

    private static final /* synthetic */ NowPlayingServiceType[] $values() {
        return new NowPlayingServiceType[]{NONE, LAST_FM, STATS_FM};
    }

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }

    public static NowPlayingServiceType valueOf(String str) {
        return (NowPlayingServiceType) Enum.valueOf(NowPlayingServiceType.class, str);
    }

    public static NowPlayingServiceType[] values() {
        return (NowPlayingServiceType[]) $VALUES.clone();
    }

    private NowPlayingServiceType(String str, int i, String str2) {
        super(str, i);
        this.displayName = str2;
    }

    public final String getDisplayName() {
        return this.displayName;
    }

    static {
        String string = LocaleController.getString(R.string.None);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        NONE = new NowPlayingServiceType("NONE", 0, string);
        LAST_FM = new NowPlayingServiceType("LAST_FM", 1, "Last.fm");
        STATS_FM = new NowPlayingServiceType("STATS_FM", 2, "Stats.fm");
        NowPlayingServiceType[] nowPlayingServiceTypeArr$values = $values();
        $VALUES = nowPlayingServiceTypeArr$values;
        $ENTRIES = EnumEntriesKt.enumEntries(nowPlayingServiceTypeArr$values);
    }
}
