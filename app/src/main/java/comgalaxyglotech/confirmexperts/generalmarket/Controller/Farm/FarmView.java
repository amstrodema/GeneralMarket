package comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet.FarmOwnerWallet;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class FarmView extends AppCompatActivity {
    private static final  String TAG="TabzStoreOwner";
    private ViewPager mViewPager;
    private ModelClass modelClass = new ModelClass();
    private String creator, farmId;
    //dont delete... important as used in all reference to edit
    //called from farm
    public static FarmMainModel EditReference;
    public static FarmMainModel EditReferenceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_view);
    }
    private void basePanelListeners(){
        Button home, store, trades, wallet, insight;
        home = findViewById(R.id.home_floor);
        store = findViewById(R.id.home_store);
        trades = findViewById(R.id.account);
        wallet = findViewById(R.id.notification);
        insight = findViewById(R.id.setting);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmView");
                intent.putExtra("storeId",farmId);
                intent.putExtra("creator",creator);
                startActivity(intent);
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmView.this, FarmOwnerTrades.class);
                intent.putExtra("storeId", farmId);
                startActivity(intent);
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmView.this, FarmOwnerWallet.class);
                intent.putExtra("storeId", farmId);
                startActivity(intent);
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmView.this, FarmOwnerInsight.class);
                intent.putExtra("storeId", farmId);
                startActivity(intent);
            }
        });
    }
}
