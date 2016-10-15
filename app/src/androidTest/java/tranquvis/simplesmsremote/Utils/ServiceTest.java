package tranquvis.simplesmsremote.Utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.rule.ServiceTestRule;

import org.junit.Rule;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Services.TestService;

/**
 * Created by Andi on 15.10.2016.
 */

public abstract class ServiceTest extends AppContextTest
{
    private TestService service;
    IBinder binder;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create the service Intent.
        Intent serviceIntent = new Intent(appContext, TestService.class);

        // Bind the service and grab a reference to the binder.

        binder = mServiceRule.bindService(serviceIntent);

        if(binder == null)
            throw new Exception("Failed to initialize test service.");

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        service = ((TestService.LocalBinder) binder).getService();
    }

    protected TestService getService()
    {
        return service;
    }
}
