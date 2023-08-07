package com.example.assignmnet_networking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.assignmnet_networking.model.NguoiDung;
import com.example.assignmnet_networking.model.TruyenTranh;
import com.example.assignmnet_networking.service.ServiceNguoiDung;
import com.example.assignmnet_networking.service.ServiceTruyenTranh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangKy extends AppCompatActivity {

    private List<NguoiDung> list;
    private Retrofit retrofit ;
    private ServiceNguoiDung serviceNguoiDung;
    public static final String URL = "http://10.24.31.43:3000/api/";

    String img = "";
    Uri imageUri;

    private ProgressDialog progressDialog;
    private ImageView imgAvatar;
    private TextInputLayout textinputLayoutUsername;
    private TextInputEditText textinputEdittextUsername;
    private TextInputLayout textinputLayoutPassword;
    private TextInputEditText textinputEdittextPassword;
    private TextInputLayout textinputLayoutFullname;
    private TextInputEditText textinputEdittextFullname;
    private TextInputLayout textinputLayoutEmail;
    private TextInputEditText textinputEdittextEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải lên...");

        textinputLayoutUsername = (TextInputLayout) findViewById(R.id.textinput_layout_username);
        textinputEdittextUsername = (TextInputEditText) findViewById(R.id.textinput_edittext_username);
        textinputLayoutPassword = (TextInputLayout) findViewById(R.id.textinput_layout_password);
        textinputEdittextPassword = (TextInputEditText) findViewById(R.id.textinput_edittext_password);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        textinputLayoutFullname = (TextInputLayout) findViewById(R.id.textinput_layout_fullname);
        textinputEdittextFullname = (TextInputEditText) findViewById(R.id.textinput_edittext_fullname);
        textinputLayoutEmail = (TextInputLayout) findViewById(R.id.textinput_layout_email);
        textinputEdittextEmail = (TextInputEditText) findViewById(R.id.textinput_edittext_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getList();

        findViewById(R.id.btnRegister).setOnClickListener(v ->{
            kiemtra();
        });

        findViewById(R.id.tvLogin).setOnClickListener(v ->{
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);

            startActivity(new Intent(this, DangNhap.class),
                    options.toBundle());
        });

        findViewById(R.id.cvImage).setOnClickListener(v ->{
            openGallery();
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

            Glide.with(DangKy.this)
                    .load(imageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgAvatar);

        }
    }

    private void getList(){

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceNguoiDung = retrofit.create(ServiceNguoiDung.class);

        Call<List<NguoiDung>> call = serviceNguoiDung.getAll();
        call.enqueue(new Callback<List<NguoiDung>>() {

            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                list = response.body();
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
            }
        });
    }

    private void kiemtra(){

        if (textinputEdittextUsername.getText().toString().isEmpty()) {
            textinputLayoutUsername.setError("Không để trống");
            textinputLayoutUsername.requestFocus();
            return;
        } else {
            textinputLayoutUsername.setError("");
        }

        if (textinputEdittextPassword.getText().toString().isEmpty()) {
            textinputLayoutPassword.setError("Không để trống");
            textinputLayoutPassword.requestFocus();
            return;
        } else {
            textinputLayoutPassword.setError("");
        }

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
            if (nguoiDung.getUsername().equals(textinputEdittextUsername.getText().toString())) {
                textinputLayoutUsername.setError("Tên đăng nhập đã tồn tại");
                textinputLayoutUsername.requestFocus();
                return;
            }
        }

        dangKy();
    }

    private void dangKy(){

        progressDialog.show();

        String imageName = "image_" + System.currentTimeMillis() + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageURL = uri.toString();
                                        Log.d("ImageURL", imageURL);

                                        NguoiDung nguoiDung = new NguoiDung();
                                        nguoiDung.setUsername(textinputEdittextUsername.getText().toString());
                                        nguoiDung.setPassword(textinputEdittextPassword.getText().toString());
                                        nguoiDung.setAvatar(imageURL);
                                        nguoiDung.setFullname(textinputEdittextFullname.getText().toString());
                                        nguoiDung.setEmail(textinputEdittextEmail.getText().toString());

                                        retrofit = new Retrofit.Builder().baseUrl(URL)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        serviceNguoiDung = retrofit.create(ServiceNguoiDung.class);

                                        Call<NguoiDung> call = serviceNguoiDung.postData(nguoiDung);
                                        call.enqueue(new Callback<NguoiDung>() {
                                            @Override
                                            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                                                if (response.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(DangKy.this, DangNhap.class));
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<NguoiDung> call, Throwable t) {
                                                Log.e("RetrofitError", "Error: " + t.getMessage());
                                            }
                                        });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.e("ImageURL", "Failed to get download URL.");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Upload", "Upload failed: " + e.getMessage());
                    }
                });

    }
}