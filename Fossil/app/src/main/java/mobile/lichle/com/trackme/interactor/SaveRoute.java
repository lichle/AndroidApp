package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;

/**
 * Created by lich on 9/29/18.
 */

public class SaveRoute extends Interactor<SaveRoute.RequestValues, SaveRoute.ResponseValue> {

    private final RouteRepository mRouteRepository;

    public SaveRoute(@NonNull RouteRepository routeRepository) {
        mRouteRepository = routeRepository;
    }

    @Override
    public void executeIterator(RequestValues requestValues) {
        List<RouteEntity> routeEntity = requestValues.getRouteEntity();
        mRouteRepository.saveRoute(routeEntity);
        getIteratorCallback().onSuccess(new ResponseValue(routeEntity));

    }

    public static final class RequestValues implements Interactor.RequestValues {

        private final List<RouteEntity> mRouteEntity;

        public RequestValues(@NonNull List<RouteEntity> routeEntity) {
            mRouteEntity = routeEntity;
        }

        public List<RouteEntity> getRouteEntity() {
            return mRouteEntity;
        }
    }

    public static final class ResponseValue implements Interactor.ResponseValue {

        private final List<RouteEntity> mRouteEntity;

        public ResponseValue(@NonNull List<RouteEntity> routeEntity) {
            mRouteEntity = routeEntity;
        }

        public List<RouteEntity> getTrackEntity() {
            return mRouteEntity;
        }
    }

}
