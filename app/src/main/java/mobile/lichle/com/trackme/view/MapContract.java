package mobile.lichle.com.trackme.view;

import android.location.Location;

import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.model.Track;
import mobile.lichle.com.trackme.repository.model.TrackLocation;

/**
 * Created by lich on 9/27/18.
 */

public interface MapContract {

    interface View {

        void changeViewToStartTracking();

        void changeViewToStopTracking();

        void changeViewToResumeTracking();

        void changeViewToPauseTracking();

        void startLocationService();

        boolean isActive();

        void updateDuration(String duration);

        void updateVelocity(String velocity);

        void updateDistance(String distance);

        void updateRoute(Location location);

        void addStartMarker(TrackLocation trackLocation);

        void onAddNewRoute();

        void onUpdateCurrentRouteInfo(Route route);

        void onTrackSaved(boolean mIsSuccess);

        void showMessageNoDataSaved();

    }

    interface Presenter {

        void setView(View view);

        void synchronizeData();

        void requestUpdateLocation();

        void toggleStartPauseTracking();

        void resumeTracking();

        void stopTracking();

        Track getTrack();
    }


}
