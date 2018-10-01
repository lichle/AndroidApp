package mobile.lichle.com.trackme.interactor;

/**
 * Created by lichvl.dp on 9/28/2018.
 */

public class InteractorHandler {

    private static InteractorHandler sInstance;

    private InteratorScheduler mScheduler;

    private InteractorHandler(InteratorScheduler scheduler) {
        mScheduler = scheduler;
    }

    public static InteractorHandler getInstance() {
        if (null == sInstance) {
            sInstance = new InteractorHandler(new InteratorThreadPoolScheduler());
        }
        return sInstance;
    }

    public <V extends Interactor.ResponseValue> void notifyResponse(final V response,
                                                                    final Interactor.InteractorCallback<V> useCaseCallback) {
        mScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends Interactor.ResponseValue> void notifyError(
            final Interactor.InteractorCallback<V> useCaseCallback) {
        mScheduler.onError(useCaseCallback);
    }

    public <T extends Interactor.RequestValues, R extends Interactor.ResponseValue> void execute(
            final Interactor<T, R> useCase, T values, Interactor.InteractorCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setInteratorCallback(new UiCallbackWrapper(callback, this));

        mScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    private static final class UiCallbackWrapper<V extends Interactor.ResponseValue> implements
            Interactor.InteractorCallback<V> {
        private final Interactor.InteractorCallback<V> mCallback;
        private final InteractorHandler mInteractorHandler;

        public UiCallbackWrapper(Interactor.InteractorCallback<V> callback,
                                 InteractorHandler useCaseHandler) {
            mCallback = callback;
            mInteractorHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mInteractorHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError() {
            mInteractorHandler.notifyError(mCallback);
        }
    }


}
