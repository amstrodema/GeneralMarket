package comgalaxyglotech.confirmexperts.generalmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ELECTRON on 10/23/2019.
 */

public class NotificationAdapter extends RecyclerView.Adapter <NotificationAdapter.MetricViewHolder> {
    private ArrayList<NotificationModel> MetricList;
    private NotificationAdapter.OnMetricItemClickListener mListener;
    public void setOnItemClickListener(NotificationAdapter.OnMetricItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMetricItemClickListener{
        void onItemClick(int position);
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder{
        public TextView message,messageMeta;
        public MetricViewHolder(View itemView, final NotificationAdapter.OnMetricItemClickListener listener){
            super(itemView);
            message=itemView.findViewById(R.id.message);
            messageMeta=itemView.findViewById(R.id.messageMeta);
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
    public NotificationAdapter.MetricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_notification, parent, false);
        NotificationAdapter.MetricViewHolder evh = new NotificationAdapter.MetricViewHolder(v, mListener);
        return evh;
    }

    public NotificationAdapter(ArrayList<NotificationModel>metricList){
        MetricList =metricList;
    }
    @Override
    public void onBindViewHolder(NotificationAdapter.MetricViewHolder holder, int position) {
        NotificationModel currentItem= MetricList.get(position);
      //  UserProfile profile = UserDataClass.userData.get(currentItem.getReviewerId());

        holder.message.setText(currentItem.getBody());
        String meta = currentItem.getFromId() + "("+currentItem.getDateTime()+")";
        holder.messageMeta.setText(meta);
      /*   holder.noTraders.setText(currentItem.getNoTrader());*/
    }

    @Override
    public int getItemCount() {
        return MetricList.size();
    }


}
