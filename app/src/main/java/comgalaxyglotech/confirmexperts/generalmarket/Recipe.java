package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private String recipeId;
    private TextView panelTop,signature,procedure,estimate;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ImageView recipeImageView;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Setting Recipe Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Intent thisIntent= getIntent();
        recipeId =thisIntent.getStringExtra("recipeId");
        firebaseStorage = FirebaseStorage.getInstance();
        setUI();
        setRecipe();
    }
    private void setUI(){
        recipeImageView =findViewById(R.id.recipeImageView);
        panelTop = findViewById(R.id.panelTop);
        signature = findViewById(R.id.signature);
        procedure = findViewById(R.id.procedure);
        estimate = findViewById(R.id.estimate);
    }
    private void setRecipe() {
        Query query = firebaseDatabase.getInstance().getReference("Recipe")
                .orderByChild("id")
                .equalTo(recipeId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        // String name;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RecipeMainModel recipeMainModel= snapshot.getValue(RecipeMainModel.class);
                    //String[] itemId = recipeMainModel.getRecipeIngredients().split("¦");
                   try {
                       //initial test data didnt have this function, hence the need for a try and catch
                       String[] diffPicks = recipeMainModel.getRecipeIngredients().split("┘"); //splitting the ingredients from the qty alt 217
                    String[] itemId = diffPicks[0].split("¦"); //splitting the various ingredients apart ¦
                    String[] itemQty = diffPicks[1].split("¦"); //splitting the various ingredients apart ¦
                    ArrayList<ListDisplayClass> list = new ArrayList<>();
                    int count=0;
                    for (String id: itemId) {
                        //each item comes with a mandatory qty, hence the item number == item qty number
                        //itemIdToName loads in dictionary with id to name
                        ListDisplayClass display= new ListDisplayClass(DataClass.itemIdToName.get(id),id,itemQty[count]);
                        list.add(display);
                        count++;
                    }
                    //get image
                       // sets the ingredients to a clickable form
                       setRecycler(list);
                }catch (Exception e){

                   }
                    StorageReference storageReference = firebaseStorage.getReference();
                    storageReference.child("Recipe").child(recipeMainModel.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(recipeImageView);
                            recipeImageView.setVisibility(View.VISIBLE);
                        }
                    });
                    panelTop.setText(recipeMainModel.getRecipeName().toUpperCase());
                    signature.setText(recipeMainModel.getRecipeSignature());
                    procedure.setText(recipeMainModel.getRecipeProcedure());
                }

            }
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void setRecycler(ArrayList<ListDisplayClass>ingredients){
        //recycler
        mlayoutManager =new GridLayoutManager(this,4);
        mRecyclerView = findViewById(R.id.ingredientRecycler);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ListAdapter(ingredients);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ListAdapter.OnAllCommoditiesItemClickListener() {
            @Override
            public void onItemClick(int position) {
                 Intent intent =new Intent(Recipe.this,StockLanding.class);
                 startActivity(intent);
                /*intent.putExtra("recipeId",RecipeDataClass.recipeData.get(position).getId());
                progressDialog.setMessage("Loading Recipe Ingredients");
                progressDialog.show();
                dataClass.getSmartData(progressDialog,context,activity,intent);*/
            }
        });
    }
}
