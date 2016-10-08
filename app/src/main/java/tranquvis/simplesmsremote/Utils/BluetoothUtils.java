package tranquvis.simplesmsremote.Utils;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Andreas Kaltenleitner on 05.10.2016.
 */

public class BluetoothUtils
{
    private static BluetoothAdapter getDefaultBluetoothHandle() throws Exception
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)
            throw new Exception("bluetooth not supported on this device");
        return bluetoothAdapter;
    }

    /**
     * set bluetooth state
     * @param enabled bluetooth state
     * @throws Exception
     */
    public static void SetBluetoothState(boolean enabled) throws Exception
    {
        BluetoothAdapter bluetoothAdapter = getDefaultBluetoothHandle();

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

    /**
     * check if bluetooth is enabled
     * @return true if bluetooth is enabled
     * @throws Exception
     */
    public static boolean IsBluetoothEnabled() throws Exception
    {
        BluetoothAdapter bluetoothAdapter = getDefaultBluetoothHandle();
        return bluetoothAdapter.isEnabled();
    }
}
