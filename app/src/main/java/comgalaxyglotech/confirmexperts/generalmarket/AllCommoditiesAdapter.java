package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 03/28/2019.
 */

public class AllCommoditiesAdapter extends RecyclerView.Adapter <AllCommoditiesAdapter.AllCommoditiesViewHolder> {
    private ArrayList<AllCommodities> AllCommoditiesList;

    private AllCommoditiesAdapter.OnAllCommoditiesItemClickListener mListener;
    public void setOnItemClickListener(AllCommoditiesAdapter.OnAllCommoditiesItemClickListener listener){
        mListener=listener;
    }
    public interface  OnAllCommoditiesItemClickListener{
        void onItemClick(int position);
    }

    public static class AllCommoditiesViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,price,marketOrStoreName,metric,metricLabel,priceLabel;
        public AllCommoditiesViewHolder(View itemView, final AllCommoditiesAdapter.OnAllCommoditiesItemClickListener listener){
            super(itemView);
            itemName = itemView.findViewById(R.id.allCommodityItemName);
            price=itemView.findViewById(R.id.allCommodityPrice);
            marketOrStoreName=itemView.findViewById(R.id.allCommodityMarketOrStore);
            metric=itemView.findViewById(R.id.allCommodityMetrics);
            metricLabel=itemView.findViewById(R.id.allCommodityMetricsLabel);
            priceLabel=itemView.findViewById(R.id.allCommodityPriceLabel);
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
    public AllCommoditiesAdapter.AllCommoditiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_commodities, parent, false);
        AllCommoditiesAdapter.AllCommoditiesViewHolder evh = new AllCommoditiesAdapter.AllCommoditiesViewHolder(v, mListener);
        return evh;

    }

    public AllCommoditiesAdapter (ArrayList<AllCommodities>allCommoditiesList){
        AllCommoditiesList =allCommoditiesList;
    }
    @Override
    public void onBindViewHolder(AllCommoditiesAdapter.AllCommoditiesViewHolder holder, int position) {
       AllCommodities currentItem= AllCommoditiesList.get(position);
        holder.itemName.setText(currentItem.getItemName());
        if (currentItem.getStoreOrMarket().equals("Store")){
            holder.priceLabel.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.metricLabel.setVisibility(View.VISIBLE);
            holder.metric.setVisibility(View.VISIBLE);}
            //NoUser is store price
        holder.price.setText(currentItem.getNoUser());
        holder.metric.setText(currentItem.getMetric());
        holder.marketOrStoreName.setText(currentItem.getMarketOrStoreName());
    }

    @Override
    public int getItemCount() {
        return AllCommoditiesList.size();
    }


}

