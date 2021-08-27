package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;

public interface DataManager {
    UserData getUserData();

    List<LogEntry> getLog();

    void LoadUserData(Context context) throws IOException;
    void SaveUserData(Context context) throws IOException;
    void LoadLog(Context context) throws IOException;
    void addLogEntry(LogEntry logEntry, Context context) throws IOException;

    default void tryAddLogEntry(LogEntry logEntry, Context context) {
        try {
            addLogEntry(logEntry, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default ModuleUserData getModuleUserData(Module module) {
        if(getUserData() == null) return null;
        for (ModuleUserData moduleUserData : getUserData().getModules()) {
            if (moduleUserData.getModuleId().equals(module.getId()))
                return moduleUserData;
        }
        return null;
    }

    default boolean isModuleEnabled(Module module) {
        return getModuleUserData(module) != null;
    }

    default List<Module> getEnabledPhoneAllowlistModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        Collection<Module> modules = Instances.GetAll(null);

        for (Module module : modules) {
            if (!isModuleEnabled(module)) continue;
            if (!(getModuleUserData(module) instanceof PhoneAllowlistModuleUserData)) continue;
            enabledModules.add(module);
        }

        return enabledModules;
    }
}
