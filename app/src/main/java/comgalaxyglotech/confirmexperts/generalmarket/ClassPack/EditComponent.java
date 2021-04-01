package comgalaxyglotech.confirmexperts.generalmarket.ClassPack;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import comgalaxyglotech.confirmexperts.generalmarket.R;

public class EditComponent extends AppCompatActivity {
    private String callComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_edit_component);
        Intent prevIntent = getIntent();
        callComponent = prevIntent.getStringExtra("Component");
        if(callComponent.equals("Edit_Store")){
            setContentView(R.layout.activity_store_add);
        }
        else {
            setContentView(R.layout.activity_new_stock);
        }
    }
}
