package tranquvis.simplesmsremote.Data;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import tranquvis.simplesmsremote.Helper.*;

/**
 * Created by Andreas Kaltenleitner on 23.08.2016.
 */
public class ControlAction
{
    public static final ControlAction HOTSPOT_ENABLE;
    public static final ControlAction HOTSPOT_DISABLE;
    public static final ControlAction MOBILE_DATA_ENABLE;
    public static final ControlAction MOBILE_DATA_DISABLE;

    private static final ControlAction[] All_ACTIONS;

    static {
        HOTSPOT_ENABLE = new ControlAction("hotspot_enable", "enable hotspot",
                -1, -1,
                new String[]{
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_SETTINGS
                }
        );
        HOTSPOT_DISABLE = new ControlAction("hotspot_disable", "disable hotspot",
                -1, -1,
                new String[]{
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_SETTINGS
                }
        );

        MOBILE_DATA_ENABLE = new ControlAction("mobile_data_enable", "enable mobile data",
                -1, Build.VERSION_CODES.LOLLIPOP,
                new String[]{
                    Manifest.permission.CHANGE_NETWORK_STATE
                }
        );

        MOBILE_DATA_DISABLE = new ControlAction("mobile_data_disable", "disable mobile data",
                -1, Build.VERSION_CODES.LOLLIPOP,
                new String[]{
                        Manifest.permission.CHANGE_NETWORK_STATE
                }
        );

        All_ACTIONS = new ControlAction[]{
            HOTSPOT_ENABLE, HOTSPOT_DISABLE, MOBILE_DATA_ENABLE, MOBILE_DATA_DISABLE
        };
    }

    public static ControlAction getFromId(String id)
    {
        for (ControlAction controlAction : All_ACTIONS)
        {
            if (controlAction.getId().equals(id))
                return controlAction;
        }
        return null;
    }

    public static ControlAction getFromCommand(String command)
    {
        for (ControlAction controlAction : All_ACTIONS)
        {
            if (controlAction.getCommand().equals(command))
                return controlAction;
        }
        return null;
    }

    private String id;
    private String command;
    private int sdkMin;
    private int sdkMax;
    private String[] requiredPermissions;

    public ControlAction(String id, String command, int sdkMin, int sdkMax, String[] requiredPermissions) {
        this.id = id;
        this.command = command;
        this.sdkMin = sdkMin;
        this.sdkMax = sdkMax;
        this.requiredPermissions = requiredPermissions;
    }

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public int getSdkMin() {
        return sdkMin;
    }

    public int getSdkMax() {
        return sdkMax;
    }

    public ControlActionUserData getUserData()
    {
        return DataManager.getUserDataForControlAction(this);
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlAction that = (ControlAction) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    public boolean execute(Context context)
    {
        if(this.equals(ControlAction.HOTSPOT_ENABLE))
        {
            return HotspotHelper.setHotspotState(context, true);
        }
        if(this.equals(ControlAction.HOTSPOT_DISABLE))
        {
            return HotspotHelper.setHotspotState(context, false);
        }
        if(this.equals(ControlAction.MOBILE_DATA_ENABLE))
        {
            return MobileDataHelper.setMobileDataState(context, true);
        }
        if(this.equals(ControlAction.MOBILE_DATA_DISABLE))
        {
            return MobileDataHelper.setMobileDataState(context, false);
        }
        return false;
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

    public static ControlAction[] getAllControlActions()
    {
        return All_ACTIONS.clone();
    }
}
