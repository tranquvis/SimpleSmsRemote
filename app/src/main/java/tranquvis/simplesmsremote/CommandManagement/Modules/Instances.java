package tranquvis.simplesmsremote.CommandManagement.Modules;

/**
 * Created by Kaltenleitner Andreas on 27.10.2016.
 */

import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Module;

/**
 * Contains all registered Modules
 */
public class Instances {
    public static final ModuleWifiHotspot WIFI_HOTSPOT = new ModuleWifiHotspot();
    public static final ModuleWifi WIFI = new ModuleWifi();
    public static final ModuleMobileData MOBILE_DATA = new ModuleMobileData();
    public static final ModuleCamera CAMERA = new ModuleCamera();


    /**
     * Get all registered modules using system reflection.
     * @param sortComparator This comparator is used to sort the list. <br/>
     *                       Use {@code DefaultComparator} to get default order.
     * @return (sorted) list of all modules
     * @see Comparator
     */
    public static List<Module> GetAll(@Nullable Comparator<Module> sortComparator)
    {
        List<Module> modules = new ArrayList<>();
        for (Field field : Instances.class.getDeclaredFields())
        {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && Module.class.isAssignableFrom(field.getType()))
            {
                try
                {
                    Module module = (Module) field.get(null);

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
}
