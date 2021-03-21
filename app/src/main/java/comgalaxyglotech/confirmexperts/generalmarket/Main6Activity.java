package comgalaxyglotech.confirmexperts.generalmarket;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;

public class Main6Activity extends AppCompatActivity {
    private String  commonNameId,metricLabel;
    private RecyclerView mRecyclerView;
    private NewStock_StoreAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<NewStockDisplayModel> priceList= new ArrayList<>();
    private EditText searchStringField;
    private ArrayList<NewStockMainModel>storeItemKeep=new ArrayList<>();
    public static NewStockMainModel EditReference;
    private ProgressDialog progressDialog;
    private Context context = this;
    private Activity activity = this;
    private ModelClass modelClass = new ModelClass();
    private DatabaseReference databaseReference;
    private ImageView cartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        searchBarOp();
        basePanelListeners();
        progressDialog=new ProgressDialog(Main6Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //intent value getter
        Intent previousActivity=getIntent();
        commonNameId=previousActivity.getStringExtra("commonNameId");
        cartView = findViewById(R.id.cartView);
        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.CartInvoice");
                startActivity(intent);
            }
        });
        //storeId=previousActivity.getStringExtra("storeId");
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.priceRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
    }
    private void setPriceList(){
        Query query = FirebaseDatabase.getInstance().getReference("Store_Stock")
                .orderByChild("commonNameId")
                .equalTo(commonNameId);
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            priceList.clear();
            storeItemKeep.clear();
            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NewStockMainModel newPrice= snapshot.getValue(NewStockMainModel.class);
                    storeItemKeep.add(newPrice);
                    //String name, String highest, String lowest, String id
                        NewStockDisplayModel thisData= new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),"", newPrice.isBan());
                        //NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getTraderName(),newPrice.getHighPrice(),newPrice.getLowPrice(),newPrice.getId(),newPrice.getRating(), newPrice.getVariant1Name(),newPrice.getVariant1LowPrice(),newPrice.getVariant1HighPrice(),newPrice.getVariant2Name(),newPrice.getVariant2LowPrice(),newPrice.getVariant2HighPrice(),newPrice.getVariant3Name(),newPrice.getVariant3LowPrice(),newPrice.getVariant3HighPrice(),newPrice.getVariant4Name(),newPrice.getVariant4LowPrice(),newPrice.getVariant4HighPrice(),newPrice.getStoreId(), newPrice.getMetricId(),newPrice.getTraderDesc(), newPrice.getCreatorId(), newPrice.isBan());
                        priceList.add(thisData);

                }
                adapterOp();
            }
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };

    @Override
    protected void onRestart() {
        super.onRestart();
        DataModel dataModel = new DataModel();
        DataClass dataClass = new DataClass();
        //progressDialog.show();
        //could reduce time by calling a refresh that interlinks them both
        dataClass.getData();
        dataModel.getData();
    }

    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2,bottomPanel;
        bottomPanel = findViewById(R.id.bottomPanel);
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        stopSearch = findViewById(R.id.stopSearch);
        searchStringField = findViewById(R.id.searchString);
        SearchAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show search input and hide bottom panel
                topPanel2.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.GONE);
            }
        });
        stopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide search input and show bottom panel
                searchStringField.setText("");
                topPanel.setVisibility(View.GONE);
                topPanel2.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.VISIBLE);
            }
        });
    }
    private void basePanelListeners(){
        Button home, account, market, store;
        Button gps;
        home = findViewById(R.id.home_floor);
        account = findViewById(R.id.account_floor);
        market = findViewById(R.id.store_floor);
        store = findViewById(R.id.store_ll_floor);
        gps = findViewById(R.id.gps);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Farm_Transition");
                startActivity(intent);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(),NotificationView.class);
                //  startActivity(intent);
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Market_Transition");
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Store_Transition");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setPriceList();
    }
    public void openDialog(){
        CartPurchaseQuantityDialogue qtyDialog = new CartPurchaseQuantityDialogue();
        qtyDialog.show(getSupportFragmentManager(),"Set Item Quantity");
    }
    private void adapterOp(){
        mAdapter = new NewStock_StoreAdapter(priceList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new NewStock_StoreAdapter.OnPriceClickListener() {
            @Override
            public void onItemLongClick(int position) {
                if(ModelClass.admin) {
                    StoreItems.EditReference = storeItemKeep.get(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Set As Hot Deal?");
                    alertDialog.setMessage("Activates for front page");
                    // alertDialog.setIcon(R.drawable.contact);
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StoreItems.EditReference.setIsEditorsPick("True");
                            databaseReference = FirebaseDatabase.getInstance().getReference("Store_Stock");
                            databaseReference.child(StoreItems.EditReference.getId()).setValue(StoreItems.EditReference).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Editors Pick Set", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onStoreMetaClick(int position) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreView");
                intent.putExtra("storeId",priceList.get(position).getStoreId());
                //intent.putExtra("metric",metricLabel);
                startActivity(intent);
            }

            @Override
            public void onCartClick(int position) {
                if(modelClass.userLoggedIn()){
                    CartPurchaseQuantityDialogue.thisItem = storeItemKeep.get(position);
                    openDialog();
                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFavClick(int position, ImageView favBtn) {
                if(modelClass.userLoggedIn()){
                    StoreItems.EditReference = storeItemKeep.get(position);
                    String itemId = StoreItems.EditReference.getId();
                    String userId = modelClass.getCurrentUserId();
                    ProcessFavourites processFavourites= new ProcessFavourites();
                    processFavourites.setFav(userId,itemId, favBtn);
                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBuyClick(int position) {
                if(modelClass.userLoggedIn()){
                    StoreItems.EditReference = storeItemKeep.get(position);
                    final Model_Transaction modelTransaction= new Model_Transaction();
                    modelTransaction.setItmQty(1.0);
                    modelTransaction.setBuyerId(modelClass.getCurrentUserId());
                    modelTransaction.setPayerId(modelClass.getCurrentUserId());
                    modelTransaction.setItemId(StoreItems.EditReference.getId());
                    double cost = Double.parseDouble(StoreItems.EditReference.getItemCost());
                    modelTransaction.setItemCost(cost);
                    modelTransaction.setStoreId(StoreItems.EditReference.getStoreId());
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Purchase Alert!!!");
                    alertDialog.setMessage("You are about to purchase "+StoreItems.EditReference.getItemQty()+" "+StoreItems.EditReference.getAdvertName()+"\nCost: ₦"+cost);
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
                            progressDialog.setMessage("processing...");
                            progressDialog.show();
                            ProcessWallet processWallet = new ProcessWallet();
                            processWallet.getDeductData(modelClass.getCurrentUserId(),progressDialog,context,modelTransaction);

                        }
                    });
                    alertDialog.show();

                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditClick(int position) {
                StoreItems.EditReference = storeItemKeep.get(position);
                ModelClass modelClass = new ModelClass();
                if(modelClass.getCurrentUserId().equals(StoreItems.EditReference.getCreatorId())){
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.NewStockActivity");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Main6Activity.this,"Contact store owner",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onBanClick(int position) {
            }
        });

        searchOperandi();
    }
    private void searchOperandi(){
        searchStringField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Toast.makeText(StoreActivity.this,s,Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
