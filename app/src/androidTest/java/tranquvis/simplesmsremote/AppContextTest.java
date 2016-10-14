package tranquvis.simplesmsremote;

import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.util.concurrent.ExecutionError;
import android.util.Log;

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

    /**
     * Run method until its return value equals {@code successValue}
     * @return latest return value of method
     */
    protected <T> T TryUntil(TryMethod<T> method, T successValue) throws Exception {
        return TryUntil(method, successValue, 10, 4000);
    }

    /**
     * Run method until its return value equals {@code successValue}
     * @param timeout timeout between tries in milliseconds
     * @param maxTime max time to tryin milliseconds
     * @return latest return value of method
     */
    protected <T> T TryUntil(TryMethod<T> method, T successValue, int timeout, int maxTime)
            throws Exception
    {
        long startTime = System.currentTimeMillis();
        T returnValue;
        do
        {
            returnValue = method.run();
            if(returnValue == successValue)
                break;

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return returnValue;
            }
        }
        while((System.currentTimeMillis() - startTime) < maxTime);

        return returnValue;
    }

    protected interface TryMethod<T>
    {
        T run() throws Exception;
    }

    protected boolean isEmulator()
    {
        Log.i("fingerprint", Build.FINGERPRINT);
        return Build.FINGERPRINT.contains("generic");
    }
}
