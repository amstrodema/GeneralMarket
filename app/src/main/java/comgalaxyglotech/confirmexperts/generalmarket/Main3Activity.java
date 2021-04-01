package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Market.MarketEditDialogue;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Market.marketReviewDialogue;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market.newMarketModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.User.UserDataClass;
import comgalaxyglotech.confirmexperts.generalmarket.Services.Location.LocationHandler;

public class Main3Activity extends AppCompatActivity{
    public static newMarketModel EditModel;
    public static newMarketModel EditModelHolder;
    private Button thisButton,enterMarket,mktReview, viewStores,currentlyHere;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private String marketId;
    private ModelClass modelClass = new ModelClass();
    private boolean isLoggedIn = modelClass.userLoggedIn();
    private ImageButton thisMarketImageUploadBtn;
    private TextView title,desc,loc, noReviewLabel,editMarket;
    private RatingBar marketRatingBar;
    private UserDataClass userDataClass = new UserDataClass();
    private ProgressDialog progressDialog;
    private Context context = this;
    private Activity activity = this;
    private String topic;
    private DataClass dataClass = new DataClass();
    private ImageView thisMarketImage;
    private static int PICK_IMAGE =123;
    Uri imagePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        setUI();
        if(!isLoggedIn){
            thisMarketImageUploadBtn.setVisibility(View.GONE);
            editMarket.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(Main3Activity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        dataClass.getData();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        Intent thisIntent= getIntent();
        marketId =thisIntent.getStringExtra("marketID");
        userDataClass.getData(2,marketId,context,activity,noReviewLabel);
        storageReference.child("Market").child(marketId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(thisMarketImage);
            }

        });
        setMarket();
        thisButton =findViewById(R.id.marketReview);
        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        thisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add review to this market
                if(user != null){
                    marketReviewDialogue.activity=activity;
                    marketReviewDialogue.storeId =marketId;
                    marketReviewDialogue.ratingBar =marketRatingBar;
                    marketReviewDialogue.context = context;
                    marketReviewDialogue.noReviewLabel =noReviewLabel;
                    openDialog();
                }
                else{
                    Toast.makeText(Main3Activity.this,"Log in first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
       /* mktStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //add new stores related to this market
              if(isLoggedIn){
                  Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreAdd");
                  intent.putExtra("marketId",marketId);
                  startActivity(intent);
              }
              else{
                  Toast.makeText(Main3Activity.this,"Log In First!",Toast.LENGTH_SHORT).show();
              }
            }
        });*/
        viewStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreActivity");
                intent.putExtra("marketId",marketId);
                startActivity(intent);
            }
        });
        thisMarketImageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
                }
                else{
                    Toast.makeText(Main3Activity.this,"Log in first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        editMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){
                    EditModel = EditModelHolder;
                    MarketEditDialogue.context =context;
                    openEditDialog();
                }
                else{
                    Toast.makeText(Main3Activity.this,"Log in first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        currentlyHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditModel = EditModelHolder;
                ModelClass model = new ModelClass();
               boolean user= model.userLoggedIn();
                if(user){
                    LocationHandler locationHandler = new LocationHandler();
                    locationHandler.getGpsLocation(context,activity,"market");
                }
                else{
                    Toast.makeText(Main3Activity.this,"Log In First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        enterMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main4Activity");
                intent.putExtra("thisMarketID",marketId);
                intent.putExtra("marketName",topic);
                startActivity(intent);
            }
        });
    }
    public void openEditDialog(){
        MarketEditDialogue itemDialog = new MarketEditDialogue();
        itemDialog.show(getSupportFragmentManager(),"Edit Market Details");
    }
    public void openDialog(){
        marketReviewDialogue itemDialog = new marketReviewDialogue();
        itemDialog.show(getSupportFragmentManager(),"Add New Review");
    }
    public void setMarket() {
        Query query = firebaseDatabase.getInstance().getReference("Market")
                .orderByChild("id")
                .equalTo(marketId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
       // String name;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    newMarketModel newMarketMode= snapshot.getValue(newMarketModel.class);
                    EditModelHolder = newMarketMode;
                    title.setText(newMarketMode.getMarketName().toUpperCase());
                    topic=newMarketMode.getMarketName().toUpperCase();
                    marketRatingBar.setRating(newMarketMode.getRating());
                    desc.setText(newMarketMode.getMarketDescription());
                    loc.setText(newMarketMode.getLocation());
                    enterMarket.setEnabled(true);
                    mktReview.setEnabled(true);
                }

            }
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    public void setUI(){
        currentlyHere = findViewById(R.id.currentlyHere);
        editMarket = findViewById(R.id.editMarket);
        thisMarketImageUploadBtn = findViewById(R.id.thisMarketImageUploadBtn);
        thisMarketImage = findViewById(R.id.thisMarketImage);
        noReviewLabel = findViewById(R.id.noReviewLabel);
        marketRatingBar = findViewById(R.id.marketRatingBar);
        viewStores = findViewById(R.id.view_market_store);
        title =findViewById(R.id.thisMarketName);
        desc =findViewById(R.id.thisMarketDescription);
        loc =findViewById(R.id.thisMarketLocation);
        enterMarket=findViewById(R.id.thisEnterMarketBtn);
        mktReview=findViewById(R.id.marketReview);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
        {
            imagePath = data.getData();
            final StorageReference imageReference = storageReference.child("Market").child(marketId);
            imageReference.putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(Main3Activity.this,"Market Image Saved Successfully",Toast.LENGTH_SHORT).show();
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(thisMarketImage);
                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Main3Activity.this,"Market Image Not Saved",Toast.LENGTH_SHORT).show();
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

