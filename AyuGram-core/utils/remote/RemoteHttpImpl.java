package com.radolyn.ayugram.utils.remote;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLRPC;

public abstract class RemoteHttpImpl {
    private static final OkHttpClient client;
    private static final Gson gson;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        client = builder.readTimeout(10L, timeUnit).connectTimeout(10L, timeUnit).build();
        gson = new Gson();
    }

    public static void getConfig(final RequestDelegate requestDelegate) {
        try {
            client.newCall(new Request.Builder().url("https://update.ayugram.one/rc/current/android").build()).enqueue(new Callback() { // from class: com.radolyn.ayugram.utils.remote.RemoteHttpImpl.1
                @Override // okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    try {
                        requestDelegate.run(null, null);
                    } catch (Exception unused) {
                    }
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, Response response) {
                    try {
                        if (response.isSuccessful()) {
                            try {
                                processResponse(response);
                                return;
                            } catch (Exception unused) {
                                requestDelegate.run(null, null);
                                requestDelegate.run(null, null);
                            }
                        }
                        requestDelegate.run(null, null);
                    } catch (Exception unused2) {
                    }
                }

                private void processResponse(Response response) {
                    RC rc;
                    String[] strArr;
                    try {
                        String strString = response.body().string();
                        if (TextUtils.isEmpty(strString) || (rc = (RC) RemoteHttpImpl.gson.fromJson(strString, RC.class)) == null || (strArr = rc.messages) == null || strArr.length <= 0) {
                            return;
                        }
                        TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
                        for (String str : rc.messages) {
                            TLRPC.TL_message tL_message = new TLRPC.TL_message();
                            tL_message.message = str;
                            tL_messages_messages.messages.add(tL_message);
                        }
                        requestDelegate.run(tL_messages_messages, null);
                    } catch (Exception unused) {
                    }
                }
            });
        } catch (Exception unused) {
        }
    }

    private static class RC {
        public String[] messages;

        private RC() {
        }
    }
}
