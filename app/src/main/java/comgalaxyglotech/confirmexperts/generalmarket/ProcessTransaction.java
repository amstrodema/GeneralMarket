package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;

/**
 * Created by ELECTRON on 02/01/2020.
 */

public class ProcessTransaction {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private Model_Transaction storeTransModel = new Model_Transaction();
    private boolean secondRound = false;
    private ModelClass modelClass = new ModelClass();

    public void payStoreFee(double fee, final Context context, final ProgressDialog progressDialog, StoreMainModel newStore, Uri imagePath, Activity activity, boolean isStore){
        Model_Transaction feeTransaction = new Model_Transaction();
        ProcessWallet processWallet = new ProcessWallet();
        feeTransaction.setItemCost(fee);
        feeTransaction.setBuyerId(modelClass.getCurrentUserId());
        feeTransaction.setIsNew("True");
        feeTransaction.setItmQty(1.0);
        feeTransaction.setItemId("Surcharged Fee");
        feeTransaction.setIsConfirmed("True");
        processWallet.payStoreFee(feeTransaction,context,progressDialog, newStore, imagePath, activity, isStore);
    }
    public void payFarmFee(double fee, final Context context, final ProgressDialog progressDialog, FarmMainModel newFarm, Uri imagePath, Activity activity, boolean isStore){
        Model_Transaction feeTransaction = new Model_Transaction();
        ProcessWallet processWallet = new ProcessWallet();
        feeTransaction.setItemCost(fee);
        feeTransaction.setBuyerId(modelClass.getCurrentUserId());
        feeTransaction.setIsNew("True");
        feeTransaction.setItmQty(1.0);
        feeTransaction.setItemId("Surcharged Fee");
        feeTransaction.setIsConfirmed("True");
        processWallet.payFarmFee(feeTransaction,context,progressDialog, newFarm, imagePath, activity, isStore);
    }

    public void isConfirmed(final String id, final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Transaction> transModel) {
                storeTransModel = transModel.get(0);
                //confirms dat transaction is not barred den sets confirmation
                //reconfirms dat transaction is not barred den sets for payment receival
                if(storeTransModel.getIsBarred().equals("True") && !secondRound){
                    Toast.makeText(context,"Transaction Cancelled Already", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!storeTransModel.getIsConfirmed().equals("True")){
                    if(secondRound){
                        //deduct from upfront
                        secondRound=false;
                        //keepModel to refund account after delete
                        progressDialog.setMessage("Cancelling Transaction...");
                        DataModel dataModel = new DataModel();
                        dataModel.deleteTransData("Transaction",id,progressDialog, context,storeTransModel);
                    }
                    else{
                        progressDialog.setMessage("Verifying...");
                        setIsBarred(storeTransModel, progressDialog, context);
                        //  Toast.makeText(context,"Validating...",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(context,"Transaction Already Processed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Transaction","id",id);
    }

    public void isBarred(String id, final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Transaction> transModel) {
                if (transModel != null)
                {
                    storeTransModel = transModel.get(0);
                //confirms dat transaction is not barred den sets confirmation
                //reconfirms dat transaction is not barred den sets for payment receival

                if (storeTransModel.getIsConfirmed().equals("True") && !secondRound) {
                    Toast.makeText(context, "Transaction Already Processed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!storeTransModel.getIsBarred().equals("True")) {
                    if (secondRound) {
                        //deduct from upfront
                        secondRound = false;
                        progressDialog.setMessage("Verifying Payment Wallet...");
                        ProcessWallet processWallet = new ProcessWallet();
                        processWallet.deductFrom_Upfront(storeTransModel, progressDialog, context);
                        // Toast.makeText(context,"Verifying Payment Wallet",Toast.LENGTH_SHORT).show();
                    } else {
                        //validation confirms and concludes the transaction before setting second round as true, and calling this same
                        //method again but the condition returns true and proceed as is the statement of the 'if' above
                        //sets transaction confirmation to true especially
                        progressDialog.setMessage("Validating...");
                        setIsConfirmed(storeTransModel, progressDialog, context);
                        //  Toast.makeText(context,"Validating...",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Transaction No Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
                else{
                    Toast.makeText(context, "Transaction No Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Transaction","id",id);
    }
    public void setIsConfirmed(final Model_Transaction model_transaction, final ProgressDialog progressDialog, final Context context){
        //confirms the transaction and makes it become irrevesible
        model_transaction.setIsConfirmed("True");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
        databaseReference.child(model_transaction.getId()).setValue(model_transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                secondRound=true;
                isBarred(model_transaction.getId(), progressDialog, context);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }
    public void setIsBarred(final Model_Transaction model_transaction, final ProgressDialog progressDialog, final Context context){
        model_transaction.setIsBarred("True");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
        databaseReference.child(model_transaction.getId()).setValue(model_transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                secondRound=true;
                isConfirmed(model_transaction.getId(), progressDialog, context);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
    public void addNewTransaction(final Model_Transaction model_transaction, final ProgressDialog progressDialog, final Context context){
        Date date = new Date();
        model_transaction.setDate(date.toString());
        model_transaction.setIsBarred("False");
        model_transaction.setIsConfirmed("False");
        model_transaction.setIsNew("True");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
        final String id = databaseReference.push().getKey();
        model_transaction.setId(id);
        databaseReference.child(id).setValue(model_transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Transaction Initiated",Toast.LENGTH_SHORT).show();
                        NotificationAdd newNotify = new NotificationAdd();
                        NotificationAdd newNotify2 = new NotificationAdd();
                        newNotify.ownerNotice("GM",model_transaction.getStoreOwnerId(),"Purchase Alert!","Confirm A Pending Transaction worth "+model_transaction.getItemCost()+" now!", context,5);
                        newNotify2.notice("GM",model_transaction.getBuyerId(),"Transaction Initiated","A purchase transaction worth "+model_transaction.getItemCost()+" was initiated. Kindly await confirmation from seller.", context,7);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Transaction Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //refund wallet accordin to variations in deduction
                    }
                });
        //  activity.finish();
        // Intent intent = new Intent(context, Main3Activity.class);
        //  intent.putExtra("marketID",MarketModel.getId());
        //progressDialog.dismiss();
        // Toast.makeText(context,"Market Edited Successfully", Toast.LENGTH_SHORT).show();
        // startActivity(intent);
    }
    public void getTransactionHistory(final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Transaction> transModels) {
              if(transModels != null){
                  progressDialog.setMessage("Clearing transaction history...");
                  deleteTransactionHistory(progressDialog,context,transModels);
              }
              else{
                  progressDialog.dismiss();
                  Toast.makeText(context, "History Not Found", Toast.LENGTH_SHORT).show();
              }
            }
        },"Transaction","isConfirmed","True");
    }
    public void deleteTransactionHistory(final ProgressDialog progressDialog, final Context context, final ArrayList<Model_Transaction> models){
        if(!models.isEmpty()){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction").child(models.get(0).getId());
            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    models.remove(0);
                    deleteTransactionHistory(progressDialog,context,models);
                }
            });
        }
        else{
            Trade.confirmedTransactions.clear();
            Trade.mAdapter_confirmed.notifyDataSetChanged();
            Toast.makeText(context, "Transaction History Cleared", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


    //sets the parameter for the query
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
               else{
                    firebaseCallback.onCallback(null);
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
        void onCallback (ArrayList<Model_Transaction> transModel);
    }
}
