package mobile.lichle.com.trackme.repository;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by lichvl.dp on 9/27/2018.
 */

public class MainThreadExecutor implements Executor {

    private static MainThreadExecutor sInstance;

    private Handler mainThreadHandler;

    private MainThreadExecutor() {
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static MainThreadExecutor getInstance() {
        if (sInstance == null) {
            synchronized (MainThreadExecutor.class) {
                if (sInstance == null) {
                    sInstance = new MainThreadExecutor();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mainThreadHandler.post(command);
    }


}
