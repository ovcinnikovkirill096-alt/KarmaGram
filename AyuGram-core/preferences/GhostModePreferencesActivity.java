package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.ui.PopupUtils;
import com.radolyn.ayugram.AyuGhostConfig;
import com.radolyn.ayugram.AyuState;
import com.radolyn.ayugram.preferences.components.AccountCell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

public class GhostModePreferencesActivity extends BasePreferencesActivity {
    private AyuGhostConfig.GhostModeSettings currentSettings;
    private boolean ghostModeMenuExpanded;
    private CharSequence[] sendWithoutSoundItems;
    private ActionBarMenuItem switchItem;

    private enum ItemId {
        HEADER,
        GHOST_MODE,
        DONT_READ_MESSAGES,
        DONT_READ_STORIES,
        DONT_SEND_ONLINE,
        DONT_SEND_UPLOAD,
        SEND_OFFLINE_AFTER_ONLINE,
        GHOST_LONG_TAP_DESC,
        MARK_READ_AFTER_ACTION,
        MARK_READ_DESC,
        USE_SCHEDULED,
        USE_SCHEDULED_DESC,
        SEND_WITHOUT_SOUND,
        SEND_WITHOUT_SOUND_DESC,
        SUGGEST_BEFORE_STORY,
        SUGGEST_BEFORE_STORY_DESC;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void initializeOptionStrings() {
        this.sendWithoutSoundItems = new CharSequence[]{LocaleController.getString(R.string.SendWithoutSoundByDefaultNever), LocaleController.getString(R.string.SendWithoutSoundByDefaultInGhostMode), LocaleController.getString(R.string.SendWithoutSoundByDefaultAlways)};
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        Drawable avatarDrawable;
        TLRPC.UserProfilePhoto userProfilePhoto;
        float f;
        boolean z;
        if (UserConfig.getActivatedAccountsCount() == 1 && !AyuGhostConfig.isGlobalOverride()) {
            AyuGhostConfig.GhostModeSettings ghostModeSettings = AyuGhostConfig.getGhostModeSettings(getUserConfig().getClientUserId());
            ghostModeSettings.userId = -1L;
            ghostModeSettings.save();
            AyuGhostConfig.setGlobalOverride(true, BulletinFactory.of(this));
            AyuGhostConfig.reloadConfig();
        }
        this.currentSettings = AyuGhostConfig.getGhostModeSettings(getUserConfig().getClientUserId());
        View viewCreateView = super.createView(context);
        this.switchItem = this.actionBar.menu.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0f));
        final AvatarDrawable avatarDrawable2 = new AvatarDrawable();
        avatarDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
        final BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(ExteraConfig.getAvatarCorners(36.0f));
        this.switchItem.addView(backupImageView, LayoutHelper.createFrame(36, 36, 17));
        final HashMap map = new HashMap();
        final AccountCell accountCell = new AccountCell(context);
        accountCell.setSelected(AyuGhostConfig.isGlobalOverride());
        float f2 = 268.0f;
        this.switchItem.addSubItem(10, accountCell, AndroidUtilities.dp(268.0f), AndroidUtilities.dp(48.0f));
        map.put(10L, accountCell);
        int i = 0;
        while (i < 16) {
            TLRPC.User currentUser = AccountInstance.getInstance(i).getUserConfig().getCurrentUser();
            if (currentUser != null) {
                AccountSelectCell accountSelectCell = new AccountSelectCell(context, false);
                accountSelectCell.setAccount(i, true);
                if (AyuGhostConfig.isGlobalOverride()) {
                    f = f2;
                } else {
                    f = f2;
                    if (currentUser.id == this.currentSettings.userId) {
                        z = true;
                    }
                    accountSelectCell.setSelected(z);
                    int i2 = i + 20;
                    this.switchItem.addSubItem(i2, accountSelectCell, AndroidUtilities.dp(f), AndroidUtilities.dp(48.0f));
                    map.put(Long.valueOf(i2), accountSelectCell);
                }
                z = false;
                accountSelectCell.setSelected(z);
                int i3 = i + 20;
                this.switchItem.addSubItem(i3, accountSelectCell, AndroidUtilities.dp(f), AndroidUtilities.dp(48.0f));
                map.put(Long.valueOf(i3), accountSelectCell);
            } else {
                f = f2;
            }
            i++;
            f2 = f;
        }
        final TLRPC.User currentUser2 = getUserConfig().getCurrentUser();
        avatarDrawable2.setInfo(this.currentAccount, currentUser2);
        backupImageView.getImageReceiver().setCurrentAccount(this.currentAccount);
        if (currentUser2 == null || (userProfilePhoto = currentUser2.photo) == null || (avatarDrawable = userProfilePhoto.strippedBitmap) == null) {
            avatarDrawable = avatarDrawable2;
        }
        if (AyuGhostConfig.isGlobalOverride()) {
            avatarDrawable = accountCell.getAvatarDrawable();
        }
        updateImage(backupImageView, currentUser2, avatarDrawable);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: com.radolyn.ayugram.preferences.GhostModePreferencesActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                Drawable drawable;
                if (i4 == -1) {
                    GhostModePreferencesActivity.this.finishFragment();
                } else if (i4 == 10) {
                    AyuGhostConfig.setGlobalOverride(true);
                    GhostModePreferencesActivity.this.currentSettings = AyuGhostConfig.getGhostModeSettings(-1L);
                    GhostModePreferencesActivity.this.updateImage(backupImageView, currentUser2, accountCell.getAvatarDrawable());
                } else {
                    AyuGhostConfig.setGlobalOverride(false);
                    int i5 = i4 - 20;
                    TLRPC.User currentUser3 = UserConfig.getInstance(i5).getCurrentUser();
                    if (currentUser3 != null) {
                        GhostModePreferencesActivity.this.currentSettings = AyuGhostConfig.getGhostModeSettings(currentUser3.id);
                        avatarDrawable2.setInfo(i5, currentUser3);
                        TLRPC.UserProfilePhoto userProfilePhoto2 = currentUser3.photo;
                        if (userProfilePhoto2 == null || (drawable = userProfilePhoto2.strippedBitmap) == null) {
                            drawable = avatarDrawable2;
                        }
                        GhostModePreferencesActivity.this.updateImage(backupImageView, currentUser3, drawable);
                    }
                }
                for (Map.Entry entry : map.entrySet()) {
                    ((FrameLayout) entry.getValue()).setSelected(((Long) entry.getKey()).longValue() == ((long) i4));
                }
                ((BasePreferencesActivity) GhostModePreferencesActivity.this).listView.adapter.update(true);
                NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
            }
        });
        if (UserConfig.getActivatedAccountsCount() == 1) {
            this.switchItem.setVisibility(8);
        }
        return viewCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImage(BackupImageView backupImageView, TLRPC.User user, Drawable drawable) {
        if (AyuGhostConfig.isGlobalOverride()) {
            backupImageView.setImageDrawable(drawable);
        } else {
            backupImageView.setImage(ImageLocation.getForUserOrChat(user, 1), "50_50", ImageLocation.getForUserOrChat(user, 2), "50_50", drawable, user);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.GhostEssentialsHeader)));
        arrayList.add(UItem.asExteraExpandableSwitch(ItemId.GHOST_MODE.getId(), LocaleController.getString(R.string.GhostModeToggle), String.format(Locale.US, "%d/5", Integer.valueOf(getGhostModeSelectedCount())), new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.GhostModePreferencesActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$fillItems$0(view);
            }
        }).setChecked(AyuGhostConfig.isGhostModeActive(this.currentSettings.userId)).setCollapsed(!this.ghostModeMenuExpanded).setSearchable(this).setLinkAlias("ghostModeToggle", this));
        if (this.ghostModeMenuExpanded) {
            arrayList.add(UItem.asRoundCheckbox(ItemId.DONT_READ_MESSAGES.getId(), LocaleController.getString(R.string.DontReadMessages)).setChecked(!this.currentSettings.sendReadMessagePackets).setEnabled(!this.currentSettings.sendReadMessagePacketsLocked).pad());
            arrayList.add(UItem.asRoundCheckbox(ItemId.DONT_READ_STORIES.getId(), LocaleController.getString(R.string.DontReadStories)).setChecked(!this.currentSettings.sendReadStoryPackets).setEnabled(!this.currentSettings.sendReadStoryPacketsLocked).pad());
            arrayList.add(UItem.asRoundCheckbox(ItemId.DONT_SEND_ONLINE.getId(), LocaleController.getString(R.string.DontSendOnlinePackets)).setChecked(!this.currentSettings.sendOnlinePackets).setEnabled(!this.currentSettings.sendOnlinePacketsLocked).pad());
            arrayList.add(UItem.asRoundCheckbox(ItemId.DONT_SEND_UPLOAD.getId(), LocaleController.getString(R.string.DontSendUploadProgress)).setChecked(!this.currentSettings.sendUploadProgress).setEnabled(!this.currentSettings.sendUploadProgressLocked).pad());
            arrayList.add(UItem.asRoundCheckbox(ItemId.SEND_OFFLINE_AFTER_ONLINE.getId(), LocaleController.getString(R.string.SendOfflinePacketAfterOnline)).setChecked(this.currentSettings.sendOfflinePacketAfterOnline).setEnabled(!this.currentSettings.sendOfflinePacketAfterOnlineLocked).pad());
            arrayList.add(UItem.asShadow(LocaleController.getString(R.string.GhostModeOptionLongTapDescription)));
        }
        arrayList.add(UItem.asCheck(ItemId.MARK_READ_AFTER_ACTION.getId(), LocaleController.getString(R.string.MarkReadAfterAction)).setChecked(this.currentSettings.markReadAfterAction).setSearchable(this).setLinkAlias("markReadAfterAction", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.MarkReadAfterActionDescription)));
        arrayList.add(UItem.asCheck(ItemId.USE_SCHEDULED.getId(), LocaleController.getString(R.string.UseScheduledMessages)).setChecked(this.currentSettings.useScheduledMessages).setSearchable(this).setLinkAlias("useScheduledMessages", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.UseScheduledMessagesDescription)));
        arrayList.add(UItem.asButton(ItemId.SEND_WITHOUT_SOUND.getId(), LocaleController.getString(R.string.SendWithoutSoundByDefault), this.sendWithoutSoundItems[this.currentSettings.sendWithoutSound]).setSearchable(this).setLinkAlias("sendWithoutSound", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.SendWithoutSoundByDefaultDescription)));
        arrayList.add(UItem.asCheck(ItemId.SUGGEST_BEFORE_STORY.getId(), LocaleController.getString(R.string.SuggestGhostModeBeforeViewingStory)).setChecked(this.currentSettings.suggestGhostModeBeforeViewingStory).setSearchable(this).setLinkAlias("suggestGhostModeBeforeViewingStory", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.SuggestGhostModeBeforeViewingStoryDescription)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$0(View view) {
        AyuGhostConfig.toggleGhostMode(this.currentSettings.userId, BulletinFactory.of(this));
        this.listView.adapter.update(true);
    }

    private int getGhostModeSelectedCount() {
        AyuGhostConfig.GhostModeSettings ghostModeSettings = this.currentSettings;
        int i = !ghostModeSettings.sendReadMessagePackets ? 1 : 0;
        if (!ghostModeSettings.sendReadStoryPackets) {
            i++;
        }
        if (!ghostModeSettings.sendOnlinePackets) {
            i++;
        }
        if (!ghostModeSettings.sendUploadProgress) {
            i++;
        }
        return ghostModeSettings.sendOfflinePacketAfterOnline ? i + 1 : i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [boolean, int] */
    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        int i2;
        GhostModePreferencesActivity ghostModePreferencesActivity;
        if (uItem == null) {
            return false;
        }
        int i3 = uItem.id;
        ItemId itemId = ItemId.DONT_READ_MESSAGES;
        if (i3 >= itemId.getId()) {
            int i4 = uItem.id;
            ItemId itemId2 = ItemId.SEND_OFFLINE_AFTER_ONLINE;
            if (i4 <= itemId2.getId()) {
                AyuGhostConfig.GhostModeSettings ghostModeSettings = this.currentSettings;
                ?? r4 = ghostModeSettings.sendReadMessagePacketsLocked;
                if (ghostModeSettings.sendReadStoryPacketsLocked) {
                    i2 = r4;
                    i2 = r4 + 1;
                }
                i2 = r4;
                int i5 = i2;
                if (ghostModeSettings.sendOnlinePacketsLocked) {
                    i5 = i2 + 1;
                }
                int i6 = i5;
                if (ghostModeSettings.sendUploadProgressLocked) {
                    i6 = i5 + 1;
                }
                int i7 = i6;
                if (ghostModeSettings.sendOfflinePacketAfterOnlineLocked) {
                    i7 = i6 + 1;
                }
                boolean z = (uItem.id == itemId.getId() && !this.currentSettings.sendReadMessagePacketsLocked) || (uItem.id == ItemId.DONT_READ_STORIES.getId() && !this.currentSettings.sendReadStoryPacketsLocked) || ((uItem.id == ItemId.DONT_SEND_ONLINE.getId() && !this.currentSettings.sendOnlinePacketsLocked) || ((uItem.id == ItemId.DONT_SEND_UPLOAD.getId() && !this.currentSettings.sendUploadProgressLocked) || (uItem.id == itemId2.getId() && !this.currentSettings.sendOfflinePacketAfterOnlineLocked)));
                if (i7 == 4 && z) {
                    BotWebViewVibrationEffect.NOTIFICATION_WARNING.vibrate();
                    return false;
                }
                int i8 = AnonymousClass2.$SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.values()[uItem.id - 1].ordinal()];
                if (i8 == 1) {
                    ghostModePreferencesActivity = this;
                    AyuGhostConfig.GhostModeSettings ghostModeSettings2 = ghostModePreferencesActivity.currentSettings;
                    ghostModeSettings2.sendReadMessagePacketsLocked = !ghostModeSettings2.sendReadMessagePacketsLocked;
                } else if (i8 == 2) {
                    ghostModePreferencesActivity = this;
                    AyuGhostConfig.GhostModeSettings ghostModeSettings3 = ghostModePreferencesActivity.currentSettings;
                    ghostModeSettings3.sendReadStoryPacketsLocked = !ghostModeSettings3.sendReadStoryPacketsLocked;
                } else if (i8 == 3) {
                    ghostModePreferencesActivity = this;
                    AyuGhostConfig.GhostModeSettings ghostModeSettings4 = ghostModePreferencesActivity.currentSettings;
                    ghostModeSettings4.sendOnlinePacketsLocked = !ghostModeSettings4.sendOnlinePacketsLocked;
                } else if (i8 == 4) {
                    ghostModePreferencesActivity = this;
                    AyuGhostConfig.GhostModeSettings ghostModeSettings5 = ghostModePreferencesActivity.currentSettings;
                    ghostModeSettings5.sendUploadProgressLocked = !ghostModeSettings5.sendUploadProgressLocked;
                } else if (i8 == 5) {
                    ghostModePreferencesActivity = this;
                    AyuGhostConfig.GhostModeSettings ghostModeSettings6 = ghostModePreferencesActivity.currentSettings;
                    ghostModeSettings6.sendOfflinePacketAfterOnlineLocked = !ghostModeSettings6.sendOfflinePacketAfterOnlineLocked;
                } else {
                    return super.onLongClick(uItem, view, i, f, f2);
                }
                ghostModePreferencesActivity.currentSettings.save();
                ghostModePreferencesActivity.listView.adapter.update(true);
                return true;
            }
        }
        return super.onLongClick(uItem, view, i, f, f2);
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.GhostModePreferencesActivity$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId;

        static {
            int[] iArr = new int[ItemId.values().length];
            $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId = iArr;
            try {
                iArr[ItemId.DONT_READ_MESSAGES.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.DONT_READ_STORIES.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.DONT_SEND_ONLINE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.DONT_SEND_UPLOAD.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.SEND_OFFLINE_AFTER_ONLINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.GHOST_MODE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.MARK_READ_AFTER_ACTION.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.USE_SCHEDULED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.SEND_WITHOUT_SOUND.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.SUGGEST_BEFORE_STORY.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > ItemId.values().length) {
            return;
        }
        switch (AnonymousClass2.$SwitchMap$com$radolyn$ayugram$preferences$GhostModePreferencesActivity$ItemId[ItemId.values()[uItem.id - 1].ordinal()]) {
            case 1:
                AyuGhostConfig.GhostModeSettings ghostModeSettings = this.currentSettings;
                if (!ghostModeSettings.sendReadMessagePacketsLocked) {
                    ghostModeSettings.sendReadMessagePackets = !ghostModeSettings.sendReadMessagePackets;
                    ghostModeSettings.save();
                    this.listView.adapter.update(true);
                    break;
                }
                break;
            case 2:
                AyuGhostConfig.GhostModeSettings ghostModeSettings2 = this.currentSettings;
                if (!ghostModeSettings2.sendReadStoryPacketsLocked) {
                    ghostModeSettings2.sendReadStoryPackets = !ghostModeSettings2.sendReadStoryPackets;
                    ghostModeSettings2.save();
                    this.listView.adapter.update(true);
                    break;
                }
                break;
            case 3:
                AyuGhostConfig.GhostModeSettings ghostModeSettings3 = this.currentSettings;
                if (!ghostModeSettings3.sendOnlinePacketsLocked) {
                    ghostModeSettings3.sendOnlinePackets = !ghostModeSettings3.sendOnlinePackets;
                    ghostModeSettings3.save();
                    this.listView.adapter.update(true);
                    break;
                }
                break;
            case 4:
                AyuGhostConfig.GhostModeSettings ghostModeSettings4 = this.currentSettings;
                if (!ghostModeSettings4.sendUploadProgressLocked) {
                    ghostModeSettings4.sendUploadProgress = !ghostModeSettings4.sendUploadProgress;
                    ghostModeSettings4.save();
                    this.listView.adapter.update(true);
                    break;
                }
                break;
            case 5:
                AyuGhostConfig.GhostModeSettings ghostModeSettings5 = this.currentSettings;
                if (!ghostModeSettings5.sendOfflinePacketAfterOnlineLocked) {
                    ghostModeSettings5.sendOfflinePacketAfterOnline = !ghostModeSettings5.sendOfflinePacketAfterOnline;
                    ghostModeSettings5.save();
                    this.listView.adapter.update(true);
                    break;
                }
                break;
            case 6:
                this.ghostModeMenuExpanded = !this.ghostModeMenuExpanded;
                this.listView.adapter.update(true);
                break;
            case 7:
                AyuGhostConfig.GhostModeSettings ghostModeSettings6 = this.currentSettings;
                ghostModeSettings6.markReadAfterAction = !ghostModeSettings6.markReadAfterAction;
                ghostModeSettings6.save();
                AyuGhostConfig.GhostModeSettings ghostModeSettings7 = this.currentSettings;
                if (ghostModeSettings7.markReadAfterAction && ghostModeSettings7.useScheduledMessages) {
                    ghostModeSettings7.useScheduledMessages = false;
                    ghostModeSettings7.save();
                }
                this.listView.adapter.update(true);
                break;
            case 8:
                AyuGhostConfig.GhostModeSettings ghostModeSettings8 = this.currentSettings;
                ghostModeSettings8.useScheduledMessages = !ghostModeSettings8.useScheduledMessages;
                ghostModeSettings8.save();
                AyuState.setAutomaticallyScheduled(false, -1);
                AyuGhostConfig.GhostModeSettings ghostModeSettings9 = this.currentSettings;
                if (ghostModeSettings9.useScheduledMessages && ghostModeSettings9.markReadAfterAction) {
                    ghostModeSettings9.markReadAfterAction = false;
                    ghostModeSettings9.save();
                }
                this.listView.adapter.update(true);
                break;
            case 9:
                if (getParentActivity() != null) {
                    showListDialog(uItem, this.sendWithoutSoundItems, LocaleController.getString(R.string.SendWithoutSoundByDefault), this.currentSettings.sendWithoutSound, new PopupUtils.OnItemClickListener() { // from class: com.radolyn.ayugram.preferences.GhostModePreferencesActivity$$ExternalSyntheticLambda0
                        @Override // com.exteragram.messenger.utils.ui.PopupUtils.OnItemClickListener
                        public final void onClick(int i3) {
                            this.f$0.lambda$onClick$1(i3);
                        }
                    });
                }
                break;
            case 10:
                AyuGhostConfig.GhostModeSettings ghostModeSettings10 = this.currentSettings;
                ghostModeSettings10.suggestGhostModeBeforeViewingStory = !ghostModeSettings10.suggestGhostModeBeforeViewingStory;
                ghostModeSettings10.save();
                this.listView.adapter.update(true);
                break;
            default:
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$1(int i) {
        AyuGhostConfig.GhostModeSettings ghostModeSettings = this.currentSettings;
        if (ghostModeSettings.sendWithoutSound == i) {
            return;
        }
        ghostModeSettings.sendWithoutSound = i;
        ghostModeSettings.save();
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.CategoryGhostMode);
    }
}
