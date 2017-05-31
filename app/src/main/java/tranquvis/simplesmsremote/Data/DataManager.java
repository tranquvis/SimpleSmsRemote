package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Modules.Module;

/**
 * Created by Andi on 28.08.2016.
 */
public class DataManager {
    private static final String FILENAME_LOG = "log";
    private static final String FILENAME_USER_DATA = "user.data";

    private static UserData userData;
    private static List<LogEntry> log;

    private static boolean firstStart;

    public static ControlModuleUserData getUserDataForControlModule(Module module) {
        if(userData == null) return null;
        for (ControlModuleUserData moduleUserData : userData.getControlModules()) {
            if (moduleUserData.getControlModuleId().equals(module.getId()))
                return moduleUserData;
        }
        return null;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static List<LogEntry> getLog() {
        return log;
    }

    public static boolean isFirstStart() {
        return firstStart;
    }

    public static void LoadLog(Context context) throws IOException {
        log = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = context.openFileInput(FILENAME_LOG);
        } catch (FileNotFoundException e) {
            return;
        }

        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) {
            LogEntry logEntry = LogEntry.parseFromTextLine(line);
            if (logEntry != null) log.add(logEntry);
        }

        br.close();
        isr.close();
        fis.close();
    }

    public static void LoadUserData(Context context) throws IOException {
        FileInputStream fis;
        try {
            fis = context.openFileInput(FILENAME_USER_DATA);
        } catch (FileNotFoundException e) {
            //apply default values
            firstStart = true;
            userData = new UserData(new ArrayList<ControlModuleUserData>(), new UserSettings());
            SaveUserData(context);
            return;
        }

        ObjectInputStream os = new ObjectInputStream(fis);
        try {
            userData = (UserData) os.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException | InvalidClassException cce) {
            userData = new UserData(new ArrayList<ControlModuleUserData>(), new UserSettings());
        }
        os.close();
        fis.close();

        firstStart = false;
    }

    public static void SaveUserData(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME_USER_DATA, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(userData);
        os.close();
        fos.close();
    }

    /**
     * add log entry and save to file
     *
     * @param logEntry log entry
     * @param context  file context
     */
    public static void addLogEntry(LogEntry logEntry, Context context) {
        if (log != null) log.add(0, logEntry);
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_LOG, Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(fos);
            writer.println(logEntry.toTextLine());
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear log and save to file
     *
     * @param context file context
     */
    public static void clearLog(Context context) {
        if (log != null) log.clear();
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_LOG, Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            writer.print("");
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
