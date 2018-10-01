package mobile.lichle.com.trackme.view;

import mobile.lichle.com.trackme.presenter.BasePresenter;

/**
 * Created by lich on 9/26/18.
 */

public interface BaseView<T extends BasePresenter> {

    boolean isActive();

    void setPresenter(T presenter);

}
