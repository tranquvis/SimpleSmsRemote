package tranquvis.simplesmsremote.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class UserData implements Serializable {
    private List<ModuleUserData> controlModules;
    private UserSettings userSettings;

    public UserData(List<ModuleUserData> controlModules, UserSettings userSettings) {
        this.controlModules = controlModules;
        this.userSettings = userSettings;
    }

    public List<ModuleUserData> getControlModules() {
        return controlModules;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void addControlModule(ModuleUserData userData) {
        controlModules.add(userData);
    }

    public void setControlModule(ModuleUserData moduleUserData) {
        int i = 0;
        for (ModuleUserData userData : controlModules) {
            if (userData.getControlModuleId().equals(moduleUserData.getControlModuleId())) {
                controlModules.set(i, moduleUserData);
                break;
            }
            i++;
        }
    }

    public void removeControlModule(String moduleId) {
        for (ModuleUserData userData : controlModules) {
            if (userData.getControlModuleId().equals(moduleId)) {
                controlModules.remove(userData);
                break;
            }
        }
    }

    public List<String> getAllUsedPhones() {
        List<String> phones = new ArrayList<>();
        for (ModuleUserData moduleUserData : controlModules) {
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
