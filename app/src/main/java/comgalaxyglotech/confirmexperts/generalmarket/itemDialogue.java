package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by ELECTRON on 01/29/2019.
 */

public class itemDialogue extends AppCompatDialogFragment {
    itemDialogListener listener;
    EditText ItemName;
    Spinner newItemClass;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog,null);
        ItemName= view.findViewById(R.id.newItem);
        newItemClass = view.findViewById(R.id.newItemClass);
        builder.setView(view)
                .setTitle("Add New Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add New Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemName, NewItemClass;
                        NewItemClass = newItemClass.getSelectedItem().toString();
                        itemName= ItemName.getText().toString();
                      if(itemName.isEmpty()){
                          ItemName.setHint("Fill This Field");
                          ItemName.setHintTextColor(Color.RED);
                      }
                      else{
                          listener.applyTexts(itemName,NewItemClass);
                      }
                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (itemDialogue.itemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement itemDialogListener");
        }
    }

    public interface itemDialogListener
    {
        void applyTexts(String itemName,String newItemClass);
    }
}
