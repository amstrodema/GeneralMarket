package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.ExampleItem;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.itemDialogue;

public class NewRecipe extends AppCompatActivity{
    private LinearLayout categoryHolder;
    private Spinner newItemClass;
    private Button clearList,addNewItem, addToList, saveRecipe,uploadPicker;
    private EditText itemQuantity,recipeName,recipeLink,recipeProcedure,recipeSignature,recipeDesc;
    private TextView ingredientShow,showUploadPicker;
    private Context context = this;
    private DataClass dataClass = new DataClass();
    //finalvalue holds concatenated string with line break for the display
    //finalvalueDb holds concatenated id with ¦ divider meant for db save.
    private String finalValue ="", finalValueDb ="", finalQtyDb ="";
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    ModelClass model=new ModelClass();
    private AutoCompleteTextView itemPicker;
    private int count =0;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE =123;
    private ArrayList<String> verifyList = new ArrayList<>();
    private ArrayList<String> catList = new ArrayList<>();
    Uri imagePath;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
       // StorageReference classic = storageReference.child("User");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        setContentView(R.layout.activity_new_recipe);
        setUI();
        //setting spinner with data obtained from list of itemNames.
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,DataClass.itemIdData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);*/
        for (String itm:DataClass.itemIdData) {
            verifyList.add(itm.toLowerCase());
        }
        ArrayAdapter<String> adapterItm = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,DataClass.itemIdData);
        itemPicker.setAdapter(adapterItm);
        listeners();
    }
    public void openDialog(){
        itemDialogue itemDialog = new itemDialogue();
        itemDialog.show(getSupportFragmentManager(),"Add New Item");
    }
    public void listeners(){
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
        uploadPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
        saveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeName.getText().toString().equals("")){
                    Toast.makeText(NewRecipe.this,"Name this recipe",Toast.LENGTH_SHORT).show();
                }
                else if(recipeProcedure.getText().toString().equals("")){
                    Toast.makeText(NewRecipe.this,"Recipe procedure cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else if(finalValueDb.equals("")){
                    Toast.makeText(NewRecipe.this,"Add ingredients for this recipe",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setMessage("Saving Recipe");
                    progressDialog.show();
                   if(model.userLoggedIn()){
                       //Converts all ingredients items into id, saves new items and passes the id along
                       setUpItemID();
                       databaseReference= FirebaseDatabase.getInstance().getReference("Recipe");
                       String creatorID;
                       final String id= databaseReference.push().getKey();
                       creatorID=model.getCurrentUserId();
                       finalValueDb += "┘" + finalQtyDb;    // binded with qty ┘ alt217


                       RecipeMainModel mainModel =new RecipeMainModel(id,creatorID,recipeName.getText().toString(),recipeLink.getText().toString(),recipeProcedure.getText().toString(),recipeSignature.getText().toString(),finalValueDb,recipeDesc.getText().toString());
                       databaseReference.child(id).setValue(mainModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(imagePath != null){
                                   StorageReference imageReference = storageReference.child("Recipe").child(id);
                                   imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                           Toast.makeText(NewRecipe.this,"Recipe Saved Successfully",Toast.LENGTH_SHORT).show();
                                           finish();
                                           Intent intent =new Intent(NewRecipe.this, Recipe.class);
                                           intent.putExtra("recipeId",id);
                                           dataClass.getSmarterData(progressDialog,context,intent);
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(NewRecipe.this,"Recipe Saved Without Image",Toast.LENGTH_SHORT).show();
                                           finish();
                                           Intent intent =new Intent(NewRecipe.this,Recipe.class);
                                           intent.putExtra("recipeId",id);
                                           dataClass.getSmarterData(progressDialog,context,intent);
                                       }
                                   });
                               }
                               else{
                                   Toast.makeText(NewRecipe.this,"Recipe Saved Image",Toast.LENGTH_SHORT).show();
                                   finish();
                                   Intent intent =new Intent(NewRecipe.this,Recipe.class);
                                   intent.putExtra("recipeId",id);
                                   startActivity(intent);
                                   progressDialog.dismiss();
                               }


                           }
                       });
                   }
                   else {
                       Toast.makeText(NewRecipe.this,"Log in first!",Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });
        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item =itemPicker.getText().toString();// name.getSelectedItem().toString();
                String qty = itemQuantity.getText().toString();
                String qtyContainer =qty; // necessary since qty was modified alongside
                if(item.equals("")){
                    Toast.makeText(NewRecipe.this,"Select An Item",Toast.LENGTH_SHORT).show();
                }
                else if(qty.equals("")){

                    Toast.makeText(NewRecipe.this,"Input Quantity",Toast.LENGTH_SHORT).show();
                }
                else {
                    ingredientShow.setText("");
                    count++;
                    qty =item+" "+qty;
                    itemQuantity.setText("");
                    //the divider shouldn't appear on the first
                    //plain item is concatenated as string but separated by special char. Upon call of method, the strings are changed
                    //to corresponding id before been saved
                    if(count == 1){
                        finalValueDb = item; //alt221
                        finalQtyDb = qtyContainer;
                        catList.clear();
                        catList.add(newItemClass.getSelectedItem().toString());
                    }
                    else{
                        finalValueDb += "¦"+item; //alt221 .. binded with qty ┘ alt217
                        finalQtyDb += "¦"+ qtyContainer; // didnt use qty, snce it got modified along transmission to include the item name.
                        catList.add(newItemClass.getSelectedItem().toString());
                    }
                    //selectedItem.add(qty);
                    finalValue += count+": "+qty+"\n";
                    ingredientShow.setText(finalValue);
                }
            }
        });
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientShow.setText("");
                finalValue="";
                itemQuantity.setText("");
                finalValueDb="";
                count =0;

            }
        });
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    private void setUpItemID(){
        count = 0;
        String workString = finalValueDb;
        for (String val: workString.split("¦")) {
            count++;

            if(verifyList.contains(val.toLowerCase())){
                if(count == 1){
                    finalValueDb = DataClass.itemNameToId.get(val); //alt221
                }
                else{
                    finalValueDb += "¦"+DataClass.itemNameToId.get(val); //alt221 .. binded with qty ┘ alt217
                 }
            }
            else{
                String itemId;
                String newItemClass = catList.get(count-1);
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("Item");
                itemId = databaseReference.push().getKey();
                //marketId= marketID;
                //creatorId=model.getCurrentUserId();
                ExampleItem newItem = new ExampleItem(val,itemId,"","", newItemClass);
                databaseReference.child(itemId).setValue(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(StoreActivity.this,"Item Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                if(count == 1){
                    finalValueDb = itemId; //alt221
                }
                else{
                    finalValueDb += "¦"+itemId; //alt221 .. eventually binded with qty ┘ alt217
                }
            }
        }

    }
    private void setUI(){
        newItemClass = findViewById(R.id.newItemClass);
        itemPicker = findViewById(R.id.itemPicker);
        showUploadPicker= findViewById(R.id.showUploadPicker);
        uploadPicker = findViewById(R.id.uploadPicker);
        imageView = findViewById(R.id.imageView);
        recipeDesc= findViewById(R.id.recipeDesc);
        recipeName = findViewById(R.id.recipeName);
        recipeLink = findViewById(R.id.recipeLink);
        recipeProcedure = findViewById(R.id.recipeProcedure);
        recipeSignature = findViewById(R.id.recipeSignature);
        saveRecipe = findViewById(R.id.saveRecipe);
        categoryHolder = findViewById(R.id.categoryHolder);
        clearList = findViewById(R.id.clearList);
        addNewItem = findViewById(R.id.newListItem);
        addToList = findViewById(R.id.adToList);
        itemQuantity = findViewById(R.id.itemQuantity);
        ingredientShow = findViewById(R.id.ingredientShow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            try {
                showUploadPicker.setText(imagePath.toString());//displays the path
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
