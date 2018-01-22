package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Andreas Kaltenleitner on 22.01.2018.
 */

public class AppUtils {

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
