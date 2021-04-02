package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class LandingPage_InfoGraphics extends AppCompatActivity {
    private Button page1Continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        page1Continue = findViewById(R.id.page1Continue);
        setAdvert();
    }
    private void setAdvert(){
        final ImageView Landing_Page_Full = findViewById(R.id.Landing_Page_Full);
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        page1Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage_InfoGraphics.this, HomePage.class);
                startActivity(intent);
            }
        });
        storageReference.child("Advert").child("Landing_Page_Full").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Landing_Page_Full);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page1Continue.setVisibility(View.VISIBLE);
                    }
                },3000);
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

            }
        });
    }
}
