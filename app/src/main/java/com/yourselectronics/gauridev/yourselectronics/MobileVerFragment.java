package com.yourselectronics.gauridev.yourselectronics;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MobileVerFragment extends Fragment {
   private CardView mSendOTP,mLogInBtn;
   private Fragment fragmentRegFill;
   private EditText mEditPhone;
   private ProgressDialog mProgress;
   private ImageView closeIcon;
   private String codeSent;


    public MobileVerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_ver, container, false);

        closeIcon = view.findViewById(R.id.mobile_ver_close_icon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null)
                getActivity().finish();
            }
        });
        mLogInBtn = view.findViewById(R.id.login_btn);
        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mProgress = new ProgressDialog(getActivity());
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setTitle("Please Wait...");
        mProgress.setMessage("Sending OTP");

        mSendOTP = view.findViewById(R.id.send_OTP);
        mSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        mEditPhone = view.findViewById(R.id.editPhoneNum);





        return view;
    }

    private void sendVerificationCode() {
        final  String phone = mEditPhone.getText().toString();

        if(phone.isEmpty()){
            mEditPhone.setError("Phone number is required");
            mEditPhone.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            mEditPhone.setError("Please enter a valid phone");
            mEditPhone.requestFocus();
            return;
        }
        mProgress.show();


        if (getActivity()!=null)

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity  (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential){

            final String phone = mEditPhone.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString("id", phone);
            fragmentRegFill = new RegistrationFillFragment();
            fragmentRegFill.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.reg_frame, fragmentRegFill);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mProgress.dismiss();
            Toast.makeText(getActivity(), "OTP Verified", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(),"Verification Failed\n Please try again...",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(getActivity(),"OTP Sent Successfully",Toast.LENGTH_LONG).show();
            mProgress.setMessage("Fetching OTP");
            codeSent = s;
        }
    };


}
