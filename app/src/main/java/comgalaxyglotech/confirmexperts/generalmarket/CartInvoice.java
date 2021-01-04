package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartInvoice extends AppCompatActivity {
    private EditText invoiceCode;
    private Button proceed,clearList,makeInvoiceCode;
    private TextView invoiceList, invoiceTotal,invoiceDate;
    private double total;
    private ModelClass modelClass = new ModelClass();
    private Activity activity = this;
    private ProgressDialog progressDialog;
    private Context context = this;
    private ImageView copyBtn;
    private int counta=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_invoice);
        if(FragStockStore.storeCart.isEmpty()){
            Toast.makeText(this,"Cart Invoice Is Empty",Toast.LENGTH_SHORT).show();
            this.finish();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        setUI();
        final String invoice_Code = modelClass.getNewId();
        String itemz;
        String storeId = "";
        int count =0;
        StringBuilder builder = new StringBuilder();
        for (CartModel item:FragStockStore.storeCart) {
            count++;
            /*if(storeId.isEmpty() || !storeId.equals(item.getStockToBuy().getStoreId())){
                storeId= item.getStockToBuy().getStoreId();
                String val = storeId+"\n";
                builder.append(val);
            }*/
            double mainCost = item.getItemQty()* Float.parseFloat(item.getStockToBuy().getItemCost());
            itemz= count+":\t"+ item.getStockToBuy().getAdvertName()+"\t ₦"+item.getStockToBuy().getItemCost()+" x "+item.getItemQty()+" = "+mainCost+"\n";
            total += mainCost;
            builder.append(itemz);
        }
        final String totl ="₦"+total;
        invoiceDate.setText(modelClass.getDate());
        invoiceList.setText(builder.toString());
        invoiceTotal.setText(totl);
        //sets the invoice code
        invoiceCode.setText(invoice_Code.substring(1));
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     final Model_Transaction modelTransaction= new Model_Transaction();
                modelTransaction.setItmQty(1.0);
                modelTransaction.setBuyerId(modelClass.getCurrentUserId());
                modelTransaction.setItemId(StoreItems.EditReference.getId());
                double cost = total;
                modelTransaction.setItemCost(cost);
                modelTransaction.setStoreId(StoreItems.EditReference.getStoreId());*/
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Purchase Alert!!!");
                alertDialog.setMessage("Pay for items on this Invoice?\nBill: "+totl);//₦ comes with totl
                alertDialog.setIcon(R.drawable.transaction_pics);
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //foot the bill
                        progressDialog.setMessage("Paying Invoice Items...");
                        progressDialog.show();
                        CartInvoiceTransaction invoiceTransaction = new CartInvoiceTransaction(FragStockStore.storeCart,progressDialog,context,activity);
                        invoiceTransaction.footInvoice();
                    }
                });
                alertDialog.show();
               // FragStockStore.storeCart.clear();
               // activity.finish();
            }
        });
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragStockStore.storeCart.clear();
                activity.finish();
            }
        });
        makeInvoiceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Invoice Alert!!!");
                alertDialog.setMessage("Proceed to log this invoice?\nBill: "+totl);//₦ comes with totl
                alertDialog.setIcon(R.drawable.transaction_pics);
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Logging Invoice...");
                        progressDialog.show();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");
                        final String id = invoice_Code;
                        CartInvoiceModel model = new CartInvoiceModel(id,modelClass.getCurrentUserId(),"Unpaid","",modelClass.getDate(),total,FragStockStore.storeCart.size());
                        databaseReference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                for (CartModel cart: FragStockStore.storeCart) {
                                    counta++;
                                    DatabaseReference  databaseReferenceContent = FirebaseDatabase.getInstance().getReference("Cart");
                                    final String cartId = databaseReferenceContent.push().getKey();
                                    cart.setId(cartId);
                                    cart.setInvoiceId(id);
                                    assert cartId != null;
                                    databaseReferenceContent.child(cartId).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if(counta==FragStockStore.storeCart.size()){
                                                FragStockStore.storeCart.clear();
                                                Toast.makeText(context,"Invoice Logged Successfully",Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                activity.finish();
                                            }
                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(counta==FragStockStore.storeCart.size()){
                                    Toast.makeText(context,"Invoice Logging Failed",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context,"Invoice Logging Failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelClass.copyToClipboard(context,invoiceCode.getText().toString(),"Copied Invoice Code");
            }
        });
    }
    private void setUI(){
        copyBtn = findViewById(R.id.copyBtn);
        makeInvoiceCode = findViewById(R.id.makeInvoiceCode);
        clearList = findViewById(R.id.clearList);
        invoiceDate = findViewById(R.id.invoiceDate);
        invoiceCode = findViewById(R.id.invoiceCode);
        proceed = findViewById(R.id.proceed);
        invoiceList = findViewById(R.id.invoiceList);
        invoiceTotal = findViewById(R.id.invoiceTotal);
    }
}
