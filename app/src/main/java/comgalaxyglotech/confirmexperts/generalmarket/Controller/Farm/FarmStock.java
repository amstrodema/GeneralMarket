package comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockAdd;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems;

public class FarmStock extends AppCompatActivity {
    private boolean isEdit =false;
    Button priceSend;
    private boolean ban = false;
    private LinearLayout categoryHolder,itemHolder;
    AutoCompleteTextView itemPicker;
    EditText advertName,priceSingle,desc, quantity;
    private ImageView itemImage;
    private ImageButton itemImageBtn;
    private TextView itemImageLabel;
    //used in swap, to lazy to modify appearance
    Spinner newItemClaz, newFarmClass;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    String nameId, farmId, id, creatorID;
    private String description, adName, price, qty;
    DataClass dataClass = new DataClass();
    private Context context= this;
    private Activity activity=this;
    private static int PICK_IMAGE =123;
    private ArrayList<String> verifyList = new ArrayList<>();
    Uri imagePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    //item names are loaded into dictionary alongside there id. Dictionary is snt to the next activity to decipher the id
    // ArrayList<Dictionary<String, String>> itemNameDictionary= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stock);
       // dataClass.getData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        setUI();
        itemHolder.setVisibility(View.GONE);
        newFarmClass.setVisibility(View.VISIBLE);
        newItemClaz.setVisibility(View.GONE);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        setUpEdit();
       setListener();
    }
    private void setListener(){
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
                description = model.getEditTextValue(desc);
                adName = model.getEditTextValue(advertName);
                price = model.getEditTextValue(priceSingle);
                qty = model.getEditTextValue(quantity);
                final String category = newFarmClass.getSelectedItem().toString();//farm_categories
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
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!description.isEmpty()&& !adName.isEmpty() && !price.isEmpty() && !qty.isEmpty()){
                            NewStockAdd addStock = new NewStockAdd(adName, "",description,qty,price, "",farmId,imagePath,context,activity, category);
                            if(isEdit)
                            addStock.editStock(id,"Farm_Stock");
                            else addStock.saveStock("Farm_Stock");
                        }
                        else{
                            Toast.makeText(FarmStock.this,"Complete all fields",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
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
     /*   else{
            String itemId;
            String newItemClass = newFarmClass.getSelectedItem().toString();
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("Item");
            itemId = databaseReference.push().getKey();
            //marketId= marketID;
            //creatorId=model.getCurrentUserId();
            ExampleItem newItem = new ExampleItem(item,itemId,"","", newItemClass);
            databaseReference.child(itemId).setValue(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(StoreActivity.this,"Item Added Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            nameId = itemId;
        }*/
    }
    private void saveStockImage(String id){
        if(imagePath == null){
            Toast.makeText(FarmStock.this,"Stock Added Successfully",Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            StorageReference imageReference = storageReference.child("Stock").child(id);
            imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(FarmStock.this,"Stock Added Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FarmStock.this,"Stock Saved Without Image",Toast.LENGTH_SHORT).show();
                    finish();
                    /*
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreItems");
                    intent.putExtra("storeId",farmId);
                    intent.putExtra("farmClick","True");
                    startActivity(intent);*/
                }
            });
        }
    }
    private NewStockMainModel editModel;
    private void setUpEdit()  {
        if (StoreItems.EditReference != null) {
            String tt = "MODIFY STOCK";
            priceSend.setText(tt);
            isEdit = true;
            editModel = StoreItems.EditReference;
            StoreItems.EditReference = null;
            ban = editModel.isBan();
            id =  editModel.getId();
            farmId = editModel.getStoreId();
            creatorID = editModel.getCreatorId();
            if(!editModel.getCategory().isEmpty()) {
                itemPicker.setText(editModel.getCategory());
                categoryHolder.setVisibility(View.GONE);
            }
            storageReference.child("Stock").child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(itemImage);
                    itemImageLabel.setVisibility(View.GONE);
                }

            });
            advertName.setText(editModel.getAdvertName());
            desc.setText(editModel.getItemDesc());
            quantity.setText(editModel.getItemQty());
        }
        else{
            Intent previousActivity=getIntent();
            farmId = previousActivity.getStringExtra("farmId");
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
        newFarmClass= findViewById(R.id.newFarmClass);
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