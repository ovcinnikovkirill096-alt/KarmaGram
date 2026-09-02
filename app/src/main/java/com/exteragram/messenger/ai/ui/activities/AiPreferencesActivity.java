package com.exteragram.messenger.ai.ui.activities;

import android.text.TextUtils;
import android.view.View;
import com.exteragram.messenger.ai.AiConfig;
import com.exteragram.messenger.ai.AiController;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.google.android.exoplayer2.util.Consumer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

public class AiPreferencesActivity extends BasePreferencesActivity {
    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean hasWhiteActionBar() {
        return true;
    }

    public enum PreferenceItem {
        ENDPOINT,
        ROLE,
        SAVE_HISTORY,
        RESPONSE_STREAMING,
        SHOW_RESPONSE_ONLY,
        INSERT_AS_QUOTE;

        public int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return LocaleController.getString(R.string.AIChat);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        arrayList.add(UItem.asTopView(null, "exteraGramPlaceholders", "🤖"));
        arrayList.add(UItem.asShadow());
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.General)));
        arrayList.add(UItem.asButton(PreferenceItem.ENDPOINT.getId(), R.drawable.msg_language, LocaleController.getString(R.string.Services), getEndpointValue()).prioritizeTitleOverValue(true).setSearchable(this).setLinkAlias("aiServices", this));
        arrayList.add(UItem.asButton(PreferenceItem.ROLE.getId(), R.drawable.msg_openprofile, LocaleController.getString(R.string.Roles), AiConfig.getSelectedRole()).prioritizeTitleOverValue(true).setSearchable(this).setLinkAlias("aiRoles", this));
        arrayList.add(UItem.asCheck(PreferenceItem.SAVE_HISTORY.getId(), LocaleController.getString(R.string.MessageHistory), R.drawable.msg_discuss).setChecked(AiConfig.saveHistory).showDivider(false).setSearchable(this).setLinkAlias("saveAiHistory", this));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.HistoryInfo)));
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.LocalOther)));
        arrayList.add(UItem.asCheck(PreferenceItem.RESPONSE_STREAMING.getId(), LocaleController.getString(R.string.ResponseStreaming), LocaleController.getString(R.string.ResponseStreamingInfo), true).setChecked(AiConfig.responseStreaming).setSearchable(this).setLinkAlias("responseStreaming", this));
        arrayList.add(UItem.asCheck(PreferenceItem.SHOW_RESPONSE_ONLY.getId(), LocaleController.getString(R.string.ShowResponseOnly)).setChecked(AiConfig.showResponseOnly).setSearchable(this).setLinkAlias("showResponseOnly", this));
        arrayList.add(UItem.asCheck(PreferenceItem.INSERT_AS_QUOTE.getId(), LocaleController.getString(R.string.InsertResponseAsQuote)).setChecked(AiConfig.insertAsQuote).showDivider(false).setSearchable(this).setLinkAlias("insertResponseAsQuote", this));
        arrayList.add(UItem.asShadow());
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        int i2 = uItem.id;
        if (i2 <= 0 || i2 > PreferenceItem.values().length) {
            return;
        }
        switch (AnonymousClass1.$SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.values()[uItem.id - 1].ordinal()]) {
            case 1:
                presentFragment(new ServicesActivity());
                break;
            case 2:
                presentFragment(new RolesActivity());
                break;
            case 3:
                toggleBooleanSettingAndRefresh(AiConfig.preferences, "saveHistory", uItem, new Consumer() { // from class: com.exteragram.messenger.ai.ui.activities.AiPreferencesActivity$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AiConfig.saveHistory = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 4:
                toggleBooleanSettingAndRefresh(AiConfig.preferences, "responseStreaming", uItem, new Consumer() { // from class: com.exteragram.messenger.ai.ui.activities.AiPreferencesActivity$$ExternalSyntheticLambda1
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AiConfig.responseStreaming = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 5:
                toggleBooleanSettingAndRefresh(AiConfig.preferences, "showResponseOnly", uItem, new Consumer() { // from class: com.exteragram.messenger.ai.ui.activities.AiPreferencesActivity$$ExternalSyntheticLambda2
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AiConfig.showResponseOnly = ((Boolean) obj).booleanValue();
                    }
                });
                break;
            case 6:
                toggleBooleanSettingAndRefresh(AiConfig.preferences, "insertAsQuote", uItem, new Consumer() { // from class: com.exteragram.messenger.ai.ui.activities.AiPreferencesActivity$$ExternalSyntheticLambda3
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        AiConfig.insertAsQuote = ((Boolean) obj).booleanValue();
                    }
                });
                break;
        }
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.ai.ui.activities.AiPreferencesActivity$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem;

        static {
            int[] iArr = new int[PreferenceItem.values().length];
            $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem = iArr;
            try {
                iArr[PreferenceItem.ENDPOINT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.ROLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.SAVE_HISTORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.RESPONSE_STREAMING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.SHOW_RESPONSE_ONLY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$exteragram$messenger$ai$ui$activities$AiPreferencesActivity$PreferenceItem[PreferenceItem.INSERT_AS_QUOTE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private String getEndpointValue() {
        try {
            String host = new URL(AiController.getInstance().getSelected().getUrl()).getHost();
            if (!TextUtils.isEmpty(host) && AiController.canUseAI()) {
                return host.contains("generativelanguage.googleapis") ? "Gemini" : host;
            }
            return LocaleController.getString(R.string.BlockedEmpty);
        } catch (MalformedURLException unused) {
            return LocaleController.getString(R.string.BlockedEmpty);
        }
    }
}
