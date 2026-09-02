package org.telegram.messenger.utils;

import android.util.Base64;
import androidx.core.util.Pair;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Charsets;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public abstract class BillingUtilities {
    public static void extractCurrencyExp(Map map) {
        if (map.isEmpty()) {
            try {
                InputStream inputStreamOpen = ApplicationLoader.applicationContext.getAssets().open("currencies.json");
                JSONObject jSONObject = new JSONObject(new String(Util.toByteArray(inputStreamOpen), Charsets.UTF_8));
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, Integer.valueOf(jSONObject.optJSONObject(next).optInt("exp")));
                }
                inputStreamOpen.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static Pair createDeveloperPayload(TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose, AccountInstance accountInstance) {
        String strEncodeToString;
        if (accountInstance.getUserConfig().isClientActivated()) {
            strEncodeToString = Base64.encodeToString(String.valueOf(accountInstance.getUserConfig().getClientUserId()).getBytes(Charsets.UTF_8), 0);
        } else {
            strEncodeToString = Base64.encodeToString(("account-" + accountInstance.getCurrentAccount()).getBytes(Charsets.UTF_8), 0);
        }
        return Pair.create(strEncodeToString, savePurpose(inputStorePaymentPurpose));
    }

    public static String savePurpose(TLRPC.InputStorePaymentPurpose inputStorePaymentPurpose) {
        long jNextLong = Utilities.random.nextLong();
        FileLog.d("BillingUtilities.savePurpose id=" + jNextLong + " paymentPurpose=" + inputStorePaymentPurpose);
        SerializedData serializedData = new SerializedData(8);
        serializedData.writeInt64(jNextLong);
        String strBytesToHex = Utilities.bytesToHex(serializedData.toByteArray());
        serializedData.cleanup();
        FileLog.d("BillingUtilities.savePurpose id_hex=" + strBytesToHex + " paymentPurpose=" + inputStorePaymentPurpose);
        TL_savedPurpose tL_savedPurpose = new TL_savedPurpose();
        tL_savedPurpose.id = jNextLong;
        tL_savedPurpose.flags = 1;
        tL_savedPurpose.purpose = inputStorePaymentPurpose;
        SerializedData serializedData2 = new SerializedData(tL_savedPurpose.getObjectSize());
        tL_savedPurpose.serializeToStream(serializedData2);
        String strBytesToHex2 = Utilities.bytesToHex(serializedData2.toByteArray());
        serializedData2.cleanup();
        if (tL_savedPurpose.getObjectSize() > 28) {
            FileLog.d("BillingUtilities.savePurpose: sending short version, original size is " + tL_savedPurpose.getObjectSize() + " bytes");
            tL_savedPurpose.flags = 0;
            tL_savedPurpose.purpose = null;
        }
        SerializedData serializedData3 = new SerializedData(tL_savedPurpose.getObjectSize());
        tL_savedPurpose.serializeToStream(serializedData3);
        String strBytesToHex3 = Utilities.bytesToHex(serializedData3.toByteArray());
        serializedData3.cleanup();
        ApplicationLoader.applicationContext.getSharedPreferences("purchases", 0).edit().putString(strBytesToHex, strBytesToHex2).apply();
        FileLog.d("BillingUtilities.savePurpose: saved {" + strBytesToHex2 + "} under " + strBytesToHex);
        StringBuilder sb = new StringBuilder();
        sb.append("BillingUtilities.savePurpose: but sending {");
        sb.append(strBytesToHex3);
        sb.append("}");
        FileLog.d(sb.toString());
        return strBytesToHex3;
    }

    public static class TL_savedPurpose extends TLObject {
        public int flags;
        public long id;
        public TLRPC.InputStorePaymentPurpose purpose;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.id = inputSerializedData.readInt64(z);
            if ((this.flags & 1) != 0) {
                this.purpose = TLRPC.InputStorePaymentPurpose.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(495638674);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt64(this.id);
            if ((this.flags & 1) != 0) {
                this.purpose.serializeToStream(outputSerializedData);
            }
        }
    }
}
