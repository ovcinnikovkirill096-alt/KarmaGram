package com.radolyn.ayugram.preferences.utils;

import android.view.View;
import com.android.dx.io.Opcodes;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.utils.filters.AyuFilterUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

public abstract class ShadowBanPopup {
    public static void show(BaseFragment baseFragment, View view, float f, float f2, long j) {
        if (baseFragment.getFragmentView() == null) {
            return;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(baseFragment.getContext());
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayoutCreatePopupLayout = createPopupLayout(actionBarPopupWindowLayout, actionBarPopupWindow, baseFragment, j);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(Opcodes.REM_INT_LIT8);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayoutCreatePopupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
        while (view != baseFragment.getFragmentView()) {
            if (view.getParent() == null) {
                return;
            }
            f += view.getX();
            f2 += view.getY();
            view = (View) view.getParent();
        }
        actionBarPopupWindow.showAtLocation(baseFragment.getFragmentView(), 0, (int) (f - (actionBarPopupWindowLayoutCreatePopupLayout.getMeasuredWidth() / 2.0f)), (int) (f2 + (actionBarPopupWindowLayoutCreatePopupLayout.getMeasuredHeight() / 2.0f)));
        actionBarPopupWindow.dimBehind();
    }

    private static ActionBarPopupWindow.ActionBarPopupWindowLayout createPopupLayout(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, final ActionBarPopupWindow actionBarPopupWindow, final BaseFragment baseFragment, final long j) {
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuSubItem actionBarMenuSubItemAddItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.Delete), false, baseFragment.getResourceProvider());
        actionBarMenuSubItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.ShadowBanPopup$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ShadowBanPopup.$r8$lambda$xmGdjm6sRhnreL4uhPW39FE7BgM(j, baseFragment, actionBarPopupWindow, view);
            }
        });
        int color = Theme.getColor(Theme.key_text_RedBold);
        actionBarMenuSubItemAddItem.setColors(color, color);
        return actionBarPopupWindowLayout;
    }

    public static /* synthetic */ void $r8$lambda$xmGdjm6sRhnreL4uhPW39FE7BgM(long j, BaseFragment baseFragment, ActionBarPopupWindow actionBarPopupWindow, View view) {
        AyuFilterUtils.removeShadowBan(j);
        AyuFilterCacheController.rebuildCache();
        baseFragment.onResume();
        actionBarPopupWindow.dismiss();
    }
}
