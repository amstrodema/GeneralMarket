package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 10/23/2019.
 */

public class ReviewData {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static ArrayList<ReviewDisplayClass> displayData = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    //sets the parameter for the query
    public void getData(String StoreId, final Context context, final Activity activity, final TextView noReviewLabel){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ReviewDisplayClass> list) {
                displayData=list;
                //recycler
                if (!displayData.isEmpty())noReviewLabel.setVisibility(View.GONE);
                mlayoutManager =new LinearLayoutManager(context);
                mRecyclerView = activity.findViewById(R.id.thisStoreReviewView);
                mRecyclerView.setHasFixedSize(true);
                mAdapter = new ReviewAdapter(displayData);
                mRecyclerView.setLayoutManager(mlayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        },"StoreReview","storeId",StoreId);

    }

    public void getStar_Load_Data(boolean farmData, final ProgressBar progressBar, final Context context, final Intent intent){
        if(!farmData){
            readData(new FirebaseCallback() {
                @Override
                public void onCallback(ArrayList<ReviewDisplayClass> list) {
                    displayData=list;
                    progressBar.setVisibility(View.GONE);
                }
            },"StoreReview","storeId","");
        }
        else{
            readData(new FirebaseCallback() {
                @Override
                public void onCallback(ArrayList<ReviewDisplayClass> list) {
                    displayData=list;
                    progressBar.setVisibility(View.GONE);
                }
            },"FarmReview","storeId","");

        }


    }
    public void getStar_Reload_Data(boolean farmData, final StoreAdapter storeAdapter, final FarmAdapter farmAdapter) {
        if (!farmData) {
            readData(new FirebaseCallback() {
                @Override
                public void onCallback(ArrayList<ReviewDisplayClass> list) {
                    displayData = list;
                    storeAdapter.notifyDataSetChanged();
                }
            }, "StoreReview", "storeId", "");
        } else {
            readData(new FirebaseCallback() {
                @Override
                public void onCallback(ArrayList<ReviewDisplayClass> list) {
                    displayData = list;
                    farmAdapter.notifyDataSetChanged();
                }
            }, "FarmReview", "storeId", "");

        }
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                ArrayList<ReviewDisplayClass> itm = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ReviewMainClass newReview= snapshot.getValue(ReviewMainClass.class);
                        ReviewDisplayClass thisData= new ReviewDisplayClass(newReview.getId(),newReview.getReviewerId(),newReview.getReview(),newReview.getStoreId(),newReview.getRating());
                        itm.add(thisData);
                        // ExampleItem itm =new ExampleItem(newData.getItemName(),newData.getItemId(),newData.getMarketId(),newData.getCreatorId());
                        //String itmId =newData.getItemName();
                        //itemList.add(itm);
                    }
                    firebaseCallback.onCallback(itm);
                }
                else{
                    firebaseCallback.onCallback(itm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(id.isEmpty()){
            Query query = firebaseDatabase.getReference(reference).child(id)
                    .orderByChild(OrderBy);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else{
            Query query = firebaseDatabase.getReference(reference).child(id)
                    .orderByChild(OrderBy)
                    .equalTo(id);
            query.addListenerForSingleValueEvent(valueEventListener);
        }

    }
    private interface FirebaseCallback{
        void onCallback (ArrayList<ReviewDisplayClass> list);
    }
}
