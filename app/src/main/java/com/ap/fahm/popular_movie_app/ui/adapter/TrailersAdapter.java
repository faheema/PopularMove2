package com.ap.fahm.popular_movie_app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.model.Video;
import com.ap.fahm.popular_movie_app.ui.listner.OnTrailersItemClickListner;

import java.util.List;

/**
 * Created by Faheem on 18/08/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder>
{

    private List<Video> videoList;
    OnTrailersItemClickListner listner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_trailer_name);

        }
        public void bind(final Video item, final OnTrailersItemClickListner listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public TrailersAdapter(List<Video> videoList, OnTrailersItemClickListner listener) {
        this.videoList = videoList;
        this.listner=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_movie_trailer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Video video = videoList.get(position);
        holder.name.setText(video.getName());
        holder.bind(video, listner);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}