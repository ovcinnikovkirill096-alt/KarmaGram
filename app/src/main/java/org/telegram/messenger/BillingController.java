package org.telegram.messenger;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.appcompat.app.WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.core.util.Consumer;
import androidx.core.util.Pair;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.QueryProductDetailsParams$Product;
import j$.util.concurrent.ConcurrentHashMap;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.internal.url._UrlKt;
import org.telegram.PhoneFormat.CallingCodeInfo;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.utils.BillingUtilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.PremiumPreviewFragment;

public class BillingController {
    public static final QueryProductDetailsParams$Product PREMIUM_PRODUCT = QueryProductDetailsParams$Product.newBuilder().setProductType("subs").setProductId("telegram_premium").build();
    public static ProductDetails PREMIUM_PRODUCT_DETAILS = null;
    public static final String PREMIUM_PRODUCT_ID = "telegram_premium";
    public static boolean billingClientEmpty;
    private static NumberFormat currencyInstance;
    private static NumberFormat currencyInstanceRounded;
    private static BillingController instance;
    private boolean isDisconnected;
    private String lastPremiumToken;
    private String lastPremiumTransaction;
    private Runnable onCanceled;
    private final Map<String, Consumer> resultListeners = new HashMap();
    private final Set<String> requestingTokens = Collections.newSetFromMap(new ConcurrentHashMap());
    private final Map<String, Integer> currencyExpMap = new HashMap();
    private ArrayList<Runnable> setupListeners = new ArrayList<>();
    private int triesLeft = 0;

    public boolean isReady() {
        return false;
    }

    public void queryPurchases(String str, PurchasesResponseListener purchasesResponseListener) {
    }

    public static BillingController getInstance() {
        if (instance == null) {
            instance = new BillingController(ApplicationLoader.applicationContext);
        }
        return instance;
    }

    private BillingController(Context context) {
    }

    public void setOnCanceled(Runnable runnable) {
        this.onCanceled = runnable;
    }

    public String getLastPremiumTransaction() {
        return this.lastPremiumTransaction;
    }

    public String getLastPremiumToken() {
        return this.lastPremiumToken;
    }

    public String formatCurrency(long j, String str) {
        return formatCurrency(j, str, getCurrencyExp(str));
    }

    public String formatCurrency(long j, String str, int i) {
        return formatCurrency(j, str, i, false);
    }

