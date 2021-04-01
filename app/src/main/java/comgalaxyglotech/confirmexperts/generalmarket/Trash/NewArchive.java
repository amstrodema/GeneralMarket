package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class NewArchive extends AppCompatActivity {
    Spinner focusItem, focusRecipe,focusMarket;
    //private ArrayList<String> selectedItem = new ArrayList<>();
    private EditText archiveName,archiveDesc,archiveLink,archiveDetails,archiveSig,archiveAuthor;
    private Button showMarket, showRecipe, showItem, newItem, newMarket, newRecipe,clearlist, addToList, saveBtn,archiveImageBtn;
    private TextView ingredientShow,archiveImageLink;
    String focus, focusDb;
    private ImageView archiveImage;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private static int PICK_IMAGE =123;
    ModelClass model=new ModelClass();
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    Uri imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_archive);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        setUI();
        //setting spinner with data obtained from list of itemNames.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataClass.itemIdData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        focusItem.setAdapter(adapter);
        //method contains all listeners
        Listener();
    }
    private void Listener(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(archiveName.getText().toString().equals("")){
                    Toast.makeText(NewArchive.this,"Give this archive a name",Toast.LENGTH_SHORT).show();
                }
                else if(archiveDesc.getText().toString().equals("")){
                    Toast.makeText(NewArchive.this,"Describe this archive",Toast.LENGTH_SHORT).show();
                }
                else if (archiveDetails.getText().toString().equals("")){
                    Toast.makeText(NewArchive.this,"Input archive details",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setMessage("Saving Archive");
                    progressDialog.show();
                    if(model.userLoggedIn()){
                        databaseReference= FirebaseDatabase.getInstance().getReference("Archive");
                        String creatorID;
                        final String id= databaseReference.push().getKey();
                        creatorID=model.getCurrentUserId();
                        ArchiveMainModel mainModel =new ArchiveMainModel(id,creatorID,archiveName.getText().toString(),archiveDesc.getText().toString(),archiveLink.getText().toString(),archiveDetails.getText().toString(),archiveSig.getText().toString(),"",archiveAuthor.getText().toString());
                        databaseReference.child(id).setValue(mainModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(imagePath != null){
                                    StorageReference imageReference = storageReference.child("Archive").child(id);
                                    imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            Toast.makeText(NewArchive.this,"Archive Saved Successfully",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent =new Intent(NewArchive.this, Archive.class);
                                            intent.putExtra("archiveId",id);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(NewArchive.this,"Archive Saved Without Image",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent =new Intent(NewArchive.this,Archive.class);
                                            intent.putExtra("archiveId",id);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(NewArchive.this,"Archive Saved Successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent =new Intent(NewArchive.this,Archive.class);
                                    intent.putExtra("archiveId",id);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(NewArchive.this,"Log in first!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(focusItem.getVisibility()==View.VISIBLE){
                    focus+= focusItem.getSelectedItem().toString()+"\n";
                }
                else if (focusMarket.getVisibility()==View.VISIBLE){
                    focus+= focusMarket.getSelectedItem().toString()+"\n";
                }
                else {
                    focus+= focusRecipe.getSelectedItem().toString()+"\n";
                }
                ingredientShow.setText(focus);
            }
        });
        clearlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focus="";
                ingredientShow.setText("");
                focusDb="";
            }
        });
        showItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusItem.setVisibility(View.VISIBLE);
                focusMarket.setVisibility(View.GONE);
                focusRecipe.setVisibility(View.GONE);
            }
        });
        showMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusMarket.setVisibility(View.VISIBLE);
                focusItem.setVisibility(View.GONE);
                focusRecipe.setVisibility(View.GONE);
            }
        });
        showRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusRecipe.setVisibility(View.VISIBLE);
                focusItem.setVisibility(View.GONE);
                focusMarket.setVisibility(View.GONE);
            }
        });
        archiveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
    }
    private void setUI(){
        archiveImageLink= findViewById(R.id.archiveImageLink);
        archiveImage = findViewById(R.id.archiveImage);
        archiveImageBtn = findViewById(R.id.archiveImageBtn);
        archiveAuthor = findViewById(R.id.archiveAuthor);
        ingredientShow = findViewById(R.id.ingredientShow);
        focusMarket = findViewById(R.id.focusMarket);
        focusRecipe = findViewById(R.id.focusRecipe);
        archiveName = findViewById(R.id.archiveName);
        archiveDesc = findViewById(R.id.archiveDesc);
        archiveLink = findViewById(R.id.archiveLink);
        archiveDetails = findViewById(R.id.archiveDetails);
        archiveSig = findViewById(R.id.archiveSig);
        showMarket = findViewById(R.id.showMarket);
        showRecipe = findViewById(R.id.showRecipe);
        showItem = findViewById(R.id.showItem);
        newItem = findViewById(R.id.newListItem);
        newMarket = findViewById(R.id.newListMarket);
        newRecipe = findViewById(R.id.newListRecipe);
        clearlist = findViewById(R.id.clearList);
        addToList = findViewById(R.id.adToList);
        saveBtn = findViewById(R.id.saveArchive);
        focusItem = findViewById(R.id.focusItem);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            try {
                archiveImageLink.setText(imagePath.toString());//displays the path
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                archiveImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
