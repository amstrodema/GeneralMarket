package comgalaxyglotech.confirmexperts.generalmarket;


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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Stock.NewStock_FarmAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Cart.CartPurchaseQuantityDialogue;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart.CartModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockDisplayModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragStockFarm extends Fragment{

    private View view;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FloatingActionButton newItem;
    private Button noData;
    private RecyclerView mRecyclerView;
    private NewStock_FarmAdapter mAdapter;
    private EditText searchStringField;
    private ArrayList<NewStockMainModel>storeItemKeep=new ArrayList<>();
    public static ArrayList<CartModel>storeCart=new ArrayList<>();
    private ArrayList<ModelFavey>faveys=new ArrayList<>();
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<NewStockDisplayModel> storeList= new ArrayList<>();
    private String farmId ="";
    private Context context;
    private Activity activity;
    private ProgressBar progress;
    private ProgressDialog progressDialog;
    private ModelClass modelClass = new ModelClass();
    private ImageView cartView;
    private SwipeRefreshLayout swiper;
    public FragStockFarm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stock, container, false);

        context = getContext();
        activity = getActivity();
        setUI();
        //setStoreList();
        basePanelListeners();
        searchBarOp();
        Intent prevIntent = activity.getIntent();
        try{
            farmId = prevIntent.getStringExtra("farmId");
        }
        catch (Exception e)
        {
        }
        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Cart.CartInvoice");
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        //recycler
        mlayoutManager =new LinearLayoutManager(activity);
        mRecyclerView = view.findViewById(R.id.categoryItemsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        return view;
    }
    private void setUI(){
        progress = view.findViewById(R.id.progress);
        newItem = view.findViewById(R.id.homeBtnx);
        noData = view.findViewById(R.id.noData);
        cartView = view.findViewById(R.id.cartView);
        swiper  = view.findViewById(R.id.swiper);
    }


    private void setStoreList(){
        if(farmId.isEmpty()) {
            Query query = FirebaseDatabase.getInstance().getReference("Farm_Stock")
                    .orderByChild("advertName");
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            Query query = FirebaseDatabase.getInstance().getReference("Farm_Stock")
                    .orderByChild("storeId")
                    .equalTo(farmId);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                boolean mark = false;
                storeItemKeep.clear();
                storeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NewStockMainModel newPrice= snapshot.getValue(NewStockMainModel.class);
                    //obtains category name
                        storeItemKeep.add(newPrice);
                        assert newPrice != null;
                        //String name, String highest, String lowest, String id
                        if((modelClass.userLoggedIn() && modelClass.getCurrentUserId().equals(newPrice.getCreatorId())) || ModelClass.admin){
                            NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());
                            storeList.add(newPriceDisplay);
                        }
                        else{
                            if(!newPrice.isBan()){
                                NewStockDisplayModel newPriceDisplay=new NewStockDisplayModel(newPrice.getId(),newPrice.getAdvertName(),newPrice.getStoreId(),newPrice.getCategory(),newPrice.getItemDesc(),newPrice.getItemCost(), newPrice.getCreatorId(),newPrice.getRating(),isFav(newPrice.getId()), newPrice.isBan());
                                storeList.add(newPriceDisplay);
                            }
                        }


                        progress.setVisibility(View.GONE);
                        noData.setVisibility(View.GONE);
                    }
                    adapterOp();
                }
            else{
                    noData.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
            //  progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void basePanelListeners(){
        Button home, account, market, store;
        Button gps;
        home = view.findViewById(R.id.home_floor);
        account = view.findViewById(R.id.account_floor);
        market = view.findViewById(R.id.store_floor);
        store = view.findViewById(R.id.store_ll_floor);
        gps = view.findViewById(R.id.gps);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Farm_Transition");
                startActivity(intent);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(),NotificationView.class);
                //  startActivity(intent);
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Market_Transition");
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay");
                intent.putExtra("Location","Store_Transition");
                startActivity(intent);
            }
        });
    }
    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2,bottomPanel;
        bottomPanel = view.findViewById(R.id.bottomPanel);
        topPanel = view.findViewById(R.id.topPanel);
        topPanel2 = view.findViewById(R.id.topPanel2);
        SearchAccess = view.findViewById(R.id.SearchAccess);
        stopSearch = view.findViewById(R.id.stopSearch);
        searchStringField = view.findViewById(R.id.searchString);
        SearchAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show search input and hide bottom panel
                topPanel2.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.GONE);
            }
        });
        stopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide search input and show bottom panel
                searchStringField.setText("");
                topPanel.setVisibility(View.GONE);
                topPanel2.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.VISIBLE);
            }
        });
    }
    public void openDialog(){
        CartPurchaseQuantityDialogue qtyDialog = new CartPurchaseQuantityDialogue();
        qtyDialog.show(getFragmentManager(),"Set Item Quantity");
    }

    @Override
    public void onStart() {
        super.onStart();
        getFarmData();
    }
    DataModel dataModel = new DataModel();
    public void getFarmData(){
        dataModel.readFarmData(new DataModel.FirebaseFarmCallback() {
            @Override
            public void onCallback(Map<String, FarmMainModel> list) {
                DataModel.displayEntireFarmData=list;
                loadFav();
            }
        },"AllFarms","farmName","");
    }
    private void adapterOp(){
        mAdapter = new NewStock_FarmAdapter(storeList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new NewStock_FarmAdapter.OnPriceClickListener() {
            @Override
            public void onItemLongClick(int position) {
            /*    if(ModelClass.admin) {
                    StoreItems.EditReference = storeItemKeep.get(position);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle("Set As Hot Deal?");
                    alertDialog.setMessage("Activates for front page");
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
                            StoreItems.EditReference.setIsEditorsPick("True");
                            databaseReference = FirebaseDatabase.getInstance().getReference("Store_Stock");
                            databaseReference.child(StoreItems.EditReference.getId()).setValue(StoreItems.EditReference).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Editors Pick Set", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alertDialog.show();
                }*/
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onStoreMetaClick(int position) {
                Intent intent = new Intent(getContext(), FarmView.class);
                //storeId is only an identifier for farmId here, used to ensure easy flow btw store and farm wallets
                intent.putExtra("storeId",storeList.get(position).getStoreId());
                intent.putExtra("creator",storeList.get(position).getCreatorID());
                    startActivity(intent);
            }

            @Override
            public void onCartClick(int position) {
                if(modelClass.userLoggedIn()){
                    CartPurchaseQuantityDialogue.thisItem = storeItemKeep.get(position);
                    openDialog();
                }
                else{
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
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
                    modelTransaction.setStoreOwnerId(StoreItems.EditReference.getCreatorId());
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
                            DataClass dataClass = new DataClass();
                            Intent intent = new Intent(context, FarmStock.class);
                            intent.putExtra("farmId",farmId);
                            dataClass.getSmarterData(progressDialog,context,intent);
                           // startActivity(intent);
                        }
                        else{
                            Toast.makeText(context,"Contact Farm Owner",Toast.LENGTH_SHORT).show();
                        }

                }
                else{
                    Toast.makeText(context,"Log In Required.",Toast.LENGTH_SHORT).show();
                }


            }
        });
        searchOperandi();
    }
    private void searchOperandi(){
        searchStringField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Toast.makeText(StoreActivity.this,s,Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        if (modelClass.userLoggedIn()){
            readData(new FirebaseCallback() {
                @Override
                public void onCallback( ArrayList<ModelFavey> favModel) {
                    faveys = new ArrayList<>(favModel);
                    setStoreList();
                }
            },"Favey","userId",modelClass.getCurrentUserId());
        }
        else{
            setStoreList();
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
                    setStoreList();
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
}
