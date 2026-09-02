package org.telegram.messenger;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.concurrent.ConcurrentMap$EL;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;
import org.webrtc.MediaStreamTrack;

public class FileStreamLoadOperation extends BaseDataSource implements FileLoadOperationStream {
    public static final ConcurrentHashMap<Long, FileStreamLoadOperation> allStreams = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Integer> priorityMap = new ConcurrentHashMap<>();
    private long bytesRemaining;
    private long bytesTransferred;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    File currentFile;
    private long currentOffset;
    private TLRPC.Document document;
    private RandomAccessFile file;
    private FileLoadOperation loadOperation;
    private boolean opened;
    private Object parentObject;
    private long requestedLength;
    private Uri uri;

    @Override // com.google.android.exoplayer2.upstream.BaseDataSource, com.google.android.exoplayer2.upstream.DataSource
    public /* bridge */ /* synthetic */ Map getResponseHeaders() {
        return Collections.EMPTY_MAP;
    }

    public FileStreamLoadOperation() {
        super(true);
    }

    @Deprecated
    public FileStreamLoadOperation(TransferListener transferListener) {
        this();
        if (transferListener != null) {
            addTransferListener(transferListener);
        }
    }

    public static int getStreamPrioriy(TLRPC.Document document) {
        Integer num;
        if (document == null || (num = priorityMap.get(Long.valueOf(document.id))) == null) {
            return 3;
        }
        return num.intValue();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) {
        this.uri = dataSpec.uri;
        transferInitializing(dataSpec);
        int iIntValue = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("account")).intValue();
        this.currentAccount = iIntValue;
        this.parentObject = FileLoader.getInstance(iIntValue).getParentObject(Utilities.parseInt((CharSequence) this.uri.getQueryParameter("rid")).intValue());
        TLRPC.TL_document tL_document = new TLRPC.TL_document();
        this.document = tL_document;
        tL_document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash")).longValue();
        this.document.id = Utilities.parseLong(this.uri.getQueryParameter("id")).longValue();
        this.document.size = Utilities.parseLong(this.uri.getQueryParameter("size")).longValue();
        this.document.dc_id = Utilities.parseInt((CharSequence) this.uri.getQueryParameter("dc")).intValue();
        this.document.mime_type = this.uri.getQueryParameter("mime");
        this.document.file_reference = Utilities.hexToBytes(this.uri.getQueryParameter("reference"));
        TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
        tL_documentAttributeFilename.file_name = this.uri.getQueryParameter("name");
        this.document.attributes.add(tL_documentAttributeFilename);
        if (this.document.mime_type.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
            this.document.attributes.add(new TLRPC.TL_documentAttributeVideo());
        } else if (this.document.mime_type.startsWith(MediaStreamTrack.AUDIO_TRACK_KIND)) {
            this.document.attributes.add(new TLRPC.TL_documentAttributeAudio());
        }
        allStreams.put(Long.valueOf(this.document.id), this);
        this.currentOffset = dataSpec.position;
        this.requestedLength = dataSpec.length;
        this.loadOperation = FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, null, this.parentObject, this.currentOffset, false, getCurrentPriority());
        this.bytesTransferred = 0L;
        long j = this.document.size - dataSpec.position;
        this.bytesRemaining = j;
        long j2 = this.requestedLength;
        if (j2 != -1) {
            this.bytesRemaining = Math.min(j, j2);
        }
        this.opened = true;
        transferStarted(dataSpec);
        FileLoadOperation fileLoadOperation = this.loadOperation;
        if (fileLoadOperation != null) {
            File currentFile = fileLoadOperation.getCurrentFile();
            this.currentFile = currentFile;
            if (currentFile != null) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(this.currentFile, "r");
                    this.file = randomAccessFile;
                    randomAccessFile.seek(this.currentOffset);
                    if (this.loadOperation.isFinished()) {
                        this.isNetwork = false;
                        long length = this.currentFile.length() - this.currentOffset;
                        this.bytesRemaining = length;
                        long j3 = this.requestedLength;
                        if (j3 != -1) {
                            this.bytesRemaining = Math.min(length, j3 - this.bytesTransferred);
                        }
                    }
                } catch (Throwable unused) {
                }
            }
        }
        FileLog.e("FileStreamLoadOperation " + this.document.id + " open operation=" + this.loadOperation + " currentFile=" + this.currentFile + " file=" + this.file + " bytesRemaining=" + this.bytesRemaining + " me=" + this);
        FileLog.e("FileStreamLoadOperation " + this.document.id + " " + MessageObject.getVideoWidth(this.document) + "x" + MessageObject.getVideoWidth(this.document) + " mime_type=" + this.document.mime_type + " codec=" + MessageObject.getVideoCodec(this.document) + " size=" + this.document.size);
        return this.bytesRemaining;
    }

    private int getCurrentPriority() {
        Integer num = (Integer) ConcurrentMap$EL.getOrDefault(priorityMap, Long.valueOf(this.document.id), null);
        if (num != null) {
            return num.intValue();
        }
        return 3;
    }

    /* JADX WARN: Code duplicated, block: B:100:0x0015 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:101:0x0015 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:25:0x0038 A[Catch: Exception -> 0x0074, InterruptedException -> 0x0077, TRY_LEAVE, TryCatch #6 {InterruptedException -> 0x0077, Exception -> 0x0074, blocks: (B:23:0x002a, B:25:0x0038, B:21:0x0026), top: B:87:0x002a }] */
    /* JADX WARN: Code duplicated, block: B:29:0x005b A[Catch: Exception -> 0x0061, InterruptedException -> 0x0065, TryCatch #8 {InterruptedException -> 0x0065, Exception -> 0x0061, blocks: (B:27:0x0053, B:29:0x005b, B:34:0x0069, B:36:0x006d, B:42:0x007b, B:44:0x0085, B:46:0x008d, B:48:0x0091, B:49:0x00a5, B:52:0x00ac, B:60:0x00e9, B:62:0x00f1, B:64:0x00f9, B:66:0x0122, B:67:0x0129, B:70:0x012f, B:72:0x0135), top: B:83:0x0053 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x006d A[Catch: Exception -> 0x0061, InterruptedException -> 0x0065, TryCatch #8 {InterruptedException -> 0x0065, Exception -> 0x0061, blocks: (B:27:0x0053, B:29:0x005b, B:34:0x0069, B:36:0x006d, B:42:0x007b, B:44:0x0085, B:46:0x008d, B:48:0x0091, B:49:0x00a5, B:52:0x00ac, B:60:0x00e9, B:62:0x00f1, B:64:0x00f9, B:66:0x0122, B:67:0x0129, B:70:0x012f, B:72:0x0135), top: B:83:0x0053 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x007a  */
    /* JADX WARN: Code duplicated, block: B:48:0x0091 A[Catch: Exception -> 0x0061, InterruptedException -> 0x0065, TryCatch #8 {InterruptedException -> 0x0065, Exception -> 0x0061, blocks: (B:27:0x0053, B:29:0x005b, B:34:0x0069, B:36:0x006d, B:42:0x007b, B:44:0x0085, B:46:0x008d, B:48:0x0091, B:49:0x00a5, B:52:0x00ac, B:60:0x00e9, B:62:0x00f1, B:64:0x00f9, B:66:0x0122, B:67:0x0129, B:70:0x012f, B:72:0x0135), top: B:83:0x0053 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x00a9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:81:0x00b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:92:0x00c9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:93:0x00de A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:99:0x0015 A[SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:48:0x0091, please report this as an issue */
    @Override // com.google.android.exoplayer2.upstream.DataReader
    public int read(byte[] bArr, int i, int i2) throws IOException {
        Exception exc;
        InterruptedException interruptedException;
        Exception e;
        InterruptedException e2;
        FileStreamLoadOperation fileStreamLoadOperation;
        File currentFileFast;
        RandomAccessFile randomAccessFile;
        long length;
        long j;
        FileLoadOperation fileLoadOperationLoadStreamFile;
        FileLoadOperation fileLoadOperation;
        CountDownLatch countDownLatch;
        RandomAccessFile randomAccessFile2;
        if (i2 == 0) {
            return 0;
        }
        long j2 = this.bytesRemaining;
        if (j2 == 0) {
            return -1;
        }
        if (j2 < i2) {
            i2 = (int) j2;
        }
        int i3 = 0;
        while (true) {
            try {
                if (i3 == 0) {
                    try {
                        if (!this.opened) {
                        }
                        i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i2)[0];
                        if (i3 == 0) {
                            this.countDownLatch = new CountDownLatch(1);
                            fileStreamLoadOperation = this;
                            try {
                                fileLoadOperationLoadStreamFile = FileLoader.getInstance(this.currentAccount).loadStreamFile(fileStreamLoadOperation, this.document, null, this.parentObject, this.currentOffset, false, getCurrentPriority());
                                fileLoadOperation = fileStreamLoadOperation.loadOperation;
                                if (fileLoadOperation != fileLoadOperationLoadStreamFile) {
                                    fileLoadOperation.removeStreamListener(this);
                                    fileStreamLoadOperation.loadOperation = fileLoadOperationLoadStreamFile;
                                }
                                countDownLatch = fileStreamLoadOperation.countDownLatch;
                                if (countDownLatch != null) {
                                    countDownLatch.await();
                                    fileStreamLoadOperation.countDownLatch = null;
                                }
                            } catch (InterruptedException e3) {
                                e2 = e3;
                                interruptedException = e2;
                                FileLog.e(interruptedException);
                                return -3;
                            } catch (Exception e4) {
                                e = e4;
                                exc = e;
                                throw new IOException(exc);
                            }
                        } else {
                            fileStreamLoadOperation = this;
                        }
                        currentFileFast = fileStreamLoadOperation.loadOperation.getCurrentFileFast();
                        if (fileStreamLoadOperation.file != null || !Objects.equals(fileStreamLoadOperation.currentFile, currentFileFast)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("check stream file " + currentFileFast);
                            }
                            randomAccessFile = fileStreamLoadOperation.file;
                            if (randomAccessFile != null) {
                                try {
                                    randomAccessFile.close();
                                } catch (Exception unused) {
                                }
                            }
                            fileStreamLoadOperation.currentFile = currentFileFast;
                            if (currentFileFast != null) {
                                try {
                                    RandomAccessFile randomAccessFile3 = new RandomAccessFile(fileStreamLoadOperation.currentFile, "r");
                                    fileStreamLoadOperation.file = randomAccessFile3;
                                    randomAccessFile3.seek(fileStreamLoadOperation.currentOffset);
                                    if (fileStreamLoadOperation.loadOperation.isFinished()) {
                                        fileStreamLoadOperation.isNetwork = false;
                                        length = fileStreamLoadOperation.currentFile.length() - fileStreamLoadOperation.currentOffset;
                                        fileStreamLoadOperation.bytesRemaining = length;
                                        j = fileStreamLoadOperation.requestedLength;
                                        if (j != -1) {
                                            fileStreamLoadOperation.bytesRemaining = Math.min(length, j - fileStreamLoadOperation.bytesTransferred);
                                        }
                                    }
                                } catch (Throwable unused2) {
                                    if (fileStreamLoadOperation.loadOperation.isFinished() && !fileStreamLoadOperation.currentFile.exists()) {
                                        FileLoader.getInstance(fileStreamLoadOperation.currentAccount).cancelLoadFile(fileStreamLoadOperation.loadOperation.getFileName());
                                        FileLoadOperation fileLoadOperationLoadStreamFile2 = FileLoader.getInstance(fileStreamLoadOperation.currentAccount).loadStreamFile(fileStreamLoadOperation, fileStreamLoadOperation.document, null, fileStreamLoadOperation.parentObject, fileStreamLoadOperation.currentOffset, false, getCurrentPriority());
                                        FileLoadOperation fileLoadOperation2 = fileStreamLoadOperation.loadOperation;
                                        if (fileLoadOperation2 != fileLoadOperationLoadStreamFile2) {
                                            fileLoadOperation2.removeStreamListener(this);
                                            fileStreamLoadOperation.loadOperation = fileLoadOperationLoadStreamFile2;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e5) {
                        interruptedException = e5;
                        FileLog.e(interruptedException);
                        return -3;
                    } catch (Exception e6) {
                        exc = e6;
                        throw new IOException(exc);
                    }
                }
                i3 = (int) this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, i2)[0];
                if (i3 == 0) {
                    this.countDownLatch = new CountDownLatch(1);
                    fileStreamLoadOperation = this;
                    fileLoadOperationLoadStreamFile = FileLoader.getInstance(this.currentAccount).loadStreamFile(fileStreamLoadOperation, this.document, null, this.parentObject, this.currentOffset, false, getCurrentPriority());
                    fileLoadOperation = fileStreamLoadOperation.loadOperation;
                    if (fileLoadOperation != fileLoadOperationLoadStreamFile) {
                        fileLoadOperation.removeStreamListener(this);
                        fileStreamLoadOperation.loadOperation = fileLoadOperationLoadStreamFile;
                    }
                    countDownLatch = fileStreamLoadOperation.countDownLatch;
                    if (countDownLatch != null) {
                        countDownLatch.await();
                        fileStreamLoadOperation.countDownLatch = null;
                    }
                } else {
                    fileStreamLoadOperation = this;
                }
                currentFileFast = fileStreamLoadOperation.loadOperation.getCurrentFileFast();
                if (fileStreamLoadOperation.file != null) {
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("check stream file " + currentFileFast);
                }
                randomAccessFile = fileStreamLoadOperation.file;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                fileStreamLoadOperation.currentFile = currentFileFast;
                if (currentFileFast != null) {
                    RandomAccessFile randomAccessFile4 = new RandomAccessFile(fileStreamLoadOperation.currentFile, "r");
                    fileStreamLoadOperation.file = randomAccessFile4;
                    randomAccessFile4.seek(fileStreamLoadOperation.currentOffset);
                    if (fileStreamLoadOperation.loadOperation.isFinished()) {
                        fileStreamLoadOperation.isNetwork = false;
                        length = fileStreamLoadOperation.currentFile.length() - fileStreamLoadOperation.currentOffset;
                        fileStreamLoadOperation.bytesRemaining = length;
                        j = fileStreamLoadOperation.requestedLength;
                        if (j != -1) {
                            fileStreamLoadOperation.bytesRemaining = Math.min(length, j - fileStreamLoadOperation.bytesTransferred);
                        }
                    }
                }
            } catch (InterruptedException e7) {
                e2 = e7;
                interruptedException = e2;
                FileLog.e(interruptedException);
                return -3;
            } catch (Exception e8) {
                e = e8;
                exc = e;
                throw new IOException(exc);
            }
            randomAccessFile2 = this.file;
            if (randomAccessFile2 != null) {
                break;
            }
        }
        if (!this.opened) {
            return 0;
        }
        int i4 = randomAccessFile2.read(bArr, i, i3);
        if (i4 > 0) {
            long j3 = i4;
            this.currentOffset += j3;
            this.bytesRemaining -= j3;
            this.bytesTransferred += j3;
            bytesTransferred(i4);
        }
        return i4;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() {
        FileLog.e("FileStreamLoadOperation " + this.document.id + " close me=" + this);
        FileLoadOperation fileLoadOperation = this.loadOperation;
        if (fileLoadOperation != null) {
            fileLoadOperation.removeStreamListener(this);
        }
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.file = null;
        }
        this.uri = null;
        allStreams.remove(Long.valueOf(this.document.id));
        if (this.opened) {
            this.opened = false;
            transferEnded();
        }
        CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }

    @Override // org.telegram.messenger.FileLoadOperationStream
    public void newDataAvailable() {
        CountDownLatch countDownLatch = this.countDownLatch;
        this.countDownLatch = null;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public static void setPriorityForDocument(TLRPC.Document document, int i) {
        if (document != null) {
            priorityMap.put(Long.valueOf(document.id), Integer.valueOf(i));
        }
    }

    public static Uri prepareUri(int i, TLRPC.Document document, Object obj) {
        String attachFileName = FileLoader.getAttachFileName(document);
        File pathToAttach = FileLoader.getInstance(i).getPathToAttach(document);
        if (pathToAttach != null && pathToAttach.exists()) {
            return Uri.fromFile(pathToAttach);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("?account=");
            sb.append(i);
            sb.append("&id=");
            sb.append(document.id);
            sb.append("&hash=");
            sb.append(document.access_hash);
            sb.append("&dc=");
            sb.append(document.dc_id);
            sb.append("&size=");
            sb.append(document.size);
            sb.append("&mime=");
            sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
            sb.append("&rid=");
            sb.append(FileLoader.getInstance(i).getFileReference(obj));
            sb.append("&name=");
            sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
            sb.append("&reference=");
            byte[] bArr = document.file_reference;
            if (bArr == null) {
                bArr = new byte[0];
            }
            sb.append(Utilities.bytesToHex(bArr));
            return Uri.parse("tg://" + attachFileName + sb.toString());
        } catch (UnsupportedEncodingException e) {
            FileLog.e(e);
            return null;
        }
    }
}
