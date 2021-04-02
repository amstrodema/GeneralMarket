package comgalaxyglotech.confirmexperts.generalmarket.Controller.Stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockAdd;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class NewStockActivity extends AppCompatActivity {
    private boolean isEdit =false;
    Button priceSend;
    private boolean ban = false;
    private LinearLayout categoryHolder,itemHolder;
    AutoCompleteTextView itemPicker;
    EditText advertName,priceSingle,desc, quantity;
    private ImageView itemImage;
    private ImageButton itemImageBtn;
    private TextView itemImageLabel;
    Spinner newItemClaz;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    String nameId, marketId, storeId, id, creatorID;
    private String description, adName, price, qty;
    DataClass dataClass = new DataClass();
    private Context context= this;
    private Activity activity=this;
    private static int PICK_IMAGE =123;
    private ArrayList<String> verifyList = new ArrayList<>();
    Uri imagePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stock);
        //dataClass.getData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        setUI();
        Intent previousActivity=getIntent();
        storeId = previousActivity.getStringExtra("storeId");
        marketId = previousActivity.getStringExtra("marketId");
        setUpEdit();
        //setting spinner with data obtained from list of common names.
        //keep a list of uniform case value for comparison sake
        for (String itm:DataClass.itemIdData) {
            verifyList.add(itm.toLowerCase());
        }
        ArrayAdapter<String> adapterItm = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,DataClass.itemIdData);
        itemPicker.setAdapter(adapterItm);
        if(marketId.isEmpty()){
            itemHolder.setVisibility(View.GONE);
        }
        setListeners();

    }
    private Boolean setUpItemID(String item){
        //sets up id for common item names. If Item is found in the list, nameId will be set else item
        //will be saved and nameId extracted
        if(verifyList.contains(item.toLowerCase())){
            String theItem = DataClass.itemIdData.get(verifyList.indexOf(item.toLowerCase()));
            nameId = DataClass.itemNameToId.get(theItem);
            return true;
        }
        return false;
    }
    private void setListeners(){
        itemPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(verifyList.contains(s.toString().trim().toLowerCase())){
                    categoryHolder.setVisibility(View.GONE);
                }
                else{
                    categoryHolder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        itemImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
        priceSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelClass model=new ModelClass();
                final String item = model.getEditTextValue(itemPicker);
                description = model.getEditTextValue(desc);
                adName = model.getEditTextValue(advertName);
                price = model.getEditTextValue(priceSingle);
                qty = model.getEditTextValue(quantity);
                final String category = newItemClaz.getSelectedItem().toString();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                if(isEdit)
                {
                    alertDialog.setTitle("Modify Stock?");
                    alertDialog.setMessage("You are about to modify this stock. Confirm to proceed!");
                }
                else{
                    alertDialog.setTitle("Save New Stock?");
                    alertDialog.setMessage("You are about to add a new stock. Confirm to proceed!");
                }

                alertDialog.setIcon(R.drawable.danger);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(DataClass.itemIdData.size() < 1 && !marketId.isEmpty())
                        {Toast.makeText(NewStockActivity.this,"please wait...",Toast.LENGTH_SHORT).show();}
                        else if(!item.trim().isEmpty()&& !description.isEmpty() && !adName.isEmpty() && !price.isEmpty() && !qty.isEmpty() && !marketId.isEmpty()){
                            if(setUpItemID(item)){
                                NewStockAdd addStock = new NewStockAdd(adName, nameId,description,qty,price, marketId,storeId,imagePath,context,activity, category);
                                if(isEdit)
                                addStock.editStock(id,"Store_Stock");
                                else
                                    addStock.saveStock("Store_Stock");
                            }
                            else{
                                Toast.makeText(NewStockActivity.this,"Common Name Is Unavailable",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(!description.isEmpty()&& !adName.isEmpty() && !price.isEmpty() && !qty.isEmpty() && marketId.isEmpty()){
                            NewStockAdd addStock = new NewStockAdd(adName, "",description,qty,price, "",storeId,imagePath,context,activity, category);
                            if(isEdit)
                                addStock.editStock(id,"Store_Stock");
                            else
                                addStock.saveStock("Store_Stock");
                        }
                        else{
                            Toast.makeText(NewStockActivity.this,"Complete all fields",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();

            }
        });
    }
    private NewStockMainModel editModel;
    private void setUpEdit() {
        if (StoreItems.EditReference != null) {
            String tt = "MODIFY STOCK";
            priceSend.setText(tt);
            isEdit = true;
            editModel = StoreItems.EditReference;
            StoreItems.EditReference = null;
            id =  editModel.getId();
            ban = editModel.isBan();
            if(!editModel.getCategory().isEmpty()) {
                itemPicker.setText(editModel.getCategory());
                categoryHolder.setVisibility(View.GONE);
            }
            creatorID = editModel.getCreatorId();
            storeId = editModel.getStoreId();
            marketId = editModel.getMarketId();
            storageReference.child("Stock").child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(itemImage);
                    itemImageLabel.setVisibility(View.GONE);
                }

            });
            advertName.setText(editModel.getAdvertName());
            desc.setText(editModel.getItemDesc());
            priceSingle.setText(editModel.getItemCost());
            quantity.setText(editModel.getItemQty());
        }
    }
    private void setUI(){
        categoryHolder = findViewById(R.id.categoryHolder);
        itemPicker  = findViewById(R.id.itemPicker);
        advertName = findViewById(R.id.advertName);
        itemImage =findViewById(R.id.itemImage);
        itemImageBtn =findViewById(R.id.itemImageBtn);
        itemImageLabel =findViewById(R.id.itemImageLabel);
        priceSend=findViewById(R.id.newPriceSend);
        priceSingle=findViewById(R.id.newPriceSingleValue);
        newItemClaz=findViewById(R.id.newItemClass);
        desc=findViewById(R.id.newPriceTraderDesc);
        itemHolder = findViewById(R.id.itemHolder);
        quantity = findViewById(R.id.newPriceQty);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            try {
                itemImageLabel.setVisibility(View.GONE);//removes image upload label
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                itemImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
