package comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm;

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

import java.io.IOException;
import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Setting.SettingsModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Payment.PaymentPop;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.ProcessTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreAdd;

public class FarmAdd extends AppCompatActivity  implements PaymentPop.paymentDialogListener {
    //called from farm activity
    private boolean isEdit = false;
    Button saveStore;
    LinearLayout othersView;
    private FarmMainModel newFarm;
    private Context context = this;
    private Activity activity = this;
    RadioButton newStoreOthers, everyday,weekdays,weekends,openSoon,tempClosed;
    TextView activityTitle;
    private boolean ban =false;
    EditText storeName, storeDesc, storeLoc,openTime,closeTime,phone;
    CheckBox mon,tue,wed,thurs,fri,sat,sun;
    String itemId,creatorId, days;
    private Spinner newFarmDelivery;
    private ModelClass model;
    ArrayList<CheckBox> allCheckBox=new ArrayList<>();
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private double longitude, latitude;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private String name,desc,loc,open,close,fone;
    private FirebaseStorage firebaseStorage;
    private ImageButton uploadPicker;
    private ImageView imageView;
    private static int PICK_IMAGE =123;
    public Uri imagePath;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_farm);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        setUI();
        setUpEdit();
        startLocationUpdates();
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
                    Toast.makeText(FarmAdd.this,"Invalid Time Format",Toast.LENGTH_SHORT).show();
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
                            if(isEdit){
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle("Modify Farm Details?");
                                alertDialog.setIcon(R.drawable.danger);
                                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setMessage("Modifying Farm Details");
                                        progressDialog.show();
                                        databaseReference = FirebaseDatabase.getInstance().getReference("AllFarms");
                                        //itemId = databaseReference.push().getKey();
                                        //creatorId=model.getCurrentUserId();// loc
                                        final FarmMainModel newFarm = new FarmMainModel(5.0f,itemId,name,creatorId,desc,loc,fone,days,open,close,newFarmDelivery.getSelectedItem().toString(),longitude,latitude, ban);
                                        databaseReference.child(itemId).setValue(newFarm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String farmId= itemId;
                                                Toast.makeText(FarmAdd.this,"Farm Edited Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmView");
                                                //storeId is only an identifier for farmId here, used to ensure easy flow btw store and farm wallets
                                                intent.putExtra("storeId",newFarm.getId());
                                                intent.putExtra("creator",newFarm.getCreatorId());
                                                progressDialog.dismiss();
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                });
                                alertDialog.show();
                            }
                            else {
                                progressDialog.show();
                                databaseReference = FirebaseDatabase.getInstance().getReference("AllFarms");
                                itemId = databaseReference.push().getKey();
                                creatorId=model.getCurrentUserId();// loc
                                final FusedLocationProviderClient client =
                                        LocationServices.getFusedLocationProviderClient(FarmAdd.this);
                                if (ContextCompat.checkSelfPermission(FarmAdd.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    // Get the last known location
                                    client.getLastLocation()
                                            .addOnCompleteListener(FarmAdd.this, new OnCompleteListener<Location>() {
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
                                                                newFarm = new FarmMainModel(5.0f,itemId,name,creatorId,desc,loc,fone,days,open,close,newFarmDelivery.getSelectedItem().toString(),longitude,latitude, ban);
                                                          //make payment
                                                             getSettings();
                                                                progressDialog.setMessage("Please Wait");
                                                            }
                                                            catch (Exception ex){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(FarmAdd.this,"Operation Failed, enable gps and try again!", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(FarmAdd.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //show message
                                            Toast.makeText(FarmAdd.this,"Location failed, enable gps and try again",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(FarmAdd.this,"Grant Access To GPS",Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                        else {
                            Toast.makeText(FarmAdd.this,"Please Log In", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(FarmAdd.this,"Carefully check the right set of boxes",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FarmAdd.this,"Please fill all the fields correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });
        newStoreOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersView.setVisibility(View.VISIBLE);
            }
        });
    }
    //get owner costs from settings
    private void getSettings(){
        model.readData(new ModelClass.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<SettingsModel> settings) {
                if(settings != null){
                    for (SettingsModel set:settings) {
                        if (set.getSettingType().equals("StoreCost")) {
                            StoreAdd.cost = set.getSettingValueDouble();
                            break;
                        }
                    }
                    progressDialog.dismiss();
                    StoreAdd.ownerType = "Farm";
                    openDialog();
                }
                else{
                    Toast.makeText(context, "Try Later - Error SHP_CX-5509!", Toast.LENGTH_SHORT).show();
                }
            }
        },"Settings","id","");
    }
    private void openDialog(){
        PaymentPop paymentDialog = new PaymentPop();
        paymentDialog.show(getSupportFragmentManager(),"Make Payment");
    }
    @Override
    public void applyTexts(boolean isPaid, double fee) {
        if(isPaid){
            progressDialog.setMessage("Forwarding Rent Request");
            progressDialog.show();
            //save store after payment confirmation
            ProcessTransaction transaction = new ProcessTransaction();
            transaction.payFarmFee(fee, context, progressDialog, newFarm, imagePath, activity,true);
            Toast.makeText(FarmAdd.this,"Payment Successful",Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Setting Farm Up");
            progressDialog.show();
            //save store after payment confirmation
            //saveFarm();
        }
        else {
            Toast.makeText(FarmAdd.this,"Payment Transaction Failed",Toast.LENGTH_SHORT).show();

        }
    }
    private void setUpEdit(){

        if(FarmView.EditReference != null){
            String tt ="EDIT FARM DETAILS";
            activityTitle.setText(tt);
            tt ="MODIFY FARM";
            saveStore.setText(tt);
            othersView.setVisibility(View.VISIBLE);
            isEdit=true;
            FarmMainModel editModel= FarmView.EditReference;
            FarmView.EditReference = null;
            ban = editModel.isBan();
            creatorId = editModel.getCreatorId();
            latitude = editModel.getLatitude();
            longitude = editModel.getLongitude();
            itemId = editModel.getId();
            storeName.setText(editModel.getFarmName());
            storeDesc.setText(editModel.getFarmDesc());
            storeLoc.setText(editModel.getFarmLocation());
            openTime.setText(editModel.getFarmOpeningTime());
            closeTime.setText(editModel.getFarmClosingTime());
            phone.setText(editModel.getFarmPhone());
            if(editModel.getDelivery().equals("AVAILABLE"))
            {
                newFarmDelivery.setSelection(0,true);
            }
            else {
                newFarmDelivery.setSelection(1,true);
            }
            String openDays = editModel.getFarmOpeningDays();
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
        imageView = findViewById(R.id.newStoreImage);
        uploadPicker = findViewById(R.id.uploadPicker);
        activityTitle = findViewById(R.id.activityTitle);
        newFarmDelivery = findViewById(R.id.newFarmDelivery);
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
    private String getOpenDays(){
        String days="Closed";
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
                if(count==1){
                    days ="";
                    days+=getDay(count);}
                else
                    days+=", "+getDay(count);
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
}