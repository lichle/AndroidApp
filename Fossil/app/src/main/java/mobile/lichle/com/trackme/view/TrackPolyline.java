package mobile.lichle.com.trackme.view;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by lich on 9/28/18.
 */

public class TrackPolyline {

    private Marker mMarker;

    private Polyline mPolyline;
    private PolylineOptions mPolylineOptions;

    public void addPolylineToMap(GoogleMap map) {
        mPolyline = map.addPolyline(mPolylineOptions);
    }

    public void setPolylineOption(PolylineOptions polylineOptions) {
        mPolylineOptions = polylineOptions;
    }

    public PolylineOptions getPolylineOptions() {
        return mPolylineOptions;
    }

    public Polyline getPolyline() {
        return mPolyline;
    }

    public void addMarkerToMap(MarkerOptions markerOptions, GoogleMap map) {
        mMarker = map.addMarker(markerOptions);

    }

    public Marker getMarker() {
        return mMarker;
    }

}
