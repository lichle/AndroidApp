package mobile.lichle.com.trackme.interactor;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.TrackLocation;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;
import mobile.lichle.com.trackme.utils.LocationUtils;

/**
 * Created by lich on 9/27/18.
 */

public class UpdateVelocityTask {

    private TrackCacheRepository mTrackRepository;

    private Handler mHandler;

    private Runnable mRunnable;

    private float mVelocityAverage;


    public UpdateVelocityTask(@NonNull TrackCacheRepository repository) {
        mTrackRepository = repository;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Calculate velocity every time location change
     *
     * @param currentLocation
     * @param previousLocation
     * @return velocity value in km/h
     */
    public double calculateVelocity(TrackLocation currentLocation, TrackLocation previousLocation) {
        //distance is in meters
        float distance = LocationUtils.getDistance(currentLocation, previousLocation);
        //time is in mili seconds
        long time = (currentLocation.getCreatedTime() - previousLocation.getCreatedTime());
        //velocity in km/h
        double velocity = distance / time * 3600;
        return velocity;
    }

    public void start(final VelocityChanged velocityChanged) {
        final Route route = mTrackRepository.getTrack().getCurrentRoute();
        if (null == mRunnable) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    TrackLocation location = route.getCurrentLocation();
                    float velocity = 0;
                    if (null != location) {
                        velocity = route.getCurrentLocation().getVelocity();
                        velocityChanged.onVelocityChanged(velocity);

                        if (mVelocityAverage == 0) {
                            mVelocityAverage = velocity;
                        } else {
                            float newVel = mVelocityAverage + velocity;
                            mVelocityAverage = newVel / 2;
                        }
                    }
                    mHandler.postDelayed(mRunnable, 1200);
                }
            };
            mHandler.post(mRunnable);
        }
    }

    public float getVelocityAverage() {
        return mVelocityAverage;
    }

    public void stopTracking() {
        resetData();
        mHandler.removeCallbacksAndMessages(null);
        mRunnable = null;
    }

    public void resetData() {
        mVelocityAverage = 0;
        mHandler.removeCallbacksAndMessages(null);
        mRunnable = null;
    }

    public interface VelocityChanged {

        void onVelocityChanged(float value);

    }

}
