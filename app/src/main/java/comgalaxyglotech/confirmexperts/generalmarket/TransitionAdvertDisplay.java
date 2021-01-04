package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class TransitionAdvertDisplay extends AppCompatActivity {
    Context context = this;
    //this static class is used to end the advert transition plane from the inbound activity
    public static Activity StopThisActivity;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_advert_display);
        Intent intent = getIntent();
        String loc = intent.getStringExtra("Location");
        setAdvert(loc);
    }
    private void setAdvert(final String location){
        final ProgressBar progress;
        progress= findViewById(R.id.progress_adv);
        final ImageView Landing_Page_Full = findViewById(R.id.transitionAdvert);

        //to re-introduce adverts in later versions

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(location.equals("Market_Transition")){
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main2Activity");
                    startActivity(intent);
                    TransitionAdvertDisplay.this.finish();
                }
                else if(location.equals("Farm_Transition")){
                    DataStock dataStock = new DataStock();
                    Intent intent = new Intent(TransitionAdvertDisplay.this,Farm.class);
                    StopThisActivity=activity;
                    //dataStock.getData(true,progress,context,intent);
                    startActivity(intent);
                }
                else if(location.equals("favey")){
                    DataStock dataStock = new DataStock();
                    Intent intent = new Intent(TransitionAdvertDisplay.this,Favey.class);
                    intent.putExtra("dataType","Fav");
                    StopThisActivity=activity;
                    startActivity(intent);
                }
                else if(location.equals("loan")){
                    DataStock dataStock = new DataStock();
                    Intent intent = new Intent(TransitionAdvertDisplay.this,Loans.class);
                    StopThisActivity=activity;
                    startActivity(intent);
                }
                else if(location.equals("cart")){
                    DataStock dataStock = new DataStock();
                    Intent intent = new Intent(TransitionAdvertDisplay.this,Bonus.class);
                    StopThisActivity=activity;
                    startActivity(intent);
                }
                else if(location.equals("Store_Transition")){
                    DataStock dataStock = new DataStock();
                    Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreActivity");
                    intent.putExtra("marketId","");
                    StopThisActivity=activity;
                    startActivity(intent);
                }
                else if(location.equals("Recipe_Transition")){
                    //call the dataclass to get ingredient item into dictionary. getRecipePerequsite() then calls on the recipe class upon completion.
                    DataClass dataClass = new DataClass();
                    StopThisActivity=activity;
                    dataClass.getRecipePerequsite(context,new Intent(TransitionAdvertDisplay.this,RecipeList.class));
                }
                else if(location.equals("Archive_Transition")){
                    StopThisActivity=activity;
                    ArchiveDataClass archiveDataClass = new ArchiveDataClass();
                    archiveDataClass.getData(context,new Intent(TransitionAdvertDisplay.this,ArchiveList.class));
                }
            }
        },3000);
        /*
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storageReference.child("Advert").child(location).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Landing_Page_Full);
           //     loadBanner.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(location.equals("Market_Transition")){
                            Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.Main2Activity");
                            startActivity(intent);
                            TransitionAdvertDisplay.this.finish();
                        }
                        else if(location.equals("Farm_Transition")){
                            DataStock dataStock = new DataStock();
                            Intent intent = new Intent(TransitionAdvertDisplay.this,Farm.class);
                            StopThisActivity=activity;
                            dataStock.getData(true,progress,context,intent);
                        }
                        else if(location.equals("Store_Transition")){
                            DataStock dataStock = new DataStock();
                            Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreActivity");
                            intent.putExtra("marketId","");
                            StopThisActivity=activity;
                            dataStock.getData(false,progress,context,intent);
                        }
                        else if(location.equals("Recipe_Transition")){
                            //call the dataclass to get ingredient item into dictionary. getRecipePerequsite() then calls on the recipe class upon completion.
                            DataClass dataClass = new DataClass();
                            StopThisActivity=activity;
                            dataClass.getRecipePerequsite(context,new Intent(TransitionAdvertDisplay.this,RecipeList.class));
                        }
                        else if(location.equals("Archive_Transition")){
                            StopThisActivity=activity;
                            ArchiveDataClass archiveDataClass = new ArchiveDataClass();
                            archiveDataClass.getData(context,new Intent(TransitionAdvertDisplay.this,ArchiveList.class));
                        }
                    }
                },3000);
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
             //advert must be shown
            }
        });
    */  }

}