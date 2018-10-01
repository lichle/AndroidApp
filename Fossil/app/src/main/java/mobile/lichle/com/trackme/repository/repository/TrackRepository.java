package mobile.lichle.com.trackme.repository.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

import mobile.lichle.com.trackme.interactor.SaveTrack;
import mobile.lichle.com.trackme.repository.MainThreadExecutor;
import mobile.lichle.com.trackme.repository.dao.TrackDao;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lich on 9/29/18.
 */

public class TrackRepository implements TrackDataSource {

    private static volatile TrackRepository sInstance;

    private TrackDao mTrackDao;

    private Executor mBackgroundThread;

    private Executor mMainThread;

    private TrackRepository(@NonNull TrackDao trackDao, @NotNull Executor backgroundThread, @NotNull MainThreadExecutor mainThread) {
        mTrackDao = trackDao;
        mBackgroundThread = backgroundThread;
        mMainThread = mainThread;
    }

    public static TrackRepository getInstance(@NonNull TrackDao trackDao,
                                              @NotNull Executor backgroundThread,
                                              @NotNull MainThreadExecutor mainThread) {
        if (sInstance == null) {
            synchronized (TrackRepository.class) {
                if (sInstance == null) {
                    sInstance = new TrackRepository(trackDao, backgroundThread, mainThread);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void saveTrack(@NonNull final TrackEntity entity, final SaveTrack.ResponseValue responseValue) {
        long trackId = mTrackDao.insert(entity);
        responseValue.getTrackEntity().setId(trackId);
        Log.d("lichne", "count ne");
    }


    @Override
    public void getTracks(@NonNull final LoadTracksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<TrackEntity> trackEntityList = mTrackDao.loadAllTracks();
                mMainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (trackEntityList.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTracksLoaded(trackEntityList);
                        }
                    }
                });
            }
        };

        mBackgroundThread.execute(runnable);
    }

    @Override
    public void getTrack(@NonNull final int trackId, @NonNull final GetTrackCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final TrackEntity trackEntity = mTrackDao.getTrack(trackId);
                mMainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (null != trackEntity) {
                            callback.onTrackLoaded(trackEntity);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mBackgroundThread.execute(runnable);
    }

}
