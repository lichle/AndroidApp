package mobile.lichle.com.trackme.repository.model;

import java.util.List;

/**
 * Created by lich on 9/27/18.
 */

public class Track {

    private long mDuration;
    private List<Route> mRouteList;

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public List<Route> getRouteList() {
        return mRouteList;
    }

    public void setRouteList(List<Route> mRouteList) {
        this.mRouteList = mRouteList;
    }

    public Route getCurrentRoute() {
        int size = mRouteList.size();
        return mRouteList.get(size - 1);
    }

}
