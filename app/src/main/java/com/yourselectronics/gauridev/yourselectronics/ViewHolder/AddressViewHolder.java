package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yourselectronics.gauridev.yourselectronics.R;

public class AddressViewHolder extends RecyclerView.ViewHolder{
    private View mView;
    private TextView mAddress, mFullName;
    public RadioButton mSelectBtn;

    public AddressViewHolder(View itemView) {
        super(itemView);
        mView =itemView;

        mAddress = itemView.findViewById(R.id.card_full_address);
        mFullName = itemView.findViewById(R.id.add_full_name);
        mSelectBtn = itemView.findViewById(R.id.add_card_radio_btn);


    }

    public void setFull_address(String full_address) {
       mAddress.setText(full_address);
    }
    public void setFirst_name(String first_name) {
        mFullName.setText(first_name);

    }



}
