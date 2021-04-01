package comgalaxyglotech.confirmexperts.generalmarket.Controller.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comgalaxyglotech.confirmexperts.generalmarket.Main8Activity;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class StoreOwnerWallet extends AppCompatActivity {
    //this class is very important
    private Context context = this;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_wallet);
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
                Intent intent = new Intent(context, Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StoreOwnerHome.class);
                intent.putExtra("storeId",storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StoreOwnerTrades.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StoreOwnerWallet.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StoreOwnerInsight.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("creatorId",creatorId);
                startActivity(intent);
                finish();
            }
        });
    }
}
