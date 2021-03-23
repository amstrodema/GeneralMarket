package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Account.Account;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Admin.Admin;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet.Wallet;

public class Main8Activity extends AppCompatActivity implements loginDialog.dialogListener {
    private Button btnAcct,notification,setting,helpBtn,loan,favey,logOutClick,adminBtn;
    private TextView logInStatus,marque;
    FirebaseAuth firebaseAuth;
    private TextView trades, stocksBtn,label;
    private ImageView advert2, advert3,storeBtn,marktBtn,farmBtn,archiveBtn,cartBonus,wallet;
    private ModelClass modelClass = new ModelClass();
    ProgressDialog progressDialog;
    private RelativeLayout adminPanel,logOutPanel;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    int counter =3;
    private DbHelper dbHelper;
    //Icon_Manager icon_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
      //  dbHelper = new DbHelper(this);
        //icon_manager = new Icon_Manager();
        //((TextView)findViewById(R.id.frontLabel)).setTypeface(icon_manager.get_icons("fonts/ionicons.ttf",this));
        setup();
     /*   ArrayList<SettingsModel>ky = new ArrayList<>();
        ky.add(new SettingsModel("","","int",180,12.00));
        ky.add(new SettingsModel("","gbam","text",0,0));
        ky.add(new SettingsModel("","","double",0,12.00));
        dbHelper.insertUserDetails(ky);
StringBuilder val= new StringBuilder();
        for (SettingsModel x: dbHelper.getUserDetails()) {
            val.append(x.getSettingName()).append(": ").append(x.getValueInt()).append("\n");
        }
        label.setText(val.toString());*/
        if(splashScreen.message!= null){
            marque.setText(splashScreen.message);
            marque.setVisibility(View.VISIBLE);
        }
        else{
            marque.setVisibility(View.GONE);
        }

        marque.setSelected(true);
        clickListeners();
        firebaseAuth = FirebaseAuth.getInstance();
        String stat =modelClass.getCurrentUserMail();
        if(stat.equals("oluyinkabiz@gmail.com")){
            adminPanel.setVisibility(View.VISIBLE);
            ModelClass.admin =true;
        }
        else
            ModelClass.admin =false;
        if(stat.equals("Guest")){
            logOutPanel.setVisibility(View.GONE);
            adminPanel.setVisibility(View.GONE);
        }
        stat ="Logged in as: "+stat;
        logInStatus.setText(stat);
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();//Main_Page_Two
        storageReference.child("Advert").child("Main_Page").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert2);
            }
        });
        storageReference.child("Advert").child("Main_Page_Two").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert3);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        notificationWorker();
        startLocationUpdates();
    }
    private void setup(){
        label = findViewById(R.id.label);
        logOutPanel = findViewById(R.id.logOutPanel);
        adminPanel = findViewById(R.id.adminPanel);
        wallet = findViewById(R.id.wallet);
        trades = findViewById(R.id.trades);
        notification = findViewById(R.id.notification);
        advert3 = findViewById(R.id.advert3);
        setting = findViewById(R.id.setting);
        logOutClick =findViewById(R.id.logOut);
        logInStatus =findViewById(R.id.logInStatus);
        adminBtn =findViewById(R.id.adminBtn);
        advert2 =findViewById(R.id.advert2);
        btnAcct =findViewById(R.id.account);
        helpBtn= findViewById(R.id.L3c);
        archiveBtn= findViewById(R.id.L2b);
       // recipeBtn= findViewById(R.id.L2c);
        farmBtn= findViewById(R.id.L11a);
        marktBtn= findViewById(R.id.btn2);
        stocksBtn = findViewById(R.id.btnooo);
        storeBtn = findViewById(R.id.L2a);
        cartBonus = findViewById(R.id.cartBonus);
        loan = findViewById(R.id.loan);
        favey = findViewById(R.id.favey);
        marque = findViewById(R.id.marque);
    }
    @Override
    public void finish() {
      /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit General Market?");
        alertDialog.setIcon(R.drawable.danger);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //delete transaction
            }
        });
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //confirm param and receive payment if safe
                super.finish();
            }
        });
        alertDialog.show();*/
    }

    private void clickListeners() {
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, Wallet.class));
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!modelClass.userLoggedIn()){
                    Toast.makeText(Main8Activity.this, "Log In Required!", Toast.LENGTH_SHORT).show();
                   // finish();
                }
                else
                startActivity(new Intent(Main8Activity.this, Trade.class));
            }
        });
        cartBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "cart");
                startActivity(intent);
            }
        });
        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "loan");
                startActivity(intent);
            }
        });
        favey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "favey");
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, NotificationView.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, SettingsActivity.class));
            }
        });
        logOutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(Main8Activity.this, Main8Activity.class));
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, Admin.class));
            }
        });
        btnAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if not user den proceed to sin-in or create else profile
                if (modelClass.userLoggedIn()) {
                    startActivity(new Intent(Main8Activity.this, Account.class));
                } else {
                    openDialog();
                }

            }
        });
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, Help.class));
              }
        });
        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "Archive_Transition");
                startActivity(intent);
            }
        });
       /* recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "Recipe_Transition");
                startActivity(intent);
            }
        });*/
        farmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "Farm_Transition");
                startActivity(intent);
            }
        });
        marktBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "Market_Transition");
                startActivity(intent);
            }
        });
        stocksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main8Activity.this, Stocks.class));
            }
        });
        //store btn and smart store btn does d same tin. adjust bot wen need arises.
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main8Activity.this, TransitionAdvertDisplay.class);
                intent.putExtra("Location", "Store_Transition");
                startActivity(intent);
            }
        });

    }
    private void openDialog(){
        loginDialog itemDialog = new loginDialog();
        itemDialog.show(getSupportFragmentManager(),"Log In");
    }

    public void registerUserPlatform(View view){
        Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.RegisterActivity");
        startActivity(intent);
    }
    @Override
    public void applyTexts(String thisValue1, String thisValue2) {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(thisValue1,thisValue2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    checkEmailVerification();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(Main8Activity.this,"Log In Not Successful", Toast.LENGTH_SHORT).show();
                    counter --;
                }
            }
        });

    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();
        progressDialog.dismiss();
        if(emailFlag){
            finish();
            startActivity(new Intent(Main8Activity.this, Main8Activity.class));
            Toast.makeText(Main8Activity.this,"Log In Successful", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseUser.sendEmailVerification();
            Toast.makeText(Main8Activity.this,"Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
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
   private void notificationWorker(){
        Data data  = new Data.Builder()
                .putString("type","notification")
                .build();
        Constraints constraints =  new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SampleWorker.class, 5, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(2, TimeUnit.MINUTES)
                .setInputData(data)
                .build();
        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }
}
