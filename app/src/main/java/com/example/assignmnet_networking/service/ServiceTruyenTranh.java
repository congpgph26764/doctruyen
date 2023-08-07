package com.example.assignmnet_networking.service;

import com.example.assignmnet_networking.model.TruyenTranh;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceTruyenTranh {

    @GET("truyentranh")
    Call<List<TruyenTranh>> getAll();
    @GET("truyentranh/{id}")
    Call<TruyenTranh> findById(@Path("id") String id);
    @POST("truyentranh/add")
    Call<TruyenTranh> postData(@Body TruyenTranh data);
    @PUT("truyentranh/edit/{id}")
    Call<TruyenTranh> editById(@Path("id") String id, @Body TruyenTranh data);
    @DELETE("truyentranh/{id}")
    Call<TruyenTranh> deleteById(@Path("id") String id);

}
