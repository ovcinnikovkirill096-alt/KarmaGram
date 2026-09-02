package j$.util.concurrent;

import j$.util.Spliterator;
import j$.util.function.BiConsumer$CC;
import j$.util.function.BiFunction$CC;
import j$.util.function.Consumer$CC;
import j$.util.stream.AbstractC0201b;
import j$.util.stream.AbstractC0301v0;
import j$.util.stream.AbstractC0322z1;
import j$.util.stream.C0260m3;
import j$.util.stream.C0311x0;
import j$.util.stream.EnumC0215d3;
import j$.util.stream.EnumC0220e3;
import j$.util.stream.EnumC0306w0;
import j$.util.stream.M3;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final /* synthetic */ class s implements BiConsumer, BiFunction, Consumer, M3 {
    public final /* synthetic */ int a;
    public final Object b;
    public final Object c;

    public /* synthetic */ s(int i, Object obj, Object obj2) {
        this.a = i;
        this.b = obj;
        this.c = obj2;
    }

    public /* synthetic */ s(BiFunction biFunction, Function function) {
        this.a = 2;
        this.c = biFunction;
        this.b = function;
    }

    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 0:
                break;
        }
        return BiConsumer$CC.$default$andThen(this, biConsumer);
    }

    public /* synthetic */ BiFunction andThen(Function function) {
        return BiFunction$CC.$default$andThen(this, function);
    }

    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 3:
                break;
            case 4:
                break;
            case 6:
                break;
        }
        return Consumer$CC.$default$andThen(this, consumer);
    }

    @Override // java.util.function.BiFunction
    public Object apply(Object obj, Object obj2) {
        return ((Function) this.b).apply(((BiFunction) this.c).apply(obj, obj2));
    }

    @Override // java.util.function.BiConsumer
    public void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                ConcurrentMap concurrentMap = (ConcurrentMap) this.b;
                BiFunction biFunction = (BiFunction) this.c;
                while (!concurrentMap.replace(obj, obj2, biFunction.apply(obj, obj2)) && (obj2 = concurrentMap.get(obj)) != null) {
                }
                break;
            default:
                BiConsumer biConsumer = (BiConsumer) this.b;
                BiConsumer biConsumer2 = (BiConsumer) this.c;
                biConsumer.accept(obj, obj2);
                biConsumer2.accept(obj, obj2);
                break;
        }
    }

    public s(EnumC0220e3 enumC0220e3, EnumC0306w0 enumC0306w0, Supplier supplier) {
        this.a = 5;
        this.b = enumC0306w0;
        this.c = supplier;
    }

    @Override // j$.util.stream.M3
    public int s() {
        return EnumC0215d3.u | EnumC0215d3.r;
    }

    @Override // j$.util.stream.M3
    public Object f(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        AbstractC0301v0 abstractC0301v0 = (AbstractC0301v0) ((Supplier) this.c).get();
        abstractC0201b.t0(spliterator, abstractC0301v0);
        return Boolean.valueOf(abstractC0301v0.b);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // j$.util.stream.M3
    public Object i(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        return (Boolean) new C0311x0(this, (AbstractC0201b) abstractC0322z1, spliterator).invoke();
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public void v(Object obj) {
        switch (this.a) {
            case 3:
                Consumer consumer = (Consumer) this.b;
                Consumer consumer2 = (Consumer) this.c;
                consumer.v(obj);
                consumer2.v(obj);
                break;
            case 4:
                AtomicBoolean atomicBoolean = (AtomicBoolean) this.b;
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.c;
                if (obj != null) {
                    concurrentHashMap.putIfAbsent(obj, Boolean.TRUE);
                } else {
                    atomicBoolean.set(true);
                }
                break;
            case 5:
            default:
                C0260m3 c0260m3 = (C0260m3) this.b;
                Consumer consumer3 = (Consumer) this.c;
                if (c0260m3.b.putIfAbsent(obj != null ? obj : C0260m3.d, Boolean.TRUE) == null) {
                    consumer3.v(obj);
                }
                break;
            case 6:
                ((BiConsumer) this.b).accept(this.c, obj);
                break;
        }
    }
}
