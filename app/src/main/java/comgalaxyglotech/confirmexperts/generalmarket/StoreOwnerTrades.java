package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StoreOwnerTrades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_trades);
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
                Intent intent = new Intent(getApplicationContext(),Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerTrades.this,StoreOwnerHome.class);
                intent.putExtra("storeId",storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerTrades.this,StoreOwnerTrades.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerTrades.this,StoreOwnerWallet.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerTrades.this,StoreOwnerInsight.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
    }
}
