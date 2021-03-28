package comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartInvoiceModel;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.FragStockStore;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.Model_Transaction;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.Trade;

/**
 * Created by ELECTRON on 08/04/2020.
 */

public class CartInvoiceTransaction {
    private ArrayList<CartModel> thisInvoice;
    private ProgressDialog progressDialog;
    private Context context;
    private Activity activity;
    private int count = 0;

    private ModelClass modelClass = new ModelClass();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    public CartInvoiceTransaction() {
    }

    public CartInvoiceTransaction(ArrayList<CartModel> thisInvoice, ProgressDialog progressDialog, Context context, Activity activity) {
        this.thisInvoice = thisInvoice;
        this.progressDialog = progressDialog;
        this.context = context;
        this.activity = activity;
    }
    public void footInvoice(){
        //receive invoice directly and pay
        if(thisInvoice != null) {
            //sets up transaction for each cart content
            if(modelClass.userLoggedIn()){
                progressDialog.setMessage("Processing Invoice Item...");
               // NewStockMainModel item = thisInvoice.getStockToBuy();
                ArrayList<Model_Transaction> transactions = new ArrayList<>();
                for (CartModel model: thisInvoice) {
                    final Model_Transaction modelTransaction= new Model_Transaction();
                    NewStockMainModel item = model.getStockToBuy();
                    try{
                        modelTransaction.setItmQty((double)model.getItemQty());
                        modelTransaction.setPayerId(modelClass.getCurrentUserId());
                        modelTransaction.setBuyerId(modelClass.getCurrentUserId());
                        modelTransaction.setItemId(item.getId());
                        double cost = modelTransaction.getItmQty()* Float.parseFloat(item.getItemCost());
                        modelTransaction.setItemCost(cost);
                        modelTransaction.setStoreId(item.getStoreId());
                        modelTransaction.setStoreOwnerId(item.getCreatorId());
                        transactions.add(modelTransaction);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(context,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                ProcessWallet processWallet = new ProcessWallet();
                processWallet.getInvoiceDeductData(modelClass.getCurrentUserId(),progressDialog,context,transactions);

            }
            else{
                Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
            FragStockStore.storeCart.clear();
        }
        else{
            Toast.makeText(context, "Invoice Not Found!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
    public void payInvoice(final String invoiceId, final String payerId, final ProgressDialog progressDialog, final Context context, final Activity activity){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<CartInvoiceModel> cartInvoice) {

                if(cartInvoice != null){
                    final CartInvoiceModel thisInvoice = cartInvoice.get(0);
                    final ModelClass modelClass = new ModelClass();
                   //check if invoice has expired
                    if(modelClass.isInvoiceExpired(thisInvoice.getDateTime())) {
                        progressDialog.dismiss();
                        //pay invoice
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        alertDialog.setTitle("Purchase Alert!!!");
                        alertDialog.setMessage("You are about to pay for this Invoice\nCost: ₦"+thisInvoice.getCost());
                        alertDialog.setIcon(R.drawable.transaction_pics);
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Analyzing Cart Invoice");
                                progressDialog.show();
                                getCart(thisInvoice.getBuyerId(),payerId,thisInvoice.getId(),progressDialog,context,activity);
                            }
                        });
                        alertDialog.show();
                    }
                    else{
                        Toast.makeText(context, "Invoice has expired!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(context, "Invoice Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Invoice","id",invoiceId);
    }

    public void getCart(final String buyerId, final String payerId, final String invoiceId, final ProgressDialog progressDialog, final Context context, final Activity activity){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        final ModelClass modelClass = new ModelClass();
        readCartData(new FirebaseCartCallback() {
            @Override
            public void onCallback( ArrayList<CartModel> cartModels) {
                if(cartModels != null) {
                    ArrayList<Model_Transaction> transactions = new ArrayList<>();
                    for (CartModel model: cartModels) {
                        final Model_Transaction modelTransaction= new Model_Transaction();
                        NewStockMainModel item = model.getStockToBuy();
                        try{
                            modelTransaction.setItmQty((double)model.getItemQty());
                            modelTransaction.setPayerId(payerId);
                            modelTransaction.setBuyerId(buyerId);
                            modelTransaction.setItemId(item.getId());
                            double cost = modelTransaction.getItmQty()* Float.parseFloat(item.getItemCost());
                            modelTransaction.setItemCost(cost);
                            modelTransaction.setStoreId(item.getStoreId());
                            transactions.add(modelTransaction);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(context,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    ProcessWallet processWallet = new ProcessWallet();
                    processWallet.getInvoiceDeductData(payerId,progressDialog,context,transactions);
                }
                else{
                    Toast.makeText(context, "Carts Items Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Cart","invoiceId",invoiceId);
    }


    public void payInvoiceWithStore(final String invoiceId, final String payerId, final ProgressDialog progressDialog, final Context context, final Activity activity){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<CartInvoiceModel> cartInvoice) {
                if(cartInvoice != null){
                    final CartInvoiceModel thisInvoice = cartInvoice.get(0);
                    final ModelClass modelClass = new ModelClass();
                    //check if invoice has expired
                    if(modelClass.isInvoiceExpired(thisInvoice.getDateTime())){
                        progressDialog.dismiss();
                        //pay invoice
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        alertDialog.setTitle("Purchase Alert!!!");
                        alertDialog.setMessage("You are about to pay for this Invoice\nCost: ₦"+thisInvoice.getCost());
                        alertDialog.setIcon(R.drawable.transaction_pics);
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Analyzing Cart Invoice");
                                progressDialog.show();
                                //this implies that a store is funding the the bills for the invoice, as against the personal
                                //not that the invoice belongs to the store
                                getCartForStore(thisInvoice.getBuyerId(),payerId,thisInvoice.getId(),progressDialog,context,activity);
                            }
                        });
                        alertDialog.show();
                    }
                    else{
                        Toast.makeText(context, "Invoice has expired!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(context, "Invoice Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Invoice","id",invoiceId);
    }
    public void getCartForStore(final String buyerId, final String payerId, final String invoiceId, final ProgressDialog progressDialog, final Context context, final Activity activity){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        readCartData(new FirebaseCartCallback() {
            @Override
            public void onCallback( ArrayList<CartModel> cartModels) {
                if(cartModels != null){
                    ArrayList<Model_Transaction> transactions = new ArrayList<>();
                    for (CartModel model: cartModels) {
                        final Model_Transaction modelTransaction= new Model_Transaction();
                        NewStockMainModel item = model.getStockToBuy();
                        try{
                            modelTransaction.setItmQty((double)model.getItemQty());
                            modelTransaction.setPayerId(payerId);
                            modelTransaction.setBuyerId(buyerId);
                            modelTransaction.setItemId(item.getId());
                            double cost = modelTransaction.getItmQty()* Float.parseFloat(item.getItemCost());
                            modelTransaction.setItemCost(cost);
                            modelTransaction.setStoreId(item.getStoreId());
                            modelTransaction.setStoreOwnerId(item.getCreatorId());
                            transactions.add(modelTransaction);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(context,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    ProcessWallet processWallet = new ProcessWallet();
                    processWallet.getInvoiceDeductStoreData(payerId,progressDialog,context,transactions);
                }
                else{
                    Toast.makeText(context, "Carts Items Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Cart","invoiceId",invoiceId);
    }

    public void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CartInvoiceModel> profile = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        CartInvoiceModel userProfile= snapshot.getValue(CartInvoiceModel.class);
                        //UserProfile prof = new  UserProfile(wallet_Mode.getId(),wallet_Mode.getUserId(),wallet_Mode.getMainWallet(),wallet_Mode.getBonusWallet(),wallet_Mode.getReferralWallet(),wallet_Mode.getUpfrontWallet());
                        profile.add(userProfile);

                    }
                    firebaseCallback.onCallback(profile);
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
    public interface FirebaseCallback{
        void onCallback (ArrayList<CartInvoiceModel> cartInvoice);
    }

    private void readCartData(final FirebaseCartCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CartModel> profile = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        CartModel model= snapshot.getValue(CartModel.class);
                        //UserProfile prof = new  UserProfile(wallet_Mode.getId(),wallet_Mode.getUserId(),wallet_Mode.getMainWallet(),wallet_Mode.getBonusWallet(),wallet_Mode.getReferralWallet(),wallet_Mode.getUpfrontWallet());
                        profile.add(model);

                    }
                    firebaseCallback.onCallback(profile);
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
    private interface FirebaseCartCallback{
        void onCallback (ArrayList<CartModel> cartModel);
    }

    public void deleteInvoice(final String id, final int position, final ProgressDialog progressDialog, final Context context){

        readCartData(new FirebaseCartCallback() {
            @Override
            public void onCallback( ArrayList<CartModel> cartModels) {
                if(cartModels != null){
                    deleteInvoiceCart(id,position,progressDialog,context,cartModels);
                }
                else{
                  try {
                      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice").child(id);
                      databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Toast.makeText(context, "Invoice Deleted", Toast.LENGTH_SHORT).show();
                              Trade.invoices.remove(position);
                              Trade.mAdapter_Invoice.notifyDataSetChanged();
                              progressDialog.dismiss();
                          }
                      });
                  }
                  catch (Exception ignored){

                  }
                    Toast.makeText(context, "Carts Items Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Cart","invoiceId",id);
    }
    public void deleteInvoiceCart(final String id, final int position, final ProgressDialog progressDialog, final Context context, final ArrayList<CartModel> cartModels){
       if(!cartModels.isEmpty()){
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(cartModels.get(0).getId());
           databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   cartModels.remove(0);
                   deleteInvoiceCart(id,position,progressDialog,context,cartModels);
               }
           });
       }
       else{
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice").child(id);
           databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(context, "Invoice Deleted", Toast.LENGTH_SHORT).show();
                   Trade.invoices.remove(position);
                   Trade.mAdapter_Invoice.notifyDataSetChanged();
                   progressDialog.dismiss();
               }
           });
       }

    }

}
