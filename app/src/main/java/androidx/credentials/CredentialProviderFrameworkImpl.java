package androidx.credentials;

import android.content.Context;
import android.credentials.CreateCredentialException;
import android.credentials.GetCredentialException;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.OutcomeReceiver;
import android.util.Log;
import androidx.credentials.exceptions.CreateCredentialUnsupportedException;
import androidx.credentials.exceptions.GetCredentialUnsupportedException;
import androidx.credentials.internal.ConversionUtilsKt;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class CredentialProviderFrameworkImpl implements CredentialProvider {
    private static final Companion Companion = new Companion(null);
    private final android.credentials.CredentialManager credentialManager;

    public CredentialProviderFrameworkImpl(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.credentialManager = CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline6.m(context.getSystemService("credential"));
    }

    @Override // androidx.credentials.CredentialProvider
    public void onGetCredential(Context context, GetCredentialRequest request, CancellationSignal cancellationSignal, Executor executor, final CredentialManagerCallback callback) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(request, "request");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (isCredmanDisabled(new Function0() { // from class: androidx.credentials.CredentialProviderFrameworkImpl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return CredentialProviderFrameworkImpl.onGetCredential$lambda$2(callback);
            }
        })) {
            return;
        }
        OutcomeReceiver<android.credentials.GetCredentialResponse, GetCredentialException> outcomeReceiver = new OutcomeReceiver() { // from class: androidx.credentials.CredentialProviderFrameworkImpl$onGetCredential$outcome$2
            public /* bridge */ /* synthetic */ void onError(Throwable th) {
                onError(CredentialProviderFrameworkImpl$onGetCredential$outcome$2$$ExternalSyntheticApiModelOutline0.m(th));
            }

            public /* bridge */ /* synthetic */ void onResult(Object obj) {
                onResult(CredentialProviderFrameworkImpl$onGetCredential$outcome$2$$ExternalSyntheticApiModelOutline1.m(obj));
            }

            public void onResult(android.credentials.GetCredentialResponse response) {
                Intrinsics.checkNotNullParameter(response, "response");
                Log.i("CredManProvService", "GetCredentialResponse returned from framework");
                callback.onResult(this.convertGetResponseToJetpackClass$credentials_release(response));
            }

            public void onError(GetCredentialException error) {
                Intrinsics.checkNotNullParameter(error, "error");
                Log.i("CredManProvService", "GetCredentialResponse error returned from framework");
                callback.onError(this.convertToJetpackGetException$credentials_release(error));
            }
        };
        android.credentials.CredentialManager credentialManager = this.credentialManager;
        Intrinsics.checkNotNull(credentialManager);
        credentialManager.getCredential(context, convertGetRequestToFrameworkClass(request), cancellationSignal, executor, outcomeReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit onGetCredential$lambda$2(CredentialManagerCallback credentialManagerCallback) {
        credentialManagerCallback.onError(new GetCredentialUnsupportedException("Your device doesn't support credential manager"));
        return Unit.INSTANCE;
    }

    private final boolean isCredmanDisabled(Function0 function0) {
        if (this.credentialManager != null) {
            return false;
        }
        function0.invoke();
        return true;
    }

    @Override // androidx.credentials.CredentialProvider
    public void onCreateCredential(Context context, final CreateCredentialRequest request, CancellationSignal cancellationSignal, Executor executor, final CredentialManagerCallback callback) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(request, "request");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (isCredmanDisabled(new Function0() { // from class: androidx.credentials.CredentialProviderFrameworkImpl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return CredentialProviderFrameworkImpl.onCreateCredential$lambda$3(callback);
            }
        })) {
            return;
        }
        OutcomeReceiver<android.credentials.CreateCredentialResponse, CreateCredentialException> outcomeReceiver = new OutcomeReceiver() { // from class: androidx.credentials.CredentialProviderFrameworkImpl$onCreateCredential$outcome$1
            public /* bridge */ /* synthetic */ void onError(Throwable th) {
                onError(CredentialProviderFrameworkImpl$onCreateCredential$outcome$1$$ExternalSyntheticApiModelOutline0.m(th));
            }

            public /* bridge */ /* synthetic */ void onResult(Object obj) {
                onResult(CredentialProviderFrameworkImpl$onCreateCredential$outcome$1$$ExternalSyntheticApiModelOutline1.m(obj));
            }

            public void onResult(android.credentials.CreateCredentialResponse response) {
                Intrinsics.checkNotNullParameter(response, "response");
                Log.i("CredManProvService", "Create Result returned from framework: ");
                CredentialManagerCallback credentialManagerCallback = callback;
                CreateCredentialResponse.Companion companion = CreateCredentialResponse.Companion;
                String type = request.getType();
                Bundle data = response.getData();
                Intrinsics.checkNotNullExpressionValue(data, "getData(...)");
                credentialManagerCallback.onResult(companion.createFrom(type, data));
            }

            public void onError(CreateCredentialException error) {
                Intrinsics.checkNotNullParameter(error, "error");
                Log.i("CredManProvService", "CreateCredentialResponse error returned from framework");
                callback.onError(this.convertToJetpackCreateException$credentials_release(error));
            }
        };
        android.credentials.CredentialManager credentialManager = this.credentialManager;
        Intrinsics.checkNotNull(credentialManager);
        credentialManager.createCredential(context, convertCreateRequestToFrameworkClass(request, context), cancellationSignal, executor, outcomeReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit onCreateCredential$lambda$3(CredentialManagerCallback credentialManagerCallback) {
        credentialManagerCallback.onError(new CreateCredentialUnsupportedException("Your device doesn't support credential manager"));
        return Unit.INSTANCE;
    }

    private final android.credentials.CreateCredentialRequest convertCreateRequestToFrameworkClass(CreateCredentialRequest createCredentialRequest, Context context) {
        CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline5.m();
        android.credentials.CreateCredentialRequest.Builder alwaysSendAppInfoToProvider = CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline4.m(createCredentialRequest.getType(), ConversionUtilsKt.getFinalCreateCredentialData(createCredentialRequest, context), createCredentialRequest.getCandidateQueryData()).setIsSystemProviderRequired(createCredentialRequest.isSystemProviderRequired()).setAlwaysSendAppInfoToProvider(true);
        Intrinsics.checkNotNullExpressionValue(alwaysSendAppInfoToProvider, "setAlwaysSendAppInfoToProvider(...)");
        setOriginForCreateRequest(createCredentialRequest, alwaysSendAppInfoToProvider);
        android.credentials.CreateCredentialRequest createCredentialRequestBuild = alwaysSendAppInfoToProvider.build();
        Intrinsics.checkNotNullExpressionValue(createCredentialRequestBuild, "build(...)");
        return createCredentialRequestBuild;
    }

    private final void setOriginForCreateRequest(CreateCredentialRequest createCredentialRequest, android.credentials.CreateCredentialRequest.Builder builder) {
        if (createCredentialRequest.getOrigin() != null) {
            builder.setOrigin(createCredentialRequest.getOrigin());
        }
    }

    private final android.credentials.GetCredentialRequest convertGetRequestToFrameworkClass(GetCredentialRequest getCredentialRequest) {
        CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline2.m();
        android.credentials.GetCredentialRequest.Builder builderM = CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline0.m(GetCredentialRequest.Companion.getRequestMetadataBundle(getCredentialRequest));
        for (CredentialOption credentialOption : getCredentialRequest.getCredentialOptions()) {
            CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline3.m();
            builderM.addCredentialOption(CredentialProviderFrameworkImpl$$ExternalSyntheticApiModelOutline1.m(credentialOption.getType(), credentialOption.getRequestData(), credentialOption.getCandidateQueryData()).setIsSystemProviderRequired(credentialOption.isSystemProviderRequired()).setAllowedProviders(credentialOption.getAllowedProviders()).build());
        }
        setOriginForGetRequest(getCredentialRequest, builderM);
        android.credentials.GetCredentialRequest getCredentialRequestBuild = builderM.build();
        Intrinsics.checkNotNullExpressionValue(getCredentialRequestBuild, "build(...)");
        return getCredentialRequestBuild;
    }

    private final void setOriginForGetRequest(GetCredentialRequest getCredentialRequest, android.credentials.GetCredentialRequest.Builder builder) {
        if (getCredentialRequest.getOrigin() != null) {
            builder.setOrigin(getCredentialRequest.getOrigin());
        }
    }

    public final androidx.credentials.exceptions.GetCredentialException convertToJetpackGetException$credentials_release(GetCredentialException error) {
        Intrinsics.checkNotNullParameter(error, "error");
        String type = error.getType();
        Intrinsics.checkNotNullExpressionValue(type, "getType(...)");
        return ConversionUtilsKt.toJetpackGetException(type, error.getMessage());
    }

    public final androidx.credentials.exceptions.CreateCredentialException convertToJetpackCreateException$credentials_release(CreateCredentialException error) {
        Intrinsics.checkNotNullParameter(error, "error");
        String type = error.getType();
        Intrinsics.checkNotNullExpressionValue(type, "getType(...)");
        return ConversionUtilsKt.toJetpackCreateException(type, error.getMessage());
    }

    public final GetCredentialResponse convertGetResponseToJetpackClass$credentials_release(android.credentials.GetCredentialResponse response) {
        Intrinsics.checkNotNullParameter(response, "response");
        android.credentials.Credential credential = response.getCredential();
        Intrinsics.checkNotNullExpressionValue(credential, "getCredential(...)");
        Credential.Companion companion = Credential.Companion;
        String type = credential.getType();
        Intrinsics.checkNotNullExpressionValue(type, "getType(...)");
        Bundle data = credential.getData();
        Intrinsics.checkNotNullExpressionValue(data, "getData(...)");
        return new GetCredentialResponse(companion.createFrom(type, data));
    }

    @Override // androidx.credentials.CredentialProvider
    public boolean isAvailableOnDevice() {
        return Build.VERSION.SDK_INT >= 34 && this.credentialManager != null;
    }

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
