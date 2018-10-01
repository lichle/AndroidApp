package mobile.lichle.com.trackme.utils;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by lich on 9/24/18.
 */

public class DeviceUtils {

    public static boolean isDeviceHasGPS(Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        final List<String> providers = locationManager.getAllProviders();
        if (providers == null) {
            return false;
        }
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public static boolean isGPSEnable(Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
