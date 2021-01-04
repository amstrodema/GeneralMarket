package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.content.Context;
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
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Farm extends AppCompatActivity{
    //called from activity 8
    private SwipeRefreshLayout swiper;
    private RecyclerView mRecyclerView;
    private FarmAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<FarmDisplayModel> marketList= new ArrayList<>();
    private ArrayList<NewStockDisplayModel> displayData = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference marketReference;
    private Context context = this;
    private ModelClass modelClass = new ModelClass();
    ProgressBar progressBar;
    private EditText searchStringField;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private double longitude, latitude;
    RegisterActivity gt =new RegisterActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        //dataClass.getData();
        swiper = findViewById(R.id.swiper);
        modelClass.getPreferences(this);
        progressBar = findViewById(R.id.progress);
        Button searchBtn  = findViewById(R.id.searchBtn);
        //marketReference= FirebaseDatabase.getInstance().getReference("Farm");
        startLocationUpdates();
        Intent rIntent = new Intent("finish");
        sendBroadcast(rIntent);
        try{
            TransitionAdvertDisplay.StopThisActivity.finish();
        }
        catch (Exception ignored){

        }
        searchBarOp();
        basePanelListeners();
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.farmRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mlayoutManager);
        //click listener
        getReloadData();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReloadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(marketList.size() > 0)
        getReloadData();
    }

    /* public void openDialog(){
            itemDialogue_Farm itemDialog = new itemDialogue_Farm();
            itemDialog.show(getSupportFragmentManager(),"Add New Farm Item");
        }
        @Override
        public void applyTexts(String itemName,String newItemClass) {
            String itemId;

            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("Item");
            itemId = databaseReference.push().getKey();
            //marketId= marketID;
            ModelClass model = new ModelClass();
            String creatorId=model.getCurrentUserId();
            ExampleItem newItem = new ExampleItem(itemName,itemId,"","", newItemClass);
            databaseReference.child(itemId).setValue(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Farm.this,"Farm Item Added Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    private void locationFinder(){
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
                                    setFarmList();
                                }
                            });
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //show message
                    Toast.makeText(Farm.this,"Location failed, enable gps and try again",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // Trigger new location updates at interval
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
       // getReloadData();
    }

    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2;
        final LinearLayout bottomPanel;
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
    private void setFarmList(){
        Query query = FirebaseDatabase.getInstance().getReference("AllFarms")
                .orderByChild("farmName");
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
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            marketList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FarmMainModel newMarketMode= snapshot.getValue(FarmMainModel.class);
                    String dist = checkDistance(latitude, longitude,newMarketMode.getLatitude(), newMarketMode.getLongitude());
                    if(ModelClass.distanceRestriction && ModelClass.noUnknown.equals("-1"))
                    {
                        if(newMarketMode.getLongitude() != 0 && newMarketMode.getLatitude() != 0){

                            if(!dist.equals("RESTRICTED")){
                                FarmDisplayModel newMarket=new FarmDisplayModel(newMarketMode.getRatings(),newMarketMode.getFarmName(),newMarketMode.getFarmOpeningTime(),newMarketMode.getFarmClosingTime(),newMarketMode.getDelivery(),"186",newMarketMode.getId(),newMarketMode.getFarmDesc(),newMarketMode.getFarmLocation(),dist, newMarketMode.isBan(), newMarketMode.getCreatorId());
                                marketList.add(newMarket);
                            }

                        }
                    }
                    else {
                        if(!dist.equals("RESTRICTED")){
                            FarmDisplayModel newMarket=new FarmDisplayModel(newMarketMode.getRatings(),newMarketMode.getFarmName(),newMarketMode.getFarmOpeningTime(),newMarketMode.getFarmClosingTime(),newMarketMode.getDelivery(),"186",newMarketMode.getId(),newMarketMode.getFarmDesc(),newMarketMode.getFarmLocation(),dist, newMarketMode.isBan(), newMarketMode.getCreatorId());
                            marketList.add(newMarket);
                        }

                    }

                }
                adapterOp();
            }
            progressBar.setVisibility(View.GONE);
            swiper.setRefreshing(false);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private DataStock dataStock = new DataStock();
    public void getReloadData(){
        dataStock.readData(new DataStock.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NewStockDisplayModel> list) {
                displayData = new ArrayList<>(list);
                locationFinder();
            }
        },"Farm_Stock","storeId","");
    }
    private void adapterOp(){
        mAdapter = new FarmAdapter(marketList, displayData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new FarmAdapter.OnAllCommoditiesItemClickListener() {
            @Override
            public void onItemClick(int position) {


                if(modelClass.userLoggedIn() && marketList.get(position).getCreator().equals(modelClass.getCurrentUserId())){
                    Intent intent = new Intent(Farm.this, FarmOwnerHome.class);
                    //storeId is only an identifier for farmId here, used to ensure easy flow btw store and farm wallets
                    intent.putExtra("storeId",marketList.get(position).getId());
                    intent.putExtra("creator",marketList.get(position).getCreator());
                      startActivity(intent);
                }
                else{
                    Intent intent = new Intent(Farm.this, FarmView.class);
                    startActivity(intent);
                }

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
