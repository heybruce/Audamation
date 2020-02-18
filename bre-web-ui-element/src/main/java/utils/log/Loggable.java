package utils.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Loggable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
    public enum Level {TRACE, DEBUG, INFO, WARN, ERROR};

    Level level() default Level.DEBUG;

    boolean params() default true;

    boolean result() default true;

}
