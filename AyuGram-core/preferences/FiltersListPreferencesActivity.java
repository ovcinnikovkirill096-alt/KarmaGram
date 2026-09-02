package com.radolyn.ayugram.preferences;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.google.android.exoplayer2.util.Consumer;
import com.radolyn.ayugram.controllers.AyuFilterCacheController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import com.radolyn.ayugram.preferences.utils.RegexFilterPopup;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import j$.util.Collection;
import j$.util.function.Predicate$CC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

public class FiltersListPreferencesActivity extends BasePreferencesActivity {
    private ActionBarMenuItem addItem;
    private final Long dialogId;
    private ActionBarMenuItem excludeItem;
    private List exclusions;
    private List filters;
    private Consumer filtersReducer;
    private Consumer onFilterChosen;
    private final Map idToFilter = new HashMap();
    private final Map idToExclusion = new HashMap();

    public FiltersListPreferencesActivity(Long l) {
        this.dialogId = l;
    }

    public void setIsChooseMode(Consumer consumer) {
        this.onFilterChosen = consumer;
    }

    public void setReduce(Consumer consumer) {
        this.filtersReducer = consumer;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        ActionBarMenu actionBarMenuCreateMenu = this.actionBar.createMenu();
        ActionBarMenuItem actionBarMenuItemAddItem = actionBarMenuCreateMenu.addItem(0, R.drawable.msg_blur_radial);
        this.excludeItem = actionBarMenuItemAddItem;
        actionBarMenuItemAddItem.setVisibility(0);
        this.excludeItem.setTag(null);
        this.excludeItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersListPreferencesActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$3(view);
            }
        });
        ActionBarMenuItem actionBarMenuItemAddItem2 = actionBarMenuCreateMenu.addItem(1, R.drawable.msg_add);
        this.addItem = actionBarMenuItemAddItem2;
        actionBarMenuItemAddItem2.setContentDescription(LocaleController.getString(R.string.Add));
        this.addItem.setVisibility(0);
        this.addItem.setTag(null);
        this.addItem.setOnClickListener(new View.OnClickListener() { // from class: com.radolyn.ayugram.preferences.FiltersListPreferencesActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$createView$4(view);
            }
        });
        if (this.dialogId == null) {
            this.excludeItem.setVisibility(8);
        }
        if (this.onFilterChosen != null) {
            this.addItem.setVisibility(8);
            this.excludeItem.setVisibility(8);
        }
        return viewCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        FiltersListPreferencesActivity filtersListPreferencesActivity = new FiltersListPreferencesActivity(null);
        filtersListPreferencesActivity.setIsChooseMode(new Consumer() { // from class: com.radolyn.ayugram.preferences.FiltersListPreferencesActivity$$ExternalSyntheticLambda2
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$createView$0((RegexFilter) obj);
            }
        });
        if (this.exclusions != null) {
            filtersListPreferencesActivity.setReduce(new Consumer() { // from class: com.radolyn.ayugram.preferences.FiltersListPreferencesActivity$$ExternalSyntheticLambda3
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$createView$2((List) obj);
                }
            });
        }
        presentFragment(filtersListPreferencesActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(RegexFilter regexFilter) {
        RegexFilterGlobalExclusion regexFilterGlobalExclusion = new RegexFilterGlobalExclusion();
        regexFilterGlobalExclusion.dialogId = this.dialogId.longValue();
        regexFilterGlobalExclusion.filterId = regexFilter.id;
        AyuData.getRegexFilterDao().insertExclusion(regexFilterGlobalExclusion);
        AyuFilterCacheController.rebuildCache();
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(List list) {
        for (final RegexFilter regexFilter : this.exclusions) {
            Collection.EL.removeIf(list, new Predicate() { // from class: com.radolyn.ayugram.preferences.FiltersListPreferencesActivity$$ExternalSyntheticLambda4
                public /* synthetic */ Predicate and(Predicate predicate) {
                    return Predicate$CC.$default$and(this, predicate);
                }

                public /* synthetic */ Predicate negate() {
                    return Predicate$CC.$default$negate(this);
                }

                public /* synthetic */ Predicate or(Predicate predicate) {
                    return Predicate$CC.$default$or(this, predicate);
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((RegexFilter) obj).id.equals(regexFilter.id);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        presentFragment(new RegexFilterEditActivity(this.dialogId));
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        if (this.dialogId == null) {
            this.filters = AyuData.getRegexFilterDao().getShared();
            this.exclusions = null;
        } else {
            this.filters = AyuData.getRegexFilterDao().getByDialogId(this.dialogId.longValue());
            this.exclusions = AyuData.getRegexFilterDao().getExcludedByDialogId(this.dialogId.longValue());
        }
        Consumer consumer = this.filtersReducer;
        if (consumer != null) {
            consumer.accept(this.filters);
        }
        this.idToFilter.clear();
        this.idToExclusion.clear();
        List list = this.filters;
        if (list != null && !list.isEmpty()) {
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.RegexFiltersHeader)));
            for (int i = 0; i < this.filters.size(); i++) {
                RegexFilter regexFilter = (RegexFilter) this.filters.get(i);
                int i2 = i + 20000;
                this.idToFilter.put(Integer.valueOf(i2), regexFilter);
                UItem uItemAsButton = UItem.asButton(i2, regexFilter.text);
                if (!regexFilter.enabled) {
                    uItemAsButton.gray();
                }
                arrayList.add(uItemAsButton);
            }
        }
        List list2 = this.exclusions;
        if (list2 != null && !list2.isEmpty()) {
            List list3 = this.filters;
            if (list3 != null && !list3.isEmpty()) {
                arrayList.add(UItem.asShadow());
            }
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.RegexFiltersExcluded)));
            for (int i3 = 0; i3 < this.exclusions.size(); i3++) {
                RegexFilter regexFilter2 = (RegexFilter) this.exclusions.get(i3);
                int i4 = i3 + 30000;
                this.idToExclusion.put(Integer.valueOf(i4), regexFilter2);
                arrayList.add(UItem.asButton(i4, regexFilter2.text));
            }
        }
        List list4 = this.filters;
        if (list4 == null || list4.isEmpty()) {
            List list5 = this.exclusions;
            if (list5 == null || list5.isEmpty()) {
                arrayList.add(UItem.asShadow(LocaleController.getString(R.string.RegexFiltersListEmpty)));
            }
        }
    }

    private boolean isExclusionItem(int i) {
        return i >= 30000 && this.idToExclusion.containsKey(Integer.valueOf(i));
    }

    private RegexFilter getFilterById(int i) {
        if (this.idToFilter.containsKey(Integer.valueOf(i))) {
            return (RegexFilter) this.idToFilter.get(Integer.valueOf(i));
        }
        if (this.idToExclusion.containsKey(Integer.valueOf(i))) {
            return (RegexFilter) this.idToExclusion.get(Integer.valueOf(i));
        }
        return null;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        RegexFilter filterById;
        if (uItem == null || (filterById = getFilterById(uItem.id)) == null) {
            return;
        }
        Consumer consumer = this.onFilterChosen;
        if (consumer != null) {
            consumer.accept(filterById);
            finishFragment();
        } else {
            RegexFilterPopup.show(this, view, f, f2, filterById, isExclusionItem(uItem.id) ? this.dialogId : null);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    protected boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        if (uItem == null || this.onFilterChosen == null || isExclusionItem(uItem.id)) {
            return super.onLongClick(uItem, view, i, f, f2);
        }
        RegexFilter filterById = getFilterById(uItem.id);
        if (filterById == null) {
            return super.onLongClick(uItem, view, i, f, f2);
        }
        RegexFilterPopup.show(this, view, f, f2, filterById, null);
        return true;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView != null) {
            universalRecyclerView.adapter.update(false);
        }
        super.onResume();
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        Long l = this.dialogId;
        if (l == null) {
            return LocaleController.getString(R.string.RegexFiltersShared);
        }
        String name = ContactsController.formatName(AyuMessageUtils.getDialogInAnyWay(l.longValue(), Integer.valueOf(UserConfig.selectedAccount)));
        return TextUtils.isEmpty(name) ? "?" : AyuMessageUtils.shortify(name, 20).toString();
    }
}
