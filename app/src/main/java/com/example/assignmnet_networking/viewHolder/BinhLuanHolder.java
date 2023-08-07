package com.example.assignmnet_networking.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmnet_networking.R;

public class BinhLuanHolder extends RecyclerView.ViewHolder {
    public ImageView imgAvatar;
    public TextView tvName;
    public TextView tvContent;
    public TextView tvDate;

    public BinhLuanHolder(@NonNull View itemView) {
        super(itemView);

        imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
    }
}
