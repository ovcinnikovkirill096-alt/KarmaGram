package com.google.firebase.sessions.settings;

import java.util.Map;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;

public interface CrashlyticsSettingsFetcher {
    Object doConfigFetch(Map map, Function2 function2, Function2 function3, Continuation continuation);
}
