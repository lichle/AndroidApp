package mobile.lichle.com.trackme.view;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import mobile.lichle.com.trackme.Constants;
import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.service.LocationService;

/**
 * Created by lich on 9/28/18.
 */

public class MapFragment extends BaseMapFragment implements View.OnClickListener, MapContract.View {

    boolean mBounded = false;
    private SupportMapFragment mMapFragment;
    private ImageView mStartButton, mResumeButton, mStopButton;

    private TextView mDurationText, mVelocityText, mDistanceText;

    private MapContract.Presenter mPresenter;

    private boolean mAreRoutesDrawn;

    private boolean mIsMapCameraCenter;

    private boolean mIsWaitingForSaving;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mPresenter = binder.getService();
            mPresenter.setView(MapFragment.this);
            mPresenter.requestUpdateLocation();
            mPresenter.synchronizeData();
            if (!mAreRoutesDrawn && null != mMap) {
                Track track = mPresenter.getTrack();
                drawRoute(track.getRouteList(), false);
                mAreRoutesDrawn = true;
            }
            mBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBounded = false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mStartButton = root.findViewById(R.id.btn_start);
        mResumeButton = root.findViewById(R.id.btn_resume);
        mStopButton = root.findViewById(R.id.btn_stop);
        mDurationText = root.findViewById(R.id.tv_duration);
        mVelocityText = root.findViewById(R.id.tv_speed);
        mDistanceText = root.findViewById(R.id.tv_distance);

        mMapFragment.getMapAsync(this);
        mStartButton.setOnClickListener(this);
        mResumeButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        return root;
    }

    /**
     * Move map to current location at the first time map is loaded
     *
     * @param map
     */
    private void navigateMapToCurrentLocation(GoogleMap map) {
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.MAP_DEFAULT_ZOOM_LEVEL));
                mMap.setOnMyLocationChangeListener(null);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady() {
        navigateMapToCurrentLocation(mMap);
        if (!mAreRoutesDrawn && null != mPresenter) {
            Track track = mPresenter.getTrack();
            drawRoute(track.getRouteList(), false);
            mAreRoutesDrawn = true;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        startLocationService();
    }

    @Override
    public void onStop() {
        getActivity().unbindService(mConnection);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
//        if (mIsWaitingForSaving) {
//            String waingForSavingText = getString(R.string.waiting_for_saving);
//            Toast.makeText(getContext(), waingForSavingText, Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (v.getId() == R.id.btn_start) {
            mPresenter.toggleStartPauseTracking();
        } else if (v.getId() == R.id.btn_resume) {
            mPresenter.resumeTracking();
        } else if (v.getId() == R.id.btn_stop) {
            mPresenter.stopTracking();
            mIsWaitingForSaving = true;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void startLocationService() {
        Intent intent = new Intent(getContext(), LocationService.class);
        getContext().startService(intent);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void updateRoute(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!mIsMapCameraCenter) {
            mIsMapCameraCenter = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.MAP_DEFAULT_ZOOM_LEVEL));
        }

        TrackPolyline trackPolyline = getCurrentPolyline();
        if (trackPolyline == null) {
            return;
        }
        PolylineOptions polylineOptions = trackPolyline.getPolylineOptions();
        polylineOptions.add(latLng);
        List<LatLng> points = polylineOptions.getPoints();

        Polyline polyline = trackPolyline.getPolyline();
        polyline.setPoints(points);
    }

    @Override
    public void addStartMarker(TrackLocation location) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions.position(latLng);
        String text = getString(R.string.route_info_in_tracking);
        markerOptions.title(text);
        TrackPolyline trackPolyline = getCurrentPolyline();
        if (trackPolyline == null) {
            return;
        }
        trackPolyline.addMarkerToMap(markerOptions, mMap);
    }

    private TrackPolyline getCurrentPolyline() {
        int size = mPolylineList.size();
        return size > 0 ? mPolylineList.get(size - 1) : null;
    }

    @Override
    public void onAddNewRoute() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.geodesic(true);
        polylineOptions.width(Constants.ROUTE_WIDTH);
        polylineOptions.visible(true);

        TrackPolyline trackPolyline = new TrackPolyline();
        trackPolyline.setPolylineOption(polylineOptions);
        trackPolyline.addPolylineToMap(mMap);
        mPolylineList.add(trackPolyline);
    }

    @Override
    public void onUpdateCurrentRouteInfo(Route route) {
        TrackPolyline trackPolyline = getCurrentPolyline();
        if (null != trackPolyline) {
            Marker marker = trackPolyline.getMarker();
            marker.setTitle(route.getDistanceInformation());
            marker.setSnippet(route.getVelocityAndDuration());
        }
    }

    @Override
    public void onTrackSaved(boolean isSuccess) {
        String text;
        if (isSuccess) {
            text = getString(R.string.track_saved_successfully);
        } else {
            text = getString(R.string.track_saved_failed);
        }
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        mPolylineList.clear();
        mMap.clear();
        mAreRoutesDrawn = false;
        mIsWaitingForSaving = false;
    }

    @Override
    public void showMessageNoDataSaved() {
        String text = getString(R.string.no_track_data);
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDuration(final String duration) {
        mDurationText.setText(duration);
    }

    @Override
    public void updateVelocity(String velocity) {
        mVelocityText.setText(velocity);
    }

    @Override
    public void updateDistance(String distance) {
        mDistanceText.setText(distance);
    }

    /**
     * Toggle show record button and the resumeTracking/stopTracking button
     *
     * @param value
     */
    private void showStartButton(boolean value) {
        int visibility = value ? View.VISIBLE : View.INVISIBLE;
        int invisibility = !value ? View.VISIBLE : View.INVISIBLE;
        mStartButton.setVisibility(visibility);
        mStopButton.setVisibility(invisibility);
        mResumeButton.setVisibility(invisibility);
    }


    @Override
    public void changeViewToStartTracking() {
        showStartButton(true);
        mStartButton.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void changeViewToStopTracking() {
        showStartButton(true);
        mStartButton.setImageResource(R.drawable.ic_start);
    }

    @Override
    public void changeViewToResumeTracking() {
        showStartButton(true);
        mStartButton.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void changeViewToPauseTracking() {
        showStartButton(false);
    }

}
