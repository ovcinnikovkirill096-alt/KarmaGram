package com.radolyn.ayugram.controllers;

import com.radolyn.ayugram.AyuGhostConfig;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.Components.BulletinFactory;

public class AyuGhostController extends BaseController {
    private static final AyuGhostController[] Instance = new AyuGhostController[16];

    public AyuGhostController(int i) {
        super(i);
    }

    public static AyuGhostController getInstance(int i) {
        AyuGhostController ayuGhostController;
        AyuGhostController[] ayuGhostControllerArr = Instance;
        AyuGhostController ayuGhostController2 = ayuGhostControllerArr[i];
        if (ayuGhostController2 != null) {
            return ayuGhostController2;
        }
        synchronized (AyuGhostController.class) {
            try {
                ayuGhostController = ayuGhostControllerArr[i];
                if (ayuGhostController == null) {
                    ayuGhostController = new AyuGhostController(i);
                    ayuGhostControllerArr[i] = ayuGhostController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuGhostController;
    }

    public boolean isGhostModeActive() {
        return AyuGhostConfig.isGhostModeActive(getUserConfig().getClientUserId());
    }

    public void setGhostMode(boolean z, BulletinFactory bulletinFactory) {
        AyuGhostConfig.setGhostMode(getUserConfig().getClientUserId(), z, bulletinFactory);
    }

    public void toggleGhostMode(BulletinFactory bulletinFactory) {
        AyuGhostConfig.toggleGhostMode(getUserConfig().getClientUserId(), bulletinFactory);
    }

    public boolean isUseScheduledMessages() {
        return AyuGhostConfig.isUseScheduledMessages(getUserConfig().getClientUserId());
    }

    public boolean isSendReadMessagePackets() {
        return AyuGhostConfig.isSendReadMessagePackets(getUserConfig().getClientUserId());
    }

    public boolean isSendReadStoryPackets() {
        return AyuGhostConfig.isSendReadStoryPackets(getUserConfig().getClientUserId());
    }

    public boolean isSendOnlinePackets() {
        return AyuGhostConfig.isSendOnlinePackets(getUserConfig().getClientUserId());
    }

    public boolean isSendUploadProgress() {
        return AyuGhostConfig.isSendUploadProgress(getUserConfig().getClientUserId());
    }

    public boolean isSendOfflinePacketAfterOnline() {
        return AyuGhostConfig.isSendOfflinePacketAfterOnline(getUserConfig().getClientUserId());
    }

    public boolean isMarkReadAfterAction() {
        return AyuGhostConfig.isMarkReadAfterAction(getUserConfig().getClientUserId());
    }

    public boolean isSendWithoutSound() {
        return AyuGhostConfig.isSendWithoutSound(getUserConfig().getClientUserId());
    }

    public String getSendWithoutSoundString() {
        return LocaleController.getString(isSendWithoutSound() ? R.string.SendWithSound : R.string.SendWithoutSound);
    }

    public int getSendWithoutSoundIcon() {
        return isSendWithoutSound() ? R.drawable.input_notify_on : R.drawable.input_notify_off;
    }

    public boolean isSuggestGhostModeBeforeViewingStory() {
        return AyuGhostConfig.isSuggestGhostModeBeforeViewingStory(getUserConfig().getClientUserId());
    }

    public boolean showLastOnline() {
        return !isSendOnlinePackets() || isSendOfflinePacketAfterOnline() || isGhostModeActive();
    }
}
