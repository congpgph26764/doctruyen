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
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.viewHolder.TruyenTranhHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.callback.Callback;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhHolder> {

    private List<TruyenTranh> list;
    private Context context;

    private Callback callback;
    int clickCount = 0;


    public TruyenTranhAdapter(List<TruyenTranh> list, Context context, Callback callback) {
        this.list = list;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public TruyenTranhHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new TruyenTranhHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenTranhHolder holder, int position) {
        TruyenTranh truyenTranh = list.get(position);

        Glide.with(context)
                .load(truyenTranh.getThumbnail())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgThumbnail);
        holder.tvName.setText(truyenTranh.getName());

        holder.imgLike.setVisibility(View.INVISIBLE);


//        holder.imgLike.setOnClickListener(v ->{
//
//            if (clickCount % 2 == 0) {
//                holder.imgLike.setImageResource(R.drawable.heart_1);
//
//            } else {
//                holder.imgLike.setImageResource(R.drawable.heart);
//            }
//            clickCount++;
//        });

        holder.layoutTruyen.setOnClickListener(v ->{
            callback.chitiet(truyenTranh);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void filterList(ArrayList<TruyenTranh> filteredList){
        list = filteredList;
    }

    public interface Callback{
        void chitiet(TruyenTranh truyenTranh);
    }

    public void reverseList() {
        Collections.reverse(list);
        notifyDataSetChanged();
    }
}
