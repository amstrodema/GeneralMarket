package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

/**
 * Created by ELECTRON on 11/09/2019.
 */

public class LocationHandler {
    public static FarmMainModel FarmModel;
    public static StoreMainModel StoreModel;
    public static newMarketModel MarketModel;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private DatabaseReference databaseReference;
 /*   @Override
    public void onMapReady(GoogleMap googleMap) {

        if(checkPermissions()) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }*/
 public String distance(double lat1, double lon1, double lat2, double lon2) {

     if((lat1 == 0 && lon1==0) || (lat2 == 0 && lon2== 0)){
         return "Distance Unknown";
     }
     if ((lat1 == lat2) && (lon1 == lon2)) {
         return "0";
     }
     else {
         double theta = lon1 - lon2;
         double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
         dist = Math.acos(dist);
         dist = Math.toDegrees(dist);
         dist = dist * 60 * 1.1515;
         //KM conversion
         dist = dist * 1.609344;
         String result = String.format("%.2f", dist);
         dist = Float.parseFloat(result);
         if(ModelClass.distanceRestriction){
             if(dist > ModelClass.maxDistance){
                 return  "RESTRICTED";
             }
         }
         if(dist <= 3){
             return ("Within Location Range");
         }
      /*   if (unit == "K") {
             dist = dist * 1.609344;
         } else if (unit == "N") {
             dist = dist * 0.8684;
         }*/
         return ("Approx "+result+"KM Away");
     }
 }
    protected void startLocationUpdates(Context context) {

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
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        final FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
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
    public void getGpsLocation(final Context context, final Activity activity, final String pickUp){
     startLocationUpdates(context);
        final FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(activity);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location
            client.getLastLocation()
                    .addOnCompleteListener(activity, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // ...
                            //  Task<Location>
                            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    try{
                                        if(pickUp.equals("market")) {
                                            MarketModel = Main3Activity.EditModel;
                                            Main3Activity.EditModel=null;
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Market");
                                            MarketModel.setLongitude(location.getLongitude());
                                            MarketModel.setLatitude(location.getLatitude());
                                            databaseReference.child(MarketModel.getId()).setValue(MarketModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context,"Location Modified.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else if(pickUp.equals("store")){
                                            StoreModel = StoreView.EditReference;
                                            StoreView.EditReference=null;
                                            databaseReference = FirebaseDatabase.getInstance().getReference("AllStores");
                                            StoreModel.setLongitude(location.getLongitude());
                                            StoreModel.setLatitude(location.getLatitude());
                                            databaseReference.child(StoreModel.getId()).setValue(StoreModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context,"Location Modified.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else if(pickUp.equals("farm")){
                                            FarmModel = FarmView.EditReference;
                                            FarmView.EditReference = null;
                                            databaseReference = FirebaseDatabase.getInstance().getReference("AllFarms");
                                            FarmModel.setLongitude(location.getLongitude());
                                            FarmModel.setLatitude(location.getLatitude());
                                            databaseReference.child(FarmModel.getId()).setValue(FarmModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context,"Location Modified.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }
                                    catch (Exception ex){
                                        Toast.makeText(context,"Operation Failed, enable gps and refresh page!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }).addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //show message
                    Toast.makeText(context,"Location failed, enable gps and try again",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(context,"Grant Access To GPS",Toast.LENGTH_SHORT).show();
        }
    }
}
