package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class Payment_Loader extends AppCompatActivity {
    ModelClass modelClass = new ModelClass();
    ProgressDialog progressDialog;
    Context context = this;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_loader);
        progressDialog = new ProgressDialog(this);
        performCharge();
    }
    // This is the subroutine you will call after creating the charge
    // adding a card and setting the access_code
    public void performCharge(){
        Card card = ModelClass.paymentModel.getCard();
        //create a Charge object
        Toast.makeText(Payment_Loader.this, "Charge Card", Toast.LENGTH_SHORT).show();
        Charge charge = new Charge();
        charge.setCard(card); //sets the card to charge
        ProcessWallet addToWallet = new ProcessWallet();
        charge.setAmount(ModelClass.paymentModel.getAmount());
        charge.setEmail(modelClass.getCurrentUserMail());
        String ref = modelClass.getCurrentUserId()+ modelClass.getDate();
        charge.setReference(ref);
       /* setCurrency, setPlan, setSubaccount,
        setTransactionCharge, setAmount, setEmail, setReference, setBearer,
                putMetadata, putCustomField */

        PaystackSdk.chargeCard(Payment_Loader.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                if(ModelClass.paymentModel.getType().equals("personal")){
                    addToWallet.addTo_Wallet(ModelClass.paymentModel.getAmount(), progressDialog,context,activity,ModelClass.paymentModel.getWalletId());
                }
                else{
                    addToWallet.addTo_Store_Wallet_Acct(ModelClass.paymentModel.getWalletId(),ModelClass.paymentModel.getAmount(), progressDialog,context,activity);
                }
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
              //  Toast.makeText(Payment_Loader.this, "before validate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
                Toast.makeText(Payment_Loader.this, "error "+error, Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}