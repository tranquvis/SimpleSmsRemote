package tranquvis.simplesmsremote;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Data.ModuleSettingsData;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.Data.PhoneAllowlistModuleUserData;
import tranquvis.simplesmsremote.Data.UserData;
import tranquvis.simplesmsremote.Data.UserSettings;

public class TestDataManager implements DataManager {
    private final UserData userData = new UserData(new ArrayList<>(), new UserSettings());
    private final List<LogEntry> log = new ArrayList<>();

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public List<LogEntry> getLog() {
        return log;
    }

    @Override
    public void addLogEntry(LogEntry logEntry, Context context) throws IOException {
        log.add(logEntry);
    }

    public void enableModule(Module module) {
        enableModule(module, null);
    }

    public void enableModule(Module module, ModuleSettingsData settings) {
        if(module.equals(Instances.GRANT_PHONE_REMOTELY)) {
            userData.addModule(new ModuleUserData(module.getId(), settings));
        } else {
            userData.addModule(new PhoneAllowlistModuleUserData(module.getId(), null,
                    settings));
        }
    }

    public boolean isPhoneGranted(Module module, String phone) {
        PhoneAllowlistModuleUserData moduleUserData =
                (PhoneAllowlistModuleUserData) getModuleUserData(module);
        return moduleUserData.isPhoneGranted(phone);
    }

    @Override
    public void LoadUserData(Context context) throws IOException {

    }

    @Override
    public void SaveUserData(Context context) throws IOException {

    }

    @Override
    public void LoadLog(Context context) throws IOException {

    }
}
