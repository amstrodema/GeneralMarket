package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreItemDisplayModel;

/**
 * Created by ELECTRON on 03/29/2019.
 */

public class StoreItemAdapter extends RecyclerView.Adapter <StoreItemAdapter.StoreItemViewHolder> {
    private ArrayList<StoreItemDisplayModel> StoreItemList;
    private  OnStoreItemClickListener mListener;
    public void setOnItemClickListener(OnStoreItemClickListener listener){
        mListener=listener;
    }
    public interface  OnStoreItemClickListener{
        void onItemClick(int position);
    }

    public static class StoreItemViewHolder extends RecyclerView.ViewHolder{
        public TextView storeItemName,qty,availQty,storeItemMetric,price;
        public StoreItemViewHolder(View itemView, final OnStoreItemClickListener listener){
            super(itemView);
            storeItemName = itemView.findViewById(R.id.storeItemItemName);
            qty = itemView.findViewById(R.id.storeItemMetricsQty);
            storeItemMetric = itemView.findViewById(R.id.storeItemMetrics);
            price = itemView.findViewById(R.id.storeItemPrice);
            availQty = itemView.findViewById(R.id.storeItemAvailableValue);
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
    public StoreItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item_recycler, parent, false);
        StoreItemViewHolder evh = new StoreItemViewHolder(v, mListener);
        return evh;

    }

    public StoreItemAdapter (ArrayList<StoreItemDisplayModel>storeList){
        StoreItemList =storeList;
    }
    @Override
    public void onBindViewHolder(StoreItemViewHolder holder, int position) {
        StoreItemDisplayModel currentItem= StoreItemList.get(position);
        holder.storeItemName.setText(currentItem.getStoreItemName());
        holder.qty.setText(currentItem.getStoreItemMetric());
        holder.availQty.setText(currentItem.getStoreItemAvailQty());
        holder.storeItemMetric.setText(currentItem.getStoreItemQtyMetric());
        int low, high;
        low= Integer.parseInt(currentItem.getStoreItemPriceLow());
        high =Integer.parseInt(currentItem.getStoreItemPriceHigh());
        if(low==high){
            holder.price.setText(currentItem.getStoreItemPriceLow());
        }
        else{
            String val =currentItem.getStoreItemPriceLow()+" - "+currentItem.getStoreItemPriceHigh();
            holder.price.setText(val);
        }
    }

    @Override
    public int getItemCount() {
        return StoreItemList.size();
    }


}