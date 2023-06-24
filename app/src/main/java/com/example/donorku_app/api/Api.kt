package com.example.donorku_app.api

import com.example.donorku_app.api.model.ResponseModel
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
        @Field("no_telp") no_telp: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") pasword_confirmation: String
    ): Call<ResponseModel>

    //    Login
    @FormUrlEncoded
    @POST("login")
    fun loginPost(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<ResponseModel>


    //Kegiatan Donor Darah
    @GET("jadwal-kegiatan-donor")
    fun jadwalGet(): Call<ResponseModel>

//Stock Kantung Darah
//Info Darah Darurat
//Permintaan Darah
//Pengajuan Kegiatan Darah
}