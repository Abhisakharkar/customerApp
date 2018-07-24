package com.example.chinmay.anew;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText mobNo;
    private TextInputEditText age;
    private TextInputEditText password;
    private RadioGroup gender;
    private ActionProcessButton signUp;

    private String emailStr;
    private String passwordStr;
    private String nameStr;
    private String mobNoStr;
    private String ageStr;
    private String genderStr;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;

    private String flagPhone;
    private String flagEmail;

    private  View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.nameAtSignUpAct);
        email = findViewById(R.id.emailAtSignUpAct);
        mobNo = findViewById(R.id.mobNoAtSignUpAct);
        age = findViewById(R.id.ageAtSignUpAct);
        password = findViewById(R.id.passwordAtSignUpAct);
        gender = findViewById(R.id.genderRadGrpAtSignUpAct);
        signUp = findViewById(R.id.signUpBtnAtSignupAct);

        if(getIntent() == null){
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                if(radioButton != null){
                    genderStr = radioButton.getText().toString();
                }
            }
        });
    }

    private void attemptLogin() {
        // Reset errors.
        email.setError(null);
        password.setError(null);
        name.setError(null);
        mobNo.setError(null);
        age.setError(null);

        // Store values at the time of the login attempt.
        emailStr = email.getText().toString().trim().toLowerCase();
        passwordStr = password.getText().toString().trim();
        nameStr = name.getText().toString().trim();
        mobNoStr = mobNo.getText().toString().trim();
        ageStr = age.getText().toString().trim();

        boolean isValidated = validateField();
        addUser(isValidated);

    }

    private boolean validateField(){

        boolean cancel = false;

        if (TextUtils.isEmpty(emailStr)) {
            email.setError(getString(R.string.emailIsReq));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(ageStr)) {
            age.setError(getResources().getString(R.string.ageIsReq));
            focusView = age;
            cancel = true;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            password.setError(getString(R.string.passwordIsReq));
            focusView = password;
            cancel = true;
        }
        if(!isPasswordValid(passwordStr)){
            password.setError(getString(R.string.errorInvalidPass));
            cancel = true;
        }
        if (!isEmailValid(emailStr)) {
            email.setError(getString(R.string.errorInvalidEmail));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(mobNoStr) || mobNoStr.length() != 10) {
            mobNo.setError(getString(R.string.errorInvalidMob));
            focusView = mobNo;
            cancel = true;
        }
        if (TextUtils.isEmpty(nameStr)) {
            name.setError(getString(R.string.errorFeildReq));
            focusView = name;
            cancel = true;
        }
        if(genderStr == null || genderStr.isEmpty()){
            Toast.makeText(this,getResources().getString(R.string.errorGender),Toast.LENGTH_SHORT).show();
            focusView = gender;
            cancel = true;
        }

        try{
            long mobNo = Long.parseLong(mobNoStr);
        }catch (Exception e){
            mobNo.setError(getString(R.string.errorInvalidMob));
            cancel = true;
        }

        try{
            int age = Integer.parseInt(ageStr);
        }catch (Exception e){
            cancel = true;
            mobNo.setError(getString(R.string.errorInvalidAge));
        }

        return cancel;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void addUser(boolean isValidate){
        if (isValidate) {
            focusView.requestFocus();
        } else {
            signUp.setMode(ActionProcessButton.Mode.ENDLESS);
            signUp.setProgress(1);

            firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                storeData();
                            } else {
                                Toast.makeText(SignUp.this, getString(R.string.errorAuth), Toast.LENGTH_SHORT).show();
                                signUp.setProgress(-1);
                            }
                        }
                    }
            );


        }
    }

    private void storeData(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("UserRel").child(userId);
        myRef.child("Name").setValue(nameStr);
        myRef.child("Phone").setValue(mobNoStr);
        myRef.child("Age").setValue(ageStr);
        myRef.child("Complete").setValue("1");
        myRef.child("Email").setValue(emailStr);
        myRef.child("Gender").setValue(genderStr);
        signUp.setProgress(100);
        takeUserToMainPage();
    }

    private void takeUserToMainPage(){
        startActivity(new Intent(SignUp.this,MainActivity.class));
        finish();
    }

}

