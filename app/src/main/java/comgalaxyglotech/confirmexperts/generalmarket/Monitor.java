package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ELECTRON on 08/13/2020.
 */

public class Monitor {

    public void log(MonitorLog newLog){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Monitor");
        newLog.setId(databaseReference.getKey());
        databaseReference.child(newLog.getId()).setValue(newLog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //say whoosh!
                //we might launch a clean market protocol to bar markets and then encrypt log at a much later tim.
                //for now toodles!
            }
        });
    }
}
