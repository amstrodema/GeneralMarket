package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 12/02/2019.
 */

public class HelpAdapter extends RecyclerView.Adapter <HelpAdapter.AllCommoditiesViewHolder> implements Filterable{
    private ArrayList<HelpModel> HelpList;
    private ArrayList<HelpModel> HelpListFull;

    private HelpAdapter.OnAllCommoditiesItemClickListener mListener;
    public void setOnItemClickListener(HelpAdapter.OnAllCommoditiesItemClickListener listener){
        mListener=listener;
    }
    public interface  OnAllCommoditiesItemClickListener{
        void onItemClick(int position);
    }

    public static class AllCommoditiesViewHolder extends RecyclerView.ViewHolder{
        public ImageButton helpUpDown;
        public TextView helpSubject, helpMessage;
        private boolean messageShow = false;
        private RelativeLayout helpRelative;
        public AllCommoditiesViewHolder(View itemView, final HelpAdapter.OnAllCommoditiesItemClickListener listener){
            super(itemView);
            helpRelative = itemView.findViewById(R.id.helpRelative);
            helpUpDown= itemView.findViewById(R.id.helpUpDownImgBtn);
            helpSubject = itemView.findViewById(R.id.helpSubject);
            helpMessage = itemView.findViewById(R.id.helpMessage);
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
            helpRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(messageShow){
                      helpUpDown.setImageResource(R.drawable.arrow_down);
                      helpMessage.setVisibility(View.GONE);
                      messageShow =false;
                  }
                  else{
                      helpUpDown.setImageResource(R.drawable.arrow_up);
                      helpMessage.setVisibility(View.VISIBLE);
                      messageShow = true;
                  }
                }
            });
        }
    }
    @Override
    public HelpAdapter.AllCommoditiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_recycler, parent, false);
        HelpAdapter.AllCommoditiesViewHolder evh = new HelpAdapter.AllCommoditiesViewHolder(v, mListener);
        return evh;

    }

    public HelpAdapter (ArrayList<HelpModel>helpList){
        HelpList =helpList;
        HelpListFull = new ArrayList<>(helpList);
    }
    @Override
    public void onBindViewHolder(HelpAdapter.AllCommoditiesViewHolder holder, int position) {
        HelpModel currentItem= HelpList.get(position);
        holder.helpMessage.setText(currentItem.getHelpMessage());
        holder.helpSubject.setText(currentItem.getHelpTitle());
        //String theIngrdient= currentItem.getQty()+" "+ currentItem.getItemName();
       // holder.itemName.setText(theIngrdient);
    }

    @Override
    public int getItemCount() {
        return HelpList.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<HelpModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(HelpListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (HelpModel item: HelpListFull) {
                    if(item.getHelpMessage().toLowerCase().contains(filteredPattern) || item.getHelpTitle().toLowerCase().contains(filteredPattern)){
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
            HelpList.clear();
            HelpList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

}