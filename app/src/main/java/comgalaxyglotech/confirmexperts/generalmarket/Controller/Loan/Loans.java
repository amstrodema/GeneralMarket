package comgalaxyglotech.confirmexperts.generalmarket.Controller.Loan;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay;

public class Loans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);
        TransitionAdvertDisplay.StopThisActivity.finish();
    }
}
