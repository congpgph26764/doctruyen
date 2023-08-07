package com.example.assignmnet_networking.service;

import com.example.assignmnet_networking.model.LoginResponse;
import com.example.assignmnet_networking.model.NguoiDung;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceNguoiDung {

    @GET("nguoidung")
    Call<List<NguoiDung>> getAll();
    @FormUrlEncoded
    @POST("nguoidung/login")
    Call<LoginResponse> loginUser(@Field("username") String username,
                              @Field("password") String password);
    @POST("nguoidung/add")
    Call<NguoiDung> postData(@Body NguoiDung data);
    @PUT("nguoidung/edit/{id}")
    Call<NguoiDung> editById(@Path("id") String id, @Body NguoiDung data);
    @DELETE("nguoidung/{id}")
    Call<NguoiDung> deleteById(@Path("id") String id);
}
