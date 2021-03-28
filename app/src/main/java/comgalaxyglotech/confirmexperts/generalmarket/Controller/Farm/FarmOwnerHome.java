package comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet.FarmOwnerWallet;
import comgalaxyglotech.confirmexperts.generalmarket.Main8Activity;
import comgalaxyglotech.confirmexperts.generalmarket.R;

//All Commodities main java file
public class FarmOwnerHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_owner_home);
        basePanelListeners();
    }
    private void basePanelListeners(){
        Button home, store, trades, wallet, insight;
        home = findViewById(R.id.home_floor);
        store = findViewById(R.id.home_store);
        trades = findViewById(R.id.account);
        wallet = findViewById(R.id.notification);
        insight = findViewById(R.id.setting);
        Intent prevIntent = getIntent();
        final String storeId = prevIntent.getStringExtra("storeId");
        final String creatorId = prevIntent.getStringExtra("creatorId");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmView");
                intent.putExtra("storeId",storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerHome.this, FarmOwnerTrades.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerHome.this, FarmOwnerWallet.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerHome.this, FarmOwnerInsight.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
    }
}
