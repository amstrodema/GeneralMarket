package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ELECTRON on 02/13/2019.
 */

public class newMetricDialogue extends AppCompatDialogFragment {
    newMetricDialogListener listener;
    EditText measure,quantity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_metric_dialogue, null);
        measure=view.findViewById(R.id.metricMeasure);
        quantity=view.findViewById(R.id.metricQuantity);
        builder.setView(view)
                .setTitle("New Metric")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save Metric", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id,  itemID,  metric, creatorID; int quantiti;
                        id="0";itemID="0";metric=measure.getText().toString();creatorID="";quantiti=Integer.parseInt(quantity.getText().toString());
                        listener.applyTexts( id,  itemID,  metric,  creatorID, quantiti);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (newMetricDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement newMetricDialogListener");
        }
    }
    public interface newMetricDialogListener
    {
        void applyTexts(String id, String itemID, String metric, String creatorID, int quantity);
    }
}