package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;

public class StoreView extends AppCompatActivity {
    private ModelClass modelClass = new ModelClass();
    private String creator, storeId;
    //dont delete... important as used in all reference to edit
    public static StoreMainModel EditReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(modelClass.userLoggedIn()){
            Intent intent = getIntent();
            String userId =modelClass.getCurrentUserId();
            creator =intent.getStringExtra("creator") ;
            storeId = intent.getStringExtra("storeId");
            if(userId.equals(creator)){
               setContentView(R.layout.activity_store_owner_home);
               basePanelListeners();
            }
            else {
                //send user to public page
                setContentView(R.layout.activity_store_view);
            }
        }
        else{
            //send user to public page
            setContentView(R.layout.activity_store_view);
        }
        //setContentView(R.layout.activity_store_view);

        //startActivity(new Intent(StoreView.this, TabzStoreOwner.class));

       // fragment = new FragmentStoreHome();
       // FragmentManager fragmentManager = getSupportFragmentManager();
       // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    }
    private void basePanelListeners(){
        Button home, trades, wallet, insight;
        home = findViewById(R.id.home_floor);
        trades = findViewById(R.id.account);
        wallet = findViewById(R.id.notification);
        insight = findViewById(R.id.setting);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreView.this,StoreOwnerHome.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreView.this,StoreOwnerTrades.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreView.this,StoreOwnerWallet.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreView.this,StoreOwnerInsight.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
    }
}
