package mobile.lichle.com.trackme.utils;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;

/**
 * Created by lich on 9/29/18.
 */

public class ModelConverterUtils {

    public static TrackEntity createTrackEntityFromTrack(Track track) {
        TrackEntity trackEntity = new TrackEntity();
        trackEntity.setCreatedTime(System.currentTimeMillis());
        return trackEntity;
    }

    public static RouteEntity createRouteEntityFromRoute(Route route, long trackId) {
        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setCreatedTime(System.currentTimeMillis());
        routeEntity.setDuration(route.getDuration());
        routeEntity.setDistance(route.getDistance());
        routeEntity.setVelocity(route.getVelocity());
        routeEntity.setTrackId(trackId);

        List<TrackLocation> trackLocationList = route.getLocationList();
        String locationListText = LocationUtils.convertLocationArrayToString(trackLocationList);
        routeEntity.setRoute(locationListText);

        return routeEntity;
    }

    public static Route createRouteFromRouteEntity(RouteEntity routeEntity) {
        Route route = new Route();
        route.setDistance(routeEntity.getDistance());
        route.setVelocity(routeEntity.getVelocity());
        route.setDuration(routeEntity.getDuration());
        String routesText = routeEntity.getRoute();
        List<TrackLocation> trackLocationList = LocationUtils.convertStrinngToLocationList(routesText);
        route.setLocationList(trackLocationList);
        return route;
    }

}
