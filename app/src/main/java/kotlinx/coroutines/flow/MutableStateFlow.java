package kotlinx.coroutines.flow;

public interface MutableStateFlow extends StateFlow, MutableSharedFlow {
    boolean compareAndSet(Object obj, Object obj2);

    Object getValue();

    void setValue(Object obj);
}
