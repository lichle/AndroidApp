package mobile.lichle.com.trackme.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.interactor.GetTracks;
import mobile.lichle.com.trackme.interactor.Interactor;
import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.view.TracksContract;

/**
 * Created by lich on 9/28/18.
 */

public class TracksPresenter implements TracksContract.Presenter {

    private final TracksContract.View mView;
    private final GetTracks mGetTask;

    private final InteractorHandler mHandler;

    public TracksPresenter(@NonNull TracksContract.View view,
                           @NonNull InteractorHandler handler, @NonNull GetTracks getTracks) {
        mView = view;
        mHandler = handler;
        mGetTask = getTracks;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTrack();
    }

    @Override
    public void loadTrack() {
        mHandler.execute(mGetTask, new GetTracks.RequestValues(), new Interactor.InteractorCallback<GetTracks.ResponseValue>() {
            @Override
            public void onSuccess(GetTracks.ResponseValue response) {
                List<TrackEntity> trackEntityList = response.getTracks();
                if (mView.isActive()) {
                    mView.showTrack(trackEntityList);
                }
            }

            @Override
            public void onError() {
                if (mView.isActive()) {
                    mView.showLoadTracksError();
                }
            }
        });
    }

    @Override
    public void openTrackDetails(TrackEntity trackEntity) {
        if (mView.isActive()) {
            mView.showTrackDetailUi(trackEntity.getId());
        }
    }


}
