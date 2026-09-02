package retrofit2.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HTTP {
    boolean hasBody() default false;

    String method();

    String path() default "";
}
