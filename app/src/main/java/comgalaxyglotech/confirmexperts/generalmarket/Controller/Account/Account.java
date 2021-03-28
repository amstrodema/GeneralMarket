package comgalaxyglotech.confirmexperts.generalmarket.Controller.Account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Farm.FarmAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet.Wallet;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Store.StoreAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmAdd;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.marketModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.newMarketModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataStock;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmView;
import comgalaxyglotech.confirmexperts.generalmarket.LocationHandler;
import comgalaxyglotech.confirmexperts.generalmarket.Main8Activity;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.Trade;
import comgalaxyglotech.confirmexperts.generalmarket.UserProfile;
import comgalaxyglotech.confirmexperts.generalmarket.marketDialog;

public class Account extends AppCompatActivity implements marketDialog.marketDialogListener {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private ProgressBar profileProBar;
    private ScrollView profilePanel;
    private LinearLayout profileMetaPanel,marketPickLayout;
    private TextView profileTel,profileCountry,profileMail,profileName;
    private Button createMarketStore, createStandaloneStore,proceed, farmStoreCreate;
    private ModelClass modelClass = new ModelClass();
    private ArrayList<NewStockDisplayModel> displayData= new ArrayList<>();
    private ArrayList<NewStockDisplayModel> displayFarmData = new ArrayList<>();
    private AutoCompleteTextView marketName;
    //private String[] mktNames=new String[]{"Zealot","Truce","Capanon","Strum","Vector"};
    private ImageView cancel_action;
    ArrayAdapter<String> adapterItm;
    private double longitude, latitude;
    private RelativeLayout bottomPanel;
    public boolean isFound = false;
    private String marketId;
    private ArrayList<marketModel> mktList= new ArrayList<>();
    private ArrayList<String> verifyList= new ArrayList<>();
    private ArrayList<String> list= new ArrayList<>();
    private ProgressDialog progressDialog;


    private StoreAdapter mAdapter_stores;
    private ArrayList<StoreDisplayModel> storeList= new ArrayList<>();
    private RecyclerView mRecyclerView_stores;

    private FarmAdapter mAdapter_farms;
    private ArrayList<FarmDisplayModel> farmList= new ArrayList<>();
    private RecyclerView mRecyclerView_farms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        setUI();
        basePanelListeners();

        RecyclerView.LayoutManager mlayoutManager_stores = new LinearLayoutManager(this);
        mRecyclerView_stores = findViewById(R.id.stores);
        mRecyclerView_stores.setHasFixedSize(true);
        mRecyclerView_stores.setLayoutManager(mlayoutManager_stores);

        RecyclerView.LayoutManager mlayoutManager_farms = new LinearLayoutManager(this);
        mRecyclerView_farms = findViewById(R.id.farms);
        mRecyclerView_farms.setHasFixedSize(true);
        mRecyclerView_farms.setLayoutManager(mlayoutManager_farms);


        setListener();

