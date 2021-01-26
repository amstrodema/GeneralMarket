package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ELECTRON on 03/03/2019.
 */

public class MarketAdapter extends RecyclerView.Adapter <MarketAdapter.MarketViewHolder> implements Filterable{
    private ArrayList<marketModel> MarketList;
    private ArrayList<marketModel> MarketListFull;
    private  OnMarketItemClickListener mListener;
    LocationHandler locationHandler = new LocationHandler();
    public void setOnItemClickListener(OnMarketItemClickListener listener){
        mListener=listener;
    }
    public interface  OnMarketItemClickListener{
        void onItemClick(int position);
    }

    public static class MarketViewHolder extends RecyclerView.ViewHolder{
        public TextView marketName,nxtTrade,marketId,tradeFreq,mktRecy_Loc, locationDistance,storeImage;
        public RatingBar rating;
        public CardView contentHolder;
        public MarketViewHolder(View itemView, final OnMarketItemClickListener listener){
            super(itemView);
            storeImage =  itemView.findViewById(R.id.storeImage);
            contentHolder =  itemView.findViewById(R.id.contentHolder);
            locationDistance = itemView.findViewById(R.id.locationDistance);
            mktRecy_Loc = itemView.findViewById(R.id.mktRecy_Loc);
            marketName = itemView.findViewById(R.id.mktRecy_marketName);
            nxtTrade=itemView.findViewById(R.id.mktRecy_NxtTrade);
            tradeFreq=itemView.findViewById(R.id.mktRecy_TradeFreq);
            marketId=itemView.findViewById(R.id.mktRecy_mktID);
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
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_recycler, parent, false);
        MarketViewHolder evh = new MarketViewHolder(v, mListener);
        return evh;

    }

    public MarketAdapter (ArrayList<marketModel>marketList){
        MarketList =marketList;
        MarketListFull = new ArrayList<>(marketList);
    }
    @Override
    public void onBindViewHolder(MarketViewHolder holder, int position) {
        marketModel currentItem= MarketList.get(position);

        holder.storeImage.setText(ModelClass.initials(currentItem.getMarketName()));
        holder.marketName.setText(currentItem.getMarketName());
        String NextTrade = GetNextDay(currentItem.getLastTradingDay(),currentItem.getMarketFreq());
        String lastTradeDay;
        if(NextTrade.equals("0"))
        {
            NextTrade ="Next Trade: "+DayAdd(checkFreq(currentItem.getMarketFreq()));
            holder.nxtTrade.setText(NextTrade);
            lastTradeDay ="Trades Today";
            holder.tradeFreq.setText(lastTradeDay);
        }
        else {
            lastTradeDay ="Traded on "+DayMinus(checkFreq(currentItem.getMarketFreq()),NextTrade);
            holder.tradeFreq.setText(lastTradeDay);
            NextTrade = "Next Trade: " +NextTrade;
            holder.nxtTrade.setText(NextTrade);
        }
        //location1 is where we are and location2 is the market

        holder.locationDistance.setText(currentItem.getGps());
        holder.mktRecy_Loc.setText(currentItem.getMktRecy_Loc());

        holder.marketId.setText(currentItem.getMarketId());
    }

    @Override
    public int getItemCount() {
        return MarketList.size();
    }
    private String setFreq(String MarketFreq){
       //freq
       String [] MarketFreqOptions = new String[]{"Everyday Trading","Every Week Trading","Every 2 Days Trading","Every 3 Days Trading","Every 4 Days Trading","Every 5 Days Trading","Every 6 Days Trading"};
       if(MarketFreqOptions[0].equals(MarketFreq)){
           return "Trades Everyday";
       }
       else if(MarketFreqOptions[1].equals(MarketFreq)){
           return "Trades Every Week";
       }
       else if(MarketFreqOptions[2].equals(MarketFreq)){
           return "2 days Trade Interval";
       }
       else if(MarketFreqOptions[3].equals(MarketFreq)){
           return "3 days Trade Interval";
       }
       else if(MarketFreqOptions[4].equals(MarketFreq)){
           return "4 days Trade Interval";
       }
       else if(MarketFreqOptions[5].equals(MarketFreq)){
           return "5 days Trade Interval";
       }
       else if(MarketFreqOptions[6].equals(MarketFreq)){
           return "6 days Trade Interval";
       }
       else{
           return "Trades Everyday";
       }
   }
    private int checkFreq(String MarketFreq){
        //freq
        String [] MarketFreqOptions = new String[]{"Everyday Trading","Every Week Trading","Every 2 Days Trading","Every 3 Days Trading","Every 4 Days Trading","Every 5 Days Trading","Every 6 Days Trading"};
        if(MarketFreqOptions[0].equals(MarketFreq)){
            return 1;
        }
        else if(MarketFreqOptions[1].equals(MarketFreq)){
            return 7;
        }
        else if(MarketFreqOptions[2].equals(MarketFreq)){
            return 2;
        }
        else if(MarketFreqOptions[3].equals(MarketFreq)){
            return 3;
        }
        else if(MarketFreqOptions[4].equals(MarketFreq)){
            return 4;
        }
        else if(MarketFreqOptions[5].equals(MarketFreq)){
            return 5;
        }
        else if(MarketFreqOptions[6].equals(MarketFreq)){
            return 6;
        }
        else{
            return 0;
        }
    }
    private String DayAdd(int dateAdd){
        SimpleDateFormat ddf=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf=new SimpleDateFormat("EEEE, dd-MM-yyyy");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(ddf.parse(ddf.format(date)));
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH,dateAdd);
        return sdf.format(c.getTime());
    }
    private String GetNextDay(String prevDate, String marketFreq){
        int dayDifference =0;
       try{
            dayDifference = Integer.parseInt(timeDifference(prevDate).split("-")[0]);
       }
       catch (Exception ex){
           return "Trade Day Unknown";
       }
       int freq = checkFreq(marketFreq);
       int modulus = dayDifference % freq;
       if(modulus == 0)
       {
           return "0";
       }
        return DayAdd(freq - modulus);
    // return timeDifference(prevDate)+"";
    }
    private String timeDifference(String prevDate) {

        // String dateStart = "01/14/2012 09:29:58";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String current = format.format(new Date());
        Date d1;
        Date d2;
        String theTime="";
        try {
            d1 = format.parse(prevDate);
            d2 = format.parse(current);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            theTime=diffDays+"-";
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return theTime;
    }
    private String DayMinus(int dayMinus, String futureDate){
      //  SimpleDateFormat ddf=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf=new SimpleDateFormat("EEEE, dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(futureDate));
        } catch (ParseException e) {
            return "Unknown";
        }
        c.add(Calendar.DAY_OF_MONTH,-dayMinus);
        return sdf.format(c.getTime());
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<marketModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(MarketListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (marketModel item: MarketListFull) {
                    if(item.getMarketName().toLowerCase().contains(filteredPattern) || item.getMarketDesc().toLowerCase().contains(filteredPattern) || item.getMktRecy_Loc().toLowerCase().contains(filteredPattern)){
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
            MarketList.clear();
            MarketList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
