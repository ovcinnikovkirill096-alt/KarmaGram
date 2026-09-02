package com.radolyn.ayugram.preferences.utils;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.radolyn.ayugram.utils.filters.AyuFilterUtils;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.StickerImageView;

public class FiltersImportBottomSheet extends BottomSheet {
    public FiltersImportBottomSheet(final BaseFragment baseFragment, final AyuFilterUtils.ApplyChanges applyChanges, final Runnable runnable) {
        super(baseFragment.getParentActivity(), false, baseFragment.getResourceProvider());
        Activity parentActivity = baseFragment.getParentActivity();
        fixNavigationBar();
        StringBuilder sb = new StringBuilder();
        if (!applyChanges.newFilters.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetNewFilters", applyChanges.newFilters.size(), new Object[0]));
            sb.append("\n");
        }
        if (!applyChanges.removeFiltersById.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetRemovedFilters", applyChanges.removeFiltersById.size(), new Object[0]));
            sb.append("\n");
        }
        if (!applyChanges.filtersOverrides.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetUpdatedFilters", applyChanges.filtersOverrides.size(), new Object[0]));
            sb.append("\n");
        }
        if (!applyChanges.newExclusions.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetNewExclusions", applyChanges.newExclusions.size(), new Object[0]));
            sb.append("\n");
        }
        if (!applyChanges.removeExclusions.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetRemovedExclusions", applyChanges.removeExclusions.size(), new Object[0]));
            sb.append("\n");
        }
        if (!applyChanges.peersToBeResolved.isEmpty()) {
            sb.append(LocaleController.formatPluralString("FiltersSheetDialogsToResolve", applyChanges.peersToBeResolved.size(), new Object[0]));
            sb.append("\n");
        }
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        frameLayout.addView(linearLayout);
        StickerImageView stickerImageView = new StickerImageView(parentActivity, this.currentAccount);
        stickerImageView.setStickerPackName("exteraGramPlaceholders");
        stickerImageView.setStickerNum(6);
        stickerImageView.getImageReceiver().setAutoRepeat(1);
        stickerImageView.getImageReceiver().setAutoRepeatCount(1);
        linearLayout.addView(stickerImageView, LayoutHelper.createLinear(144, 144, 1, 0, 16, 0, 0));
        TextView textView = new TextView(parentActivity);
        textView.setGravity(1);
        textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setText(LocaleController.getString(R.string.FiltersSheetTitle));
        linearLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 40.0f, 20.0f, 40.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setGravity(1);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        textView2.setText(AndroidUtilities.replaceTags(sb.toString()));
        linearLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 15.0f, 21.0f, 8.0f));
        TextView textView3 = new TextView(parentActivity);
        ScaleStateListAnimator.apply(textView3, 0.02f, 1.5f);
        textView3.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView3.setGravity(17);
        textView3.setTextSize(1, 14.0f);
        textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView3.setText(LocaleController.getString(R.string.ImportConfirm));
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.FiltersImportBottomSheet$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$0(applyChanges, baseFragment, runnable, view);
            }
        });
        textView3.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
        int iDp = AndroidUtilities.dp(8.0f);
        int i = Theme.key_featuredStickers_addButton;
        textView3.setBackground(Theme.createSimpleSelectorRoundRectDrawable(iDp, getThemedColor(i), ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundWhite), 120)));
        linearLayout.addView(textView3, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 15.0f, 16.0f, 8.0f));
        TextView textView4 = new TextView(parentActivity);
        ScaleStateListAnimator.apply(textView4, 0.02f, 1.5f);
        textView4.setGravity(17);
        textView4.setTextSize(1, 14.0f);
        textView4.setText(LocaleController.getString(R.string.CancelConfirm));
        textView4.setTextColor(getThemedColor(i));
        textView4.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.utils.FiltersImportBottomSheet$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$new$1(view);
            }
        });
        linearLayout.addView(textView4, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 0.0f, 16.0f, 0.0f));
        ScrollView scrollView = new ScrollView(parentActivity);
        scrollView.addView(frameLayout);
        setCustomView(scrollView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(AyuFilterUtils.ApplyChanges applyChanges, BaseFragment baseFragment, Runnable runnable, View view) {
        dismiss();
        try {
            AyuFilterUtils.applyChanges(applyChanges);
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.FiltersToastSuccess)).show();
        } catch (Exception unused) {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.FiltersToastFailImport)).show();
        } finally {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        dismiss();
    }
}
