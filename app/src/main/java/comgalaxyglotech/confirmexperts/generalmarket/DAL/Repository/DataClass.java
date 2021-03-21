package comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import comgalaxyglotech.confirmexperts.generalmarket.ExampleItem;
import comgalaxyglotech.confirmexperts.generalmarket.Trash.RecipeDataClass;

/**
 * Created by ELECTRON on 09/26/2019.
 */

public class DataClass {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    //Name key to ID
    public static Map<String, String> itemNameToId = new HashMap<String, String>();
    //ID key to Name
    public static Map<String, String> itemIdToName = new HashMap<String, String>();
    public static Map<String, String> itemCategoryData = new HashMap<String, String>();
    public static ArrayList<String> itemIdData = new ArrayList<>();
    public static ArrayList<String> farmItemIdData = new ArrayList<>();
    private ProgressDialog ProgressDialog;
    private Context Context;
    private Activity Activity;
    private Intent Intent;
    private boolean recipe =false;
    public void getRecipePerequsite( final Context context,  final Intent intent){
        Context =context;
        Intent =intent;
        getData();
        recipe=true;
    }
    public void getSmartData(final ProgressDialog progressDialog, final Context context, final Activity activity, final Intent intent){
        ProgressDialog = progressDialog;
        Context =context;
        Activity =activity;
        Intent =intent;
        getData();
    }
    public void getSmarterData(final ProgressDialog progressDialog,final Context context, final Intent intent){
        ProgressDialog = progressDialog;
        Context =context;
        Intent =intent;
        getData();
    }
    //sets the parameter for the query
    public void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(Map<String, String> list,ArrayList<String> list2,Map<String, String> list3,Map<String, String> listCategory,ArrayList<String> farm_ItemIdData) {
                itemNameToId =list;
                itemIdData=list2;
                itemIdToName = list3;
                itemCategoryData= listCategory;
                farmItemIdData =farm_ItemIdData;
                if(recipe)
                {
                    //this is a recipe call
                    RecipeDataClass recipeDataClass = new RecipeDataClass();
                    recipeDataClass.getData(Context,Intent);
                }
                else{
                    if(ProgressDialog != null)
                    {
                         if(Activity != null)
                         {
                             Activity.finish();
                         }
                        ProgressDialog.dismiss();
                        Context.startActivity(Intent);
                    }
                }
            }
        },"Item","itemName","");
    }

    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> itm = new HashMap<String, String>();
                Map<String, String> itm2 = new HashMap<String, String>();
                Map<String, String> listCat = new HashMap<String, String>();
                ArrayList<String> itemIdList=new ArrayList<>();
                ArrayList<String> farmItemIdDat = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ExampleItem newData= snapshot.getValue(ExampleItem.class);
                        itm.put(newData.getItemName(),newData.getItemId()) ;
                        itm2.put(newData.getItemId(),newData.getItemName()) ;
                        listCat.put(newData.getItemName(),newData.getNewItemClass()) ;
                        String itmId =newData.getItemName();
                        if(newData.getNewItemClass().equals("Farm Products")){
                            farmItemIdDat.add(itmId);
                        }
                        else
                        itemIdList.add(itmId);
                    }
                    firebaseCallback.onCallback(itm,itemIdList,itm2,listCat,farmItemIdDat);
                }
                else{
                    firebaseCallback.onCallback(itm,itemIdList,itm2,listCat,farmItemIdDat);
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
    private interface FirebaseCallback{
        void onCallback (Map<String, String> list, ArrayList<String> list2,Map<String, String> list3,Map<String, String> listCategory,ArrayList<String> farmItemIdData);
    }
    public String timeDifference(long diff) {
        String theTime="";
        try {

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if(diffDays > 0)
            {
                if(diffDays > 1)
                {
                    theTime=diffDays+" days ";
                }
                else{
                    theTime=diffDays+" day ";
                }
               // theTime+="ago";
                //return theTime;
            }
            if(diffHours > 0)
            {
                if(diffHours > 1)
                {
                    theTime+=diffHours+" hrs ";
                }
                else{
                    theTime+=diffHours+" hr ";
                }
              //  theTime+="ago";
               // return theTime;
            }
            if(diffMinutes > 0)
            {
                if(diffMinutes > 1)
                {
                    theTime+=diffMinutes+" mins ";
                }
                else{
                    theTime+=diffMinutes+" min ";
                }
               // theTime+="ago";
                //return theTime;
            }
            if(diffSeconds > 0)
            {
                if(diffSeconds > 1)
                {
                    theTime+=diffSeconds+" secs ";
                }
                else{
                    theTime+=diffSeconds+" sec ";
                }
               // theTime+="ago";
              //  return theTime;
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return theTime;
    }

    public String timeCheck(String open){
        String [] mark = open.split(":");
        if(Integer.parseInt(mark[0]) < 12)
        {
            open +=" AM";
        }
        else if (Integer.parseInt(mark[0]) == 12)
        {
            open +=" PM";
        }
        else
        {
            int pp = Integer.parseInt(mark[0]) - 12;
            try{
                open =pp+":"+mark[1]+" PM";
            }
            catch (Exception e){
                open =pp+" PM";
            }
        }
        return open;
    }
}
