package tranquvis.simplesmsremote.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

import tranquvis.simplesmsremote.Data.AppDataManager;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.Services.SMSReceiverService;

/**
 * Created by Andi on 01.09.2016.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppDataManager dataManager = AppDataManager.getDefault();
        try {
            dataManager.LoadUserData(context);
            if (dataManager.getUserData().getUserSettings().isStartReceiverOnSystemStart()) {
                SMSReceiverService.start(context,
                        dataManager.getUserData().getUserSettings().isReceiverStartForeground());
                Log.i("receiver", "service started successful");
            }
        } catch (Exception e) {
            Log.e("bootCompletedReceiver", "failed to start service");
            e.printStackTrace();
            dataManager.tryAddLogEntry(LogEntry.Predefined.AfterBootReceiverStartFailedUnexpected(
                    context), context);
            MyNotificationManager.getInstance(context).notifyStartReceiverAfterBootFailed();
        }
    }
}
