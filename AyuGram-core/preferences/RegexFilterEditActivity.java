package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OutlineTextContainerView;

public class RegexFilterEditActivity extends BaseFragment {
    private CheckBoxCell caseInsensitiveCell;
    private ChatActivity chatActivity;
    private final Long dialogId;
    private View doneButton;
    private EditTextBoldCursor editField;
    private OutlineTextContainerView editFieldContainer;
    private CheckBoxCell enabledCell;
    private TextView errorTextView;
    private final RegexFilter filter;
    private final String initialText;
    private CheckBoxCell reversedCell;

    public RegexFilterEditActivity(Long l) {
        this.filter = null;
        this.dialogId = l;
        this.initialText = null;
    }

    public RegexFilterEditActivity(Long l, String str, ChatActivity chatActivity) {
        this.filter = null;
        this.dialogId = l;
        this.initialText = str;
        this.chatActivity = chatActivity;
    }

    public RegexFilterEditActivity(RegexFilter regexFilter) {
        this.filter = regexFilter;
        this.dialogId = regexFilter.dialogId;
        this.initialText = null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString(this.filter == null ? R.string.RegexFiltersAdd : R.string.RegexFiltersEdit));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ActionBarMenu actionBarMenuCreateMenu = this.actionBar.createMenu();
        actionBarMenuCreateMenu.addItem(1, R.drawable.msg_help_14);
        ActionBarMenuItem actionBarMenuItemAddItemWithWidth = actionBarMenuCreateMenu.addItemWithWidth(2, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f));
        this.doneButton = actionBarMenuItemAddItemWithWidth;
        actionBarMenuItemAddItemWithWidth.setContentDescription(LocaleController.getString(R.string.Done));
        LinearLayout linearLayout = new LinearLayout(context);
        this.fragmentView = linearLayout;
        linearLayout.setOrientation(1);
        this.fragmentView.setOnTouchListener(new View.OnTouchListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return RegexFilterEditActivity.$r8$lambda$fEA0teG5P20kk02Zodjdb77BTC8(view, motionEvent);
            }
        });
        OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
        this.editFieldContainer = outlineTextContainerView;
        outlineTextContainerView.setText(LocaleController.getString(R.string.RegexFiltersPlaceholder));
        linearLayout.addView(this.editFieldContainer, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        this.editField = editTextBoldCursor;
        editTextBoldCursor.setTextSize(1, 18.0f);
        this.editField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.editField.setBackground(null);
        this.editField.setImeOptions(268435456);
        this.editField.setImeOptions(6);
        this.editField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated));
        this.editField.setCursorWidth(1.5f);
        this.editField.setGravity(LocaleController.isRTL ? 5 : 3);
        this.editField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                this.f$0.lambda$createView$1(view, z);
            }
        });
        int iDp = AndroidUtilities.dp(16.0f);
        this.editField.setPadding(iDp, iDp, iDp, iDp);
        this.editField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.editField.setInputType(655361);
        this.editFieldContainer.addView(this.editField, LayoutHelper.createFrame(-1, -2.0f));
        this.editFieldContainer.attachEditText(this.editField);
        this.editField.addTextChangedListener(new TextWatcher() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                RegexFilterEditActivity.this.doneButton.setEnabled(!TextUtils.isEmpty(editable));
                if (RegexFilterEditActivity.this.errorTextView != null) {
                    RegexFilterEditActivity.this.errorTextView.setText("");
                    RegexFilterEditActivity.this.errorTextView.setVisibility(8);
                }
            }
        });
        RegexFilter regexFilter = this.filter;
        if (regexFilter != null) {
            this.editField.setText(regexFilter.text);
        } else if (!TextUtils.isEmpty(this.initialText)) {
            this.editField.setText(this.initialText);
        }
        TextView textView = new TextView(context);
        this.errorTextView = textView;
        textView.setFocusable(true);
        this.errorTextView.setTextSize(1, 15.0f);
        this.errorTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText8));
        this.errorTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.errorTextView.setText("");
        this.errorTextView.setVisibility(8);
        linearLayout.addView(this.errorTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 24, 10, 24, 0));
        CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1);
        this.enabledCell = checkBoxCell;
        checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        CheckBoxCell checkBoxCell2 = this.enabledCell;
        String string = LocaleController.getString(R.string.EnableExpression);
        String string2 = "";
        RegexFilter regexFilter2 = this.filter;
        checkBoxCell2.setText(string, string2, regexFilter2 == null || regexFilter2.enabled, true);
        this.enabledCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        this.enabledCell.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$2(view);
            }
        });
        linearLayout.addView(this.enabledCell, LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 0.0f, 0.0f));
        CheckBoxCell checkBoxCell3 = new CheckBoxCell(context, 1);
        this.caseInsensitiveCell = checkBoxCell3;
        checkBoxCell3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        CheckBoxCell checkBoxCell4 = this.caseInsensitiveCell;
        String string3 = LocaleController.getString(R.string.CaseInsensitiveExpression);
        String string4 = "";
        RegexFilter regexFilter3 = this.filter;
        checkBoxCell4.setText(string3, string4, regexFilter3 == null || regexFilter3.caseInsensitive, true);
        this.caseInsensitiveCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        this.caseInsensitiveCell.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$3(view);
            }
        });
        linearLayout.addView(this.caseInsensitiveCell, LayoutHelper.createLinear(-1, -2));
        CheckBoxCell checkBoxCell5 = new CheckBoxCell(context, 1);
        this.reversedCell = checkBoxCell5;
        checkBoxCell5.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        CheckBoxCell checkBoxCell6 = this.reversedCell;
        String string5 = LocaleController.getString(R.string.ReversedExpression);
        String string6 = "";
        RegexFilter regexFilter4 = this.filter;
        checkBoxCell6.setText(string5, string6, regexFilter4 != null && regexFilter4.reversed, false);
        this.reversedCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        this.reversedCell.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$4(view);
            }
        });
        linearLayout.addView(this.reversedCell, LayoutHelper.createLinear(-1, -2));
        return this.fragmentView;
    }

    /* JADX INFO: renamed from: com.radolyn.ayugram.preferences.RegexFilterEditActivity$1, reason: invalid class name */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                RegexFilterEditActivity.this.finishFragment();
                return;
            }
            if (i == 1) {
                String string = "m";
                if (RegexFilterEditActivity.this.caseInsensitiveCell.isChecked()) {
                    string = string + "i";
                }
                final String str = "https://regex101.com/?flags=" + string + "&flavor=java";
                AlertDialog.Builder builder = new AlertDialog.Builder(RegexFilterEditActivity.this.getContext());
                builder.setMessage(AyuUtils.htmlToString(LocaleController.getString(R.string.RegexFiltersAddDescription).replace("https://regex101.com/r/3XHb17", str)));
                builder.setTitle(LocaleController.getString(R.string.RegexFilters));
                builder.setPositiveButton(LocaleController.getString(R.string.RegexFiltersAddAction), new AlertDialog.OnButtonClickListener() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                    public final void onClick(AlertDialog alertDialog, int i2) {
                        this.f$0.lambda$onItemClick$0(str, alertDialog, i2);
                    }
                });
                RegexFilterEditActivity.this.showDialog(builder.create());
                return;
            }
            if (i == 2) {
                String string2 = RegexFilterEditActivity.this.editField.getText().toString();
                if (TextUtils.isEmpty(string2)) {
                    BulletinFactory.of(RegexFilterEditActivity.this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.RegexFiltersAddError)).show();
                    return;
                }
                try {
                    Pattern.compile(string2);
                    final RegexFilter regexFilter = null;
                    if (RegexFilterEditActivity.this.filter == null) {
                        RegexFilter regexFilter2 = new RegexFilter();
                        regexFilter2.text = string2;
                        regexFilter2.enabled = RegexFilterEditActivity.this.enabledCell.isChecked();
                        regexFilter2.reversed = RegexFilterEditActivity.this.reversedCell.isChecked();
                        regexFilter2.caseInsensitive = RegexFilterEditActivity.this.caseInsensitiveCell.isChecked();
                        regexFilter2.dialogId = RegexFilterEditActivity.this.initialText == null ? RegexFilterEditActivity.this.dialogId : null;
                        AyuData.getRegexFilterDao().insert(regexFilter2);
                        regexFilter = regexFilter2;
                    } else {
                        RegexFilterEditActivity.this.filter.text = string2;
                        RegexFilterEditActivity.this.filter.enabled = RegexFilterEditActivity.this.enabledCell.isChecked();
                        RegexFilterEditActivity.this.filter.reversed = RegexFilterEditActivity.this.reversedCell.isChecked();
                        RegexFilterEditActivity.this.filter.caseInsensitive = RegexFilterEditActivity.this.caseInsensitiveCell.isChecked();
                        AyuData.getRegexFilterDao().update(RegexFilterEditActivity.this.filter);
                    }
                    AyuFilterCacheController.rebuildCache();
                    if (RegexFilterEditActivity.this.initialText != null && regexFilter != null) {
                        final Long l = RegexFilterEditActivity.this.dialogId;
                        final ChatActivity chatActivity = RegexFilterEditActivity.this.chatActivity;
                        try {
                            chatActivity.clearSelectionMode();
                        } catch (Throwable unused) {
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$1$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BulletinFactory.of(chatActivity).createSimpleBulletin(R.raw.info, AndroidUtilities.replaceTags(LocaleController.getString(R.string.RegexFilterBulletinText)), LocaleController.getString(R.string.RegexFilterBulletinAction), new Runnable() { // from class: com.radolyn.ayugram.preferences.RegexFilterEditActivity$1$$ExternalSyntheticLambda2
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        RegexFilterEditActivity.AnonymousClass1.$r8$lambda$IZfdp78wD3kjDCNBNQt2FB8_1ws(regexFilter, l);
                                    }
                                }).show();
                            }
                        }, 300L);
                    }
                    RegexFilterEditActivity.this.finishFragment();
                } catch (PatternSyntaxException e) {
                    String message = e.getMessage();
                    if (!TextUtils.isEmpty(message)) {
                        message = message.replace(string2, "");
                    }
                    RegexFilterEditActivity.this.errorTextView.setText(AyuUtils.htmlToString("<b>" + message + "</b>"));
                    RegexFilterEditActivity.this.errorTextView.setVisibility(0);
                    BulletinFactory.of(RegexFilterEditActivity.this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.RegexFiltersAddError)).show();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(String str, AlertDialog alertDialog, int i) {
            Browser.openUrl(RegexFilterEditActivity.this.getParentActivity(), str);
        }

        public static /* synthetic */ void $r8$lambda$IZfdp78wD3kjDCNBNQt2FB8_1ws(RegexFilter regexFilter, Long l) {
            regexFilter.dialogId = l;
            AyuData.getRegexFilterDao().update(regexFilter);
            AyuFilterCacheController.rebuildCache();
        }
    }

    public static /* synthetic */ boolean $r8$lambda$fEA0teG5P20kk02Zodjdb77BTC8(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view, boolean z) {
        this.editFieldContainer.animateSelection(z ? 1.0f : 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        CheckBoxCell checkBoxCell = this.enabledCell;
        checkBoxCell.setChecked(!checkBoxCell.isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        CheckBoxCell checkBoxCell = this.caseInsensitiveCell;
        checkBoxCell.setChecked(!checkBoxCell.isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        CheckBoxCell checkBoxCell = this.reversedCell;
        checkBoxCell.setChecked(!checkBoxCell.isChecked(), true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        if (MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            return;
        }
        this.editField.requestFocus();
        AndroidUtilities.showKeyboard(this.editField);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.editField.requestFocus();
            AndroidUtilities.showKeyboard(this.editField);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.editField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.editField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(this.editField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(this.editField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        return arrayList;
    }
}
