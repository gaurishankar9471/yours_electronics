package com.yourselectronics.gauridev.yourselectronics;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFillUpFragment extends Fragment {
    private CardView mSaveAddBtn;
    private EditText mEditFirstName, mEditLastName, mEditFullAddress, mEditPinCode, mEditState, mEditMobileNo_Address;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    public AddressFillUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_fill_up, container, false);

        mRef = FirebaseDatabase.getInstance().getReference().child("user_database");
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mProgress.setTitle("Please Wait...");
        mProgress.setMessage("Saving Address");

        mEditFirstName = view.findViewById(R.id.editFirstName);
        mEditLastName = view.findViewById(R.id.editLastName);
        mEditFullAddress = view.findViewById(R.id.editFullAddress);
        mEditPinCode = view.findViewById(R.id.editPinCode);
        mEditState = view.findViewById(R.id.editState);
        mEditMobileNo_Address = view.findViewById(R.id.editMobileNo_Address);






        mSaveAddBtn = view.findViewById(R.id.save_add_btn);
        mSaveAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startSavingAddress();
                }
        });
        return view;
    }

    private void startSavingAddress() {
        final String first_name = mEditFirstName.getText().toString();
        final String last_name = mEditLastName.getText().toString();
        final String full_address = mEditFullAddress.getText().toString();
        final String pin_code = mEditPinCode.getText().toString();
        final String state_name = mEditState.getText().toString();
        final String mobile_no = mEditMobileNo_Address.getText().toString();

        if(first_name.isEmpty()){
            mEditFirstName.setError("First Name is required");
            mEditFirstName.requestFocus();
            return;
        }
        if(last_name.isEmpty()){
            mEditLastName.setError("Last Name is required");
            mEditLastName.requestFocus();
            return;
        }
        if(full_address.isEmpty()){
            mEditFullAddress.setError("Full Address is required");
            mEditFullAddress.requestFocus();
            return;
        }
        if(pin_code.isEmpty()){
            mEditPinCode.setError("Pin Code is required");
            mEditPinCode.requestFocus();
            return;
        }
        if(pin_code.length()<6 || pin_code.length()>6){
            mEditPinCode.setError("Enter a valid pin");
            mEditPinCode.requestFocus();
            return;
        }
        if(state_name.isEmpty()){
            mEditState.setError("State is required");
            mEditState.requestFocus();
            return;
        }
        if(mobile_no.isEmpty()){
            mEditMobileNo_Address.setError("Mobile No. is required");
            mEditMobileNo_Address.requestFocus();
            return;
        }
        if(mobile_no.length()<10 || mobile_no.length()>10){
            mEditMobileNo_Address.setError("Mobile No. must be in 10 digit");
            mEditMobileNo_Address.requestFocus();
            return;
        }
        mProgress.show();


        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference mUser_db= mRef.child(user_uid);


        DatabaseReference mUserAddress = mUser_db.child("address").push();
        Map address_data = new HashMap<>();
        address_data.put("first_name",first_name);
        address_data.put("last_name",last_name);
        address_data.put("full_address",full_address);
        address_data.put("pin_code",pin_code);
        address_data.put("state_name",state_name);
        address_data.put("mobile_no",mobile_no);
        address_data.put("last_selected_address",false);


        mUserAddress.updateChildren(address_data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                mProgress.dismiss();

                if (databaseError != null) {
                    Toast.makeText(getActivity(),"Error in Saving...",Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(),"Saved Successfully...",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    }
            }



        });




        }



}
