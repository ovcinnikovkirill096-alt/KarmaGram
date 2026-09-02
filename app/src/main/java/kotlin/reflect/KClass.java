package kotlin.reflect;

public interface KClass extends KDeclarationContainer, KAnnotatedElement {
    String getQualifiedName();

    String getSimpleName();

    boolean isInstance(Object obj);
}
