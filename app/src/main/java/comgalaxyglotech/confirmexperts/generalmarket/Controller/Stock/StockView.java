package comgalaxyglotech.confirmexperts.generalmarket.Controller.Stock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class StockView extends AppCompatActivity {
    private TextView itemName, sellerName, desc, qty, price;
    private ImageView itemImage;
    private NewStockMainModel data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_view);
        data = StoreItems.EditReference;
        SetUI();
        LoadComponents();
    }

    private void LoadComponents(){
        itemName.setText(data.getAdvertName());
        sellerName.setText(data.getStoreId());
        desc.setText(data.getItemDesc());
        qty.setText(data.getItemQty());
        price.setText(data.getItemCost());
    }

    private void SetUI(){
        itemImage = findViewById(R.id.itemImage);
        itemName = findViewById(R.id.itemName);
        sellerName = findViewById(R.id.sellerName);
        desc = findViewById(R.id.description);
        price = findViewById(R.id.price);
        qty = findViewById(R.id.qty);
    }
}