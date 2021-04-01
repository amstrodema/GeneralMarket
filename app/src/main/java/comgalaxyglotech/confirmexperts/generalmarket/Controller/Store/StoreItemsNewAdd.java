package comgalaxyglotech.confirmexperts.generalmarket.Controller.Store;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreItemMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class StoreItemsNewAdd extends AppCompatActivity {
    private RadioButton limited, unlimited, specify;
    private CheckBox priceRange;
    private  LinearLayout priceLayout;
    private EditText priceRangeValue, availQuantity,metric,metricQty,itemName,itemDesc,priceLow,priceHigh;
    private Button saveBtn;
    private String storeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_items_new_add);
        setUI();
        Intent prevIntent =getIntent();
        storeID = prevIntent.getStringExtra("storeId");
        priceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priceRange.isChecked())
                {
                    priceLayout.setVisibility(View.VISIBLE);
                    priceRangeValue.setVisibility(View.GONE);
                }
                else {
                    priceLayout.setVisibility(View.GONE);
                    priceRangeValue.setVisibility(View.VISIBLE);
                }
            }
        });
        specify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(specify.isChecked()){
                    availQuantity.setVisibility(View.VISIBLE);
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelClass model = new ModelClass();
                String itemNam,itemDes,availQty,metricQti,metrc,priceLw,priceHi,singPrice;
                boolean user = model.userLoggedIn();
                String itemId, storeId,creatorId;
                itemNam=model.getEditTextValue(itemName);
                itemDes=model.getEditTextValue(itemDesc);
                metricQti=model.getEditTextValue(metricQty);
                metrc=model.getEditTextValue(metric);
                priceLw=model.getEditTextValue(priceLow);
                priceHi=model.getEditTextValue(priceHigh);
                singPrice=model.getEditTextValue(priceRangeValue);
                if(unlimited.isChecked())
                {
                    availQty="Unlimited";
                }
                else if(limited.isChecked()){
                    availQty="Limited";
                }
                else {
                    availQty=model.getEditTextValue(availQuantity);
                }
                if(priceRange.isChecked()&& !priceHi.isEmpty()&& !priceLw.isEmpty()){
                    singPrice=priceLw;
                }
                else if(!priceRange.isChecked() && !singPrice.isEmpty()){
                    priceHi=priceLw=singPrice;
                }
                else{
                    Toast.makeText(StoreItemsNewAdd.this,"No price quotations", Toast.LENGTH_SHORT).show();
                }

                if(user)
                {
                    if(!itemNam.isEmpty()&&!itemDes.isEmpty()&&!metricQti.isEmpty()&&!metrc.isEmpty()&&!availQty.isEmpty() && !singPrice.isEmpty()){
                        if(Integer.parseInt(priceLw) <= Integer.parseInt(priceHi)){
                            DatabaseReference databaseReference;
                            databaseReference = FirebaseDatabase.getInstance().getReference("StoreItem");
                            itemId = databaseReference.push().getKey();
                            storeId= storeID;
                            creatorId=model.getCurrentUserId();
                            DateFormat ddf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = new Date();
                            String dateNow=ddf.format(date).toString();
                            StoreItemMainModel newItem = new StoreItemMainModel(itemId,storeId,itemNam,itemDes,availQty,metrc,metricQti,priceLw,priceHi,creatorId,dateNow);
                            databaseReference.child(itemId).setValue(newItem);
                            Toast.makeText(StoreItemsNewAdd.this,"Store Item Added Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems");
                            intent.putExtra("storeId",storeId);
                            finish();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(StoreItemsNewAdd.this,"Lowest price bigger than highest price", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(StoreItemsNewAdd.this,"Fill all fields correctly.", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(StoreItemsNewAdd.this,"Please Log In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void hideSpecificQty(View v){
        availQuantity.setVisibility(View.GONE);
    }
    public void setUI(){
        metric=findViewById(R.id.newStoreItemMinQty);
        metricQty=findViewById(R.id.newStoreItemMinQtyMetric);
        itemName=findViewById(R.id.newStoreItemName);
        itemDesc=findViewById(R.id.newStoreItemDesc);
        priceLow=findViewById(R.id.newStoreItemPriceLow);
        priceHigh=findViewById(R.id.newStoreItemPriceHigh);
        saveBtn =findViewById(R.id.newStoreItemSaveBtn);
        availQuantity=findViewById(R.id.newStoreItemSpecifyAvailQty);
        priceRangeValue=findViewById(R.id.newStoreItemPrice);
        priceLayout=findViewById(R.id.newStoreItempriceRangeLayout);
        limited=findViewById(R.id.newStoreItemLimitedRadio);
        unlimited =findViewById(R.id.newStoreItemUnlimitedRadio);
        specify =findViewById(R.id.newStoreItemSpecifyRadio);
        priceRange=findViewById(R.id.newStoreItemPriceRangeCheckBox);
    }
}
