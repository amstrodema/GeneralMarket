package comgalaxyglotech.confirmexperts.generalmarket;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 02/14/2020.
 */

public class TransactionAdapter  extends RecyclerView.Adapter <TransactionAdapter.MetricViewHolder> {
    private ArrayList<ModelDisplayTransaction> transactionList;
    private OnMetricItemClickListener mListener;
    public void setOnItemClickListener(TransactionAdapter.OnMetricItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMetricItemClickListener{
        void onItemClick(int position);
        void onCancelClick(int position);
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView itemQty;
        public TextView itemCost;
        public Button cancelTrans;
        public TextView storeName;
        public MetricViewHolder(View itemView, final TransactionAdapter.OnMetricItemClickListener listener){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQty=itemView.findViewById(R.id.itemQuantity);
            itemCost=itemView.findViewById(R.id.itemCost);
            cancelTrans=itemView.findViewById(R.id.cancel);
            storeName=itemView.findViewById(R.id.storeName);
            cancelTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCancelClick(position);
                        }
                    }
                }
            });
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
    public TransactionAdapter.MetricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_transaction, parent, false);
        TransactionAdapter.MetricViewHolder evh = new TransactionAdapter.MetricViewHolder(v, mListener);
        return evh;
    }

    public TransactionAdapter (ArrayList<ModelDisplayTransaction>transaction_List){
        transactionList =transaction_List;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(TransactionAdapter.MetricViewHolder holder, int position) {
        ModelDisplayTransaction currentItem= transactionList.get(position);
       try{
           if(currentItem.getType().equals("Store") || currentItem.getType().equals("Farm")){
               holder.cancelTrans.setText("Proceed");
             //  holder.cancelTrans.setBackgroundColor(R.color.myGreen);
           }
           if(currentItem.getIsConfirmed().equals("True")){
               holder.cancelTrans.setVisibility(View.GONE);
           }
       }
       catch (Exception e){

       }
        holder.itemName.setText(currentItem.getItemName());
        holder.itemCost.setText(currentItem.getItmCost()+"");
        holder.storeName.setText(currentItem.getStoreName());
        holder.itemQty.setText(currentItem.getItmQty().toString()+"");
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


}
