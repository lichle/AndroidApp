package mobile.lichle.com.trackme.view;

import java.util.List;

import mobile.lichle.com.trackme.presenter.BasePresenter;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lich on 9/28/18.
 */

public interface TracksContract {

    interface View extends BaseView<Presenter> {

        void showTrack(List<TrackEntity> list);

        void showTrackDetailUi(long trackId);

        void showLoadTracksError();

    }

    interface Presenter extends BasePresenter {

        void loadTrack();

        void openTrackDetails(TrackEntity trackEntity);

    }


}
