package comgalaxyglotech.confirmexperts.generalmarket.Controller.Utilities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.Main8Activity;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class CleaningInProgress extends AppCompatActivity {

    private ImageView overrideBtn;
    private int counta = 0, count =0;
    private Button click;
    private TextView timerDisplay;
    private String EVENT_DATE_TIME;// = "07/09/2021 16:06:24 PM";
    private String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss a";
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_in_progress);
        overrideBtn = findViewById(R.id.overrideBtn);
        timerDisplay = findViewById(R.id.timerDisplay);
        click = findViewById(R.id.click);
        overrideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counta++;
            }
        });
        //override
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(counta >=10 && count >=10){
                    Intent intent = new Intent(CleaningInProgress.this, Main8Activity.class);
                    intent.putExtra("dataType","deal");
                    startActivity(intent);
                    finish();
                }
            }
        });
        Intent intent = getIntent();
        EVENT_DATE_TIME = intent.getStringExtra("date");
       // timerDisplay
        countDownStart();
    }
    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        String counta =Days+":"+Hours+":"+Minutes+":"+Seconds;
                        timerDisplay.setText(counta);
                    } else {
                       //log in
                        Intent intent = new Intent(CleaningInProgress.this, Main8Activity.class);
                        intent.putExtra("dataType","deal");
                        startActivity(intent);
                        finish();
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
