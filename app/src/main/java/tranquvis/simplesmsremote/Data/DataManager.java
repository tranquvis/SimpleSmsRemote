package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 28.08.2016.
 */
public class DataManager {
    private static final String FILENAME = "user.data";

    private static List<ControlActionUserData> ControlActionsUserData;

    public static ControlActionUserData getUserDataForControlAction(ControlAction controlAction)
    {
        for (ControlActionUserData userData : ControlActionsUserData) {
            if(userData.getControlActionId().equals(controlAction.getId()))
                return userData;
        }
        return null;
    }

    public static void LoadData(Context context) throws IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        ControlActionsUserData = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if((line = line.trim()).equals(""))
                continue;
            try {
                ControlActionUserData controlActionUserData = ControlActionUserData.Parse(line);
                ControlActionsUserData.add(controlActionUserData);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        fis.close();
    }

    public static void SaveData(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
        BufferedWriter bw = new BufferedWriter(osw);

        for (ControlActionUserData action : ControlActionsUserData) {
            bw.write(action.toTextLine());
            bw.newLine();
        }

        fos.close();
    }
}
