package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Stocks extends AppCompatActivity {
TextView generalFoodStuff;
private DataModel dataModel = new DataModel();
private ProgressDialog progressDialog;
private Context context= this;
private ImageView advert1,advert2,advert3,advert4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();//Main_Page_Two
        storageReference.child("Advert").child("Category_List_One").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert1);
            }
        });
        storageReference.child("Advert").child("Category_List_Two").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert2);
            }
        });
        storageReference.child("Advert").child("Category_List_Three").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert3);
            }
        });
        storageReference.child("Advert").child("Category_List_Four").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(advert4);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Data");
        setUI();
        basePanelListeners();
      //  listeners();
    }
    public void listeners(View v){
        //listens to onclick events from buttons on stock
        final TextView generalBtn = (TextView) v;
        String cat = generalBtn.getHint().toString();
        Intent intent = new Intent(Stocks.this, StockLanding.class);
        intent.putExtra("category",cat);
        startActivity(intent);
    }
    public void farmListeners(View v){
        //listens to onclick events from buttons on farmstock
         /*  final Button generalBtn = (Button) v;
     progressDialog.show();
        String cat = generalBtn.getHint().toString();
        Intent intent = new Intent(Stocks.this, StockLanding.class);
        intent.putExtra("category",cat);
        dataModel.getDoubleFarmData(progressDialog,context,intent);*/
        Intent intent = new Intent(context, FarmItems.class);
        intent.putExtra("farmId","");
        startActivity(intent);
       // dataModel.getDoubleFarmData(progressDialog, context,intent);
    }
    private void setUI(){
        advert1 = findViewById(R.id.advert1);
        advert2 = findViewById(R.id.advert2);
        advert3 = findViewById(R.id.advert3);
        advert4 = findViewById(R.id.advert4);
        generalFoodStuff = findViewById(R.id.catGeneralFoodstuff);
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
                Intent intent =  new Intent(Stocks.this,TransitionAdvertDisplay.class);
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
                Intent intent =  new Intent(Stocks.this,TransitionAdvertDisplay.class);
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

}
