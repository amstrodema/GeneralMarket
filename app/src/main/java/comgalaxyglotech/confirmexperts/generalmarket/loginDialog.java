package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by ELECTRON on 02/13/2019.
 */

public class loginDialog extends AppCompatDialogFragment {
    private EditText value1;
    private EditText value2;
    loginDialog.dialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_dialogue,null);
        value1 =view.findViewById(R.id.logInEmail);
        value2 =view.findViewById(R.id.logInPassword);
        ImageButton cancelLogIn = view.findViewById(R.id.cancelLogIn);
        Button logInBtn = view.findViewById(R.id.logInBtn);
        cancelLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val1 = value1.getText().toString();
                String val2 = value2.getText().toString();
                if(val2.isEmpty() || val1.isEmpty()){
                    Toast.makeText(getContext(), "Empty Field(s)", Toast.LENGTH_SHORT).show();
                }
                else{
                    listener.applyTexts(val1,val2);
                }


            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (loginDialog.dialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement marketDialogListener");
        }
    }

    public interface dialogListener
    {
        void applyTexts(String thisValue1, String thisValue2);
    }
}
