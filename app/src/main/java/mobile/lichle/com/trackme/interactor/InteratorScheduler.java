package mobile.lichle.com.trackme.interactor;

/**
 * Created by lichvl.dp on 9/28/2018.
 */

public interface InteratorScheduler {

    void execute(Runnable runnable);

    <V extends Interactor.ResponseValue> void notifyResponse(final V response,
                                                             final Interactor.InteractorCallback<V> successCallback);

    <V extends Interactor.ResponseValue> void onError(final Interactor.InteractorCallback<V> errorCallback);

}
