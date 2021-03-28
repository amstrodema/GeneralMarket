package comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;

/**
 * Created by ELECTRON on 10/16/2019.
 */

public class DataStock {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    //this methods access the required query resources in a listener and runs the query from getData
    //used by queries from different classes
    public void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                ArrayList<NewStockDisplayModel> itm = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NewStockMainModel newPrice= snapshot.getValue(NewStockMainModel.class);
                        //fav doesn't matter here since only the counting of stocks related to each store is intended
                        NewStockDisplayModel thisData= new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),"", newPrice.isBan());
                       itm.add(thisData);
                    }
                }
                firebaseCallback.onCallback(itm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(id.isEmpty()){
            Query query = firebaseDatabase.getReference(reference)
                    .orderByChild(OrderBy);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else{
            Query query = firebaseDatabase.getReference(reference)
                    .orderByChild(OrderBy)
                    .equalTo(id);
            query.addListenerForSingleValueEvent(valueEventListener);
        }

    }
    public interface FirebaseCallback{
        void onCallback (ArrayList<NewStockDisplayModel> list);
    }
}
