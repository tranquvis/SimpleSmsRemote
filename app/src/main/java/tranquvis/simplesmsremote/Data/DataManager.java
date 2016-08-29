package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlModule;

/**
 * Created by Andi on 28.08.2016.
 */
public class DataManager {
    private static final String FILENAME = "user.data";

    private static List<ControlModuleUserData> ControlModulesUserData;

    public static ControlModuleUserData getUserDataForControlModule(ControlModule controlModule)
    {
        for (ControlModuleUserData userData : ControlModulesUserData) {
            if(userData.getControlModuleId().equals(controlModule.getId()))
                return userData;
        }
        return null;
    }

    public static void LoadData(Context context) throws IOException {
        ControlModulesUserData = new ArrayList<>();
        FileInputStream fis;
        try
        {
            fis = context.openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            return;
        }
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) {
            if((line = line.trim()).equals(""))
                continue;
            try {
                ControlModuleUserData controlModuleUserData = ControlModuleUserData.Parse(line);
                ControlModulesUserData.add(controlModuleUserData);
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

        for (ControlModuleUserData action : ControlModulesUserData) {
            bw.write(action.toTextLine());
            bw.newLine();
        }

        fos.close();
    }
}
