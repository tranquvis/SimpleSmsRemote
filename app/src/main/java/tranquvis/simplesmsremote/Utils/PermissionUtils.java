package tranquvis.simplesmsremote.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 25.08.2016.
 */
public class PermissionUtils
{
    /**
     * Check if the app has permissions
     * @param context app context
     * @param permissions permissions to check
     * @return true if app has all permissions
     */
    public static boolean AppHasPermissions(Context context, String[] permissions)
    {
        for(String perm : permissions)
        {
            if(!AppHasPermission(context, perm))
                return false;
        }
        return true;
    }

    /**
     * Check if the app has a specific permissions
     * @param context app context
     * @param permission permission to check
     * @return true if app has permission
     */
    public static boolean AppHasPermission(Context context, String permission)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && permission.equals(Manifest.permission.WRITE_SETTINGS))
        {
            return Settings.System.canWrite(context);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && permission.equals(Manifest.permission.ACCESS_NOTIFICATION_POLICY))
        {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            return notificationManager.isNotificationPolicyAccessGranted();
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get all permissions, which are not granted to the app, from {@code permissions}
     * @param context app context
     * @param permissions permissions to search
     * @return not granted permissions
     */
    public static String[] FilterAppPermissions(Context context, String[] permissions)
    {
        List<String> perms = new ArrayList<>();
        for(String permission : permissions)
        {
            if(!PermissionUtils.AppHasPermission(context, permission))
                perms.add(permission);
        }

        return perms.toArray(new String[perms.size()]);
    }

    /**
     * request common permissions via dialog <br/>
     * with common permissions are meant all permissions that can be request via the standard dialog
     * @param activity any activity
     * @param permissions permissions to request
     * @param resultCode code to identify this request
     */
    public static void RequestCommonPermissions(Activity activity, String[] permissions, int resultCode)
    {
        ActivityCompat.requestPermissions(activity, permissions, resultCode);
    }

    /**
     * request common permissions via dialog <br/>
     * with common permissions are meant all permissions that can be request via the standard dialog
     * @param fragment any fragment
     * @param permissions permissions to request
     * @param resultCode code to identify this request
     */
    public static void RequestCommonPermissions(Fragment fragment, String[] permissions, int resultCode)
    {
        FragmentCompat.requestPermissions(fragment, permissions, resultCode);
    }

    /**
     * Use this method to request permissions successively
     * @param activity any activity
     * @param permissions permissions to request
     * @param resultCode code to identify this request
     * @return request result with remaining and just requested permissions
     */
    public static RequestResult RequestNextPermissions(
            Activity activity, String[] permissions, int resultCode)
    {
        if(permissions == null || permissions.length == 0)
            return null;

        int i;
        String requestPermission = null;
        for(i = 0; i < permissions.length; i++)
        {
            String perm = permissions[i];

            if(requestPermission == null)
            {
                requestPermission = perm;
            }
            else if(!requestPermission.equals(perm))
            {
                break;
            }
        }

        if(requestPermission == null)
            return null;

        String[] requestPermissions = ArrayUtils.subarray(permissions, 0, i);
        String[] remainingPermissions = i == permissions.length ? null :
                ArrayUtils.subarray(permissions, i, permissions.length);

        RequestType requestType = RequestType.NO_REQUEST;
        if(requestPermission.equals(Manifest.permission.WRITE_SETTINGS))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestType = RequestType.INDEPENDENT_ACTIVITY;
                RequestWriteSettingsPermission(activity);
            }
        }
        else if(requestPermission.equals(Manifest.permission.ACCESS_NOTIFICATION_POLICY))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                requestType = RequestType.INDEPENDENT_ACTIVITY;
                RequestAccessNotificationPolicyPermission(activity);
            }
        }
        else
        {
            requestType = RequestType.COMMON_REQUEST_DIALOG;
            RequestCommonPermissions(activity, requestPermissions, resultCode);
        }

        return new RequestResult(requestPermissions, remainingPermissions, requestType);
    }

    /**
     * Start activity in order to request permission for writing settings <br/>
     * @param activity any activity
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void RequestWriteSettingsPermission(Activity activity)
    {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void RequestAccessNotificationPolicyPermission(Activity activity)
    {
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            activity.startActivity(intent);
        }
    }

    public static class RequestResult
    {
        private String[] requestPermissions;
        private String[] remainingPermissions;
        private RequestType requestType;

        RequestResult(String[] requestPermissions, String[] remainingPermissions, RequestType requestType)
        {
            this.requestPermissions = requestPermissions;
            this.remainingPermissions = remainingPermissions;
            this.requestType = requestType;
        }

        public String[] getRequestPermissions()
        {
            return requestPermissions;
        }

        public String[] getRemainingPermissions()
        {
            return remainingPermissions;
        }

        public RequestType getRequestType()
        {
            return requestType;
        }
    }

    public enum RequestType { COMMON_REQUEST_DIALOG, INDEPENDENT_ACTIVITY, NO_REQUEST }
}
