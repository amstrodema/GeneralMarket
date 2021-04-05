package comgalaxyglotech.confirmexperts.generalmarket.Controller.Stock;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Stock.NewStock_StoreAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Example.ExampleItem;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Favorite.ModelFavey;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction.Model_Transaction;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Favorite.ProcessFavourites;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Process.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragStock_picks_store extends Fragment {

    private View view;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FloatingActionButton newItem;
    private Button noData;
    private RecyclerView mRecyclerView;
    private NewStock_StoreAdapter mAdapter;
    private ArrayList<NewStockMainModel>storeItemKeep=new ArrayList<>();
    private ArrayList<NewStockMainModel>storeItemRaw=new ArrayList<>();
    private ArrayList<ModelFavey>faveys=new ArrayList<>();
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<NewStockDisplayModel> storeList= new ArrayList<>();
    private String dataType ="";
    private Context context;
    private Activity activity;
    private ProgressBar progress;
    private ProgressDialog progressDialog;
    private ModelClass modelClass = new ModelClass();
    public FragStock_picks_store() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stock_picks, container, false);

        context = getContext();
        activity = getActivity();
        setUI();
        //setStoreList();
        Intent prevIntent = activity.getIntent();
        try{
            dataType = prevIntent.getStringExtra("dataType");
        }
        catch (Exception e)
        {
        }
        if(dataType==null){
            dataType="deal";
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        //recycler
        mlayoutManager =new LinearLayoutManager(activity);
        mRecyclerView = view.findViewById(R.id.categoryItemsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        return view;
    }
    private void setUI(){
        progress = view.findViewById(R.id.progress);
      //  newItem = view.findViewById(R.id.homeBtnx);
      noData = view.findViewById(R.id.noData);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFav();
    }
    private void setStoreList(){
        readStockData(new FirebaseCallbackStock() {
            @Override
            public void onCallback( ArrayList<NewStockMainModel> stockModel) {
                storeItemKeep.clear();
                storeList.clear();
                if(stockModel != null){
                    storeItemRaw = stockModel;
                }

                setFarmList();
            }
        },"Store_Stock","storeId","");
    }
    private void setFarmList(){
        readStockData(new FirebaseCallbackStock() {
            @Override
            public void onCallback( ArrayList<NewStockMainModel> stockModel) {


                if(stockModel == null && storeItemRaw.size()<1){
                    noData.setVisibility(View.VISIBLE);
                }
                else{
                    noData.setVisibility(View.INVISIBLE);
                    storeItemRaw.addAll(stockModel);
                }
                progress.setVisibility(View.GONE);
                filterData(storeItemRaw);
            }
        },"Farm_Stock","storeId","");
    }
    private void readStockData(final FirebaseCallbackStock firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<NewStockMainModel> stockData=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NewStockMainModel newData= snapshot.getValue(NewStockMainModel.class);
                        stockData.add(newData);
                    }
                    firebaseCallback.onCallback(stockData);
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
    private interface FirebaseCallbackStock{
        void onCallback (ArrayList<NewStockMainModel> stockModel);
    }
   private void filterData(ArrayList<NewStockMainModel> stockModels){
       for (NewStockMainModel newPrice: stockModels) {
         /*  try{
               if(newPrice.getIsEditorsPick().equals("True") && !dataType.equals("Fav")){
                   if((modelClass.userLoggedIn() && modelClass.getCurrentUserId().equals(newPrice.getCreatorId())) || ModelClass.admin){
                       NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());

                       //store identical data as field, not to be altered or reassined and must appear wit d its twin variable
                       storeItemKeep.add(newPrice);
                       //displays for editors picks
                       storeList.add(newPriceDisplay);
                   }
                   else{
                       if(!newPrice.isBan()){
                           NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());
                           //store identical data as field, not to be altered or reassined and must appear wit d its twin variable
                           storeItemKeep.add(newPrice);
                           //displays for editors picks
                           storeList.add(newPriceDisplay);
                       }
                   }
                   progress.setVisibility(View.GONE);
                   noData.setVisibility(View.GONE);
               }
           }
           catch (Exception e)
           {

           }*/
           try{
               String isFvor = isFav(newPrice.getId());
               //displays for fav
               if(isFvor.equals("True") && dataType.equals("Fav")){
                   if((modelClass.userLoggedIn() && modelClass.getCurrentUserId().equals(newPrice.getCreatorId())) || ModelClass.admin){
                       NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());
                       //store identical data as field, not to be altered or reassined and must appear wit d its twin variable
                       storeItemKeep.add(newPrice);
                       //displays for editors picks
                       storeList.add(newPriceDisplay);
                   }
                   else{
                       if(!newPrice.isBan()){
                           NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());
                           //store identical data as field, not to be altered or reassined and must appear wit d its twin variable
                           storeItemKeep.add(newPrice);
                           //displays for editors picks
                           storeList.add(newPriceDisplay);
                       }
                   }
                   progress.setVisibility(View.GONE);
                   noData.setVisibility(View.GONE);
               }
           }
           catch (Exception e){

           }



       }

       adapterOp();
   }
    private void adapterOp(){
        mAdapter = new NewStock_StoreAdapter(storeList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new NewStock_StoreAdapter.OnPriceClickListener() {
            @Override
            public void onItemLongClick(int position) {
                if(ModelClass.admin) {
                    StoreItems.EditReference = storeItemKeep.get(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Remove From Hot Deal?");
                    alertDialog.setMessage("Deactivates for front page");
                    // alertDialog.setIcon(R.drawable.contact);
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StoreItems.EditReference.setIsEditorsPick("False");
                            databaseReference = FirebaseDatabase.getInstance().getReference("Store_Stock");
                            databaseReference.child(StoreItems.EditReference.getId()).setValue(StoreItems.EditReference).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Hot Deal Removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onStoreMetaClick(int position) {
                StoreItems.EditReference = storeItemKeep.get(position);
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreView");
                    intent.putExtra("storeId",storeList.get(position).getStoreId());
                    //intent.putExtra("metric",metricLabel);
                    startActivity(intent);

            }

            @Override
            public void onCartClick(int position) {

            }

            @Override
            public void onFavClick(int position, ImageView favBtn) {
                if(modelClass.userLoggedIn()){
                    StoreItems.EditReference = storeItemKeep.get(position);
                    String itemId = StoreItems.EditReference.getId();
                    String userId = modelClass.getCurrentUserId();
                    ProcessFavourites processFavourites= new ProcessFavourites();
                    processFavourites.setFav(userId,itemId, favBtn);
                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBuyClick(int position) {
                if(modelClass.userLoggedIn()){
                    StoreItems.EditReference = storeItemKeep.get(position);
                    final Model_Transaction modelTransaction= new Model_Transaction();
                    modelTransaction.setItmQty(1.0);
                    modelTransaction.setBuyerId(modelClass.getCurrentUserId());
                    modelTransaction.setPayerId(modelClass.getCurrentUserId());
                    modelTransaction.setItemId(StoreItems.EditReference.getId());
                    double cost = Double.parseDouble(StoreItems.EditReference.getItemCost());
                    modelTransaction.setItemCost(cost);
                    modelTransaction.setStoreId(StoreItems.EditReference.getStoreId());
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Purchase Alert!!!");
                    alertDialog.setMessage("You are about to purchase "+StoreItems.EditReference.getItemQty()+" "+StoreItems.EditReference.getAdvertName()+"\nCost: ₦"+cost);
                    alertDialog.setIcon(R.drawable.transaction_pics);
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("processing...");
                            progressDialog.show();
                                ProcessWallet processWallet = new ProcessWallet();
                                processWallet.getDeductData(modelClass.getCurrentUserId(),progressDialog,context,modelTransaction);

                        }
                    });
                    alertDialog.show();
                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBanClick(int position) {
                StoreItems.EditReference = storeItemKeep.get(position);
                    ModelClass.banStoreStock( StoreItems.EditReference);
            }

            @Override
            public void onEditClick(int position) {
                StoreItems.EditReference = storeItemKeep.get(position);
                if(modelClass.userLoggedIn()){
                        if(modelClass.getCurrentUserId().equals(StoreItems.EditReference.getCreatorId())){
                            Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Stock.NewStockActivity");
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(context,"Contact store owner",Toast.LENGTH_SHORT).show();
                        }

                }
                else{
                    Toast.makeText(context,"Log In Required.",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private String isFav(String itemId){
        if (modelClass.userLoggedIn() && faveys != null){
            for(ModelFavey fav : faveys ){
                if(fav.getItemId().equals(itemId)){
                    return "True";
                }
            }
        }
        return "False";
    }
    private void loadFav(){
        if (modelClass.userLoggedIn() && dataType.equals("Fav")){
            readData(new FirebaseCallback() {
                @Override
                public void onCallback( ArrayList<ModelFavey> favModel) {
                    faveys = new ArrayList<>(favModel);
                    getItemData();
                }
            },"Favey","userId",modelClass.getCurrentUserId());
        }
        else if (!dataType.equals("Fav")){
            getItemData();
        }
        else{
            Toast.makeText(context,"Log In Required", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModelFavey> favModelz=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ModelFavey newData= snapshot.getValue(ModelFavey.class);
                        favModelz.add(newData);
                    }
                    firebaseCallback.onCallback(favModelz);
                }
                else{
                    getItemData();
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
        void onCallback(ArrayList<ModelFavey> favModel);
    }

//fetc items details
    private void getItemData(){
        readItemData(new FirebaseItemCallback() {
            @Override
            public void onCallback(Map<String, String> list, ArrayList<String> list2, Map<String, String> list3, Map<String, String> listCategory, ArrayList<String> farm_ItemIdData) {
                DataClass.itemNameToId =list;
                DataClass.itemIdData=list2;
                DataClass.itemIdToName = list3;
                DataClass.itemCategoryData= listCategory;
                DataClass.farmItemIdData =farm_ItemIdData;
                getStoreData();
            }

        },"Item","itemName","");
    }

    //this methods access the required query resources in a listener and runs the query from getData
    private void readItemData(final FirebaseItemCallback firebaseCallback, String reference, String OrderBy, String id){
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
    private interface FirebaseItemCallback{
        void onCallback (Map<String, String> list, ArrayList<String> list2,Map<String, String> list3,Map<String, String> listCategory,ArrayList<String> farmItemIdData);
    }

    public void getStoreData(){
        readStoreData(new FirebaseStoreCallback() {
            @Override
            public void onCallback(Map<String, String> list) {
                DataModel.displayData=list;
                setStoreList();
            }
        },"AllStores","storeName","");
    }

    //gets records of stores
    private void readStoreData(final FirebaseStoreCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                Map<String, String> itm = new HashMap<String, String>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        StoreMainModel newData= snapshot.getValue(StoreMainModel.class);
                        //ExampleItem thisData= new ExampleItem(,,newData.getMarketId(),newData.getCreatorId());
                        itm.put(newData.getId(),newData.getStoreName()) ;
                        // ExampleItem itm =new ExampleItem(newData.getItemName(),newData.getItemId(),newData.getMarketId(),newData.getCreatorId());
                        //String itmId =newData.getItemName();
                        //itemList.add(itm);
                    }
                    firebaseCallback.onCallback(itm);
                }
                else{
                    firebaseCallback.onCallback(itm);
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

    private interface FirebaseStoreCallback{
        void onCallback (Map<String, String> list);
    }
}
