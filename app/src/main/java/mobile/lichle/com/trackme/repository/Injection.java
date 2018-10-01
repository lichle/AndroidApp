package mobile.lichle.com.trackme.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;
import mobile.lichle.com.trackme.repository.repository.TrackCacheRepository;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;

/**
 * Created by lichvl.dp on 9/27/2018.
 */

public class Injection {

    private static BackgroundThreadExecutor provideBackgroundThreadExecutor() {
        return BackgroundThreadExecutor.getInstance();
    }

    private static MainThreadExecutor provideMainThreadExecutor() {
        return MainThreadExecutor.getInstance();
    }

    public static TrackCacheRepository provideTrackCacheRepository() {
        return TrackCacheRepository.getInstance();
    }

    public static TrackRepository provideTrackRepository(@NonNull Context context) {
        TrackMeDatabase database = TrackMeDatabase.getInstance(context);
        return TrackRepository.getInstance(database.trackDao(),
                provideBackgroundThreadExecutor(), provideMainThreadExecutor());
    }

    public static RouteRepository provideRouteRepository(@NonNull Context context) {
        TrackMeDatabase database = TrackMeDatabase.getInstance(context);
        return RouteRepository.getInstance(database.routeDao(),
                provideBackgroundThreadExecutor(), provideMainThreadExecutor());
    }

    public static InteractorHandler provideInteractorHandler() {
        return InteractorHandler.getInstance();
    }

}
