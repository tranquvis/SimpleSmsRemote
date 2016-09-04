package tranquvis.simplesmsremote;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;

/**
 * Created by Andi on 04.09.2016.
 */
public abstract class AppContextTest {
    protected Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
    }
}
