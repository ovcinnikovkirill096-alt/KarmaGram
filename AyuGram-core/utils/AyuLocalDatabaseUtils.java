package com.radolyn.ayugram.utils;

import android.text.TextUtils;
import android.util.Pair;
import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

public abstract class AyuLocalDatabaseUtils {
    public static Pair getMinAndMaxForDialog(int i, long j) {
        MessagesStorage messagesStorage = MessagesStorage.getInstance(i);
        SQLiteCursor sQLiteCursorQueryFinalized = null;
        try {
            try {
                sQLiteCursorQueryFinalized = messagesStorage.getDatabase().queryFinalized(String.format(Locale.US, "SELECT MIN(mid), MAX(mid) FROM messages_v2 WHERE uid = %d", Long.valueOf(j)), new Object[0]);
                if (sQLiteCursorQueryFinalized.next()) {
                    Pair pair = new Pair(Integer.valueOf(sQLiteCursorQueryFinalized.intValue(0)), Integer.valueOf(sQLiteCursorQueryFinalized.intValue(1)));
                    sQLiteCursorQueryFinalized.dispose();
                    return pair;
                }
            } catch (Exception e) {
                messagesStorage.checkSQLException(e);
                if (sQLiteCursorQueryFinalized != null) {
                }
                return new Pair(0, 0);
            }
            sQLiteCursorQueryFinalized.dispose();
            return new Pair(0, 0);
        } catch (Throwable th) {
            if (sQLiteCursorQueryFinalized != null) {
                sQLiteCursorQueryFinalized.dispose();
            }
            throw th;
        }
    }

    public static LongSparseArray getMessageIdsByRandomIds(int i, ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return new LongSparseArray();
        }
        MessagesStorage messagesStorage = MessagesStorage.getInstance(i);
        SQLiteDatabase database = messagesStorage.getDatabase();
        SQLiteCursor sQLiteCursorQueryFinalized = null;
        try {
            try {
                sQLiteCursorQueryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT mid, uid FROM randoms_v2 WHERE random_id IN(%s)", TextUtils.join(",", arrayList)), new Object[0]);
                LongSparseArray longSparseArray = new LongSparseArray();
                while (sQLiteCursorQueryFinalized.next()) {
                    long jLongValue = sQLiteCursorQueryFinalized.longValue(1);
                    ArrayList arrayList2 = (ArrayList) longSparseArray.get(jLongValue);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        longSparseArray.put(jLongValue, arrayList2);
                    }
                    arrayList2.add(Integer.valueOf(sQLiteCursorQueryFinalized.intValue(0)));
                }
                sQLiteCursorQueryFinalized.dispose();
                return longSparseArray;
            } catch (Exception e) {
                messagesStorage.checkSQLException(e);
                if (sQLiteCursorQueryFinalized != null) {
                    sQLiteCursorQueryFinalized.dispose();
                }
                return new LongSparseArray();
            }
        } catch (Throwable th) {
            if (sQLiteCursorQueryFinalized != null) {
                sQLiteCursorQueryFinalized.dispose();
            }
            throw th;
        }
    }

    public static Iterator iterateThroughMessages(int i, long j) {
        return iterateThroughMessages(i, "SELECT data FROM messages_v2 WHERE uid = " + j);
    }

    public static Iterator iterateThroughMessages(int i, String str) {
        return new Iterator(i, str) { // from class: com.radolyn.ayugram.utils.AyuLocalDatabaseUtils.1
            private SQLiteCursor cursor;
            private TLRPC.Message nextMessage = null;
            final /* synthetic */ int val$accountNum;
            final /* synthetic */ String val$sqlQuery;

            {
                this.val$accountNum = i;
                this.val$sqlQuery = str;
                this.cursor = null;
                try {
                    this.cursor = MessagesStorage.getInstance(i).getDatabase().queryFinalized(str, new Object[0]);
                    advanceCursor();
                } catch (Exception e) {
                    handleException(e);
                }
            }

            private void advanceCursor() {
                TLRPC.Message messageTLdeserialize;
                while (true) {
                    try {
                        SQLiteCursor sQLiteCursor = this.cursor;
                        if (sQLiteCursor == null || !sQLiteCursor.next()) {
                            break;
                        }
                        NativeByteBuffer nativeByteBufferByteBufferValue = this.cursor.byteBufferValue(0);
                        if (nativeByteBufferByteBufferValue != null && (messageTLdeserialize = TLRPC.Message.TLdeserialize(nativeByteBufferByteBufferValue, nativeByteBufferByteBufferValue.readInt32(false), false)) != null) {
                            messageTLdeserialize.readAttachPath(nativeByteBufferByteBufferValue, UserConfig.getInstance(this.val$accountNum).clientUserId);
                            nativeByteBufferByteBufferValue.reuse();
                            this.nextMessage = messageTLdeserialize;
                            return;
                        }
                    } catch (Exception e) {
                        handleException(e);
                        return;
                    }
                }
                disposeCursor();
            }

            private void disposeCursor() {
                SQLiteCursor sQLiteCursor = this.cursor;
                if (sQLiteCursor != null) {
                    sQLiteCursor.dispose();
                    this.cursor = null;
                }
            }

            private void handleException(Exception exc) {
                MessagesStorage.getInstance(this.val$accountNum).checkSQLException(exc);
                disposeCursor();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.nextMessage != null;
            }

            @Override // java.util.Iterator
            public TLRPC.Message next() {
                TLRPC.Message message = this.nextMessage;
                this.nextMessage = null;
                advanceCursor();
                return message;
            }
        };
    }

    public static Iterator iterateThroughMessageIds(int i, String str) {
        return new Iterator(i, str) { // from class: com.radolyn.ayugram.utils.AyuLocalDatabaseUtils.2
            private SQLiteCursor cursor;
            private Integer nextMessageId = null;
            final /* synthetic */ int val$accountNum;
            final /* synthetic */ String val$sqlQuery;

            {
                this.val$accountNum = i;
                this.val$sqlQuery = str;
                this.cursor = null;
                try {
                    this.cursor = MessagesStorage.getInstance(i).getDatabase().queryFinalized(str, new Object[0]);
                    advanceCursor();
                } catch (Exception e) {
                    handleException(e);
                }
            }

            private void advanceCursor() {
                NativeByteBuffer nativeByteBufferByteBufferValue;
                do {
                    try {
                        SQLiteCursor sQLiteCursor = this.cursor;
                        if (sQLiteCursor != null && sQLiteCursor.next()) {
                            nativeByteBufferByteBufferValue = this.cursor.byteBufferValue(0);
                        } else {
                            disposeCursor();
                            return;
                        }
                    } catch (Exception e) {
                        handleException(e);
                        return;
                    }
                } while (nativeByteBufferByteBufferValue == null);
                this.nextMessageId = Integer.valueOf(this.cursor.intValue(0));
                nativeByteBufferByteBufferValue.reuse();
            }

            private void disposeCursor() {
                SQLiteCursor sQLiteCursor = this.cursor;
                if (sQLiteCursor != null) {
                    sQLiteCursor.dispose();
                    this.cursor = null;
                }
            }

            private void handleException(Exception exc) {
                MessagesStorage.getInstance(this.val$accountNum).checkSQLException(exc);
                disposeCursor();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.nextMessageId != null;
            }

            @Override // java.util.Iterator
            public Integer next() {
                Integer num = this.nextMessageId;
                this.nextMessageId = null;
                advanceCursor();
                return num;
            }
        };
    }
}
