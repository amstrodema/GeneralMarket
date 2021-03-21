package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import comgalaxyglotech.confirmexperts.generalmarket.R;

public class Archive extends AppCompatActivity {
    private TextView archiveName,archiveDesc,archiveLink,archiveDetails,archiveSig,archiveAuthor;
    FirebaseDatabase firebaseDatabase;
    private ImageView archiveViewImage;
    private ProgressDialog progressDialog;
    String archiveId;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseStorage = FirebaseStorage.getInstance();
        Intent thisIntent= getIntent();
        archiveId =thisIntent.getStringExtra("archiveId");
        setUI();
        setArchive();
    }
    private void setUI(){
        archiveViewImage = findViewById(R.id.archiveViewImage);
        archiveName = findViewById(R.id.archiveName);
        //archiveDesc = findViewById(R.id.archiveDesc);
        archiveLink = findViewById(R.id.archiveLink);
        archiveDetails = findViewById(R.id.archiveDetails);
        archiveSig = findViewById(R.id.archiveSig);
        archiveAuthor = findViewById(R.id.archiveAuthor);
    }
    public void setArchive() {
        Query query = firebaseDatabase.getInstance().getReference("Archive")
                .orderByChild("id")
                .equalTo(archiveId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        // String name;
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ArchiveMainModel archiveMainModel= snapshot.getValue(ArchiveMainModel.class);
                    archiveName.setText(archiveMainModel.getArchiveName().toUpperCase());
                    archiveAuthor.setText(archiveMainModel.getArchiveAuthor().toUpperCase());
                    archiveLink.setText(archiveMainModel.getArchiveLink());
                    archiveSig.setText(archiveMainModel.getArchiveSig());
                    archiveDetails.setText(archiveMainModel.getArchiveDetails());
                    StorageReference storageReference = firebaseStorage.getReference();
                    storageReference.child("Archive").child(archiveMainModel.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(archiveViewImage);
                            archiveViewImage.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
