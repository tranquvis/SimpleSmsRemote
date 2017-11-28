package tranquvis.simplesmsremote.CommandManagement.Modules;

/**
 * Created by Kaltenleitner Andreas on 27.10.2016.
 */

import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains all registered Modules
 */
public class Instances {
    public static final ModuleWifiHotspot WIFI_HOTSPOT = new ModuleWifiHotspot();
    public static final ModuleWifi WIFI = new ModuleWifi();
    public static final ModuleMobileData MOBILE_DATA = new ModuleMobileData();
    public static final ModuleCamera CAMERA = new ModuleCamera();
    public static final ModuleAudio AUDIO = new ModuleAudio();
    public static final ModuleBattery BATTERY = new ModuleBattery();
    public static final ModuleBluetooth BLUETOOTH = new ModuleBluetooth();
    public static final ModuleDisplay DISPLAY = new ModuleDisplay();
    public static final ModuleLocation LOCATION = new ModuleLocation();

    private static boolean commandsInitialized = false;

    /**
     * Get all registered modules using system reflection.
     *
     * @param sortComparator This comparator is used to sort the list. <br/>
     *                       Use {@code DefaultComparator} to get default order.
     * @return (sorted) list of all modules
     * @see Comparator
     */
    public static List<Module> GetAll(@Nullable Comparator<Module> sortComparator) {
        List<Module> modules = new ArrayList<>();
        for (Field field : Instances.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && Module.class.isAssignableFrom(field.getType())) {
                try {
                    Module module = (Module) field.get(null);

                    boolean inserted = false;
                    if (sortComparator != null) {
                        for (int i = 0; i < modules.size(); i++) {
                            int compareResult = sortComparator.compare(modules.get(i), module);
                            if (compareResult > 0) {
                                modules.add(i, module);
                                inserted = true;
                                break;
                            }
                        }
                    }
                    if (!inserted) modules.add(module);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return modules;
    }

    public static void InitCommands() {
        if (commandsInitialized) return;

        for (Field field : Instances.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && Module.class.isAssignableFrom(field.getType())) {
                try {
                    Module module = (Module) field.get(null);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        commandsInitialized = true;
    }
}
