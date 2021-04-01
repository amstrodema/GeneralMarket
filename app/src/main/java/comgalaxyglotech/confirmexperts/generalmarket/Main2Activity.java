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
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Market.MarketAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Market.marketDialog;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Register.RegisterActivity;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.marketModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.newMarketModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.Services.Location.LocationHandler;

public class Main2Activity extends AppCompatActivity{
    private SwipeRefreshLayout swiper;
    private RecyclerView mRecyclerView;
    private MarketAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<marketModel> marketList= new ArrayList<>();
    private EditText searchStringField;
    private DatabaseReference marketReference;
    private Context context = this;
    private ModelClass modelClass = new ModelClass();
    private double latitude, longitude;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressBar progressBar;
    //ProgressDialog progressDialog;
    private boolean isLoggedIn = modelClass.userLoggedIn();
 //   private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    //ProgressBar progressBar;
    RegisterActivity gt =new RegisterActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        searchBarOp();
        basePanelListeners();
        modelClass.getPreferences(this);
        firebaseAuth=FirebaseAuth.getInstance();
        user =firebaseAuth.getCurrentUser();
        marketReference=FirebaseDatabase.getInstance().getReference("Market");
        setView();
       progressBar = findViewById(R.id.progress);

       //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recyclerViewMarket);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        startLocationUpdates();

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationFinder();
    }

    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2,bottomPanel;
        bottomPanel = findViewById(R.id.bottomPanel);
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        Button searchBtn  = findViewById(R.id.searchBtn);
        stopSearch = findViewById(R.id.stopSearch);
        searchStringField = findViewById(R.id.searchString);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOperandi();
            }
        });
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
    private void setView(){
        swiper= findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                locationFinder();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(user != null)
        {
            getMenuInflater().inflate(R.menu.market_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_market)
        {

        }
        return super.onOptionsItemSelected(item);
    }


    public void openDialog(){
        marketDialog itemDialog = new marketDialog();
        itemDialog.show(getSupportFragmentManager(),"Add New Market");
    }
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
                                    setMarketList();
                                }
                            });
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //show message
                    Toast.makeText(Main2Activity.this,"Location failed, enable gps and try again",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest  mLocationRequest = new LocationRequest();
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
    private void setMarketList(){
        Query query = FirebaseDatabase.getInstance().getReference("Market")
                .orderByChild("marketName");
        query.addListenerForSingleValueEvent(valueEventListener);
       // databaseReference.addListenerForSingleValueEvent(valueEventListener);
     }
    private String checkDistance(double lat, double longi, double theLat, double theLongi){
        LocationHandler locationHandler = new LocationHandler();
        String locationDist = locationHandler.distance(lat, longi, theLat, theLongi);
        if(locationDist.equals("0")){
            return "You are currently here!";
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
                    newMarketModel newMarketMode= snapshot.getValue(newMarketModel.class);
                    String dist = checkDistance(latitude, longitude,newMarketMode.getLatitude(), newMarketMode.getLongitude());
                    if(ModelClass.distanceRestriction && ModelClass.noUnknown.equals("-1"))
                    {
                        if(newMarketMode.getLongitude() != 0 && newMarketMode.getLatitude() != 0){
                            if(!dist.equals("RESTRICTED")){
                                marketModel newMarket=new marketModel(newMarketMode.getMarketName(),newMarketMode.getMarketLastTradingDay(),newMarketMode.getId(),newMarketMode.getMarketTradeFreq(),newMarketMode.getMarketDescription(),newMarketMode.getRating(),newMarketMode.getMarketDescription(), dist);
                                marketList.add(newMarket);
                            }

                        }
                    }
                    else {
                        if(!dist.equals("RESTRICTED")){
                            marketModel newMarket=new marketModel(newMarketMode.getMarketName(),newMarketMode.getMarketLastTradingDay(),newMarketMode.getId(),newMarketMode.getMarketTradeFreq(),newMarketMode.getMarketDescription(),newMarketMode.getRating(),newMarketMode.getMarketDescription(), dist);
                            marketList.add(newMarket);
                        }

                    }

                }
                adapterOp();
            }
            swiper.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void adapterOp(){
        mAdapter = new MarketAdapter(marketList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new MarketAdapter.OnMarketItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main3Activity");
                intent.putExtra("marketID",marketList.get(position).getMarketId());
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
