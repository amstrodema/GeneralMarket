package comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.Transaction;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.Model_Transaction;

/**
 * Created by ELECTRON on 02/15/2020.
 */

public class TransactionData {

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    //Name key to IDpublic
    private ArrayList<Model_Transaction> transactionData = new ArrayList<>();
    public void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Model_Transaction> transactionData) {

            }
        },"Transaction","date","");
    }

    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Model_Transaction> transactionDat=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Model_Transaction newData= snapshot.getValue(Model_Transaction.class);
                        transactionDat.add(newData);
                    }
                    firebaseCallback.onCallback(transactionDat);
                }
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
    private interface FirebaseCallback{
        void onCallback (ArrayList<Model_Transaction> transactionData);
    }
}
