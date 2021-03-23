package comgalaxyglotech.confirmexperts.generalmarket.Controller.Bonus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.TransitionAdvertDisplay;

public class Bonus extends AppCompatActivity {
    private pl.droidsonroids.gif.GifImageView claimBtn;
    private ImageView cancel;
    private RelativeLayout claimPage, oopsPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        TransitionAdvertDisplay.StopThisActivity.finish();
      setup();
      clickListener();
    }
    private void setup(){
        claimBtn = findViewById(R.id.claimBtn);
        oopsPage = findViewById(R.id.oopsPage);
        claimPage = findViewById(R.id.claimPage);
        cancel = findViewById(R.id.cancel);
    }
    private void clickListener(){
        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oopsPage.setVisibility(View.VISIBLE);
                claimPage.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claimPage.setVisibility(View.VISIBLE);
                oopsPage.setVisibility(View.GONE);
            }
        });
    }
}
