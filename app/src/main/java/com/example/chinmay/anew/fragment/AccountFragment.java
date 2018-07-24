package com.example.chinmay.anew.fragment;

import android.animation.Animator;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.SignUp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Belal on 1/23/2018.
 */

public class AccountFragment extends Fragment {

    private EditText emailTv;
    private EditText passwordTv;
    private ActionProcessButton loginBtn;
    private TextView logout;
    private TextView signUp;
    private TextView forget;
    private LinearLayout profileLayout;
    private RelativeLayout loginLayout;

    private int RC_SIGN_IN = 3;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;

    private String emailStr;
    private String password;
    private View focusView = null;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private static final String TAG = "FirebasePhoneNumAuth";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.accountfragment, null);

        emailTv = view.findViewById(R.id.emailAtAccountFrag);
        passwordTv = view.findViewById(R.id.passwordAtAccountFrag);
        loginBtn = view.findViewById(R.id.loginBtn);
        signUp = view.findViewById(R.id.signUpBtnAtLoginFrag);
        forget = view.findViewById(R.id.forgetPassword);
        profileLayout = view.findViewById(R.id.userProfileLayout);
        loginLayout = view.findViewById(R.id.loginLayout);
        logout = view.findViewById(R.id.logout);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            profileLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }else{
            profileLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }

       //    getInstallationIdentifier();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        getActivity().findViewById(R.id.googleLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),SignUp.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                profileLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                loginBtn.setProgress(0);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View prompt = inflater.inflate(R.layout.dialog_forget, null);
                builder.setView(prompt)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText e1 = (EditText) prompt.findViewById(R.id.username);
                                String s = e1.getText().toString();
                                if(!s.equals("")) {
                                    FirebaseAuth.getInstance().sendPasswordResetEmail(s)
                                            .addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_LONG).show();
                                                }


                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Check Network Connection/Check Email", Toast.LENGTH_LONG).show();
                                        }


                                    });
                                }
                                else {
                                    Toast.makeText(getActivity(), "Check email", Toast.LENGTH_SHORT).show();
                                }

                                builder.setView(prompt).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                AlertDialog d=builder.create();
                d.show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        loginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
        loginBtn.setProgress(1);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String personName = acct.getDisplayName();
                            String personEmail = acct.getEmail();
                            Uri personPhoto = acct.getPhotoUrl();
                            String personPhotoURL = acct.getPhotoUrl().toString();

                            myRef = FirebaseDatabase.getInstance().getReference().child("UserRel").child(firebaseAuth.getCurrentUser().getUid());
                            myRef.child("Name").setValue(personName);
                            myRef.child("Email").setValue(personEmail);
                            myRef.child("Image").setValue(personPhotoURL);
                            loginBtn.setProgress(100);
                            signed();
                        }else {
                            loginBtn.setProgress(-1);
                            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.errorAuth),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void attemptLogin() {
        emailTv.setError(null);
        passwordTv.setError(null);

        emailStr = emailTv.getText().toString();
        password = passwordTv.getText().toString();

        if (validateField()) {
            focusView.requestFocus();
        } else {
           loginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
           loginBtn.setProgress(1);
           firebaseAuth.signInWithEmailAndPassword(emailStr,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       loginBtn.setProgress(100);
                       signed();
                   }else{
                       loginBtn.setProgress(-1);
                       Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.errorAuth),Toast.LENGTH_SHORT).show();
                   }
               }
           });
        }
    }

    private boolean validateField(){
        boolean cancel = false;

        if (TextUtils.isEmpty(password)) {
            passwordTv.setError(getString(R.string.passwordIsReq));
            focusView = passwordTv;
            cancel = true;
        }

        if(!isPasswordValid(password)){
            passwordTv.setError(getString(R.string.errorInvalidPass));
            cancel = true;
            focusView = passwordTv;
        }

        if (TextUtils.isEmpty(emailStr)) {
            emailTv.setError(getString(R.string.emailIsReq));
            focusView = emailTv;
            cancel = true;
        } else if (!isEmailValid(emailStr)) {
            emailTv.setError(getString(R.string.errorInvalidEmail));
            focusView = emailTv;
            cancel = true;
        }
        return cancel;
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    private void signed(){
        profileLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
    }



/*
    private void createCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "verification completed" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getActivity(),
                            "Trying too many times",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "code sent " + verificationId);
                addVerificationDataToFirestore(phone, verificationId);
            }
        };
    }
    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            //   phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "code verified signIn successful");
                            firebaseUser = task.getResult().getUser();
                            firebaseUser.getDisplayName();
                            Toast.makeText(getActivity(), "Signed In"  , Toast.LENGTH_SHORT).show();
                            int flag=0;
                            for(int i=0;i<phoneList.size();i++)
                            {
                                if(phone.equals(phoneList.get(i)))
                                {
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==0)
                            {
                                Intent i=new Intent(getActivity(),SignUp.class);
                                i.putExtra("phone",""+phone);
                                i.putExtra("flagphone","1");
                                i.putExtra("flagemail","0");
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "You have already signed up", Toast.LENGTH_SHORT).show();
                                //Open Required Activity

                            }

                        } else {
                            Log.w(TAG, "code verification failed", task.getException());
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                //   verifyCodeET.setError("Invalid code.");
                            }
                        }
                    }
                });
    }
    private void createCredentialSignIn(String verificationId, String verifyCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.
                getCredential(verificationId, verifyCode);
        signInWithPhoneAuthCredential(credential);
    }
    private void addVerificationDataToFirestore(String phone, String verificationId) {
        Map verifyMap = new HashMap();
        verifyMap.put("phone", phone);
        verifyMap.put("verificationId", verificationId);
        verifyMap.put("timestamp",System.currentTimeMillis());

        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .set(verifyMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "phone auth info added to db ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding phone auth info", e);
                    }
                });
    }
    private void getVerificationDataFromFirestoreAndVerify(final String code) {

        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            if(ds.exists()){
                                disableSendCodeButton(ds.getLong("timestamp"));
                                if(code != null){
                                    createCredentialSignIn(ds.getString("verificationId"),
                                            code);
                                }else{
                                    verifyPhoneNumber(ds.getString("phone"));
                                }
                            }else{

                                Log.d(TAG, "Code hasn't been sent yet");
                            }

                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }
    public synchronized String getInstallationIdentifier() {
        if (uniqueIdentifier == null) {
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                    UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueIdentifier = sharedPrefs.getString(UNIQUE_ID, null);
            if (uniqueIdentifier == null) {
                uniqueIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(UNIQUE_ID, uniqueIdentifier);
                editor.commit();
            }
        }
        return uniqueIdentifier;
    }
    private void disableSendCodeButton(long codeSentTimestamp){
        long timeElapsed = System.currentTimeMillis()- codeSentTimestamp;
        if(timeElapsed > ONE_HOUR_MILLI){

        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(getActivity(), "Signed In", Toast.LENGTH_SHORT).show();

            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    private void verifyPhoneNumberInit() {

        if (!validatePhoneNumber(phone)) {
            return;
        }
        verifyPhoneNumber(phone);

    }
    private void verifyPhoneNumber(String phno){
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phno, 70,
                TimeUnit.SECONDS, getActivity(), callbacks);
    }

    */

}