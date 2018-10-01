package mobile.lichle.com.trackme.utils;

import android.location.Location;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import mobile.lichle.com.trackme.repository.model.TrackLocation;

/**
 * Created by lich on 9/27/18.
 */

public class LocationUtils {

    public static final String DEFAUT_PROVIDER = "gps";

    public static final String SEPARATE_LAT_LONG_SIGN = "-";

    public static final String SEPARATE_LOCATION_SIGN = ",";


    public static String convertToString(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return latitude + SEPARATE_LAT_LONG_SIGN + longitude;
    }

    public static String convertToString(TrackLocation trackLocation) {
        double latitude = trackLocation.getLatitude();
        double longitude = trackLocation.getLongitude();
        return latitude + SEPARATE_LAT_LONG_SIGN + longitude;
    }


    public static Location convertToLocation(String value) {
        String[] latLong = value.split(SEPARATE_LAT_LONG_SIGN);
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);
        Location location = new Location(DEFAUT_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public static String addLocationToList(String listString, Location location) {
        String locationString = convertToString(location);
        return listString + SEPARATE_LOCATION_SIGN + locationString;
    }


    public static List<Location> convertLocationListStringToArray(String listString) {
        List<Location> locations = new ArrayList<>();
        String[] size = listString.split(SEPARATE_LOCATION_SIGN);
        for (int i = 0; i < size.length; i++) {
            String locationString = size[i];
            Location location = convertToLocation(locationString);
            locations.add(location);
        }
        return locations;
    }

    public static List<TrackLocation> convertStrinngToLocationList(String routeString) {
        List<TrackLocation> trackLocationList = new ArrayList<>();
        String[] locationTextArray = routeString.split(SEPARATE_LOCATION_SIGN);
        for (int i = 0; i < locationTextArray.length; i++) {
            String locationText = locationTextArray[i];
            if (!TextUtils.isEmpty(locationText)) {
                String latitudeText = locationText.split(SEPARATE_LAT_LONG_SIGN)[0];
                String longitudeText = locationText.split(SEPARATE_LAT_LONG_SIGN)[1];
                double latitude = Double.parseDouble(latitudeText);
                double longitude = Double.parseDouble(longitudeText);
                TrackLocation trackLocation = new TrackLocation();
                trackLocation.setLatitude(latitude);
                trackLocation.setLongitude(longitude);
                trackLocationList.add(trackLocation);
            }
        }

        return trackLocationList;
    }

    public static String convertLocationArrayToString(List<TrackLocation> trackLocationList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < trackLocationList.size(); i++) {
            TrackLocation trackLocation = trackLocationList.get(i);
            String locationText = convertToString(trackLocation);
            stringBuilder.append(locationText);
            stringBuilder.append(SEPARATE_LOCATION_SIGN);
        }
        return stringBuilder.toString();
    }

    public static float getDistance(TrackLocation location1, TrackLocation location2) {
        Location gLocation1 = convertToGoogleMapLocation(location1);
        Location gLocation2 = convertToGoogleMapLocation(location2);
        return gLocation1.distanceTo(gLocation2);
    }


    public static Location convertToGoogleMapLocation(TrackLocation location) {
        double lat = location.getLatitude();
        double log = location.getLongitude();
        Location result = new Location("");
        result.setLatitude(lat);
        result.setLongitude(log);
        return result;
    }

}
