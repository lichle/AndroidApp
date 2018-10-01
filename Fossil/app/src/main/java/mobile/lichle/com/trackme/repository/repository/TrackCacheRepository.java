package mobile.lichle.com.trackme.repository.repository;

import java.util.ArrayList;
import java.util.List;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;

/**
 * Created by lich on 9/28/18.
 */

public class TrackCacheRepository implements MemTrackDataSource {

    private static volatile TrackCacheRepository sInstance;

    private Track mTrack;

    public TrackCacheRepository() {
        mTrack = new Track();
        List<Route> routeList = new ArrayList<>();
        Route route = new Route();
        routeList.add(route);
        mTrack.setRouteList(routeList);
        List<TrackLocation> locationList = new ArrayList<>();
        mTrack.getRouteList().get(0).setLocationList(locationList);
    }

    public static TrackCacheRepository getInstance() {
        if (sInstance == null) {
            synchronized (RouteRepository.class) {
                if (sInstance == null) {
                    sInstance = new TrackCacheRepository();
                }
            }
        }
        return sInstance;
    }

    private void initializeData() {
        mTrack = new Track();
        List<Route> routeList = new ArrayList<>();
        Route route = new Route();
        routeList.add(route);
        mTrack.setRouteList(routeList);
        List<TrackLocation> locationList = new ArrayList<>();
        mTrack.getRouteList().get(0).setLocationList(locationList);
    }

    public void addNewRoute() {
        Route route = new Route();
        List<TrackLocation> locationList = new ArrayList<>();
        route.setLocationList(locationList);
        mTrack.getRouteList().add(route);
    }

    @Override
    public Track getTrack() {
        return mTrack;
    }

    @Override
    public void clear() {
        List<Route> routeList = mTrack.getRouteList();
        for (int i = 0; i < routeList.size(); i++) {
            Route route = routeList.get(i);
            List<TrackLocation> locationList = route.getLocationList();
            locationList.clear();
        }
        routeList.clear();
        initializeData();
    }


}
