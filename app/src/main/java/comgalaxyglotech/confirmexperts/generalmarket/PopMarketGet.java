package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 01/30/2020.
 */

public class PopMarketGet extends AppCompatDialogFragment{
    public boolean isFound = true;
    private ImageView cancel_action;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private Context context;
    private String marketId;
    private ArrayList<marketModel> mktList= new ArrayList<>();
    private AutoCompleteTextView marketName;
    private String[] mktNames=new String[]{"Zealot","Truce","Capanon","Strum","Vector"};
    ArrayAdapter<String> adapterItm;
    private ArrayList<String> verifyList= new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      //  locationFinder();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        context = getContext();
        View view = inflater.inflate(R.layout.pop_get_market,null);
        cancel_action= view.findViewById(R.id.cancel_action);
        marketName= view.findViewById(R.id.marketAuto);
        //marketName.setEnabled(true);
        adapterItm = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,mktNames);
        marketName.setAdapter(adapterItm);
        listener();
       //getData();
        builder.setCancelable(false);
        builder.setView(view)
                .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mkt = marketName.getText().toString().trim();
                      if(!mkt.equals("")){
                          mkt = mkt.toLowerCase();
                          if(verifyList.contains(mkt)){
                              int indx = verifyList.indexOf(mkt);
                              isFound=true;
                              marketId = mktList.get(indx).getMarketId();
                          }
                          else {
                              isFound =false;
                          }

                          if(isFound){
                              Intent intent = new Intent("comgalaxyglotech.confirmexperts.generalmarket.StoreAdd");
                              intent.putExtra("marketId",marketId);
                              startActivity(intent);
                          }
                          else{
                              openDialog();
                          }
                      }
                    }
                });
        return builder.create();
    }
    private void listener(){
        marketName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(verifyList.contains(s.toString().trim().toLowerCase())){
                }
                else{
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void openDialog(){
        marketDialog itemDialog = new marketDialog();
        itemDialog.show(getFragmentManager(),"Add New Market");
    }


}
