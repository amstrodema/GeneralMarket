package comgalaxyglotech.confirmexperts.generalmarket.BL.Farm;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.LocationHandler;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 09/23/2019.
 */

public class FarmAdapter extends RecyclerView.Adapter <FarmAdapter.AllCommoditiesViewHolder> implements Filterable{
    private ArrayList<FarmDisplayModel> AllCommoditiesList;
    private ArrayList<FarmDisplayModel> AllCommoditiesListFull;
    private ArrayList<NewStockDisplayModel> displayData;
    private DataClass dataClass= new DataClass();
    private FarmAdapter.OnAllCommoditiesItemClickListener mListener;
    private ModelClass modelClass = new ModelClass();
    private LocationHandler locationHandler = new LocationHandler();
    public void setOnItemClickListener(FarmAdapter.OnAllCommoditiesItemClickListener listener){
        mListener=listener;
    }
    public interface  OnAllCommoditiesItemClickListener{
        void onItemClick(int position);
    }

    public static class AllCommoditiesViewHolder extends RecyclerView.ViewHolder{
        public TextView farmName,farmStatus,farmCloseTime,farmDelivery,farmStock,farmDistance,banned;
        public RatingBar farmRating;
        public CardView contentHolder;
        public AllCommoditiesViewHolder(View itemView, final FarmAdapter.OnAllCommoditiesItemClickListener listener){
            super(itemView);
            contentHolder = itemView.findViewById(R.id.contentHolder);
            farmDistance = itemView.findViewById(R.id.farmDistance);
            farmCloseTime = itemView.findViewById(R.id.farmCloseTime);
            farmDelivery=itemView.findViewById(R.id.farmdelivery);
            farmName=itemView.findViewById(R.id.farmName);
            farmStatus=itemView.findViewById(R.id.farmStatus);
            farmStock=itemView.findViewById(R.id.farmStock);
            banned=itemView.findViewById(R.id.banned);
           // farmRating=itemView.findViewById(R.id.farm_ratingBar);
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
    public FarmAdapter.AllCommoditiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farm_recycler, parent, false);
        FarmAdapter.AllCommoditiesViewHolder evh = new FarmAdapter.AllCommoditiesViewHolder(v, mListener);
        return evh;

    }

    public FarmAdapter (ArrayList<FarmDisplayModel>allCommoditiesList, ArrayList<NewStockDisplayModel> displayData){
        AllCommoditiesList =allCommoditiesList;
        AllCommoditiesListFull = new ArrayList<>(allCommoditiesList);
        this.displayData = new ArrayList<>(displayData);
    }
    @Override
    public void onBindViewHolder(FarmAdapter.AllCommoditiesViewHolder holder, int position) {
        FarmDisplayModel currentItem= AllCommoditiesList.get(position);
        holder.farmName.setText(currentItem.getName());
       // holder.farmStatus.setText(currentItem.getStatus());
       // holder.farmCloseTime.setText(currentItem.getCloseTime());
        if(currentItem.isBan())
            holder.banned.setVisibility(View.VISIBLE);
        else
            holder.banned.setVisibility(View.INVISIBLE);
        String open ="OPENS "+dataClass.timeCheck(currentItem.getStatus());
        if(TimeLeft(currentItem.getCloseTime(),currentItem.getStatus()).equals("CLOSED")){
            holder.farmCloseTime.setText("CLOSED");
            holder.farmStatus.setText(open);
        }
        else{
            String close=TimeLeft(currentItem.getCloseTime(),currentItem.getStatus());
            holder.farmCloseTime.setText(close);
            holder.farmStatus.setText("Currently Opened");
        }
        try{
            if(currentItem.getDeliveryMode().equals("YES") || currentItem.getDeliveryMode().equals("AVAILABLE")){
                holder.farmDelivery.setText("DSA");
            }
            else{
                holder.farmDelivery.setText("NDS");
            }
        }
        catch (Exception e){
            holder.farmDelivery.setText("NDS");
        }
        int itemCount=0;
        for (NewStockDisplayModel item:displayData) {
            if(item.getStoreId().equals(currentItem.getId())){
                itemCount++;
            }
        }
        String itemCnt = ""+itemCount;
        holder.farmStock.setText(itemCnt);
        holder.farmDistance.setText(currentItem.getGps());
    }

    @Override
    public int getItemCount() {
        return AllCommoditiesList.size();
    }
    private String TimeLeft(String closeTimeString, String openTimeString){
        SimpleDateFormat ddf=new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Date openTime= formatDate(openTimeString);
        Date closeTime= formatDate(closeTimeString);
        Date currentTime = formatDate(ddf.format(date).toString());
        // Calendar currentCal = calendar.setTime(calendar);
        long longCloseTime =closeTime.getTime();
        long longCurrentTime =currentTime.getTime();
        long longOpenTime =openTime.getTime();
        //in the instance of having operational within same meridian, such as opening 6am and closing 4am.
        if(longCloseTime > longOpenTime){
            if((currentTime.before(closeTime) && currentTime.before(openTime)) || (currentTime.after(closeTime) && currentTime.after(openTime))){
                return "CLOSED";
            }
          /*  if((openTime.before(closeTime))&&(currentTime.before(closeTime)  && currentTime.before(openTime))){

            }*/
        }
        else{
            if((currentTime.after(closeTime) && currentTime.before(openTime)) || (currentTime.before(closeTime) && currentTime.after(openTime))){
                return "CLOSED";
            }
           /* if((openTime.after(closeTime))&&(currentTime.after(closeTime)  && currentTime.after(openTime))){
                return "CLOSED";
            }*/
        }
        if(longCloseTime < longOpenTime){
            Date AddTime = formatDate("24:00");
            longCloseTime += AddTime.getTime();
        }
        long xx = longCloseTime - longCurrentTime;
        //before and after is boolean. Therefore to close, both conditions have to be true
        //close 8am, open 6pm--18:00

        return dataClass.timeDifference(xx);
        //LocalDate localDate = LocalDate.of(date)
        // return "\n \n"+currentTime.before(closeTime)  +"=="+ currentTime.before(openTime)+"\n \n"+DateText(closeTime)+currentTime.before(closeTime)  +"-"+ DateText(openTime)+currentTime.after(openTime)+" ;; "+DateText(closeTime)+currentTime.after(closeTime)  + DateText(openTime)+currentTime.before(openTime) +"::"+DateText(currentTime);
    }
    private Date formatDate(String timeString){
        SimpleDateFormat ddf=new SimpleDateFormat("HH:mm");
        //SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        try{
            return ddf.parse(timeString);
        }
        catch (Exception ex){
            return new Date();
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<FarmDisplayModel> filteredFarmList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredFarmList.addAll(AllCommoditiesListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (FarmDisplayModel item: AllCommoditiesListFull) {
                    if(item.getName().toLowerCase().contains(filteredPattern) || item.getDesc().toLowerCase().contains(filteredPattern) || item.getLoc().toLowerCase().contains(filteredPattern)){
                        filteredFarmList.add(item);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values = filteredFarmList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            AllCommoditiesList.clear();
            AllCommoditiesList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}

