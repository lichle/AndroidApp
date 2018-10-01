package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mobile.lichle.com.trackme.TrackStatus;
import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;
import mobile.lichle.com.trackme.utils.ModelConverterUtils;

/**
 * Created by lich on 9/27/18.
 */

public class TrackTask {

    public static final String TAG = "TrackTask";

    private TrackCacheRepository mTrackCacheRepository;

    private UpdateDistanceTask mUpdateDistanceTask;
    private UpdateDurationTask mUpdateDurationTask;
    private UpdateVelocityTask mUpdateVelocityTask;
    private CacheLocationTask mCacheLocationTask;

    private CacheRouteTask mCacheRouteTask;

    private InteractorHandler mInteractor;

    private SaveTrack mSaveTrack;
    private SaveRoute mSaveRoute;


    private TrackStatus mStatus = TrackStatus.STOPPED;

    private TrackTaskUpdater mListener;


    public TrackTask(@NonNull InteractorHandler interactor,
                     @NonNull TrackCacheRepository cacheRepository, @NonNull TrackRepository trackRepository, @NonNull RouteRepository routeRepository) {
        mInteractor = interactor;
        mTrackCacheRepository = cacheRepository;
        mUpdateDistanceTask = new UpdateDistanceTask(mTrackCacheRepository);
        mUpdateDurationTask = new UpdateDurationTask(mTrackCacheRepository);
        mUpdateVelocityTask = new UpdateVelocityTask(mTrackCacheRepository);
        mCacheLocationTask = new CacheLocationTask(mTrackCacheRepository);
        mCacheRouteTask = new CacheRouteTask(mTrackCacheRepository);

        mSaveTrack = new SaveTrack(trackRepository);
        mSaveRoute = new SaveRoute(routeRepository);
    }

    public boolean isTracking() {
        return mStatus == TrackStatus.RECORDING;
    }

    public Track getTrack() {
        return mTrackCacheRepository.getTrack();
    }

    public void setUpdateListener(TrackTaskUpdater listener) {
        mListener = listener;
    }

    /**
     * Execute each time location is changed.
     *
     * @param trackLocation
     */
    public void onNewLocation(TrackLocation trackLocation) {
        if (mStatus == TrackStatus.RECORDING) {
            final Route currentRoute = mTrackCacheRepository.getTrack().getCurrentRoute();
            TrackLocation currentLocation = currentRoute.getCurrentLocation();
            TrackLocation previousLocation = currentRoute.getPreviousLocation();
            if (null != previousLocation) {
                double velocity = calculateVelocity(currentLocation, previousLocation);
                trackLocation.setVelocity((float) velocity);
            }
            mCacheLocationTask.addLocation(trackLocation);
            updateDistance();
        }
    }

    public double calculateVelocity(TrackLocation currentLocation, TrackLocation previousLocation) {
        return mUpdateVelocityTask.calculateVelocity(currentLocation, previousLocation);
    }

    public void startCountingDuration() {
        mUpdateDurationTask.start(new UpdateDurationTask.DurationChanged() {
            @Override
            public void onDurationChanged(int value) {
                mListener.onDurationCalculated(value);
            }
        });
    }

    public void startTrackingVelocity() {
        mUpdateVelocityTask.start(new UpdateVelocityTask.VelocityChanged() {
            @Override
            public void onVelocityChanged(float value) {
                mListener.onVelocityCalculated(value);
            }
        });
    }

    public void updateDistance() {
        mUpdateDistanceTask.getTotalDistance(new UpdateDistanceTask.DistanceChanged() {
            @Override
            public void onDistanceChange(float value) {
                mListener.onDistanceCalculated(value);
            }
        });
    }

    public void toggleStartPauseTracking() {
        if (mStatus == TrackStatus.STOPPED) { //is stopping ==> move to Recording status (begin recording)
            mStatus = TrackStatus.RECORDING;
            startCountingDuration();
            startTrackingVelocity();
            mListener.onStartTracking(false);
            mUpdateDurationTask.allowTracking(true);
        } else if (mStatus == TrackStatus.RECORDING) { // is recording ==> move to pauseTracking status
            mStatus = TrackStatus.PAUSE;
            Route currentRoute = mTrackCacheRepository.getTrack().getCurrentRoute();
            if (null != currentRoute.getPreviousLocation()) { //only save route if it has data
                setRouteInformation();

                //notify Presenter to ask view to update the marker info window
                mListener.onSetRouteInformation(mTrackCacheRepository.getTrack().getCurrentRoute());
            }

            mUpdateDurationTask.allowTracking(false);
            mUpdateDurationTask.resetData();
            mUpdateVelocityTask.resetData();
            mUpdateDistanceTask.resetData();
            mListener.onPauseTracking();
        }
    }

