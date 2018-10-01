package mobile.lichle.com.trackme.interactor;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lichvl.dp on 9/28/2018.
 */

public class InteratorThreadPoolScheduler implements InteratorScheduler {

    public static final int POOL_SIZE = 4;
    public static final int MAX_POOL_SIZE = 9;
    public static final int TIMEOUT = 30;
    private final Handler mHandler = new Handler();
    ThreadPoolExecutor mThreadPoolExecutor;

    public InteratorThreadPoolScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    @Override
    public <V extends Interactor.ResponseValue> void notifyResponse(final V response, final Interactor.InteractorCallback<V> successCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                successCallback.onSuccess(response);
            }
        });
    }

    @Override
    public <V extends Interactor.ResponseValue> void onError(final Interactor.InteractorCallback<V> errorCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                errorCallback.onError();
            }
        });
    }

}
