package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.Meta;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.util.List;

public class AddMoney extends AppCompatActivity {
    private Button payFund;
    private EditText amountPayable,cardNumber ,expiryMonth ,expiryYear ,cvv;
    private ProgressDialog progressDialog;
    private ModelClass modelClass = new ModelClass();
    private String walletId, type;
    private Activity activity= this;
    private Context context = this;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        Intent intent = getIntent();
        walletId = intent.getStringExtra("walletId");
        type = intent.getStringExtra("type");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        payFund = findViewById(R.id.payFund);
        amountPayable = findViewById(R.id.amountPayable);
        cardNumber = findViewById(R.id.cardNumber);
        expiryMonth = findViewById(R.id.expiryMonth);
        expiryYear = findViewById(R.id.expiryYear);
        cvv = findViewById(R.id.cvv);
        payFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Double.parseDouble(amountPayable.getText().toString().trim());
                if(!amountPayable.getText().toString().isEmpty())
                if (amount >= 500) {
                   /* progressDialog.setMessage("Loading...");
                    progressDialog.show();*/
                    String ref = modelClass.getCurrentUserId()+ modelClass.getDate();
                    RaveUiManager raveUiManager =  new RaveUiManager(activity).setAmount(amount)
                            .setCurrency("NGN")
                            .setEmail(modelClass.getCurrentUserMail())
                            .setfName("first Name")
                            .setlName("Last name")
                            .setNarration("narration")
                            .setPublicKey("FLWPUBK_TEST-e9f83cc2f6b1f0d112eadb0699129701-X")
                            .setEncryptionKey("FLWSECK_TESTa6f8fd9f170b")
                            .setTxRef(ref)
                           // .setPhoneNumber(phoneNumber, boolean)
                    .acceptAccountPayments(true)
                    .acceptCardPayments(true)
                    .acceptMpesaPayments(true)
                    .acceptAchPayments(true)
                    .acceptGHMobileMoneyPayments(true)
                    .acceptUgMobileMoneyPayments(true)
                    .acceptZmMobileMoneyPayments(true)
                    .acceptRwfMobileMoneyPayments(true)
                    .acceptSaBankPayments(true)
                    .acceptUkPayments(true)
                    .acceptBankTransferPayments(true)
                    .acceptUssdPayments(true)
                    .acceptBarterPayments(true)
                    .acceptFrancMobileMoneyPayments(true)
                    .allowSaveCardFeature(true)
                    .onStagingEnv(true)
                    /*.setMeta(List<Meta>)
                            .withTheme(styleId)
                            .isPreAuth(boolean)*/
                   /* .setSubAccounts(List<SubAccount>)
                            .shouldDisplayFee(boolean)*/
                    .showStagingLabel(true)
                    .initialize();
                }
                else{
                    Toast.makeText(AddMoney.this, "Minimum payable is ₦500", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(AddMoney.this, "Amount is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                ProcessWallet addToWallet = new ProcessWallet();
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                if(type.equals("personal")){
                    addToWallet.addTo_Wallet(amount, progressDialog,context,activity,walletId);
                }
                else{
                    addToWallet.addTo_Store_Wallet_Acct(walletId,amount, progressDialog,context,activity);
                }
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
