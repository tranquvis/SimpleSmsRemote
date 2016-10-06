package tranquvis.simplesmsremote.Helper;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */
public class BluetoothHelperTest extends AppContextTest
{
    @Test
    public void setBluetoothStateEnabled() throws Exception
    {
        BluetoothHelper.SetBluetoothState(true);

        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(BluetoothHelper.IsBluetoothEnabled())
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    public void setBluetoothStateDisabled() throws Exception
    {
        BluetoothHelper.SetBluetoothState(false);

        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(!BluetoothHelper.IsBluetoothEnabled())
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    public void isBluetoothEnabled() throws Exception
    {
        BluetoothHelper.IsBluetoothEnabled();
    }

}