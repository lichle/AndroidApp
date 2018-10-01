package mobile.lichle.com.trackme.repository.model;

import java.util.List;

import mobile.lichle.com.trackme.utils.FormatterUtils;

/**
 * Created by lich on 9/27/18.
 */

public class Route {

    private float mDistance;
    private int mDuration;
    private float mVelocity;
    private List<TrackLocation> mLocationList;


    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float mVelocity) {
        this.mVelocity = mVelocity;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public List<TrackLocation> getLocationList() {
        return mLocationList;
    }

    public void setLocationList(List<TrackLocation> mLocationList) {
        this.mLocationList = mLocationList;
    }

    public TrackLocation getPreviousLocation() {
        int size = mLocationList.size();
        if (size > 1) {
            return mLocationList.get(size - 2);
        }
        return null;
    }

    public TrackLocation getCurrentLocation() {
        int size = mLocationList.size();
        if (size > 0) {
            return mLocationList.get(size - 1);
        }
        return null;
    }


    public String getDistanceInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        String distanceText = FormatterUtils.convertDistanceToString(getDistance());
        stringBuilder.append("Distance: " + distanceText);
        return stringBuilder.toString();
    }

    public String getVelocityAndDuration() {
        StringBuilder stringBuilder = new StringBuilder();
        String velocityText = FormatterUtils.converVelocityToString(getVelocity());
        String durationText = FormatterUtils.convertTimeToString(getDuration());
        stringBuilder.append("Velocity: " + velocityText);
        stringBuilder.append("\t");
        stringBuilder.append("Duration: " + durationText);
        return stringBuilder.toString();
    }

}
