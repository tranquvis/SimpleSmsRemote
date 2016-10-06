package tranquvis.simplesmsremote;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExecSequentially
{
    String value();
}
