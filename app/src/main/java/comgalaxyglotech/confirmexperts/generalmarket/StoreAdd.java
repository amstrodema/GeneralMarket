package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class StoreAdd extends AppCompatActivity implements PaymentPop.paymentDialogListener{
    private boolean isEdit = false;
    Button saveStore;
    private Context context = this;
    private Activity activity = this;
    private float rating=0.0f;
    LinearLayout othersView;
    RelativeLayout container;
    RadioButton newStoreOthers, everyday,weekdays,weekends,openSoon,tempClosed,locationYes,locationNo;
    EditText storeName, storeDesc, storeLoc,openTime,closeTime,phone;
    private boolean ban = false;
    CheckBox mon,tue,wed,thurs,fri,sat,sun;
    String marketId = "";
    private TextView activityTitle,hereLabel;
    private ProgressDialog progressDialog;
    private Spinner newStoreDelivery;
    private String itemId,creatorId, dateCreated, lastRentDate, duration;
    private ModelClass model;
    ArrayList<CheckBox>allCheckBox=new ArrayList<>();
    private double longitude, latitude;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private DatabaseReference databaseReference;
    private String name,desc,loc,open,close,fone, days;
    private ImageButton uploadPicker;
    private ImageView imageView;
    public StoreMainModel newStore;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE =123;
    public Uri imagePath;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        setUI();
        setUpEdit();
        startLocationUpdates();
        listeners();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Intent previousActivity=getIntent();
        marketId = previousActivity.getStringExtra("marketId");
        //Toast.makeText(StoreAdd.this,marketId, Toast.LENGTH_SHORT).show();
    }
    private void listeners(){
        uploadPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
        saveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verify if te input details are correct for bot edit and creatin and den
                //on confirmation of payment, save store
                verifyFields();
            }
        });
        newStoreOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void verifyFields(){
        //id,creatorId,storeName,storeDesc,StoreLocation,storePhone,storeOpeningDays,storeOpeningTime,storeClosingTime
        model=new ModelClass();
        name=model.getEditTextValue(storeName);
        desc=model.getEditTextValue(storeDesc);
        loc=model.getEditTextValue(storeLoc);
        open=model.getEditTextValue(openTime);
        close=model.getEditTextValue(closeTime);
        fone=model.getEditTextValue(phone);
        days=getOpenDays();
        if(!isValidateEntry()){
            Toast.makeText(StoreAdd.this,"Invalid Time Format",Toast.LENGTH_SHORT).show();
        }
        else if( !name.isEmpty()&&!desc.isEmpty()&&!loc.isEmpty()&&!open.isEmpty()&&!close.isEmpty()&&!fone.isEmpty()){
            if((newStoreOthers.isChecked() && !days.equals("Closed")) || everyday.isChecked() || weekdays.isChecked() || weekends.isChecked() || openSoon.isChecked() || tempClosed.isChecked()){
                if(everyday.isChecked())
                    days="Everyday";
                else if (weekdays.isChecked())
                    days="Week Days";
                else if (weekends.isChecked())
                    days="Weekends";
                else if (openSoon.isChecked())
                    days="Opening Soon";
                else if (tempClosed.isChecked())
                    days="Closed For Now";
                model = new ModelClass();
                boolean user = model.userLoggedIn();
                if(user)
                {
                    if(isEdit)
                    {
                       editStore();
                    }
                    else{
                        databaseReference = FirebaseDatabase.getInstance().getReference("AllStores");
                        itemId = databaseReference.push().getKey();
                        creatorId=model.getCurrentUserId();

                        final FusedLocationProviderClient client =
                                LocationServices.getFusedLocationProviderClient(StoreAdd.this);
                        if (ContextCompat.checkSelfPermission(StoreAdd.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            // Get the last known location
                            client.getLastLocation()
                                    .addOnCompleteListener(StoreAdd.this, new OnCompleteListener<Location>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Location> task) {
                                            // ...
                                            //  Task<Location>
                                            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    try{
                                                        if(locationNo.isChecked()){
                                                            latitude = 0;
                                                            longitude = 0;
                                                        }
                                                        else{
                                                            latitude = location.getLatitude();
                                                            longitude = location.getLongitude();
                                                        }

                                                         newStore = new StoreMainModel(model.getNewId(),creatorId,name,desc,loc,fone,days,open,close,marketId,newStoreDelivery.getSelectedItem().toString(),0.0f,longitude,latitude, ban,model.getDate(),model.getDate(),"1");
                                                        //make payment for rent
                                                        progressDialog.setMessage("Please Wait!");
                                                        progressDialog.show();
                                                        getSettings();
                                                    }
                                                    catch (Exception ex){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(StoreAdd.this,"Operation Failed, enable gps and try again!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(StoreAdd.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //show message
                                    Toast.makeText(StoreAdd.this,"Location failed, enable gps and try again",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                }
                else {
                    Toast.makeText(StoreAdd.this,"Please Log In", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(StoreAdd.this,"Carefully check the right set of boxes",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(StoreAdd.this,"Please fill all the fields correctly",Toast.LENGTH_SHORT).show();
        }
    }
    private void editStore(){
        progressDialog.setMessage("Saving Modifications...");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("AllStores");
        final StoreMainModel newStore = new StoreMainModel(itemId,creatorId,name,desc,loc,fone,days,open,close,marketId,newStoreDelivery.getSelectedItem().toString(),rating,longitude,latitude, ban,dateCreated,lastRentDate,duration);
        databaseReference.child(itemId).setValue(newStore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.setMessage("Finishing Up");
                final String storeId= itemId;
                final StorageReference imageReference = storageReference.child("Store").child(storeId);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Modify Store Details?");
                alertDialog.setIcon(R.drawable.danger);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(imagePath == null){
                            Toast.makeText(StoreAdd.this,"Store Modified Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreView");
                            intent.putExtra("storeId",storeId);
                            intent.putExtra("creator",newStore.getCreatorId());
                            startActivity(intent);
                            finish();
                        }
                        else{
                            progressDialog.setMessage("Finishing Up");
                            imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Toast.makeText(StoreAdd.this,"Store Modified Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreView");
                                    intent.putExtra("storeId",storeId);
                                    intent.putExtra("creator",newStore.getCreatorId());
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StoreAdd.this,"Store Image Not Saved",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void setUpEdit(){

        if(StoreView.EditReference != null){
            String tt ="EDIT STORE DETAILS";
            activityTitle.setText(tt);
            tt ="MODIFY STORE";
            saveStore.setText(tt);
            locationYes.setVisibility(View.GONE);
            locationNo.setVisibility(View.GONE);
            hereLabel.setVisibility(View.GONE);
            othersView.setVisibility(View.VISIBLE);
            isEdit=true;
            StoreMainModel editModel= StoreView.EditReference;
            StoreView.EditReference = null;
            duration = editModel.getRentDuration();
            lastRentDate =editModel.getLastPaymentDate();
            dateCreated = editModel.getRegDate();
            ban =editModel.isBan();
            storageReference.child("Store").child(editModel.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageView);
                }

            });
            rating = editModel.getRating();
            creatorId = editModel.getCreatorId();
            latitude = editModel.getLatitude();
            longitude = editModel.getLongitude();
            itemId = editModel.getId();
            storeName.setText(editModel.getStoreName());
            storeDesc.setText(editModel.getStoreDesc());
            storeLoc.setText(editModel.getStoreLocation());
            openTime.setText(editModel.getStoreOpeningTime());
            closeTime.setText(editModel.getStoreClosingTime());
            phone.setText(editModel.getStorePhone());
            if(editModel.getDelievery().equals("AVAILABLE"))
            {
                newStoreDelivery.setSelection(0,true);
            }
            else {
                newStoreDelivery.setSelection(1,true);
            }
            String openDays = editModel.getStoreOpeningDays();
            if(!openDays.contains(",")){
                if(openDays.equals("Everyday"))
                    everyday.setChecked(true);
                else if (openDays.equals("Week Days"))weekdays.setChecked(true);
                else if (openDays.equals("Weekends"))weekends.setChecked(true);
                else if (openDays.equals("Opening Soon"))openSoon.setChecked(true);
                else if (openDays.equals("Closed For Now"))tempClosed.setChecked(true);
                othersView.setVisibility(View.GONE);
            }
            else {
                newStoreOthers.setChecked(true);
                othersView.setVisibility(View.VISIBLE);
                String [] daysSplit = openDays.split(",");
                for (String pDay: daysSplit) {
                    if(pDay.trim().equals("Monday")){
                        mon.setChecked(true);
                    }
                    else if(pDay.trim().equals("Tuesday")){
                        tue.setChecked(true);
                    }
                    else if(pDay.trim().equals("Wednesday")){
                        wed.setChecked(true);
                    }
                    else if(pDay.trim().equals("Thursday")){
                        thurs.setChecked(true);
                    }
                    else if(pDay.trim().equals("Friday")){
                        fri.setChecked(true);
                    }
                    else if(pDay.trim().equals("Saturday")){
                        sat.setChecked(true);
                    }
                    else if(pDay.trim().equals("Sunday")){
                        sun.setChecked(true);
                    }
                }
            }
        }
    }
    private void setUI(){
        hereLabel = findViewById(R.id.hereLabel);
        activityTitle =findViewById(R.id.activityTitle);
        uploadPicker = findViewById(R.id.uploadPicker);
        imageView = findViewById(R.id.newStoreImage);
        locationNo = findViewById(R.id.locationNo);
        locationYes = findViewById(R.id.locationYes);
        newStoreDelivery = findViewById(R.id.newStoreDelivery);
        phone=findViewById(R.id.newStorePhone);
        closeTime=findViewById(R.id.newStoreClosingTime);
        openTime=findViewById(R.id.newStoreOpeningTime);
        storeLoc=findViewById(R.id.newStoreLocation);
        storeDesc=findViewById(R.id.newStoreDesc);
        storeName=findViewById(R.id.newStoreName);
        sun=findViewById(R.id.newStoreSun);
        sat=findViewById(R.id.newStoreSat);
        fri=findViewById(R.id.newStoreFri);
        thurs=findViewById(R.id.newStoreThur);
        wed=findViewById(R.id.newStoreWed);
        tue=findViewById(R.id.newStoreTue);
        mon=findViewById(R.id.newStoreMon);
        tempClosed=findViewById(R.id.newStoreTempClosed);
        openSoon=findViewById(R.id.newStoreToOpen);
        weekends=findViewById(R.id.newStoreWeekEnds);
        weekdays=findViewById(R.id.newStoreWeekDays);
        everyday=findViewById(R.id.newStoreEveryday);
        saveStore=findViewById(R.id.newStoreSaveBtn);
        newStoreOthers=findViewById(R.id.newStoreOthers);
        othersView=findViewById(R.id.radioOtherOptions);
        container=findViewById(R.id.container);
    }
    public void disableOthersRadioView(View view){
        othersView.setVisibility(View.GONE);
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
    private String getOpenDays(){
        String days="Closed";
        allCheckBox.clear();
        allCheckBox.add(mon);
        allCheckBox.add(tue);
        allCheckBox.add(wed);
        allCheckBox.add(thurs);
        allCheckBox.add(fri);
        allCheckBox.add(sat);
        allCheckBox.add(sun);
        int count=1;
        for (CheckBox thisCheckBox:allCheckBox) {
            if(thisCheckBox.isChecked()){
                if(count==1)
                { days="";
                days+=getDay(count);}
                else
                {days+=", "+getDay(count);}
            }
            count++;
        }
        return days;
    }
    private String getDay(int no){
        if(no==1){
            return "Monday";
        }
        else if(no==2){
            return "Tuesday";
        }
        else if(no==3){
            return "Wednesday";
        }
        else if(no==4){
            return "Thursday";
        }
        else if(no==5){
            return "Friday";
        }
        else if(no==6){
            return "Saturday";
        }
        else if(no==7){
            return "Sunday";
        }
        else
            return "Closed";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
private boolean isValidateEntry(){
    String hold =closeTime.getText().toString();
    boolean ver = true;
    if(!hold.contains(":")){
        return false;
    }
    String [] timeVer = hold.split(":");
    int ist= Integer.parseInt(timeVer[0]);
    int snd= Integer.parseInt(timeVer[1]);
    if(ist >23 || snd >59){
        return false;
    }
    hold = openTime.getText().toString();
    if(!hold.contains(":")){
        return false;
    }
    timeVer = hold.split(":");
     ist= Integer.parseInt(timeVer[0]);
     snd= Integer.parseInt(timeVer[1]);
    if(ist >23 || snd >59){
        return false;
    }
    return ver;
}
    //get owner costs from settings
    private void getSettings(){
        model.readData(new ModelClass.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<A_Settings> settings) {
                if(settings != null){
                    for (A_Settings set:settings) {
                        if (set.getSettingType().equals("StoreCost")) {
                            cost = set.getSettingValueDouble();
                            break;
                        }
                    }
                    progressDialog.dismiss();
                    ownerType="Store";
                    openDialog();
                }
                else{
                    Toast.makeText(context, "Try Later - Error SHP_CX-5509!", Toast.LENGTH_SHORT).show();
                }
            }
        },"Settings","id","");
    }
    public static double cost =0.0;
    public static String ownerType;
    private void openDialog(){
        PaymentPop paymentDialog = new PaymentPop();
        paymentDialog.show(getSupportFragmentManager(),"Make Payment");
        //value is returned in applyTexts(), which prepares the rental invoice
    }
    @Override
    public void applyTexts(boolean isPaid, double fee) {
        if(isPaid){
            progressDialog.setMessage("Forwarding Rent Request");
            progressDialog.show();
            //save store after payment confirmation
            ProcessTransaction transaction = new ProcessTransaction();
            transaction.payStoreFee(fee, context, progressDialog, newStore, imagePath, activity,true);
        }
        else {
            Toast.makeText(StoreAdd.this,"Payment Transaction Cancelled",Toast.LENGTH_SHORT).show();

        }
    }
}
