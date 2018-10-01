package mobile.lichle.com.trackme.view;

import java.util.List;

import mobile.lichle.com.trackme.presenter.BasePresenter;
import mobile.lichle.com.trackme.repository.model.Route;

/**
 * Created by lich on 9/28/18.
 */

public interface TrackDetailContract {

    interface View extends BaseView<TrackDetailContract.Presenter> {

        void showRoutes(List<Route> routeList);

        void showLoadRoutesError();

    }

    interface Presenter extends BasePresenter {

        void loadRoute(long trackId);

    }

}
