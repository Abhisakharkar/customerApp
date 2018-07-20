package com.example.chinmay.anew;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {


    private String phone;
    private EditText name, contact, password,gender,age,email;
    private int flag=0;
    private ProgressDialog prdialog;
    private int once=0;

    private FirebaseAuth mAuth;
    private ImageView i1;

    private ArrayList<String> listemail;
    private int done=0;
    private int photo=0;
    private DatabaseReference myRef;
    private String email1,password1,name1,contact1,age1,roleq,gender1;
    private String username3;
    private String[] subject;
    private Uri selectedImage1;
    public static final int GET_FROM_GALLERY1 = 1;
    DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String flagphone;
    private String flagemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Bundle b=getIntent().getExtras();
        name = (EditText) findViewById(R.id.name);
        contact = (EditText) findViewById(R.id.contact);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);

        if(b!=null)
        {
            email1=b.getString("email");
            phone=b.getString("phone");
            flagphone=b.getString("flagphone");
            flagemail=b.getString("flagemail");
        }
        if(flagphone.equals("1"))
        {
            contact.setText(phone);
        }
        if(flagemail.equals("1"))
        {
            email.setText(email1);
        }
        mAuth = FirebaseAuth.getInstance();

    }
    public void signup(View view)
    {
        attemptLogin();
    }

    private void attemptLogin() {
        flag = 0;


        // Reset errors.
        email.setError(null);
        password.setError(null);
        name.setError(null);
        contact.setError(null);
        age.setError(null);
        gender.setError(null);


        // Store values at the time of the login attempt.
        email1 = email.getText().toString();
        password1 = password.getText().toString();
        name1 = name.getText().toString();
        contact1 = contact.getText().toString();
        gender1 = gender.getText().toString();
        age1 = age.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.


        // Check for a valid email address.
        if (TextUtils.isEmpty(email1)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(age1)) {
            age.setError("Age is required");
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(password1)) {
            password.setError("Password is required");
            focusView = email;
            cancel = true;
        }
        if (!(gender1.equals("M") || gender1.equals("F"))) {
            gender.setError("Gender is incorrect");
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(email1)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(contact1)) {
            contact.setError(getString(R.string.error_field_required));
            //  focusView = contact;
            cancel = true;
        }
        if (TextUtils.isEmpty(name1)) {
            name.setError(getString(R.string.error_field_required));
            //   focusView = name;
            cancel = true;
        }




        if ( cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //focusView.requestFocus();
        } else {
                    if(flagemail.equals("0"))
                     {

                         mAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {



                                    } else {
                                        Toast.makeText(SignUp.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                         myRef = FirebaseDatabase.getInstance().getReference().child("UserRel").child(name1);
                         DatabaseReference postsRef = myRef;                                       //

                         postsRef.child("name").setValue(name1);
                         postsRef.child("phone").setValue(contact1);
                         postsRef.child("age").setValue(age1);
                         postsRef.child("complete").setValue("1");


                         //
                         postsRef.child("email").setValue(email1);

                         postsRef.child("gender").setValue(gender1);
                         finish();
        }
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }


}
