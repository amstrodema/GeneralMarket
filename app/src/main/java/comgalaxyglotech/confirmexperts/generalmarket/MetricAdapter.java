package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 03/17/2019.
 */

public class MetricAdapter extends RecyclerView.Adapter <MetricAdapter.MetricViewHolder> {
    private ArrayList<MetricDisplayModel> MetricList;
    private OnMetricItemClickListener mListener;
    public void setOnItemClickListener(MetricAdapter.OnMetricItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMetricItemClickListener{
        void onItemClick(int position);
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,metric,noTraders,lowestPrice,highestPrice;
        public MetricViewHolder(View itemView, final MetricAdapter.OnMetricItemClickListener listener){
            super(itemView);
           // itemName=itemView.findViewById(R.id.metricItem);
           // metric=itemView.findViewById(R.id.metricValue);
          //  noTraders=itemView.findViewById(R.id.metricNoSellers);
          //  lowestPrice=itemView.findViewById(R.id.metricLowestPrice);
           // highestPrice=itemView.findViewById(R.id.metricHighestPrice);
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
    public MetricAdapter.MetricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.metric_recycler, parent, false);
        MetricAdapter.MetricViewHolder evh = new MetricAdapter.MetricViewHolder(v, mListener);
        return evh;
    }

    public MetricAdapter (ArrayList<MetricDisplayModel>metricList){
        MetricList =metricList;
    }
    @Override
    public void onBindViewHolder(MetricAdapter.MetricViewHolder holder, int position) {
        MetricDisplayModel currentItem= MetricList.get(position);
        holder.itemName.setText(currentItem.getItemName());
        holder.highestPrice.setText(currentItem.getHighestPrice());
        holder.lowestPrice.setText(currentItem.getLowestPrice());
        holder.metric.setText(currentItem.getMetric());
        holder.noTraders.setText(currentItem.getNoTrader());
    }

    @Override
    public int getItemCount() {
        return MetricList.size();
    }


}
