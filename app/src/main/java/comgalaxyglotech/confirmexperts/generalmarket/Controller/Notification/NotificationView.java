package comgalaxyglotech.confirmexperts.generalmarket.Controller.Notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Notification.NotificationAdapter;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Notification.NotificationModel;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/*import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;*/

public class NotificationView extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private RecyclerView mRecyclerView;
    public NotificationAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ArrayList<NotificationModel> notificationList = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private ProgressBar progBar;
    private ModelClass model = new ModelClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if(!model.userLoggedIn())
            this.finish();
        else{
        swiper = findViewById(R.id.swiper);
        mRecyclerView = findViewById(R.id.notification);
        progBar = findViewById(R.id.progBar);
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mAdapter = new NotificationAdapter(notificationList);
        mRecyclerView.setAdapter(mAdapter);
        loadNotify();
        swipeRefresh();}
    }
    private void swipeRefresh(){
        //swipe to refresh
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//do something
                loadNotify();
            }
        });
    }
    public void loadNotify(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<NotificationModel> notifyModel) {
             //returns a list vale
                swiper.setRefreshing(false);
                progBar.setVisibility(View.GONE);
                markAsRead(notifyModel);
                //modify the notifications as read using a recursive function
            }
        },"Notification","toId",model.getCurrentUserId());
    }
    private void markAsRead(ArrayList<NotificationModel>list){
        Log.d("Empty", "Empty");
        if(list != null && !list.isEmpty()){
            Log.d("Empty", "Check");
            NotificationModel notification = list.get(0);
            if(!notification.getIsSeen().equals("True")) {
                Log.d("Empty", "In");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
                notification.setIsSeen("True");
                databaseReference.child(notification.getId()).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        list.remove(0);
                        markAsRead(list);
                        Log.d("Empty", "Done");
                    }
                });
            }
            else{
                list.remove(0);
                markAsRead(list);
            }
        }else Log.d("Empty", "Empty");

    }
    //sets the parameter for the query
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NotificationModel newData= snapshot.getValue(NotificationModel.class);
                        notificationList.add(newData);
                    }
                    mAdapter.notifyDataSetChanged();
                    ArrayList<NotificationModel> list = new ArrayList<>(notificationList);
                    firebaseCallback.onCallback(list);
                }
                firebaseCallback.onCallback(null);
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
        void onCallback (ArrayList<NotificationModel> notifyModel);
    }
}
