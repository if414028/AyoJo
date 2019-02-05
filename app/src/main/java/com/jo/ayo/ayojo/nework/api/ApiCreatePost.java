package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.create.PostDataResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiCreatePost {

    @FormUrlEncoded
    @POST("api/otherReports/")
    Call<PostDataResult> createPost(
            @Header("authorization") String token,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("images") String imageUrl
    );
}
