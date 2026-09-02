package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.internal.url._UrlKt;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialogDecor;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedColor;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.LaunchActivity;

public class FactCheckController {
    private static AlertDialog currentDialog;
    private boolean clearedExpiredInDatabase;
    public final int currentAccount;
    private static volatile FactCheckController[] Instance = new FactCheckController[16];
    private static final Object[] lockObjects = new Object[16];
    private final LongSparseArray<TLRPC.TL_factCheck> localCache = new LongSparseArray<>();
    private final LongSparseArray<HashMap<Key, Utilities.Callback<TLRPC.TL_factCheck>>> toload = new LongSparseArray<>();
    private final ArrayList<Key> loading = new ArrayList<>();
    private final Runnable loadMissingRunnable = new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda17
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.loadMissing();
        }
    };

    static {
        for (int i = 0; i < 16; i++) {
            lockObjects[i] = new Object();
        }
    }

    public static FactCheckController getInstance(int i) {
        FactCheckController factCheckController;
        FactCheckController factCheckController2 = Instance[i];
        if (factCheckController2 != null) {
            return factCheckController2;
        }
        synchronized (lockObjects[i]) {
            try {
                factCheckController = Instance[i];
                if (factCheckController == null) {
                    FactCheckController[] factCheckControllerArr = Instance;
                    FactCheckController factCheckController3 = new FactCheckController(i);
                    factCheckControllerArr[i] = factCheckController3;
                    factCheckController = factCheckController3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return factCheckController;
    }

    private FactCheckController(int i) {
        this.currentAccount = i;
    }

    public TLRPC.TL_factCheck getFactCheck(final MessageObject messageObject) {
        TLRPC.Message message;
        TLRPC.TL_factCheck tL_factCheck;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (tL_factCheck = message.factcheck) == null) {
            return null;
        }
        if (!tL_factCheck.need_check) {
            if (this.localCache.get(tL_factCheck.hash) == null) {
                LongSparseArray<TLRPC.TL_factCheck> longSparseArray = this.localCache;
                TLRPC.TL_factCheck tL_factCheck2 = messageObject.messageOwner.factcheck;
                longSparseArray.put(tL_factCheck2.hash, tL_factCheck2);
                saveToDatabase(messageObject.messageOwner.factcheck);
            }
            return messageObject.messageOwner.factcheck;
        }
        final Key keyOf = Key.of(messageObject);
        if (keyOf == null || keyOf.messageId < 0) {
            return null;
        }
        TLRPC.TL_factCheck tL_factCheck3 = this.localCache.get(keyOf.hash);
        if (tL_factCheck3 != null) {
            messageObject.messageOwner.factcheck = tL_factCheck3;
            return tL_factCheck3;
        }
        if (this.loading.contains(keyOf)) {
            return messageObject.messageOwner.factcheck;
        }
        HashMap<Key, Utilities.Callback<TLRPC.TL_factCheck>> map = this.toload.get(keyOf.dialogId);
        if (map == null) {
            LongSparseArray<HashMap<Key, Utilities.Callback<TLRPC.TL_factCheck>>> longSparseArray2 = this.toload;
            long j = keyOf.dialogId;
            HashMap<Key, Utilities.Callback<TLRPC.TL_factCheck>> map2 = new HashMap<>();
            longSparseArray2.put(j, map2);
            map = map2;
        }
        if (!map.containsKey(keyOf)) {
            map.put(keyOf, new Utilities.Callback() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda15
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$getFactCheck$0(keyOf, messageObject, (TLRPC.TL_factCheck) obj);
                }
            });
            scheduleLoadMissing();
        }
        return messageObject.messageOwner.factcheck;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFactCheck$0(Key key, MessageObject messageObject, TLRPC.TL_factCheck tL_factCheck) {
        this.localCache.put(key.hash, tL_factCheck);
        messageObject.messageOwner.factcheck = tL_factCheck;
    }

    private void scheduleLoadMissing() {
        AndroidUtilities.cancelRunOnUIThread(this.loadMissingRunnable);
        AndroidUtilities.runOnUIThread(this.loadMissingRunnable, 80L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMissing() {
        while (this.toload.size() > 0) {
            final long jKeyAt = this.toload.keyAt(0);
            final HashMap<Key, Utilities.Callback<TLRPC.TL_factCheck>> mapValueAt = this.toload.valueAt(0);
            this.toload.removeAt(0);
            final ArrayList<Key> arrayList = new ArrayList<>(mapValueAt.keySet());
            this.loading.addAll(arrayList);
            getFromDatabase(arrayList, new Utilities.Callback() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda11
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    this.f$0.lambda$loadMissing$3(jKeyAt, arrayList, mapValueAt, (ArrayList) obj);
                }
            });
        }
        this.toload.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMissing$3(long j, ArrayList arrayList, final HashMap map, ArrayList arrayList2) {
        final TLRPC.TL_getFactCheck tL_getFactCheck = new TLRPC.TL_getFactCheck();
        tL_getFactCheck.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(j);
        final ArrayList arrayList3 = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            Key key = (Key) arrayList.get(i2);
            TLRPC.TL_factCheck tL_factCheck = (TLRPC.TL_factCheck) arrayList2.get(i2);
            if (tL_factCheck == null) {
                arrayList3.add(key);
                tL_getFactCheck.msg_id.add(Integer.valueOf(key.messageId));
            } else {
                this.loading.remove(key);
                Utilities.Callback callback = (Utilities.Callback) map.get(key);
                if (callback != null) {
                    callback.run(tL_factCheck);
                    i++;
                }
            }
        }
        if (i > 0) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.factCheckLoaded, new Object[0]);
        }
        if (tL_getFactCheck.msg_id.isEmpty()) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_getFactCheck, new RequestDelegate() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda16
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                this.f$0.lambda$loadMissing$2(tL_getFactCheck, arrayList3, map, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMissing$2(final TLRPC.TL_getFactCheck tL_getFactCheck, final ArrayList arrayList, final HashMap map, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadMissing$1(tLObject, tL_getFactCheck, arrayList, map);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMissing$1(TLObject tLObject, TLRPC.TL_getFactCheck tL_getFactCheck, ArrayList arrayList, HashMap map) {
        ArrayList arrayList2 = new ArrayList();
        if (tLObject instanceof Vector) {
            ArrayList arrayList3 = ((Vector) tLObject).objects;
            for (int i = 0; i < arrayList3.size(); i++) {
                if (arrayList3.get(i) instanceof TLRPC.TL_factCheck) {
                    arrayList2.add((TLRPC.TL_factCheck) arrayList3.get(i));
                }
            }
        }
        HashMap map2 = new HashMap();
        for (int i2 = 0; i2 < Math.min(tL_getFactCheck.msg_id.size(), arrayList2.size()); i2++) {
            Integer num = (Integer) tL_getFactCheck.msg_id.get(i2);
            num.intValue();
            map2.put(num, (TLRPC.TL_factCheck) arrayList2.get(i2));
        }
        int i3 = 0;
        for (int i4 = 0; i4 < tL_getFactCheck.msg_id.size(); i4++) {
            Key key = (Key) arrayList.get(i4);
            Integer num2 = (Integer) tL_getFactCheck.msg_id.get(i4);
            num2.intValue();
            TLRPC.TL_factCheck tL_factCheck = (TLRPC.TL_factCheck) map2.get(num2);
            Utilities.Callback callback = (Utilities.Callback) map.get(key);
            if (tL_factCheck != null && !tL_factCheck.need_check && callback != null) {
                callback.run(tL_factCheck);
                i3++;
                this.loading.remove(key);
            }
        }
        if (i3 > 0) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.factCheckLoaded, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Key {
        public final long dialogId;
        public final long hash;
        public final int messageId;

        private Key(long j, int i, long j2) {
            this.dialogId = j;
            this.messageId = i;
            this.hash = j2;
        }

        public int hashCode() {
            return CameraTimestamp$$ExternalSyntheticBackport0.m(this.hash);
        }

        public static Key of(MessageObject messageObject) {
            TLRPC.Message message;
            if (messageObject == null || (message = messageObject.messageOwner) == null || message.factcheck == null) {
                return null;
            }
            return new Key(messageObject.getDialogId(), messageObject.getId(), messageObject.messageOwner.factcheck.hash);
        }
    }

    private void getFromDatabase(final ArrayList<Key> arrayList, final Utilities.Callback<ArrayList<TLRPC.TL_factCheck>> callback) {
        if (callback == null) {
            return;
        }
        if (arrayList == null || arrayList.isEmpty()) {
            callback.run(new ArrayList<>());
        } else {
            final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    FactCheckController.$r8$lambda$eRU3nxQjWPT8Oj30fqwMG3VVK3A(messagesStorage, arrayList, callback);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$eRU3nxQjWPT8Oj30fqwMG3VVK3A(MessagesStorage messagesStorage, ArrayList arrayList, final Utilities.Callback callback) throws Throwable {
        final ArrayList arrayList2 = new ArrayList();
        SQLiteCursor sQLiteCursor = null;
        NativeByteBuffer nativeByteBuffer = null;
        sQLiteCursor = null;
        try {
            try {
                SQLiteDatabase database = messagesStorage.getDatabase();
                ArrayList arrayList3 = new ArrayList();
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    arrayList3.add(Long.valueOf(((Key) obj).hash));
                    arrayList2.add(null);
                }
                SQLiteCursor sQLiteCursorQueryFinalized = database.queryFinalized("SELECT data FROM fact_checks WHERE hash IN (" + TextUtils.join(", ", arrayList3) + ")", new Object[0]);
                while (sQLiteCursorQueryFinalized.next()) {
                    try {
                        try {
                            NativeByteBuffer nativeByteBufferByteBufferValue = sQLiteCursorQueryFinalized.byteBufferValue(0);
                            try {
                                TLRPC.TL_factCheck tL_factCheckTLdeserialize = TLRPC.TL_factCheck.TLdeserialize(nativeByteBufferByteBufferValue, nativeByteBufferByteBufferValue.readInt32(false), false);
                                if (tL_factCheckTLdeserialize != null) {
                                    int i2 = -1;
                                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                                        if (tL_factCheckTLdeserialize.hash == ((Key) arrayList.get(i3)).hash) {
                                            i2 = i3;
                                        }
                                    }
                                    if (i2 >= 0 && i2 < arrayList2.size()) {
                                        arrayList2.set(i2, tL_factCheckTLdeserialize);
                                    }
                                    nativeByteBufferByteBufferValue.reuse();
                                    break;
                                }
                                nativeByteBufferByteBufferValue.reuse();
                            } catch (Throwable th) {
                                th = th;
                                nativeByteBuffer = nativeByteBufferByteBufferValue;
                                if (nativeByteBuffer != null) {
                                    nativeByteBuffer.reuse();
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Exception e) {
                        sQLiteCursor = sQLiteCursorQueryFinalized;
                        e = e;
                        FileLog.e(e);
                        if (sQLiteCursor != null) {
                            sQLiteCursor.dispose();
                        }
                    } catch (Throwable th3) {
                        sQLiteCursor = sQLiteCursorQueryFinalized;
                        th = th3;
                        if (sQLiteCursor != null) {
                            sQLiteCursor.dispose();
                        }
                        throw th;
                    }
                }
                sQLiteCursorQueryFinalized.dispose();
            } catch (Exception e2) {
                e = e2;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    callback.run(arrayList2);
                }
            });
        } catch (Throwable th4) {
            th = th4;
        }
    }

    private void saveToDatabase(final TLRPC.TL_factCheck tL_factCheck) {
        if (tL_factCheck == null) {
            return;
        }
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                FactCheckController.m2861$r8$lambda$hwZvWtn8X40ulbUsYqEavkirk8(messagesStorage, tL_factCheck);
            }
        });
        clearExpiredInDatabase();
    }

    /* JADX WARN: Code duplicated, block: B:28:0x005c  */
    /* JADX WARN: Code duplicated, block: B:30:0x0061  */
    /* JADX INFO: renamed from: $r8$lambda$hwZvWtn8X40ulbUsYqEavkir-k8, reason: not valid java name */
    public static /* synthetic */ void m2861$r8$lambda$hwZvWtn8X40ulbUsYqEavkirk8(MessagesStorage messagesStorage, TLRPC.TL_factCheck tL_factCheck) throws Throwable {
        NativeByteBuffer nativeByteBuffer;
        SQLitePreparedStatement sQLitePreparedStatement = null;
        try {
            SQLitePreparedStatement sQLitePreparedStatementExecuteFast = messagesStorage.getDatabase().executeFast("REPLACE INTO fact_checks VALUES(?, ?, ?)");
            try {
                sQLitePreparedStatementExecuteFast.requery();
                sQLitePreparedStatementExecuteFast.bindLong(1, tL_factCheck.hash);
                nativeByteBuffer = new NativeByteBuffer(tL_factCheck.getObjectSize());
                try {
                    tL_factCheck.serializeToStream(nativeByteBuffer);
                    sQLitePreparedStatementExecuteFast.bindByteBuffer(2, nativeByteBuffer);
                    sQLitePreparedStatementExecuteFast.bindLong(3, System.currentTimeMillis() + 889032704);
                    sQLitePreparedStatementExecuteFast.step();
                    sQLitePreparedStatementExecuteFast.dispose();
                    nativeByteBuffer.reuse();
                } catch (Exception e) {
                    e = e;
                    sQLitePreparedStatement = sQLitePreparedStatementExecuteFast;
                    try {
                        FileLog.e(e);
                        if (sQLitePreparedStatement != null) {
                            sQLitePreparedStatement.dispose();
                        }
                        if (nativeByteBuffer != null) {
                            nativeByteBuffer.reuse();
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (sQLitePreparedStatement != null) {
                            sQLitePreparedStatement.dispose();
                        }
                        if (nativeByteBuffer != null) {
                            nativeByteBuffer.reuse();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLitePreparedStatement = sQLitePreparedStatementExecuteFast;
                    if (sQLitePreparedStatement != null) {
                        sQLitePreparedStatement.dispose();
                    }
                    if (nativeByteBuffer != null) {
                        nativeByteBuffer.reuse();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                nativeByteBuffer = null;
            } catch (Throwable th3) {
                th = th3;
                nativeByteBuffer = null;
            }
        } catch (Exception e3) {
            e = e3;
            nativeByteBuffer = null;
        } catch (Throwable th4) {
            th = th4;
            nativeByteBuffer = null;
        }
    }

    private void clearExpiredInDatabase() {
        if (this.clearedExpiredInDatabase) {
            return;
        }
        this.clearedExpiredInDatabase = true;
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FactCheckController.$r8$lambda$JpH7yHT6pnclw1XNRCmFTCCRSQU(messagesStorage);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$JpH7yHT6pnclw1XNRCmFTCCRSQU(MessagesStorage messagesStorage) {
        try {
            messagesStorage.getDatabase().executeFast("DELETE FROM fact_checks WHERE expires > " + System.currentTimeMillis()).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void openFactCheckEditor(Context context, final Theme.ResourcesProvider resourcesProvider, final MessageObject messageObject, boolean z) {
        AlertDialog.Builder builder;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        TLRPC.Message message;
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        Activity activityFindActivity = AndroidUtilities.findActivity(context);
        final View currentFocus = activityFindActivity != null ? activityFindActivity.getCurrentFocus() : null;
        boolean z2 = lastFragment != null && (lastFragment.getFragmentView() instanceof SizeNotifierFrameLayout) && ((SizeNotifierFrameLayout) lastFragment.getFragmentView()).measureKeyboardHeight() > AndroidUtilities.dp(20.0f) && !z;
        final AlertDialog[] alertDialogArr = new AlertDialog[1];
        if (z2) {
            builder = new AlertDialogDecor.Builder(context, resourcesProvider);
        } else {
            builder = new AlertDialog.Builder(context, resourcesProvider);
        }
        AlertDialog.Builder builder2 = builder;
        final TextView[] textViewArr = new TextView[1];
        boolean z3 = messageObject == null || (message = messageObject.messageOwner) == null || message.factcheck == null;
        builder2.setTitle(LocaleController.getString(R.string.FactCheckDialog));
        final int i = MessagesController.getInstance(this.currentAccount).factcheckLengthLimit;
        final EditTextCaption editTextCaption = new EditTextCaption(context, resourcesProvider) { // from class: org.telegram.messenger.FactCheckController.1
            AnimatedTextView.AnimatedTextDrawable limit;
            AnimatedColor limitColor = new AnimatedColor(this);
            private int limitCount;

            {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
                this.limit = animatedTextDrawable;
                animatedTextDrawable.setAnimationProperties(0.2f, 0L, 160L, CubicBezierInterpolator.EASE_OUT_QUINT);
                this.limit.setTextSize(AndroidUtilities.dp(15.33f));
                this.limit.setCallback(this);
                this.limit.setGravity(5);
            }

            @Override // android.widget.TextView, android.view.View
            protected boolean verifyDrawable(Drawable drawable) {
                return drawable == this.limit || super.verifyDrawable(drawable);
            }

            @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
            protected void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                super.onTextChanged(charSequence, i2, i3, i4);
                if (this.limit != null) {
                    this.limitCount = i - charSequence.length();
                    this.limit.cancelAnimation();
                    AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.limit;
                    int i5 = this.limitCount;
                    String str = _UrlKt.FRAGMENT_ENCODE_SET;
                    if (i5 <= 4) {
                        str = _UrlKt.FRAGMENT_ENCODE_SET + this.limitCount;
                    }
                    animatedTextDrawable.setText(str);
                }
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor
            protected void extendActionMode(ActionMode actionMode, Menu menu) {
                if (menu.findItem(R.id.menu_bold) != null) {
                    return;
                }
                menu.removeItem(android.R.id.shareText);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.Bold));
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableStringBuilder.length(), 33);
                menu.add(R.id.menu_groupbolditalic, R.id.menu_bold, 6, spannableStringBuilder);
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(LocaleController.getString(R.string.Italic));
                spannableStringBuilder2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM_ITALIC)), 0, spannableStringBuilder2.length(), 33);
                menu.add(R.id.menu_groupbolditalic, R.id.menu_italic, 7, spannableStringBuilder2);
                menu.add(R.id.menu_groupbolditalic, R.id.menu_link, 8, LocaleController.getString(R.string.CreateLink));
                menu.add(R.id.menu_groupbolditalic, R.id.menu_regular, 9, LocaleController.getString(R.string.Regular));
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                this.limit.setTextColor(this.limitColor.set(Theme.getColor(this.limitCount < 0 ? Theme.key_text_RedRegular : Theme.key_dialogSearchHint, resourcesProvider)));
                this.limit.setBounds(getScrollX(), 0, getScrollX() + getWidth(), getHeight());
                this.limit.draw(canvas);
            }
        };
        editTextCaption.lineYFix = true;
        final boolean z4 = z3;
        editTextCaption.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.messenger.FactCheckController.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                if (i2 != 6) {
                    return false;
                }
                if (editTextCaption.getText().toString().length() > i) {
                    AndroidUtilities.shakeView(editTextCaption);
                    return true;
                }
                TLRPC.TL_textWithEntities tL_textWithEntities2 = new TLRPC.TL_textWithEntities();
                CharSequence[] charSequenceArr = {editTextCaption.getText()};
                tL_textWithEntities2.entities = MediaDataController.getInstance(FactCheckController.this.currentAccount).getEntities(charSequenceArr, true);
                CharSequence charSequence = charSequenceArr[0];
                tL_textWithEntities2.text = charSequence == null ? _UrlKt.FRAGMENT_ENCODE_SET : charSequence.toString();
                FactCheckController.this.applyFactCheck(messageObject, tL_textWithEntities2, z4);
                AlertDialog alertDialog = alertDialogArr[0];
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                if (alertDialogArr[0] == FactCheckController.currentDialog) {
                    FactCheckController.currentDialog = null;
                }
                View view = currentFocus;
                if (view != null) {
                    view.requestFocus();
                }
                return true;
            }
        });
        MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(AndroidUtilities.getCurrentKeyboardLanguage(), true);
        editTextCaption.setTextSize(1, 18.0f);
        editTextCaption.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        editTextCaption.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText, resourcesProvider));
        editTextCaption.setHintText(LocaleController.getString(R.string.FactCheckPlaceholder));
        editTextCaption.setFocusable(true);
        editTextCaption.setInputType(147457);
        editTextCaption.setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField, resourcesProvider), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated, resourcesProvider), Theme.getColor(Theme.key_text_RedRegular, resourcesProvider));
        editTextCaption.setImeOptions(6);
        editTextCaption.setBackgroundDrawable(null);
        editTextCaption.setPadding(0, AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f));
        final TLRPC.TL_factCheck factCheck = messageObject.getFactCheck();
        if (factCheck != null && (tL_textWithEntities = factCheck.text) != null) {
            SpannableStringBuilder spannableStringBuilderValueOf = SpannableStringBuilder.valueOf(tL_textWithEntities.text);
            MessageObject.addEntitiesToText(spannableStringBuilderValueOf, factCheck.text.entities, false, true, false, false);
            editTextCaption.setText(spannableStringBuilderValueOf);
        }
        editTextCaption.addTextChangedListener(new TextWatcher() { // from class: org.telegram.messenger.FactCheckController.3
            boolean ignoreTextChange;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (this.ignoreTextChange) {
                    return;
                }
                int length = editable.length();
                int i2 = i;
                boolean z5 = true;
                if (length > i2) {
                    this.ignoreTextChange = true;
                    editable.delete(i2, editable.length());
                    AndroidUtilities.shakeView(editTextCaption);
                    try {
                        editTextCaption.performHapticFeedback(3, 2);
                    } catch (Exception unused) {
                    }
                    this.ignoreTextChange = false;
                }
                if (textViewArr[0] != null) {
                    if (editable.length() <= 0 && factCheck != null) {
                        z5 = false;
                    }
                    textViewArr[0].setText(LocaleController.getString(z5 ? R.string.Done : R.string.Remove));
                    textViewArr[0].setTextColor(Theme.getColor(z5 ? Theme.key_dialogButton : Theme.key_text_RedBold));
                }
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.addView(editTextCaption, LayoutHelper.createLinear(-1, -2, 24.0f, 0.0f, 24.0f, 10.0f));
        builder2.makeCustomMaxHeight();
        builder2.setView(linearLayout);
        builder2.setWidth(AndroidUtilities.dp(292.0f));
        builder2.setPositiveButton(LocaleController.getString(R.string.Done), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                this.f$0.lambda$openFactCheckEditor$8(editTextCaption, i, messageObject, z4, alertDialog, i2);
            }
        });
        builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                alertDialog.dismiss();
            }
        });
        if (z2) {
            AlertDialog alertDialogCreate = builder2.create();
            currentDialog = alertDialogCreate;
            alertDialogArr[0] = alertDialogCreate;
            alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    FactCheckController.$r8$lambda$covAdcOkR6_V4SG3w1tVgkD6jFY(currentFocus, dialogInterface);
                }
            });
            currentDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    FactCheckController.$r8$lambda$cSn0YY6kSzQ16psVIG73jVTJqRo(editTextCaption, dialogInterface);
                }
            });
            currentDialog.showDelayed(250L);
        } else {
            AlertDialog alertDialogCreate2 = builder2.create();
            alertDialogArr[0] = alertDialogCreate2;
            alertDialogCreate2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    AndroidUtilities.hideKeyboard(editTextCaption);
                }
            });
            alertDialogArr[0].setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    FactCheckController.$r8$lambda$rCZDn9DTeD00226gcwmQQ1cnVzY(editTextCaption, dialogInterface);
                }
            });
            alertDialogArr[0].show();
        }
        alertDialogArr[0].setDismissDialogByButtons(false);
        View button = alertDialogArr[0].getButton(-1);
        if (button instanceof TextView) {
            textViewArr[0] = (TextView) button;
        }
        editTextCaption.setSelection(editTextCaption.getText().length());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openFactCheckEditor$8(EditTextCaption editTextCaption, int i, MessageObject messageObject, boolean z, AlertDialog alertDialog, int i2) {
        if (editTextCaption.getText().toString().length() > i) {
            AndroidUtilities.shakeView(editTextCaption);
            return;
        }
        TLRPC.TL_textWithEntities tL_textWithEntities = new TLRPC.TL_textWithEntities();
        CharSequence[] charSequenceArr = {editTextCaption.getText()};
        tL_textWithEntities.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true);
        CharSequence charSequence = charSequenceArr[0];
        tL_textWithEntities.text = charSequence == null ? _UrlKt.FRAGMENT_ENCODE_SET : charSequence.toString();
        applyFactCheck(messageObject, tL_textWithEntities, z);
        alertDialog.dismiss();
    }

    public static /* synthetic */ void $r8$lambda$covAdcOkR6_V4SG3w1tVgkD6jFY(View view, DialogInterface dialogInterface) {
        currentDialog = null;
        view.requestFocus();
    }

    public static /* synthetic */ void $r8$lambda$cSn0YY6kSzQ16psVIG73jVTJqRo(EditTextCaption editTextCaption, DialogInterface dialogInterface) {
        editTextCaption.requestFocus();
        AndroidUtilities.showKeyboard(editTextCaption);
    }

    public static /* synthetic */ void $r8$lambda$rCZDn9DTeD00226gcwmQQ1cnVzY(EditTextCaption editTextCaption, DialogInterface dialogInterface) {
        editTextCaption.requestFocus();
        AndroidUtilities.showKeyboard(editTextCaption);
    }

    public void applyFactCheck(MessageObject messageObject, final TLRPC.TL_textWithEntities tL_textWithEntities, final boolean z) {
        TLObject tLObject;
        if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
            TLRPC.TL_editFactCheck tL_editFactCheck = new TLRPC.TL_editFactCheck();
            tL_editFactCheck.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(messageObject.getDialogId());
            tL_editFactCheck.msg_id = messageObject.getId();
            tL_editFactCheck.text = tL_textWithEntities;
            tLObject = tL_editFactCheck;
        } else {
            if (z) {
                return;
            }
            TLRPC.TL_deleteFactCheck tL_deleteFactCheck = new TLRPC.TL_deleteFactCheck();
            tL_deleteFactCheck.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(messageObject.getDialogId());
            tL_deleteFactCheck.msg_id = messageObject.getId();
            tLObject = tL_deleteFactCheck;
        }
        Context context = LaunchActivity.instance;
        if (context == null) {
            context = ApplicationLoader.applicationContext;
        }
        final AlertDialog alertDialog = new AlertDialog(context, 3);
        alertDialog.showDelayed(320L);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda0
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                this.f$0.lambda$applyFactCheck$16(tL_textWithEntities, z, alertDialog, tLObject2, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyFactCheck$16(final TLRPC.TL_textWithEntities tL_textWithEntities, final boolean z, final AlertDialog alertDialog, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$applyFactCheck$15(tLObject, tL_textWithEntities, z, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyFactCheck$15(final TLObject tLObject, TLRPC.TL_textWithEntities tL_textWithEntities, boolean z, AlertDialog alertDialog) {
        if (tLObject instanceof TLRPC.Updates) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FactCheckController$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$applyFactCheck$14(tLObject);
                }
            });
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment != null) {
                boolean z2 = tL_textWithEntities == null || TextUtils.isEmpty(tL_textWithEntities.text);
                if (z2 || !z) {
                    BulletinFactory.of(safeLastFragment).createSimpleBulletin(z2 ? R.raw.ic_delete : R.raw.contact_check, LocaleController.getString(z2 ? R.string.FactCheckDeleted : R.string.FactCheckEdited)).show();
                }
            }
        }
        alertDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyFactCheck$14(TLObject tLObject) {
        MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
    }
}
