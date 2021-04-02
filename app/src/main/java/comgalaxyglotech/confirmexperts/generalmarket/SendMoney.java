package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Process.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store.StoreMainModel;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.User.UserProfile;

public class SendMoney extends AppCompatActivity {
    private Button sendMoney;
    private EditText amountToSend, recipientAddress;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private ProgressDialog progressDialog;
    private String senderWalletId;
    private Context context= this;
    private Activity activity = this;
    private Double amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sendMoney = findViewById(R.id.sendMoney);
        amountToSend = findViewById(R.id.amountToSend);
        recipientAddress = findViewById(R.id.walletCode);
        Intent intent = getIntent();
        senderWalletId = intent.getStringExtra("walletId");
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check wallet code
                if(!amountToSend.getText().toString().isEmpty()){
                double amount = Double.parseDouble(amountToSend.getText().toString());
                amt = amount;
                if (amount >= 50) {
                  if(!recipientAddress.getText().toString().isEmpty()) {
                      //check walletCode
                      progressDialog.setMessage("Verifying Recipient Wallet..");
                      progressDialog.show();
                      getProfile(recipientAddress.getText().toString(), senderWalletId, amount, context, activity);
                      //if the wallet remains un-found, check for store profile
                  }
                  else{
                      Toast.makeText(context, "Recipient Wallet Address Is Empty", Toast.LENGTH_SHORT).show();
                  }
                } else {
                    Toast.makeText(SendMoney.this, "Amount is too low!", Toast.LENGTH_SHORT).show();
                }
            }
            else
                    Toast.makeText(SendMoney.this, "Amount is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getProfile(final String profileId, final String senderWalletId, final Double amount, final Context context, final Activity activity){
        //profileId of the recipient of the funds been sent. If the user profile is not found then check if a store is to receive the
        //funds. If it's not store then, the wallet address is wrong

        //action is d function to be called upon callback
        readData(new FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<UserProfile> profile) {
                UserProfile userProfile = profile.get(0);
                if(userProfile != null){
                    ProcessWallet processWallet = new ProcessWallet();
                    //specify that the recipent wallet is a personal wallet
                    processWallet.validatePersonalWallet("personal",amount,progressDialog,context,activity,senderWalletId,profileId);
                    //calling validatePersonalWallet since sender can be both personal or store
                    //if validatePersonalWallet returns null, then we can check the store
                }
            }
        },"userProfile","id",profileId);
    }
    private void readData(final FirebaseCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserProfile> profile = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserProfile userProfile= snapshot.getValue(UserProfile.class);
                        //UserProfile prof = new  UserProfile(wallet_Mode.getId(),wallet_Mode.getUserId(),wallet_Mode.getMainWallet(),wallet_Mode.getBonusWallet(),wallet_Mode.getReferralWallet(),wallet_Mode.getUpfrontWallet());
                        profile.add(userProfile);

                    }
                    firebaseCallback.onCallback(profile);
                }
                else{
                    progressDialog.setMessage("Scanning Wallet Address...");

                    String code = "-"+recipientAddress.getText().toString();
                    getStoreProfile(code, senderWalletId, amt, context, activity);
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
        void onCallback (ArrayList<UserProfile> profile);
    }

///use getstore profile if the personal account returns null. It implies the account bn looked for is a store rather than a personal or
    //individiual account

    private void getStoreProfile(final String profileId, final String senderWalletId, final Double amount, final Context context, final Activity activity){
        //profileId is the reference to the Id of each wallet
        //action is d function to be called upon callback
        readStoreData(new FirebaseStoreCallback() {
            @Override
            public void onCallback( ArrayList<StoreMainModel> profile) {
                StoreMainModel storeProfile = profile.get(0);
                if(storeProfile != null){
                    progressDialog.setMessage("Validating Recipient Wallet...");
                    ProcessWallet processWallet = new ProcessWallet();
                    //specify the recipient wallet type is not personal
                    processWallet.validatePersonalWallet("store",amount,progressDialog,context,activity,senderWalletId,profileId);
              //calling validatePersonalWallet since sender can be both personal or store
                    //if validatePersonalWallet returns null, then we can check the store
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(SendMoney.this, "Recipient Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        },"AllStores","id",profileId);
    }
    private void readStoreData(final FirebaseStoreCallback firebaseCallback, String reference, String OrderBy, String id){
        ValueEventListener valueEventListener = new ValueEventListener() {
            // String name;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StoreMainModel> profile = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        StoreMainModel userProfile= snapshot.getValue(StoreMainModel.class);
                        profile.add(userProfile);

                    }
                    //We disabled this function 1-jan-2021 1:23pm
                    //current policy states that funds cannot be transferred to store wallet in any regards
                  //  firebaseCallback.onCallback(profile);
                    progressDialog.dismiss();
                    Toast.makeText(SendMoney.this, "Forbidden Transaction! Refer to our T&C", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(SendMoney.this, "Recipient Wallet Not Found!", Toast.LENGTH_SHORT).show();
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
    private interface FirebaseStoreCallback{
        void onCallback (ArrayList<StoreMainModel> profile);
    }
}
