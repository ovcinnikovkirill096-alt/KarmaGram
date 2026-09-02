package okio.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Socket;
import okio.Source;

public final class DefaultSocket implements Socket {
    private AtomicInteger closeBits;
    private final Sink sink;
    private final java.net.Socket socket;
    private final Source source;

    public DefaultSocket(java.net.Socket socket) {
        Intrinsics.checkNotNullParameter(socket, "socket");
        this.socket = socket;
        this.closeBits = new AtomicInteger();
        this.source = new SocketSource();
        this.sink = new SocketSink();
    }

    public final java.net.Socket getSocket() {
        return this.socket;
    }

    @Override // okio.Socket
    public Source getSource() {
        return this.source;
    }

    @Override // okio.Socket
    public Sink getSink() {
        return this.sink;
    }

    @Override // okio.Socket
    public void cancel() throws IOException {
        this.socket.close();
    }

    public String toString() {
        String string = this.socket.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public final class SocketSink implements Sink {
        private final OutputStream outputStream;
        private final SocketAsyncTimeout timeout;

        public SocketSink() {
            this.outputStream = DefaultSocket.this.getSocket().getOutputStream();
            this.timeout = new SocketAsyncTimeout(DefaultSocket.this.getSocket());
        }

        @Override // okio.Sink
        public void write(Buffer source, long j) throws IOException {
            Intrinsics.checkNotNullParameter(source, "source");
            SegmentedByteString.checkOffsetAndCount(source.size(), 0L, j);
            while (j > 0) {
                this.timeout.throwIfReached();
                Segment segment = source.head;
                Intrinsics.checkNotNull(segment);
                int iMin = (int) Math.min(j, segment.limit - segment.pos);
                SocketAsyncTimeout socketAsyncTimeout = this.timeout;
                socketAsyncTimeout.enter();
                try {
                    try {
                        this.outputStream.write(segment.data, segment.pos, iMin);
                        Unit unit = Unit.INSTANCE;
                        if (!socketAsyncTimeout.exit()) {
                            segment.pos += iMin;
                            long j2 = iMin;
                            j -= j2;
                            source.setSize$okio(source.size() - j2);
                            if (segment.pos == segment.limit) {
                                source.head = segment.pop();
                                SegmentPool.recycle(segment);
                            }
                        } else {
                            throw socketAsyncTimeout.access$newTimeoutException(null);
                        }
                    } catch (IOException e) {
                        if (!socketAsyncTimeout.exit()) {
                            throw e;
                        }
                        throw socketAsyncTimeout.access$newTimeoutException(e);
                    }
                } catch (Throwable th) {
                    socketAsyncTimeout.exit();
                    throw th;
                }
            }
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            SocketAsyncTimeout socketAsyncTimeout = this.timeout;
            socketAsyncTimeout.enter();
            try {
                try {
                    this.outputStream.flush();
                    Unit unit = Unit.INSTANCE;
                    if (socketAsyncTimeout.exit()) {
                        throw socketAsyncTimeout.access$newTimeoutException(null);
                    }
                } catch (IOException e) {
                    if (!socketAsyncTimeout.exit()) {
                        throw e;
                    }
                    throw socketAsyncTimeout.access$newTimeoutException(e);
                }
            } catch (Throwable th) {
                socketAsyncTimeout.exit();
                throw th;
            }
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [kotlin.coroutines.Continuation, okio.AsyncTimeout, okio.internal.SocketAsyncTimeout] */
        /* JADX WARN: Type inference failed for: r1v9, types: [java.net.Socket, kotlin.coroutines.jvm.internal.DebugProbesKt] */
        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            ?? r0 = this.timeout;
            DefaultSocket defaultSocket = DefaultSocket.this;
            r0.enter();
            try {
                try {
                    int bitsOrZero = _AtomicKt.setBitsOrZero(defaultSocket.closeBits, 1);
                    if (bitsOrZero == 0) {
                        r0.exit();
                        return;
                    }
                    if (bitsOrZero != 3) {
                        if (!defaultSocket.getSocket().isClosed() && !defaultSocket.getSocket().isOutputShutdown()) {
                            this.outputStream.flush();
                            try {
                                defaultSocket.getSocket().probeCoroutineSuspended(r0);
                            } catch (UnsupportedOperationException unused) {
                                this.outputStream.close();
                            }
                        }
                        r0.exit();
                        return;
                    }
                    defaultSocket.getSocket().close();
                    Unit unit = Unit.INSTANCE;
                    if (r0.exit()) {
                        throw r0.access$newTimeoutException(null);
                    }
                    return;
                } catch (IOException e) {
                    if (!r0.exit()) {
                        throw e;
                    }
                    throw r0.access$newTimeoutException(e);
                }
            } catch (Throwable th) {
                r0.exit();
                throw th;
            }
            r0.exit();
            throw th;
        }

        @Override // okio.Sink
        public SocketAsyncTimeout timeout() {
            return this.timeout;
        }

        public String toString() {
            return "sink(" + DefaultSocket.this.getSocket() + ')';
        }
    }

    public final class SocketSource implements Source {
        private final InputStream inputStream;
        private final SocketAsyncTimeout timeout;

        public SocketSource() {
            this.inputStream = DefaultSocket.this.getSocket().getInputStream();
            this.timeout = new SocketAsyncTimeout(DefaultSocket.this.getSocket());
        }

        @Override // okio.Source
        public long read(Buffer sink, long j) throws IOException {
            Intrinsics.checkNotNullParameter(sink, "sink");
            if (j == 0) {
                return 0L;
            }
            if (j < 0) {
                throw new IllegalArgumentException(("byteCount < 0: " + j).toString());
            }
            this.timeout.throwIfReached();
            Segment segmentWritableSegment$okio = sink.writableSegment$okio(1);
            int iMin = (int) Math.min(j, 8192 - segmentWritableSegment$okio.limit);
            try {
                SocketAsyncTimeout socketAsyncTimeout = this.timeout;
                socketAsyncTimeout.enter();
                try {
                    try {
                        int i = this.inputStream.read(segmentWritableSegment$okio.data, segmentWritableSegment$okio.limit, iMin);
                        if (socketAsyncTimeout.exit()) {
                            throw socketAsyncTimeout.access$newTimeoutException(null);
                        }
                        if (i == -1) {
                            if (segmentWritableSegment$okio.pos != segmentWritableSegment$okio.limit) {
                                return -1L;
                            }
                            sink.head = segmentWritableSegment$okio.pop();
                            SegmentPool.recycle(segmentWritableSegment$okio);
                            return -1L;
                        }
                        segmentWritableSegment$okio.limit += i;
                        long j2 = i;
                        sink.setSize$okio(sink.size() + j2);
                        return j2;
                    } catch (IOException e) {
                        if (socketAsyncTimeout.exit()) {
                            throw socketAsyncTimeout.access$newTimeoutException(e);
                        }
                        throw e;
                    }
                } catch (Throwable th) {
                    socketAsyncTimeout.exit();
                    throw th;
                }
            } catch (AssertionError e2) {
                if (_JavaIoKt.isAndroidGetsocknameError(e2)) {
                    throw new IOException(e2);
                }
                throw e2;
            }
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [kotlin.coroutines.Continuation, okio.AsyncTimeout, okio.internal.SocketAsyncTimeout] */
        /* JADX WARN: Type inference failed for: r1v9, types: [java.net.Socket, kotlin.coroutines.jvm.internal.DebugProbesKt] */
        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            ?? r0 = this.timeout;
            DefaultSocket defaultSocket = DefaultSocket.this;
            r0.enter();
            try {
                try {
                    int bitsOrZero = _AtomicKt.setBitsOrZero(defaultSocket.closeBits, 2);
                    if (bitsOrZero == 0) {
                        r0.exit();
                        return;
                    }
                    if (bitsOrZero == 3) {
                        defaultSocket.getSocket().close();
                    } else if (defaultSocket.getSocket().isClosed() || defaultSocket.getSocket().isInputShutdown()) {
                        r0.exit();
                        return;
                    } else {
                        try {
                            defaultSocket.getSocket().probeCoroutineResumed(r0);
                        } catch (UnsupportedOperationException unused) {
                            this.inputStream.close();
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                    if (r0.exit()) {
                        throw r0.access$newTimeoutException(null);
                    }
                } catch (Throwable th) {
                    r0.exit();
                    throw th;
                }
            } catch (IOException e) {
                if (!r0.exit()) {
                    throw e;
                }
                throw r0.access$newTimeoutException(e);
            }
        }

        @Override // okio.Source
        public SocketAsyncTimeout timeout() {
            return this.timeout;
        }

        public String toString() {
            return "source(" + DefaultSocket.this.getSocket() + ')';
        }
    }
}
