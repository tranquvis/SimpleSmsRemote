package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;
import tranquvis.simplesmsremote.Utils.Device.BluetoothUtils;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */
public class BluetoothUtilsTest extends AppContextTest
{
    @Test
    @ExecSequentially("bluetooth")
    public void setBluetoothStateEnabled() throws Exception
    {
        BluetoothUtils.SetBluetoothState(true);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return BluetoothUtils.IsBluetoothEnabled();
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("bluetooth")
    public void setBluetoothStateDisabled() throws Exception
    {
        BluetoothUtils.SetBluetoothState(false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return BluetoothUtils.IsBluetoothEnabled();
            }
        }, false, 10, 10000);
        assertFalse(enabled);
    }

    @Test
    @ExecSequentially("bluetooth")
    public void isBluetoothEnabled() throws Exception
    {
        BluetoothUtils.IsBluetoothEnabled();
    }
}