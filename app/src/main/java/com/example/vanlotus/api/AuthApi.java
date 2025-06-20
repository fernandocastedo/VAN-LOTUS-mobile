package com.example.vanlotus.api;

import com.example.vanlotus.model.LoginRequest;
import com.example.vanlotus.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest request);
} 