package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import androidx.collection.LongSparseArray;
import com.exteragram.messenger.ExteraConfig;
import com.google.android.material.timepicker.TimeModel;
import java.util.ArrayList;
import okhttp3.internal.url._UrlKt;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumUtilities;

class ChatsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private AccountInstance accountInstance;
    private int appWidgetId;
    private RectF bitmapRect;
    private boolean deleted;
    private Context mContext;
    private Paint roundPaint;
    private ArrayList<Long> dids = new ArrayList<>();
    private LongSparseArray dialogs = new LongSparseArray();
    private LongSparseArray messageObjects = new LongSparseArray();

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getViewTypeCount() {
        return 2;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDestroy() {
    }

    public ChatsRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        Theme.createDialogsResources(context);
        this.appWidgetId = intent.getIntExtra("appWidgetId", 0);
        SharedPreferences sharedPreferences = context.getSharedPreferences("shortcut_widget", 0);
        int i = sharedPreferences.getInt("account" + this.appWidgetId, -1);
        if (i >= 0) {
            this.accountInstance = AccountInstance.getInstance(i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("deleted");
        sb.append(this.appWidgetId);
        this.deleted = sharedPreferences.getBoolean(sb.toString(), false) || this.accountInstance == null;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onCreate() {
        ApplicationLoader.postInitApplication();
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public int getCount() {
        if (this.deleted) {
            return 1;
        }
        return this.dids.size() + 1;
    }

    /* JADX WARN: Code duplicated, block: B:108:0x0266  */
    /* JADX WARN: Code duplicated, block: B:110:0x0270  */
    /* JADX WARN: Code duplicated, block: B:111:0x0281  */
    /* JADX WARN: Code duplicated, block: B:114:0x02a3  */
    /* JADX WARN: Code duplicated, block: B:116:0x02a9  */
    /* JADX WARN: Code duplicated, block: B:121:0x02b6  */
    /* JADX WARN: Code duplicated, block: B:123:0x02c6  */
    /* JADX WARN: Code duplicated, block: B:190:0x0465  */
    /* JADX WARN: Code duplicated, block: B:192:0x046d  */
    /* JADX WARN: Code duplicated, block: B:197:0x047f  */
    /* JADX WARN: Code duplicated, block: B:199:0x0483  */
    /* JADX WARN: Code duplicated, block: B:204:0x0495  */
    /* JADX WARN: Code duplicated, block: B:206:0x0499  */
    /* JADX WARN: Code duplicated, block: B:208:0x049f  */
    /* JADX WARN: Code duplicated, block: B:209:0x04a2  */
    /* JADX WARN: Code duplicated, block: B:211:0x04a8  */
    /* JADX WARN: Code duplicated, block: B:212:0x04ab  */
    /* JADX WARN: Code duplicated, block: B:214:0x04b1  */
    /* JADX WARN: Code duplicated, block: B:215:0x04b4  */
    /* JADX WARN: Code duplicated, block: B:217:0x04ba  */
    /* JADX WARN: Code duplicated, block: B:218:0x04bd  */
    /* JADX WARN: Code duplicated, block: B:220:0x04d2  */
    /* JADX WARN: Code duplicated, block: B:222:0x04d6  */
    /* JADX WARN: Code duplicated, block: B:224:0x04f1  */
    /* JADX WARN: Code duplicated, block: B:226:0x04f5  */
    /* JADX WARN: Code duplicated, block: B:227:0x050f  */
    /* JADX WARN: Code duplicated, block: B:229:0x0515  */
    /* JADX WARN: Code duplicated, block: B:230:0x052f  */
    /* JADX WARN: Code duplicated, block: B:233:0x053d  */
    /* JADX WARN: Code duplicated, block: B:237:0x056c  */
    /* JADX WARN: Code duplicated, block: B:241:0x057d  */
    /* JADX WARN: Code duplicated, block: B:251:0x05d8  */
    /* JADX WARN: Code duplicated, block: B:254:0x05ed  */
    /* JADX WARN: Code duplicated, block: B:255:0x05f7  */
    /* JADX WARN: Code duplicated, block: B:258:0x061f  */
    /* JADX WARN: Code duplicated, block: B:259:0x0621  */
    /* JADX WARN: Instruction removed from duplicated block: B:222:0x04d6, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:226:0x04f5, please report this as an issue */
    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public RemoteViews getViewAt(int i) {
        TLRPC.Chat chat;
        String str;
        TLRPC.User user;
        TLRPC.FileLocation fileLocation;
        String monoForumTitle;
        TLRPC.FileLocation fileLocation2;
        TLRPC.ChatPhoto chatPhoto;
        int i2;
        Bitmap bitmapDecodeFile;
        MessageObject messageObject;
        TLRPC.Dialog dialog;
        int i3;
        int i4;
        Bundle bundle;
        int i5;
        int i6;
        long fromChatId;
        TLRPC.Chat chat2;
        TLRPC.User user2;
        int color;
        TLRPC.MessageMedia messageMedia;
        CharSequence charSequence;
        CharSequence charSequence2;
        String str2;
        String strReplace;
        SpannableStringBuilder spannableStringBuilderValueOf;
        char c;
        int i7;
        String string;
        SpannableStringBuilder spannableStringBuilder;
        String str3;
        CharSequence string2;
        CharSequence charSequence3;
        TLRPC.MessageAction messageAction;
        AvatarDrawable avatarDrawable;
        String name;
        TLRPC.UserProfilePhoto userProfilePhoto;
        TLRPC.FileLocation fileLocation3;
        if (this.deleted) {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_deleted);
            remoteViews.setTextViewText(R.id.widget_deleted_text, LocaleController.getString(R.string.WidgetLoggedOff));
            return remoteViews;
        }
        char c2 = 0;
        if (i >= this.dids.size()) {
            RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_edititem);
            remoteViews2.setTextViewText(R.id.widget_edititem_text, LocaleController.getString(R.string.TapToEditWidget));
            Bundle bundle2 = new Bundle();
            bundle2.putInt("appWidgetId", this.appWidgetId);
            bundle2.putInt("appWidgetType", 0);
            bundle2.putInt("currentAccount", this.accountInstance.getCurrentAccount());
            Intent intent = new Intent();
            intent.putExtras(bundle2);
            remoteViews2.setOnClickFillInIntent(R.id.widget_edititem, intent);
            return remoteViews2;
        }
        Long l = this.dids.get(i);
        boolean zIsUserDialog = DialogObject.isUserDialog(l.longValue());
        String str4 = _UrlKt.FRAGMENT_ENCODE_SET;
        if (zIsUserDialog) {
            user = this.accountInstance.getMessagesController().getUser(l);
            if (user != null) {
                if (UserObject.isUserSelf(user)) {
                    name = LocaleController.getString(R.string.SavedMessages);
                } else if (UserObject.isReplyUser(user)) {
                    name = LocaleController.getString(R.string.RepliesTitle);
                } else if (UserObject.isDeleted(user)) {
                    name = LocaleController.getString(R.string.HiddenName);
                } else {
                    name = ContactsController.formatName(user.first_name, user.last_name);
                }
                if (UserObject.isReplyUser(user) || UserObject.isUserSelf(user) || (userProfilePhoto = user.photo) == null || (fileLocation3 = userProfilePhoto.photo_small) == null || fileLocation3.volume_id == 0 || fileLocation3.local_id == 0) {
                    fileLocation = null;
                    str = name;
                    chat = null;
                } else {
                    fileLocation = fileLocation3;
                    str = name;
                    chat = null;
                }
            } else {
                str = _UrlKt.FRAGMENT_ENCODE_SET;
                chat = null;
                fileLocation = null;
            }
        } else {
            TLRPC.Chat chat3 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-l.longValue()));
            if (chat3 != null) {
                if (ChatObject.isMonoForum(chat3)) {
                    monoForumTitle = ForumUtilities.getMonoForumTitle(this.accountInstance.getCurrentAccount(), chat3);
                    TLRPC.Chat chat4 = this.accountInstance.getMessagesController().getChat(Long.valueOf(chat3.linked_monoforum_id));
                    if (chat4 == null || (chatPhoto = chat4.photo) == null || (fileLocation2 = chatPhoto.photo_small) == null || fileLocation2.volume_id == 0 || fileLocation2.local_id == 0) {
                        fileLocation2 = null;
                    }
                } else {
                    monoForumTitle = chat3.title;
                    TLRPC.ChatPhoto chatPhoto2 = chat3.photo;
                    if (chatPhoto2 == null || (fileLocation2 = chatPhoto2.photo_small) == null || fileLocation2.volume_id == 0 || fileLocation2.local_id == 0) {
                        fileLocation = null;
                        str = monoForumTitle;
                        chat = chat3;
                        user = null;
                    }
                }
                fileLocation = fileLocation2;
                str = monoForumTitle;
                chat = chat3;
                user = null;
            } else {
                chat = chat3;
                str = _UrlKt.FRAGMENT_ENCODE_SET;
                user = null;
                fileLocation = null;
            }
        }
        RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.shortcut_widget_item);
        remoteViews3.setTextViewText(R.id.shortcut_widget_item_text, str);
        if (fileLocation != null) {
            try {
                bitmapDecodeFile = BitmapFactory.decodeFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(fileLocation, true).toString());
            } catch (Throwable th) {
                th = th;
                i2 = 1;
                FileLog.e(th);
                messageObject = (MessageObject) this.messageObjects.get(l.longValue());
                dialog = (TLRPC.Dialog) this.dialogs.get(l.longValue());
                if (messageObject != null) {
                    fromChatId = messageObject.getFromChatId();
                    if (DialogObject.isUserDialog(fromChatId)) {
                        user2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                        chat2 = null;
                    } else {
                        chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                        user2 = null;
                    }
                    color = this.mContext.getResources().getColor(R.color.widget_text);
                    if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                        if (ChatObject.isChannel(chat)) {
                            messageAction = messageObject.messageOwner.action;
                            if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear)) {
                                charSequence3 = str4;
                                charSequence3 = str4;
                                charSequence3 = messageObject.messageText;
                            }
                        } else {
                            charSequence3 = str4;
                            charSequence3 = str4;
                            charSequence3 = messageObject.messageText;
                        }
                        charSequence3 = str4;
                        charSequence3 = str4;
                        charSequence3 = str4;
                        color = this.mContext.getResources().getColor(R.color.widget_action_text);
                        string2 = charSequence3;
                    } else if (chat == null) {
                        messageMedia = messageObject.messageOwner.media;
                        if (!(messageMedia instanceof TLRPC.TL_messageMediaPhoto)) {
                            if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                                if (messageObject.caption != null) {
                                    if (messageObject.isVideo()) {
                                        str2 = "📹 ";
                                    } else if (messageObject.isVoice()) {
                                        str2 = "🎤 ";
                                    } else if (messageObject.isMusic()) {
                                        str2 = "🎧 ";
                                    } else if (messageObject.isPhoto()) {
                                        str2 = "🖼 ";
                                    } else {
                                        str2 = "📎 ";
                                    }
                                    string2 = str2 + ((Object) messageObject.caption);
                                } else {
                                    if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                        charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                    } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                        charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                    } else if (messageObject.type == 14) {
                                        charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                    } else {
                                        charSequence = messageObject.messageText;
                                        AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                    }
                                    charSequence2 = charSequence;
                                    string2 = charSequence2;
                                    if (messageObject.messageOwner.media != null) {
                                        string2 = charSequence2;
                                        color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                        string2 = charSequence2;
                                    }
                                }
                            } else if (messageObject.caption != null) {
                                if (messageObject.isVideo()) {
                                    str2 = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str2 = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str2 = "🎧 ";
                                } else if (messageObject.isPhoto()) {
                                    str2 = "🖼 ";
                                } else {
                                    str2 = "📎 ";
                                }
                                string2 = str2 + ((Object) messageObject.caption);
                            } else {
                                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                    charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                    charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                } else if (messageObject.type == 14) {
                                    charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                } else {
                                    charSequence = messageObject.messageText;
                                    AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                }
                                charSequence2 = charSequence;
                                string2 = charSequence2;
                                if (messageObject.messageOwner.media != null) {
                                    string2 = charSequence2;
                                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                    string2 = charSequence2;
                                }
                            }
                        } else if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                            if (messageObject.caption != null) {
                                if (messageObject.isVideo()) {
                                    str2 = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str2 = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str2 = "🎧 ";
                                } else if (messageObject.isPhoto()) {
                                    str2 = "🖼 ";
                                } else {
                                    str2 = "📎 ";
                                }
                                string2 = str2 + ((Object) messageObject.caption);
                            } else {
                                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                    charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                    charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                } else if (messageObject.type == 14) {
                                    charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                } else {
                                    charSequence = messageObject.messageText;
                                    AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                }
                                charSequence2 = charSequence;
                                string2 = charSequence2;
                                if (messageObject.messageOwner.media != null) {
                                    string2 = charSequence2;
                                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                    string2 = charSequence2;
                                }
                            }
                        } else if (messageObject.caption != null) {
                            if (messageObject.isVideo()) {
                                str2 = "📹 ";
                            } else if (messageObject.isVoice()) {
                                str2 = "🎤 ";
                            } else if (messageObject.isMusic()) {
                                str2 = "🎧 ";
                            } else if (messageObject.isPhoto()) {
                                str2 = "🖼 ";
                            } else {
                                str2 = "📎 ";
                            }
                            string2 = str2 + ((Object) messageObject.caption);
                        } else {
                            if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                            } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                            } else if (messageObject.type == 14) {
                                charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                            } else {
                                charSequence = messageObject.messageText;
                                AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                            }
                            charSequence2 = charSequence;
                            string2 = charSequence2;
                            if (messageObject.messageOwner.media != null) {
                                string2 = charSequence2;
                                color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                string2 = charSequence2;
                            }
                        }
                    } else {
                        messageMedia = messageObject.messageOwner.media;
                        if (!(messageMedia instanceof TLRPC.TL_messageMediaPhoto)) {
                            if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                                if (messageObject.caption != null) {
                                    if (messageObject.isVideo()) {
                                        str2 = "📹 ";
                                    } else if (messageObject.isVoice()) {
                                        str2 = "🎤 ";
                                    } else if (messageObject.isMusic()) {
                                        str2 = "🎧 ";
                                    } else if (messageObject.isPhoto()) {
                                        str2 = "🖼 ";
                                    } else {
                                        str2 = "📎 ";
                                    }
                                    string2 = str2 + ((Object) messageObject.caption);
                                } else {
                                    if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                        charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                    } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                        charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                    } else if (messageObject.type == 14) {
                                        charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                    } else {
                                        charSequence = messageObject.messageText;
                                        AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                    }
                                    charSequence2 = charSequence;
                                    string2 = charSequence2;
                                    if (messageObject.messageOwner.media != null) {
                                        string2 = charSequence2;
                                        color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                        string2 = charSequence2;
                                    }
                                }
                            } else if (messageObject.caption != null) {
                                if (messageObject.isVideo()) {
                                    str2 = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str2 = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str2 = "🎧 ";
                                } else if (messageObject.isPhoto()) {
                                    str2 = "🖼 ";
                                } else {
                                    str2 = "📎 ";
                                }
                                string2 = str2 + ((Object) messageObject.caption);
                            } else {
                                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                    charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                    charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                } else if (messageObject.type == 14) {
                                    charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                } else {
                                    charSequence = messageObject.messageText;
                                    AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                }
                                charSequence2 = charSequence;
                                string2 = charSequence2;
                                if (messageObject.messageOwner.media != null) {
                                    string2 = charSequence2;
                                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                    string2 = charSequence2;
                                }
                            }
                        } else if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                            if (messageObject.caption != null) {
                                if (messageObject.isVideo()) {
                                    str2 = "📹 ";
                                } else if (messageObject.isVoice()) {
                                    str2 = "🎤 ";
                                } else if (messageObject.isMusic()) {
                                    str2 = "🎧 ";
                                } else if (messageObject.isPhoto()) {
                                    str2 = "🖼 ";
                                } else {
                                    str2 = "📎 ";
                                }
                                string2 = str2 + ((Object) messageObject.caption);
                            } else {
                                if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                    charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                    charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                } else if (messageObject.type == 14) {
                                    charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                } else {
                                    charSequence = messageObject.messageText;
                                    AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                }
                                charSequence2 = charSequence;
                                string2 = charSequence2;
                                if (messageObject.messageOwner.media != null) {
                                    string2 = charSequence2;
                                    color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                    string2 = charSequence2;
                                }
                            }
                        } else if (messageObject.caption != null) {
                            if (messageObject.isVideo()) {
                                str2 = "📹 ";
                            } else if (messageObject.isVoice()) {
                                str2 = "🎤 ";
                            } else if (messageObject.isMusic()) {
                                str2 = "🎧 ";
                            } else if (messageObject.isPhoto()) {
                                str2 = "🖼 ";
                            } else {
                                str2 = "📎 ";
                            }
                            string2 = str2 + ((Object) messageObject.caption);
                        } else {
                            if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                            } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                            } else if (messageObject.type == 14) {
                                charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                            } else {
                                charSequence = messageObject.messageText;
                                AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                            }
                            charSequence2 = charSequence;
                            string2 = charSequence2;
                            if (messageObject.messageOwner.media != null) {
                                string2 = charSequence2;
                                color = this.mContext.getResources().getColor(R.color.widget_action_text);
                                string2 = charSequence2;
                            }
                        }
                    }
                    string2 = charSequence2;
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, string2.toString());
                    remoteViews3.setTextColor(R.id.shortcut_widget_item_message, color);
                } else {
                    if (dialog == null) {
                        remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, _UrlKt.FRAGMENT_ENCODE_SET);
                    } else {
                        remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, _UrlKt.FRAGMENT_ENCODE_SET);
                    }
                    remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, _UrlKt.FRAGMENT_ENCODE_SET);
                }
                if (dialog == null) {
                    i4 = 0;
                    remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
                } else {
                    i4 = 0;
                    remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
                }
                bundle = new Bundle();
                if (DialogObject.isUserDialog(l.longValue())) {
                    bundle.putLong("userId", l.longValue());
                } else {
                    bundle.putLong("chatId", -l.longValue());
                }
                bundle.putInt("currentAccount", this.accountInstance.getCurrentAccount());
                Intent intent2 = new Intent();
                intent2.putExtras(bundle);
                remoteViews3.setOnClickFillInIntent(R.id.shortcut_widget_item, intent2);
                int i8 = R.id.shortcut_widget_item_divider;
                if (i == getCount()) {
                    i5 = 8;
                } else {
                    i5 = i4;
                }
                remoteViews3.setViewVisibility(i8, i5);
                return remoteViews3;
            }
        } else {
            bitmapDecodeFile = null;
        }
        int iDp = AndroidUtilities.dp(48.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iDp, iDp, Bitmap.Config.ARGB_8888);
        bitmapCreateBitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        if (bitmapDecodeFile == null) {
            if (user != null) {
                avatarDrawable = new AvatarDrawable(user);
                if (UserObject.isReplyUser(user)) {
                    avatarDrawable.setAvatarType(12);
                } else if (UserObject.isUserSelf(user)) {
                    avatarDrawable.setAvatarType(1);
                }
            } else {
                avatarDrawable = new AvatarDrawable();
                avatarDrawable.setInfo(this.accountInstance.getCurrentAccount(), chat);
            }
            avatarDrawable.setRoundRadius(ExteraConfig.getAvatarCorners(iDp, true, chat != null && chat.forum));
            avatarDrawable.setBounds(0, 0, iDp, iDp);
            avatarDrawable.draw(canvas);
            i2 = 1;
        } else {
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            BitmapShader bitmapShader = new BitmapShader(bitmapDecodeFile, tileMode, tileMode);
            if (this.roundPaint == null) {
                this.roundPaint = new Paint(1);
                this.bitmapRect = new RectF();
            }
            float width = iDp / bitmapDecodeFile.getWidth();
            canvas.save();
            canvas.scale(width, width);
            float avatarCorners = ExteraConfig.getAvatarCorners(bitmapDecodeFile.getWidth(), true, chat != null && chat.forum);
            this.roundPaint.setShader(bitmapShader);
            i2 = 1;
            try {
                this.bitmapRect.set(0.0f, 0.0f, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight());
                canvas.drawRoundRect(this.bitmapRect, avatarCorners, avatarCorners, this.roundPaint);
                canvas.restore();
            } catch (Throwable th2) {
                th = th2;
                FileLog.e(th);
            }
        }
        canvas.setBitmap(null);
        remoteViews3.setImageViewBitmap(R.id.shortcut_widget_item_avatar, bitmapCreateBitmap);
        messageObject = (MessageObject) this.messageObjects.get(l.longValue());
        dialog = (TLRPC.Dialog) this.dialogs.get(l.longValue());
        if (messageObject != null) {
            fromChatId = messageObject.getFromChatId();
            if (DialogObject.isUserDialog(fromChatId)) {
                user2 = this.accountInstance.getMessagesController().getUser(Long.valueOf(fromChatId));
                chat2 = null;
            } else {
                chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-fromChatId));
                user2 = null;
            }
            color = this.mContext.getResources().getColor(R.color.widget_text);
            if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                if (ChatObject.isChannel(chat)) {
                    messageAction = messageObject.messageOwner.action;
                    if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear) && !(messageAction instanceof TLRPC.TL_messageActionChannelMigrateFrom)) {
                        charSequence3 = str4;
                        charSequence3 = str4;
                        charSequence3 = messageObject.messageText;
                    }
                } else {
                    charSequence3 = str4;
                    charSequence3 = str4;
                    charSequence3 = messageObject.messageText;
                }
                charSequence3 = str4;
                charSequence3 = str4;
                charSequence3 = str4;
                color = this.mContext.getResources().getColor(R.color.widget_action_text);
                string2 = charSequence3;
            } else if (chat == null && chat2 == null && (!ChatObject.isChannel(chat) || ChatObject.isMegagroup(chat))) {
                if (messageObject.isOutOwner()) {
                    strReplace = LocaleController.getString(R.string.FromYou);
                } else if (user2 != null) {
                    strReplace = UserObject.getFirstName(user2).replace("\n", _UrlKt.FRAGMENT_ENCODE_SET);
                } else {
                    strReplace = "DELETED";
                }
                String str5 = strReplace;
                CharSequence charSequence4 = messageObject.caption;
                try {
                    if (charSequence4 != null) {
                        String string3 = charSequence4.toString();
                        if (string3.length() > 150) {
                            string3 = string3.substring(0, 150);
                        }
                        if (messageObject.isVideo()) {
                            str3 = "📹 ";
                        } else if (messageObject.isVoice()) {
                            str3 = "🎤 ";
                        } else if (messageObject.isMusic()) {
                            str3 = "🎧 ";
                        } else {
                            str3 = messageObject.isPhoto() ? "🖼 " : "📎 ";
                        }
                        Object[] objArr = new Object[2];
                        objArr[0] = str3 + string3.replace('\n', ' ');
                        objArr[i2] = str5;
                        spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                    } else {
                        if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                            color = this.mContext.getResources().getColor(R.color.widget_action_text);
                            TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
                            try {
                                if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                    Object[] objArr2 = new Object[i2];
                                    objArr2[0] = ((TLRPC.TL_messageMediaPoll) messageMedia2).poll.question.text;
                                    string = String.format("📊 \u2068%s\u2069", objArr2);
                                } else {
                                    int i9 = i2;
                                    if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                        Object[] objArr3 = new Object[i9];
                                        objArr3[0] = messageMedia2.game.title;
                                        string = String.format("🎮 \u2068%s\u2069", objArr3);
                                        i2 = i9;
                                    } else if (messageObject.type == 14) {
                                        String musicAuthor = messageObject.getMusicAuthor();
                                        String musicTitle = messageObject.getMusicTitle();
                                        c2 = 0;
                                        i2 = i9;
                                        i7 = 2;
                                        Object[] objArr4 = new Object[2];
                                        objArr4[0] = musicAuthor;
                                        objArr4[i2] = musicTitle;
                                        string = String.format("🎧 \u2068%s - %s\u2069", objArr4);
                                    } else {
                                        c2 = 0;
                                        i2 = i9;
                                        i7 = 2;
                                        string = messageObject.messageText.toString();
                                    }
                                    Object[] objArr5 = new Object[i7];
                                    objArr5[c2] = string.replace('\n', ' ');
                                    objArr5[i2] = str5;
                                    SpannableStringBuilder spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr5));
                                    spannableStringBuilderValueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str5.length() + 2, spannableStringBuilderValueOf2.length(), 33);
                                    spannableStringBuilder = spannableStringBuilderValueOf2;
                                }
                                spannableStringBuilderValueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str5.length() + 2, spannableStringBuilderValueOf2.length(), 33);
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            i7 = 2;
                            Object[] objArr6 = new Object[i7];
                            objArr6[c2] = string.replace('\n', ' ');
                            objArr6[i2] = str5;
                            SpannableStringBuilder spannableStringBuilderValueOf3 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr6));
                            spannableStringBuilder = spannableStringBuilderValueOf3;
                        } else {
                            String strSubstring = messageObject.messageOwner.message;
                            if (strSubstring != null) {
                                if (strSubstring.length() > 150) {
                                    c = 0;
                                    strSubstring = strSubstring.substring(0, 150);
                                } else {
                                    c = 0;
                                }
                                Object[] objArr7 = new Object[2];
                                objArr7[c] = strSubstring.replace('\n', ' ').trim();
                                objArr7[1] = str5;
                                spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr7));
                            } else {
                                spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(_UrlKt.FRAGMENT_ENCODE_SET);
                            }
                        }
                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str5.length() + 1, 33);
                        string2 = spannableStringBuilder;
                    }
                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str5.length() + 1, 33);
                    string2 = spannableStringBuilder;
                } catch (Exception e2) {
                    FileLog.e(e2);
                    string2 = spannableStringBuilder;
                }
                spannableStringBuilder = spannableStringBuilderValueOf;
            } else {
                messageMedia = messageObject.messageOwner.media;
                if (!(messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (messageMedia.photo instanceof TLRPC.TL_photoEmpty) && messageMedia.ttl_seconds != 0) {
                    string2 = LocaleController.getString(R.string.AttachPhotoExpired);
                } else if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument) && (messageMedia.document instanceof TLRPC.TL_documentEmpty) && messageMedia.ttl_seconds != 0) {
                    string2 = LocaleController.getString(R.string.AttachVideoExpired);
                } else if (messageObject.caption != null) {
                    if (messageObject.isVideo()) {
                        str2 = "📹 ";
                    } else if (messageObject.isVoice()) {
                        str2 = "🎤 ";
                    } else if (messageObject.isMusic()) {
                        str2 = "🎧 ";
                    } else if (messageObject.isPhoto()) {
                        str2 = "🖼 ";
                    } else {
                        str2 = "📎 ";
                    }
                    string2 = str2 + ((Object) messageObject.caption);
                } else {
                    if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                        charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                    } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                        charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                    } else if (messageObject.type == 14) {
                        charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                    } else {
                        charSequence = messageObject.messageText;
                        AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                    }
                    charSequence2 = charSequence;
                    string2 = charSequence2;
                    if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                        string2 = charSequence2;
                        color = this.mContext.getResources().getColor(R.color.widget_action_text);
                        string2 = charSequence2;
                    }
                }
            }
            string2 = charSequence2;
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, string2.toString());
            remoteViews3.setTextColor(R.id.shortcut_widget_item_message, color);
        } else {
            if (dialog == null && (i3 = dialog.last_message_date) != 0) {
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, LocaleController.stringForMessageListDate(i3));
            } else {
                remoteViews3.setTextViewText(R.id.shortcut_widget_item_time, _UrlKt.FRAGMENT_ENCODE_SET);
            }
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_message, _UrlKt.FRAGMENT_ENCODE_SET);
        }
        if (dialog == null && (i6 = dialog.unread_count) > 0) {
            i4 = 0;
            remoteViews3.setTextViewText(R.id.shortcut_widget_item_badge, String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i6)));
            remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 0);
            if (this.accountInstance.getMessagesController().isDialogMuted(dialog.id, 0L)) {
                remoteViews3.setBoolean(R.id.shortcut_widget_item_badge, "setEnabled", false);
                remoteViews3.setInt(R.id.shortcut_widget_item_badge, "setBackgroundResource", R.drawable.widget_badge_muted_background);
            } else {
                remoteViews3.setBoolean(R.id.shortcut_widget_item_badge, "setEnabled", true);
                remoteViews3.setInt(R.id.shortcut_widget_item_badge, "setBackgroundResource", R.drawable.widget_badge_background);
            }
        } else {
            i4 = 0;
            remoteViews3.setViewVisibility(R.id.shortcut_widget_item_badge, 8);
        }
        bundle = new Bundle();
        if (DialogObject.isUserDialog(l.longValue())) {
            bundle.putLong("userId", l.longValue());
        } else {
            bundle.putLong("chatId", -l.longValue());
        }
        bundle.putInt("currentAccount", this.accountInstance.getCurrentAccount());
        Intent intent3 = new Intent();
        intent3.putExtras(bundle);
        remoteViews3.setOnClickFillInIntent(R.id.shortcut_widget_item, intent3);
        int i10 = R.id.shortcut_widget_item_divider;
        if (i == getCount()) {
            i5 = 8;
        } else {
            i5 = i4;
        }
        remoteViews3.setViewVisibility(i10, i5);
        return remoteViews3;
    }

    @Override // android.widget.RemoteViewsService.RemoteViewsFactory
    public void onDataSetChanged() {
        this.dids.clear();
        this.messageObjects.clear();
        AccountInstance accountInstance = this.accountInstance;
        if (accountInstance == null || !accountInstance.getUserConfig().isClientActivated()) {
            return;
        }
        ArrayList<TLRPC.User> arrayList = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList2 = new ArrayList<>();
        LongSparseArray longSparseArray = new LongSparseArray();
        this.accountInstance.getMessagesStorage().getWidgetDialogs(this.appWidgetId, 0, this.dids, this.dialogs, longSparseArray, arrayList, arrayList2);
        this.accountInstance.getMessagesController().putUsers(arrayList, true);
        this.accountInstance.getMessagesController().putChats(arrayList2, true);
        this.messageObjects.clear();
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            this.messageObjects.put(longSparseArray.keyAt(i), new MessageObject(this.accountInstance.getCurrentAccount(), (TLRPC.Message) longSparseArray.valueAt(i), (LongSparseArray) null, (LongSparseArray) null, false, true));
        }
    }
}
