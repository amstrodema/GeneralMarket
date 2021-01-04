package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by ELECTRON on 08/17/2020.
 */

public class SampleWorker  extends Worker {
    private static String TAG ="Notify";
    private Context context = getApplicationContext();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public SampleWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //accepting two value
        Data inputData = getInputData();
        //this could be notification, storeFee and other type such as change in some settings
        final String selectWorkerType = inputData.getString("type");

        ModelClass modelClass = new ModelClass();
        Log.d(TAG,"before");
        //all functions that require a mandatory userLog in
        //most background workers maybe mutually exclusive
        if(modelClass.userLoggedIn()) {
            Log.d(TAG,"logged In");
            //alert unread notification
            getNotification(modelClass.getCurrentUserId());


                //this occurs for checking rentFee periodically
                //checkRentFee


            //mutually exclusive ones should be placed below


        }
        //if type does not require log in, place below

        return Result.success();
    }

    private void getNotification(final String id){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<NotificationModel> notifications) {
                         if(notifications != null && !notifications.isEmpty()){
                             Notify newNotification = new Notify();
                            /* for (NotificationModel notify: notifications) {
                                 newNotification.notifyAlert(getApplicationContext(),"You Have "+notifications.size()+" Unread Notifications",notify.getBody());
                             }*/
                             newNotification.notifyAlert(getApplicationContext(),"You Have "+notifications.size()+" Unread Notifications","A buyer might be waiting for you!",670);

                         }
            }
        },"Notification","toId",id);
    }
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<NotificationModel> notifications=new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NotificationModel notification= snapshot.getValue(NotificationModel.class);
                        if(notification.getIsSeen().equals("False")){
                            notifications.add(notification);
                        }

                    }
                    firebaseCallback.onCallback(notifications);
                }
                else{
                    firebaseCallback.onCallback(null);
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
        void onCallback (ArrayList<NotificationModel> notification);
    }
}
