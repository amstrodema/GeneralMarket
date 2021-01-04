package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 02/24/2020.
 */

public class ProcessFavourites {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private String UserId, ItemId;
    private ImageView FavView;
    private boolean isExist =false;
    public void setFav(final String userId, final String itemId, final ImageView favBtn){
        UserId = userId;
        ItemId = itemId;
        FavView =favBtn;
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ModelFavey> favModel) {
               // FavModel = favModel.get(0);
              for(ModelFavey fav : favModel){
                  if(fav.getItemId().equals(itemId)){
                      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Favey").child(fav.getId());
                      databaseReference.removeValue();
                      favBtn.setImageResource(R.drawable.fav);
                      isExist=true;
                  }
              }
              if(!isExist){
                  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Favey");
                  final String id = databaseReference.push().getKey();
                  ModelFavey modelFavey = new ModelFavey(id,UserId,ItemId);
                  databaseReference.child(id).setValue(modelFavey).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          FavView.setImageResource(R.drawable.favey_click);
                      }
                  });
              }
            }
        },"Favey","userId",userId);
    }

    //sets the parameter for the query
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModelFavey> favModelz=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ModelFavey newData= snapshot.getValue(ModelFavey.class);
                        favModelz.add(newData);
                    }
                    firebaseCallback.onCallback(favModelz);
                }
                else{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Favey");
                    final String id = databaseReference.push().getKey();
                    ModelFavey modelFavey = new ModelFavey(id,UserId,ItemId);
                    databaseReference.child(id).setValue(modelFavey).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FavView.setImageResource(R.drawable.favey_click);
                        }
                    });
                }
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

    private interface FirebaseCallback{
        void onCallback (ArrayList<ModelFavey> favModel);
    }
}
