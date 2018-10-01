package mobile.lichle.com.trackme.repository.model;

/**
 * Created by lich on 9/27/18.
 */

public class TrackLocation {

    private double mLatitude;
    private double mLongitude;
    private long mCreatedTime;
    private float mVelocity;

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public long getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(long mCreatedTime) {
        this.mCreatedTime = mCreatedTime;
    }

    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float mVelocity) {
        this.mVelocity = mVelocity;
    }
}
