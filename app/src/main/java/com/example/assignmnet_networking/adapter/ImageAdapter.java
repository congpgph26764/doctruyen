package com.example.assignmnet_networking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignmnet_networking.R;
import com.example.assignmnet_networking.model.Image;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.viewHolder.ImageHolder;
import com.example.assignmnet_networking.viewHolder.TruyenTranhHolder;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

    private List<Image> list;
    private Context context;

    public ImageAdapter(List<Image> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image image = list.get(position);

        Glide.with(context)
                .load(image.getImage())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgTruyen);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
