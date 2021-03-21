package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Setting.SettingsModel;

public class splashScreen extends AppCompatActivity {
   // private GifImageView gifImageView;
  //  private ImageView imageView;
    final private static int requestCode = 123;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static String message;
    private TextView appSignature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        appSignature = findViewById(R.id.appSignature);
       // gifImageView= findViewById(R.id.logo_anime);
        //imageView= findViewById(R.id.splashImage);
        //look out for weda permission for gps is granted to prevent errors
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    requestCode);
        }
        else{
            // delay for 1seconds to allow logo sink in users mind
            new Handler().postDelayed(new Runnable() {
                @Override
                    public void run() {
                 //   imageView.setVisibility(View.INVISIBLE);
                 //   gifImageView.setVisibility(View.VISIBLE);
                   setAdvert();
                }
            },1000);
        }

    }
    //set notifications cannel from start, no need to repeat setup just call d notify class
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case splashScreen.requestCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           // imageView.setVisibility(View.INVISIBLE);
                           // gifImageView.setVisibility(View.VISIBLE);
                            setAdvert();
                        }
                    },3000);
                } else {
                    Toast.makeText(this, "App Needs GPS Permission For Best Performance", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private void setAdvert(){
       // load initial advert screen settable from admin activity
        final ImageView Landing_Page_Full = findViewById(R.id.Landing_Page_Full);
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child("Advert").child("Landing_Page_Full").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Landing_Page_Full);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                     /*   Intent intent = new Intent(splashScreen.this, Main8Activity.class);
                        intent.putExtra("dataType","deal");
                        startActivity(intent);
                       finish();*/
                        getSettings();
                    }
                },2000);
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

            }
        });
    }
    public void getSettings(){
        readData("Settings","id","");
    }

    private void readData(String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String updateDate="";
                    SettingsModel appAccessSetting = new SettingsModel(), appCleanSetting = new SettingsModel(), appVersionSetting = new SettingsModel(), appUpdateSetting = new SettingsModel(), appMinSetting = new SettingsModel();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        SettingsModel set= snapshot.getValue(SettingsModel.class);
                        assert set != null;
                        if(set.getSettingType().equals("GeneralMessage")){
                            message= set.getSettingValue();
                        }
                        if(set.getSettingType().equals("UpdateTime")){
                            updateDate= set.getSettingValue();
                        }
                        if(set.getSettingType().equals("AppAccess")){
                            appAccessSetting =set;
                        }
                        if(set.getSettingType().equals("AppClean")){
                            appCleanSetting = set;
                        }
                        if(set.getSettingType().equals("AppVersion")){
                            appVersionSetting = set;
                        }
                        if(set.getSettingType().equals("appMin")){
                            appMinSetting =set;
                        }
                    }
                    //Cleaning Market
                    if (appCleanSetting.getSettingValue() !=  null && appCleanSetting.getSettingValue().equals("Yes")){
                        Intent intent = new Intent(splashScreen.this, CleaningInProgress.class);
                        intent.putExtra("date", updateDate);
                        startActivity(intent);
                        finish();
                    }
                    else if (appAccessSetting.getSettingValue() !=  null && appAccessSetting.getSettingValue().equals("Allow Current Only")) {
                        //allows current app version
                       if(appVersionSetting.getSettingValue() != null && appVersionSetting.getSettingValue().equals(appSignature.getText().toString())){
                           Intent intent = new Intent(splashScreen.this, Main8Activity.class);
                           intent.putExtra("dataType","deal");
                           startActivity(intent);
                           finish();
                       }
                       else {
                           Intent intent = new Intent(splashScreen.this, Update.class);
                           intent.putExtra("date", updateDate);
                           startActivity(intent);
                           finish();
                       }
                    } else if (appAccessSetting.getSettingValue() != null && appAccessSetting.getSettingValue().equals("Allow Min & Notify Update")) {

                        ArrayList<String> acceptedVer = new ArrayList<>();
                        try{
                            String [] ver = appMinSetting.getSettingValue().split(",");
                            acceptedVer= new ArrayList<>(Arrays.asList(ver));
                        }
                        catch (Exception e){

                        }

                        if(appVersionSetting.getSettingValue().equals(appSignature.getText().toString())){
                            Intent intent = new Intent(splashScreen.this, Main8Activity.class);
                            intent.putExtra("dataType","deal");
                            startActivity(intent);
                            finish();
                        }
                        else if(acceptedVer.contains(appSignature.getText().toString())){
                            Intent intent = new Intent(splashScreen.this, Update.class);
                            intent.putExtra("date", updateDate);
                            intent.putExtra("allow", "Yes");
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(splashScreen.this, Update.class);
                            intent.putExtra("date", updateDate);
                            intent.putExtra("allow", "No");
                            startActivity(intent);
                            finish();
                        }
                    } else if (appAccessSetting.getSettingValue() != null && appAccessSetting.getSettingValue().equals("Force Update")) {
                        Intent intent = new Intent(splashScreen.this, Update.class);
                        intent.putExtra("date", updateDate);
                        intent.putExtra("allow", "No");
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(splashScreen.this, CleaningInProgress.class);
                        intent.putExtra("date", updateDate);
                        startActivity(intent);
                        finish();
                        Toast.makeText(splashScreen.this, "phew!!", Toast.LENGTH_SHORT).show();
                    }
                    //  firebaseCallback.onCallback(settings);
                }
                else{
                    //  firebaseCallback.onCallback(settings);
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
}
