package com.example.assignmnet_networking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignmnet_networking.api.API;
import com.example.assignmnet_networking.model.NguoiDung;
import com.example.assignmnet_networking.service.ServiceNguoiDung;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Thongtin extends AppCompatActivity {

    private String userId ="";
    private String fullname ="";
    private String avatar ="";
    private String email ="";

    private ImageView imgAvatar;
    private TextView tvFullname;
    private TextView tvName;
    private TextView tvEmail;

    private Retrofit retrofit ;
    private ServiceNguoiDung serviceNguoiDung;
    private static final String URL = API.URL;

    private List<NguoiDung> list;

    Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_dung);

        imgAvatar = findViewById(R.id.imgAvatar);
        tvFullname = findViewById(R.id.tvFullname);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        processToken();

        Glide.with(Thongtin.this)
                .load(avatar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgAvatar);
        tvFullname.setText(fullname);
        tvName.setText("Full name: "+fullname);
        tvEmail.setText("Email: "+email);

        findViewById(R.id.imgDangxuat).setOnClickListener(v ->{
            logout();
        });

        findViewById(R.id.btnTrangchu).setOnClickListener(v ->{
            startActivity(new Intent(this, TrangChu.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceNguoiDung = retrofit.create(ServiceNguoiDung.class);

        getList();

        findViewById(R.id.btnChinhsua).setOnClickListener(v ->{
            edit();
        });

        findViewById(R.id.cvImage).setOnClickListener(v ->{
            openGallery();
        });
    }

    private void getList(){

        Call<List<NguoiDung>> call = serviceNguoiDung.getAll();
        call.enqueue(new Callback<List<NguoiDung>>() {

            @Override
            public void onResponse(Call<List<NguoiDung>> call, retrofit2.Response<List<NguoiDung>> response) {
                list = new ArrayList<>(response.body());
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Toast.makeText(Thongtin.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void edit(){
        final Dialog dialog = new Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_edit);

        TextInputLayout textinputLayoutFullname = (TextInputLayout) dialog.findViewById(R.id.textinput_layout_fullname);
        TextInputEditText textinputEdittextFullname = (TextInputEditText) dialog.findViewById(R.id.textinput_edittext_fullname);
        TextInputLayout textinputLayoutEmail = (TextInputLayout) dialog.findViewById(R.id.textinput_layout_email);
        TextInputEditText textinputEdittextEmail = (TextInputEditText) dialog.findViewById(R.id.textinput_edittext_email);

        textinputEdittextEmail.setText(email);
        textinputEdittextFullname.setText(fullname);

        dialog.findViewById(R.id.btnChinhsua).setOnClickListener(v ->{

            if (textinputEdittextFullname.getText().toString().isEmpty()) {
                textinputLayoutFullname.setError("Không để trống");
                textinputLayoutFullname.requestFocus();
                return;
            } else {
                textinputLayoutFullname.setError("");
            }

            if (textinputEdittextEmail.getText().toString().isEmpty()) {
                textinputLayoutEmail.setError("Không để trống");
                textinputLayoutEmail.requestFocus();
                return;
            } else if(!Patterns.EMAIL_ADDRESS.matcher(textinputEdittextEmail.getText().toString()).matches()){
                textinputLayoutEmail.setError("Địa chỉ email không hợp lệ");
                textinputLayoutEmail.requestFocus();
                return;
            } else {
                textinputLayoutEmail.setError("");
            }

            for (NguoiDung nguoiDung: list) {
                if (nguoiDung.get_id().equals(userId)) {

                    NguoiDung nguoiDung1 = new NguoiDung();
                    nguoiDung1.set_id(userId);
                    nguoiDung1.setUsername(nguoiDung.getUsername());
                    nguoiDung1.setPassword(nguoiDung.getPassword());
                    nguoiDung1.setAvatar(nguoiDung.getAvatar());
                    nguoiDung1.setFullname(textinputEdittextFullname.getText().toString());
                    nguoiDung1.setEmail(textinputEdittextEmail.getText().toString());

                    Call<NguoiDung> call = serviceNguoiDung.editById(userId, nguoiDung1);
                    call.enqueue(new Callback<NguoiDung>() {
                        @Override
                        public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {

                            dialog.dismiss();
                            Toast.makeText(Thongtin.this, "Đăng nhập lại để load thông tin", Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("token");
                            editor.putBoolean("isLoggedIn", false);
                            editor.apply();
                            startActivity(new Intent(Thongtin.this, DangNhap.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<NguoiDung> call, Throwable t) {
                            Toast.makeText(Thongtin.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

    private void processToken() {

        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String token = preferences.getString("token", null);

        try {
            DecodedJWT jwt = JWT.decode(token);

            userId = jwt.getClaim("userId").asString();
            fullname = jwt.getClaim("fullname").asString();
            avatar = jwt.getClaim("avatar").asString();
            email = jwt.getClaim("email").asString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Thongtin.this);
        builder.setTitle("Đăng xuất");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("token");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Toast.makeText(Thongtin.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Thongtin.this, DangNhap.class));
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private static final int REQUEST_PICK_IMAGE = 1;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            Glide.with(Thongtin.this)
                    .load(imageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgAvatar);

        }
    }
}