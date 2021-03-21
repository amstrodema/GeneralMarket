package comgalaxyglotech.confirmexperts.generalmarket;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Setting.SettingsModel;

public class Admin extends AppCompatActivity implements  itemDialogue.itemDialogListener {
    private ImageButton advertImagePick;
    private Button uploadAdvert,setPromo,setStoreCost,setMarketItem,setAppVersion,setMinVersions,setUpdateTime,setAppAccess,setAppClean,setGeneralMessage;
    private ImageView advertImage;
    private Spinner advertLocation,appCleanGrant,appGrant;
    private EditText promoValue, storeCost,appVersionValue,updateTimeValue,generalMessageValue,minVersions;
    private AutoCompleteTextView itemPicker;
    private Activity activity;
    private static int PICK_IMAGE =123;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    Uri imagePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ArrayList<SettingsModel>settings = new ArrayList<>();
    private Context context = this;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog = new ProgressDialog(context);
        setUI();
        ArrayAdapter<String> adapterItm = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,DataClass.itemIdData);
        itemPicker.setAdapter(adapterItm);
    }
    private void setUI(){
        setMinVersions = findViewById(R.id.setMinVersions);
        advertImagePick = findViewById(R.id.advertImagePick);
        uploadAdvert = findViewById(R.id.uploadAdvert);
        advertImage = findViewById(R.id.advertImage);
        advertLocation = findViewById(R.id.advertLocation);

        promoValue = findViewById(R.id.promoValue);
        storeCost = findViewById(R.id.storeCost);
        appVersionValue = findViewById(R.id.appVersionValue);
        updateTimeValue = findViewById(R.id.updateTimeValue);
        generalMessageValue = findViewById(R.id.generalMessageValue);
        minVersions  = findViewById(R.id.minVersions);

        appCleanGrant = findViewById(R.id.appCleanGrant);
        appGrant = findViewById(R.id.appGrant);

        itemPicker = findViewById(R.id.itemPicker);

        setPromo = findViewById(R.id.setPromo);
        setStoreCost = findViewById(R.id.setStoreCost);
        setMarketItem = findViewById(R.id.setMarketItem);
        setAppVersion = findViewById(R.id.setAppVersion);
        setUpdateTime = findViewById(R.id.setUpdateTime);
        setAppAccess = findViewById(R.id.setAppAccess);
        setGeneralMessage = findViewById(R.id.setGeneralMessage);
        setAppClean = findViewById(R.id.setAppClean);

        getSettings();

    }
    private void logSetting(final SettingsModel setting){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Save Settings?");
        alertDialog.setIcon(R.drawable.danger);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Settings");
                if(setting.getId() == null)
                    setting.setId(ref.push().getKey());
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                ref.child(setting.getId()).setValue(setting).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        });
        alertDialog.show();
    }
    private  void setListeners(){
        setMinVersions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = minVersions.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("appMin")) {
                            setting = set;
                           isPresent = true;
                           break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("appMin");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();

            }
        });
        setPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = promoValue.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("Bonus")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("Bonus");
                        setting.setSettingValueDouble(Double.parseDouble(value));
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValueDouble(Double.parseDouble(value));
                        logSetting(setting);
                    }
            }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();

            }
        });
        setStoreCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = storeCost.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("StoreCost")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("StoreCost");
                        setting.setSettingValueDouble(Double.parseDouble(value));
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValueDouble(Double.parseDouble(value));
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();


            }
        });
        setMarketItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        setAppVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = appVersionValue.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("AppVersion")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("AppVersion");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        setUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = updateTimeValue.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("UpdateTime")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("UpdateTime");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        setAppAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = appGrant.getSelectedItem().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("AppAccess")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("AppAccess");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        setAppClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = appCleanGrant.getSelectedItem().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("AppClean")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("AppClean");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        setGeneralMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = generalMessageValue.getText().toString();
                if (!value.trim().isEmpty())
                {
                    SettingsModel setting = new SettingsModel();
                    boolean isPresent = false;
                    for (SettingsModel set : settings) {
                        if (set.getSettingType().equals("GeneralMessage")) {
                            setting = set;
                            isPresent = true;
                            break;
                        }
                    }
                    if(!isPresent){
                        setting.setSettingType("GeneralMessage");
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                    else{
                        setting.setSettingValue(value);
                        logSetting(setting);
                    }
                }
                else
                    Toast.makeText(context, "Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        });

        advertImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
        uploadAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advrtLocation = advertLocation.getSelectedItem().toString();
                final StorageReference imageReference = storageReference.child("Advert").child(advrtLocation);
                imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(Admin.this,"Advert Saved Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin.this,"Advert Not Saved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            uploadAdvert.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                advertImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getSettings(){
        readData("Settings","id","");
    }

    private void readData(String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        SettingsModel set= snapshot.getValue(SettingsModel.class);
                        assert set != null;
                        switch (set.getSettingType()) {
                            case "GeneralMessage":
                                generalMessageValue.setText(set.getSettingValue());
                                break;
                            case "StoreCost":
                                storeCost.setText(set.getSettingValueDouble()+"");
                                break;
                            case "appMin":
                                minVersions.setText(set.getSettingValue());
                                break;
                            case "AppClean":
                                if (set.getSettingValue().equals("Yes")) {
                                    appCleanGrant.setSelection(0);
                                } else {
                                    appCleanGrant.setSelection(1);
                                }
                                break;
                            case "AppAccess":
                                if (set.getSettingValue().equals("Allow Current Only")) {
                                    appGrant.setSelection(0);
                                } else if (set.getSettingValue().equals("Allow Min & Notify Update")) {
                                    appGrant.setSelection(1);
                                } else if (set.getSettingValue().equals("Force Update")) {
                                    appGrant.setSelection(2);
                                } else {
                                    appGrant.setSelection(0);
                                }

                                break;
                            case "AppVersion":
                                appVersionValue.setText(set.getSettingValue());
                                break;
                            case "UpdateTime":
                                updateTimeValue.setText(set.getSettingValue());
                                break;
                            case "Bonus":
                                promoValue.setText(set.getSettingValueDouble()+"");
                                break;
                        }
                        settings.add(set);

                    }
                  //  firebaseCallback.onCallback(settings);
                }
                else{
                  //  firebaseCallback.onCallback(settings);
                }
                setListeners();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(id.isEmpty()){
            Query query = firebaseDatabase.getReference(reference)
                    .orderByChild(OrderBy);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else{
            Query query = firebaseDatabase.getReference(reference)
                    .orderByChild(OrderBy)
                    .equalTo(id);
            query.addListenerForSingleValueEvent(valueEventListener);
        }

    }
    private void openDialog(){
        itemDialogue itemDialog = new itemDialogue();
        itemDialog.show(getSupportFragmentManager(),"Add New Item");
    }
    @Override
    public void applyTexts(String itemName, String NewItem) {
        ModelClass model = new ModelClass();
        boolean user = model.userLoggedIn();
        String itemId, marketId,creatorId;
        if(user)
        {
            progressDialog.setMessage("Saving New Item");
            progressDialog.show();
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("Item");
            itemId = databaseReference.push().getKey();
            // creatorId=model.getCurrentUserId();
            ExampleItem newItem = new ExampleItem(itemName,itemId,"","",NewItem);
            databaseReference.child(itemId).setValue(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   progressDialog.dismiss();
                }
            });

        }
        else {
            Toast.makeText(context,"Please Log In", Toast.LENGTH_SHORT).show();
        }
    }
}
