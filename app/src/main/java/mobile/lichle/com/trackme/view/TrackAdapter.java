package mobile.lichle.com.trackme.view;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.repository.entity.TrackEntity;

/**
 * Created by lich on 9/28/18.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<TrackEntity> mTrackList;
    private ItemClickListener mListener;

    public TrackAdapter(List<TrackEntity> list) {
        mTrackList = list;
    }

    public void setTrackList(List<TrackEntity> trackList) {
        mTrackList = trackList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_row, parent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        TrackEntity track = mTrackList.get(position);
        final String timeText = DateFormat.format("yyyy-MM-dd hh:mm:ss a", new Date(track.getCreatedTime())).toString();
        holder.mCreatedTimeText.setText(timeText);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTrackList == null) {
            return 0;
        }
        return mTrackList.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mCreatedTimeText;

        private ItemClickListener mItemClickListener;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mCreatedTimeText = itemView.findViewById(R.id.text_view_created_time);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onClick(v, getAdapterPosition());
        }
    }

}
