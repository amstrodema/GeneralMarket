package comgalaxyglotech.confirmexperts.generalmarket;

import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ELECTRON on 03/19/2019.
 */

public class StoreAdapter  extends RecyclerView.Adapter <StoreAdapter.StoreViewHolder> implements Filterable{
    private ArrayList<StoreDisplayModel> StoreList;
    private ArrayList<StoreDisplayModel> StoreListFull;
    private ArrayList<NewStockDisplayModel> displayData;
    private StoreAdapter.OnStoreItemClickListener mListener;
    private DataClass dataClass = new DataClass();
    private FirebaseStorage firebaseStorage;
    private LocationHandler locationHandler = new LocationHandler();
    public void setOnItemClickListener(StoreAdapter.OnStoreItemClickListener listener){
        mListener=listener;
    }
    public interface  OnStoreItemClickListener{
        void onItemClick(int position);
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder{
        public TextView storeName,storeTotal,openTime,newStoreClosing,newStoreDelivery,storeDistance,banned;
        public CardView contentHolder;
        public RatingBar rating;
        public ImageView storeImage;
        public StoreViewHolder(View itemView, final StoreAdapter.OnStoreItemClickListener listener){
            super(itemView);
            storeImage  =  itemView.findViewById(R.id.storeImage);
            contentHolder =  itemView.findViewById(R.id.contentHolder);
            storeDistance = itemView.findViewById(R.id.storeDistance);
            newStoreDelivery= itemView.findViewById(R.id.newStoreDelivery);
            storeName = itemView.findViewById(R.id.newStoreName);
            storeTotal=itemView.findViewById(R.id.newStoreTotal);
            openTime=itemView.findViewById(R.id.newStoreTradeDays);
            newStoreClosing=itemView.findViewById(R.id.newStoreClosing);
            banned=itemView.findViewById(R.id.banned);
            rating = itemView.findViewById(R.id.storeRating);
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
    public StoreAdapter.StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_recycler, parent, false);
        StoreAdapter.StoreViewHolder evh = new StoreAdapter.StoreViewHolder(v, mListener);
        return evh;

    }

    public StoreAdapter (ArrayList<StoreDisplayModel>storeList, ArrayList<NewStockDisplayModel> displayDat){
        StoreList =storeList;
        StoreListFull = new ArrayList<>(storeList);
        displayData = new ArrayList<>(displayDat);
        firebaseStorage = FirebaseStorage.getInstance();
    }
    @Override
    public void onBindViewHolder(final StoreAdapter.StoreViewHolder holder, int position) {
        //currentItem.getTotalItemz()
        StoreDisplayModel currentItem= StoreList.get(position);
        int itemCount=0;
        ModelClass model = new ModelClass();
        holder.storeImage.setImageResource(R.drawable.store_clip);
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Store").child(currentItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.storeImage);
            }

        });
        holder.rating.setRating(model.loadRating(currentItem.getRatin()));
        for (NewStockDisplayModel item: displayData) {
            if(item.getStoreId().equals(currentItem.getId())){
                itemCount++;
            }
        }
        String itemCnt = ""+itemCount;
        if(currentItem.isBan())
            holder.banned.setVisibility(View.VISIBLE);
        else
            holder.banned.setVisibility(View.INVISIBLE);
        holder.storeName.setText(currentItem.getStoreName());
        holder.storeTotal.setText(itemCnt);
       //opening time is set by tradedays and total item passes closing time.
      try{
          if(currentItem.getDelivery().equals("YES") || currentItem.getDelivery().equals("AVAILABLE")){
              holder.newStoreDelivery.setText("DSA");
          }
          else{
              holder.newStoreDelivery.setText("NDS");
          }
      }
      catch (Exception e){
          holder.newStoreDelivery.setText("NDS");
      }
        String open ="Opens "+dataClass.timeCheck(currentItem.getTradeDays());
        if(TimeLeft(currentItem.getTotalItemz(),currentItem.getTradeDays()).equals("CLOSED")){
            //holder.newStoreClosing.setText("CLOSED");
            String value = "Closed ["+open+"]";
            holder.openTime.setText(value);
        }
        else{
            String close=TimeLeft(currentItem.getTotalItemz(),currentItem.getTradeDays());
            //holder.newStoreClosing.setText(close);
            String value = "Opened ["+close+"]";
            holder.openTime.setText(value);
        }
        //location1 is where we are and location2 is the market

        holder.storeDistance.setText(currentItem.getGps());
    }

    @Override
    public int getItemCount() {
        return StoreList.size();
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
            ArrayList<StoreDisplayModel> filteredStoreList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredStoreList.addAll(StoreListFull);
            }
            else{
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (StoreDisplayModel item: StoreListFull) {
                    if(item.getStoreName().toLowerCase().contains(filteredPattern) || item.getDesc().toLowerCase().contains(filteredPattern) || item.getLoc().toLowerCase().contains(filteredPattern)){
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
            StoreList.clear();
            StoreList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}

