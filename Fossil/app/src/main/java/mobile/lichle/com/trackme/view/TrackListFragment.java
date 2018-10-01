package mobile.lichle.com.trackme.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.interactor.GetTracks;
import mobile.lichle.com.trackme.interactor.InteractorHandler;
import mobile.lichle.com.trackme.presenter.TracksPresenter;
import mobile.lichle.com.trackme.repository.Injection;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;
import mobile.lichle.com.trackme.repository.repository.TrackRepository;


/**
 * Created by lich on 9/28/18.
 */

public class TrackListFragment extends Fragment implements TracksContract.View, TrackAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private TrackAdapter mAdapter;
    private List<TrackEntity> mTrackList;

    private TracksContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InteractorHandler interactorHandler = Injection.provideInteractorHandler();
        TrackRepository trackRepository = Injection.provideTrackRepository(getContext());
        GetTracks getTracks = new GetTracks(trackRepository);
        mPresenter = new TracksPresenter(this, interactorHandler, getTracks);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_track_list, null, false);
        mRecyclerView = root.findViewById(R.id.recycler_view_track);
        mAdapter = new TrackAdapter(mTrackList);
        mAdapter.setItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(TracksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTrack(List<TrackEntity> list) {
        mTrackList = list;
        mAdapter.setTrackList(mTrackList);
    }

    @Override
    public void showTrackDetailUi(long trackId) {
        TrackDetailFragment detailFragment = new TrackDetailFragment();
        detailFragment.setTrackId(trackId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showLoadTracksError() {

    }


    @Override
    public void onClick(View view, int position) {
        TrackEntity trackEntity = mTrackList.get(position);
        mPresenter.openTrackDetails(trackEntity);
    }


}
