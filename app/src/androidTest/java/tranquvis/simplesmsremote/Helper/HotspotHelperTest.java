package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import static org.junit.Assert.*;

/**
 * Created by Andi on 04.10.2016.
 */
public class HotspotHelperTest extends AppContextTest
{
    @Test
    public void setHotspotStateEnabled() throws Exception {
        //enabled wifi must be turned of. So turn on wifi ...
        WifiManager wifimanager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);

        HotspotHelper.SetHotspotState(appContext, true);
        boolean success = false;
        for(int i  = 0; i < 10; i++)
        {
            if(HotspotHelper.IsHotspotEnabled(appContext))
            {
                success = true;
                break;
            }
            Thread.sleep(100);
        }

        assertTrue(success);
    }

    @Test
    public void setHotspotStateDisabled() throws Exception {
        HotspotHelper.SetHotspotState(appContext, false);
        boolean success = false;
        for(int i  = 0; i < 10; i++)
        {
            if(!HotspotHelper.IsHotspotEnabled(appContext))
            {
                success = true;
                break;
            }
            Thread.sleep(100);
        }

        assertTrue(success);
    }
}