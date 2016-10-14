package tranquvis.simplesmsremote.CommandManagement;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.PermissionUtils;

/**
 * Created by Andreas Kaltenleitner on 23.08.2016.
 */
public class ControlModule
{
    public static final ControlModule
            WIFI_HOTSPOT, MOBILE_DATA, BATTERY, LOCATION, WIFI, BLUETOOTH, AUDIO, DISPLAY;

    static
    {
        //region
        WIFI_HOTSPOT = new ControlModule("wifi_hotspot",
                new ControlCommand[]{
                        ControlCommand.WIFI_HOTSPOT_ENABLE,
                        ControlCommand.WIFI_HOTSPOT_DISABLE,
                        ControlCommand.WIFI_HOTSPOT_IS_ENABLED
                });
        WIFI_HOTSPOT.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_SETTINGS
        };
        WIFI_HOTSPOT.titleRes = R.string.control_module_title_wifi_hotspot;
        WIFI_HOTSPOT.descriptionRes = R.string.control_module_desc_wifi_hotspot;
        WIFI_HOTSPOT.iconRes = R.drawable.ic_wifi_tethering_grey_700_36dp;
        //endregion

        //region mobile data
        MOBILE_DATA = new ControlModule("mobile_data",
                new ControlCommand[]{
                        ControlCommand.MOBILE_DATA_ENABLE,
                        ControlCommand.MOBILE_DATA_DISABLE,
                        ControlCommand.MOBILE_DATA_IS_ENABLED
                });
        MOBILE_DATA.sdkMax = Build.VERSION_CODES.LOLLIPOP;
        MOBILE_DATA.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        MOBILE_DATA.titleRes = R.string.control_module_title_mobile_data;
        MOBILE_DATA.descriptionRes = R.string.control_module_desc_mobile_data;
        MOBILE_DATA.iconRes = R.drawable.ic_network_cell_grey_700_36dp;
        //endregion

        //region battery
        BATTERY = new ControlModule("battery",
                new ControlCommand[]{
                        ControlCommand.BATTERY_LEVEL_GET,
                        ControlCommand.BATTERY_IS_CHARGING
                });
        BATTERY.titleRes = R.string.control_module_title_battery;
        BATTERY.descriptionRes = R.string.control_module_desc_battery;
        BATTERY.iconRes = R.drawable.ic_battery_50_grey_700_36dp;
        //endregion

        //region location
        LOCATION = new ControlModule("location",
                new ControlCommand[]{
                        ControlCommand.LOCATION_GET
                });
        LOCATION.requiredPermissions =
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
        LOCATION.titleRes = R.string.control_module_title_location;
        LOCATION.descriptionRes = R.string.control_module_desc_location;
        LOCATION.iconRes = R.drawable.ic_location_on_grey_700_36dp;
        //endregion

