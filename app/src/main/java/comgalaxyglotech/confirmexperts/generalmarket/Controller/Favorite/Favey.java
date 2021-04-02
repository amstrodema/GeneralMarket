package comgalaxyglotech.confirmexperts.generalmarket.Controller.Favorite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay;

public class Favey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favey);
        TransitionAdvertDisplay.StopThisActivity.finish();
    }
}
