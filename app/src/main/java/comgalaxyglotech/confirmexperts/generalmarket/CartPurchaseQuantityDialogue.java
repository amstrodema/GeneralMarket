package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ELECTRON on 01/29/2019.
 */

public class CartPurchaseQuantityDialogue extends AppCompatDialogFragment {
    public static NewStockMainModel thisItem;
    private TextView stockQuantityCost;
    private boolean isValid;
    private float itemQty;
    private EditText stockQuantity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cart_purchase_quantity_dialogue,null);

        stockQuantity = view.findViewById(R.id.stockQuantity);
        TextView stockQuantityLbl = view.findViewById(R.id.stockQuantityLbl);

        stockQuantityCost = view.findViewById(R.id.stockQuantityCost);
        String title = "Select Purchase Quantity For "+thisItem.getAdvertName();
        stockQuantityLbl.setText(title);
        definer(stockQuantityCost.getText().toString());
        stockQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                stockQuantity.setText("");
            }
        });
        stockQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                   definer(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //title for the item quantity
        //stockQuantityLbl
        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Add To Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      if(isValid && thisItem!= null){
                          CartModel cartModel = new CartModel(thisItem, itemQty,"","");
                          for (CartModel item: FragStockStore.storeCart) {
                              if(item.getStockToBuy().getId().equals(cartModel.getStockToBuy().getId())){
                                  FragStockStore.storeCart.remove(item);
                                  break;
                              }
                          }
                          FragStockStore.storeCart.add(cartModel);
                          Toast.makeText(getContext(),"Added to Invoice",Toast.LENGTH_SHORT).show();
                      }
                      else{
                         dismiss();
                      }
                    }
                });
        return builder.create();
    }
   private void definer(String s){
       try{
           itemQty =Float.parseFloat(s);
           String price = "Total Cost: "+(itemQty * Double.parseDouble(thisItem.getItemCost()));
           stockQuantityCost.setText(price);
           isValid=true;
           if(itemQty==0)
               isValid =false;
       }
       catch (Exception e){
           isValid =false;
       }
   }
}
