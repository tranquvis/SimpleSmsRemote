package tranquvis.simplesmsremote;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;

import static org.junit.Assert.*;

/**
 * Created by Andi on 07.10.2016.
 */
public class CommandInstanceTest {

/*

    @Test
    public void getFromCommandWifiHotspotIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is hotspot enabled");
        assertTrue(ci != null &&  ci.getCommand() == Command.WIFI_HOTSPOT_IS_ENABLED);
    }

    @Test
    public void getFromCommandMobileDataEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable mobile data");
        assertTrue(ci != null &&  ci.getCommand() == Command.MOBILE_DATA_ENABLE);
    }

    @Test
    public void getFromCommandMobileDataDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable mobile data");
        assertTrue(ci != null &&  ci.getCommand() == Command.MOBILE_DATA_DISABLE);
    }

    @Test
    public void getFromCommandMobileDataIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is mobile data enabled");
        assertTrue(ci != null &&  ci.getCommand() == Command.MOBILE_DATA_IS_ENABLED);
    }

    @Test
    public void getFromCommandBatteryLevelGet() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get battery level");
        assertTrue(ci != null &&  ci.getCommand() == Command.BATTERY_LEVEL_GET);
    }

    @Test
    public void getFromCommandBatteryIsCharging() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is battery charging");
        assertTrue(ci != null &&  ci.getCommand() == Command.BATTERY_IS_CHARGING);
    }

    @Test
    public void getFromCommandLocationGet() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get location");
        assertTrue(ci != null &&  ci.getCommand() == Command.LOCATION_GET);
    }

    @Test
    public void getFromCommandBluetoothEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable bluetooth");
        assertTrue(ci != null &&  ci.getCommand() == Command.BLUETOOTH_ENABLE);
    }

    @Test
    public void getFromCommandBluetoothDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable bluetooth");
        assertTrue(ci != null &&  ci.getCommand() == Command.BLUETOOTH_DISABLE);
    }

    @Test
    public void getFromCommandBluetoothIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is bluetooth enabled");
        assertTrue(ci != null &&  ci.getCommand() == Command.BLUETOOTH_IS_ENABLED);
    }

    @Test
    public void getFromCommandAudioSetVolume() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set volume ring to 100%");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume music to 1");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume ring to vibrate");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume ring to silent");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_SET_VOLUME);
    }

    @Test
    public void getFromCommandAudioGetVolume() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get volume for music");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_GET_VOLUME);
    }

    @Test
    public void getFromCommandAudioGetVolumePercentage() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get volume percentage for music");
        assertTrue(ci != null &&  ci.getCommand() == Command.AUDIO_GET_VOLUME_PERCENTAGE);
    }

    @Test
    public void getFromCommandDisplayGetBrightness() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get brightness");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_GET_BRIGHTNESS);
    }

    @Test
    public void getFromCommandDisplaySetBrightness() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set brightness to 50%");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_BRIGHTNESS);

        ci = CommandInstance.CreateFromCommand("set brightness to 0");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_BRIGHTNESS);

        ci = CommandInstance.CreateFromCommand("set brightness to auto");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_BRIGHTNESS);
    }

    @Test
    public void getFromCommandDisplayGetOffTimeout() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get display off timeout");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_GET_OFF_TIMEOUT);
    }

    @Test
    public void getFromCommandDisplaySetOffTimeout() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set display off timeout to 10000.888ms");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_OFF_TIMEOUT);

        ci = CommandInstance.CreateFromCommand("set display off timeout to 10000s");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_OFF_TIMEOUT);

        ci = CommandInstance.CreateFromCommand("set display off timeout to 0.1min");
        assertTrue(ci != null &&  ci.getCommand() == Command.DISPLAY_SET_OFF_TIMEOUT);
    }

*/
}