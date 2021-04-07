package comgalaxyglotech.confirmexperts.generalmarket.Controller.Store;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
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

import comgalaxyglotech.confirmexperts.generalmarket.BL.Store.StoreAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataStock;
import comgalaxyglotech.confirmexperts.generalmarket.Services.Location.LocationHandler;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay;

public class StoreActivity extends AppCompatActivity {
    private SwipeRefreshLayout swiper;
    private RecyclerView mRecyclerView;
    private StoreAdapter mAdapter;
    private ModelClass modelClass = new ModelClass();
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<StoreDisplayModel> storeList= new ArrayList<>();
    private ArrayList<NewStockDisplayModel> displayData= new ArrayList<>();
    private ProgressBar progress;
    private DataStock dataStock = new DataStock();
    private String marketId ="";
    private double longitude, latitude;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private EditText searchStringField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        searchStringField = findViewById(R.id.searchString);
        swiper = findViewById(R.id.swiper);
        modelClass.getPreferences(this);
        searchBarOp();
        basePanelListeners();
        progress = findViewById(R.id.progress);
        try{
            TransitionAdvertDisplay.StopThisActivity.finish();
        }
        catch (Exception ignored){

        }
        //get data (marketId to limit the displayed stores to only those belonging to the market alone)
        //for request with no marketId, all stores are displayed
        try{
            Intent thisIntent= getIntent();
            marketId =thisIntent.getStringExtra("marketId");
        }
        catch (Exception e){ marketId= "";}
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.storeRecyclerView_New);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        startLocationUpdates();
        reloadData();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
              /*  if(storeList.size() > 0)
                    reloadData();*/
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //storelist is called the moment location is gotten
      //  locationFinder();
     //   DataStock dataStock = new DataStock();
        //
      //   dataStock.getReloadData(false, mAdapter, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

private void reloadData(){
        dataStock.readData(new DataStock.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> list) {
                displayData = new ArrayList<>(list);
                //   ReviewData reviewData = new ReviewData();
                locationFinder();
            }
        },"Store_Stock","storeId","");

}
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
                              }
                          });
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //show message
                    Toast.makeText(StoreActivity.this,"Location failed, try again",Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay");
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
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay");
                intent.putExtra("Location","Market_Transition");
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay");
                intent.putExtra("Location","Store_Transition");
                startActivity(intent);
            }
        });
    }
    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2,bottomPanel;
        bottomPanel = findViewById(R.id.bottomPanel);
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        stopSearch = findViewById(R.id.stopSearch);
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
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        final FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            // onLocationChanged(locationResult.getLastLocation());

                        }
                    },
                    Looper.myLooper());
        }
    }
    private void setStoreList(){
        Query query = FirebaseDatabase.getInstance().getReference("AllStores")
                .orderByChild("rating");
        query.addListenerForSingleValueEvent(valueEventListener);
        // databaseReference.addListenerForSingleValueEvent(valueEventListener);
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
    private void distanceRestrictedAndAllStoresAllowed(StoreMainModel newStoreMode, String dist){
        if(newStoreMode.getLongitude() != 0 && newStoreMode.getLatitude() != 0){
            if(marketId.equals("")){
                if(!dist.equals("RESTRICTED")){
                    StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), dist,newStoreMode.isBan(),newStoreMode.getCreatorId());
                    storeList.add(newStore);
                }

            }
            else{
                if(marketId.equals(newStoreMode.getMarketId())){
                    if(!dist.equals("RESTRICTED")){
                        StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), dist, newStoreMode.isBan(),newStoreMode.getCreatorId());
                        storeList.add(newStore);
                    }

                }
            }
        }
    }
    private void distanceNotRestricted(StoreMainModel newStoreMode, String dist){
        if(marketId.equals("")){
            if(!dist.equals("RESTRICTED")){
                StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), dist, newStoreMode.isBan(),newStoreMode.getCreatorId());
                storeList.add(newStore);
            }

        }
        else{
            if(marketId.equals(newStoreMode.getMarketId())){
                if(!dist.equals("RESTRICTED")){
                    StoreDisplayModel newStore=new StoreDisplayModel(newStoreMode.getId(),newStoreMode.getStoreName(),newStoreMode.getStoreOpeningTime(),newStoreMode.getStoreClosingTime(),newStoreMode.getDelievery(),newStoreMode.getRating(),newStoreMode.getStoreDesc(),newStoreMode.getStoreLocation(), dist, newStoreMode.isBan(),newStoreMode.getCreatorId());
                    storeList.add(newStore);
                }

            }
        }
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
                    if(ModelClass.distanceRestriction && ModelClass.noUnknown.equals("-1"))
                    {
                       distanceRestrictedAndAllStoresAllowed(newStoreMode,dist);
                    }
                    else {
                        distanceNotRestricted(newStoreMode,dist);
                    }
                }
                adapterOp();
            }
            progress.setVisibility(View.GONE);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void adapterOp(){
        mAdapter = new StoreAdapter(storeList,displayData);
        mAdapter.notifyDataSetChanged();
        swiper.setRefreshing(false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new StoreAdapter.OnStoreItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(StoreActivity.this, StoreView.class);
                intent.putExtra("storeId",storeList.get(position).getId());
                intent.putExtra("creator",storeList.get(position).getCreatorId());
                startActivity(intent);
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
