package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ELECTRON on 11/06/2019.
 */

public class UserDataClass {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static Map<String, UserProfile> userData = new HashMap<>();
    //userId on entire profile
    private ReviewData reviewData = new ReviewData();
    private ReviewDataMarket reviewDataMarket = new ReviewDataMarket();
    private ReviewDataFarm reviewDataFarm = new ReviewDataFarm();
    public void getData(final int requestNo, final String StoreId, final Context context, final Activity activity, final TextView noReviewLabel){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(Map<String, UserProfile>  list) {
                userData=list;
                //recycler
                if (requestNo == 1){
                    reviewData.getData(StoreId,context,activity,noReviewLabel);
                }
                else  if (requestNo == 2){
                    reviewDataMarket.getData(StoreId,context,activity,noReviewLabel);
                }
                else  if (requestNo == 3){
                    reviewDataFarm.getData(StoreId,context,activity,noReviewLabel);
                }
            }
        },"userProfile","fname","");

    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                Map<String, UserProfile>  itm = new HashMap<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserProfile newUser= snapshot.getValue(UserProfile.class);
                        UserProfile thisData= new UserProfile(newUser.getId(),newUser.getFname(),newUser.getLname(),newUser.getEmail(),newUser.getCountry(),newUser.getPhone());
                        itm.put(newUser.getId(),newUser);
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
        void onCallback (Map<String, UserProfile>  list);
    }
}
