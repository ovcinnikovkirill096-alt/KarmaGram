package com.google.firebase.sessions;

import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.CompositeEncoder;
import kotlinx.serialization.internal.PluginExceptionsKt;
import kotlinx.serialization.internal.SerializationConstructorMarker;
import org.telegram.messenger.MediaDataController;

public final class Time {
    public static final Companion Companion = new Companion(null);
    private final long ms;
    private final long seconds;
    private final long us;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Time) && this.ms == ((Time) obj).ms;
    }

    public int hashCode() {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(this.ms);
    }

    public String toString() {
        return "Time(ms=" + this.ms + ')';
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KSerializer serializer() {
            return Time$$serializer.INSTANCE;
        }
    }

    public /* synthetic */ Time(int i, long j, long j2, long j3, SerializationConstructorMarker serializationConstructorMarker) {
        if (1 != (i & 1)) {
            PluginExceptionsKt.throwMissingFieldException(i, 1, Time$$serializer.INSTANCE.getDescriptor());
        }
        this.ms = j;
        this.us = (i & 2) == 0 ? ((long) MediaDataController.MAX_STYLE_RUNS_COUNT) * j : j2;
        if ((i & 4) == 0) {
            this.seconds = j / ((long) MediaDataController.MAX_STYLE_RUNS_COUNT);
        } else {
            this.seconds = j3;
        }
    }

    public static final /* synthetic */ void write$Self$com_google_firebase_firebase_sessions(Time time, CompositeEncoder compositeEncoder, SerialDescriptor serialDescriptor) {
        compositeEncoder.encodeLongElement(serialDescriptor, 0, time.ms);
        if (compositeEncoder.shouldEncodeElementDefault(serialDescriptor, 1) || time.us != time.ms * ((long) MediaDataController.MAX_STYLE_RUNS_COUNT)) {
            compositeEncoder.encodeLongElement(serialDescriptor, 1, time.us);
        }
        if (!compositeEncoder.shouldEncodeElementDefault(serialDescriptor, 2) && time.seconds == time.ms / ((long) MediaDataController.MAX_STYLE_RUNS_COUNT)) {
            return;
        }
        compositeEncoder.encodeLongElement(serialDescriptor, 2, time.seconds);
    }

    public Time(long j) {
        this.ms = j;
        long j2 = MediaDataController.MAX_STYLE_RUNS_COUNT;
        this.us = j * j2;
        this.seconds = j / j2;
    }

    public final long getUs() {
        return this.us;
    }

    public final long getSeconds() {
        return this.seconds;
    }

    /* JADX INFO: renamed from: minus-5sfh64U, reason: not valid java name */
    public final long m2210minus5sfh64U(Time time) {
        Intrinsics.checkNotNullParameter(time, "time");
        Duration.Companion companion = Duration.Companion;
        return DurationKt.toDuration(this.ms - time.ms, DurationUnit.MILLISECONDS);
    }
}
