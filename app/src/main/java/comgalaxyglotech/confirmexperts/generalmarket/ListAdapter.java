package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 10/11/2019.
 */

public class ListAdapter extends RecyclerView.Adapter <ListAdapter.AllCommoditiesViewHolder> {
    private ArrayList<ListDisplayClass> AllCommoditiesList;

    private ListAdapter.OnAllCommoditiesItemClickListener mListener;
    public void setOnItemClickListener(ListAdapter.OnAllCommoditiesItemClickListener listener){
        mListener=listener;
    }
    public interface  OnAllCommoditiesItemClickListener{
        void onItemClick(int position);
    }

    public static class AllCommoditiesViewHolder extends RecyclerView.ViewHolder{
        public Button itemName;
        public AllCommoditiesViewHolder(View itemView, final ListAdapter.OnAllCommoditiesItemClickListener listener){
            super(itemView);
            itemName = itemView.findViewById(R.id.listItemName);
            //farmDelivery=itemView.findViewById(R.id.farmdelivery);
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
    public ListAdapter.AllCommoditiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recycler, parent, false);
        ListAdapter.AllCommoditiesViewHolder evh = new ListAdapter.AllCommoditiesViewHolder(v, mListener);
        return evh;

    }

    public ListAdapter (ArrayList<ListDisplayClass>allCommoditiesList){
        AllCommoditiesList =allCommoditiesList;
    }
    @Override
    public void onBindViewHolder(ListAdapter.AllCommoditiesViewHolder holder, int position) {
        ListDisplayClass currentItem= AllCommoditiesList.get(position);
        String theIngrdient= currentItem.getQty()+" "+ currentItem.getItemName();
        holder.itemName.setText(theIngrdient);
    }

    @Override
    public int getItemCount() {
        return AllCommoditiesList.size();
    }


}

