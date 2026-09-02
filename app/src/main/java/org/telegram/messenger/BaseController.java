package org.telegram.messenger;

import com.radolyn.ayugram.controllers.AyuAttachments;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.controllers.AyuFilterController;
import com.radolyn.ayugram.controllers.AyuGhostController;
import com.radolyn.ayugram.controllers.AyuMapper;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.controllers.AyuSpyController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Components.Paint.PersistColorPalette;

public class BaseController {
    protected final int currentAccount;
    private AccountInstance parentAccountInstance;

    public BaseController(int i) {
        this.parentAccountInstance = AccountInstance.getInstance(i);
        this.currentAccount = i;
    }

    protected final AccountInstance getAccountInstance() {
        return this.parentAccountInstance;
    }

    protected final AppGlobalConfig getAppGlobalConfig() {
        return getMessagesController().config;
    }

    protected final MessagesController getMessagesController() {
        return this.parentAccountInstance.getMessagesController();
    }

    protected final ContactsController getContactsController() {
        return this.parentAccountInstance.getContactsController();
    }

    protected final PersistColorPalette getColorPalette() {
        return this.parentAccountInstance.getColorPalette();
    }

    protected final MediaDataController getMediaDataController() {
        return this.parentAccountInstance.getMediaDataController();
    }

    protected final ConnectionsManager getConnectionsManager() {
        return this.parentAccountInstance.getConnectionsManager();
    }

    protected final LocationController getLocationController() {
        return this.parentAccountInstance.getLocationController();
    }

    protected final NotificationsController getNotificationsController() {
        return this.parentAccountInstance.getNotificationsController();
    }

    protected final NotificationCenter getNotificationCenter() {
        return this.parentAccountInstance.getNotificationCenter();
    }

    protected final UserConfig getUserConfig() {
        return this.parentAccountInstance.getUserConfig();
    }

    protected final MessagesStorage getMessagesStorage() {
        return this.parentAccountInstance.getMessagesStorage();
    }

    protected final DownloadController getDownloadController() {
        return this.parentAccountInstance.getDownloadController();
    }

    protected final SendMessagesHelper getSendMessagesHelper() {
        return this.parentAccountInstance.getSendMessagesHelper();
    }

    protected final SecretChatHelper getSecretChatHelper() {
        return this.parentAccountInstance.getSecretChatHelper();
    }

    protected final StatsController getStatsController() {
        return this.parentAccountInstance.getStatsController();
    }

    protected final FileLoader getFileLoader() {
        return this.parentAccountInstance.getFileLoader();
    }

    protected final FileRefController getFileRefController() {
        return this.parentAccountInstance.getFileRefController();
    }

    protected final MemberRequestsController getMemberRequestsController() {
        return this.parentAccountInstance.getMemberRequestsController();
    }

    protected final AyuMessagesController getAyuMessagesController() {
        return this.parentAccountInstance.getAyuMessagesController();
    }

    protected final AyuMapper getAyuMapper() {
        return this.parentAccountInstance.getAyuMapper();
    }

    protected final AyuAttachments getAyuAttachments() {
        return this.parentAccountInstance.getAyuAttachments();
    }

    protected final AyuFilterController getAyuFilterController() {
        return this.parentAccountInstance.getAyuFilterController();
    }

    protected final AyuFilterCacheController getAyuFilterCacheController() {
        return this.parentAccountInstance.getAyuFilterCacheController();
    }

    protected final AyuSpyController getAyuSpyController() {
        return this.parentAccountInstance.getAyuSpyController();
    }

    protected final AyuGhostController getAyuGhostController() {
        return this.parentAccountInstance.getAyuGhostController();
    }
}
