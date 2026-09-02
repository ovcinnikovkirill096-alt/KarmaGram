package okhttp3.internal.connection;

import java.io.IOException;
import kotlin.ExceptionsKt;
import kotlin.jvm.internal.Intrinsics;

public final class SequentialExchangeFinder implements ExchangeFinder {
    private final RoutePlanner routePlanner;

    public SequentialExchangeFinder(RoutePlanner routePlanner) {
        Intrinsics.checkNotNullParameter(routePlanner, "routePlanner");
        this.routePlanner = routePlanner;
    }

    @Override // okhttp3.internal.connection.ExchangeFinder
    public RoutePlanner getRoutePlanner() {
        return this.routePlanner;
    }

    @Override // okhttp3.internal.connection.ExchangeFinder
    public RealConnection find() throws Throwable {
        IOException iOException = null;
        while (!getRoutePlanner().isCanceled()) {
            try {
                RoutePlanner.Plan plan = getRoutePlanner().plan();
                if (!plan.isReady()) {
                    RoutePlanner.ConnectResult connectResultMo2719connectTcp = plan.mo2719connectTcp();
                    if (connectResultMo2719connectTcp.isSuccess()) {
                        connectResultMo2719connectTcp = plan.mo2720connectTlsEtc();
                    }
                    RoutePlanner.Plan planComponent2 = connectResultMo2719connectTcp.component2();
                    Throwable thComponent3 = connectResultMo2719connectTcp.component3();
                    if (thComponent3 != null) {
                        throw thComponent3;
                    }
                    if (planComponent2 != null) {
                        getRoutePlanner().getDeferredPlans().addFirst(planComponent2);
                    }
                }
                return plan.mo2716handleSuccess();
            } catch (IOException e) {
                if (iOException == null) {
                    iOException = e;
                } else {
                    ExceptionsKt.addSuppressed(iOException, e);
                }
                if (!RoutePlanner.CC.hasNext$default(getRoutePlanner(), null, 1, null)) {
                    throw iOException;
                }
            }
        }
        throw new IOException("Canceled");
    }
}