        //marketName.setEnabled(true);
        adapterItm = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        marketName.setAdapter(adapterItm);
        getData();
        reloadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    DataStock dataStock = new DataStock();
    //get stocks for stores
    private void reloadData(){
        dataStock.readData(new DataStock.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> list) {
                displayData = new ArrayList<>(list);
                //   ReviewData reviewData = new ReviewData();
               // mAdapter_stores.notifyDataSetChanged();
                getReloadData();
            }
        },"Store_Stock","creatorId",modelClass.getCurrentUserId());

    }
    //get stocks for farms
    public void getReloadData(){
        dataStock.readData(new DataStock.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> list) {
                displayFarmData = new ArrayList<>(list);
              //  mAdapter_farms.notifyDataSetChanged();
                locationFinder();
            }
        },"Farm_Stock","creatorId",modelClass.getCurrentUserId());
    }
    private void setListener(){
        farmStoreCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, FarmAdd.class));
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mkt = marketName.getText().toString().trim();
                if(!mkt.equals("")){
                    mkt = mkt.toLowerCase();
                    if(verifyList.contains(mkt)){
                        int indx = verifyList.indexOf(mkt);
                        marketId = mktList.get(indx).getMarketId();
                        Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreAdd");
                        intent.putExtra("marketId",marketId);
                        startActivity(intent);
                    }
                    else {
                       marketDialog.newMarketName = marketName.getText().toString().trim();
                        openDialog();
                    }
                }
                else{
                    Toast.makeText(Account.this, "Type Market Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createMarketStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketPickLayout.setVisibility(View.VISIBLE);
                profilePanel.setVisibility(View.GONE);
                bottomPanel.setVisibility(View.GONE);
                progressDialog.setMessage("Loading Markets...");
                progressDialog.show();
                getMktData();
            }
        });
        createStandaloneStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreAdd");
                //intent.putExtra("marketId",marketId);
                startActivity(intent);
            }
        });
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              cancelPanel();
            }
        });
    }
    private void cancelPanel(){
        marketPickLayout.setVisibility(View.GONE);
        profilePanel.setVisibility(View.VISIBLE);
        bottomPanel.setVisibility(View.VISIBLE);
        marketName.setText("");
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
                Intent intent = new Intent(getApplicationContext(), Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Account.this,Account.class));
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Wallet.class));
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, Trade.class));
            }
        });
    }
    private void setUI(){
        farmStoreCreate = findViewById(R.id.farmStoreCreate);
        proceed = findViewById(R.id.proceed);
        bottomPanel = findViewById(R.id.bottomPanel);
        marketName= findViewById(R.id.marketAuto);
        cancel_action = findViewById(R.id.cancel_action);
        profilePanel = findViewById(R.id.profilePanel);
        marketPickLayout = findViewById(R.id.marketPickLayout);
        profileMetaPanel = findViewById(R.id.profileMetaPanel);
        profileTel = findViewById(R.id.profileTel);
        profileCountry = findViewById(R.id.profileCountry);
        profileMail = findViewById(R.id.profileMail);
        profileName = findViewById(R.id.profileName);
        createMarketStore = findViewById(R.id.marketStoreCreate);
        createStandaloneStore = findViewById(R.id.standaloneStoreCreate);
        profileProBar = findViewById(R.id.profileProBar);
    }
    private void openDialog(){
        marketDialog itemDialog = new marketDialog();
        itemDialog.show(getSupportFragmentManager(),"Add New Market");
    }

    @Override
    public void launchStore(String mktId) {
        marketId = mktId;
        Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreAdd");
        intent.putExtra("marketId",marketId);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cancelPanel();
    }

    //sets the parameter for the query
    private void getData(){
        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<UserProfile> profile) {
                UserProfile userProfile = profile.get(0);
                profileName.setText(userProfile.getLname()+" "+userProfile.getFname());
                profileMail.setText(userProfile.getEmail());
                profileCountry.setText(userProfile.getCountry());
                profileTel.setText(userProfile.getPhone());
                profileProBar.setVisibility(View.GONE);
                profileMetaPanel.setVisibility(View.VISIBLE);

            }
        },"userProfile","id",modelClass.getCurrentUserId());
    }

    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserProfile> profile = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserProfile userProfile= snapshot.getValue(UserProfile.class);
                        //UserProfile prof = new  UserProfile(wallet_Mode.getId(),wallet_Mode.getUserId(),wallet_Mode.getMainWallet(),wallet_Mode.getBonusWallet(),wallet_Mode.getReferralWallet(),wallet_Mode.getUpfrontWallet());
                        profile.add(userProfile);

                    }
                    firebaseCallback.onCallback(profile);
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
        void onCallback (ArrayList<UserProfile> profile);
    }

    //set market data values
    //sets the parameter for the query
    private void getMktData(){
        readMktData(new FirebaseMktCallback() {
            @Override
            public void onCallback( ArrayList<marketModel> mktModel) {
                mktList.clear();
                verifyList.clear();
                for (marketModel mkt: mktModel) {
                    list.add(mkt.getMarketName());
                    verifyList.add(mkt.getMarketName().toLowerCase());
                }
                mktList= mktModel;
                adapterItm.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        },"Market","marketName","");
    }
    //this methods access the required query resources in a listener and runs the query from getData
    private void readMktData(final FirebaseMktCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<marketModel> mktItms = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        newMarketModel newMarketMode= snapshot.getValue(newMarketModel.class);
                        marketModel newMarket=new marketModel(newMarketMode.getMarketName(),newMarketMode.getMarketLastTradingDay(),newMarketMode.getId(),newMarketMode.getMarketTradeFreq(),newMarketMode.getMarketDescription(),newMarketMode.getRating(),newMarketMode.getMarketDescription(), "");
                        mktItms.add(newMarket);

                    }
                    firebaseCallback.onCallback(mktItms);
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
    private interface FirebaseMktCallback{
        void onCallback (ArrayList<marketModel> mktModel);
    }


    private void setStoreList(){
        Query query = FirebaseDatabase.getInstance().getReference("AllStores")
                .orderByChild("creatorId")
                .equalTo(modelClass.getCurrentUserId());
        query.addListenerForSingleValueEvent(valueEventListener);
        // databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            storeList.clear();
            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StoreMainModel newStoreMode= snapshot.getValue(StoreMainModel.class);
                    String dist = checkDistance(latitude, longitude,newStoreMode.getLatitude(), newStoreMode.getLongitude());
                    //when user enables search distance restriction and allows stores without specified location
                    StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), dist, newStoreMode.isBan(),newStoreMode.getCreatorId());
                    storeList.add(newStore);
                }
            }
            mAdapter_stores = new StoreAdapter(storeList,displayData);
            mRecyclerView_stores.setAdapter(mAdapter_stores);
            mAdapter_stores.notifyDataSetChanged();
            mAdapter_stores.setOnItemClickListener(new StoreAdapter.OnStoreItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreView");
                    intent.putExtra("storeId",storeList.get(position).getId());
                    intent.putExtra("creator",storeList.get(position).getCreatorId());
                    startActivity(intent);
                }
            });
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void setFarmList(){
        Query query = FirebaseDatabase.getInstance().getReference("AllFarms")
                .orderByChild("creatorId")
                .equalTo(modelClass.getCurrentUserId());
        query.addListenerForSingleValueEvent(valueFarmEventListener);
        // databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }
    private ValueEventListener valueFarmEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            farmList.clear();
            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FarmMainModel newMarketMode= snapshot.getValue(FarmMainModel.class);
                    String dist = checkDistance(latitude, longitude,newMarketMode.getLatitude(), newMarketMode.getLongitude());
                    FarmDisplayModel farm=new FarmDisplayModel(newMarketMode.getRatings(),newMarketMode.getFarmName(),newMarketMode.getFarmOpeningTime(),newMarketMode.getFarmClosingTime(),newMarketMode.getDelivery(),"186",newMarketMode.getId(),newMarketMode.getFarmDesc(),newMarketMode.getFarmLocation(),dist, newMarketMode.isBan(), newMarketMode.getCreatorId());
                    farmList.add(farm);
                }
            }
            mAdapter_farms = new FarmAdapter(farmList, displayFarmData);
            mRecyclerView_farms.setAdapter(mAdapter_farms);
            mAdapter_farms.notifyDataSetChanged();
            mAdapter_farms.setOnItemClickListener(new FarmAdapter.OnAllCommoditiesItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(Account.this, FarmView.class);
                    intent.putExtra("storeId",farmList.get(position).getId());
                    intent.putExtra("creator",farmList.get(position).getCreator());
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



    public void locationFinder(){
        final FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location
            client.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // ...
                            //  Task<Location>
                            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    try{
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                    catch (Exception ex){
                                        latitude = 0;
                                        longitude = 0;
                                    }
                                    setStoreList();
                                    setFarmList();
                                }
                            });
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //show message
                    Toast.makeText(Account.this,"Location failed, try again",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private String checkDistance(double lat, double longi, double theLat, double theLongi){
        LocationHandler locationHandler = new LocationHandler();
        String locationDist = locationHandler.distance(lat, longi, theLat, theLongi);
        if(locationDist.equals("0")){
            return "Currently Here";
        }
        else{
            return locationDist;
        }
    }
}
