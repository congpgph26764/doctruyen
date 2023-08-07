package com.example.assignmnet_networking.service;

import com.example.assignmnet_networking.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface ServiceLogin {
    @POST("nguoidung/login")
    Call<LoginResponse> loginUser(@Field("username") String username,
                                  @Field("password") String password);
}
