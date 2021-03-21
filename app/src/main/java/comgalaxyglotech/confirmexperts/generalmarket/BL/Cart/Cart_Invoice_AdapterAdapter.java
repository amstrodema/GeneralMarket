package comgalaxyglotech.confirmexperts.generalmarket.BL.Cart;

import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartInvoiceDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 02/14/2020.
 */

public class Cart_Invoice_AdapterAdapter extends RecyclerView.Adapter <Cart_Invoice_AdapterAdapter.MetricViewHolder> {
    private ArrayList<CartInvoiceDisplayModel> transactionList;
    private OnMetricItemClickListener mListener;
    public void setOnItemClickListener(Cart_Invoice_AdapterAdapter.OnMetricItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMetricItemClickListener{
        void onItemClick(int position);
        void onCancelClick(int position);
        void onRefreshClick(int position);
        void onDeleteClick(int position);
    }

    public static class MetricViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView itemQty;
        public TextView itemCost;
        public TextView cancelTrans;
        public TextView storeName;
        public TextView delete;
        public TextView refresh;
        public MetricViewHolder(View itemView, final Cart_Invoice_AdapterAdapter.OnMetricItemClickListener listener){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQty=itemView.findViewById(R.id.itemQuantity);
            itemCost=itemView.findViewById(R.id.itemCost);
            cancelTrans=itemView.findViewById(R.id.cancel);
            storeName=itemView.findViewById(R.id.storeName);
            delete=itemView.findViewById(R.id.delete);
            refresh=itemView.findViewById(R.id.refresh);
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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onRefreshClick(position);
                        }
                    }
                }
            });
        }
    }
    @Override
    public Cart_Invoice_AdapterAdapter.MetricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_invoice, parent, false);
        return new MetricViewHolder(v, mListener);
    }

    public Cart_Invoice_AdapterAdapter(ArrayList<CartInvoiceDisplayModel>transaction_List){
        transactionList =transaction_List;
    }
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(Cart_Invoice_AdapterAdapter.MetricViewHolder holder, int position) {
        CartInvoiceDisplayModel currentItem= transactionList.get(position);
        if(currentItem.getStatus().equals("Expired")){
            holder.cancelTrans.setVisibility(View.GONE);
        }
        holder.storeName.setText(currentItem.getInvoiceName());
        //String val = Double.parseDouble(currentItem.getInvoiceName())+"";
        holder.itemCost.setText(currentItem.getCost()+"");
        holder.itemName.setText(currentItem.getCartNo()+" Items");
        ModelClass modelClass = new ModelClass();
        if(modelClass.isInvoiceExpired(currentItem.getDateTime()))
            holder.itemQty.setText("(Valid)");
        else
        holder.itemQty.setText("(Expired!)");
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


}
