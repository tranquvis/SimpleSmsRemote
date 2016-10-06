package tranquvis.simplesmsremote.Helper;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */
public class BluetoothHelperTest extends AppContextTest
{
    @Test
    @ExecSequentially("bluetooth")
    public void setBluetoothStateEnabled() throws Exception
    {
        BluetoothHelper.SetBluetoothState(true);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return BluetoothHelper.IsBluetoothEnabled();
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("bluetooth")
    public void setBluetoothStateDisabled() throws Exception
    {
        BluetoothHelper.SetBluetoothState(false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return BluetoothHelper.IsBluetoothEnabled();
            }
        }, false, 10, 10000);
        assertFalse(enabled);
    }

    @Test
    @ExecSequentially("bluetooth")
    public void isBluetoothEnabled() throws Exception
    {
        BluetoothHelper.IsBluetoothEnabled();
    }
}