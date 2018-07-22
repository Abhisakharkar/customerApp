package com.example.chinmay.anew;

import android.animation.Animator;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapterRecyclerMainAc madapter;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View mProgressView;
    private TextView forget,login;
    int RC_SIGN_IN=3;
    private UserLoginTask mAuthTask = null;
    private String email,phone;
    private int done=0;


    private TextView phoneSignIn,emailSignIn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private static final String TAG = "FirebasePhoneNumAuth";

    private FirebaseAuth firebaseAuth;






    private FirebaseFirestore firestoreDB;
    private  FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private ArrayList<String> phoneList,emailList;
    private ProgressDialog prdialog;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.accountfragment, null);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        phoneList=new ArrayList<String>();
        emailList=new ArrayList<String>();
        //phoneSignIn=(TextView)view.findViewById(R.id.phonesign);
        emailSignIn=(TextView)view.findViewById(R.id.sign_up_button);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        createCallback();
        firestoreDB= FirebaseFirestore.getInstance();
        getInstallationIdentifier();
        prdialog = new ProgressDialog(getActivity());
        prdialog.setTitle("Loading");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();

                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        prdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prdialog.setCancelable(false);
        prdialog.show();
        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),SignUp.class);
                i.putExtra("flagphone","0");
                i.putExtra("flagemail","0");
                startActivity(i);
            }
        });
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
        //        if (done == 0) {
                    emailList.clear();
                    phoneList.clear();

                    for (DataSnapshot myitem : dataSnapshot.getChildren())

                    {
                        if (myitem.getKey().equals("UserRel")) {
                            for (final DataSnapshot myitem2 : myitem.getChildren()) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        emailList.add(myitem2.child("email").getValue().toString());
                                        phoneList.add(myitem2.child("phone").getValue().toString());




                                    }

                                },3000);

                            }
                        }
                    }
                    prdialog.cancel();
                    done = 1;


                }
           // }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     /*   phoneSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // Get the layout inflater
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View prompt = inflater.inflate(R.layout.dialog_phonesignin, null);


                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setView(prompt)

                            // Add action buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    EditText e1 = (EditText) prompt.findViewById(R.id.username);
                                    String s = e1.getText().toString();
                                    if(s.length()>=10) {

                                        phone=s;
                                        verifyPhoneNumberInit();
                       //                 getVerificationDataFromFirestoreAndVerify("123456");
                                        // sign in the user ...

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Check Number", Toast.LENGTH_SHORT).show();
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


        });*/
//        populateAutoComplete();

        mPasswordView = (EditText)  view.findViewById(R.id.password);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    //   Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mProgressView = view.findViewById(R.id.login_progress);
        forget=(TextView)view.findViewById(R.id.textView2);
        login=(TextView)view.findViewById(R.id.email_sign_in_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View prompt = inflater.inflate(R.layout.dialog_forget, null);


                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(prompt)

                        // Add action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText e1 = (EditText) prompt.findViewById(R.id.username);
                                String s = e1.getText().toString();
                                if(!s.equals("")) {


                                    // sign in the user ...
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
                                else
                                {
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



        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return view;
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuth.signInWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //  Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            if(task.isSuccessful())
                            {

                                //  Toast.makeText(MainActivity.this, "hvghv", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                                int flag=0;
                                for(int i=0;i<emailList.size();i++)
                                {
                                    if(mEmailView.getText().equals(emailList.get(i)))
                                    {
                                        flag=1;
                                        break;
                                    }
                                }
                                if(flag==0)
                                {
                                    Intent i=new Intent(getActivity(),SignUp.class);
                                    i.putExtra("email",""+mEmailView.getText());
                                    i.putExtra("flagphone","0");
                                    i.putExtra("flagemail","1");
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "You have already signed up", Toast.LENGTH_SHORT).show();
                                    //Open Required Activity

                                }
                                //finish();


                            }

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(getActivity(), "signInWithEmail:failed", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }

                            // ...
                        }
                    });
            showProgress(true);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }
   

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //  Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(getActivity(), "signInWithEmail:failed", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            showProgress(false);

            if (success) {

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }
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
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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





}