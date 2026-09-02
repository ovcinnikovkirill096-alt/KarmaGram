package com.radolyn.ayugram.preferences.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.exteragram.messenger.ExteraConfig;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class AccountCell extends FrameLayout {
    private final AvatarDrawable avatarDrawable;
    private final ImageView checkImageView;
    private final BackupImageView imageView;
    private final TextView infoTextView;
    private final SimpleTextView textView;

    public AccountCell(Context context) {
        super(context);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setAvatarType(6);
        avatarDrawable.setColor(-18621, -618956);
        BackupImageView backupImageView = new BackupImageView(context);
        this.imageView = backupImageView;
        backupImageView.setRoundRadius(ExteraConfig.getAvatarCorners(36.0f));
        backupImageView.setImageDrawable(avatarDrawable);
        addView(backupImageView, LayoutHelper.createFrame(36, 36.0f, 51, 10.0f, 10.0f, 0.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.textView = simpleTextView;
        simpleTextView.setTextSize(15);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setEllipsizeByGradient(true);
        simpleTextView.setMaxLines(1);
        simpleTextView.setGravity(51);
        addView(simpleTextView, LayoutHelper.createFrame(-1, -1.0f, 51, 61.0f, 10.0f, 52.0f, 0.0f));
        simpleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        TextView textView = new TextView(context);
        this.infoTextView = textView;
        textView.setTextColor(Theme.getColor(Theme.key_voipgroup_lastSeenText));
        textView.setTextSize(1, 15.0f);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setMaxWidth(AndroidUtilities.dp(320.0f));
        textView.setGravity(51);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 61.0f, 27.0f, 8.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.checkImageView = imageView;
        imageView.setImageResource(R.drawable.account_check);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_menuItemCheck), PorterDuff.Mode.MULTIPLY));
        addView(imageView, LayoutHelper.createFrame(40, -1.0f, 53, 0.0f, 0.0f, 6.0f, 0.0f));
        TLRPC.TL_user tL_user = new TLRPC.TL_user();
        tL_user.id = 0L;
        tL_user.first_name = LocaleController.getString(R.string.GhostModeGlobalSettings);
        textView.setText(LocaleController.getString(R.string.GhostModeGlobalSettingsDescription));
        simpleTextView.setText(LocaleController.getString(R.string.GhostModeGlobalSettings));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.checkImageView != null || (this.infoTextView != null && getLayoutParams().width != -2)) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), TLObject.FLAG_30), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), TLObject.FLAG_30));
        } else {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), TLObject.FLAG_30));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.infoTextView == null) {
            this.textView.setTextColor(Theme.getColor(Theme.key_chats_menuItemText));
        }
    }

    @Override // android.view.View
    public void setSelected(boolean z) {
        this.checkImageView.setVisibility(z ? 0 : 4);
    }

    public AvatarDrawable getAvatarDrawable() {
        return this.avatarDrawable;
    }
}
