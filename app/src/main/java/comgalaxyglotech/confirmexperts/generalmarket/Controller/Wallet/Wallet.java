package comgalaxyglotech.confirmexperts.generalmarket.Controller.Wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Account.Account;
import comgalaxyglotech.confirmexperts.generalmarket.Controller.Transaction.AddMoney;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Transaction.CartInvoiceTransaction;
import comgalaxyglotech.confirmexperts.generalmarket.Main8Activity;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Wallet.Model_Wallet;
import comgalaxyglotech.confirmexperts.generalmarket.BL.Process.ProcessWallet;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.SendMoney;
import comgalaxyglotech.confirmexperts.generalmarket.Trade;

public class Wallet extends AppCompatActivity {
    private SwipeRefreshLayout swiper;
    private Button createWallet,expressPayBtn;
    public Button sendMoney,addMoney, copyAcct;
    private RelativeLayout createWalletPanel;
    private LinearLayout acctDetails;
    private ModelClass modelClass = new ModelClass();
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private TextView mainAcct,bonusAcct,refAcct,upFrontAcct,total;
    private Context context = this;
    private Activity activity = this;
    private EditText expressPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        swiper = findViewById(R.id.swiper);
        expressPay =  findViewById(R.id.expressPay);
        expressPayBtn =  findViewById(R.id.expressPayBtn);
        addMoney =  findViewById(R.id.addMoney);
        mainAcct =  findViewById(R.id.mainAcct);
        bonusAcct =  findViewById(R.id.bonusAcct);
        refAcct =  findViewById(R.id.refAcct);
        upFrontAcct =  findViewById(R.id.upFrontAcct);
        total =  findViewById(R.id.total);
        copyAcct =  findViewById(R.id.copyAcct);
        createWallet =  findViewById(R.id.createWallet);
        createWalletPanel =  findViewById(R.id.createWalletPanel);
        acctDetails =  findViewById(R.id.acctDetails);
        progressBar =  findViewById(R.id.progBar);
        sendMoney =  findViewById(R.id.sendMoney);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        addMoney.setEnabled(false);
        sendMoney.setEnabled(false);
        copyAcct.setEnabled(false);
        basePanelListeners();
        setListener();
    }
    @Override
    public void onStart() {
        super.onStart();
        loadWallet();
    }

    ProcessWallet processWallet = new ProcessWallet();
    public void loadWallet(){
        if(modelClass.userLoggedIn()){
            isWallet(modelClass.getCurrentUserId(),progressBar,createWallet, createWalletPanel,acctDetails,mainAcct,bonusAcct,refAcct,upFrontAcct,total);
        }
        else {
            activity.finish();
            Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
        }
    }

    //(final String invoiceId, final ProgressDialog progressDialog, final Context context, final Activity activity)
    private void setListener(){
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWallet();
            }
        });
        expressPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!expressPay.getText().toString().isEmpty()){
                    progressDialog.setMessage("Scanning Invoice Code...");
                    progressDialog.show();
                    CartInvoiceTransaction trans = new CartInvoiceTransaction();
                    trans.payInvoice("-"+expressPay.getText().toString(),modelClass.getCurrentUserId(), progressDialog,context,activity);
                }
                else{
                    Toast.makeText(context,"Invoice Address Is Empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        copyAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"-"was excluded
                String acctNo = modelClass.getCurrentUserId();
                modelClass.copyToClipboard(context,acctNo,"Account Address Copied");
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Wallet.this, AddMoney.class);
                intent.putExtra("walletId",modelClass.getCurrentUserId());
                intent.putExtra("type","personal");
                startActivity(intent);
            }
        });
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Wallet.this, SendMoney.class);
                intent.putExtra("walletId",modelClass.getCurrentUserId());
                intent.putExtra("type","personal");
                startActivity(intent);
            }
        });
        createWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWallet.setVisibility(View.GONE);
                progressDialog.setMessage("Setting Up Personal Wallet...");
                progressDialog.show();
                ProcessWallet processWallet = new ProcessWallet();
                create_Wallet(modelClass.getCurrentUserId(),context,progressBar,progressDialog,createWallet, createWalletPanel,acctDetails,mainAcct,bonusAcct,refAcct,upFrontAcct,total);
            }
        });
    }
    private void isWallet(String userId, final ProgressBar progressBar, final Button btn, final RelativeLayout relativeLayout, final LinearLayout linearLayout, final TextView mainAcct, final TextView  bonusAcct, final TextView  refAcct, final TextView  upFrontAcct, final TextView  total){
        //action is d function to be called upon callback
        processWallet.readData(new ProcessWallet.FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model == null){
                    progressBar.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                }
                else
                {
                    Model_Wallet wallet = wallet_Model.get(0);
                    double tot = wallet.getMainWallet() + wallet.getBonusWallet()+ wallet.getReferralWallet() - wallet.getUpfrontWallet();
                    mainAcct.setText(wallet.getMainWallet()+"");
                    bonusAcct.setText(wallet.getBonusWallet()+"");
                    refAcct.setText(wallet.getReferralWallet()+"");
                    upFrontAcct.setText(wallet.getUpfrontWallet()+"");
                    total.setText(tot+"");
                    relativeLayout.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    sendMoney.setEnabled(true);
                    String stat =modelClass.getCurrentUserMail();
                    if(stat.equals("oluyinkabiz@gmail.com")){
                        addMoney.setEnabled(true);
                    }
                    swiper.setRefreshing(false);
                    copyAcct.setEnabled(true);
                }

            }
        },"Wallet","userId",userId);
    }
    private void create_Wallet(final String userId, final Context context, final ProgressBar progressBar, final ProgressDialog progressDialog, final Button btn, final RelativeLayout relativeLayout, final LinearLayout linearLayout, final TextView mainAcct, final TextView  bonusAcct, final TextView  refAcct, final TextView  upFrontAcct, final TextView  total){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
        final String id = databaseReference.push().getKey();
        Model_Wallet model_wallet = new Model_Wallet(id,userId,0.0,0.0,0.0,0.0);
        databaseReference.child(id).setValue(model_wallet)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Wallet Created Successfully",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        isWallet(userId, progressBar, btn, relativeLayout, linearLayout,mainAcct,bonusAcct,refAcct,upFrontAcct,total);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void basePanelListeners(){
        Button home, account, market, store;
        home = findViewById(R.id.home_floor);
        account = findViewById(R.id.account);
        market = findViewById(R.id.notification);
        store = findViewById(R.id.setting);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main8Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this, Account.class));
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(Wallet.this,Wallet.class));
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wallet.this, Trade.class));
            }
        });
    }
}
