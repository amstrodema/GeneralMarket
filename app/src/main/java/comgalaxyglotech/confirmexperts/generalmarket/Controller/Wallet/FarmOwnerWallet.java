package comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmOwnerHome;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmOwnerInsight;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm.FarmOwnerTrades;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class FarmOwnerWallet extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_owner_wallet);
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
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerWallet.this, FarmOwnerHome.class);
                intent.putExtra("storeId",storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerWallet.this, FarmOwnerTrades.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerWallet.this,FarmOwnerWallet.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmOwnerWallet.this, FarmOwnerInsight.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creator",creatorId);
                startActivity(intent);
            }
        });
    }
}
