package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.user.UserCheckLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiCheckLogin {

    @FormUrlEncoded
    @POST("api/appUsers/cekStatus")
    Call<UserCheckLogin> checkLogin(@Field("oldToken") String token);
}
