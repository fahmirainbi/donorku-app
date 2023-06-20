package com.example.donorku_app.api

import com.example.donorku_app.signup.RegisterApi
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
//    Register
    @FormUrlEncoded
    @POST("register")
    fun registerPost(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") pasword_confirmation: String,
        @Field("no_telp") no_telp: String
    ): Call<ArrayList<RegisterApi>>
}