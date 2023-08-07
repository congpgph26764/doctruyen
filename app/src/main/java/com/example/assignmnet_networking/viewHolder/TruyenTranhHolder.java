package com.example.assignmnet_networking.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmnet_networking.R;

public class TruyenTranhHolder extends RecyclerView.ViewHolder {

    public ImageView imgThumbnail;
    public TextView tvName;
    public ImageView imgLike;
    public ConstraintLayout layoutTruyen;

    public TruyenTranhHolder(@NonNull View itemView) {
        super(itemView);

        imgThumbnail = (ImageView) itemView.findViewById(R.id.imgThumbnail);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
        layoutTruyen = (ConstraintLayout) itemView.findViewById(R.id.layoutTruyen);
    }
}
