package tranquvis.simplesmsremote;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 28.08.2016.
 */
public class DataManager {
    private static DataManager ourInstance = new DataManager();
    private static final String FILENAME = "user.data";

    public static DataManager getInstance() {
        return ourInstance;
    }

    public List<ControlAction> UserControlActions;

    private DataManager() {
    }

    public void LoadData(Context context) throws IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        UserControlActions = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            ControlAction controlAction = ControlAction.getFromId(line);
            if(controlAction != null)
                UserControlActions.add(controlAction);
        }

        fis.close();
    }

    public void SaveData() {

    }
}
