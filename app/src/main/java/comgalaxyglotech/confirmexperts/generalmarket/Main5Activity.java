package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
*/
public class Main5Activity extends AppCompatActivity implements  newMetricDialogue.newMetricDialogListener {
   // AdView mAdview;
    DatabaseReference databaseReference;
    String itemID,itemName;
    private RecyclerView mRecyclerView;
    private MetricAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<MetricDisplayModel> metricList= new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        progressDialog=new ProgressDialog(Main5Activity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        //intent value getter
        Intent previousActivity=getIntent();
        itemID=previousActivity.getStringExtra("itemId");
        itemName=previousActivity.getStringExtra("itemName");
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.metricRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MetricAdapter(metricList);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MetricAdapter.OnMetricItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main6Activity");
                intent.putExtra("metricID",metricList.get(position).getId());
                String metricLabel=metricList.get(position).getMetric().toUpperCase()+" of "+itemName.toUpperCase();
                intent.putExtra("metric",metricLabel);
                 startActivity(intent);
            }
        });
        setMetricList();
    /*    MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        mAdview =findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);*/
    }
    public void openDialog(){
        newMetricDialogue itemDialog = new newMetricDialogue();
        itemDialog.show(getSupportFragmentManager(),"Add New Metric");
    }
    public void applyTexts(String id, String itemId, String metric, String creatorID, int quantity) {
       ModelClass model = new ModelClass();
        if(model.userLoggedIn())
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("Metric");
             id = databaseReference.push().getKey();
            creatorID=model.getCurrentUserId();
            itemId=itemID;
            MetricMainModel newMetric = new MetricMainModel(id,itemId,metric,creatorID,quantity);
            databaseReference.child(id).setValue(newMetric);
            Toast.makeText(Main5Activity.this,"Metric Created Successfully", Toast.LENGTH_SHORT).show();
            setMetricList();
        }
        else {
            Toast.makeText(Main5Activity.this,"Please Log In", Toast.LENGTH_SHORT).show();
        }
    }
    private void setMetricList(){
        Query query = FirebaseDatabase.getInstance().getReference("Metric")
                .orderByChild("itemID")
                .equalTo(itemID);
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            metricList.clear();
            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MetricMainModel newMetric= snapshot.getValue(MetricMainModel.class);
                    MetricDisplayModel newMetricDisplay=new MetricDisplayModel(itemName,newMetric.getQuantity()+newMetric.getMetric(),"0","0","0",newMetric.getId());
                    metricList.add(newMetricDisplay);
                }
                mAdapter.notifyDataSetChanged();
            }
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
