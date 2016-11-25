package tranquvis.simplesmsremote.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        try {
            DataManager.LoadUserData(context);
            if (DataManager.getUserData().getUserSettings().isStartReceiverOnSystemStart()) {
                SMSReceiverService.start(context,
                        DataManager.getUserData().getUserSettings().isReceiverStartForeground());
                Log.i("receiver", "service started successful");
            }
        } catch (Exception e) {
            Log.e("bootCompletedReceiver", "failed to start service");
            e.printStackTrace();
            DataManager.addLogEntry(LogEntry.Predefined.AfterBootReceiverStartFailedUnexpected(
                    context), context);
            MyNotificationManager.getInstance(context).notifyStartReceiverAfterBootFailed();
        }
    }
}
