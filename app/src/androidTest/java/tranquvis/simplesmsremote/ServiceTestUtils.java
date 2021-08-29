package tranquvis.simplesmsremote;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

import java.util.List;

public class ServiceTestUtils {
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass){
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
     }
}