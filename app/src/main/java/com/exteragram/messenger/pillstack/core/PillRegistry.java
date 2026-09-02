package com.exteragram.messenger.pillstack.core;

import android.content.Context;
import androidx.annotation.Keep;
import com.android.tools.r8.RecordTag;
import com.exteragram.messenger.ai.ui.GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0;
import com.exteragram.messenger.pillstack.ui.pills.BasePill;
import com.exteragram.messenger.pillstack.ui.pills.crypto.BtcPill;
import com.exteragram.messenger.pillstack.ui.pills.crypto.TonPill;
import com.exteragram.messenger.pillstack.ui.pills.crypto.UsdPill;
import com.exteragram.messenger.pillstack.ui.pills.system.CachePill;
import com.exteragram.messenger.pillstack.ui.pills.system.ProxyPill;
import com.exteragram.messenger.pillstack.ui.pills.weather.WeatherPill;
import com.radolyn.ayugram.ui.LastSeenPill;
import j$.util.Objects;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;

public class PillRegistry {
    private static boolean batchRegistration;
    private static final Map registry = new LinkedHashMap();

    public interface PillCreator {
        BasePill create(Context context, Theme.ResourcesProvider resourcesProvider);
    }

    public static final class PillInfo extends RecordTag {
        private final PillCreator creator;
        private final int iconColorBottom;
        private final int iconColorTop;
        private final int iconRes;
        private final int id;
        private final CharSequence name;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof PillInfo)) {
                return false;
            }
            PillInfo pillInfo = (PillInfo) obj;
            return this.id == pillInfo.id && this.iconRes == pillInfo.iconRes && this.iconColorTop == pillInfo.iconColorTop && this.iconColorBottom == pillInfo.iconColorBottom && Objects.equals(this.name, pillInfo.name) && Objects.equals(this.creator, pillInfo.creator);
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.id), this.name, Integer.valueOf(this.iconRes), Integer.valueOf(this.iconColorTop), Integer.valueOf(this.iconColorBottom), this.creator};
        }

        public PillInfo(int i, CharSequence charSequence, int i2, int i3, int i4, PillCreator pillCreator) {
            this.id = i;
            this.name = charSequence;
            this.iconRes = i2;
            this.iconColorTop = i3;
            this.iconColorBottom = i4;
            this.creator = pillCreator;
        }

        public PillCreator creator() {
            return this.creator;
        }

        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public final int hashCode() {
            return PillRegistry$PillInfo$$ExternalSyntheticRecord0.m(this.id, this.iconRes, this.iconColorTop, this.iconColorBottom, this.name, this.creator);
        }

        public int iconColorBottom() {
            return this.iconColorBottom;
        }

        public int iconColorTop() {
            return this.iconColorTop;
        }

        public int iconRes() {
            return this.iconRes;
        }

        public int id() {
            return this.id;
        }

        public CharSequence name() {
            return this.name;
        }

        public final String toString() {
            return GenerateFromMessageBottomSheet$GenerationData$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), PillInfo.class, "id;name;iconRes;iconColorTop;iconColorBottom;creator");
        }
    }

    static {
        beginTransaction();
        registerDefaultPills();
        endTransaction();
    }

    @Keep
    public static void beginTransaction() {
        batchRegistration = true;
    }

    @Keep
    public static void endTransaction() {
        batchRegistration = false;
        if (PillStackConfig.configLoaded) {
            PillStackConfig.sanitizePills();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pillStackLayoutChanged, new Object[0]);
                }
            });
        }
    }

    private static void registerDefaultPills() {
        register(new PillInfo(PillStackConfig.PillType.WEATHER.id, LocaleController.getString(R.string.WeatherPill), R.drawable.weather_cloudy, -10893326, -12933400, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda2
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new WeatherPill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.TON.id, "TON", R.drawable.settings_ton, -14965523, -15431455, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda3
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new TonPill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.BTC.id, "BTC", R.drawable.pillstack_btc_settings, -1071598, -1608430, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda4
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new BtcPill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.USD.id, "USD", R.drawable.pillstack_usd_settings, -14840995, -15172775, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda5
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new UsdPill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.CACHE.id, LocaleController.getString(R.string.StorageUsage), R.drawable.msg_filled_storageusage, -11565578, -13276952, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda6
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new CachePill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.PROXY.id, LocaleController.getString(R.string.Proxy), R.drawable.drawer_proxy_on, -11154873, -14175180, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda7
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new ProxyPill(context, resourcesProvider);
            }
        }));
        register(new PillInfo(PillStackConfig.PillType.LAST_SEEN.id, LocaleController.getString(R.string.PremiumPreviewLastSeen), R.drawable.menu_premium_seen, -7434605, -10263706, new PillCreator() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda8
            @Override // com.exteragram.messenger.pillstack.core.PillRegistry.PillCreator
            public final BasePill create(Context context, Theme.ResourcesProvider resourcesProvider) {
                return new LastSeenPill(context, resourcesProvider);
            }
        }));
    }

    public static void register(PillInfo pillInfo) {
        registry.put(Integer.valueOf(pillInfo.id), pillInfo);
        if (batchRegistration) {
            return;
        }
        PillStackConfig.sanitizePills();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pillStackLayoutChanged, new Object[0]);
            }
        });
    }

    @Keep
    public static void activatePill(int i) {
        if (isRegistered(i) && !PillStackConfig.activePills.contains(Integer.valueOf(i))) {
            PillStackConfig.hiddenPills.remove(Integer.valueOf(i));
            PillStackConfig.activePills.add(Integer.valueOf(i));
            PillStackConfig.savePillsLayout();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pillStackLayoutChanged, new Object[0]);
                }
            });
        }
    }

    public static PillInfo getPillInfo(int i) {
        return (PillInfo) registry.get(Integer.valueOf(i));
    }

    public static Collection getRegisteredPills() {
        return registry.values();
    }

    public static boolean isRegistered(int i) {
        return registry.containsKey(Integer.valueOf(i));
    }

    @Keep
    public static void unregister(int i) {
        if (registry.remove(Integer.valueOf(i)) == null || batchRegistration) {
            return;
        }
        PillStackConfig.sanitizePills();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.pillstack.core.PillRegistry$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pillStackLayoutChanged, new Object[0]);
            }
        });
    }
}
