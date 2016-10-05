package tranquvis.simplesmsremote.Helper;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by Andreas Kaltenleitner on 05.10.2016.
 */

public class BluetoothHelper
{
    public static void SetBluetoothState(boolean enabled) throws Exception
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enabled && !isEnabled)
        {
            if(!bluetoothAdapter.enable())
                throw new Exception("enabling bluetooth failed");
        }
        else if (!enabled && isEnabled)
        {
            if(!bluetoothAdapter.disable())
                throw new Exception("disabling bluetooth failed");
        }
    }

    public static boolean IsBluetoothEnabled()
    {
        throw new NotImplementedException("TODO");
    }
}
