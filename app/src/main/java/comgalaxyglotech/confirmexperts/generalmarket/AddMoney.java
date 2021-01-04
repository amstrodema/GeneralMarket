package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMoney extends AppCompatActivity {
    private Button payFund;
    private EditText amountPayable,cardNumber ,expiryMonth ,expiryYear ,cvv;
    private ProgressDialog progressDialog;
    private ModelClass modelClass = new ModelClass();
    private String walletId, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        PaystackSdk.initialize(getApplicationContext());
       // setPaystackKey();
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

                if(!amountPayable.getText().toString().isEmpty())
                if (Integer.parseInt(amountPayable.getText().toString().trim()) >= 500) {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    // This sets up the card and check for validity
                    // This is a test card from paystack
                    String _cardNumber = cardNumber.getText().toString();
                    int _expiryMonth = Integer.parseInt(expiryMonth.getText().toString()); //any month in the future
                    int _expiryYear =Integer.parseInt(expiryYear.getText().toString()); // any year in the future. '2018' would work also!
                    String _cvv = cvv.getText().toString() ;  // cvv of the test card

                    Card card = new Card(_cardNumber, _expiryMonth, _expiryYear, _cvv);
                    if(card.validNumber()) {
                        progressDialog.setMessage("Validating Card.... (1)");
                        if(card.validCVC()){
                            progressDialog.setMessage("Validating Card.... (2)");
                            if(card.validExpiryDate()){
                                progressDialog.setMessage("Validating Card.... (3)");
                                if (card.isValid()) {
                                    progressDialog.setMessage("Validating Card.... (4)");
                                    // charge card'
                                    ModelClass.paymentModel = new PaymentModel(card,walletId,type, Integer.parseInt(amountPayable.getText().toString().trim()));
                                    startActivity(new Intent(AddMoney.this, Payment_Loader.class));
                                    finish();
                                } else {
                                    //do something
                                    progressDialog.dismiss();
                                    Toast.makeText(AddMoney.this, "Invalid Card!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(AddMoney.this, "Card has expired!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(AddMoney.this, "Invalid Card CVV", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(AddMoney.this, "Invalid Card Number", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddMoney.this, "Minimum payable is ₦500", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(AddMoney.this, "Amount is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
   /* public static void setPaystackKey() {
        PaystackSdk.setPublicKey("pk_test_7b0176a35fa3b5473e1f61f5b790c2880b6085b6");
    }*/
}
