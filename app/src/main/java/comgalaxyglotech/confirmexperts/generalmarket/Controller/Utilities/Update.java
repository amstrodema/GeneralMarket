package comgalaxyglotech.confirmexperts.generalmarket.Controller.Utilities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class Update extends AppCompatActivity {
    private Button update, ignore;
    private String allow,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        update = findViewById(R.id.update);
        ignore = findViewById(R.id.ignore);
        Intent intent = getIntent();
        allow = intent.getStringExtra("allow");
        date = intent.getStringExtra("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
       try {
           Date event_date = dateFormat.parse(date);
           Date current_date = new Date();
           if (!current_date.after(event_date)) {
               ignore.setVisibility(View.VISIBLE);
           }
       }
       catch (Exception ignore){}
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to playstore
            }
        });
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, HomePage.class);
                intent.putExtra("dataType","deal");
                startActivity(intent);
                finish();
            }
        });
    }
}
