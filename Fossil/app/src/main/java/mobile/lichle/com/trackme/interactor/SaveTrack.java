package mobile.lichle.com.trackme.interactor;

import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;

/**
 * Created by lich on 9/29/18.
 */

public class SaveTrack extends Interactor<SaveTrack.RequestValues, SaveTrack.ResponseValue> {

    private final TrackRepository mTrackRepository;

    public SaveTrack(@NonNull TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void executeIterator(RequestValues requestValues) {
        TrackEntity trackEntity = requestValues.getTrackEntity();
        ResponseValue responseValue = new ResponseValue(trackEntity);
        mTrackRepository.saveTrack(trackEntity, responseValue);
//        getIteratorCallback().onSuccess(new ResponseValue(trackEntity));
        getIteratorCallback().onSuccess(responseValue);
    }

    public static final class RequestValues implements Interactor.RequestValues {

        private final TrackEntity mTrackEntity;

        public RequestValues(@NonNull TrackEntity track) {
            mTrackEntity = track;
        }

        public TrackEntity getTrackEntity() {
            return mTrackEntity;
        }
    }

    public static final class ResponseValue implements Interactor.ResponseValue {

        private final TrackEntity mTrackEntity;

        public ResponseValue(@NonNull TrackEntity trackEntity) {
            mTrackEntity = trackEntity;
        }

        public TrackEntity getTrackEntity() {
            return mTrackEntity;
        }
    }

}
