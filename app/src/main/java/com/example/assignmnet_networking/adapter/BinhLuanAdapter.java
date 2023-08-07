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
import com.example.assignmnet_networking.model.BinhLuan;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.viewHolder.BinhLuanHolder;
import com.example.assignmnet_networking.viewHolder.TruyenTranhHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanHolder> {

    private List<BinhLuan> list;
    private Context context;
    int clickLike = 0;
    int clickDislike = 0;
    public BinhLuanAdapter(List<BinhLuan> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BinhLuanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_binhluan, parent, false);
        return new BinhLuanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuanHolder holder, int position) {
        BinhLuan binhLuan = list.get(position);

        Calendar calendarToday = Calendar.getInstance();
        Date today = calendarToday.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date targetDate = null;
        try {
            targetDate = dateFormat.parse(binhLuan.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeDiff = today.getTime() - targetDate.getTime();

        long daysDiff = timeDiff / (24 * 60 * 60 * 1000);


        Glide.with(context)
                .load(binhLuan.getAvatar())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgAvatar);
        holder.tvName.setText(binhLuan.getName());
        holder.tvContent.setText(binhLuan.getContent());

        if (daysDiff >= 30 && daysDiff < 365) {
            int months = (int) (daysDiff / 30);
            holder.tvDate.setText(months + " tháng trước");
        } else if (daysDiff >= 365) {
            int years = (int) (daysDiff / 365);
            holder.tvDate.setText(years + " năm trước");
        } else {
            holder.tvDate.setText(daysDiff + " ngày trước");
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void reverseList() {
        Collections.reverse(list);
        notifyDataSetChanged();
    }
}
