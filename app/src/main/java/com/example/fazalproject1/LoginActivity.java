package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

//import static com.example.fazalproject1.Traveler.phoneNumber; // what's this?



public class LoginActivity extends AppCompatActivity  {

    EditText editTextPhone, editTextCode;
    Button register;
    ImageView submit;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CheckBox checkBox ;
    TextView termsOfService;

    FirebaseAuth mAuth;
    FirebaseFirestore db ;

    public static String userPhoneNumber;

    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkBox = findViewById(R.id.chkBox);
        termsOfService = findViewById(R.id.TermsOfService);


        mAuth = FirebaseAuth.getInstance();

        editTextPhone = findViewById(R.id.MobileNumber);
        submit = findViewById(R.id.submitButtonImg);
        editTextCode = findViewById(R.id.PhoneCode);
        register = findViewById(R.id.registerButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBtn();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextPhone.getText())){
                    editTextPhone.setError( "PhoneNumber is required!" );

                }
                else if(TextUtils.isEmpty(editTextCode.getText())) {
                    editTextCode.setError( "PhoneCode is required!" );

                }
                else if(!checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(),"Agree with the terms & conditions first!", Toast.LENGTH_LONG).show();
                }
                else {
                    //changes for anonymous user
                    registerBtn();
                }

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    userPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();

                    db = FirebaseFirestore.getInstance();

                    final DocumentReference documentReference = db.collection("UsersTable").document(userPhoneNumber);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null && documentSnapshot.exists()) {
                                    Intent intent = new Intent(LoginActivity.this, STRActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, EditStartUpProfile.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d("Androidview", e.getMessage());
                        }
                    });




                }
            }
        };



        termsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,TermsOfServiceActivity.class);
                startActivity(intent);
            }
        });








    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        mAuth.addAuthStateListener(mAuthListener);

    }

    private void verifySignInCode(){
        String code = editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(),
                                    "Login Successfull", Toast.LENGTH_LONG).show();
                            db = FirebaseFirestore.getInstance();

                            final DocumentReference documentReference = db.collection("UsersTable").document("+923062337855");
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if(documentSnapshot != null && documentSnapshot.exists()) {
                                            Intent intent = new Intent(LoginActivity.this, STRActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Intent intent = new Intent(LoginActivity.this, EditStartUpProfile.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            Log.d("Androidview", e.getMessage());
                                        }
                                    });
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {
        String phone = editTextPhone.getText().toString();
        String regexStr = "03[0-9]{2}[0-9]{7}";
        if (phone.isEmpty()) {
            editTextPhone.setError("Phone number must require");
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    public void submitBtn() {
        /*String phone = editTextPhone.getText().toString();
        String regexStr = "03[0-9]{2}[0-9]{7}";
        if (phone.length() != 11 || !phone.equals(regexStr)){
            editTextPhone.setError("Phone number must require & Valid");
        }
        else {*/
            sendVerificationCode();
            Toast.makeText(getApplicationContext(),"Code Sent", Toast.LENGTH_LONG).show();

    }
    public void registerBtn(){
        verifySignInCode();
    }
}
