package comgalaxyglotech.confirmexperts.generalmarket.Controller.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Store.StoreAdd;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 01/29/2020.
 */

public class PaymentPop extends AppCompatDialogFragment {
    public boolean isPaid;
    private ImageView cancel_action;
    private paymentDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_pop,null);
        final double cost = StoreAdd.cost;
        final String cst = cost+"";
        TextView ownerType = view.findViewById(R.id.ownerType);
        TextView storeCost= view.findViewById(R.id.storeCost);
        TextView totalCost= view.findViewById(R.id.totalCost);
        Button payBtn = view.findViewById(R.id.payBtn);
        storeCost.setText(cst);
        totalCost.setText(cst);
        ownerType.setText(StoreAdd.ownerType);
        cancel_action= view.findViewById(R.id.cancel_action);
        String ss = "Pay "+cost+" Rent Fee";
        payBtn.setText(ss);
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // isPaid=false;
                listener.applyTexts(false,cost);
                dismiss();
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Processing Payment",Toast.LENGTH_SHORT).show();
                isPaid=true;
                listener.applyTexts(true,cost);
            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (paymentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement paymentDialogListener");
        }
    }

    public interface paymentDialogListener
    {
        void applyTexts(boolean isPaid, double fee);
    }
}
