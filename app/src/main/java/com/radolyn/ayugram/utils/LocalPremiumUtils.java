package com.radolyn.ayugram.utils;

import android.content.SharedPreferences;
import android.util.Base64;
import com.exteragram.messenger.backup.PreferencesUtils;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

public abstract class LocalPremiumUtils {
    private static boolean configLoaded;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    private static final Object sync = new Object();

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = PreferencesUtils.getPreferences("localpremiumconfig");
                preferences = preferences2;
                editor = preferences2.edit();
                configLoaded = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void setFakePremiumEmojiStatus(long j, TLRPC.EmojiStatus emojiStatus) {
        if (emojiStatus == null) {
            editor.remove("fakePremium_" + j).apply();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.LocalPremiumUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LocalPremiumUtils.$r8$lambda$708j3LXH53nTvalClpR0WzRrVvY();
                }
            });
            return;
        }
        NativeByteBuffer nativeByteBuffer = null;
        try {
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(emojiStatus.getObjectSize());
            try {
                emojiStatus.serializeToStream(nativeByteBuffer2);
                nativeByteBuffer2.rewind();
                byte[] bArr = new byte[nativeByteBuffer2.remaining()];
                nativeByteBuffer2.readBytes(bArr, false);
                String strEncodeToString = Base64.encodeToString(bArr, 0);
                editor.putString("fakePremium_" + j, strEncodeToString).apply();
                nativeByteBuffer2.reuse();
            } catch (Throwable unused) {
                nativeByteBuffer = nativeByteBuffer2;
                if (nativeByteBuffer != null) {
                    nativeByteBuffer.reuse();
                }
            }
        } catch (Throwable unused2) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.utils.LocalPremiumUtils$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                LocalPremiumUtils.$r8$lambda$gd5KPBC750FAhTlJMKaXCuhZaKk();
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$708j3LXH53nTvalClpR0WzRrVvY() {
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
    }

    public static /* synthetic */ void $r8$lambda$gd5KPBC750FAhTlJMKaXCuhZaKk() {
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
    }

    public static TLRPC.EmojiStatus getFakePremiumEmojiStatus(long j) {
        NativeByteBuffer nativeByteBuffer;
        String string = preferences.getString("fakePremium_" + j, null);
        if (string == null) {
            return null;
        }
        try {
            byte[] bArrDecode = Base64.decode(string, 0);
            nativeByteBuffer = new NativeByteBuffer(bArrDecode.length);
            try {
                nativeByteBuffer.writeBytes(bArrDecode);
                nativeByteBuffer.position(0);
                TLRPC.EmojiStatus emojiStatusTLdeserialize = TLRPC.EmojiStatus.TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                nativeByteBuffer.reuse();
                return emojiStatusTLdeserialize;
            } catch (Throwable unused) {
                if (nativeByteBuffer != null) {
                    nativeByteBuffer.reuse();
                }
                return null;
            }
        } catch (Throwable unused2) {
            nativeByteBuffer = null;
        }
    }

    public static void setLocalColorData(int i, int i2, long j, int i3, long j2) {
        editor.putInt("localQuoteColor_" + UserConfig.getInstance(i).getClientUserId(), i2).apply();
        editor.putLong("localQuoteEmoji_" + UserConfig.getInstance(i).getClientUserId(), j).apply();
        editor.putInt("localProfileColor_" + UserConfig.getInstance(i).getClientUserId(), i3).apply();
        editor.putLong("localProfileEmoji_" + UserConfig.getInstance(i).getClientUserId(), j2).apply();
    }

    public static int getLocalQuoteColor(long j) {
        return preferences.getInt("localQuoteColor_" + j, -1);
    }

    public static long getLocalQuoteEmoji(long j) {
        return preferences.getLong("localQuoteEmoji_" + j, -1L);
    }

    public static int getLocalProfileColor(long j) {
        return preferences.getInt("localProfileColor_" + j, -1);
    }

    public static long getLocalProfileEmoji(long j) {
        return preferences.getLong("localProfileEmoji_" + j, -1L);
    }
}
