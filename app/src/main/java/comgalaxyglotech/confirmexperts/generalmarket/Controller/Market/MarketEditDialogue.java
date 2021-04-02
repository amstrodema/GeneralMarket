package comgalaxyglotech.confirmexperts.generalmarket.Controller.Market;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.newMarketModel;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 11/22/2019.
 */

public class MarketEditDialogue  extends AppCompatDialogFragment {
    public static Context context;
    public static Activity activity;
    public static newMarketModel MarketModel;
    private EditText marketName;
    private EditText marketDescription, marketLocation;
    private Spinner marketTradeFreq,marketPrevTradeDay;
    private RadioButton on, off;
    private TextView activityTitle,inMarketLabel;
    private ProgressDialog progressDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.new_market_dialog, null);
        activityTitle = view.findViewById(R.id.activityTitle);
        inMarketLabel = view.findViewById(R.id.inMarketLabel);
        on =view.findViewById(R.id.locationYes);
        off =view.findViewById(R.id.locationNo);
        marketName =view.findViewById(R.id.newMarketName);
        marketDescription =view.findViewById(R.id.newMarketDescription);
        marketLocation =view.findViewById(R.id.newMarketLocation);
        marketTradeFreq =view.findViewById(R.id.marketFreqSpinner);
        marketPrevTradeDay =view.findViewById(R.id.lastMarketDaySpinner);

        String title;
        title="EDIT MARKET DETAILS";
        activityTitle.setText(title);
        inMarketLabel.setVisibility(View.GONE);
        MarketModel = Main3Activity.EditModel;
        Main3Activity.EditModel=null;
        marketName.setText(MarketModel.getMarketName());
        marketDescription.setText(MarketModel.getMarketDescription());
        marketLocation.setText(MarketModel.getLocation());
        on.setVisibility(View.GONE);
        off.setVisibility(View.GONE);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Edit Market", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog= new ProgressDialog(context);
                        progressDialog.setMessage("Saving...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        MarketModel.setMarketName(marketName.getText().toString());
                        MarketModel.setMarketDescription(marketDescription.getText().toString());
                        MarketModel.setLocation(marketLocation.getText().toString());
                        MarketModel.setMarketTradeFreq(marketTradeFreq.getSelectedItem().toString());
                        MarketModel.setMarketLastTradingDay(GetPrevMarketDate(marketPrevTradeDay.getSelectedItem().toString()));
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Market");
                        databaseReference.child(MarketModel.getId()).setValue(MarketModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              //  activity.finish();
                               // Intent intent = new Intent(context, Main3Activity.class);
                              //  intent.putExtra("marketID",MarketModel.getId());
                                progressDialog.dismiss();
                                Toast.makeText(context,"Market Edited Successfully", Toast.LENGTH_SHORT).show();
                               // startActivity(intent);
                             }
                        });

                    }
                });
        return builder.create();
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
}
