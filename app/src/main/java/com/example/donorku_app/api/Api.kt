package com.example.donorku_app.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    //    Register
    @FormUrlEncoded
    @POST("register")
    fun registerPost(
        @Field("name") name: String,
        @Field("no_telp") no_telp: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") pasword_confirmation: String
    ): Call<ResponseBody>

    //    Login
    @FormUrlEncoded
    @POST("login")
    fun loginPost(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<ResponseBody>

//Kegiatan Donor Darah
//Stock Kantung Darah
//Info Darah Darurat
//Permintaan Darah
//Pengajuan Kegiatan Darah
}