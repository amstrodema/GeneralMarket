package comgalaxyglotech.confirmexperts.generalmarket.BL.Notification;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Notification.NotificationModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;

/**
 * Created by ELECTRON on 08/17/2020.
 */

public class NotificationAdd {
    public void notice(final String fromId, final String toId,final String title, final String body, final Context context, final int channelId){
        ModelClass modelClass = new ModelClass();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
        final String id = databaseReference.push().getKey();
        NotificationModel notification = new NotificationModel(id,fromId,toId, body, "False",modelClass.getDate(),"", channelId);
        databaseReference.child(notification.getId()).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Notify notify = new Notify();
                notify.notifyAlert(context,title, body,channelId);
            }
        });
    }
    public void ownerNotice(final String fromId, final String toId,final String title, final String body, final Context context, final int channelId){
        ModelClass modelClass = new ModelClass();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
        final String id = databaseReference.push().getKey();
        NotificationModel notification = new NotificationModel(id,fromId,toId, body, "False",modelClass.getDate(),"", channelId);
        databaseReference.child(notification.getId()).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Waiting for seller...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
