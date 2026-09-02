package org.telegram.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.dx.io.Opcodes;
import com.exteragram.messenger.ExteraConfig;
import com.google.android.material.timepicker.TimeModel;
import java.util.ArrayList;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatsWidgetProvider;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsWidgetProvider;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.InviteMembersBottomSheet;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RecyclerListView;

public class EditWidgetActivity extends BaseFragment {
    private int chatsEndRow;
    private int chatsStartRow;
    private int currentWidgetId;
    private EditWidgetActivityDelegate delegate;
    private int infoRow;
    private ItemTouchHelper itemTouchHelper;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ImageView previewImageView;
    private int previewRow;
    private int rowCount;
    private int selectChatsRow;
    private ArrayList selectedDialogs = new ArrayList();
    private WidgetPreviewCell widgetPreviewCell;
    private int widgetType;

    public interface EditWidgetActivityDelegate {
        void didSelectDialogs(ArrayList arrayList);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        private boolean moved;

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 3) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            int adapterPosition = viewHolder.getAdapterPosition();
            int adapterPosition2 = viewHolder2.getAdapterPosition();
            if (EditWidgetActivity.this.listAdapter.swapElements(adapterPosition, adapterPosition2)) {
                ((GroupCreateUserCell) viewHolder.itemView).setDrawDivider(adapterPosition2 != EditWidgetActivity.this.chatsEndRow - 1);
                ((GroupCreateUserCell) viewHolder2.itemView).setDrawDivider(adapterPosition != EditWidgetActivity.this.chatsEndRow - 1);
                this.moved = true;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                EditWidgetActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            } else if (this.moved) {
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
                this.moved = false;
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    public class WidgetPreviewCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
        private RectF bitmapRect;
        private ViewGroup[] cells;
        private Drawable oldBackgroundDrawable;
        private BackgroundGradientDrawable.Disposable oldBackgroundGradientDisposable;
        private Paint roundPaint;
        private Drawable shadowDrawable;

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchSetPressed(boolean z) {
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public WidgetPreviewCell(Context context) {
            super(context);
            this.roundPaint = new Paint(1);
            this.bitmapRect = new RectF();
            this.cells = new ViewGroup[2];
            int i = 0;
            setWillNotDraw(false);
            setPadding(0, AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
            ChatActionCell chatActionCell = new ChatActionCell(context);
            chatActionCell.setCustomText(LocaleController.getString(R.string.WidgetPreview));
            linearLayout.addView(chatActionCell, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 4));
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            linearLayout2.setBackgroundResource(R.drawable.widget_bg);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 17, 10, 0, 10, 0));
            EditWidgetActivity.this.previewImageView = new ImageView(context);
            if (EditWidgetActivity.this.widgetType == 0) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.shortcut_widget_item, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear(-1, -2));
                    i++;
                }
                linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear(Opcodes.MUL_INT_LIT8, 160, 17));
                EditWidgetActivity.this.previewImageView.setImageResource(R.drawable.chats_widget_preview);
            } else if (EditWidgetActivity.this.widgetType == 1) {
                while (i < 2) {
                    this.cells[i] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.contacts_widget_item, (ViewGroup) null);
                    linearLayout2.addView(this.cells[i], LayoutHelper.createLinear(160, -2));
                    i++;
                }
                linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear(160, 160, 17));
                EditWidgetActivity.this.previewImageView.setImageResource(R.drawable.contacts_widget_preview);
            }
            updateDialogs();
            this.shadowDrawable = Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
        }

        /* JADX WARN: Code duplicated, block: B:106:0x0243  */
        /* JADX WARN: Code duplicated, block: B:108:0x0246  */
        /* JADX WARN: Code duplicated, block: B:110:0x024e  */
        /* JADX WARN: Code duplicated, block: B:111:0x025f  */
        /* JADX WARN: Code duplicated, block: B:114:0x0283  */
        /* JADX WARN: Code duplicated, block: B:116:0x0289  */
        /* JADX WARN: Code duplicated, block: B:121:0x0296  */
        /* JADX WARN: Code duplicated, block: B:123:0x02a8  */
        /* JADX WARN: Code duplicated, block: B:125:0x02b4  */
        /* JADX WARN: Code duplicated, block: B:190:0x043c  */
        /* JADX WARN: Code duplicated, block: B:192:0x0444  */
        /* JADX WARN: Code duplicated, block: B:197:0x0456  */
        /* JADX WARN: Code duplicated, block: B:199:0x045a  */
        /* JADX WARN: Code duplicated, block: B:204:0x046c  */
        /* JADX WARN: Code duplicated, block: B:206:0x0470  */
        /* JADX WARN: Code duplicated, block: B:208:0x0476  */
        /* JADX WARN: Code duplicated, block: B:209:0x0479  */
        /* JADX WARN: Code duplicated, block: B:20:0x008a  */
        /* JADX WARN: Code duplicated, block: B:211:0x047f  */
        /* JADX WARN: Code duplicated, block: B:212:0x0482  */
        /* JADX WARN: Code duplicated, block: B:214:0x0488  */
        /* JADX WARN: Code duplicated, block: B:215:0x048b  */
        /* JADX WARN: Code duplicated, block: B:217:0x0491  */
        /* JADX WARN: Code duplicated, block: B:219:0x04a6  */
        /* JADX WARN: Code duplicated, block: B:21:0x0095  */
        /* JADX WARN: Code duplicated, block: B:221:0x04aa  */
        /* JADX WARN: Code duplicated, block: B:223:0x04c5  */
        /* JADX WARN: Code duplicated, block: B:225:0x04c9  */
        /* JADX WARN: Code duplicated, block: B:226:0x04e3  */
        /* JADX WARN: Code duplicated, block: B:228:0x04e9  */
        /* JADX WARN: Code duplicated, block: B:229:0x0501  */
        /* JADX WARN: Code duplicated, block: B:232:0x0510  */
        /* JADX WARN: Code duplicated, block: B:236:0x055f  */
        /* JADX WARN: Code duplicated, block: B:238:0x0563  */
        /* JADX WARN: Code duplicated, block: B:239:0x057a  */
        /* JADX WARN: Code duplicated, block: B:23:0x00a8  */
        /* JADX WARN: Code duplicated, block: B:243:0x059c  */
        /* JADX WARN: Code duplicated, block: B:245:0x05d7  */
        /* JADX WARN: Code duplicated, block: B:246:0x05e7  */
        /* JADX WARN: Code duplicated, block: B:247:0x05f7  */
        /* JADX WARN: Code duplicated, block: B:25:0x00ba  */
        /* JADX WARN: Code duplicated, block: B:264:0x0692  */
        /* JADX WARN: Code duplicated, block: B:27:0x00c0  */
        /* JADX WARN: Code duplicated, block: B:28:0x00c7  */
        /* JADX WARN: Code duplicated, block: B:30:0x00cd  */
        /* JADX WARN: Code duplicated, block: B:31:0x00d4  */
        /* JADX WARN: Code duplicated, block: B:33:0x00da  */
        /* JADX WARN: Code duplicated, block: B:34:0x00e1  */
        /* JADX WARN: Code duplicated, block: B:390:0x08c2  */
        /* JADX WARN: Code duplicated, block: B:392:0x08c6  */
        /* JADX WARN: Code duplicated, block: B:393:0x08d6  */
        /* JADX WARN: Code duplicated, block: B:396:0x08e9  */
        /* JADX WARN: Code duplicated, block: B:397:0x08ec  */
        /* JADX WARN: Code duplicated, block: B:400:0x08fd  */
        /* JADX WARN: Code duplicated, block: B:401:0x0900  */
        /* JADX WARN: Code duplicated, block: B:403:0x090a  */
        /* JADX WARN: Code duplicated, block: B:405:0x0911  */
        /* JADX WARN: Code duplicated, block: B:406:0x0914  */
        /* JADX WARN: Code duplicated, block: B:415:0x0151 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:49:0x010b  */
        /* JADX WARN: Code duplicated, block: B:50:0x010d  */
        /* JADX WARN: Code duplicated, block: B:52:0x0120  */
        /* JADX WARN: Code duplicated, block: B:63:0x013b  */
        /* JADX WARN: Code duplicated, block: B:69:0x0167  */
        /* JADX WARN: Code duplicated, block: B:72:0x017c A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:73:0x017e A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:75:0x0189 A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:76:0x018d A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:78:0x0193 A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:79:0x0197 A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:85:0x01a5  */
        /* JADX WARN: Code duplicated, block: B:88:0x01b5 A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:90:0x01c0 A[Catch: all -> 0x0164, TryCatch #0 {all -> 0x0164, blocks: (B:66:0x0151, B:70:0x0168, B:73:0x017e, B:75:0x0189, B:80:0x019c, B:82:0x019f, B:86:0x01a6, B:98:0x020d, B:76:0x018d, B:78:0x0193, B:79:0x0197, B:88:0x01b5, B:90:0x01c0, B:91:0x01ce, B:93:0x01e2, B:97:0x01e9), top: B:415:0x0151 }] */
        /* JADX WARN: Code duplicated, block: B:96:0x01e8  */
        /* JADX WARN: Instruction removed from duplicated block: B:221:0x04aa, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:225:0x04c9, please report this as an issue */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r11v0 */
        /* JADX WARN: Type inference failed for: r11v5, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r11v7 */
        public void updateDialogs() {
            TLRPC.Dialog tL_dialog;
            int i;
            String firstName;
            TLRPC.FileLocation fileLocation;
            TLRPC.Chat chat;
            TLRPC.User user;
            Bitmap bitmapDecodeFile;
            int i2;
            int i3;
            String str;
            int i4;
            int i5;
            AvatarDrawable avatarDrawable;
            TLRPC.UserProfilePhoto userProfilePhoto;
            TLRPC.Dialog dialog;
            TLRPC.Dialog tL_dialog2;
            TLRPC.Chat chat2;
            String name;
            TLRPC.User user2;
            TLRPC.FileLocation fileLocation2;
            TLRPC.ChatPhoto chatPhoto;
            Bitmap bitmapDecodeFile2;
            ArrayList arrayList;
            MessageObject messageObject;
            long fromChatId;
            TLRPC.Chat chat3;
            TLRPC.User user3;
            int color;
            String str2;
            TLRPC.MessageMedia messageMedia;
            CharSequence charSequence;
            CharSequence charSequence2;
            String strReplace;
            SpannableStringBuilder spannableStringBuilderValueOf;
            int i6;
            String string;
            SpannableStringBuilder spannableStringBuilder;
            CharSequence string2;
            CharSequence charSequence3;
            TLRPC.MessageAction messageAction;
            int iDp;
            Canvas canvas;
            boolean z;
            AvatarDrawable avatarDrawable2;
            boolean z2;
            AvatarDrawable avatarDrawable3;
            TLRPC.UserProfilePhoto userProfilePhoto2;
            int i7 = 12;
            float f = 0.0f;
            int i8 = 8;
            int i9 = 2;
            ?? r11 = 1;
            if (EditWidgetActivity.this.widgetType == 0) {
                int i10 = 0;
                while (i10 < i9) {
                    if (EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                        tL_dialog2 = i10 < EditWidgetActivity.this.getMessagesController().dialogsServerOnly.size() ? EditWidgetActivity.this.getMessagesController().dialogsServerOnly.get(i10) : null;
                    } else {
                        if (i10 < EditWidgetActivity.this.selectedDialogs.size()) {
                            tL_dialog2 = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i10)).longValue());
                            if (tL_dialog2 == null) {
                                tL_dialog2 = new TLRPC.TL_dialog();
                                tL_dialog2.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i10)).longValue();
                            }
                        } else {
                            dialog = null;
                        }
                        if (dialog == null) {
                            this.cells[i10].setVisibility(i8);
                        } else {
                            this.cells[i10].setVisibility(0);
                            if (DialogObject.isUserDialog(dialog.id)) {
                                user2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog.id));
                                if (user2 != null) {
                                    if (UserObject.isUserSelf(user2)) {
                                        name = LocaleController.getString(R.string.SavedMessages);
                                    } else if (UserObject.isReplyUser(user2)) {
                                        name = LocaleController.getString(R.string.RepliesTitle);
                                    } else if (UserObject.isDeleted(user2)) {
                                        name = LocaleController.getString(R.string.HiddenName);
                                    } else {
                                        name = ContactsController.formatName(user2.first_name, user2.last_name);
                                    }
                                    if (!UserObject.isReplyUser(user2) || UserObject.isUserSelf(user2) || (userProfilePhoto2 = user2.photo) == null || (fileLocation2 = userProfilePhoto2.photo_small) == null || fileLocation2.volume_id == 0 || fileLocation2.local_id == 0) {
                                    }
                                    chat2 = null;
                                } else {
                                    name = _UrlKt.FRAGMENT_ENCODE_SET;
                                }
                                fileLocation2 = null;
                                chat2 = null;
                            } else {
                                chat2 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog.id));
                                if (chat2 != null) {
                                    name = chat2.title;
                                    chatPhoto = chat2.photo;
                                    if (chatPhoto == null && (fileLocation2 = chatPhoto.photo_small) != null && fileLocation2.volume_id != 0 && fileLocation2.local_id != 0) {
                                        chat2 = chat2;
                                        user2 = null;
                                    }
                                } else {
                                    name = _UrlKt.FRAGMENT_ENCODE_SET;
                                }
                                user2 = null;
                                fileLocation2 = null;
                            }
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_text)).setText(name);
                            if (fileLocation2 != null) {
                                try {
                                    bitmapDecodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(fileLocation2, r11).toString());
                                } catch (Throwable th) {
                                    FileLog.e(th);
                                }
                            } else {
                                bitmapDecodeFile2 = null;
                            }
                            iDp = AndroidUtilities.dp(48.0f);
                            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iDp, iDp, Bitmap.Config.ARGB_8888);
                            bitmapCreateBitmap.eraseColor(0);
                            canvas = new Canvas(bitmapCreateBitmap);
                            if (bitmapDecodeFile2 == null) {
                                if (user2 != null) {
                                    avatarDrawable3 = new AvatarDrawable(user2);
                                    if (UserObject.isReplyUser(user2)) {
                                        avatarDrawable3.setAvatarType(i7);
                                        avatarDrawable2 = avatarDrawable3;
                                    } else if (UserObject.isUserSelf(user2)) {
                                        avatarDrawable2 = avatarDrawable3;
                                        avatarDrawable3.setAvatarType(r11);
                                        avatarDrawable2 = avatarDrawable3;
                                    }
                                } else {
                                    avatarDrawable2 = new AvatarDrawable(chat2);
                                }
                                avatarDrawable2 = avatarDrawable3;
                                float f2 = iDp;
                                if (chat2 == null && chat2.forum) {
                                    z2 = r11;
                                } else {
                                    z2 = 0;
                                }
                                avatarDrawable2.setRoundRadius(ExteraConfig.getAvatarCorners(f2, r11, z2));
                                avatarDrawable2.setBounds(0, 0, iDp, iDp);
                                avatarDrawable2.draw(canvas);
                            } else {
                                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                BitmapShader bitmapShader = new BitmapShader(bitmapDecodeFile2, tileMode, tileMode);
                                if (this.roundPaint == null) {
                                    this.roundPaint = new Paint((int) r11);
                                    this.bitmapRect = new RectF();
                                }
                                float width = iDp / bitmapDecodeFile2.getWidth();
                                canvas.save();
                                canvas.scale(width, width);
                                float width2 = bitmapDecodeFile2.getWidth();
                                if (chat2 == null && chat2.forum) {
                                    z = r11;
                                } else {
                                    z = 0;
                                }
                                float avatarCorners = ExteraConfig.getAvatarCorners(width2, r11, z);
                                this.roundPaint.setShader(bitmapShader);
                                this.bitmapRect.set(f, f, bitmapDecodeFile2.getWidth(), bitmapDecodeFile2.getHeight());
                                canvas.drawRoundRect(this.bitmapRect, avatarCorners, avatarCorners, this.roundPaint);
                                canvas.restore();
                            }
                            canvas.setBitmap(null);
                            ((ImageView) this.cells[i10].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmapCreateBitmap);
                            arrayList = (ArrayList) EditWidgetActivity.this.getMessagesController().dialogMessage.get(dialog.id);
                            if (arrayList != null || arrayList.size() <= 0) {
                                messageObject = null;
                            } else {
                                messageObject = (MessageObject) arrayList.get(0);
                            }
                            if (messageObject != null) {
                                fromChatId = messageObject.getFromChatId();
                                if (fromChatId > 0) {
                                    user3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                    chat3 = null;
                                } else {
                                    chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                    user3 = null;
                                }
                                color = getContext().getResources().getColor(R.color.widget_text);
                                if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                                    if (ChatObject.isChannel(chat2)) {
                                        messageAction = messageObject.messageOwner.action;
                                        if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear) && !(messageAction instanceof TLRPC.TL_messageActionChannelMigrateFrom)) {
                                            r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                            r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                            charSequence3 = messageObject.messageText;
                                        }
                                    } else {
                                        r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                        r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                        charSequence3 = messageObject.messageText;
                                    }
                                    r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    color = getContext().getResources().getColor(R.color.widget_action_text);
                                    string2 = charSequence3;
                                } else {
                                    str2 = "📎 ";
                                    if (chat2 != null) {
                                        int i11 = r11;
                                        if (chat2.id <= 0 && chat3 == null && (!ChatObject.isChannel(chat2) || ChatObject.isMegagroup(chat2))) {
                                            if (messageObject.isOutOwner()) {
                                                strReplace = LocaleController.getString(R.string.FromYou);
                                            } else if (user3 != null) {
                                                strReplace = UserObject.getFirstName(user3).replace("\n", _UrlKt.FRAGMENT_ENCODE_SET);
                                            } else {
                                                strReplace = "DELETED";
                                            }
                                            String str3 = strReplace;
                                            CharSequence charSequence4 = messageObject.caption;
                                            try {
                                                if (charSequence4 != null) {
                                                    String string3 = charSequence4.toString();
                                                    if (string3.length() > 150) {
                                                        string3 = string3.substring(0, 150);
                                                    }
                                                    if (messageObject.isVideo()) {
                                                        str2 = "📹 ";
                                                    } else if (messageObject.isVoice()) {
                                                        str2 = "🎤 ";
                                                    } else if (messageObject.isMusic()) {
                                                        str2 = "🎧 ";
                                                    } else if (messageObject.isPhoto()) {
                                                        str2 = "🖼 ";
                                                    }
                                                    Object[] objArr = new Object[2];
                                                    objArr[0] = str2 + string3.replace('\n', ' ');
                                                    objArr[i11] = str3;
                                                    spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr));
                                                } else {
                                                    if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                                        color = getContext().getResources().getColor(R.color.widget_action_text);
                                                        TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
                                                        try {
                                                            if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                                                Object[] objArr2 = new Object[i11];
                                                                objArr2[0] = ((TLRPC.TL_messageMediaPoll) messageMedia2).poll.question.text;
                                                                string = String.format("📊 \u2068%s\u2069", objArr2);
                                                            } else {
                                                                if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                                                    Object[] objArr3 = new Object[i11];
                                                                    objArr3[0] = messageMedia2.game.title;
                                                                    string = String.format("🎮 \u2068%s\u2069", objArr3);
                                                                    i11 = i11;
                                                                } else if (messageObject.type == 14) {
                                                                    String musicAuthor = messageObject.getMusicAuthor();
                                                                    String musicTitle = messageObject.getMusicTitle();
                                                                    i11 = i11;
                                                                    i6 = 2;
                                                                    Object[] objArr4 = new Object[2];
                                                                    objArr4[0] = musicAuthor;
                                                                    objArr4[i11 == true ? 1 : 0] = musicTitle;
                                                                    string = String.format("🎧 \u2068%s - %s\u2069", objArr4);
                                                                } else {
                                                                    i11 = i11;
                                                                    i6 = 2;
                                                                    string = messageObject.messageText.toString();
                                                                }
                                                                Object[] objArr5 = new Object[i6];
                                                                objArr5[0] = string.replace('\n', ' ');
                                                                objArr5[i11] = str3;
                                                                SpannableStringBuilder spannableStringBuilderValueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr5));
                                                                spannableStringBuilderValueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str3.length() + 2, spannableStringBuilderValueOf2.length(), 33);
                                                                spannableStringBuilder = spannableStringBuilderValueOf2;
                                                            }
                                                            spannableStringBuilderValueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), str3.length() + 2, spannableStringBuilderValueOf2.length(), 33);
                                                        } catch (Exception e) {
                                                            FileLog.e(e);
                                                        }
                                                        i6 = 2;
                                                        Object[] objArr6 = new Object[i6];
                                                        objArr6[0] = string.replace('\n', ' ');
                                                        objArr6[i11] = str3;
                                                        SpannableStringBuilder spannableStringBuilderValueOf3 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr6));
                                                        spannableStringBuilder = spannableStringBuilderValueOf3;
                                                    } else {
                                                        String strSubstring = messageObject.messageOwner.message;
                                                        if (strSubstring != null) {
                                                            if (strSubstring.length() > 150) {
                                                                strSubstring = strSubstring.substring(0, 150);
                                                            }
                                                            spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", strSubstring.replace('\n', ' ').trim(), str3));
                                                        } else {
                                                            spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(_UrlKt.FRAGMENT_ENCODE_SET);
                                                        }
                                                    }
                                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str3.length() + 1, 33);
                                                    string2 = spannableStringBuilder;
                                                }
                                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, str3.length() + 1, 33);
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
                                                    CharSequence charSequence5 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence5, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence5;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null && !messageObject.isMediaEmpty()) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
                                                    string2 = charSequence2;
                                                }
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
                                                        CharSequence charSequence6 = messageObject.messageText;
                                                        AndroidUtilities.highlightText(charSequence6, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                        charSequence = charSequence6;
                                                    }
                                                    charSequence2 = charSequence;
                                                    string2 = charSequence2;
                                                    if (messageObject.messageOwner.media != null) {
                                                        string2 = charSequence2;
                                                        color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence7 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence7, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence7;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence8 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence8, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence8;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                CharSequence charSequence9 = messageObject.messageText;
                                                AndroidUtilities.highlightText(charSequence9, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                charSequence = charSequence9;
                                            }
                                            charSequence2 = charSequence;
                                            string2 = charSequence2;
                                            if (messageObject.messageOwner.media != null) {
                                                string2 = charSequence2;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
                                                string2 = charSequence2;
                                            }
                                        }
                                    }
                                }
                                string2 = charSequence2;
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setText(string2.toString());
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setTextColor(color);
                            } else {
                                if (dialog.last_message_date != 0) {
                                    ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(dialog.last_message_date));
                                } else {
                                    ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(_UrlKt.FRAGMENT_ENCODE_SET);
                                }
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setText(_UrlKt.FRAGMENT_ENCODE_SET);
                            }
                            if (dialog.unread_count > 0) {
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_badge)).setText(String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(dialog.unread_count)));
                                this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setVisibility(0);
                                if (EditWidgetActivity.this.getMessagesController().isDialogMuted(dialog.id, 0L)) {
                                    this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter_muted);
                                } else {
                                    this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter);
                                }
                            } else {
                                this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                            }
                        }
                        i10++;
                        i7 = 12;
                        f = 0.0f;
                        i8 = 8;
                        i9 = 2;
                        r11 = 1;
                    }
                    dialog = tL_dialog2;
                    if (dialog == null) {
                        this.cells[i10].setVisibility(i8);
                    } else {
                        this.cells[i10].setVisibility(0);
                        if (DialogObject.isUserDialog(dialog.id)) {
                            user2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog.id));
                            if (user2 != null) {
                                if (UserObject.isUserSelf(user2)) {
                                    name = LocaleController.getString(R.string.SavedMessages);
                                } else if (UserObject.isReplyUser(user2)) {
                                    name = LocaleController.getString(R.string.RepliesTitle);
                                } else if (UserObject.isDeleted(user2)) {
                                    name = LocaleController.getString(R.string.HiddenName);
                                } else {
                                    name = ContactsController.formatName(user2.first_name, user2.last_name);
                                }
                                if (!UserObject.isReplyUser(user2)) {
                                }
                                chat2 = null;
                            } else {
                                name = _UrlKt.FRAGMENT_ENCODE_SET;
                            }
                            fileLocation2 = null;
                            chat2 = null;
                        } else {
                            chat2 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog.id));
                            if (chat2 != null) {
                                name = chat2.title;
                                chatPhoto = chat2.photo;
                                if (chatPhoto == null) {
                                }
                            } else {
                                name = _UrlKt.FRAGMENT_ENCODE_SET;
                            }
                            user2 = null;
                            fileLocation2 = null;
                        }
                        ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_text)).setText(name);
                        if (fileLocation2 != null) {
                            bitmapDecodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(fileLocation2, r11).toString());
                        } else {
                            bitmapDecodeFile2 = null;
                        }
                        iDp = AndroidUtilities.dp(48.0f);
                        Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(iDp, iDp, Bitmap.Config.ARGB_8888);
                        bitmapCreateBitmap2.eraseColor(0);
                        canvas = new Canvas(bitmapCreateBitmap2);
                        if (bitmapDecodeFile2 == null) {
                            if (user2 != null) {
                                avatarDrawable3 = new AvatarDrawable(user2);
                                if (UserObject.isReplyUser(user2)) {
                                    avatarDrawable3.setAvatarType(i7);
                                    avatarDrawable2 = avatarDrawable3;
                                } else if (UserObject.isUserSelf(user2)) {
                                    avatarDrawable2 = avatarDrawable3;
                                    avatarDrawable3.setAvatarType(r11);
                                    avatarDrawable2 = avatarDrawable3;
                                }
                            } else {
                                avatarDrawable2 = new AvatarDrawable(chat2);
                            }
                            avatarDrawable2 = avatarDrawable3;
                            float f3 = iDp;
                            if (chat2 == null) {
                                z2 = 0;
                            } else {
                                z2 = 0;
                            }
                            avatarDrawable2.setRoundRadius(ExteraConfig.getAvatarCorners(f3, r11, z2));
                            avatarDrawable2.setBounds(0, 0, iDp, iDp);
                            avatarDrawable2.draw(canvas);
                        } else {
                            Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
                            BitmapShader bitmapShader2 = new BitmapShader(bitmapDecodeFile2, tileMode2, tileMode2);
                            if (this.roundPaint == null) {
                                this.roundPaint = new Paint((int) r11);
                                this.bitmapRect = new RectF();
                            }
                            float width3 = iDp / bitmapDecodeFile2.getWidth();
                            canvas.save();
                            canvas.scale(width3, width3);
                            float width4 = bitmapDecodeFile2.getWidth();
                            if (chat2 == null) {
                                z = 0;
                            } else {
                                z = 0;
                            }
                            float avatarCorners2 = ExteraConfig.getAvatarCorners(width4, r11, z);
                            this.roundPaint.setShader(bitmapShader2);
                            this.bitmapRect.set(f, f, bitmapDecodeFile2.getWidth(), bitmapDecodeFile2.getHeight());
                            canvas.drawRoundRect(this.bitmapRect, avatarCorners2, avatarCorners2, this.roundPaint);
                            canvas.restore();
                        }
                        canvas.setBitmap(null);
                        ((ImageView) this.cells[i10].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(bitmapCreateBitmap2);
                        arrayList = (ArrayList) EditWidgetActivity.this.getMessagesController().dialogMessage.get(dialog.id);
                        if (arrayList != null) {
                            messageObject = null;
                        } else {
                            messageObject = null;
                        }
                        if (messageObject != null) {
                            fromChatId = messageObject.getFromChatId();
                            if (fromChatId > 0) {
                                user3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                chat3 = null;
                            } else {
                                chat3 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                user3 = null;
                            }
                            color = getContext().getResources().getColor(R.color.widget_text);
                            if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                                if (ChatObject.isChannel(chat2)) {
                                    messageAction = messageObject.messageOwner.action;
                                    if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear)) {
                                        r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                        r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                        charSequence3 = messageObject.messageText;
                                    }
                                } else {
                                    r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                    charSequence3 = messageObject.messageText;
                                }
                                r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                r6 = _UrlKt.FRAGMENT_ENCODE_SET;
                                color = getContext().getResources().getColor(R.color.widget_action_text);
                                string2 = charSequence3;
                            } else {
                                str2 = "📎 ";
                                if (chat2 != null) {
                                    int i12 = r11;
                                    if (chat2.id <= 0) {
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
                                                        CharSequence charSequence10 = messageObject.messageText;
                                                        AndroidUtilities.highlightText(charSequence10, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                        charSequence = charSequence10;
                                                    }
                                                    charSequence2 = charSequence;
                                                    string2 = charSequence2;
                                                    if (messageObject.messageOwner.media != null) {
                                                        string2 = charSequence2;
                                                        color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence11 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence11, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence11;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence12 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence12, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence12;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                CharSequence charSequence13 = messageObject.messageText;
                                                AndroidUtilities.highlightText(charSequence13, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                charSequence = charSequence13;
                                            }
                                            charSequence2 = charSequence;
                                            string2 = charSequence2;
                                            if (messageObject.messageOwner.media != null) {
                                                string2 = charSequence2;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                        CharSequence charSequence14 = messageObject.messageText;
                                                        AndroidUtilities.highlightText(charSequence14, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                        charSequence = charSequence14;
                                                    }
                                                    charSequence2 = charSequence;
                                                    string2 = charSequence2;
                                                    if (messageObject.messageOwner.media != null) {
                                                        string2 = charSequence2;
                                                        color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence15 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence15, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence15;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                    CharSequence charSequence16 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence16, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence16;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                CharSequence charSequence17 = messageObject.messageText;
                                                AndroidUtilities.highlightText(charSequence17, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                charSequence = charSequence17;
                                            }
                                            charSequence2 = charSequence;
                                            string2 = charSequence2;
                                            if (messageObject.messageOwner.media != null) {
                                                string2 = charSequence2;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
                                                string2 = charSequence2;
                                            }
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
                                                    CharSequence charSequence18 = messageObject.messageText;
                                                    AndroidUtilities.highlightText(charSequence18, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                    charSequence = charSequence18;
                                                }
                                                charSequence2 = charSequence;
                                                string2 = charSequence2;
                                                if (messageObject.messageOwner.media != null) {
                                                    string2 = charSequence2;
                                                    color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                CharSequence charSequence19 = messageObject.messageText;
                                                AndroidUtilities.highlightText(charSequence19, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                charSequence = charSequence19;
                                            }
                                            charSequence2 = charSequence;
                                            string2 = charSequence2;
                                            if (messageObject.messageOwner.media != null) {
                                                string2 = charSequence2;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                                CharSequence charSequence110 = messageObject.messageText;
                                                AndroidUtilities.highlightText(charSequence110, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                                charSequence = charSequence110;
                                            }
                                            charSequence2 = charSequence;
                                            string2 = charSequence2;
                                            if (messageObject.messageOwner.media != null) {
                                                string2 = charSequence2;
                                                color = getContext().getResources().getColor(R.color.widget_action_text);
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
                                            CharSequence charSequence111 = messageObject.messageText;
                                            AndroidUtilities.highlightText(charSequence111, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                            charSequence = charSequence111;
                                        }
                                        charSequence2 = charSequence;
                                        string2 = charSequence2;
                                        if (messageObject.messageOwner.media != null) {
                                            string2 = charSequence2;
                                            color = getContext().getResources().getColor(R.color.widget_action_text);
                                            string2 = charSequence2;
                                        }
                                    }
                                }
                            }
                            string2 = charSequence2;
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setText(string2.toString());
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setTextColor(color);
                        } else {
                            if (dialog.last_message_date != 0) {
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(dialog.last_message_date));
                            } else {
                                ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_time)).setText(_UrlKt.FRAGMENT_ENCODE_SET);
                            }
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_message)).setText(_UrlKt.FRAGMENT_ENCODE_SET);
                        }
                        if (dialog.unread_count > 0) {
                            ((TextView) this.cells[i10].findViewById(R.id.shortcut_widget_item_badge)).setText(String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(dialog.unread_count)));
                            this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setVisibility(0);
                            if (EditWidgetActivity.this.getMessagesController().isDialogMuted(dialog.id, 0L)) {
                                this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter_muted);
                            } else {
                                this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setBackgroundResource(R.drawable.widget_counter);
                            }
                        } else {
                            this.cells[i10].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                        }
                    }
                    i10++;
                    i7 = 12;
                    f = 0.0f;
                    i8 = 8;
                    i9 = 2;
                    r11 = 1;
                }
                this.cells[0].findViewById(R.id.shortcut_widget_item_divider).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(R.id.shortcut_widget_item_divider).setVisibility(8);
            } else if (EditWidgetActivity.this.widgetType == 1) {
                int i13 = 0;
                int i14 = 2;
                while (i13 < i14) {
                    int i15 = 0;
                    while (i15 < i14) {
                        int i16 = (i13 * 2) + i15;
                        if (EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                            if (i16 < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                long j = EditWidgetActivity.this.getMediaDataController().hints.get(i16).peer.user_id;
                                TLRPC.Dialog tL_dialog3 = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(j);
                                if (tL_dialog3 == null) {
                                    tL_dialog3 = new TLRPC.TL_dialog();
                                    tL_dialog3.id = j;
                                }
                                tL_dialog = tL_dialog3;
                            } else {
                                tL_dialog = null;
                            }
                        } else if (i16 < EditWidgetActivity.this.selectedDialogs.size()) {
                            tL_dialog = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i16)).longValue());
                            if (tL_dialog == null) {
                                tL_dialog = new TLRPC.TL_dialog();
                                tL_dialog.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i16)).longValue();
                            }
                        } else {
                            tL_dialog = null;
                        }
                        if (tL_dialog == null) {
                            this.cells[i13].findViewById(i15 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(4);
                            if (i16 == 0 || i16 == 2) {
                                this.cells[i13].setVisibility(8);
                                i = 2;
                            } else {
                                i = 2;
                            }
                        } else {
                            this.cells[i13].findViewById(i15 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(0);
                            i = 2;
                            if (i16 == 0 || i16 == 2) {
                                this.cells[i13].setVisibility(0);
                            }
                            if (DialogObject.isUserDialog(tL_dialog.id)) {
                                user = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(tL_dialog.id));
                                if (UserObject.isUserSelf(user)) {
                                    firstName = LocaleController.getString(R.string.SavedMessages);
                                } else if (UserObject.isReplyUser(user)) {
                                    firstName = LocaleController.getString(R.string.RepliesTitle);
                                } else if (UserObject.isDeleted(user)) {
                                    firstName = LocaleController.getString(R.string.HiddenName);
                                } else {
                                    firstName = UserObject.getFirstName(user);
                                }
                                if (UserObject.isReplyUser(user) || UserObject.isUserSelf(user) || user == null || (userProfilePhoto = user.photo) == null || (fileLocation = userProfilePhoto.photo_small) == null || fileLocation.volume_id == 0 || fileLocation.local_id == 0) {
                                    chat = null;
                                    fileLocation = null;
                                } else {
                                    chat = null;
                                }
                            } else {
                                TLRPC.Chat chat4 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-tL_dialog.id));
                                firstName = chat4.title;
                                TLRPC.ChatPhoto chatPhoto2 = chat4.photo;
                                if (chatPhoto2 != null && (fileLocation = chatPhoto2.photo_small) != null) {
                                    if (fileLocation.volume_id == 0 || fileLocation.local_id == 0) {
                                    }
                                    chat = chat4;
                                    user = null;
                                }
                                fileLocation = null;
                                chat = chat4;
                                user = null;
                            }
                            ((TextView) this.cells[i13].findViewById(i15 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(firstName);
                            if (fileLocation != null) {
                                try {
                                    bitmapDecodeFile = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(fileLocation, true).toString());
                                } catch (Throwable th2) {
                                    th = th2;
                                    FileLog.e(th);
                                    i2 = tL_dialog.unread_count;
                                    if (i2 > 0) {
                                        if (i2 > 99) {
                                            str = String.format("%d+", 99);
                                        } else {
                                            str = String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i2));
                                        }
                                        ViewGroup viewGroup = this.cells[i13];
                                        if (i15 == 0) {
                                            i4 = R.id.contacts_widget_item_badge1;
                                        } else {
                                            i4 = R.id.contacts_widget_item_badge2;
                                        }
                                        ((TextView) viewGroup.findViewById(i4)).setText(str);
                                        ViewGroup viewGroup2 = this.cells[i13];
                                        if (i15 == 0) {
                                            i5 = R.id.contacts_widget_item_badge_bg1;
                                        } else {
                                            i5 = R.id.contacts_widget_item_badge_bg2;
                                        }
                                        viewGroup2.findViewById(i5).setVisibility(0);
                                    } else {
                                        ViewGroup viewGroup3 = this.cells[i13];
                                        if (i15 == 0) {
                                            i3 = R.id.contacts_widget_item_badge_bg1;
                                        } else {
                                            i3 = R.id.contacts_widget_item_badge_bg2;
                                        }
                                        viewGroup3.findViewById(i3).setVisibility(8);
                                    }
                                    i15++;
                                    i14 = i;
                                }
                            } else {
                                bitmapDecodeFile = null;
                            }
                            int iDp2 = AndroidUtilities.dp(48.0f);
                            Bitmap bitmapCreateBitmap3 = Bitmap.createBitmap(iDp2, iDp2, Bitmap.Config.ARGB_8888);
                            bitmapCreateBitmap3.eraseColor(0);
                            Canvas canvas2 = new Canvas(bitmapCreateBitmap3);
                            if (bitmapDecodeFile == null) {
                                if (user != null) {
                                    try {
                                        avatarDrawable = new AvatarDrawable(user);
                                        if (UserObject.isReplyUser(user)) {
                                            try {
                                                avatarDrawable.setAvatarType(12);
                                            } catch (Throwable th3) {
                                                th = th3;
                                                FileLog.e(th);
                                                i2 = tL_dialog.unread_count;
                                                if (i2 > 0) {
                                                    if (i2 > 99) {
                                                        str = String.format("%d+", 99);
                                                    } else {
                                                        str = String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i2));
                                                    }
                                                    ViewGroup viewGroup4 = this.cells[i13];
                                                    if (i15 == 0) {
                                                        i4 = R.id.contacts_widget_item_badge1;
                                                    } else {
                                                        i4 = R.id.contacts_widget_item_badge2;
                                                    }
                                                    ((TextView) viewGroup4.findViewById(i4)).setText(str);
                                                    ViewGroup viewGroup5 = this.cells[i13];
                                                    if (i15 == 0) {
                                                        i5 = R.id.contacts_widget_item_badge_bg1;
                                                    } else {
                                                        i5 = R.id.contacts_widget_item_badge_bg2;
                                                    }
                                                    viewGroup5.findViewById(i5).setVisibility(0);
                                                } else {
                                                    ViewGroup viewGroup6 = this.cells[i13];
                                                    if (i15 == 0) {
                                                        i3 = R.id.contacts_widget_item_badge_bg1;
                                                    } else {
                                                        i3 = R.id.contacts_widget_item_badge_bg2;
                                                    }
                                                    viewGroup6.findViewById(i3).setVisibility(8);
                                                }
                                                i15++;
                                                i14 = i;
                                            }
                                        } else if (UserObject.isUserSelf(user)) {
                                            avatarDrawable.setAvatarType(1);
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                    }
                                } else {
                                    avatarDrawable = new AvatarDrawable(chat);
                                }
                                avatarDrawable.setBounds(0, 0, iDp2, iDp2);
                                avatarDrawable.setRoundRadius(ExteraConfig.getAvatarCorners(iDp2, true, chat != null && chat.forum));
                                avatarDrawable.draw(canvas2);
                            } else {
                                Shader.TileMode tileMode3 = Shader.TileMode.CLAMP;
                                BitmapShader bitmapShader3 = new BitmapShader(bitmapDecodeFile, tileMode3, tileMode3);
                                float width5 = iDp2 / bitmapDecodeFile.getWidth();
                                canvas2.save();
                                canvas2.scale(width5, width5);
                                float avatarCorners3 = ExteraConfig.getAvatarCorners(bitmapDecodeFile.getWidth(), true, chat != null && chat.forum);
                                this.roundPaint.setShader(bitmapShader3);
                                try {
                                    this.bitmapRect.set(0.0f, 0.0f, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight());
                                    canvas2.drawRoundRect(this.bitmapRect, avatarCorners3, avatarCorners3, this.roundPaint);
                                    canvas2.restore();
                                } catch (Throwable th5) {
                                    th = th5;
                                    FileLog.e(th);
                                    i2 = tL_dialog.unread_count;
                                    if (i2 > 0) {
                                        if (i2 > 99) {
                                            str = String.format("%d+", 99);
                                        } else {
                                            str = String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i2));
                                        }
                                        ViewGroup viewGroup7 = this.cells[i13];
                                        if (i15 == 0) {
                                            i4 = R.id.contacts_widget_item_badge1;
                                        } else {
                                            i4 = R.id.contacts_widget_item_badge2;
                                        }
                                        ((TextView) viewGroup7.findViewById(i4)).setText(str);
                                        ViewGroup viewGroup8 = this.cells[i13];
                                        if (i15 == 0) {
                                            i5 = R.id.contacts_widget_item_badge_bg1;
                                        } else {
                                            i5 = R.id.contacts_widget_item_badge_bg2;
                                        }
                                        viewGroup8.findViewById(i5).setVisibility(0);
                                    } else {
                                        ViewGroup viewGroup9 = this.cells[i13];
                                        if (i15 == 0) {
                                            i3 = R.id.contacts_widget_item_badge_bg1;
                                        } else {
                                            i3 = R.id.contacts_widget_item_badge_bg2;
                                        }
                                        viewGroup9.findViewById(i3).setVisibility(8);
                                    }
                                    i15++;
                                    i14 = i;
                                }
                            }
                            try {
                                canvas2.setBitmap(null);
                                ((ImageView) this.cells[i13].findViewById(i15 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(bitmapCreateBitmap3);
                            } catch (Throwable th6) {
                                th = th6;
                                FileLog.e(th);
                            }
                            i2 = tL_dialog.unread_count;
                            if (i2 > 0) {
                                if (i2 > 99) {
                                    str = String.format("%d+", 99);
                                } else {
                                    str = String.format(TimeModel.NUMBER_FORMAT, Integer.valueOf(i2));
                                }
                                ViewGroup viewGroup10 = this.cells[i13];
                                if (i15 == 0) {
                                    i4 = R.id.contacts_widget_item_badge1;
                                } else {
                                    i4 = R.id.contacts_widget_item_badge2;
                                }
                                ((TextView) viewGroup10.findViewById(i4)).setText(str);
                                ViewGroup viewGroup11 = this.cells[i13];
                                if (i15 == 0) {
                                    i5 = R.id.contacts_widget_item_badge_bg1;
                                } else {
                                    i5 = R.id.contacts_widget_item_badge_bg2;
                                }
                                viewGroup11.findViewById(i5).setVisibility(0);
                            } else {
                                ViewGroup viewGroup12 = this.cells[i13];
                                if (i15 == 0) {
                                    i3 = R.id.contacts_widget_item_badge_bg1;
                                } else {
                                    i3 = R.id.contacts_widget_item_badge_bg2;
                                }
                                viewGroup12.findViewById(i3).setVisibility(8);
                            }
                        }
                        i15++;
                        i14 = i;
                    }
                    i13++;
                    i14 = i14;
                }
            }
            if (this.cells[0].getVisibility() == 0) {
                EditWidgetActivity.this.previewImageView.setVisibility(8);
            } else {
                EditWidgetActivity.this.previewImageView.setVisibility(0);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(264.0f), TLObject.FLAG_30));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != this.backgroundDrawable && cachedWallpaperNonBlocking != null) {
                if (Theme.isAnimatingColor()) {
                    this.oldBackgroundDrawable = this.backgroundDrawable;
                    this.oldBackgroundGradientDisposable = this.backgroundGradientDisposable;
                } else {
                    BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                    if (disposable != null) {
                        disposable.dispose();
                        this.backgroundGradientDisposable = null;
                    }
                }
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            float themeAnimationValue = ((BaseFragment) EditWidgetActivity.this).parentLayout.getThemeAnimationValue();
            int i = 0;
            while (i < 2) {
                Drawable drawable = i == 0 ? this.oldBackgroundDrawable : this.backgroundDrawable;
                if (drawable != null) {
                    if (i == 1 && this.oldBackgroundDrawable != null && ((BaseFragment) EditWidgetActivity.this).parentLayout != null) {
                        drawable.setAlpha((int) (255.0f * themeAnimationValue));
                    } else {
                        drawable.setAlpha(255);
                    }
                    if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        if (drawable instanceof BackgroundGradientDrawable) {
                            this.backgroundGradientDisposable = ((BackgroundGradientDrawable) drawable).drawExactBoundsSize(canvas, this);
                        } else {
                            drawable.draw(canvas);
                        }
                    } else if (drawable instanceof BitmapDrawable) {
                        if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                            canvas.save();
                            float f = 2.0f / AndroidUtilities.density;
                            canvas.scale(f, f);
                            drawable.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f), (int) Math.ceil(getMeasuredHeight() / f));
                        } else {
                            int measuredHeight = getMeasuredHeight();
                            float fMax = Math.max(getMeasuredWidth() / drawable.getIntrinsicWidth(), measuredHeight / drawable.getIntrinsicHeight());
                            int iCeil = (int) Math.ceil(drawable.getIntrinsicWidth() * fMax);
                            int iCeil2 = (int) Math.ceil(drawable.getIntrinsicHeight() * fMax);
                            int measuredWidth = (getMeasuredWidth() - iCeil) / 2;
                            int i2 = (measuredHeight - iCeil2) / 2;
                            canvas.save();
                            canvas.clipRect(0, 0, iCeil, getMeasuredHeight());
                            drawable.setBounds(measuredWidth, i2, iCeil + measuredWidth, iCeil2 + i2);
                        }
                        drawable.draw(canvas);
                        canvas.restore();
                    }
                    if (i == 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                        BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
                        if (disposable2 != null) {
                            disposable2.dispose();
                            this.oldBackgroundGradientDisposable = null;
                        }
                        this.oldBackgroundDrawable = null;
                        invalidate();
                    }
                }
                i++;
            }
            this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.backgroundGradientDisposable = null;
            }
            BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
            if (disposable2 != null) {
                disposable2.dispose();
                this.oldBackgroundGradientDisposable = null;
            }
        }
    }

    public EditWidgetActivity(int i, int i2) {
        this.widgetType = i;
        this.currentWidgetId = i2;
        ArrayList<TLRPC.User> arrayList = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList2 = new ArrayList<>();
        getMessagesStorage().getWidgetDialogIds(this.currentWidgetId, this.widgetType, this.selectedDialogs, arrayList, arrayList2, true);
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        updateRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        getMediaDataController().loadHints(true);
        return super.onFragmentCreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows() {
        this.previewRow = 0;
        this.rowCount = 1 + 1;
        this.selectChatsRow = 1;
        if (this.selectedDialogs.isEmpty()) {
            this.chatsStartRow = -1;
            this.chatsEndRow = -1;
        } else {
            int i = this.rowCount;
            this.chatsStartRow = i;
            int size = i + this.selectedDialogs.size();
            this.rowCount = size;
            this.chatsEndRow = size;
        }
        int i2 = this.rowCount;
        this.rowCount = i2 + 1;
        this.infoRow = i2;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void setDelegate(EditWidgetActivityDelegate editWidgetActivityDelegate) {
        this.delegate = editWidgetActivityDelegate;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        if (this.widgetType == 0) {
            this.actionBar.setTitle(LocaleController.getString(R.string.WidgetChats));
        } else {
            this.actionBar.setTitle(LocaleController.getString(R.string.WidgetShortcuts));
        }
        this.actionBar.createMenu().addItem(1, LocaleController.getString(R.string.Done).toUpperCase());
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.EditWidgetActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    if (EditWidgetActivity.this.delegate == null) {
                        EditWidgetActivity.this.finishActivity();
                        return;
                    } else {
                        EditWidgetActivity.this.finishFragment();
                        return;
                    }
                }
                if (i != 1 || EditWidgetActivity.this.getParentActivity() == null) {
                    return;
                }
                ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                for (int i2 = 0; i2 < EditWidgetActivity.this.selectedDialogs.size(); i2++) {
                    arrayList.add(MessagesStorage.TopicKey.of(((Long) EditWidgetActivity.this.selectedDialogs.get(i2)).longValue(), 0L));
                }
                EditWidgetActivity.this.getMessagesStorage().putWidgetDialogs(EditWidgetActivity.this.currentWidgetId, arrayList);
                SharedPreferences.Editor editorEdit = EditWidgetActivity.this.getParentActivity().getSharedPreferences("shortcut_widget", 0).edit();
                editorEdit.putInt("account" + EditWidgetActivity.this.currentWidgetId, ((BaseFragment) EditWidgetActivity.this).currentAccount);
                editorEdit.putInt("type" + EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.widgetType);
                editorEdit.apply();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EditWidgetActivity.this.getParentActivity());
                if (EditWidgetActivity.this.widgetType == 0) {
                    ChatsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                } else {
                    ContactsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                }
                if (EditWidgetActivity.this.delegate != null) {
                    EditWidgetActivity.this.delegate.didSelectDialogs(EditWidgetActivity.this.selectedDialogs);
                } else {
                    EditWidgetActivity.this.finishActivity();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                this.f$0.lambda$createView$1(context, view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new AnonymousClass2());
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        if (i == this.selectChatsRow) {
            InviteMembersBottomSheet inviteMembersBottomSheet = new InviteMembersBottomSheet(context, this.currentAccount, null, 0L, this, null);
            inviteMembersBottomSheet.setDelegate(new InviteMembersBottomSheet.InviteMembersBottomSheetDelegate() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.InviteMembersBottomSheet.InviteMembersBottomSheetDelegate
                public final void didSelectDialogs(ArrayList arrayList) {
                    this.f$0.lambda$createView$0(arrayList);
                }
            }, this.selectedDialogs);
            inviteMembersBottomSheet.setSelectedContacts(this.selectedDialogs);
            showDialog(inviteMembersBottomSheet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        this.selectedDialogs.clear();
        this.selectedDialogs.addAll(arrayList);
        updateRows();
        WidgetPreviewCell widgetPreviewCell = this.widgetPreviewCell;
        if (widgetPreviewCell != null) {
            widgetPreviewCell.updateDialogs();
        }
    }

    /* JADX INFO: renamed from: org.telegram.ui.EditWidgetActivity$2, reason: invalid class name */
    class AnonymousClass2 implements RecyclerListView.OnItemLongClickListenerExtended {
        private Rect rect = new Rect();

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onMove(float f, float f2) {
        }

        AnonymousClass2() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, final int i, float f, float f2) {
            if (EditWidgetActivity.this.getParentActivity() != null && (view instanceof GroupCreateUserCell)) {
                ((ImageView) view.getTag(R.id.object_tag)).getHitRect(this.rect);
                if (!this.rect.contains((int) f, (int) f2)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditWidgetActivity.this.getParentActivity());
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Delete)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.EditWidgetActivity$2$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            this.f$0.lambda$onItemClick$0(i, dialogInterface, i2);
                        }
                    });
                    EditWidgetActivity.this.showDialog(builder.create());
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(int i, DialogInterface dialogInterface, int i2) {
            if (i2 == 0) {
                EditWidgetActivity.this.selectedDialogs.remove(i - EditWidgetActivity.this.chatsStartRow);
                EditWidgetActivity.this.updateRows();
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishActivity() {
        if (getParentActivity() == null) {
            return;
        }
        getParentActivity().finish();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.removeSelfFromStack();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditWidgetActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 1 || itemViewType == 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                view = textInfoPrivacyCell;
            } else if (i == 1) {
                TextCell textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                view = textCell;
            } else if (i == 2) {
                EditWidgetActivity editWidgetActivity = EditWidgetActivity.this;
                WidgetPreviewCell widgetPreviewCell = EditWidgetActivity.this.new WidgetPreviewCell(this.mContext);
                editWidgetActivity.widgetPreviewCell = widgetPreviewCell;
                view = widgetPreviewCell;
            } else {
                final GroupCreateUserCell groupCreateUserCell = new GroupCreateUserCell(this.mContext, 0, 0, false);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(R.drawable.list_reorder);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                groupCreateUserCell.setTag(R.id.object_tag, imageView);
                groupCreateUserCell.addView(imageView, LayoutHelper.createFrame(40, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 10.0f, 0.0f, 10.0f, 0.0f));
                imageView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view2, MotionEvent motionEvent) {
                        return this.f$0.lambda$onCreateViewHolder$0(groupCreateUserCell, view2, motionEvent);
                    }
                });
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_pinnedIcon), PorterDuff.Mode.MULTIPLY));
                view = groupCreateUserCell;
            }
            return new RecyclerListView.Holder(view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$0(GroupCreateUserCell groupCreateUserCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            EditWidgetActivity.this.itemTouchHelper.startDrag(EditWidgetActivity.this.listView.getChildViewHolder(groupCreateUserCell));
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == EditWidgetActivity.this.infoRow) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (EditWidgetActivity.this.widgetType == 0) {
                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EditWidgetChatsInfo));
                    } else if (EditWidgetActivity.this.widgetType == 1) {
                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EditWidgetContactsInfo));
                    }
                    if (SharedConfig.passcodeHash.length() > 0) {
                        spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(R.string.WidgetPasscode2)));
                    }
                    textInfoPrivacyCell.setText(spannableStringBuilder);
                    return;
                }
                return;
            }
            if (itemViewType != 1) {
                if (itemViewType != 3) {
                    return;
                }
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                Long l = (Long) EditWidgetActivity.this.selectedDialogs.get(i - EditWidgetActivity.this.chatsStartRow);
                long jLongValue = l.longValue();
                if (DialogObject.isUserDialog(jLongValue)) {
                    groupCreateUserCell.setObject(EditWidgetActivity.this.getMessagesController().getUser(l), null, null, i != EditWidgetActivity.this.chatsEndRow - 1);
                    return;
                } else {
                    groupCreateUserCell.setObject(EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-jLongValue)), null, null, i != EditWidgetActivity.this.chatsEndRow - 1);
                    return;
                }
            }
            TextCell textCell = (TextCell) viewHolder.itemView;
            textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText4);
            Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
            Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
            int color = Theme.getColor(Theme.key_switchTrackChecked);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            drawable.setColorFilter(new PorterDuffColorFilter(color, mode));
            drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), mode));
            textCell.setTextAndIcon(LocaleController.getString(R.string.SelectChats), new CombinedDrawable(drawable, drawable2), EditWidgetActivity.this.chatsStartRow != -1);
            textCell.getImageView().setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 3 || itemViewType == 1) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == EditWidgetActivity.this.previewRow) {
                return 2;
            }
            if (i == EditWidgetActivity.this.selectChatsRow) {
                return 1;
            }
            return i == EditWidgetActivity.this.infoRow ? 0 : 3;
        }

        public boolean swapElements(int i, int i2) {
            int i3 = i - EditWidgetActivity.this.chatsStartRow;
            int i4 = i2 - EditWidgetActivity.this.chatsStartRow;
            int i5 = EditWidgetActivity.this.chatsEndRow - EditWidgetActivity.this.chatsStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= i5 || i4 >= i5) {
                return false;
            }
            Long l = (Long) EditWidgetActivity.this.selectedDialogs.get(i3);
            EditWidgetActivity.this.selectedDialogs.set(i3, (Long) EditWidgetActivity.this.selectedDialogs.get(i4));
            EditWidgetActivity.this.selectedDialogs.set(i4, l);
            notifyItemMoved(i, i2);
            return true;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed(boolean z) {
        if (this.delegate != null) {
            return super.onBackPressed(z);
        }
        if (!z) {
            return false;
        }
        finishActivity();
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i3 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        return arrayList;
    }
}
