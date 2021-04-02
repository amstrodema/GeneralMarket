package comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.Model_Transaction;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Process.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;

/**
 * Created by ELECTRON on 09/23/2019.
 */

public class DataModel {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static Map<String, String> displayData = new HashMap<String, String>();
    public static Map<String, FarmMainModel> displayEntireFarmData = new HashMap<String, FarmMainModel>();
    public static Map<String, StoreMainModel> displayEntireData = new HashMap<String, StoreMainModel>();
    private ProgressDialog ProgressDialog;
    private Context Context;
    private Activity Activity;
    private Intent Intent; DataClass dataClass = new DataClass();
    private boolean doubl = false;
    public void getSmartData(final ProgressDialog progressDialog, final Context context, final Activity activity, final Intent intent){
        ProgressDialog = progressDialog;
        Context =context;
        Activity =activity;
        Intent =intent;
        getData();
    }
    public void getSmarterData(final ProgressDialog progressDialog,final Context context, final Intent intent){
        ProgressDialog = progressDialog;
        Context =context;
        Intent =intent;
        getData();
    }
    public void getDoubleData(final ProgressDialog progressDialog,final Context context, final Intent intent){
       doubl= true;
        ProgressDialog = progressDialog;
        Context =context;
        Intent =intent;
        getData();
    }
    public void getDoubleFarmData(final ProgressDialog progressDialog,final Context context, final Intent intent){
        doubl= true;
        ProgressDialog = progressDialog;
        Context =context;
        Intent =intent;
        getFarmData();
    }
    //sets the parameter for the query
    public void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(Map<String, String> list) {
                displayData=list;
                if(doubl){
                    dataClass.getSmarterData(ProgressDialog,Context,Intent);
                }
                else {
                    if (ProgressDialog != null) {
                        if (Activity != null) {
                            Activity.finish();
                        }
                        ProgressDialog.dismiss();
                        Context.startActivity(Intent);
                    }
                }
            }
        },"AllStores","storeName","");
    }
    public void getFarmData(){
        readFarmData(new FirebaseFarmCallback() {
            @Override
            public void onCallback(Map<String, FarmMainModel> list) {
                displayEntireFarmData=list;
                if(doubl){
                    dataClass.getSmarterData(ProgressDialog,Context,Intent);
                }
                else {
                    if (ProgressDialog != null) {
                        if (Activity != null) {
                            Activity.finish();
                        }
                        ProgressDialog.dismiss();
                        Context.startActivity(Intent);
                    }
                }
            }
        },"AllFarms","farmName","");
    }

    //this methods access the required query resources in a listener and runs the query from getData
    //gets records of stores
    public void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                Map<String, String> itm = new HashMap<String, String>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        StoreMainModel newData= snapshot.getValue(StoreMainModel.class);
                        displayEntireData.put(newData.getId(), newData);
                        //ExampleItem thisData= new ExampleItem(,,newData.getMarketId(),newData.getCreatorId());
                        itm.put(newData.getId(),newData.getStoreName()) ;
                        // ExampleItem itm =new ExampleItem(newData.getItemName(),newData.getItemId(),newData.getMarketId(),newData.getCreatorId());
                        //String itmId =newData.getItemName();
                        //itemList.add(itm);
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
    //get records of farms
    public void readFarmData(final FirebaseFarmCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, FarmMainModel> itm = new HashMap<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        FarmMainModel newData= snapshot.getValue(FarmMainModel.class);
                        itm.put(newData.getId(),newData) ;
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
        void onCallback (Map<String, String> list);
    }
    public interface FirebaseFarmCallback{
        void onCallback ( Map<String, FarmMainModel> list);
    }


    public void deleteData(String reference, String id, final ProgressDialog progressDialog, final Context context, final String toastMsg){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(reference).child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context,toastMsg,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void deleteTransData(String reference, String id, final ProgressDialog progressDialog, final Context context, final Model_Transaction transaction){
            String walletRef ;
            if(transaction.getPayerWalletType() == null){
                walletRef = "Wallet";
            }
            else{
                walletRef = "StoreWallet";
            }
            final String ref = walletRef;
        final Model_Transaction trans = new Model_Transaction();
            trans.setReferalWallet(transaction.getReferalWallet());
            trans.setBonusWallet(transaction.getBonusWallet());
            trans.setMainWalet(transaction.getMainWalet());
        trans.setBuyerId(transaction.getBuyerId());
        trans.setPayerId(transaction.getPayerId());
            trans.setItemCost(transaction.getItemCost());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(reference).child(id);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ProcessWallet processWallet = new ProcessWallet();
                progressDialog.setMessage("Rolling-back Upfront Funds...");
                processWallet.refundForCancelledTrans(trans,progressDialog,context,ref);
            }
        });
    }
}