    public void resumeTracking() {
        if (mStatus == TrackStatus.PAUSE) {
            mStatus = TrackStatus.RECORDING;
            mUpdateDurationTask.allowTracking(true);
            mListener.onResumeTracking();
            startTrackingVelocity();
        }
    }

    public void stopTracking() {
        if (mStatus == TrackStatus.PAUSE) {
            stopAllTrackingTasks();

            //remove routes which does not contain data
//            removeRoutesNotContainData();

            //check if the current route has data
            TrackLocation location = mTrackCacheRepository.getTrack().getCurrentRoute().getPreviousLocation();
            if (null != location) { // has data
                saveTrackToDatabase();
            } else {
                mListener.onNoTrackDataFound();
                purgeAllData();
            }
        }
    }

    private void removeRoutesNotContainData() {
        List<Route> routeList = mTrackCacheRepository.getTrack().getRouteList();
        for (int i = 1; i < routeList.size(); i++) { // Must keep 1 route
            Route route = routeList.get(i);
            if (null != route.getPreviousLocation()) {
                routeList.remove(route);
            }
        }
    }

    private void stopAllTrackingTasks() {
        mUpdateDistanceTask.stopTracking();
        mUpdateDurationTask.stopTracking();
        mUpdateVelocityTask.stopTracking();
    }

    public void saveTrackToDatabase() {
        Track track = mTrackCacheRepository.getTrack();

        TrackEntity trackEntity = ModelConverterUtils.createTrackEntityFromTrack(track);
        //save track to database
        mInteractor.execute(mSaveTrack, new SaveTrack.RequestValues(trackEntity),
                new Interactor.InteractorCallback<SaveTrack.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveTrack.ResponseValue response) {
                        long trackId = response.getTrackEntity().getId();
                        saveRouteToDatabase(trackId);
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Save Track Error");
                        purgeAllData();
                        mListener.onTrackSaved(false);
                    }
                });
    }

    private void saveRouteToDatabase(final long trackId) {
        List<Route> routeList = mTrackCacheRepository.getTrack().getRouteList();
        List<RouteEntity> routeEntityList = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i++) {
            final Route route = routeList.get(i);
            final RouteEntity routeEntity = ModelConverterUtils.createRouteEntityFromRoute(route, trackId);
            routeEntityList.add(routeEntity);
        }
        mInteractor.execute(mSaveRoute, new SaveRoute.RequestValues(routeEntityList),
                new Interactor.InteractorCallback<SaveRoute.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveRoute.ResponseValue response) {
                        Log.d(TAG, response.toString());
                        purgeAllData();
                        mListener.onTrackSaved(true);
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Save Route list Error");
                        purgeAllData();
                        mListener.onTrackSaved(true);
                        deleteTrack(trackId);
                    }
                });
    }

    private void deleteTrack(long trackId) {

    }

    private void purgeAllData() {
        clearTrackData();
        mStatus = TrackStatus.STOPPED;
        mListener.onStopTracking();
        mListener.onDistanceCalculated(0);
        mListener.onDurationCalculated(0);
        mListener.onVelocityCalculated(0);
        mUpdateVelocityTask.stopTracking();
    }

    public void clearTrackData() {
        mTrackCacheRepository.clear();
    }

    public void requestSyncData() {
        //Sync Button
        if (mStatus == TrackStatus.RECORDING) {
            mListener.onStartTracking(true);
        } else if (mStatus == TrackStatus.PAUSE) {
            mListener.onPauseTracking();
        } else if (mStatus == TrackStatus.STOPPED) {
            mListener.onStopTracking();
        }
        //Sync duration
        int duration = mUpdateDurationTask.getTimeCounter();
        mListener.onDurationCalculated(duration);
        //Sync distance
        float distance = mUpdateDistanceTask.getTotalDistance();
        mListener.onDistanceCalculated(distance);

    }

    //adding a new route only if the current one has data
    public void addNewRoute() {
        if (null != mTrackCacheRepository.getTrack().getCurrentRoute().getPreviousLocation()) {
            mTrackCacheRepository.addNewRoute();
        }
    }

    /**
     * This must be call before adding a new route;
     */
    public void setRouteInformation() {
        Route route = mTrackCacheRepository.getTrack().getCurrentRoute();
        float distance = mUpdateDistanceTask.getTotalDistance();
        int duration = mUpdateDurationTask.getTimeCounter();
        float velocity = mUpdateVelocityTask.getVelocityAverage();
        route.setDistance(distance);
        route.setDuration(duration);
        route.setVelocity(velocity);
    }

    public interface TrackTaskUpdater {

        void onStartTracking(boolean isSyncViewOnly);

        void onStopTracking();

        void onResumeTracking();

        void onPauseTracking();

        void onVelocityCalculated(float value);

        void onDurationCalculated(int second);

        void onDistanceCalculated(float meter);

        void onSetRouteInformation(Route route);

        void onTrackSaved(boolean isSuccess);

        void onNoTrackDataFound();

    }


}
