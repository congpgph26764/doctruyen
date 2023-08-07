package com.example.assignmnet_networking.service;

import com.example.assignmnet_networking.model.Image;
import com.example.assignmnet_networking.model.TruyenTranh;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceImage {

    @GET("image")
    Call<List<Image>> getAll();
}
