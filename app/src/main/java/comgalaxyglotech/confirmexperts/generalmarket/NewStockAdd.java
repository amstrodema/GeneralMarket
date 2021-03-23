package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock.NewStockMainModel;

/**
 * Created by ELECTRON on 06/13/2020.
 */

public class NewStockAdd {
    private String itemDesc, itemCost, itemQty,adName;
    private EditText descBox, costBox, advNameBox, commonNameBox, qtyBox;
    private Spinner categoryBox;
    private String  storeId, marketId, commonNameId, category;
    private Uri imagePath;
    private ModelClass model = new ModelClass();
    private Context context;
    private Activity activity;
    public NewStockAdd(String adName, String commonNameId, String itemDesc,String itemQty, String itemCost, String marketId, String storeId, Uri imagePath, Context context, Activity activity, String category){
        this.adName = adName;
        this.commonNameId = commonNameId;
        this.itemDesc = itemDesc;
        this.itemQty = itemQty;
        this.itemCost = itemCost;
        this.marketId = marketId;
        this.storeId = storeId;
        this.imagePath = imagePath;
        this.context = context;
        this.activity = activity;
        this.category = category;
    }
    private NewStockMainModel setupItem(){
        //item quantity is written into traderLoc variable. itemCategory goes to traderPhone field
        return new NewStockMainModel("",adName,model.getCurrentUserId(),itemCost,commonNameId,itemDesc,itemQty,category,model.getDate(),marketId,storeId,0.0f,false,"False");
    }

    private void saveStockImage(String id, String type){
        if(imagePath == null){
            if(type.equals("Edit")){
                Toast.makeText(context,"Stock Modified Successfully",Toast.LENGTH_SHORT).show();
            }
            else
            Toast.makeText(context,"Stock Saved Successfully",Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(context, StoreItems.class);
            //intent.putExtra("storeId",storeId); //
           // intent.putExtra("farmClick","False");
            //  gets data from datamodel class and then dataclass which then moves the activity
            // dataModel.getDoubleData(progressDialog,context,intent);
            //couldn't get this activity move to stock there left it in store after finish. Seems okay tho since the flow to stock isn't smooth
            activity.finish();
        }
        else{
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageReference = storageReference.child("Stock").child(id);
            imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(type.equals("Edit")){
                        Toast.makeText(context,"Stock Modified Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(context,"Stock Saved Successfully",Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(context, StoreItems.class);
                    intent.putExtra("storeId",storeId);
                    intent.putExtra("farmClick","False");*/
                    //  gets data from datamodel class and then dataclass which then moves the activity
                    // dataModel.getDoubleData(progressDialog,context,intent);
                    activity.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(type.equals("Edit")){
                        Toast.makeText(context,"Stock Modified Without Image",Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(context,"Stock Saved Without Image",Toast.LENGTH_SHORT).show();
                  /*  Intent intent = new Intent(context, StoreItems.class);
                    intent.putExtra("storeId",storeId);
                    intent.putExtra("farmClick","False");*/
                    //  gets data from datamodel class and then dataclass which then moves the activity
                    //   dataModel.getDoubleData(progressDialog,context,intent);
                    activity.finish();
                }
            });
        }
    }
    public void saveStock(String reference)
    {
        NewStockMainModel model = setupItem();
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving Stock");
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(reference);
        final String id = databaseReference.push().getKey();
        model.setId(id);
        databaseReference.child(model.getId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveStockImage(id,"");
            }
        });
    }
    public void editStock(String stockId, String reference){
        NewStockMainModel model = setupItem();
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Modifying Stock");
        progressDialog.show();

        model.setId(stockId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(reference);
        databaseReference.child(model.getId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveStockImage(model.getId(), "Edit");
            }
        });
    }
}
