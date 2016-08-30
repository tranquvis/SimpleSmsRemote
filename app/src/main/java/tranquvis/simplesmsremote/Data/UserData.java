package tranquvis.simplesmsremote.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class UserData implements Serializable
{
    private List<ControlModuleUserData> controlModules;
    private UserSettings userSettings;

    public UserData(List<ControlModuleUserData> controlModules, UserSettings userSettings)
    {
        this.controlModules = controlModules;
        this.userSettings = userSettings;
    }

    public List<ControlModuleUserData> getControlModules()
    {
        return controlModules;
    }

    public UserSettings getUserSettings()
    {
        return userSettings;
    }

    public void addControlModule(ControlModuleUserData userData)
    {
        controlModules.add(userData);
    }

    public void removeControlModule(String moduleId)
    {
        for (ControlModuleUserData userData : controlModules)
        {
            if(userData.getControlModuleId().equals(moduleId))
            {
                controlModules.remove(userData);
                break;
            }
        }
    }
}
