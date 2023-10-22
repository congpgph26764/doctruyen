package com.example.assignmnet_networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.assignmnet_networking.model.LoginResponse;
import com.example.assignmnet_networking.model.NguoiDung;
import com.example.assignmnet_networking.service.ServiceNguoiDung;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangNhap extends AppCompatActivity {

    TextInputEditText textinput_edittext_username, textinput_edittext_password;
    TextInputLayout textinput_layout_username, textinput_layout_password;
    private Retrofit retrofit ;
    private ServiceNguoiDung serviceNguoiDung;
    public static final String URL = "http://192.168.148.107:3000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        textinput_edittext_username = findViewById(R.id.textinput_edittext_username);
        textinput_edittext_password = findViewById(R.id.textinput_edittext_password);
        textinput_layout_username = findViewById(R.id.textinput_layout_username);
        textinput_layout_password = findViewById(R.id.textinput_layout_password);

        findViewById(R.id.btnLogin).setOnClickListener(v ->{

            kiemtra();

        });

        findViewById(R.id.tvRegister).setOnClickListener(v ->{
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_left, R.anim.slide_out_right);

            startActivity(new Intent(this, DangKy.class),
                    options.toBundle());
        });
    }

    private void kiemtra(){

        if (textinput_edittext_username.getText().toString().isEmpty()) {
            textinput_layout_username.setError("Không để trống");
            textinput_layout_username.requestFocus();
            return;
        } else {
            textinput_layout_username.setError("");
        }

        if (textinput_edittext_password.getText().toString().isEmpty()) {
            textinput_layout_password.setError("Không để trống");
            textinput_layout_password.requestFocus();
            return;
        } else {
            textinput_layout_password.setError("");
        }

        dangNhap(textinput_edittext_username.getText().toString(), textinput_edittext_password.getText().toString());

    }

    private void dangNhap(String username, String password){

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceNguoiDung = retrofit.create(ServiceNguoiDung.class);

        Call<LoginResponse> call = serviceNguoiDung.loginUser(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        if (loginResponse.getToken() != null) {
                            String token = loginResponse.getToken();
                            String message = loginResponse.getMessage();

                            SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", token);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(DangNhap.this, message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangNhap.this, TrangChu.class));
                        } else {
                            String message = loginResponse.getMessage();
                            if (message.toLowerCase().equals("mật khẩu không đúng")) {
                                textinput_layout_password.setError("Sai mật khẩu");
                                textinput_layout_password.requestFocus();
                            } else {
                                textinput_edittext_username.setText("");
                                textinput_edittext_password.setText("");
                                textinput_layout_username.requestFocus();
                                Toast.makeText(DangNhap.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("LoginActivity", "Lỗi: Response trả về null");
                    }
                } else {

                    Log.e("LoginActivity", "Lỗi trong response");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("RetrofitError", "Error: " + t.getMessage());
            }
        });
    }

}