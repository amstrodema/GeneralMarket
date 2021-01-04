package comgalaxyglotech.confirmexperts.generalmarket;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ELECTRON on 02/10/2019.
 */

public class marketDialog extends AppCompatDialogFragment {
    public static boolean marketEdit;
    public static Context context;
    public static newMarketModel MarketModel;
    private EditText marketName;
    private EditText marketDescription, marketLocation;
 //   private marketDialogListener listener;
    private Spinner marketTradeFreq,marketPrevTradeDay;
    private RadioButton on, off;
    private TextView activityTitle;
    marketDialogListener listener;
    private DatabaseReference databaseReference;
    //   private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 60 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private ModelClass modelClass = new ModelClass();
    private double latitude, longitude;
    public static String newMarketName;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_market_dialog,null);
        activityTitle = view.findViewById(R.id.activityTitle);
        on =view.findViewById(R.id.locationYes);
        off =view.findViewById(R.id.locationNo);
        marketName =view.findViewById(R.id.newMarketName);
        marketDescription =view.findViewById(R.id.newMarketDescription);
        marketLocation =view.findViewById(R.id.newMarketLocation);
        marketTradeFreq =view.findViewById(R.id.marketFreqSpinner);
        marketPrevTradeDay =view.findViewById(R.id.lastMarketDaySpinner);
        //picks name as defined already in account
        marketName.setText(newMarketName);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add Market", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mktName,  mktDesc,  mktLoc,  mktNxtTradeDay,  mktPrevTradeday, creatorId;
                        boolean isAllowLocation= false;
                        if(on.isChecked()){
                            isAllowLocation= true;
                        }
                        mktName = marketName.getText().toString();
                        mktDesc = marketDescription.getText().toString();
                        mktLoc= marketLocation.getText().toString();
                        mktNxtTradeDay= marketTradeFreq.getSelectedItem().toString();
                        mktPrevTradeday= marketPrevTradeDay.getSelectedItem().toString();
                        ModelClass newModel=new ModelClass();
                        creatorId=newModel.getCurrentUserId();
                        applyTexts(mktName,  mktDesc,  mktLoc,  mktNxtTradeDay,  mktPrevTradeday,creatorId,isAllowLocation);
                    }
                });
        return builder.create();
    }
    private void applyTexts(final String mktName, final String mktDesc, final String mktLoc, final String mktNxtTradeDay, final String mktPrevTradeday, final String creatorId, final boolean allowLocation) {
        context = getContext();
        if(modelClass.userLoggedIn())
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("Market");
            final String id = databaseReference.push().getKey();
            final FusedLocationProviderClient client =
                    LocationServices.getFusedLocationProviderClient(context);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Get the last known location
                client.getLastLocation()
                        .addOnCompleteListener( new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                // ...
                                //  Task<Location>
                                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        try{
                                            if(allowLocation){
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
                                            }
                                            else{
                                                latitude = 0;
                                                longitude = 0;
                                            }
                                            newMarketModel newMarket = new newMarketModel(mktName,mktDesc,mktLoc,mktNxtTradeDay,GetPrevMarketDate(mktPrevTradeday),id,creatorId,5.0f,longitude,latitude);
                                            databaseReference.child(id).setValue(newMarket).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context,"Market Created Successfully", Toast.LENGTH_SHORT).show();
                                                listener.launchStore(id);
                                                }
                                            });
                                            // setMarketList();
                                        }
                                        catch (Exception ex){
                                            Toast.makeText(context,"Market Not Saved, on GPS", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //show message
                        Toast.makeText(context,"Location failed, enable gps and try again!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        else {
            Toast.makeText(context,"Please Log In", Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(Main2Activity.this,username+" "+password, Toast.LENGTH_SHORT).show();
    }
    private String GetPrevMarketDate(String lastMarketDay){
        String nextTradeDay="";
        //last trade day
        String []  lastMarketDayOptions= new String[]{"Today","Yesterday","2 days ago","3 days ago","4 days ago","5 days ago","6 days ago"};
        if(lastMarketDayOptions[0].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(0);
            // nextTradeDay = ":" +checkFreq(marketFreq);
        }
        else if(lastMarketDayOptions[1].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(1);
        }
        else if(lastMarketDayOptions[2].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(2);
        }
        else if(lastMarketDayOptions[3].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(3);
        }
        else if(lastMarketDayOptions[4].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(4);
        }
        else if(lastMarketDayOptions[5].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(5);
        }
        else if(lastMarketDayOptions[6].equals(lastMarketDay))
        {
            nextTradeDay = DayMinus(6);
        }
        else{
            nextTradeDay = DayMinus(0);
            //if day is unknown, return current day
        }
        String [] timeSplit = nextTradeDay.split(" ");
        nextTradeDay =timeSplit[0]+" "+"00:00:00";
        return nextTradeDay;
    }
    private String DayMinus(int dayMinus){
        DateFormat ddf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(ddf.parse(ddf.format(date)));
        } catch (ParseException e) {
        }
        c.add(Calendar.DAY_OF_MONTH,-dayMinus);
        return ddf.format(c.getTime()).toString();
    }
    public interface marketDialogListener
    {
        void launchStore(String mktId);
    }
   /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (marketDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
            "must implement marketDialogListener");
        }
    }*/
}
