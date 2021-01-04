package comgalaxyglotech.confirmexperts.generalmarket;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Bonus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        TransitionAdvertDisplay.StopThisActivity.finish();
    }
}
