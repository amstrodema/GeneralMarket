package comgalaxyglotech.confirmexperts.generalmarket.Controller.Farm;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm.FarmMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;
import comgalaxyglotech.confirmexperts.generalmarket.LocationHandler;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.StoreItems;
import comgalaxyglotech.confirmexperts.generalmarket.UserDataClass;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragFarmMain extends Fragment {

    private RelativeLayout addStockBtnPanel;
    private View v;
    private String farmId;
    private FirebaseDatabase firebaseDatabase;
    private TextView storeName, storeLoc,storeDesc,creator,market,availability,time,delivery,noReviewLabel,editFarm;
    private Button enterStore,storeReview, stockAdd, contact,currentlyHere,banBtn;
    private RatingBar farmRating;
    ScrollView thisScroll;
    DataClass dataClass = new DataClass();
    DataModel dataModel = new DataModel();
    Context context;
    Activity activity;
    ModelClass model;
    boolean user;
    ProgressDialog progressDialog;
    private ImageButton thisStoreImageUploadBtn;
    private ImageView thisStoreImage;
    private FirebaseStorage firebaseStorage;
    private UserDataClass userDataClass = new UserDataClass();
    private static int PICK_IMAGE =123;
    Uri imagePath;
    private StorageReference storageReference;
    public FragFarmMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_farm_main, container, false);
        context = v.getContext();
        activity = getActivity();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Intent prevIntent =activity.getIntent();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        farmId = prevIntent.getStringExtra("storeId");
        model = new ModelClass();
        setUI();
        if(ModelClass.admin){
            banBtn.setVisibility(View.VISIBLE);
        }
        userDataClass.getData(3,farmId,context,activity,noReviewLabel);
        storageReference.child("Store").child(farmId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(thisStoreImage);
            }

        });
        setStore();//stockAdd
        storeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= model.userLoggedIn();
                if(user){
                    FarmReviewDialogue.context =context;
                    FarmReviewDialogue.storeId = farmId;
                    FarmReviewDialogue.activity = activity;
                    FarmReviewDialogue.ratingBar = farmRating;
                    FarmReviewDialogue.noReviewLabel = noReviewLabel;
                    openDialog();
                }
                else{
                    Toast.makeText(context,"Log in first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        thisStoreImageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Verifying Access");
                progressDialog.show();
                user= model.userLoggedIn();
                if(user){
                    progressDialog.dismiss();
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
                    }
                    else {
                        Toast.makeText(context,"You Do Not Own This Farm",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }

            }
        });
        enterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FarmItems.class);
                intent.putExtra("farmId",farmId);
                dataModel.getDoubleFarmData(progressDialog, context,intent);
            }
        });
        stockAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Verifying Access");
                progressDialog.show();
                user= model.userLoggedIn();
                if(user){
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        StoreItems.EditReference = null;
                        Intent intent = new Intent(context, FarmStock.class);
                        intent.putExtra("farmId",farmId);
                        dataClass.getSmarterData(progressDialog,context,intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"You Do Not Own This Farm",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        editFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Verifying Access");
                progressDialog.show();
                user= model.userLoggedIn();
                if(user){
                    progressDialog.dismiss();
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        FarmView.EditReference = FarmView.EditReferenceHolder;
                        Intent intent = new Intent(context, FarmAdd.class);
                        progressDialog.dismiss();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(context,"You Do Not Own This Farm",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        currentlyHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= model.userLoggedIn();
                if(user){
                    FarmView.EditReference = FarmView.EditReferenceHolder;
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        LocationHandler locationHandler = new LocationHandler();
                        locationHandler.getGpsLocation(context,activity,"farm");
                    }
                    else {
                        Toast.makeText(context,"Location Observed.",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
    private void ownerVisible(String creatorId){
        user= model.userLoggedIn();
        if(user) {
            String userId = model.getCurrentUserId();
            if (userId.equals(creatorId)) {
                addStockBtnPanel.setVisibility(View.VISIBLE);
                editFarm.setVisibility(View.VISIBLE);
                thisStoreImageUploadBtn.setVisibility(View.VISIBLE);
            }
        }

    }
    public void setStore() {
        Query query = firebaseDatabase.getInstance().getReference("AllFarms")
                .orderByChild("id")
                .equalTo(farmId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        // String name;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final FarmMainModel newStoreMode= snapshot.getValue(FarmMainModel.class);
                    FarmView.EditReferenceHolder = newStoreMode;
                    final String store =  newStoreMode.getFarmName();
                    storeName.setText(store.toUpperCase());
                    storeDesc.setText(newStoreMode.getFarmDesc());
                    String desc="FARM AVAILABLE: "+newStoreMode.getFarmOpeningDays().toUpperCase();
                    availability.setText(desc);
                    desc ="OPENS: "+dataClass.timeCheck(newStoreMode.getFarmOpeningTime())+" and CLOSES: "+dataClass.timeCheck(newStoreMode.getFarmClosingTime());
                    time.setText(desc);
                    desc = "DELIVERY SERVICES: "+newStoreMode.getDelivery();
                    delivery.setText(desc);
                    //set call uri here and attach to call button
                    final String tel =newStoreMode.getFarmPhone();
                    contact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tel.equals("")) {
                                Toast.makeText(context,"Contact Details Is Not Available",Toast.LENGTH_SHORT).show();
                            } else {
                                final String no = "tel:" + tel;
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                                alertDialog.setTitle("Do you want to call?");
                                alertDialog.setMessage("You are about to call "+store);
                                alertDialog.setIcon(R.drawable.contact);
                                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
                                        phoneIntent.setData(Uri.parse(no));
                                        startActivity(phoneIntent);
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    });

                    banBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ModelClass.banFarm(newStoreMode);
                        }
                    });
                    ownerVisible(newStoreMode.getCreatorId());
                    storeLoc.setText(newStoreMode.getFarmLocation());
                    creator.setText(newStoreMode.getCreatorId());
                    enterStore.setEnabled(true);
                    storeReview.setEnabled(true);
                }
            }
            progressDialog.dismiss();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void openDialog(){
        FarmReviewDialogue itemDialog = new FarmReviewDialogue();
        itemDialog.show(getFragmentManager(),"Add New Review");
    }
    private void setUI (){
        banBtn  = v.findViewById(R.id.banBtn);
        currentlyHere = v.findViewById(R.id.currentlyHere);
        editFarm = v.findViewById(R.id.editFarm);
        noReviewLabel = v.findViewById(R.id.noReviewLabel);
        farmRating = v.findViewById(R.id.farmRatingBar);
        thisStoreImage = v.findViewById(R.id.thisStoreImage);
        thisStoreImageUploadBtn = v.findViewById(R.id.thisStoreImageUploadBtn);
        availability =v.findViewById(R.id.availabilty);
        time =v.findViewById(R.id.store_time);
        delivery =v.findViewById(R.id.delivery);
        contact = v.findViewById(R.id.contact);
        market = v.findViewById(R.id.market);
        creator = v.findViewById(R.id.creator);
        stockAdd = v.findViewById(R.id.addStock);
        storeName=v.findViewById(R.id.thisStoreName);
        storeDesc =v.findViewById(R.id.thisStoreDescription);
        storeLoc=v.findViewById(R.id.thisStoreLocation);
        storeReview=v.findViewById(R.id.thisStoreReview);
        enterStore=v.findViewById(R.id.thisStoreBtn);
        addStockBtnPanel =v.findViewById(R.id.addStockBtnPanel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            final StorageReference imageReference = storageReference.child("Store").child(farmId);
            imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(context,"Store Image Saved Successfully",Toast.LENGTH_SHORT).show();
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(thisStoreImage);
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,"Store Image Not Saved",Toast.LENGTH_SHORT).show();
                }
            });
           /* try {
              //  itemImageLabel.setVisibility(View.GONE);//removes image upload label
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                thisStoreImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
