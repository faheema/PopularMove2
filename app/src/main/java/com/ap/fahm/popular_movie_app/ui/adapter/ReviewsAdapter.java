package com.ap.fahm.popular_movie_app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.model.Review;

import java.util.List;


/**
 * Created by Faheem on 18/08/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> implements View.OnClickListener
{

    private List<Review> reviewList;

    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author, content;

        public MyViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.tv_review_author);
            content = (TextView) view.findViewById(R.id.tv_review_content);

        }
    }


    public ReviewsAdapter(List<Review> moviesList) {
        this.reviewList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_movie_reviews, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

