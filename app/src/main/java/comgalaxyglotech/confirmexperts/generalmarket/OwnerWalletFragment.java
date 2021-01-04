package comgalaxyglotech.confirmexperts.generalmarket;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerWalletFragment extends Fragment {
    View view;
    private SwipeRefreshLayout swiper;
    private Button createWallet,expressPayBtn,cashOutBtn;
    //find an alternative to this memory leak in subsequent release
    public Button addMoney,sendMoney,copyAcct;
    private RelativeLayout createWalletPanel;
    private LinearLayout acctDetails;
    private ModelClass modelClass = new ModelClass();
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private TextView mainAcct,bonusAcct,refAcct,upFrontAcct,total;
    private String storeId;
    private Context context;
    private Activity activity;
    private EditText expressPay,cashOut;

    public OwnerWalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_owner_wallet, container, false);
        context= getContext();
        activity = getActivity();
        swiper = view.findViewById(R.id.swiper);
        Intent prevIntent = getActivity().getIntent();
        cashOutBtn = view.findViewById(R.id.cashOutBtn);
        cashOut = view.findViewById(R.id.cashOut);
        copyAcct = view.findViewById(R.id.copyAcct);
        storeId = prevIntent.getStringExtra("storeId");
        expressPayBtn = view.findViewById(R.id.expressPayBtn);
        expressPay = view.findViewById(R.id.expressPay);
        addMoney = view.findViewById(R.id.addMoney);
        mainAcct = view.findViewById(R.id.mainAcct);
        bonusAcct = view.findViewById(R.id.bonusAcct);
        refAcct = view.findViewById(R.id.refAcct);
        upFrontAcct = view.findViewById(R.id.upFrontAcct);
        total = view.findViewById(R.id.total);
        createWallet = view.findViewById(R.id.createWallet);
        createWalletPanel = view.findViewById(R.id.createWalletPanel);
        acctDetails = view.findViewById(R.id.acctDetails);
        progressBar = view.findViewById(R.id.progBar);
        sendMoney = view.findViewById(R.id.sendMoney);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        setListener();
        addMoney.setEnabled(false);
        sendMoney.setEnabled(false);
        copyAcct.setEnabled(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(modelClass.userLoggedIn()){
            isStoreWallet();
        }
        else {
            activity.finish();
            Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
        }
    }

    ProcessWallet processWallet = new ProcessWallet();
    private void setListener(){
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(modelClass.userLoggedIn()){
                    isStoreWallet();
                }
                else {
                    activity.finish();
                    Toast.makeText(context,"Log In Required",Toast.LENGTH_SHORT).show();
                }
            }
        });
        expressPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!expressPay.getText().toString().isEmpty()){
                    progressDialog.setMessage("Scanning Invoice Code...");
                    progressDialog.show();
                    CartInvoiceTransaction trans = new CartInvoiceTransaction();
                    trans.payInvoiceWithStore("-"+expressPay.getText().toString(),storeId, progressDialog,context,activity);
                }
                else{
                    Toast.makeText(context,"Invoice Address Is Empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cashOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cashOut.getText().toString().isEmpty()){
                    if(Double.parseDouble(cashOut.getText().toString())>=1000){
                        //send cashout request
                        cashOut.setText("");
                    }
                    else{
                        Toast.makeText(context, "Amount is less than 1000", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(context, "Fill Cashout Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        copyAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "-" was excluded
                String acctNo = storeId.substring(1);
                modelClass.copyToClipboard(context,acctNo,"Account Address Copied");
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,AddMoney.class);
                intent.putExtra("walletId",storeId);
                intent.putExtra("type","store");
                startActivity(intent);
            }
        });
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,SendMoney.class);
                intent.putExtra("walletId",storeId);
                intent.putExtra("type","store");
                startActivity(intent);
            }
        });
        createWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWallet.setVisibility(View.GONE);
                progressDialog.setMessage("Setting Up Store Wallet...");
                progressDialog.show();
                create_Store_Wallet(storeId,context,progressBar,progressDialog,createWallet, createWalletPanel,acctDetails,mainAcct,bonusAcct,refAcct,upFrontAcct,total);
            }
        });
    }
    //sets the parameter for the store wallet query
    private void isStoreWallet(){
        //action is d function to be called upon callback
        // String storeId, final ProgressBar progressBar, final Button btn, final RelativeLayout relativeLayout, final LinearLayout linearLayout, final TextView mainAcct, final TextView  bonusAcct, final TextView  refAcct, final TextView  upFrontAcct, final TextView  total
        processWallet.readData(new ProcessWallet.FirebaseCallback() {
            @Override
            public void onCallback( ArrayList<Model_Wallet> wallet_Model) {
                if(wallet_Model == null){
                    createWalletPanel.setVisibility(View.VISIBLE);
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
                    createWalletPanel.setVisibility(View.GONE);
                    acctDetails.setVisibility(View.VISIBLE);
                    sendMoney.setEnabled(true);
                    addMoney.setEnabled(true);
                    copyAcct.setEnabled(true);
                }
                progressBar.setVisibility(View.GONE);
                swiper.setRefreshing(false);

            }
        },"StoreWallet","userId",storeId);
    }
    private void create_Store_Wallet(final String storeId, final Context context, final ProgressBar progressBar, final ProgressDialog progressDialog, final Button btn, final RelativeLayout relativeLayout, final LinearLayout linearLayout, final TextView mainAcct, final TextView  bonusAcct, final TextView  refAcct, final TextView  upFrontAcct, final TextView  total){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoreWallet");
        final String id = databaseReference.push().getKey();
        Model_Wallet model_wallet = new Model_Wallet(id,storeId,0.0,0.0,0.0,0.0);
        databaseReference.child(id).setValue(model_wallet)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Wallet Created Successfully",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        isStoreWallet();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
