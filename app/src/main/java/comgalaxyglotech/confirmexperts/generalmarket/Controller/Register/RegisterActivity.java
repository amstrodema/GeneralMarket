package comgalaxyglotech.confirmexperts.generalmarket.Controller.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.User.UserProfile;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.R;

public class RegisterActivity extends AppCompatActivity {
   private Button registerBtn;
   private EditText fname,lname,pass,confirmPass,email,country,phone;
   private FirebaseAuth firebaseAuth;
   ProgressDialog progressDialog;
   private ImageView regAdv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setParameter();FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();//Main_Page_Two
        storageReference.child("Advert").child("Reg_Page_Adv").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(regAdv);
            }
        });
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                if(getEditTextValue(pass).trim().length() < 6){
                    Toast.makeText(RegisterActivity.this,"Password Too Short", Toast.LENGTH_SHORT).show();
                }
                else if (validate()){
                    //save to database
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(getEditTextValue(email).trim(), getEditTextValue(pass).trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendEmailVerfication();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"Failed, try a different email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private void setParameter()
    {
        regAdv =  findViewById(R.id.regAdv);
         fname = findViewById(R.id.regFname);
         lname = findViewById(R.id.regLName);
         pass = findViewById(R.id.regPassword);
         confirmPass = findViewById(R.id.regConfirmPass);
         email = findViewById(R.id.regEmail);
         country = findViewById(R.id.regCountry);
        registerBtn = findViewById(R.id.regUserBtn);
        phone = findViewById(R.id.regPhone);
    }
    private  boolean validate()
    {
        boolean result= false;

        if (editTextIsEmpty(fname) || editTextIsEmpty(lname) || editTextIsEmpty(pass) || editTextIsEmpty(confirmPass) || editTextIsEmpty(email) || editTextIsEmpty(country) || editTextIsEmpty(phone) ){
            Toast.makeText(RegisterActivity.this, "Please fill all the fields accurately", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if ( getEditTextValue(pass).equals(getEditTextValue(confirmPass))){
                result= true;
            }
            else {
                Toast.makeText(RegisterActivity.this,"Password is not consistent!",Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }
    private boolean editTextIsEmpty(EditText thisEditText){
        String value = thisEditText.getText().toString();
        if (value.isEmpty())
        {
            return true;
        }
        return false;
    }
    public String getEditTextValue(EditText thisEditText){
        return thisEditText.getText().toString();
    }
    private void sendEmailVerfication(){
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegisterActivity.this,"Verification sent to your mail!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, HomePage.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Verification mail not sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("userProfile");
        String id = firebaseAuth.getUid();
        //generates random id but we need userID  ** String id = reference.push().getKey();
        UserProfile newProfile = new UserProfile(id,getEditTextValue(fname),getEditTextValue(lname),getEditTextValue(email),getEditTextValue(country),getEditTextValue(phone));
        reference.child(id).setValue(newProfile);
    }
}
