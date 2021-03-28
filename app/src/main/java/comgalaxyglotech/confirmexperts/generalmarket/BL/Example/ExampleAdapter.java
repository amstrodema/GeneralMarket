package comgalaxyglotech.confirmexperts.generalmarket.BL.Example;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.itemModel;

/**
 * Created by ELECTRON on 03/02/2019.
 */

public class ExampleAdapter extends RecyclerView.Adapter <ExampleAdapter.ExampleViewHolder> implements Filterable{
    private  ArrayList<itemModel> mExampleList;
    private  ArrayList<itemModel> mExampleListFull;
    private  OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public interface  OnItemClickListener{
        void onItemClick(int position);
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView metricItem, metricLowestPrice, metricNoSellers, metricHighestPrice;
        public ExampleViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            metricItem=itemView.findViewById(R.id.theItem);
            metricLowestPrice=itemView.findViewById(R.id.LowestPrice);
           metricNoSellers=itemView.findViewById(R.id.NoSellers);
            metricHighestPrice=itemView.findViewById(R.id.HighestPrice);
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
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.metric_recycler, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;

    }

    public ExampleAdapter (ArrayList<itemModel>exampleList){
        mExampleList =exampleList;
        mExampleListFull = new ArrayList<>(exampleList);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        itemModel currentItem= mExampleList.get(position);
        holder.metricItem.setText(currentItem.getItemName());
        String val = currentItem.getNumberOfSeller()+"";
        holder.metricNoSellers.setText(val);
        val = currentItem.getLowest()+"";
        holder.metricLowestPrice.setText(val);
        val = currentItem.getHighest()+"";
        holder.metricHighestPrice.setText(val);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<itemModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(mExampleListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (itemModel item: mExampleListFull) {
                    if(item.getItemName().toLowerCase().contains(filteredPattern) || String.valueOf(item.getLowest()).contains(filteredPattern) || String.valueOf(item.getHighest()).contains(filteredPattern)){
                        filteredStoreList.add(item);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values = filteredStoreList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
