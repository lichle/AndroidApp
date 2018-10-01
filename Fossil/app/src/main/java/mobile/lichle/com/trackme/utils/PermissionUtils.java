package mobile.lichle.com.trackme.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lich on 9/24/18.
 */

public class PermissionUtils {

    /**
     * Check runtime permissions (for Android 6.0 and higher)
     *
     * @param activity            the activity which receives permission callback
     * @param requiredPermissions list of required permissions need for execute a task
     * @param requestCode         the code used for permission callback
     * @return true if all permission are granted. Otherwise returns false.
     */
    public static boolean checkPermissions(Activity activity, String[] requiredPermissions, int requestCode) {
        List<String> unGrantedPermissions = new ArrayList<>();
        for (int i = 0; i < requiredPermissions.length; i++) {
            String requiredItem = requiredPermissions[i];
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), requiredItem)
                    != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(requiredItem);
            }
        }
        int size = unGrantedPermissions.size();
        if (size > 0) {
            String[] permissionArray = new String[unGrantedPermissions.size()];
            permissionArray = unGrantedPermissions.toArray(permissionArray);
            ActivityCompat.requestPermissions(activity, permissionArray, requestCode);
            return false;
        }
        return true;
    }

}
