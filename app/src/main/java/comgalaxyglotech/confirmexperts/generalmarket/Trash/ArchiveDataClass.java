package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 10/06/2019.
 */

public class ArchiveDataClass {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static ArrayList<ArchiveRecipeDisplayModel> archiveData = new ArrayList<>();

    //sets the parameter for the query
    public void getData( final Context context, final Intent intent){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ArchiveRecipeDisplayModel> list) {
                archiveData=list;
                //activity.finish();
                context.startActivity(intent);
            }
        },"Archive","archiveName","");
    }

    //this methods access the required query resources in a listener and runs the query from getData
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ArchiveRecipeDisplayModel> itm = new ArrayList<ArchiveRecipeDisplayModel>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ArchiveMainModel newData= snapshot.getValue(ArchiveMainModel.class);
                        ArchiveRecipeDisplayModel thisData= new ArchiveRecipeDisplayModel(newData.getId(),newData.getArchiveName(),newData.getArchiveDesc(),newData.getArchiveSig(),"","Archive");
                        itm.add(thisData) ;

                    }
                    firebaseCallback.onCallback(itm);
                }
                else{
                    firebaseCallback.onCallback(itm);
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
        void onCallback (ArrayList<ArchiveRecipeDisplayModel> list);
    }
}
