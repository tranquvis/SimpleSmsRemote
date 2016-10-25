package tranquvis.simplesmsremote;

import android.provider.Settings;
import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.ControlCommand;

import static org.junit.Assert.*;

/**
 * Created by Andi on 07.10.2016.
 */
public class CommandInstanceTest {

    @Test
    public void getFromCommandGeneral() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set   volume \"tt \n''t\"\n to \"10 0%");
        assertTrue(ci != null);
    }

    @Test
    public void getFromCommandWifiHotspotEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable hotspot");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_HOTSPOT_ENABLE);
    }

    @Test
    public void getFromCommandWifiHotspotDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable hotspot");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_HOTSPOT_DISABLE);
    }

    @Test
    public void getFromCommandWifiHotspotIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is hotspot enabled");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_HOTSPOT_IS_ENABLED);
    }

    @Test
    public void getFromCommandMobileDataEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable mobile data");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.MOBILE_DATA_ENABLE);
    }

    @Test
    public void getFromCommandMobileDataDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable mobile data");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.MOBILE_DATA_DISABLE);
    }

    @Test
    public void getFromCommandMobileDataIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is mobile data enabled");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.MOBILE_DATA_IS_ENABLED);
    }

    @Test
    public void getFromCommandBatteryLevelGet() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get battery level");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.BATTERY_LEVEL_GET);
    }

    @Test
    public void getFromCommandBatteryIsCharging() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is battery charging");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.BATTERY_IS_CHARGING);
    }

    @Test
    public void getFromCommandLocationGet() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get location");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.LOCATION_GET);
    }

    @Test
    public void getFromCommandWifiEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable wifi");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_ENABLE);
    }

    @Test
    public void getFromCommandWifiDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable wifi");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_DISABLE);
    }

    @Test
    public void getFromCommandWifiIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is wifi enabled");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.WIFI_IS_ENABLED);
    }

    @Test
    public void getFromCommandBluetoothEnable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("enable bluetooth");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.BLUETOOTH_ENABLE);
    }

    @Test
    public void getFromCommandBluetoothDisable() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("disable bluetooth");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.BLUETOOTH_DISABLE);
    }

    @Test
    public void getFromCommandBluetoothIsEnabled() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("is bluetooth enabled");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.BLUETOOTH_IS_ENABLED);
    }

    @Test
    public void getFromCommandAudioSetVolume() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set volume ring to 100%");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume music to 1");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume ring to vibrate");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_SET_VOLUME);

        ci = CommandInstance.CreateFromCommand("set volume ring to silent");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_SET_VOLUME);
    }

    @Test
    public void getFromCommandAudioGetVolume() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get volume for music");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_GET_VOLUME);
    }

    @Test
    public void getFromCommandAudioGetVolumePercentage() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get volume percentage for music");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.AUDIO_GET_VOLUME_PERCENTAGE);
    }

    @Test
    public void getFromCommandDisplayGetBrightness() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get brightness");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_GET_BRIGHTNESS);
    }

    @Test
    public void getFromCommandDisplaySetBrightness() throws Exception {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set brightness to 50%");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_BRIGHTNESS);

        ci = CommandInstance.CreateFromCommand("set brightness to 0");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_BRIGHTNESS);

        ci = CommandInstance.CreateFromCommand("set brightness to auto");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_BRIGHTNESS);
    }

    @Test
    public void getFromCommandDisplayGetOffTimeout() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("get display off timeout");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_GET_OFF_TIMEOUT);
    }

    @Test
    public void getFromCommandDisplaySetOffTimeout() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("set display off timeout to 10000.888ms");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_OFF_TIMEOUT);

        ci = CommandInstance.CreateFromCommand("set display off timeout to 10000s");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_OFF_TIMEOUT);

        ci = CommandInstance.CreateFromCommand("set display off timeout to 0.1min");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_SET_OFF_TIMEOUT);
    }

    @Test
    public void getFromCommandCameraTakePictureSimple() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("take picture");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.CAMERA_TAKE_PICTURE);
    }

    @Test
    public void getFromCommandCameraTakePicture() throws Exception
    {
        CommandInstance ci =
                CommandInstance.CreateFromCommand("take picture with \" \"");
        assertTrue(ci != null &&  ci.getCommand() == ControlCommand.DISPLAY_TURN_OFF);
    }

    @Test
    public void temp() throws Exception
    {
        Pattern p = Pattern.compile("take\\s+picture\\s+with\\s+(flash|)\\s+");
        Matcher m = p.matcher("take picture with flash ");

        List<String> groups;
        if(m.find())
        {
            groups = new ArrayList<>();
            for (int i = 0; i <= m.groupCount(); i++)
            {
                groups.add(m.group(i));
            }
            System.out.print("");
        }
    }
}