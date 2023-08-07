package com.example.assignmnet_networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignmnet_networking.adapter.BinhLuanAdapter;
import com.example.assignmnet_networking.adapter.TruyenTranhAdapter;
import com.example.assignmnet_networking.model.BinhLuan;
import com.example.assignmnet_networking.model.Image;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.service.ServiceBinhLuan;
import com.example.assignmnet_networking.service.ServiceTruyenTranh;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChiTietTruyen extends AppCompatActivity {

    private ImageView imgThumbnail;
    private TextView tvName;
    private TextView tvAuthor;
    private TextView tvYear;
    private TextView tvDescription;

    private Retrofit retrofit ;
    private ServiceTruyenTranh serviceTruyenTranh;
    private ServiceBinhLuan serviceBinhLuan;

    private RecyclerView rcvBinhLuan;
    private List<BinhLuan> list;
    private BinhLuanAdapter adapter;
    public static final String URL = "http://10.24.31.43:3000/api/";

    private String truyenTranhId = "";
    private String truyenTranhName = "";

    private String userId ="";
    private String fullname ="";
    private String avatar ="";
    private String soluong ="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_truyen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvYear = (TextView) findViewById(R.id.tvYear);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        processToken();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                truyenTranhId = bundle.getString("truyenTranh");

            }
        }

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceBinhLuan = retrofit.create(ServiceBinhLuan.class);

        getItem();

        findViewById(R.id.btnRead).setOnClickListener(v ->{
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);

            Intent intent1 = new Intent(this, DocTruyen.class);

            Bundle bundle = new Bundle();
            bundle.putString("truyenTranh", truyenTranhId);
            bundle.putString("truyenTranhName", truyenTranhName);
            intent1.putExtras(bundle);

            startActivity(intent1, options.toBundle());
        });

        findViewById(R.id.btnComment).setOnClickListener(v ->{
            getList();
            binhLuan();
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getItem(){

        serviceTruyenTranh = retrofit.create(ServiceTruyenTranh.class);

        Call<TruyenTranh> call = serviceTruyenTranh.findById(truyenTranhId);
        call.enqueue(new Callback<TruyenTranh>() {
            @Override
            public void onResponse(Call<TruyenTranh> call, Response<TruyenTranh> response) {
                if (response.isSuccessful()) {
                    TruyenTranh truyenTranh = response.body();

                    Glide.with(ChiTietTruyen.this)
                            .load(truyenTranh.getThumbnail())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(imgThumbnail);

                    tvName.setText(truyenTranh.getName());
                    tvAuthor.setText("Tác giả: "+truyenTranh.getAuthor());
                    tvYear.setText("Phát hành: "+truyenTranh.getYear());
                    tvDescription.setText(truyenTranh.getDescription());
                    truyenTranhName= truyenTranh.getName();

                } else {
                    Toast.makeText(ChiTietTruyen.this, "Lấy thông tin truyện không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TruyenTranh> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
                Toast.makeText(ChiTietTruyen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getList() {

        Call<List<BinhLuan>> call = serviceBinhLuan.getAll();
        call.enqueue(new Callback<List<BinhLuan>>() {

            @Override
            public void onResponse(Call<List<BinhLuan>> call, Response<List<BinhLuan>> response) {
                getData(response.body());
            }

            @Override
            public void onFailure(Call<List<BinhLuan>> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
                Toast.makeText(ChiTietTruyen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData(List<BinhLuan> list ){

        List<BinhLuan> list1 = new ArrayList<>();
        for (BinhLuan binhLuan: list) {
            if (binhLuan.getId_truyen().equals(truyenTranhId)) {
                list1.add(binhLuan);
            }
        }
        soluong = String.valueOf(list1.size());
        adapter = new BinhLuanAdapter(list1, this);
        adapter.reverseList();
        rcvBinhLuan.setAdapter(adapter);
    }

    private void binhLuan(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        TextView tvComment = dialog.findViewById(R.id.tvComment);
        tvComment.setText(soluong+" bình luận");

        rcvBinhLuan = dialog.findViewById(R.id.rcvBinhLuan);

        ImageView imgAvatar = dialog.findViewById(R.id.imgAvatar);
        Glide.with(ChiTietTruyen.this)
                .load(avatar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgAvatar);

        EditText edtComment = dialog.findViewById(R.id.edtComment);

        dialog.findViewById(R.id.btnUpComment).setOnClickListener(view1 ->{

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = sdf.format(new Date());


            BinhLuan binhLuan = new BinhLuan();
            binhLuan.setId_truyen(truyenTranhId);
            binhLuan.setId_nguoidung(userId);
            binhLuan.setAvatar(avatar);
            binhLuan.setName(fullname);
            binhLuan.setContent(edtComment.getText().toString());
            binhLuan.setDate(currentDate.toString());

            Call<BinhLuan> call = serviceBinhLuan.postData(binhLuan);
            call.enqueue(new Callback<BinhLuan>() {
                @Override
                public void onResponse(Call<BinhLuan> call, Response<BinhLuan> response) {
                    Toast.makeText(ChiTietTruyen.this, "Đã thêm bình luận", Toast.LENGTH_SHORT).show();
                    edtComment.setText("");
                    edtComment.clearFocus();
                    getList();
                }

                @Override
                public void onFailure(Call<BinhLuan> call, Throwable t) {
                    Log.e("RetrofitError", "Error: " + t.getMessage());
                }
            });
        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(view ->{
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void processToken() {

        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String token = preferences.getString("token", null);

        try {
            DecodedJWT jwt = JWT.decode(token);

            userId = jwt.getClaim("userId").asString();
            fullname = jwt.getClaim("fullname").asString();
            avatar = jwt.getClaim("avatar").asString();

            Log.d("MainActivity", "UserId: " + userId);
            Log.d("MainActivity", "Fullname: " + fullname);
            Log.d("MainActivity", "Avatar: " + avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}