package tranquvis.simplesmsremote.Helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
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
public class PermissionHelper
{
    public static boolean AppHasPermissions(Context context, String[] permissions)
    {
        for(String perm : permissions)
        {
            if(!AppHasPermission(context, perm))
                return false;
        }
        return true;
    }

    public static boolean AppHasPermission(Context context, String permission)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(permission.equals(Manifest.permission.WRITE_SETTINGS))
                return Settings.System.canWrite(context);
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static String[] FilterAppPermissions(Context context, String[] permissions)
    {
        List<String> perms = new ArrayList<>();
        for(String permission : permissions)
        {
            if(!PermissionHelper.AppHasPermission(context, permission))
                perms.add(permission);
        }

        return perms.toArray(new String[perms.size()]);
    }

    public static void RequestCommonPermissions(Activity activity, String[] permissions, int resultCode)
    {
        ActivityCompat.requestPermissions(activity, permissions, resultCode);
    }

    public static void RequestCommonPermissions(Fragment fragment, String[] permissions, int resultCode)
    {
        FragmentCompat.requestPermissions(fragment, permissions, resultCode);
    }

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
        else
        {
            requestType = RequestType.COMMON_REQUEST_DIALOG;
            RequestCommonPermissions(activity, requestPermissions, resultCode);
        }

        return new RequestResult(requestPermissions, remainingPermissions, requestType);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void RequestWriteSettingsPermission(Activity activity)
    {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static class RequestResult
    {
        private String[] requestPermissions;
        private String[] remainingPermissions;
        private RequestType requestType;

        public RequestResult(String[] requestPermissions, String[] remainingPermissions, RequestType requestType)
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
