package mobile.lichle.com.trackme.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import mobile.lichle.com.trackme.Constants;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.TrackLocation;

/**
 * Created by lich on 9/28/18.
 */

public abstract class BaseMapFragment extends Fragment implements OnMapReadyCallback {

    protected GoogleMap mMap;

    protected List<TrackPolyline> mPolylineList;

    protected IFloatButtonVisibility mFloatButtonVisibility;

    public void setFloatButtonVisibility(IFloatButtonVisibility mFloatButtonVisibility) {
        this.mFloatButtonVisibility = mFloatButtonVisibility;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPolylineList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof IFloatButtonVisibility) {
            mFloatButtonVisibility = (IFloatButtonVisibility) getActivity();
            mFloatButtonVisibility.setFloatButtonVisibility(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(Constants.MAP_MAX_ZOOM_LEVEL);
        onMapReady();
    }

    public abstract void onMapReady();

    public void drawRoute(List<Route> routeList, boolean zoomToRoutes) {

        int totalRoute = routeList.size();
        LatLng firstLatlng = null;
        for (int i = 0; i < totalRoute; i++) {
            Route route = routeList.get(i);

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLUE);
            polylineOptions.geodesic(true);
            polylineOptions.width(Constants.ROUTE_WIDTH);
            polylineOptions.visible(true);

            TrackPolyline trackPolyline = new TrackPolyline();
            trackPolyline.setPolylineOption(polylineOptions);
            trackPolyline.addPolylineToMap(mMap);
            mPolylineList.add(trackPolyline);

            List<TrackLocation> locationList = route.getLocationList();
            for (int j = 0; j < locationList.size(); j++) {
                TrackLocation location = locationList.get(j);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                polylineOptions.add(latLng);
                List<LatLng> points = polylineOptions.getPoints();
                Polyline polyline = trackPolyline.getPolyline();
                polyline.setPoints(points);

                // Add a marker at the start location.
                if (j == 0) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(route.getDistanceInformation());
                    markerOptions.snippet(route.getVelocityAndDuration());
                    trackPolyline.addMarkerToMap(markerOptions, mMap);

                    firstLatlng = latLng;
                }
            }
        }

        if (zoomToRoutes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatlng, Constants.MAP_DEFAULT_ZOOM_LEVEL));
        }

    }


}
