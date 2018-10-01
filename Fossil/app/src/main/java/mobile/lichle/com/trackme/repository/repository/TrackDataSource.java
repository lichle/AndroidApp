package mobile.lichle.com.trackme.repository.repository;

import android.support.annotation.NonNull;

import java.util.List;

import mobile.lichle.com.trackme.interactor.SaveTrack;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lich on 9/29/18.
 */

public interface TrackDataSource {


    void saveTrack(@NonNull TrackEntity entity, SaveTrack.ResponseValue responseValue);

//    void saveTrack(@NonNull TrackEntity entity);

    void getTracks(@NonNull LoadTracksCallback callback);

    void getTrack(@NonNull int trackId, @NonNull GetTrackCallback callback);

    interface LoadTracksCallback {

        void onTracksLoaded(List<TrackEntity> trackEntities);

        void onDataNotAvailable();
    }

    interface GetTrackCallback {

        void onTrackLoaded(TrackEntity trackEntity);

        void onDataNotAvailable();
    }

}
