package com.example.donorku_app.api

import android.app.Activity
import com.example.donorku_app.api.model.*
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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
    ): Call<JsonElement>

    // User
    @FormUrlEncoded
    @PUT("users/{id}")
    fun updateUser(
        @Header("Authorization") token: String?,
        @Path("id") id: Int?,
        @Field("nomor_identitas") nomor_identitas: String?,
        @Field("name") name: String?,
        @Field("jenis_kelamin") jenis_kelamin: String?,
        @Field("tempat_lahir") tempat_lahir: String?,
        @Field("tanggal_lahir") tanggal_lahir: String?,
        @Field("no_telp") no_telp: String?,
        @Field("alamat") alamat: String?,
        @Field("rt") rt: String?,
        @Field("rw") rw: String?,
        @Field("kode_pos") kode_pos: String?,
        @Field("kelurahan") kelurahan: String?,
        @Field("kecamatan") kecamatan: String?,
        @Field("kabupatenkota") kabupatenkota: String?,
        @Field("provinsi") provinsi: String?,
        @Field("pekerjaan") pekerjaan: String?,
        @Field("alamat_kantor") alamat_kantor: String?,
        @Field("no_telp_kantor") no_telp_kantor: String?,
        @Field("golongan_darah") golongan_darah: String?,
    ): Call<JsonElement>


    //Kegiatan Donor Darah
    @GET("jadwal-kegiatan-donor")
    fun jadwalGet(
        @Header("Authorization") token:String?,
    ): Call<JsonElement>

    //Stock Kantung Darah
    @GET("stok-darah")
    fun getStok( @Header("Authorization") token:String?,
    ): Call<JsonElement>

    //Info Darah Darurat
//Permintaan Darah
    @POST("permintaan-darah")
    fun donorRequest(@Body bloodRequestPost: BloodRequestPost, @Header("Authorization") token: String
    ): Call<BloodRequestPost>

    //Pengajuan Kegiatan Donor

    @POST("pengajuan-kegiatan-donor")
    fun activityRequest(
        @Body activityRequest: ActivityRequest, @Header("Authorization") token:String
    ): Call<ActivityRequest>
}