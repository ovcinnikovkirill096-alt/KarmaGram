package com.radolyn.ayugram;

import com.exteragram.messenger.backup.PreferencesUtils$$ExternalSyntheticBackport1;
import java.util.ArrayList;
import java.util.Set;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.zzz0;

public abstract class AyuConstants {
    public static final int FILTERS_UPDATED;
    public static final int FIX_FORWARD;
    public static final int FORCE_MESSAGES_UPDATE;
    public static final int HISTORY_FLUSHED_NOTIFICATION;
    public static final int LAST_SEEN_PILL_FETCH;
    public static final int LAST_SEEN_PILL_UPDATE;
    public static final int MESSAGES_DELETED_NOTIFICATION;
    private static int OPTIONS;
    public static final int OPTION_DEBUG_SEND_SCREENSHOT;
    public static final int OPTION_DELETED_HISTORY;
    public static final int OPTION_GHOST_READ_EXCLUSION;
    public static final int OPTION_GHOST_TYPING_EXCLUSION;
    public static final int OPTION_SWITCH_FILTERING;
    public static final int OPTION_VIEW_FILTERS;
    public static final int PEEK_ONLINE_ITEM;
    public static final int PEER_RESOLVED_NOTIFICATION;
    public static final int SHADOW_BAN_ITEM;
    public static final int UPDATE_CHAT_RESTRICTION;
    private static int notificationId;
    public static final String DEFAULT_JUMPSCARES_CHANNEL = "ayugram_easter";
    public static final String UPDATES_CHANNEL_USERNAME = "UFhsu4hOFEufes73fkEeuw";
    public static final int OPTION_CLEAR_DELETED = 80;
    public static String AYU_DATABASE = zzz0.d("â€â€‚â­â®â€€â€‹â€Šâ€Šâ€€â¯â¬â€€â€¯â€‹â€€â®â€‹â€€â­â®â€€â¯â«â€€â­â®");
    public static String APP_NAME = zzz0.d("â€â€‚âªâ¯â€€â€‹â€Šâ€Šâ€€â¯â¬â€€â«âªâ€€â¯â€¯â€€â­â®â€€â®â¯");
    public static String BUILD_STORE_PACKAGE = "";
    public static String BUILD_ORIGINAL_PACKAGE = zzz0.d("â€â€‚â¯â€‹â€€â¯â€¯â€€â®â€¯â€€â€¯â€Œâ€€â¯â«â€€â®â€Œâ€€â®â®â€€â®â€Œâ€€â®â€¯â€€â¯â€¯â€€â­â®â€€â®â¯â€€â€¯â€Œâ€€â®â¯â€€â®â€Œâ€€â¯âªâ€€â¯âªâ€€â®â€Œâ€€â¯â€Šâ€€â®â€¯â€€â®â€Œâ€€â¯â€¯â€€â€¯â€Œâ€€â¯â®â€€â®â€Œâ€€â­â¯");
    public static int MAX_CACHE_SIZE_300_MB = -10;
    public static final ArrayList DEFAULT_JUMPSCARES_KEYS = new ArrayList() { // from class: com.radolyn.ayugram.AyuConstants.1
        {
            add("/lobster");
            add("/komaru");
            add("/saul");
            add("/pipe");
            add("/augh");
            add("/relax");
            add("/xiaomi");
        }
    };
    public static final ArrayList DEFAULT_JUMPSCARES_VALUES = new ArrayList() { // from class: com.radolyn.ayugram.AyuConstants.2
        {
            add(String.valueOf(5));
            add(String.valueOf(6));
            add(String.valueOf(7));
            add(String.valueOf(8));
            add(String.valueOf(9));
            add(String.valueOf(10));
            add(String.valueOf(11));
        }
    };
    public static final Set ALLOWED_PASTE_SERVICES = PreferencesUtils$$ExternalSyntheticBackport1.m(new Object[]{"dpaste.com", "gist.githubusercontent.com", "pastebin.com", "nekobin.com"});
    public static final int FIX_SCHEDULED_BAR = 6969;

    static {
        int i = 80 + 1;
        OPTION_VIEW_FILTERS = i;
        OPTION_SWITCH_FILTERING = i + 1;
        PEEK_ONLINE_ITEM = i + 2;
        SHADOW_BAN_ITEM = i + 3;
        OPTION_DELETED_HISTORY = i + 4;
        OPTION_GHOST_READ_EXCLUSION = i + 5;
        OPTION_GHOST_TYPING_EXCLUSION = i + 6;
        OPTIONS = i + 8;
        OPTION_DEBUG_SEND_SCREENSHOT = i + 7;
        int i2 = 6969 + 1;
        FIX_FORWARD = i2;
        MESSAGES_DELETED_NOTIFICATION = i2 + 1;
        HISTORY_FLUSHED_NOTIFICATION = i2 + 2;
        PEER_RESOLVED_NOTIFICATION = i2 + 3;
        UPDATE_CHAT_RESTRICTION = i2 + 4;
        FORCE_MESSAGES_UPDATE = i2 + 5;
        LAST_SEEN_PILL_UPDATE = i2 + 6;
        LAST_SEEN_PILL_FETCH = i2 + 7;
        notificationId = i2 + 9;
        FILTERS_UPDATED = i2 + 8;
    }
}
