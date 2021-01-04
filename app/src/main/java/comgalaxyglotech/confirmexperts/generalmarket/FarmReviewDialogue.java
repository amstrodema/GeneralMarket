package comgalaxyglotech.confirmexperts.generalmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ELECTRON on 11/12/2019.
 */

public class FarmReviewDialogue extends AppCompatDialogFragment {
    public static Context context;
    public static String storeId;
    public static Activity activity;
    public static TextView noReviewLabel;
    public static RatingBar ratingBar;
    private ProgressDialog progressDialog;
    private UserDataClass userDataClass = new UserDataClass();
    private DatabaseReference databaseReference;
    private TextView MarketReviewLabel;
    ModelClass model= new ModelClass();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.store_review_dialogue, null);
        MarketReviewLabel = view.findViewById(R.id.MarketReviewLabel);
        MarketReviewLabel.setText("Write something about this farm");
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save Review", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText review = view.findViewById(R.id.storeReviewDialogue);
                        RatingBar rating = view.findViewById(R.id.storeRatingDialog);
                        progressDialog= new ProgressDialog(context);
                        progressDialog.setMessage("Saving Review");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        databaseReference= FirebaseDatabase.getInstance().getReference("FarmReview");
                        String creatorID, id= databaseReference.push().getKey();
                        creatorID=model.getCurrentUserId();
                        ReviewMainClass reviewMainClass =new ReviewMainClass(id,creatorID,review.getText().toString(),storeId,rating.getRating());
                        databaseReference.child(storeId).child(creatorID).setValue(reviewMainClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                userDataClass.getData(3,storeId,context,activity,noReviewLabel);
                                Toast.makeText(context,"Review Saved Successfully",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
        return builder.create();
    }
}