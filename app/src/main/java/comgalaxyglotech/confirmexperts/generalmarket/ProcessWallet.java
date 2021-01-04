package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ELECTRON on 02/01/2020.
 */

public class ProcessWallet {
    ModelClass modelClass = new ModelClass();
    private StoreMainModel newStore;
    private FarmMainModel newFarm;
    private Uri imagePath;
    private Activity activity;
    private Context context;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public void payStoreFee(final Model_Transaction model_transaction, final Context context, final ProgressDialog progressDialog, StoreMainModel newStore, Uri imagePath, Activity activity, final boolean isStore){
        this.newStore = newStore;
        this.imagePath = imagePath;
        this.activity = activity;
        this.context = context;
        this.progressDialog = progressDialog;
        //set to directly surcharge user without cancellation or refund. This transaction will only be saved after
        //payment is concluded
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                progressDialog.setMessage("Fetching Wallet Details...");
                progressDialog.show();
                if(wallet_Model == null){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Wallet Not Found",Toast.LENGTH_SHORT).show();
                }
                else
                    instantDeductFrom_Wallet(wallet_Model.get(0),model_transaction, isStore);

            }
        },"Wallet","userId",modelClass.getCurrentUserId());
    }
    public void payFarmFee(final Model_Transaction model_transaction, final Context context, final ProgressDialog progressDialog, FarmMainModel newFarm, Uri imagePath, Activity activity, final boolean isStore){
        this.newFarm = newFarm;
        this.imagePath = imagePath;
        this.activity = activity;
        this.context = context;
        this.progressDialog = progressDialog;
        //set to directly surcharge user without cancellation or refund. This transaction will only be saved after
        //payment is concluded
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                progressDialog.setMessage("Fetching Wallet Details...");
                progressDialog.show();
                if(wallet_Model == null){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Wallet Not Found",Toast.LENGTH_SHORT).show();
                }
                else
                    instantDeductFrom_Wallet(wallet_Model.get(0),model_transaction, isStore);

            }
        },"Wallet","userId",modelClass.getCurrentUserId());
    }

    //chck if user has a wallet and sufficient amount
    public  void validatePersonalWallet(final String type,final double cashAdd, final ProgressDialog progressDialog, final Context context, final Activity activity, final String walletOwnerId, final String walletRecipientId ){
        progressDialog.setMessage("Validating Your Wallet...");
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null) {
                    //deduct from senders wallet and send process over to addto..
                    Model_Wallet wallet = wallet_Model.get(0);
                    wallet.setMainWallet(wallet.getMainWallet()-cashAdd);
                    if(wallet.getMainWallet() >= 0){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
                        databaseReference.child(wallet.getId()).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(type.equals("personal")){
                                    progressDialog.setMessage("Finishing Up...");
                                    addTo_Wallet(cashAdd, progressDialog,context,activity, walletRecipientId);
                                }
                                else{
                                    progressDialog.setMessage("Finishing Up...");
                                    addTo_Store_Wallet_Acct(walletRecipientId,cashAdd,progressDialog,context,activity);
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(context," Insufficient Funds", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
                else{
                    //since a null is returned, we can now check if the sender is a store
                   validateStoreWallet(type,cashAdd,progressDialog,context,activity,walletOwnerId,walletRecipientId);
                    progressDialog.setMessage("Please Wait...");
                 /*   Toast.makeText(context," Transfer failed! Sender's Wallet Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();*/
                }

            }
        },"Wallet","userId",walletOwnerId);
    }
    public  void validateStoreWallet(final String type, final double cashAdd, final ProgressDialog progressDialog, final Context context, final Activity activity, final String walletOwnerId, final String walletRecipientId ){

        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null) {
                    Model_Wallet wallet = wallet_Model.get(0);
                    wallet.setMainWallet(wallet.getMainWallet()-cashAdd);
                    if(wallet.getMainWallet() >= 0){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreWallet");
                        databaseReference.child(wallet.getId()).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(type.equals("personal")){
                                    progressDialog.setMessage("Finishing Up...");
                                    addTo_Wallet(cashAdd, progressDialog,context,activity, walletRecipientId);
                                }
                                else{
                                    progressDialog.setMessage("Finishing Up...");
                                    addTo_Store_Wallet_Acct(walletRecipientId,cashAdd,progressDialog,context,activity);
                                }
                            }
                        });
                    }
                  else {
                        Toast.makeText(context,"Insufficient Funds", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(context," Transfer failed! Sender's Wallet Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        },"StoreWallet","userId",walletOwnerId);
    }
    //add funds to personal account
    public void addTo_Wallet(final double cashAdd, final ProgressDialog progressDialog, final Context context, final Activity activity, final String walletOwnerId ) {
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null) {
                    Model_Wallet wallet = wallet_Model.get(0);
                   wallet.setMainWallet(wallet.getMainWallet()+cashAdd);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
                    databaseReference.child(wallet.getId()).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(context,cashAdd+" Top Up Successful!", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                    });
                }
                else{
                    Toast.makeText(context,"Wallet Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        },"Wallet","userId",walletOwnerId);
    }

    public void addTo_Store_Wallet_Acct(String storeId, final double amount, final ProgressDialog progressDialog, final Context context, final Activity activity){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null){
                    Model_Wallet wallet = wallet_Model.get(0);
                    wallet.setMainWallet(wallet.getMainWallet()+(amount));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreWallet");
                    databaseReference.child(wallet.getId()).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context,"Payment Received Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            activity.finish();
                        }
                    });
                }
                else{
                    Toast.makeText(context,"Wallet Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        },"StoreWallet","userId",storeId);

    }

    public void addTo_Store_Wallet(String storeId, final Double cost, final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null){
                    Model_Wallet wallet = wallet_Model.get(0);
                    wallet.setMainWallet(wallet.getMainWallet()+(cost-1));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreWallet");
                    databaseReference.child(wallet.getId()).setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context,"Payment Received Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(context,"Wallet Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        },"StoreWallet","userId",storeId);

    }


    public void deductFrom_Wallet(Model_Wallet model_wallet, final ProgressDialog progressDialog, final Context context, final Model_Transaction model_transaction){
        //deduct from main first, bonus and den referral
        double cost = model_transaction.getItemCost();
        double sourceFund =0;
        boolean canBuy = false;
        boolean mainAcct = false;
        if(model_wallet.getMainWallet()>0){
            mainAcct = true;
        }
        else{
            Toast.makeText(context, "Fund Main Account", Toast.LENGTH_SHORT).show();
        }
        //deduct from main
        double bal = model_wallet.getMainWallet() - cost;
        if(bal < 0){
            model_transaction.setMainWalet(model_wallet.getMainWallet());
            model_wallet.setMainWallet(0.0);
        }
        else{
            model_transaction.setMainWalet(cost);
            model_wallet.setMainWallet(bal);
            canBuy = true;
        }
        //if cant buy, deduct remains from bonus
        if(!canBuy){
            sourceFund=bal*-1;
            bal = model_wallet.getBonusWallet() + bal;
            if(bal < 0){
                model_transaction.setBonusWallet(model_wallet.getBonusWallet());
                model_wallet.setBonusWallet(0.0);
            }
            else{
                model_transaction.setBonusWallet(sourceFund);
                model_wallet.setBonusWallet(bal);
                canBuy = true;
            }
        }
        //if cnt still buy, deduct from referral
        if(!canBuy){
            sourceFund=bal*-1;
            bal = model_wallet.getReferralWallet() + bal;
            if(bal < 0){
                model_transaction.setReferalWallet(model_wallet.getReferralWallet());
                model_wallet.setReferralWallet(0.0);
            }
            else{
                model_transaction.setReferalWallet(sourceFund);
                model_wallet.setReferralWallet(bal);
                canBuy = true;
            }
        }
        if(canBuy && mainAcct){
            //funds move to upfront account and transaction initiated. Waiting for confirmation from the seller
            //user may decide to cancel transaction, if this happens after initiation, roll transaction back
            //using refundForCancelledTrans(). As funds are restored as set up.
            model_wallet.setUpfrontWallet(model_wallet.getUpfrontWallet()+cost);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
            databaseReference.child(model_wallet.getId()).setValue(model_wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ProcessTransaction processTransaction = new ProcessTransaction();
                    processTransaction.addNewTransaction(model_transaction,progressDialog,context);
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(context,"Insufficient Funds",Toast.LENGTH_SHORT).show();
        }

    }
    public void verifyStoreWalletPresent(final String storeId, final String transId, final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null){
                    progressDialog.setMessage("Validating Transaction...");
                    ProcessTransaction processTransaction = new ProcessTransaction();
                    processTransaction.isBarred(transId,progressDialog,context);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Wallet Not Found!", Toast.LENGTH_SHORT).show();
                }

            }
        },"StoreWallet","userId",storeId);
    }
    public void deductFrom_Upfront(final Model_Transaction transaction, final ProgressDialog progressDialog, final Context context){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null){
                    Model_Wallet buyerWallet = wallet_Model.get(0);
                    //item cost is deducted from payer upfront account upon verified by seller.
                    buyerWallet.setUpfrontWallet(buyerWallet.getUpfrontWallet()-transaction.getItemCost());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
                    databaseReference.child(buyerWallet.getId()).setValue(buyerWallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //add cost to store wallet
                            progressDialog.setMessage("Receiving Payment...");
                            addTo_Store_Wallet(transaction.getStoreId(),transaction.getItemCost(), progressDialog, context);
                        }
                    });
                }

            }
        },"Wallet","userId",transaction.getPayerId());
    }
    public void refundForCancelledTrans(final Model_Transaction transaction, final ProgressDialog progressDialog, final Context context, final String reference){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model != null){
                    Model_Wallet buyerWallet = wallet_Model.get(0);
                    try{
                        buyerWallet.setMainWallet(buyerWallet.getMainWallet() + transaction.getMainWalet());
                    }
                    catch (Exception e){

                    }
                    try{
                        buyerWallet.setBonusWallet(buyerWallet.getBonusWallet() + transaction.getBonusWallet());
                    }
                    catch (Exception e){

                    }
                    try{
                        buyerWallet.setReferralWallet(buyerWallet.getReferralWallet() + transaction.getReferalWallet());
                    }
                    catch (Exception e){

                    }
                    try{
                        buyerWallet.setUpfrontWallet(buyerWallet.getUpfrontWallet() - transaction.getItemCost());
                    }
                    catch (Exception e){

                    }
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(reference);
                    databaseReference.child(buyerWallet.getId()).setValue(buyerWallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context,"Transaction cancelled successfully!",Toast.LENGTH_SHORT).show();
                            //necessary for update after exclusion
                            Trade.pendinTransactions.remove(Trade.removeFromPosition);
                            Trade.mAdapterStaticCopy.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(context,"Wallet Not Found - Error 9002",Toast.LENGTH_SHORT).show();
                }

            }
        },reference,"userId",transaction.getPayerId());
    }

    //instant money deduction for store rent. This means we r not going tro upfront as usual
    public void instantDeductFrom_Wallet(Model_Wallet model_wallet, final Model_Transaction model_transaction, final boolean isStore){
        //deduct from main first, bonus and den referral
        final double cost = model_transaction.getItemCost();
        double sourceFund =0;
        boolean canBuy = false;
        boolean mainAcct = false;
        //deduct from main
        double bal = model_wallet.getMainWallet() - cost;
        if(model_wallet.getMainWallet()>0){
            mainAcct = true;
        }
        else{
            Toast.makeText(context, "Fund Main Account", Toast.LENGTH_SHORT).show();
        }
        if(bal < 0){
            model_transaction.setMainWalet(model_wallet.getMainWallet());
            model_wallet.setMainWallet(0.0);
        }
        else{
            model_transaction.setMainWalet(cost);
            model_wallet.setMainWallet(bal);
            canBuy = true;
        }
        //if cant buy, deduct remains from bonus
        if(!canBuy){
            sourceFund=bal*-1;
            bal = model_wallet.getBonusWallet() + bal;
            if(bal < 0){
                model_transaction.setBonusWallet(model_wallet.getBonusWallet());
                model_wallet.setBonusWallet(0.0);
            }
            else{
                model_transaction.setBonusWallet(sourceFund);
                model_wallet.setBonusWallet(bal);
                canBuy = true;
            }
        }
        //if cnt still buy, deduct from referral
        if(!canBuy){
            sourceFund=bal*-1;
            bal = model_wallet.getReferralWallet() + bal;
            if(bal < 0){
                model_transaction.setReferalWallet(model_wallet.getReferralWallet());
                model_wallet.setReferralWallet(0.0);
            }
            else{
                model_transaction.setReferalWallet(sourceFund);
                model_wallet.setReferralWallet(bal);
                canBuy = true;
            }
        }
        if(canBuy && mainAcct){
            //this fee is not refundable and not  surcharged from the upfront acct
            //model_wallet.setUpfrontWallet(model_wallet.getUpfrontWallet()+cost);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
            databaseReference.child(model_wallet.getId()).setValue(model_wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //set the transaction up as a confirmed transaction
                    progressDialog.setMessage("Almost Done...");
                    progressDialog.show();
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
                    final String id = databaseReference.push().getKey();
                    final String transId = id.substring(0,11);
                    model_transaction.setId(id);
                    databaseReference.child(id).setValue(model_transaction)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.setMessage("Finishing Up...");
                                    progressDialog.show();
                                    NotificationAdd newNotify = new NotificationAdd();
                                    newNotify.notice("GM",modelClass.getCurrentUserId(),"Setup Fee Payment","#" +cost+" was deducted from your wallet with Transaction ID -"+transId, context,10);
                                   //set up store here
                                    progressDialog.dismiss();
                                    if(isStore){
                                        createStore(transId);
                                    }
                                    else
                                    {
                                        createFarm(transId);
                                    }

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
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(context,"Insufficient Funds",Toast.LENGTH_SHORT).show();
        }

    }
    //set store up after payment
    private void createStore(final String transId){
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllStores");
        final String storeId= modelClass.getNewId();
        newStore.setId(storeId);
                                    databaseReference.child(newStore.getId()).setValue(newStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                            StorageReference storageReference = firebaseStorage.getReference();
                                            final StorageReference imageReference = storageReference.child("Store").child(storeId);
                                            if(imagePath == null){
                                                NotificationAdd newNotify = new NotificationAdd();
                                                newNotify.notice("GM",modelClass.getCurrentUserId(),"New Store Alert","Your store has been created successfully", context,50);
                                                Toast.makeText(context,"Store Created Successfully!", Toast.LENGTH_SHORT).show();
                                                activity.finish();

                                            }
                                            else{
                                                imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        NotificationAdd newNotify = new NotificationAdd();
                                                        newNotify.notice("GM",modelClass.getCurrentUserId(),"New Store Alert","Your store has been created successfully", context,50);
                                                        Toast.makeText(context,"Store Created Successfully!", Toast.LENGTH_SHORT).show();
                                                        activity.finish();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context,"Store Image Not Saved",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }

                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //send a message to admin stating this problem, date and transId
                                            //also notify the user of this error code X2345-STO-117
                                            NotificationAdd newNotify = new NotificationAdd();
                                            newNotify.notice("GM",modelClass.getCurrentUserId(),"Error!","Something went wrong. Please send us your complaint, this code: X2345-STO-117-"+ transId+" and your e-mail address if error is not resolved within 24hrs.", context,150);
                                            Toast.makeText(context,"Oops! Store not created", Toast.LENGTH_SHORT).show();
                                        }
                                    });
    }

    //set farm up after payment
    private  void createFarm(final String transId){
        final String farmId= modelClass.getNewId();
        newFarm.setId(farmId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllFarms");
        databaseReference.child(newFarm.getId()).setValue(newFarm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                final StorageReference imageReference = storageReference.child("Farm").child(farmId);
                if(imagePath == null){

                    NotificationAdd newNotify = new NotificationAdd();
                    newNotify.notice("GM",modelClass.getCurrentUserId(),"New Farm Alert!","Your farm has been created successfully", context,50);
                    Toast.makeText(context,"Farm Created Successfully!", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
                else{
                    imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            NotificationAdd newNotify = new NotificationAdd();
                            newNotify.notice("GM",modelClass.getCurrentUserId(),"New Farm Alert!","Your farm has been created successfully", context,50);
                            Toast.makeText(context,"Farm Created Successfully!", Toast.LENGTH_SHORT).show();
                            activity.finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //also notify the user of this error code X2345-FAR-117
                            NotificationAdd newNotify = new NotificationAdd();
                            newNotify.notice("GM",modelClass.getCurrentUserId(),"Error!","Something went wrong. Please send us your complaint, this code: X2345-FAR-117-"+ transId+" and your e-mail address if error is not resolved within 24hrs.", context,150);
                            Toast.makeText(context,"Oops! Farm not created ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                /*Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main7Activity");
                intent.putExtra("farmId",farmId);
                startActivity(intent);
progressDialog.dismiss();*/
            }
        });
    }
    //sets the parameter for the query
    public void getDeductData(final String userId, final ProgressDialog progressDialog, final Context context, final Model_Transaction model_transaction){
        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                   if(wallet_Model == null){
                       progressDialog.dismiss();
                       Toast.makeText(context,"Wallet Not Found",Toast.LENGTH_SHORT).show();
                   }
                   else {
                       model_transaction.setPayerId(userId);
                       deductFrom_Wallet(wallet_Model.get(0), progressDialog, context, model_transaction);
                   }
            }
        },"Wallet","userId",userId);
    }



    //recursive function to enable iteration after each successful transaction log
    public void getInvoiceDeductData(final String userId, final ProgressDialog progressDialog, final Context context, final ArrayList<Model_Transaction> model_transaction_list){
        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model == null){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Wallet Not Found",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!model_transaction_list.isEmpty()){
                        final Model_Transaction model_transaction= model_transaction_list.get(0);
                        Model_Wallet model_wallet = wallet_Model.get(0);
                        //deduct from main first, bonus and den referral
                        double cost = model_transaction.getItemCost();
                        double sourceFund =0;
                        boolean canBuy = false;
                        boolean mainAcct = false;
                        if(model_wallet.getMainWallet()>0){
                            mainAcct = true;
                        }
                        else{
                            Toast.makeText(context, "Fund Main Account", Toast.LENGTH_SHORT).show();
                        }
                        //deduct from main
                        double bal = model_wallet.getMainWallet() - cost;
                        if(bal < 0){
                            model_transaction.setMainWalet(model_wallet.getMainWallet());
                            model_wallet.setMainWallet(0.0);
                        }
                        else{
                            model_transaction.setMainWalet(cost);
                            model_wallet.setMainWallet(bal);
                            canBuy = true;
                        }
                        //if cant buy, deduct remains from bonus
                        if(!canBuy){
                            sourceFund=bal*-1;
                            bal = model_wallet.getBonusWallet() + bal;
                            if(bal < 0){
                                model_transaction.setBonusWallet(model_wallet.getBonusWallet());
                                model_wallet.setBonusWallet(0.0);
                            }
                            else{
                                model_transaction.setBonusWallet(sourceFund);
                                model_wallet.setBonusWallet(bal);
                                canBuy = true;
                            }
                        }
                        //if cnt still buy, deduct from referral
                        if(!canBuy){
                            sourceFund=bal*-1;
                            bal = model_wallet.getReferralWallet() + bal;
                            if(bal < 0){
                                model_transaction.setReferalWallet(model_wallet.getReferralWallet());
                                model_wallet.setReferralWallet(0.0);
                            }
                            else{
                                model_transaction.setReferalWallet(sourceFund);
                                model_wallet.setReferralWallet(bal);
                                canBuy = true;
                            }
                        }
                        if(canBuy && mainAcct){
                            //funds move to upfront account and transaction initiated. Waiting for confirmation from the seller
                            //user may decide to cancel transaction, if this happens after initiation, roll transaction back
                            //using refundForCancelledTrans(). As funds are restored as set up.
                            model_wallet.setUpfrontWallet(model_wallet.getUpfrontWallet()+cost);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
                            databaseReference.child(model_wallet.getId()).setValue(model_wallet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //process each transaction and recall the main method in recursion until done

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
                                                    newNotify.notice("GM",model_transaction.getStoreOwnerId(),"Purchase Alert!","Purchase Transaction worth "+model_transaction.getItemCost()+" was initiated", context,5);
                                                    newNotify2.notice("GM",model_transaction.getBuyerId(),"Transaction Initiated","Your purchase transaction worth "+model_transaction.getItemCost()+" was initiated. Kindly await confirmation seller.", context,7);
                                                    model_transaction_list.remove(0);
                                                    getInvoiceDeductData(userId,progressDialog,context,model_transaction_list);
                                                    //iterate until the items in the list is all serviced
                                                    //  progressDialog.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context,"Transaction Failed",Toast.LENGTH_SHORT).show();
                                                    //progressDialog.dismiss();
                                                    //refund wallet accordin to variations in deduction
                                                }
                                            });


                                }
                            });
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(context,"Insufficient Funds",Toast.LENGTH_SHORT).show();
                        }


                    }
                    else{
                        Toast.makeText(context, "Invoice Paid Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        },"Wallet","userId",userId);
    }





//recursive function for getting data from store wallet
public void getInvoiceDeductStoreData(final String userId, final ProgressDialog progressDialog, final Context context, final ArrayList<Model_Transaction> model_transaction_list){
    //action is d function to be called upon callback
    readData(new FirebaseCallback() {
        @Override
        public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
            if(wallet_Model == null){
                progressDialog.dismiss();
                Toast.makeText(context,"Wallet Not Found",Toast.LENGTH_SHORT).show();
            }
            else{
                if(!model_transaction_list.isEmpty()){
                    final Model_Transaction model_transaction= model_transaction_list.get(0);
                    Model_Wallet model_wallet = wallet_Model.get(0);
                    //deduct from main first, bonus and den referral
                    double cost = model_transaction.getItemCost();
                    double sourceFund =0;
                    boolean canBuy = false;
                    boolean mainAcct = false;
                    if(model_wallet.getMainWallet()>0){
                        mainAcct = true;
                    }
                    else{
                        Toast.makeText(context, "Fund Main Account", Toast.LENGTH_SHORT).show();
                    }
                    //deduct from main
                    double bal = model_wallet.getMainWallet() - cost;
                    if(bal < 0){
                        model_transaction.setMainWalet(model_wallet.getMainWallet());
                        model_wallet.setMainWallet(0.0);
                    }
                    else{
                        model_transaction.setMainWalet(cost);
                        model_wallet.setMainWallet(bal);
                        canBuy = true;
                    }
                    //if cant buy, deduct remains from bonus
                    if(!canBuy){
                        sourceFund=bal*-1;
                        bal = model_wallet.getBonusWallet() + bal;
                        if(bal < 0){
                            model_transaction.setBonusWallet(model_wallet.getBonusWallet());
                            model_wallet.setBonusWallet(0.0);
                        }
                        else{
                            model_transaction.setBonusWallet(sourceFund);
                            model_wallet.setBonusWallet(bal);
                            canBuy = true;
                        }
                    }
                    //if cnt still buy, deduct from referral
                    if(!canBuy){
                        sourceFund=bal*-1;
                        bal = model_wallet.getReferralWallet() + bal;
                        if(bal < 0){
                            model_transaction.setReferalWallet(model_wallet.getReferralWallet());
                            model_wallet.setReferralWallet(0.0);
                        }
                        else{
                            model_transaction.setReferalWallet(sourceFund);
                            model_wallet.setReferralWallet(bal);
                            canBuy = true;
                        }
                    }
                    if(canBuy && mainAcct){
                        //funds move to upfront account and transaction initiated. Waiting for confirmation from the seller
                        //user may decide to cancel transaction, if this happens after initiation, roll transaction back
                        //using refundForCancelledTrans(). As funds are restored as set up.
                        model_wallet.setUpfrontWallet(model_wallet.getUpfrontWallet()+cost);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreWallet");
                        databaseReference.child(model_wallet.getId()).setValue(model_wallet).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //process each transaction and recall the main method in recursion until done

                                Date date = new Date();
                                model_transaction.setDate(date.toString());
                                model_transaction.setIsBarred("False");
                                model_transaction.setIsConfirmed("False");
                                model_transaction.setIsNew("True");
                                model_transaction.setPayerWalletType("Store");
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
                                                newNotify.notice("GM",model_transaction.getStoreOwnerId(),"Purchase Alert!","Purchase Transaction worth "+model_transaction.getItemCost()+" was initiated", context,5);
                                                newNotify2.notice("GM",model_transaction.getBuyerId(),"Transaction Initiated","Your purchase transaction worth "+model_transaction.getItemCost()+" was initiated. Kindly await confirmation seller.", context,7);
                                               model_transaction_list.remove(0);
                                                getInvoiceDeductStoreData(userId,progressDialog,context,model_transaction_list);
                                                //iterate until the items in the list is all serviced
                                                //  progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context,"Transaction Failed",Toast.LENGTH_SHORT).show();
                                                //progressDialog.dismiss();
                                                //refund wallet accordin to variations in deduction
                                            }
                                        });


                            }
                        });
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(context,"Insufficient Funds",Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(context, "Invoice Paid Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

        }
    },"StoreWallet","userId",userId);
}





    //this methods access the required query resources in a listener and runs the query from getData
    public void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Model_Wallet> wallet_Model = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Model_Wallet wallet_Mode= snapshot.getValue(Model_Wallet.class);
                        Model_Wallet new_wallet = new  Model_Wallet(wallet_Mode.getId(),wallet_Mode.getUserId(),wallet_Mode.getMainWallet(),wallet_Mode.getBonusWallet(),wallet_Mode.getReferralWallet(),wallet_Mode.getUpfrontWallet());
                        wallet_Model.add(new_wallet);

                    }
                    firebaseCallback.onCallback(wallet_Model);
                }
                else {
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
    public interface FirebaseCallback{
        void onCallback (ArrayList<Model_Wallet> wallet_Model);
    }
}
