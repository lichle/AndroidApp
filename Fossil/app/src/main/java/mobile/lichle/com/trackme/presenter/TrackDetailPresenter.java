package mobile.lichle.com.trackme.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import mobile.lichle.com.trackme.interactor.GetRoute;
import mobile.lichle.com.trackme.interactor.Interactor;
import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.repository.entity.RouteEntity;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.utils.ModelConverterUtils;
import mobile.lichle.com.trackme.view.TrackDetailContract;

/**
 * Created by lich on 9/28/18.
 */

public class TrackDetailPresenter implements TrackDetailContract.Presenter {

    private final TrackDetailContract.View mView;
    private final GetRoute mGetRoutes;

    private final InteractorHandler mHandler;

    public TrackDetailPresenter(@NonNull TrackDetailContract.View view,
                                @NonNull InteractorHandler handler, @NonNull GetRoute getRoutes) {
        mView = view;
        mHandler = handler;
        mGetRoutes = getRoutes;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadRoute(long trackId) {
        mHandler.execute(mGetRoutes, new GetRoute.RequestValues(trackId), new Interactor.InteractorCallback<GetRoute.ResponseValue>() {
            @Override
            public void onSuccess(GetRoute.ResponseValue response) {
                List<RouteEntity> routeEntityList = response.getRouteList();
                boolean routeHasData = false;
                List<Route> routeList = new ArrayList<>();
                for (int i = 0; i < routeEntityList.size(); i++) {
                    RouteEntity routeEntity = routeEntityList.get(i);
                    Route route = ModelConverterUtils.createRouteFromRouteEntity(routeEntity);
                    routeList.add(route);
                    routeHasData = true;
                }

                if (mView.isActive()) {
                    if (routeHasData) {
                        mView.showRoutes(routeList);
                    } else {
                        mView.showLoadRoutesError();
                    }
                }
            }

            @Override
            public void onError() {
                if (mView.isActive()) {
                    mView.showLoadRoutesError();
                }
            }
        });
    }
}
