package tranquvis.simplesmsremote.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlModule;

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

    public void setControlModule(ControlModuleUserData moduleUserData)
    {
        int i = 0;
        for (ControlModuleUserData userData : controlModules)
        {
            if(userData.getControlModuleId().equals(moduleUserData.getControlModuleId()))
            {
                controlModules.set(i, moduleUserData);
                break;
            }
            i++;
        }
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

    public List<String> getAllUsedPhones()
    {
        List<String> phones = new ArrayList<>();
        for(ControlModuleUserData moduleUserData : controlModules)
        {
            for(String phone : moduleUserData.getGrantedPhones())
            {
                if(phone != null && phone.length() > 0 && !phones.contains(phone))
                    phones.add(phone);
            }
        }
        return phones;
    }
}
