package mobile.lichle.com.trackme.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.interactor.GetRoute;
import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.presenter.TrackDetailPresenter;
import mobile.lichle.com.trackme.repository.Injection;
import mobile.lichle.com.trackme.repository.model.Route;
import mobile.lichle.com.trackme.repository.repository.RouteRepository;

/**
 * Created by lich on 9/28/18.
 */

public class TrackDetailFragment extends BaseMapFragment implements TrackDetailContract.View {

    private SupportMapFragment mMapFragment;
    private long mTrackId;
    private TrackDetailContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InteractorHandler interactorHandler = Injection.provideInteractorHandler();
        RouteRepository routeRepository = Injection.provideRouteRepository(getContext());
        GetRoute getRoute = new GetRoute(routeRepository);
        mPresenter = new TrackDetailPresenter(this, interactorHandler, getRoute);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_track_detail, null, false);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadRoute(mTrackId);
    }

    public void setTrackId(long trackId) {
        this.mTrackId = trackId;
    }

    @Override
    public void onMapReady() {
        //do more config with map view
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(TrackDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRoutes(List<Route> routeList) {
        drawRoute(routeList, true);
    }

    @Override
    public void showLoadRoutesError() {
        String error = getString(R.string.no_track_data);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

}
