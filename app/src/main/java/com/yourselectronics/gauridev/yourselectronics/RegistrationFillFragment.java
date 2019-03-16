package com.yourselectronics.gauridev.yourselectronics;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFillFragment extends Fragment {

    private EditText mName, mEmail, mPassword, mConfPassword;
    private CardView mGetStart, mLogInBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ProgressDialog mProgress;
    private Fragment goAddFragment;
    private String  phone;
    private TextView mAgreeText;
    private CheckBox mAgreeCheckBox;
    private CoordinatorLayout mRootLayout;
    private RadioButton mMaleBtn, mFemaleBtn, mOthersBtn;



    public RegistrationFillFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_fill, container, false);

        mAgreeText= view.findViewById(R.id.agree_text);
        mAgreeCheckBox = view.findViewById(R.id.agree_checkbox);
        mRootLayout = view.findViewById(R.id.regRootLayout);

        mMaleBtn = view.findViewById(R.id.add_male_btn);
        mFemaleBtn = view.findViewById(R.id.add_female_btn);
        mOthersBtn = view.findViewById(R.id.add_others_btn);

        String agree_text = "Agree to Yours Electronics Terms & Conditions and Privacy Policy";

        SpannableString spannableString = new SpannableString(agree_text);

        ClickableSpan clickableTerms = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TermsActivity.class);
                startActivity(intent);
            }

        };

        ClickableSpan clickableSPrivacy = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PrivacyActivity.class);
                startActivity(intent);
            }


        };
        spannableString.setSpan(clickableTerms,27,45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSPrivacy,50,64,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mAgreeText.setText(spannableString);
        mAgreeText.setMovementMethod(LinkMovementMethod.getInstance());


        mLogInBtn = view.findViewById(R.id.login_btn);
        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mGetStart = view.findViewById(R.id.register_btn);
        mName = view.findViewById(R.id.editName);
        mEmail = view.findViewById(R.id.editEmail);
        mPassword = view.findViewById(R.id.password);
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Creating Account");
        mProgress.setTitle("Please Wait...");
        mConfPassword = view.findViewById(R.id.re_enter_Password);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("user_database");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
             phone = getArguments().getString("id");
        }


        mGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSigningProcess();
            }
        });

        return view;


        }

    private void startSigningProcess() {

        final String name = mName.getText().toString();
        final String password = mPassword.getText().toString();
        final String email = mEmail.getText().toString();
        final String confirm_password = mConfPassword.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if(name.isEmpty()){
            mName.setError("Name is required");
            mName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            mEmail.setError("Name is required");
            mEmail.requestFocus();
            return;
        }
        if (!email.matches(emailPattern)){
            mEmail.setError("Enter Valid email Address");
            mEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            mPassword.setError("Password must be in 6 characters");
            mPassword.requestFocus();
            return;
        }


        if(confirm_password.isEmpty()){
            mConfPassword.setError("Confirm password is required");
            mConfPassword.requestFocus();
            return;
        }
        if(!confirm_password.equals(password)){
            mConfPassword.setError("Password don't matches");
            mConfPassword.requestFocus();
            return;
        }

        if(!mMaleBtn.isChecked() && !mFemaleBtn.isChecked() && !mOthersBtn.isChecked()){
            Snackbar.make(mRootLayout,"Please Select Gender...",Snackbar.LENGTH_LONG).show();

            return;
        }

        if (!mAgreeCheckBox.isChecked()){
            Snackbar.make(mRootLayout,"Please Accept Terms & Conditions and Privacy Policy...",Snackbar.LENGTH_LONG).show();
            return;
        }



        mProgress.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String user_uid = mAuth.getCurrentUser().getUid();
                    DatabaseReference mUser_db= mRef.child(user_uid);
                    Map user_data = new HashMap<>();
                    user_data.put("name",name);
                    user_data.put("email",email);
                    user_data.put("mobile",phone);

                    if (mMaleBtn.isChecked()){
                        user_data.put("gender","M");
                    }
                    if (mFemaleBtn.isChecked()){
                        user_data.put("gender","F");
                    }
                    if (mOthersBtn.isChecked()){
                        user_data.put("gender","O");

                    }


                    mUser_db.updateChildren(user_data, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (getActivity()!=null) {

                                goAddFragment = new AddressFillUpFragment();

                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.reg_frame, goAddFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                mProgress.dismiss();

                            }


                        }
                    });

                }
                if (!task.isSuccessful()){
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getActivity(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(),task.getException().toString(), Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();

                }

            }
        });

    }

}
