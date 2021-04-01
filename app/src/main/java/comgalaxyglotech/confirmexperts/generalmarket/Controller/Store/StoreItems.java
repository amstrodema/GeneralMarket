package comgalaxyglotech.confirmexperts.generalmarket.Controller.Store;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class StoreItems extends AppCompatActivity {
    public static NewStockMainModel EditReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_items);
    }

}
