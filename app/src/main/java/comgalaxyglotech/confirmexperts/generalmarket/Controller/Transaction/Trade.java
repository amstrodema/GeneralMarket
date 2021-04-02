package comgalaxyglotech.confirmexperts.generalmarket.Controller.Transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Cart.Cart_Invoice_AdapterAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.CartInvoiceTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.ProcessTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.TransactionAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Account.Account;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet.Wallet;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartInvoiceDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartInvoiceModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.ModelDisplayTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.Model_Transaction;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class Trade extends AppCompatActivity {
    private SwipeRefreshLayout swiper;
    private ModelClass modelClass = new ModelClass();
    private ArrayList<NewStockMainModel> stocks = new ArrayList<>();
    private ArrayList<Model_Transaction> transactions = new ArrayList<>();
    public static ArrayList<ModelDisplayTransaction> pendinTransactions = new ArrayList<>();
    public static ArrayList<ModelDisplayTransaction> confirmedTransactions = new ArrayList<>();
    public static ArrayList<CartInvoiceDisplayModel> invoices = new ArrayList<>();
    public static int removeFromPosition;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    //do no t resolve to local variables, keep the variables for each sect together
    //pendin transaction
    private RecyclerView mRecyclerView;
    private TransactionAdapter mAdapter;
    public static TransactionAdapter mAdapterStaticCopy;
    private RecyclerView.LayoutManager mlayoutManager;
    //confirmed transaction
    private RecyclerView mRecyclerView_confirmed;
    public static TransactionAdapter mAdapter_confirmed;
    private RecyclerView.LayoutManager mlayoutManager_confirmed;

    //invoice
    private RecyclerView mRecyclerView_Invoice;
    public static Cart_Invoice_AdapterAdapter mAdapter_Invoice;
    private RecyclerView.LayoutManager mlayoutManager_Invoice;

    private ProgressDialog progressDialog;
    private Context context;
    private Activity activity = this;
    private ProgressBar history, pending,invoiceBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        swiper = findViewById(R.id.swiper);
        mRecyclerView = findViewById(R.id.trans);
        mRecyclerView_confirmed = findViewById(R.id.transHistory);
        //if user not logged in, shut transaction page

        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        basePanelListeners();

        invoices.clear();

        //important to set off the start as a clean slate
        pendinTransactions.clear();
        confirmedTransactions.clear();
        //recycler
        mlayoutManager =new LinearLayoutManager(context);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mAdapter = new TransactionAdapter(pendinTransactions);
        mRecyclerView.setAdapter(mAdapter);
        history = findViewById(R.id.history);
        pending = findViewById(R.id.pending);
        invoiceBar = findViewById(R.id.invoiceBar);
        ImageButton clearHistory = findViewById(R.id.clearHistory);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                getInvoiceData();
            }
        });
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Delete Transaction History?");
                alertDialog.setMessage("You are about to permanently delete all transaction histories. Click Ok to proceed.");
                alertDialog.setIcon(R.drawable.danger);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //confirm param and delete if safe
                        progressDialog.setMessage("Parsing Transactions...");
                        progressDialog.show();
                        ProcessTransaction processTransaction = new ProcessTransaction();
                        processTransaction.getTransactionHistory(progressDialog, context);
                    }
                });
                alertDialog.show();
            }
        });

        mAdapter.setOnItemClickListener(new TransactionAdapter.OnMetricItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCancelClick(final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Cancel Transaction?");
                alertDialog.setMessage("You are about to cancel an ongoing transaction. Click Ok to proceed.");
                alertDialog.setIcon(R.drawable.danger);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //confirm param and delete if safe
                        progressDialog.setMessage("Cancelling Transaction");
                        progressDialog.show();
                        //these are necessary for immediate update of cancellation
                        mAdapterStaticCopy = mAdapter;
                        removeFromPosition = position;
                        ProcessTransaction processTransaction = new ProcessTransaction();
                        processTransaction.isConfirmed(pendinTransactions.get(position).getId(), progressDialog, context);
                    }
                });
                alertDialog.show();
            }
        });
        mRecyclerView_confirmed.setHasFixedSize(true);
        mlayoutManager_confirmed =new LinearLayoutManager(context);
        mRecyclerView_confirmed.setLayoutManager(mlayoutManager_confirmed);
        mAdapter_confirmed = new TransactionAdapter(confirmedTransactions);
        mRecyclerView_confirmed.setAdapter(mAdapter_confirmed);


        mlayoutManager_Invoice =new LinearLayoutManager(this);
        mRecyclerView_Invoice = findViewById(R.id.invoice);
        mRecyclerView_Invoice.setHasFixedSize(true);
        mRecyclerView_Invoice.setLayoutManager(mlayoutManager_Invoice);
        mAdapter_Invoice = new Cart_Invoice_AdapterAdapter(invoices);
        mRecyclerView_Invoice.setAdapter(mAdapter_Invoice);
        mAdapter_Invoice.setOnItemClickListener(new Cart_Invoice_AdapterAdapter.OnMetricItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCancelClick(int position) {
                //copy scenario for invoice
                String id = invoices.get(position).getId().substring(1);
                modelClass.copyToClipboard(context,id,"Invoice Address Copied");
            }

            @Override
            public void onRefreshClick(int position) {
                //invoice refresh
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Refreshing Invoice..");
                progressDialog.show();
                payInvoiceRefresh(invoices.get(position).getId(),position,progressDialog,context);
            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Delete Invoice?");
                alertDialog.setMessage("You are about to delete an invoice. Click Ok to proceed.");
                alertDialog.setIcon(R.drawable.danger);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Deleting Invoice..");
                        progressDialog.show();
                        CartInvoiceTransaction invoiceTransaction = new CartInvoiceTransaction();
                        invoiceTransaction.deleteInvoice(invoices.get(position).getId(),position,progressDialog,context);
                    }
                });
                alertDialog.show();

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        getData();
        getInvoiceData();
    }

    //sets the parameter for stock query
    private void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> items) {
                //  stocks = new ArrayList<>(items);
               if (items != null)
                   getTransData();
               else
               {
                   history.setVisibility(View.GONE);
                   pending.setVisibility(View.GONE);
                   swiper.setRefreshing(false);
               }

            }
        },"Store_Stock","storeId","");
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
                    firebaseCallback.onCallback(itemz);
                }
                else
                    firebaseCallback.onCallback(null);
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
    private void getTransData(){
        readTransData(new FirebaseTransCallback() {
            @Override
            public void onCallback(ArrayList<Model_Transaction> items) {
                if(items != null){
                    transactions = new ArrayList<>(items);
                    getStoreData();
                }
                else{
                    history.setVisibility(View.GONE);
                    pending.setVisibility(View.GONE);
                    swiper.setRefreshing(false);
                }

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
                        if(newData.getBuyerId().equals(modelClass.getCurrentUserId())){
                            transactionDat.add(newData);
                        }

                    }
                    firebaseCallback.onCallback(transactionDat);
                }
                else
                    firebaseCallback.onCallback(null);
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

    //sets the parameter for store query
    private void getStoreData(){
        readStoreData(new FirebaseStoreCallback() {
            @Override
            public void onCallback(ArrayList<StoreDisplayModel> stores) {
                pendinTransactions.clear();
                confirmedTransactions.clear();
                if(transactions != null) {
                    for (Model_Transaction trans : transactions) {
                        ModelDisplayTransaction modelDisplayTransaction = new ModelDisplayTransaction(trans.getId(), itemName(trans.getItemId()), trans.getBuyerId(), trans.getIsConfirmed(), trans.getTime(), trans.getDate(), itemStore(stores, itemStoreId(trans.getItemId())), trans.getItmQty(), trans.getItemCost(), "User", trans.getPayerId());
                        if (!trans.getIsConfirmed().equals("True")) {
                            pendinTransactions.add(modelDisplayTransaction);
                        } else {
                            confirmedTransactions.add(modelDisplayTransaction);
                        }
                    }
                    //send data to adapter
                    mAdapter.notifyDataSetChanged();

                    mAdapter_confirmed.notifyDataSetChanged();
                }
                history.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);
                swiper.setRefreshing(false);
            }
        },"AllStores","storeName","");
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readStoreData(final FirebaseStoreCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StoreDisplayModel> itemz = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        StoreMainModel newStoreMode= snapshot.getValue(StoreMainModel.class);
                        StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), "",newStoreMode.isBan(),newStoreMode.getCreatorId());
                        itemz.add(newStore);
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
    private interface FirebaseStoreCallback{
        void onCallback (ArrayList<StoreDisplayModel> items);
    }


    //sets the parameter for invoice query
    private void getInvoiceData(){
        readInvoiceData(new FirebaseInvoiceCallback() {
            @Override
            public void onCallback(ArrayList<CartInvoiceDisplayModel> items) {
                invoiceBar.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        },"Invoice","buyerId",modelClass.getCurrentUserId());
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readInvoiceData(final FirebaseInvoiceCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                invoices.clear();
                ArrayList<CartInvoiceDisplayModel> transactionDat=new ArrayList<>();
                if (dataSnapshot.exists()){
                    int count =0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        count++;
                       CartInvoiceModel newData= snapshot.getValue(CartInvoiceModel.class);
                        invoices.add(new CartInvoiceDisplayModel(newData.getId(),newData.getBuyerId(),newData.getStatus(),newData.getPayerId(),newData.getDateTime(),"Invoice 00"+count,newData.getCost(),newData.getNoItems()));
                        mAdapter_Invoice.notifyDataSetChanged();
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
    private interface FirebaseInvoiceCallback{
        void onCallback (ArrayList<CartInvoiceDisplayModel> items);
    }
    //refresh the invoice transaction by resetting date. Modify to refresh the current prices and item value from seller
    private CartInvoiceTransaction invoiceTransaction = new CartInvoiceTransaction();
    public void payInvoiceRefresh(final String invoiceId, final int position, final ProgressDialog progressDialog, final Context context){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        invoiceTransaction.readData(new CartInvoiceTransaction.FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<CartInvoiceModel> cartInvoice) {
                if(cartInvoice != null){
                    final CartInvoiceModel thisInvoice = cartInvoice.get(0);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");
                    final String id =thisInvoice.getId();
                    thisInvoice.setDateTime(modelClass.getDate());
                    assert id != null;
                    databaseReference.child(id).setValue(thisInvoice).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //when done with refresh, reload invoice
                           getInvoiceData();
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Invoice Not Found!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        },"Invoice","id",invoiceId);
    }


    private String itemName(String id){
        String name ="";
        for (NewStockMainModel ss: stocks) {
            if(ss.getId().equals(id)){
                //metric carries name of ad
                name = ss.getAdvertName();
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
    private String itemStore(ArrayList<StoreDisplayModel> store, String id){
        String name ="";
        for (StoreDisplayModel ss: store) {
            if(ss.getId().equals(id)){
                name = ss.getStoreName();
                break;
            }
        }
        return name;
    }


    private void basePanelListeners(){
        Button home, account, market, store;
        home = findViewById(R.id.home_floor);
        account = findViewById(R.id.account);
        market = findViewById(R.id.notification);
        store = findViewById(R.id.setting);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trade.this, Account.class));
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trade.this, Wallet.class));
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Trade.this,Trade.class));
            }
        });
    }
}
