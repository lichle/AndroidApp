package mobile.lichle.com.trackme.repository;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lichvl.dp on 9/27/2018.
 */

public class BackgroundThreadExecutor implements Executor {

    private static BackgroundThreadExecutor sInstance;

    private final Executor mDiskIO;

    private BackgroundThreadExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    public static BackgroundThreadExecutor getInstance() {
        if (sInstance == null) {
            synchronized (BackgroundThreadExecutor.class) {
                if (sInstance == null) {
                    sInstance = new BackgroundThreadExecutor();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mDiskIO.execute(command);
    }

}
