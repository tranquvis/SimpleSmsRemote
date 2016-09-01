package tranquvis.simplesmsremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.ReceiverService.SMSReceiverService;

/**
 * Created by Andi on 01.09.2016.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("bootCompletedReceiver", "broadcast received");
            DataManager.LoadUserData(context);
            if(DataManager.getUserData().getUserSettings().isStartReceiverOnSystemStart()) {
                SMSReceiverService.start(context);
                Log.i("receiver", "service started successful");
            }
            Log.i("bootCompletedReceiver", "service not started (pref not enabled)");
        } catch (Exception e) {
            Log.e("bootCompletedReceiver", "failed to start service");
            e.printStackTrace();
            MyNotificationManager.getInstance(context).notifyStartReceiverAfterBootFailed();
        }
    }
}
