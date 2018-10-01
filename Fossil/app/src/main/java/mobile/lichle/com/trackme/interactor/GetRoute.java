package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.repository.RouteDataSource;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;

/**
 * Created by lich on 9/29/18.
 */

public class GetRoute extends Interactor<GetRoute.RequestValues, GetRoute.ResponseValue> {

    private final RouteRepository mRouteRepository;

    public GetRoute(RouteRepository trackRepository) {
        mRouteRepository = trackRepository;
    }


    @Override
    public void executeIterator(RequestValues requestValues) {
        mRouteRepository.getRoutesByTrackId(requestValues.getTrackId(), new RouteDataSource.GetEntityCallback() {
            @Override
            public void onRoutesLoaded(List<RouteEntity> routes) {
                if (null != routes) {
                    ResponseValue responseValue = new ResponseValue(routes);
                    getIteratorCallback().onSuccess(responseValue);
                } else {
                    getIteratorCallback().onError();
                }
            }

            @Override
            public void onDataNotAvailable() {
                getIteratorCallback().onError();
            }
        });
    }

    public static final class RequestValues implements Interactor.RequestValues {

        private final long mTrackId;

        public RequestValues(@NonNull long trackId) {
            mTrackId = trackId;
        }

        public long getTrackId() {
            return mTrackId;
        }
    }

    public static final class ResponseValue implements Interactor.ResponseValue {

        private List<RouteEntity> mRouteEntityList;

        public ResponseValue(@NonNull List<RouteEntity> routeEntityList) {
            mRouteEntityList = routeEntityList;
        }

        public List<RouteEntity> getRouteList() {
            return mRouteEntityList;
        }
    }

}
