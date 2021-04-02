package comgalaxyglotech.confirmexperts.generalmarket.Controller.Transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Process.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.TransactionAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.ModelDisplayTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.Model_Transaction;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.User.UserProfile;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class OwnerTradeFragment extends Fragment {
    private ProgressBar progBar;
    private SwipeRefreshLayout swiper;
    private ArrayList<NewStockMainModel> stocks = new ArrayList<>();
    private ArrayList<Model_Transaction> transactions = new ArrayList<>();
    private ArrayList<ModelDisplayTransaction> pendinTransactions = new ArrayList<>();
    private ArrayList<ModelDisplayTransaction> confirmedTransactions = new ArrayList<>();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    private TransactionAdapter mAdapter;
    private TransactionAdapter mAdapter_confirmed;
    private ProgressDialog progressDialog;
    private String storeId;

    private Context context;
    private Activity activity;

    public OwnerTradeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_trade, container, false);
        context = getContext();
        activity = getActivity();
        progressDialog = new ProgressDialog(context);
        swiper = view.findViewById(R.id.swiper);
        progBar = view.findViewById(R.id.progBar);
        progressDialog.setCancelable(false);
        //pendin transaction
        RecyclerView mRecyclerView = view.findViewById(R.id.trans);
        //confirmed transaction
        RecyclerView mRecyclerView_confirmed = view.findViewById(R.id.transHistory);
        //store id from prev layout
        Intent prevIntent = getActivity().getIntent();
        storeId = prevIntent.getStringExtra("storeId");
        //recycler
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mAdapter = new TransactionAdapter(pendinTransactions);
        mRecyclerView.setAdapter(mAdapter);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mAdapter.setOnItemClickListener(new TransactionAdapter.OnMetricItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCancelClick(final int position) {
                //this button confirms rather than cancel
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Confirm Transaction?");
                alertDialog.setMessage("You are about to confirm a purchase transaction. Click Confirm to proceed.");
                alertDialog.setIcon(R.drawable.proceed);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                        //delete transaction
                    }
                });
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //confirm param and receive payment if safe
                        progressDialog.setMessage("Confirming Wallet Setup");
                        progressDialog.show();
                        ProcessWallet processWallet = new ProcessWallet();
                        processWallet.verifyStoreWalletPresent(storeId, pendinTransactions.get(position).getId(), progressDialog, context);
                    }
                });
                alertDialog.show();
            }
        });
        mRecyclerView_confirmed.setHasFixedSize(true);
        RecyclerView.LayoutManager mlayoutManager_confirmed = new LinearLayoutManager(context);
        mRecyclerView_confirmed.setLayoutManager(mlayoutManager_confirmed);
        mAdapter_confirmed = new TransactionAdapter(confirmedTransactions);
        mRecyclerView_confirmed.setAdapter(mAdapter_confirmed);
        getData();
        return view;
    }//retrieve store details first.
    //upon retrieval get transaction data
    //sets the parameter for stock query
    private void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> items) {
                //  stocks = new ArrayList<>(items);
                getTransData();
            }
        },"Store_Stock","storeId",storeId);
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<NewStockDisplayModel> itemz = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NewStockMainModel newPrice= snapshot.getValue(NewStockMainModel.class);
                        stocks.add(newPrice);
                    }
                }
                firebaseCallback.onCallback(itemz);
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
        void onCallback (ArrayList<NewStockDisplayModel> items);
    }

    //sets the parameter for trans query
    //upon retrival of transactions, get the buyers details
    private void getTransData(){
        readTransData(new FirebaseTransCallback() {
            @Override
            public void onCallback(ArrayList<Model_Transaction> items) {
                transactions = new ArrayList<>(items);
                getBuyerData();
            }
        },"Transaction","date","");
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readTransData(final FirebaseTransCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Model_Transaction> transactionDat=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Model_Transaction newData= snapshot.getValue(Model_Transaction.class);
                        if(itemStoreId(newData.getItemId()).equals(storeId)){
                            transactionDat.add(newData);
                        }
                    }
                }
                firebaseCallback.onCallback(transactionDat);
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
    private interface FirebaseTransCallback{
        void onCallback (ArrayList<Model_Transaction> items);
    }
    //sets the parameter for buyer query
    private void getBuyerData(){
        readBuyerData(new FirebaseBuyerCallback() {
            @Override
            public void onCallback(ArrayList<UserProfile> buyers) {
                pendinTransactions.clear();
                confirmedTransactions.clear();
                for (Model_Transaction trans: transactions) {
                    //buyer name is sent wit store name since adapter contract recognizes d store name by default
                    ModelDisplayTransaction modelDisplayTransaction = new ModelDisplayTransaction(trans.getId(),itemName(trans.getItemId()),trans.getBuyerId(),trans.getIsConfirmed(),trans.getTime(),trans.getDate(),itemBuyer(buyers,trans.getBuyerId()),trans.getItmQty(),trans.getItemCost(),"Store", trans.getPayerId());
                    if (!trans.getIsConfirmed().equals("True")){
                        pendinTransactions.add(modelDisplayTransaction);
                    }
                    else {
                        confirmedTransactions.add(modelDisplayTransaction);
                    }
                }
                //send data to adapter
                mAdapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
                mAdapter_confirmed.notifyDataSetChanged();
                progBar.setVisibility(View.GONE);
            }
        },"userProfile","id","");
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readBuyerData(final FirebaseBuyerCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserProfile> buyerz = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserProfile userProfile= snapshot.getValue(UserProfile.class);
                        UserProfile userProfile1 = new UserProfile(userProfile.getId(),userProfile.getFname(),userProfile.getLname(),userProfile.getEmail(),userProfile.getCountry(),userProfile.getPhone());
                        buyerz.add(userProfile1);
                    }
                }
                firebaseCallback.onCallback(buyerz);
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
    private interface FirebaseBuyerCallback{
        void onCallback (ArrayList<UserProfile> buyers);
    }

    private String itemName(String id){
        String name ="";
        for (NewStockMainModel ss: stocks) {
            if(ss.getId().equals(id)){
                name = ss.getAdvertName();
                break;
            }
        }
        return name;
    }
    private String itemBuyer(ArrayList<UserProfile> buyer, String id){
        String name ="";
        for (UserProfile ss: buyer) {
            if(ss.getId().equals(id)){
                name = ss.getLname()+" "+ss.getFname();
                break;
            }
        }
        return name;
    }

    private String itemStoreId(String id){
        String name ="";
        for (NewStockMainModel ss: stocks) {
            if(ss.getId().equals(id)){
                name = ss.getStoreId();
                break;
            }
        }
        return name;
    }

}
