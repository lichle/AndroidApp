package mobile.lichle.com.trackme.interactor;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;

/**
 * Created by lich on 9/27/18.
 */

public class UpdateDurationTask {

    private Timer mTimer;
    private int mTimeCounter;

    private Handler mTimeHandler = new Handler(Looper.getMainLooper());

    private TimerTask mTimerTask;

    private boolean mIsTimerRunning;

    private TrackCacheRepository mTrackRepository;

    private boolean mIsRunning;

    public UpdateDurationTask(@NonNull TrackCacheRepository repository) {
        mTrackRepository = repository;
    }

    public int getTimeCounter() {
        return mTimeCounter;
    }

    public void start(final DurationChanged durationChanged) {
        if (null == mTimer) {
            mTimer = new Timer();
        }
        if (null == mTimerTask) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mIsRunning) {
                        mTimeCounter++;
                        mTimeHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                durationChanged.onDurationChanged(mTimeCounter);
                            }
                        });
                    }
                }
            };
        }
        if (!mIsTimerRunning) {
            mTimer.schedule(mTimerTask, 1000, 1000);
            mIsTimerRunning = true;
        }
    }

    public void allowTracking(boolean value) {
        mIsRunning = value;
    }

    public void stopTracking() {
        mIsRunning = false;
        mTimeCounter = 0;
        if (null != mTimerTask) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (null != mTimer) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        mIsTimerRunning = false;
    }

    public void resetData() {
        mTimeCounter = 0;
        mIsRunning = false;
        mIsTimerRunning = false;
    }

    public interface DurationChanged {
        void onDurationChanged(int value);
    }


}
