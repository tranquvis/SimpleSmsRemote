package tranquvis.simplesmsremote.Data;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class ModuleUserData implements Serializable {
    private final String controlModuleId;
    private final ModuleSettingsData settings;

    public ModuleUserData(String controlModuleId, @Nullable ModuleSettingsData settings) {
        this.controlModuleId = controlModuleId;
        this.settings = settings;
    }

    public String getModuleId() {
        return controlModuleId;
    }

    public ModuleSettingsData getSettings() {
        return settings;
    }

    public ModuleUserData withSettings(ModuleSettingsData settings) {
        return new ModuleUserData(getModuleId(), settings);
    }
}
