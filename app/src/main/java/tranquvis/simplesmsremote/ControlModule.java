package tranquvis.simplesmsremote;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import org.apache.commons.lang3.ArrayUtils;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Helper.*;

/**
 * Created by Andreas Kaltenleitner on 23.08.2016.
 */
public class ControlModule
{
    private static final ControlModule[] All_ACTIONS;

    public static final ControlModule WIFI_HOTSPOT;
    public static final ControlModule MOBILE_DATA;

    static {
        WIFI_HOTSPOT = new ControlModule("wifi_hotspot",
                new ControlCommand[]{
                        ControlCommand.WIFI_HOTSPOT_ENABLE,
                        ControlCommand.WIFI_HOTSPOT_DISABLE
                },
                -1, -1,
                new String[]{
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_SETTINGS
                },
                R.string.control_module_title_wifi_hotspot,
                R.string.control_module_desc_wifi_hotspot,
                R.drawable.ic_wifi_tethering_grey_700_36dp
                );

        MOBILE_DATA = new ControlModule("mobile_data",
                new ControlCommand[]{
                        ControlCommand.MOBILE_DATA_ENABLE,
                        ControlCommand.MOBILE_DATA_DISABLE
                },
                -1, Build.VERSION_CODES.LOLLIPOP,
                new String[]{
                    Manifest.permission.CHANGE_NETWORK_STATE
                },
                R.string.control_module_title_mobile_data,
                R.string.control_module_desc_mobile_data,
                R.drawable.ic_network_cell_grey_700_36dp);

        All_ACTIONS = new ControlModule[]{
                WIFI_HOTSPOT, MOBILE_DATA
        };
    }

    public static ControlModule getFromId(String id)
    {
        for (ControlModule controlModule : All_ACTIONS)
        {
            if (controlModule.getId().equals(id))
                return controlModule;
        }
        return null;
    }

    public static ControlModule getFromCommand(String command)
    {
        for (ControlModule controlModule : All_ACTIONS)
        {
            if (ArrayUtils.contains(controlModule.getCommands(), command))
                return controlModule;
        }
        return null;
    }

    private String id;
    private ControlCommand[] commands;
    private int sdkMin;
    private int sdkMax;
    private String[] requiredPermissions;

    private int titleRes;
    private int descriptionRes;
    private int iconRes;

    public ControlModule(String id, ControlCommand[] commands, int sdkMin, int sdkMax, String[] requiredPermissions, int titleRes, int descriptionRes, int iconRes) {
        this.id = id;
        this.commands = commands;
        this.sdkMin = sdkMin;
        this.sdkMax = sdkMax;
        this.requiredPermissions = requiredPermissions;
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
        this.iconRes = iconRes;
    }

    public String getId() {
        return id;
    }

    public ControlCommand[] getCommands() {
        return commands;
    }

    public String getCommandsString(){
        String str = "";
        for (ControlCommand com : commands) {
            if(com != commands[0])
                str += "\r\n";
            str += com.toString();
        }
        return str;
    }

    public int getSdkMin() {
        return sdkMin;
    }

    public int getSdkMax() {
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

    public int getIconRes() {
        return iconRes;
    }

    /**
     * get required permissions that are not granted so far
     * @param context app context
     * @return permissions
     */
    public String[] getRequiredPermissions(Context context)
    {
        return PermissionHelper.FilterAppPermissions(context, requiredPermissions);
    }

    public ControlModuleUserData getUserData()
    {
        return DataManager.getUserDataForControlModule(this);
    }

    /**
     * check if control action is compatible with the executing android system
     * @return true if compatible
     */
    public boolean isCompatible()
    {
        return Build.VERSION.SDK_INT >= sdkMin && (sdkMax == -1 || Build.VERSION.SDK_INT <= sdkMax);
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
     * check if required permissions for this action are granted
     * @param context app context
     * @return true if granted
     */
    public boolean CheckPermissions(Context context)
    {
        return  PermissionHelper.AppHasPermissions(context, requiredPermissions);
    }

    public static ControlModule[] getAllControlActions()
    {
        return All_ACTIONS.clone();
    }
}