        //region wifi
        WIFI = new ControlModule("wifi",
                new ControlCommand[]{
                        ControlCommand.WIFI_ENABLE,
                        ControlCommand.WIFI_DISABLE,
                        ControlCommand.WIFI_IS_ENABLED
                });
        WIFI.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_SETTINGS
        };
        WIFI.titleRes = R.string.control_module_title_wifi;
        WIFI.descriptionRes = R.string.control_module_desc_wifi;
        WIFI.iconRes = R.drawable.ic_signal_wifi_2_bar_grey_700_36dp;
        //endregion

        //region bluetooth
        BLUETOOTH = new ControlModule("bluetooth",
                new ControlCommand[]{
                        ControlCommand.BLUETOOTH_ENABLE,
                        ControlCommand.BLUETOOTH_DISABLE,
                        ControlCommand.BLUETOOTH_IS_ENABLED
                });
        BLUETOOTH.requiredPermissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        BLUETOOTH.titleRes = R.string.control_module_title_bluetooth;
        BLUETOOTH.descriptionRes = R.string.control_module_desc_bluetooth;
        BLUETOOTH.iconRes = R.drawable.ic_bluetooth_grey_700_36dp;

        //region audio
        AUDIO = new ControlModule("audio",
                new ControlCommand[]{
                        ControlCommand.AUDIO_SET_VOLUME,
                        ControlCommand.AUDIO_GET_VOLUME,
                        ControlCommand.AUDIO_GET_VOLUME_PERCENTAGE
                });
        AUDIO.requiredPermissions = new String[]{
                Manifest.permission.ACCESS_NOTIFICATION_POLICY //API requirements can be ignored
        };
        AUDIO.titleRes = R.string.control_module_title_audio;
        AUDIO.descriptionRes = R.string.control_module_desc_audio;
        AUDIO.iconRes = R.drawable.ic_volume_up_grey_700_36dp;
        AUDIO.paramInfoRes = R.string.control_module_param_desc_audio;
        //endregion

        //region display
        DISPLAY = new ControlModule("display",
                new ControlCommand[]{
                        ControlCommand.DISPLAY_GET_BRIGHTNESS,
                        ControlCommand.DISPLAY_SET_BRIGHTNESS,
                        ControlCommand.DISPLAY_GET_OFF_TIMEOUT,
                        ControlCommand.DISPLAY_SET_OFF_TIMEOUT,
                        ControlCommand.DISPLAY_TURN_OFF
                });
        DISPLAY.requiredPermissions = new String[]{
                Manifest.permission.WRITE_SETTINGS
        };
        DISPLAY.titleRes = R.string.control_module_title_display;
        DISPLAY.descriptionRes = R.string.control_module_desc_display;
        DISPLAY.iconRes = R.drawable.ic_settings_brightness_grey_700_36dp;
        DISPLAY.paramInfoRes = R.string.control_module_param_desc_display;
        //endregion
    }

    public static ControlModule getFromId(String id)
    {
        for (ControlModule controlModule : GetAllModules(null))
        {
            if (controlModule.getId().equals(id))
                return controlModule;
        }
        return null;
    }

    public static ControlModule getFromCommand(ControlCommand command)
    {
        for (ControlModule controlModule : GetAllModules(null))
        {
            if (ArrayUtils.contains(controlModule.getCommands(), command))
                return controlModule;
        }
        return null;
    }

    private String id;
    private ControlCommand[] commands;
    private int sdkMin = -1;
    private int sdkMax = -1;
    private String[] requiredPermissions;

    private int titleRes = -1;
    private int descriptionRes = -1;
    private int iconRes = -1;
    private int paramInfoRes = -1;

    private ControlModule(String id, ControlCommand[] commands)
    {
        this.id = id;
        this.commands = commands;
    }

    public String getId()
    {
        return id;
    }

    private ControlCommand[] getCommands()
    {
        return commands;
    }

    public String getCommandsString()
    {
        String str = "";
        for (ControlCommand com : commands)
        {
            if (com != commands[0])
                str += "\r\n";
            str += com.toString();
        }
        return str;
    }

    public int getSdkMin()
    {
        return sdkMin;
    }

    public int getSdkMax()
    {
        return sdkMax;
    }

    public int getTitleRes()
    {
        return titleRes;
    }

    public int getDescriptionRes()
    {
        return descriptionRes;
    }

    public int getIconRes()
    {
        return iconRes;
    }

    public int getParamInfoRes()
    {
        return paramInfoRes;
    }

    /**
     * get required permissions that are not granted so far
     *
     * @param context app context
     * @return permissions
     */
    public String[] getRequiredPermissions(Context context)
    {
        if (requiredPermissions == null)
            return new String[]{};
        return PermissionUtils.FilterAppPermissions(context, requiredPermissions);
    }

    public ControlModuleUserData getUserData()
    {
        return DataManager.getUserDataForControlModule(this);
    }

    /**
     * Check if control module is compatible with the executing android system.
     *
     * @return true if compatible
     */
    public boolean isCompatible()
    {
        return Build.VERSION.SDK_INT >= sdkMin && (sdkMax == -1 || Build.VERSION.SDK_INT <= sdkMax);
    }

    /**
     * Check if this control module is enabled. Make sure to load userdata before.
     *
     * @return if module is enabled
     */
    public boolean isEnabled()
    {
        return getUserData() != null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlModule that = (ControlModule) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Check if required permissions for this module are granted.
     *
     * @param context app context
     * @return true if granted
     */
    boolean checkPermissions(Context context)
    {
        if (requiredPermissions == null)
            return true;
        return PermissionUtils.AppHasPermissions(context, requiredPermissions);
    }

    /**
     * Get all defined modules
     *
     * @param sortComparator This comparator is used to sort the list. <br/>
     *                       Use {@code DefaultComparator} to get default order.
     * @return (sorted) list of all modules
     * @see Comparator
     */
    public static List<ControlModule> GetAllModules(@Nullable Comparator<ControlModule> sortComparator)
    {
        List<ControlModule> modules = new ArrayList<>();

        for (Field field : ControlModule.class.getDeclaredFields())
        {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getType() == ControlModule.class)
            {
                try
                {
                    ControlModule module = (ControlModule) field.get(null);

                    boolean inserted = false;
                    if (sortComparator != null)
                    {
                        for (int i = 0; i < modules.size(); i++)
                        {
                            int compareResult = sortComparator.compare(modules.get(i), module);
                            if (compareResult > 0)
                            {
                                modules.add(i, module);
                                inserted = true;
                                break;
                            }
                        }
                    }
                    if (!inserted) modules.add(module);

                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return modules;
    }

    public static Comparator<ControlModule> GetDefaultComparator(final Context context)
    {
        return new Comparator<ControlModule>()
        {
            @Override
            public int compare(ControlModule module1, ControlModule module2)
            {
                if ((module2.isEnabled() && !module1.isEnabled())
                        || (module2.isCompatible() && !module1.isCompatible()))
                    return 1;
                return context.getString(module1.getTitleRes())
                        .compareTo(context.getString(module2.getTitleRes()));
            }
        };
    }
}
