package mobile.lichle.com.trackme.repository.repository;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.RouteEntity;

/**
 * Created by lich on 9/28/18.
 */

public interface RouteDataSource {

    void saveRoute(@NonNull List<RouteEntity> entity);

    void getRoutes(@NonNull LoadEntitiesCallback callback);

    void getRoutesByTrackId(@NonNull long trackId, @NonNull GetEntityCallback callback);

    interface LoadEntitiesCallback {

        void onTasksLoaded(List<RouteEntity> routeEntities);

        void onDataNotAvailable();
    }

    interface GetEntityCallback {

        void onRoutesLoaded(List<RouteEntity> routes);

        void onDataNotAvailable();
    }

}
