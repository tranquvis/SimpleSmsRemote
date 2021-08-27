package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tranquvis.simplesmsremote.Activities.ModuleActivity;
import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Utils.PermissionUtils;

/**
 * Created by Andreas Kaltenleitner on 23.08.2016.
 */
public abstract class Module {
    protected String id = getClass().getName();
    protected int sdkMin = -1;
    protected int sdkMax = -1;
    protected String[] requiredPermissions;

    protected int titleRes = -1;
    protected int descriptionRes = -1;
    protected int iconRes = -1;
    protected int paramInfoRes = -1;

    protected Class<? extends ModuleActivity> configurationActivityType =
            ModuleActivity.class;

    /**
     * Get module from its id.
     *
     * @param id the module's id
     * @return module or null no matching module was found
     */
    public static Module getFromId(String id) {
        for (Module module : Instances.GetAll(null)) {
            if (module.getId().equals(id))
                return module;
        }
        return null;
    }

    public static Comparator<Module> GetDefaultComparator(final Context context,
            final DataManager userDataProvider) {
        return new Comparator<Module>() {
            @Override
            public int compare(Module module1, Module module2) {
                if ((userDataProvider.isModuleEnabled(module2)
                        && !userDataProvider.isModuleEnabled(module1)
                    ) || (module2.isCompatible(context) && !module1.isCompatible(context)))
                    return 1;
                if (module1.getTitleRes() != -1 && module2.getTitleRes() != -1) {
                    return context.getString(module1.getTitleRes())
                            .compareTo(context.getString(module2.getTitleRes()));
                }
                return module1.getId().compareTo(module2.getId());
            }
        };
    }

    public String getId() {
        return getClass().getSimpleName();
    }

    public int getSdkMin() {
        return sdkMin;
    }

    public int getSdkMax() {
        return sdkMax;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getParamInfoRes() {
        return paramInfoRes;
    }

    public Class<? extends ModuleActivity> getConfigurationActivityType() {
        return configurationActivityType;
    }

    /**
     * Get all commands of this module by using reflection.
     *
     * @return all defined commands in this module
     */
    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();

        for (Field field : this.getClass().getDeclaredFields()) {

            if (java.lang.reflect.Modifier.isFinal(field.getModifiers())
                    && Command.class.isAssignableFrom(field.getType())) {
                try {
                    commands.add((Command) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return commands;
    }

    /**
     * Get required permissions that are not granted so far.
     *
     * @param context app context
     * @return permissions
     */
    public String[] getRequiredPermissions(Context context) {
        if (requiredPermissions == null)
            return new String[]{};
        return PermissionUtils.FilterAppPermissions(context, requiredPermissions);
    }

    /**
     * Check if control module is compatible with the executing android system.
     *
     * @return true if compatible
     */
    public boolean isCompatible(Context context) {
        return Build.VERSION.SDK_INT >= sdkMin && (sdkMax == -1 || Build.VERSION.SDK_INT <= sdkMax);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module that = (Module) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Check if required permissions for this module are granted.
     *
     * @param context app context
     * @return true if granted
     */
    public boolean checkPermissions(Context context) {
        return requiredPermissions == null
                || PermissionUtils.AppHasPermissions(context, requiredPermissions);
    }
}
