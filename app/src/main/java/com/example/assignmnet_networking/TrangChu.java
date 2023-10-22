package com.example.assignmnet_networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignmnet_networking.adapter.TruyenTranhAdapter;
import com.example.assignmnet_networking.api.API;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.service.ServiceTruyenTranh;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangChu extends AppCompatActivity implements TruyenTranhAdapter.Callback {

    private RecyclerView rcvTruyen;
    private List<TruyenTranh> truyenTranhList;
    private TruyenTranhAdapter adapter;
    private EditText edtSearch;
    private ImageView imgAvatar;
    private Retrofit retrofit;
    private ServiceTruyenTranh serviceTruyenTranh;
    private static final String URL = API.URL;

    private String userId = "";
    private String fullname = "";
    private String avatar = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        imgAvatar = findViewById(R.id.imgAvatar);

        processToken();

        Glide.with(TrangChu.this)
                .load(avatar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgAvatar);

        ScrollView scrollView = findViewById(R.id.scroll_view);
        TextView textView = findViewById(R.id.tvTitle);
        TextView tv_toolbar = findViewById(R.id.tvToolbar);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY > 50) {
                    tv_toolbar.setText(textView.getText());
                    textView.setVisibility(View.GONE);
                } else {
                    tv_toolbar.setText("");
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        rcvTruyen = findViewById(R.id.rcvTruyen);
        edtSearch = findViewById(R.id.edtSearch);

        getList();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        findViewById(R.id.btnThongtin).setOnClickListener(v -> {
            startActivity(new Intent(this, Thongtin.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
    }

    private void getList() {

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceTruyenTranh = retrofit.create(ServiceTruyenTranh.class);

        Call<List<TruyenTranh>> call = serviceTruyenTranh.getAll();
        call.enqueue(new Callback<List<TruyenTranh>>() {

            @Override
            public void onResponse(Call<List<TruyenTranh>> call, Response<List<TruyenTranh>> response) {
                getData(response.body());
            }

            @Override
            public void onFailure(Call<List<TruyenTranh>> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
                Toast.makeText(TrangChu.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<TruyenTranh> list) {
        adapter = new TruyenTranhAdapter(list, this, this);
        adapter.reverseList();
        truyenTranhList = new ArrayList<>(list);
        rcvTruyen.setAdapter(adapter);
    }

    private void filter(String text) {
        ArrayList<TruyenTranh> filteredList = new ArrayList<>();
        for (TruyenTranh truyenTranh : truyenTranhList) {
            if (truyenTranh.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(truyenTranh);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void chitiet(TruyenTranh truyenTranh) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);

        Intent intent = new Intent(this, ChiTietTruyen.class);

        Bundle bundle = new Bundle();
        bundle.putString("truyenTranh", truyenTranh.get_id());
        intent.putExtras(bundle);

        startActivity(intent, options.toBundle());

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