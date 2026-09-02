package com.radolyn.ayugram.preferences.utils;

import android.view.View;
import com.android.dx.io.Opcodes;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.preferences.RegexFilterEditActivity;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public abstract class RegexFilterPopup {
    public static void show(BaseFragment baseFragment, View view, float f, float f2, RegexFilter regexFilter, Long l) {
        if (baseFragment.getFragmentView() == null) {
            return;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(baseFragment.getContext());
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayoutCreateExclusionPopupLayout = l != null ? createExclusionPopupLayout(actionBarPopupWindowLayout, actionBarPopupWindow, baseFragment, regexFilter, l) : createPopupLayout(actionBarPopupWindowLayout, actionBarPopupWindow, baseFragment, regexFilter);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(Opcodes.REM_INT_LIT8);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayoutCreateExclusionPopupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
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
        actionBarPopupWindow.showAtLocation(baseFragment.getFragmentView(), 0, (int) (f - (actionBarPopupWindowLayoutCreateExclusionPopupLayout.getMeasuredWidth() / 2.0f)), (int) (f2 + (actionBarPopupWindowLayoutCreateExclusionPopupLayout.getMeasuredHeight() / 2.0f)));
        actionBarPopupWindow.dimBehind();
    }

    private static ActionBarPopupWindow.ActionBarPopupWindowLayout createPopupLayout(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, final ActionBarPopupWindow actionBarPopupWindow, final BaseFragment baseFragment, final RegexFilter regexFilter) {
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_edit, LocaleController.getString(R.string.Edit), false, baseFragment.getResourceProvider()).setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.RegexFilterPopup$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RegexFilterPopup.$r8$lambda$rZppBxih5qIVEh0C6VrliZNfVWs(baseFragment, regexFilter, actionBarPopupWindow, view);
            }
        });
        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, regexFilter.enabled ? R.drawable.msg_noise_off : R.drawable.msg_noise_on, LocaleController.getString(regexFilter.enabled ? R.string.Disable : R.string.Enable), false, baseFragment.getResourceProvider()).setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.RegexFilterPopup$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RegexFilterPopup.$r8$lambda$akZiMoef257BbHPp3mNKaKy59ow(regexFilter, baseFragment, actionBarPopupWindow, view);
            }
        });
        ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(actionBarPopupWindowLayout.getContext(), baseFragment.getResourceProvider());
        gapView.setTag(R.id.fit_width_tag, 1);
        actionBarPopupWindowLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
        ActionBarMenuSubItem actionBarMenuSubItemAddItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.Delete), false, baseFragment.getResourceProvider());
        actionBarMenuSubItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.RegexFilterPopup$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RegexFilterPopup.$r8$lambda$YdFtpj0QasXPgaC3c0be0laHfD8(regexFilter, baseFragment, actionBarPopupWindow, view);
            }
        });
        int color = Theme.getColor(Theme.key_text_RedBold);
        actionBarMenuSubItemAddItem.setColors(color, color);
        return actionBarPopupWindowLayout;
    }

    public static /* synthetic */ void $r8$lambda$rZppBxih5qIVEh0C6VrliZNfVWs(BaseFragment baseFragment, RegexFilter regexFilter, ActionBarPopupWindow actionBarPopupWindow, View view) {
        baseFragment.presentFragment(new RegexFilterEditActivity(regexFilter));
        actionBarPopupWindow.dismiss();
    }

    public static /* synthetic */ void $r8$lambda$akZiMoef257BbHPp3mNKaKy59ow(RegexFilter regexFilter, BaseFragment baseFragment, ActionBarPopupWindow actionBarPopupWindow, View view) {
        regexFilter.enabled = !regexFilter.enabled;
        AyuData.getRegexFilterDao().update(regexFilter);
        AyuFilterCacheController.rebuildCache();
        baseFragment.onResume();
        actionBarPopupWindow.dismiss();
    }

    public static /* synthetic */ void $r8$lambda$YdFtpj0QasXPgaC3c0be0laHfD8(RegexFilter regexFilter, BaseFragment baseFragment, ActionBarPopupWindow actionBarPopupWindow, View view) {
        AyuData.getRegexFilterDao().delete(regexFilter.id);
        AyuData.getRegexFilterDao().deleteExclusionsByFilterId(regexFilter.id);
        AyuFilterCacheController.rebuildCache();
        baseFragment.onResume();
        actionBarPopupWindow.dismiss();
    }

    private static ActionBarPopupWindow.ActionBarPopupWindowLayout createExclusionPopupLayout(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, final ActionBarPopupWindow actionBarPopupWindow, final BaseFragment baseFragment, final RegexFilter regexFilter, final Long l) {
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuSubItem actionBarMenuSubItemAddItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.Delete), false, baseFragment.getResourceProvider());
        actionBarMenuSubItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.RegexFilterPopup$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RegexFilterPopup.$r8$lambda$0sOy6D26eGQwzxYwbibcUuuIeBA(l, regexFilter, baseFragment, actionBarPopupWindow, view);
            }
        });
        int color = Theme.getColor(Theme.key_text_RedBold);
        actionBarMenuSubItemAddItem.setColors(color, color);
        return actionBarPopupWindowLayout;
    }

    public static /* synthetic */ void $r8$lambda$0sOy6D26eGQwzxYwbibcUuuIeBA(Long l, RegexFilter regexFilter, BaseFragment baseFragment, ActionBarPopupWindow actionBarPopupWindow, View view) {
        AyuData.getRegexFilterDao().deleteExclusion(l.longValue(), regexFilter.id);
        AyuFilterCacheController.rebuildCache();
        baseFragment.onResume();
        actionBarPopupWindow.dismiss();
    }
}
