package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;

/**
 * Created by lich on 9/27/18.
 */

public class CacheLocationTask {


    private TrackCacheRepository mTrackRepository;

    public CacheLocationTask(@NonNull TrackCacheRepository repository) {
        mTrackRepository = repository;
    }


    /**
     * Because track is an ordered array ==> add location to the latest route
     *
     * @param trackLocation
     */
    public void addLocation(TrackLocation trackLocation) {
        Route currentRoute = mTrackRepository.getTrack().getCurrentRoute();
        currentRoute.getLocationList().add(trackLocation);

    }

}
