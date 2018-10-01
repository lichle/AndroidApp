package mobile.lichle.com.trackme.interactor;

/**
 * Created by lich on 9/27/18.
 */

public abstract class Interactor<Q extends Interactor.RequestValues, P extends Interactor.ResponseValue> implements BaseIterator {

    private Q mRequestValues;

    private InteractorCallback<P> mInteratorCallback;

    public Q getRequestValues() {
        return mRequestValues;
    }

    public void setRequestValues(Q mRequestValues) {
        this.mRequestValues = mRequestValues;
    }

    public InteractorCallback<P> getIteratorCallback() {
        return mInteratorCallback;
    }

    public void setInteratorCallback(InteractorCallback<P> mUseCaseCallback) {
        this.mInteratorCallback = mUseCaseCallback;
    }

    @Override
    public void run() {
        executeIterator(mRequestValues);
    }

    public abstract void executeIterator(Q requestValues);

    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    public interface ResponseValue {

    }

    public interface InteractorCallback<R> {
        void onSuccess(R response);

        void onError();
    }

}
