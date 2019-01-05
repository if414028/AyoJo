package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.user.UserDataResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiLogin {

    @FormUrlEncoded
    @POST("api/appUsers/login")
    Call<UserDataResult> login(
            @Field("username") String username,
            @Field("password") String password);
}
