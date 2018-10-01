package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.repository.repository.TrackDataSource;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;

/**
 * Created by lich on 9/29/18.
 */

public class GetTracks extends Interactor<GetTracks.RequestValues, GetTracks.ResponseValue> {

    private final TrackRepository mTrackRepository;

    public GetTracks(@NonNull TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void executeIterator(RequestValues requestValues) {
        mTrackRepository.getTracks(new TrackDataSource.LoadTracksCallback() {
            @Override
            public void onTracksLoaded(List<TrackEntity> trackEntities) {
                ResponseValue responseValue = new ResponseValue(trackEntities);
                getIteratorCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getIteratorCallback().onError();
            }
        });
    }

    public static final class RequestValues implements Interactor.RequestValues {

    }

    public static final class ResponseValue implements Interactor.ResponseValue {

        private List<TrackEntity> mTrackEntityList;

        public ResponseValue(@NonNull List<TrackEntity> trackEntityList) {
            mTrackEntityList = trackEntityList;
        }

        public List<TrackEntity> getTracks() {
            return mTrackEntityList;
        }
    }

}
