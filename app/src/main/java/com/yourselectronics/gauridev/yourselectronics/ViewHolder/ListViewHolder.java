package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yourselectronics.gauridev.yourselectronics.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    private TextView list;
    public ListViewHolder(View itemView) {
        super(itemView);
        list = itemView.findViewById(R.id.itemText);
    }
    public void setList_text(String list_text) {
        list.setText(list_text);
    }
}
