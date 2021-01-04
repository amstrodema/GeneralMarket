package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import co.paystack.android.model.Card;

/**
 * Created by ELECTRON on 02/22/2019.
 */

public class ModelClass {
    public static ArrayList<AllCommodities> AllCommoditesList;
    public static boolean admin;
    public static String noUnknown;
    public static float maxDistance;
    public static boolean distanceRestriction;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public void getPreferences(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pref = preferences.getString("example_text","1000");
        try{
            maxDistance = Float.parseFloat(pref);
        }
        catch (Exception e){
            maxDistance = 100;
        }
        Toast.makeText(context,maxDistance+"::", Toast.LENGTH_SHORT);
        distanceRestriction = preferences.getBoolean("example_switch",false);
        noUnknown = preferences.getString("example_list","1");
    }
    public boolean userLoggedIn()
    {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if (user!=null){
            return true;
        }
        return false;
    }
    public String getCurrentUserId(){
        if (userLoggedIn()){
            FirebaseAuth   firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser   user =firebaseAuth.getCurrentUser();
            return  user.getUid();
        }
        else
            return null;
    }
    public String getCurrentUserMail(){
        if (userLoggedIn()){
            FirebaseAuth  firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser  user =firebaseAuth.getCurrentUser();
            return  user.getEmail();
        }
        else
            return "Guest";
    }
    public String getEditTextValue(EditText editText){
        return  editText.getText().toString();
    }

    public static void banStore(StoreMainModel store){
        DatabaseReference databaseReference;
        if(store.isBan()){
            store.setBan(false);
        }
        else {
            store.setBan(true);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("AllStores");
        databaseReference.child(store.getId()).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    public static void banFarm(FarmMainModel farm){
        DatabaseReference databaseReference;
        if(farm.isBan()){
            farm.setBan(false);
        }
        else {
            farm.setBan(true);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("AllFarms");
        databaseReference.child(farm.getId()).setValue(farm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    public static void banStoreStock(NewStockMainModel storeStock){
        DatabaseReference databaseReference;
        if(storeStock.isBan()){
            storeStock.setBan(false);
        }
        else{
            storeStock.setBan(true);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference("Stock");
        databaseReference.child(storeStock.getId()).setValue(storeStock).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    public static void banFarmStock(NewStockMainModel farmStock){
        DatabaseReference databaseReference;
        if(farmStock.isBan()){
            farmStock.setBan(false);
        }
        else{
            farmStock.setBan(true);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference("FarmStock");
        databaseReference.child(farmStock.getId()).setValue(farmStock).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    public String getNewId(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference.push().getKey();
    }
    public String getDate (){
        DateFormat ddf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date date = new Date();
        return ddf.format(date);
    }


    public boolean isInvoiceExpired(String invoiceDateString){
        SimpleDateFormat ddf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date date = new Date();

        Date invoiceDate= formatDate(invoiceDateString);
        Date currentTime = formatDate(ddf.format(date));

        long longCurrentTime =currentTime.getTime();
        long longInvoiceDate =invoiceDate.getTime();

        long diff = longCurrentTime - longInvoiceDate;
        return isActive(diff);


        //LocalDate localDate = LocalDate.of(date)
        // return "\n \n"+currentTime.before(closeTime)  +"=="+ currentTime.before(openTime)+"\n \n"+DateText(closeTime)+currentTime.before(closeTime)  +"-"+ DateText(openTime)+currentTime.after(openTime)+" ;; "+DateText(closeTime)+currentTime.after(closeTime)  + DateText(openTime)+currentTime.before(openTime) +"::"+DateText(currentTime);
    }
    private Date formatDate(String timeString){
        //convert the string date and time to Date format
        SimpleDateFormat ddf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        //SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        try{
            return ddf.parse(timeString);
        }
        catch (Exception ex){
            return new Date();
        }
    }
    private boolean isActive(long diff){
        //  long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        //returens valid for 1hr difference
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if(diffDays > 0)
            return false;
        else if(diffHours <= 1)
            if((diffHours==1 && diffMinutes<1)|| (diffHours==0) ){
                return true;
            }

        return false;
    }
    private String countDown(long diff){
         long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        //returens valid for 1hr difference
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if(diffDays < 0)
            return "false";

        if(diffDays >=0)
        {

        }
/*
        else if(diffHours <= 1)
            if((diffHours==1 && diffMinutes<1)|| (diffHours==0) ){
                return true;
            }
*/
        return "false";
    }



    public float loadRating(float rating){
        if(rating >= 5000){
            return 5.0f;
        }
        return rating/1000.0f;
    }
    //for copying text to clipboard
    public void copyToClipboard(Context context, String value, String label){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(" Copy",value);
        assert clipboardManager != null;
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context,label,Toast.LENGTH_SHORT).show();
    }
    //copy this to any area needed
    private void getNotification(final String id){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<A_Settings> settings) {
                if(settings != null){

                }
            }
        },"Settings","id","");
    }
    public void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<A_Settings> settings = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        A_Settings set= snapshot.getValue(A_Settings.class);
                        settings.add(set);
                    }
                    firebaseCallback.onCallback(settings);
                }
                else{
                    firebaseCallback.onCallback(null);
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
    public interface FirebaseCallback{
        void onCallback (ArrayList<A_Settings> settings);
    }
    public static PaymentModel paymentModel;
}