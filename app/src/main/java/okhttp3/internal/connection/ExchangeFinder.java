package okhttp3.internal.connection;

public interface ExchangeFinder {
    RealConnection find();

    RoutePlanner getRoutePlanner();
}
