package mobile.lichle.com.trackme.interactor;

import android.location.Location;
import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;

/**
 * Created by lich on 9/27/18.
 */

public class UpdateDistanceTask {

    private float mTotalDistance;

    private TrackCacheRepository mTrackRepository;

    public UpdateDistanceTask(@NonNull TrackCacheRepository repository) {
        mTrackRepository = repository;
    }

    public float getTotalDistance() {
        return mTotalDistance;
    }

    public void getTotalDistance(DistanceChanged distanceChanged) {
        Route route = mTrackRepository.getTrack().getCurrentRoute();
        TrackLocation preLocation = route.getPreviousLocation();
        TrackLocation curLocation = route.getCurrentLocation();
        // At the first and second time, we don't have any locations in the cache
        if (null != preLocation && null != curLocation) {
            Location pLocation = new Location("");
            pLocation.setLatitude(preLocation.getLatitude());
            pLocation.setLongitude(preLocation.getLongitude());

            Location cLocation = new Location("");
            cLocation.setLatitude(curLocation.getLatitude());
            cLocation.setLongitude(curLocation.getLongitude());

            float distance = cLocation.distanceTo(pLocation) / 1000;
            mTotalDistance += distance;
            distanceChanged.onDistanceChange(mTotalDistance);
        }
    }

    public void stopTracking() {
        resetData();
    }

    public void resetData() {
        mTotalDistance = 0;
    }


    public interface DistanceChanged {
        void onDistanceChange(float value);
    }

}