    public String formatCurrency(long j, String str, int i, boolean z) {
        if (str == null || str.isEmpty()) {
            return String.valueOf(j);
        }
        if ("TON".equalsIgnoreCase(str)) {
            return "TON " + (j / 1.0E9d);
        }
        if ("XTR".equalsIgnoreCase(str)) {
            return "XTR " + LocaleController.formatNumber(j, ',');
        }
        try {
            Currency currency = Currency.getInstance(str);
            if (currency != null) {
                if (currencyInstance == null) {
                    currencyInstance = NumberFormat.getCurrencyInstance();
                }
                currencyInstance.setCurrency(currency);
                if (z) {
                    currencyInstance.setMaximumFractionDigits(0);
                    currencyInstance.setMinimumFractionDigits(0);
                    return currencyInstance.format(Math.round(j / Math.pow(10.0d, i)));
                }
                int defaultFractionDigits = currency.getDefaultFractionDigits();
                currencyInstance.setMinimumFractionDigits(defaultFractionDigits);
                currencyInstance.setMaximumFractionDigits(defaultFractionDigits);
                return currencyInstance.format(j / Math.pow(10.0d, i));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return j + " " + str;
    }

    public int getCurrencyExp(String str) {
        BillingUtilities.extractCurrencyExp(this.currencyExpMap);
        return ((Integer) j$.util.Map.EL.getOrDefault(this.currencyExpMap, str, 0)).intValue();
    }

    public String getTargetCurrency(int i, boolean z) {
        if (z) {
            return "USD";
        }
        return getCurrencyByPhone(UserConfig.getInstance(i).getClientPhone());
    }

    private String getCurrencyByPhone(String str) {
        CallingCodeInfo callingCodeInfoFindCallingCodeInfo;
        ArrayList arrayList;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String strStripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
        if (!TextUtils.isEmpty(strStripExceptNumbers) && (callingCodeInfoFindCallingCodeInfo = PhoneFormat.getInstance().findCallingCodeInfo(strStripExceptNumbers)) != null && (arrayList = callingCodeInfoFindCallingCodeInfo.countries) != null && !arrayList.isEmpty()) {
            String str2 = (String) callingCodeInfoFindCallingCodeInfo.countries.get(0);
            String country = Locale.getDefault().getCountry();
            if (!TextUtils.isEmpty(country)) {
                ArrayList arrayList2 = callingCodeInfoFindCallingCodeInfo.countries;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList2.get(i);
                    i++;
                    String str3 = (String) obj;
                    if (country.equalsIgnoreCase(str3)) {
                        str2 = str3;
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(str2) && str2.charAt(0) != '_') {
                try {
                    Currency currency = Currency.getInstance(new Locale(_UrlKt.FRAGMENT_ENCODE_SET, str2.toUpperCase(Locale.ROOT)));
                    if (currency != null) {
                        return currency.getCurrencyCode();
                    }
                    return null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
        return null;
    }

    /* JADX INFO: renamed from: startConnection, reason: merged with bridge method [inline-methods] */
    public void lambda$onBillingServiceDisconnected$10() {
        if (isReady()) {
            return;
        }
        try {
            BillingUtilities.extractCurrencyExp(this.currencyExpMap);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void switchToInvoice() {
        if (billingClientEmpty) {
            return;
        }
        billingClientEmpty = true;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.billingProductDetailsUpdated, new Object[0]);
    }

    private void switchBackFromInvoice() {
        if (billingClientEmpty) {
            billingClientEmpty = false;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.billingProductDetailsUpdated, new Object[0]);
        }
    }

    public void queryProductDetails(List<QueryProductDetailsParams$Product> list, ProductDetailsResponseListener productDetailsResponseListener) {
        if (!isReady()) {
            throw new IllegalStateException("Billing: Controller should be ready for this call!");
        }
    }

    public boolean startManageSubscription(Context context, String str) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("https://play.google.com/store/account/subscriptions?sku=%s&package=%s", str, context.getPackageName()))));
            return true;
        } catch (ActivityNotFoundException unused) {
            return false;
        }
    }

    public void addResultListener(String str, Consumer consumer) {
        this.resultListeners.put(str, consumer);
    }

    public void launchBillingFlow(Activity activity, AccountInstance accountInstance, TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, List<Object> list) {
        launchBillingFlow(activity, accountInstance, inputStorePaymentPurpose, list, null, false);
    }

    public void launchBillingFlow(final Activity activity, final AccountInstance accountInstance, final TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, final List<Object> list, final BillingFlowParams.SubscriptionUpdateParams subscriptionUpdateParams, boolean z) {
        if (!isReady() || activity == null) {
            return;
        }
        if (((inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentGiftPremium) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentStarsTopup) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentStarsGift)) && !z) {
            FileLog.d("BillingController.launchBillingFlow, checking consumables");
            queryPurchases("inapp", new PurchasesResponseListener() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda6
            });
            return;
        }
        if (z) {
            FileLog.d("BillingController.launchBillingFlow, consumables checked, launching flow...");
        }
        Pair pairCreateDeveloperPayload = BillingUtilities.createDeveloperPayload(inputStorePaymentPurpose, accountInstance);
        String str = (String) pairCreateDeveloperPayload.first;
        BillingFlowParams.Builder productDetailsParamsList = BillingFlowParams.newBuilder().setObfuscatedAccountId(str).setObfuscatedProfileId((String) pairCreateDeveloperPayload.second).setProductDetailsParamsList(list);
        if (subscriptionUpdateParams != null) {
            productDetailsParamsList.setSubscriptionUpdateParams(subscriptionUpdateParams);
        }
    }

    private /* synthetic */ void lambda$launchBillingFlow$2(final Activity activity, final AccountInstance accountInstance, final TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, final List list, final BillingFlowParams.SubscriptionUpdateParams subscriptionUpdateParams, BillingResult billingResult, List list2) {
        if (billingResult.getResponseCode() == 0) {
            FileLog.d("BillingController.launchBillingFlow, checked consumables: OK");
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$launchBillingFlow$0(activity, accountInstance, inputStorePaymentPurpose, list, subscriptionUpdateParams);
                }
            };
            AtomicInteger atomicInteger = new AtomicInteger(0);
            new ArrayList();
            Iterator it = list2.iterator();
            if (it.hasNext()) {
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                throw null;
            }
            if (atomicInteger.get() == 0) {
                runnable.run();
                return;
            }
            return;
        }
        FileLog.d("BillingController.launchBillingFlow, checked consumables: " + billingResult.getResponseCode() + " " + billingResult.getDebugMessage());
        launchBillingFlow(activity, accountInstance, inputStorePaymentPurpose, list, subscriptionUpdateParams, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$launchBillingFlow$0(Activity activity, AccountInstance accountInstance, TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, List list, BillingFlowParams.SubscriptionUpdateParams subscriptionUpdateParams) {
        launchBillingFlow(activity, accountInstance, inputStorePaymentPurpose, list, subscriptionUpdateParams, true);
    }

    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
        onPurchasesUpdatedInternal(billingResult, list, null);
    }

    public void onPurchasesUpdatedInternal(BillingResult billingResult, List<Purchase> list, Runnable runnable) {
        FileLog.d("Billing: Purchases updated: " + billingResult + ", " + list);
        if (billingResult.getResponseCode() != 0) {
            if (billingResult.getResponseCode() == 1) {
                PremiumPreviewFragment.sentPremiumBuyCanceled();
            }
            Runnable runnable2 = this.onCanceled;
            if (runnable2 != null) {
                runnable2.run();
                this.onCanceled = null;
            }
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        if (list == null || list.isEmpty()) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        AtomicInteger atomicInteger = new AtomicInteger(0);
        new AtomicInteger(0);
        this.lastPremiumTransaction = null;
        Iterator<Purchase> it = list.iterator();
        if (it.hasNext()) {
            WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        if (atomicInteger.get() != 0 || runnable == null) {
            return;
        }
        runnable.run();
    }

    private /* synthetic */ void lambda$onPurchasesUpdatedInternal$8(final AlertDialog[] alertDialogArr, Purchase purchase, TLRPC.TL_payments_assignPlayMarketTransaction tL_payments_assignPlayMarketTransaction, AccountInstance accountInstance, BillingResult billingResult, AtomicInteger atomicInteger, AtomicInteger atomicInteger2, Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                BillingController.$r8$lambda$oF4WmUdn9O6Yp_BnAIvHsPfYgGY(alertDialogArr);
            }
        });
        throw null;
    }

    public static /* synthetic */ void $r8$lambda$oF4WmUdn9O6Yp_BnAIvHsPfYgGY(AlertDialog[] alertDialogArr) {
        AlertDialog alertDialog = alertDialogArr[0];
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void consumeGiftPurchase(Purchase purchase, TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, Runnable runnable) {
        if ((inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentGiftPremium) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentPremiumGiftCode) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentStarsTopup) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentStarsGift) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentPremiumGiveaway) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentStarsGiveaway) || (inputStorePaymentPurpose instanceof TLRPC.TL_inputStorePaymentAuthCode)) {
            StringBuilder sb = new StringBuilder();
            sb.append("BillingController consumeGiftPurchase ");
            sb.append(inputStorePaymentPurpose);
            sb.append(" ");
            throw null;
        }
    }

    public void onBillingServiceDisconnected() {
        FileLog.d("Billing: Service disconnected");
        int i = this.isDisconnected ? 15000 : 5000;
        this.isDisconnected = true;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBillingServiceDisconnected$10();
            }
        }, i);
    }

    public void whenSetuped(Runnable runnable) {
        this.setupListeners.add(runnable);
    }

    public void onBillingSetupFinished(BillingResult billingResult) {
        FileLog.d("Billing: Setup finished with result " + billingResult);
        if (billingResult.getResponseCode() == 0) {
            this.isDisconnected = false;
            this.triesLeft = 3;
            try {
                queryProductDetails(Collections.singletonList(PREMIUM_PRODUCT), new BillingController$$ExternalSyntheticLambda1(this));
            } catch (Exception e) {
                FileLog.e(e);
            }
            queryPurchases("inapp", new PurchasesResponseListener() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda2
            });
            queryPurchases("subs", new PurchasesResponseListener() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda2
            });
            if (this.setupListeners.isEmpty()) {
                return;
            }
            for (int i = 0; i < this.setupListeners.size(); i++) {
                AndroidUtilities.runOnUIThread(this.setupListeners.get(i));
            }
            this.setupListeners.clear();
            return;
        }
        if (this.isDisconnected) {
            return;
        }
        switchToInvoice();
    }

    private void onQueriedPremiumProductDetails(BillingResult billingResult, List<ProductDetails> list) {
        FileLog.d("Billing: Query product details finished " + billingResult + ", " + list);
        if (billingResult.getResponseCode() == 0) {
            Iterator<ProductDetails> it = list.iterator();
            if (it.hasNext()) {
                WindowDecorActionBar$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                throw null;
            }
            switchToInvoice();
            return;
        }
        switchToInvoice();
        int i = this.triesLeft - 1;
        this.triesLeft = i;
        if (i > 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.BillingController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onQueriedPremiumProductDetails$11();
                }
            }, i == 2 ? 1000L : 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onQueriedPremiumProductDetails$11() {
        try {
            queryProductDetails(Collections.singletonList(PREMIUM_PRODUCT), new BillingController$$ExternalSyntheticLambda1(this));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static String getResponseCodeString(int i) {
        if (i != 12) {
            switch (i) {
                case -3:
                    return "SERVICE_TIMEOUT";
                case -2:
                    return "FEATURE_NOT_SUPPORTED";
                case -1:
                    return "SERVICE_DISCONNECTED";
                case 0:
                    return "OK";
                case 1:
                    return "USER_CANCELED";
                case 2:
                    return "SERVICE_UNAVAILABLE";
                case 3:
                    return "BILLING_UNAVAILABLE";
                case 4:
                    return "ITEM_UNAVAILABLE";
                case 5:
                    return "DEVELOPER_ERROR";
                case 6:
                    return "ERROR";
                case 7:
                    return "ITEM_ALREADY_OWNED";
                case 8:
                    return "ITEM_NOT_OWNED";
                default:
                    return "BILLING_UNKNOWN_ERROR";
            }
        }
        return "NETWORK_ERROR";
    }
}
