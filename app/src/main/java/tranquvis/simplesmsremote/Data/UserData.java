package tranquvis.simplesmsremote.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class UserData implements Serializable {
    private final List<ModuleUserData> modules;
    private final UserSettings userSettings;

    public UserData(List<ModuleUserData> modules, UserSettings userSettings) {
        this.modules = modules;
        this.userSettings = userSettings;
    }

    public List<ModuleUserData> getModules() {
        return modules;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void addModule(ModuleUserData userData) {
        modules.add(userData);
    }

    public void updateModule(ModuleUserData moduleUserData) {
        int i = 0;
        for (ModuleUserData userData : modules) {
            if (userData.getModuleId().equals(moduleUserData.getModuleId())) {
                modules.set(i, moduleUserData);
                break;
            }
            i++;
        }
    }

    public void removeModule(String moduleId) {
        for (ModuleUserData userData : modules) {
            if (userData.getModuleId().equals(moduleId)) {
                modules.remove(userData);
                break;
            }
        }
    }

    public List<String> getAllUsedPhones() {
        List<String> phones = new ArrayList<>();
        for (ModuleUserData moduleUserData : modules) {
            if (!(moduleUserData instanceof PhoneAllowlistModuleUserData)) continue;
            PhoneAllowlistModuleUserData phonesUserData = (PhoneAllowlistModuleUserData) moduleUserData;
            for (String phone : phonesUserData.getGrantedPhones()) {
                if (phone != null && phone.length() > 0 && !phones.contains(phone))
                    phones.add(phone);
            }
        }
        return phones;
    }
}
