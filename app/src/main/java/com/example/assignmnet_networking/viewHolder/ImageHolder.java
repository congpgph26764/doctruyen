package com.example.assignmnet_networking.viewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmnet_networking.R;

public class ImageHolder extends RecyclerView.ViewHolder {

    public ImageView imgTruyen;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);

        imgTruyen = (ImageView) itemView.findViewById(R.id.imgTruyen);
    }
}
