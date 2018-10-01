package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;

/**
 * Created by lich on 9/27/18.
 */

public class CacheRouteTask {


    private TrackCacheRepository mTrackRepository;

    public CacheRouteTask(@NonNull TrackCacheRepository repository) {
        mTrackRepository = repository;
    }

    public void addRoute(Route route) {
        mTrackRepository.getTrack().getRouteList().add(route);
    }

}
