package com.example.assignmnet_networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmnet_networking.adapter.ImageAdapter;
import com.example.assignmnet_networking.adapter.TruyenTranhAdapter;
import com.example.assignmnet_networking.api.API;
import com.example.assignmnet_networking.model.Image;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.service.ServiceImage;
import com.example.assignmnet_networking.service.ServiceTruyenTranh;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DocTruyen extends AppCompatActivity {
    private RecyclerView rcvImage;
    private TextView tvToolbar;
    private List<Image> list;
    private ImageAdapter adapter;

    private Retrofit retrofit ;
    private ServiceImage serviceImage;
    private static final String URL = API.URL;

    private String truyenTranhId = "";
    private String truyenTranhName = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rcvImage = findViewById(R.id.rcvImage);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                truyenTranhId = bundle.getString("truyenTranh");
                truyenTranhName = bundle.getString("truyenTranhName");
            }
        }

       tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText(truyenTranhName);

        getList();

    }

    private void getList(){

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceImage = retrofit.create(ServiceImage.class);

        Call<List<Image>> call = serviceImage.getAll();
        call.enqueue(new Callback<List<Image>>() {

            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                getData(response.body());
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
                Toast.makeText(DocTruyen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<Image> list ){
        List<Image> list1 = new ArrayList<>();
        for (Image image: list) {
            if (image.getId_truyen().equals(truyenTranhId)) {
                list1.add(image);
            }
        }
        adapter = new ImageAdapter(list1, this);
        adapter.notifyDataSetChanged();
        rcvImage.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("truyenTranh", truyenTranhId);
            setResult(RESULT_OK, resultIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}