package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import tranquvis.simplesmsremote.ControlModule;

/**
 * Created by Andi on 28.08.2016.
 */
public class DataManager {
    private static final String FILENAME = "user.data";

    private static UserData userData;

    public static ControlModuleUserData getUserDataForControlModule(ControlModule controlModule)
    {
        for (ControlModuleUserData moduleUserData : userData.getControlModules()) {
            if(moduleUserData.getControlModuleId().equals(controlModule.getId()))
                return moduleUserData;
        }
        return null;
    }

    public static UserData getUserData()
    {
        return userData;
    }

    public static void LoadData(Context context) throws IOException {
        FileInputStream fis;
        try
        {
            fis = context.openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            userData = new UserData(new ArrayList<ControlModuleUserData>(), new UserSettings());
            return;
        }

        ObjectInputStream os = new ObjectInputStream(fis);
        try
        {
            userData = (UserData) os.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ClassCastException cce)
        {
            userData = new UserData(new ArrayList<ControlModuleUserData>(), new UserSettings());
        }
        os.close();
        fis.close();
    }

    public static void SaveData(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(userData);
        os.close();
        fos.close();
    }
}
