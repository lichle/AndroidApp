package mobile.lichle.com.trackme.repository.repository;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

import mobile.lichle.com.trackme.repository.MainThreadExecutor;
import mobile.lichle.com.trackme.repository.dao.RouteDao;
import mobile.lichle.com.trackme.repository.entity.RouteEntity;

/**
 * Created by lichvl.dp on 9/27/2018.
 */

public class RouteRepository implements RouteDataSource {

    private static volatile RouteRepository sInstance;

    private RouteDao mRouteDao;

    private Executor mBackgroundThread;

    private Executor mMainThread;

    private RouteRepository(@NonNull RouteDao routeDao, @NotNull Executor backgroundThread, @NotNull MainThreadExecutor mainThread) {
        mRouteDao = routeDao;
        mBackgroundThread = backgroundThread;
        mMainThread = mainThread;
    }

    public static RouteRepository getInstance(@NonNull RouteDao routeDao,
                                              @NotNull Executor backgroundThread,
                                              @NotNull MainThreadExecutor mainThread) {
        if (sInstance == null) {
            synchronized (RouteRepository.class) {
                if (sInstance == null) {
                    sInstance = new RouteRepository(routeDao, backgroundThread, mainThread);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getRoutes(@NonNull LoadEntitiesCallback callback) {

    }

    @Override
    public void getRoutesByTrackId(@NonNull final long trackId, @NonNull final GetEntityCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<RouteEntity> routes = mRouteDao.loadAllRoutesByTrackId(trackId);
                mMainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (null != routes) {
                            callback.onRoutesLoaded(routes);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mBackgroundThread.execute(runnable);
    }

    @Override
    public void saveRoute(@NonNull final List<RouteEntity> entity) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mRouteDao.insert(entity);
            }
        };
        mBackgroundThread.execute(runnable);
    }

}
