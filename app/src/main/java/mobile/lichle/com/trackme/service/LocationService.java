package mobile.lichle.com.trackme.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.interactor.TrackTask;
import mobile.lichle.com.trackme.repository.Injection;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;
import mobile.lichle.com.trackme.utils.FormatterUtils;
import mobile.lichle.com.trackme.view.MapContract;

/**
 * Created by lich on 9/27/18.
 */

public class LocationService extends Service implements MapContract.Presenter, LocationListener, TrackTask.TrackTaskUpdater {

    public static final String TAG = "LocationService";

    public static final int LOCATION_REFRESH_TIME = 100;
    public static final int LOCATION_REFRESH_DISTANCE = 4;
    public static final int NOTIFICATION_ID = 99;
    public static final String NOTIFICATION_CHANEL_ID = "TrackMe";

    private final IBinder mBinder = new LocationService.LocalBinder();
    private LocationManager mLocationManager;


    private TrackTask mTrackTask;

    private MapContract.View mView;

    private boolean mIsStartMarkerAdded;

    @Override
    public void onCreate() {
        super.onCreate();
        TrackCacheRepository trackCacheRepository = Injection.provideTrackCacheRepository();
        TrackRepository trackRepository = Injection.provideTrackRepository(getApplicationContext());
        RouteRepository routeRepository = Injection.provideRouteRepository(getApplicationContext());
        InteractorHandler interactorHandler = Injection.provideInteractorHandler();
        mTrackTask = new TrackTask(interactorHandler, trackCacheRepository, trackRepository, routeRepository);
        mTrackTask.setUpdateListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void setView(MapContract.View view) {
        mView = view;
    }

    /**
     * Called at the first time its bounded view created.
     */
    @Override
    public void synchronizeData() {
        mTrackTask.requestSyncData();
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void requestUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
    }

    private void startServiceForeground() {
        Intent serviceIntent = new Intent(this.getApplicationContext(), LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, serviceIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(NOTIFICATION_CHANEL_ID)
                .setSound(null)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        // Customize notification UI: Add "Stop Service" button for stopping the service
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        notification.contentView = notificationView;

        // start service as foreground, so it's not be skilled even low memory but might causing battery run low faster
        startForeground(NOTIFICATION_ID, notification);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        String description = "Notifications for tracking location";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANEL_ID, NOTIFICATION_CHANEL_ID, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setSound(null, null);
        mChannel.enableVibration(false);
        notificationManager.createNotificationChannel(mChannel);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy " + this);
        super.onDestroy();
    }


    //////////////////////////////// implement LocationChange //////////////////////////////
    @Override
    public void onLocationChanged(Location location) {
        if (mTrackTask.isTracking()) {
            mView.updateRoute(location);
            TrackLocation trackLocation = new TrackLocation();
            trackLocation.setCreatedTime(location.getTime());
            trackLocation.setLatitude(location.getLatitude());
            trackLocation.setLongitude(location.getLongitude());

            mTrackTask.onNewLocation(trackLocation);

            if (!mIsStartMarkerAdded && mView.isActive()) {
                mView.addStartMarker(trackLocation);
                mIsStartMarkerAdded = true;
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    /////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////// Implement TrackTaskUpdater /////////////////////////////
    @Override
    public void onStartTracking(boolean isSyncViewOnly) {
        if (isSyncViewOnly) {
            mView.changeViewToStartTracking();
            return;
        }
        mView.changeViewToStartTracking();
        startServiceForeground();
        mView.startLocationService();

        mView.onAddNewRoute();
        TrackLocation trackLocation = mTrackTask.getTrack().getCurrentRoute().getCurrentLocation();
        if (!mIsStartMarkerAdded && null != trackLocation) {
            mView.addStartMarker(trackLocation);
            mIsStartMarkerAdded = true;
        }
    }

    @Override
    public void onStopTracking() {
        mView.changeViewToStopTracking();
    }

    @Override
    public void onResumeTracking() {
        mView.changeViewToResumeTracking();
        mTrackTask.addNewRoute();
        mView.onAddNewRoute();
    }

    @Override
    public void onPauseTracking() {
        mIsStartMarkerAdded = false;
        mView.changeViewToPauseTracking();
    }
    ///////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////// implement TrackTaskUpdater /////////////////////////////////
    @Override
    public void onVelocityCalculated(float value) {
        if (mView.isActive()) {
            String velocityText = FormatterUtils.converVelocityToString(value);
            mView.updateVelocity(velocityText);
        }
    }

    @Override
    public void onDurationCalculated(int second) {
        if (mView.isActive()) {
            String duration = FormatterUtils.convertTimeToString(second);
            mView.updateDuration(duration);
        }
    }

    @Override
    public void onDistanceCalculated(float meter) {
        if (mView.isActive()) {
            String distanceText = FormatterUtils.convertDistanceToString(meter);
            mView.updateDistance(distanceText);
        }
    }

    @Override
    public void onSetRouteInformation(Route route) {
        if (mView.isActive()) {
            mView.onUpdateCurrentRouteInfo(route);
        }
    }

    @Override
    public void onTrackSaved(boolean isSuccess) {
        if (mView.isActive()) {
            mView.onTrackSaved(isSuccess);
        }
    }

    @Override
    public void onNoTrackDataFound() {
        if (mView.isActive()) {
            mView.showMessageNoDataSaved();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////// Implement presenter /////////////////////////////
    @Override
    public void toggleStartPauseTracking() {
        mTrackTask.toggleStartPauseTracking();
    }

    @Override
    public void resumeTracking() {
        mTrackTask.resumeTracking();
    }

    @Override
    public void stopTracking() {
        mTrackTask.stopTracking();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        stopForeground(true);
    }
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public Track getTrack() {
        return mTrackTask.getTrack();
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationService.this;
        }
    }


}
