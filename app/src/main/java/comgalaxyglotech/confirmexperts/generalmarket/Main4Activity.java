package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;

public class Main4Activity extends AppCompatActivity{
private RecyclerView mRecyclerView;
private FloatingActionButton addNewItem;
private ExampleAdapter mAdapter;
private RecyclerView.LayoutManager mlayoutManager;
private String marketID;
private String marketName;
private ProgressDialog progressDialog;
private DataClass dataClass = new DataClass();
private DataModel dataModel = new DataModel();
    private EditText searchStringField;
    private ArrayList<itemModel> metricList= new ArrayList<>();
    private Map<String, ExampleItem> exampleList= new HashMap<>();
private TextView titleDisplay;
private itemModel newItem;
private Context context = this;
    int count =0;
private String cst;
private Double low=0.0,high =0.0;
private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        addNewItem = findViewById(R.id.homeBtnx);
        progress = findViewById(R.id.progress);
      //  dataClass.getData();
      //  dataModel.getData();
        searchBarOp();
        basePanelListeners();
        progressDialog= new ProgressDialog(Main4Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        Intent previousActivity=getIntent();
        titleDisplay= findViewById(R.id.itemDisplayMarketName);
        marketID=previousActivity.getStringExtra("thisMarketID");
        marketName=previousActivity.getStringExtra("marketName");

        String title=marketName;
        titleDisplay.setText(title);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        getData();
    }
    private void basePanelListeners(){
        Button home, account, market, store;
        Button gps;
        home = findViewById(R.id.home_floor);
        account = findViewById(R.id.account_floor);
        market = findViewById(R.id.store_floor);
        store = findViewById(R.id.store_ll_floor);
        gps = findViewById(R.id.gps);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
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
        bottomPanel = findViewById(R.id.bottomPanel);
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        stopSearch = findViewById(R.id.stopSearch);
        searchStringField = findViewById(R.id.searchString);
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
    private void listener(){
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setStockList(){
        Query query = FirebaseDatabase.getInstance().getReference("Store_Stock")
                .orderByChild("marketId")
                .equalTo(marketID);//"-L_KE2AXRrREVWhKR07S"
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    private ValueEventListener valueEventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            metricList.clear();
            ArrayList<String>selectedMetric = new ArrayList<>();
            Map<String, itemModel> itemRef = new HashMap<>();
            /*
            * selectedMetric holds a unique collection of common item nameId from a stream of the entire stock
            * the condition attached ensures that only one commonNameId is accepted into the list using the .contain
            * for each new commonNameId, a new ItemModel object is created and held in the itemRef
            * itemRef uses commonNameId as key to reference associated ItemModel. If the commonNameId is not a new
            * intake (i.e. is contained in selectedMetric), the associated ItemModel is referenced and resulting object
            * updated as required
            */
            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NewStockMainModel newStockClass= snapshot.getValue(NewStockMainModel.class);
                    assert newStockClass != null;
                    String commonNameId=newStockClass.getCommonNameId();
                    if(selectedMetric.isEmpty() || !selectedMetric.contains(commonNameId)){
                        selectedMetric.add(newStockClass.getCommonNameId());
                        ExampleItem thisItem= exampleList.get(commonNameId);
                        low=0.0;
                        high=Double.parseDouble(newStockClass.getItemCost());
                        count=1;
                        newItem =new itemModel(thisItem.getItemId(),thisItem.getItemName(),count,low,high,newStockClass.getCommonNameId());
                        itemRef.put(commonNameId, newItem);
                    }
                    else{
                        //count++;
                        newItem = itemRef.get(commonNameId);
                        count = newItem.getNumberOfSeller() + 1;
                        newItem.setNumberOfSeller(count);
                        if(Double.parseDouble(newStockClass.getItemCost()) > newItem.getHighest()){
                            newItem.setLowest(newItem.getHighest());
                            newItem.setHighest(Double.parseDouble(newStockClass.getItemCost()));
                        }
                    }
                }
              metricList.addAll(itemRef.values());
                 adapterOp();
            }
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void getData(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(Map<String, ExampleItem> list) {
                exampleList=list;
                setStockList();
            }
        },"Item","itemName","");
    }
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, String> itemList=new ArrayList<>();
                Map<String, ExampleItem> itm = new HashMap<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ExampleItem newItemClass= snapshot.getValue(ExampleItem.class);
                        itm.put(newItemClass.getItemId(),newItemClass);
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
            Query query = FirebaseDatabase.getInstance().getReference(reference)
                    .orderByChild(OrderBy);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else{
            Query query = FirebaseDatabase.getInstance().getReference(reference)
                    .orderByChild(OrderBy)
                    .equalTo(id);
            query.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private interface FirebaseCallback{
        void onCallback (Map<String, ExampleItem> list);
    }
    private void adapterOp(){
        mAdapter = new ExampleAdapter(metricList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                progressDialog.show();
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main6Activity");
                intent.putExtra("commonNameId",metricList.get(position).getCommonId());
                dataModel.getSmarterData(progressDialog,context,intent);
                // mAdapter.notifyItemChanged(position);
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

}
