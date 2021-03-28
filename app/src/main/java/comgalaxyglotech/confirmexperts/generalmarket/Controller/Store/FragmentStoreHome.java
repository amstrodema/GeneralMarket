package comgalaxyglotech.confirmexperts.generalmarket.Controller.Store;

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

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataModel;
import comgalaxyglotech.confirmexperts.generalmarket.LocationHandler;
import comgalaxyglotech.confirmexperts.generalmarket.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.NewStockActivity;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.StoreAdd;
import comgalaxyglotech.confirmexperts.generalmarket.StoreItems;
import comgalaxyglotech.confirmexperts.generalmarket.StoreReviewDialogue;
import comgalaxyglotech.confirmexperts.generalmarket.StoreView;
import comgalaxyglotech.confirmexperts.generalmarket.UserDataClass;

import static android.app.Activity.RESULT_OK;


public class FragmentStoreHome extends Fragment {
    public static StoreMainModel EditReferenceHolder;
    String storeId, marketId;
    private FirebaseDatabase firebaseDatabase;
    private RelativeLayout addStockBtnPanel;
    private TextView storeName, storeLoc,storeDesc,creator,market,availability, time,delivery, noReviewLabel,editStore;
    private Button enterStore,storeReview, stockAdd,contact,currentlyHere,banBtn;
    private ImageButton thisStoreImageUploadBtn;
    //private ReviewData reviewData= new ReviewData();
    private UserDataClass userDataClass = new UserDataClass();
    private ImageView thisStoreImage;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE =123;
    Uri imagePath;
    View v;
    private StorageReference storageReference;
    ScrollView thisScroll;
    private RatingBar storeRating;
    DataClass dataClass = new DataClass();
    DataModel dataModel = new DataModel();
    Context context;
    Activity activity;
    ModelClass model;
    boolean user;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_store_home, container, false);
        context = v.getContext();
        activity = getActivity();
        progressDialog = new ProgressDialog(context);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //dataClass.getData();
        Intent prevIntent = getActivity().getIntent();
        storeId = prevIntent.getStringExtra("storeId");
        model = new ModelClass();
        setUI();
        storeReview.setEnabled(false);
        if(ModelClass.admin){
            banBtn.setVisibility(View.VISIBLE);
        }
        userDataClass.getData(1,storeId,context,activity,noReviewLabel);
        storageReference.child("Store").child(storeId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(thisStoreImage);
            }

        });
        setStore();//stockAdd
        setListeners();
        return v;
    }
    private void ownerVisible(String creatorId){
        user= model.userLoggedIn();
        if(user) {
            String userId = model.getCurrentUserId();
            if (userId.equals(creatorId)) {
                addStockBtnPanel.setVisibility(View.VISIBLE);
                editStore.setVisibility(View.VISIBLE);
                thisStoreImageUploadBtn.setVisibility(View.VISIBLE);
            }
        }

    }
    private void setListeners(){
        currentlyHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelClass model = new ModelClass();
                boolean user= model.userLoggedIn();
                if(user){
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        StoreView.EditReference = EditReferenceHolder;
                        LocationHandler locationHandler = new LocationHandler();
                        locationHandler.getGpsLocation(context,activity,"store");
                    }
                    else {
                        Toast.makeText(context,"Store Owner Notified!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        editStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit store. calls the add store class which defects to edit so far the static EditReference variable is not null
                //reset static variable as null after successful contract.
                progressDialog.setMessage("Verifying Access");
                progressDialog.show();
                user= model.userLoggedIn();
                if(user){
                    String userId =model.getCurrentUserId(), current =creator.getText().toString() ;
                    if(userId.equals(current)){
                        StoreView.EditReference = EditReferenceHolder;
                        Intent intent =new Intent(context, StoreAdd.class);
                        progressDialog.dismiss();
                        startActivity(intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"You Do Not Own This Store",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context,"You Do Not Own This Store",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        storeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= model.userLoggedIn();
                if(user){
                    StoreReviewDialogue.context =context;
                    StoreReviewDialogue.storeId = storeId;
                    StoreReviewDialogue.activity = activity;
                    StoreReviewDialogue.ratingBar = storeRating;
                    StoreReviewDialogue.noReviewLabel = noReviewLabel;
                    openDialog();
                }
                else{
                    Toast.makeText(context,"Log in first!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        enterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreItems");
                intent.putExtra("storeId",storeId);
                intent.putExtra("farmClick","False");
                //  gets data from datamodel class and then dataclass which then moves the activity
                dataModel.getDoubleData(progressDialog,context,intent);
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
                        marketId = market.getText().toString();
                        Intent intent = new Intent(context, NewStockActivity.class);
                        intent.putExtra("storeId",storeId);
                        intent.putExtra("marketId",marketId);
                        dataClass.getSmarterData(progressDialog,context,intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"You Do Not Own This Store",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void openDialog(){
        StoreReviewDialogue itemDialog = new StoreReviewDialogue();
        itemDialog.show(getFragmentManager(),"");
    }
    private void setStore() {
        Query query = firebaseDatabase.getInstance().getReference("AllStores")
                .orderByChild("id")
                .equalTo(storeId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        // String name;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final StoreMainModel newStoreMode= snapshot.getValue(StoreMainModel.class);
                    EditReferenceHolder = newStoreMode;
                    final String store =newStoreMode.getStoreName();
                    storeName.setText(store);
                    //String desc =  ++
                    storeDesc.setText(newStoreMode.getStoreDesc());
                    availability.setText(newStoreMode.getStoreOpeningDays());
                    String desc =dataClass.timeCheck(newStoreMode.getStoreOpeningTime())+" - "+dataClass.timeCheck(newStoreMode.getStoreClosingTime());
                    time.setText(desc);
                    delivery.setText(newStoreMode.getDelievery());
                    //set call uri here and attach to call button
                    final String tel =newStoreMode.getStorePhone();
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
                    ownerVisible(newStoreMode.getCreatorId());
                    storeLoc.setText(newStoreMode.getStoreLocation());
                    creator.setText(newStoreMode.getCreatorId());
                    storeRating.setRating(model.loadRating(newStoreMode.getRating()));
                    try{
                        market.setText(newStoreMode.getMarketId());
                    }
                    catch (Exception e){

                    }
                    banBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ModelClass.banStore(newStoreMode);
                        }
                    });
                    enterStore.setEnabled(true);
                    storeReview.setEnabled(true);
                    progressDialog.dismiss();
                }

            }

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void setUI (){
        addStockBtnPanel = v.findViewById(R.id.addStockBtnPanel);
        banBtn  = v.findViewById(R.id.banBtn);
        currentlyHere = v.findViewById(R.id.currentlyHere);
        editStore = v.findViewById(R.id.editStore);
        noReviewLabel = v.findViewById(R.id.noReviewLabel);
        thisStoreImage = v.findViewById(R.id.thisStoreImage);
        thisStoreImageUploadBtn = v.findViewById(R.id.thisStoreImageUploadBtn);
        storeRating = v.findViewById(R.id.storeRating);
        market = v.findViewById(R.id.market);
        creator = v.findViewById(R.id.creator);
        stockAdd = v.findViewById(R.id.addStock);
        storeName=v.findViewById(R.id.thisStoreName);
        storeDesc =v.findViewById(R.id.thisStoreDescription);
        availability =v.findViewById(R.id.availabilty);
        time =v.findViewById(R.id.store_time);
        delivery =v.findViewById(R.id.delivery);
        contact =v.findViewById(R.id.contact);
        storeLoc=v.findViewById(R.id.thisStoreLocation);
        storeReview=v.findViewById(R.id.thisStoreReview);
        enterStore=v.findViewById(R.id.thisStoreBtn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            final StorageReference imageReference = storageReference.child("Store").child(storeId);
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
