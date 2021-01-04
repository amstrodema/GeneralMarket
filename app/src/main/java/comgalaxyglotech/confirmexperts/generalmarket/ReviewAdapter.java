package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 10/23/2019.
 */

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.MetricViewHolder> {
    private ArrayList<ReviewDisplayClass> MetricList;
    private ReviewAdapter.OnMetricItemClickListener mListener;
    public void setOnItemClickListener(ReviewAdapter.OnMetricItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMetricItemClickListener{
        void onItemClick(int position);
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder{
        public TextView name,review;
        public RatingBar ratingBar;
        public MetricViewHolder(View itemView, final ReviewAdapter.OnMetricItemClickListener listener){
            super(itemView);
            name=itemView.findViewById(R.id.reviewUser);
            review=itemView.findViewById(R.id.review);
            ratingBar=itemView.findViewById(R.id.reviewRating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    @Override
    public ReviewAdapter.MetricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_recycler, parent, false);
        ReviewAdapter.MetricViewHolder evh = new ReviewAdapter.MetricViewHolder(v, mListener);
        return evh;
    }

    public ReviewAdapter (ArrayList<ReviewDisplayClass>metricList){
        MetricList =metricList;
    }
    @Override
    public void onBindViewHolder(ReviewAdapter.MetricViewHolder holder, int position) {
        ReviewDisplayClass currentItem= MetricList.get(position);
        UserProfile profile = UserDataClass.userData.get(currentItem.getReviewerId());
        String userName = profile.lname +" "+profile.fname;
        holder.name.setText(userName.toUpperCase());
        holder.review.setText(currentItem.getReview());
        holder.ratingBar.setRating(currentItem.getRating());
       /*  holder.metric.setText(currentItem.getMetric());
        holder.noTraders.setText(currentItem.getNoTrader());*/
    }

    @Override
    public int getItemCount() {
        return MetricList.size();
    }


}
