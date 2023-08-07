package com.example.assignmnet_networking.service;

import com.example.assignmnet_networking.model.BinhLuan;
import com.example.assignmnet_networking.model.Image;
import com.example.assignmnet_networking.model.NguoiDung;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceBinhLuan {

    @GET("binhluan")
    Call<List<BinhLuan>> getAll();
    @POST("binhluan/add")
    Call<BinhLuan> postData(@Body BinhLuan data);
}
