package tranquvis.simplesmsremote.Aspects.ExecSequentially;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExecSequentially {
    String value();
}