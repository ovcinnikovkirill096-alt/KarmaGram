package com.google.firebase.crashlytics.internal.model.serialization;

import android.util.JsonReader;

public final /* synthetic */ class CrashlyticsReportJsonTransform$$ExternalSyntheticLambda8 implements CrashlyticsReportJsonTransform.ObjectParser {
    @Override // com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform.ObjectParser
    public final Object parse(JsonReader jsonReader) {
        return CrashlyticsReportJsonTransform.parseEventFrame(jsonReader);
    }
}
