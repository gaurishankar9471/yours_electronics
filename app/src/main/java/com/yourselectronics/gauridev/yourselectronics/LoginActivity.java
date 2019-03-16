package com.yourselectronics.gauridev.yourselectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Build;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class LoginActivity extends AppCompatActivity {

    private CardView mLogin_btn, mSignUp;
    private EditText mEditEmailLog,mEditPasswordLog;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        setContentView(R.layout.activity_login);

        mRef = FirebaseDatabase.getInstance().getReference().child("user_database");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Logging In");
        mProgress.setTitle("Please Wait....");
        mProgress.setCanceledOnTouchOutside(false);



        mLogin_btn = findViewById(R.id.main_login_btn);
        mSignUp =findViewById(R.id.go_sign_up);
        mEditEmailLog = findViewById(R.id.editEmailLog);
        mEditPasswordLog = findViewById(R.id.editPasswordLog);

        mAuth = FirebaseAuth.getInstance();



        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(LoginActivity.this,"Please Login", Toast.LENGTH_LONG).show();
                mProgress.show();
                startLogin();

            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startLogin() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email = mEditEmailLog.getText().toString();
        final String password = mEditPasswordLog.getText().toString();

        if(email.isEmpty()){
            mEditEmailLog.setError("Email is Required");
            mEditEmailLog.requestFocus();
            mProgress.dismiss();

            return;
        }
        if (!email.matches(emailPattern)){
            mEditEmailLog.setError("Enter Valid email Address");
            mEditEmailLog.requestFocus();
            mProgress.dismiss();
            return;
        }
        if(password.isEmpty()){
            mEditPasswordLog.setError("Password is Required");
            mEditPasswordLog.requestFocus();
            mProgress.dismiss();
            return;
        }



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ((task.isSuccessful())){

                    String user_id = mAuth.getCurrentUser().getUid();
                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    mRef.child(user_id).child("token_id").setValue(token_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(LoginActivity.this,"Logged in Successfully", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                        }
                    });



                    }

                if (!task.isSuccessful()){
                    mProgress.dismiss();
                    if (task.getException() instanceof FirebaseAuthEmailException){
                        Toast.makeText(LoginActivity.this,"Wrong Email", Toast.LENGTH_LONG).show();

                    }

                   if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                      Toast.makeText(LoginActivity.this,"Wrong Password", Toast.LENGTH_LONG).show();

                  }

                }
            }
        });

    }


}
