package com.example.donorku_app.api

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

    //    userGet
    @GET("users/{id}")
    fun getPoin(
        @Header("Authorization") token: String?,
        @Path("id") id: Int?
    ): Call<JsonElement>

    //  userPut
    @FormUrlEncoded
    @PUT("users/{id}")
    fun minPoin(
        @Header("Authorization") token: String?,
        @Path("id") id: Int?,
        @Field("poin_donor") poin_donor: Int?
    ): Call<JsonElement>

    //Kegiatan Donor Darah
    @GET("jadwal-kegiatan-donor")
    fun jadwalGet(
        @Header("Authorization") token: String?
    ): Call<JsonElement>

//    History

    @GET("jadwal-kegiatan-donor")
    fun historyGet(
        @Header("Authorization") token: String?,
    ): Call<JsonElement>

    @GET("jadwal-kegiatan-donor/{id}")
    fun getStatusDonor(
        @Header("Authorization") token: String?,
        @Path("id") id: Int?,
        @Query("user") users: Int?
    ): Call<ApiResponse>

    //Kegiatan Donor Darah
    @GET("info-darah-darurat")
    fun infoGet(
        @Header("Authorization") token: String?
    ): Call<JsonElement>


    //Stock Kantung Darah
    @GET("stok-darah")
    fun getStok(
        @Header("Authorization") token: String?
    ): Call<JsonElement>

    //Info Darah Darurat


    //Permintaan Darah
    @POST("permintaan-darah")
    fun donorRequest(
        @Body bloodRequestPost: BloodRequestPost, @Header("Authorization") token: String
    ): Call<BloodRequestPost>

    //    notifikasi update poin
    @POST("notifikasi")
    fun notification(
        @Body postNotifikasiUpdatePoin: PostNotifikasiUpdatePoin,
        @Header("Authorization") token: String
    ): Call<PostNotifikasiUpdatePoin>

    //Pengajuan Kegiatan Donor

    @POST("pengajuan-kegiatan-donor")
    fun activityRequest(
        @Body activityRequest: ActivityRequest, @Header("Authorization") token: String?
    ): Call<ActivityRequest>

    //Daftar Kegiatan Donor
    @POST("jadwal-kegiatan-donor/{id}/add-pendonor")
    fun daftarPost(
        @Body registrationPost: RegistrationPost,
        @Header("Authorization") token: String?,
        @Path("id") id: Int?
    ): Call<RegistrationPost>

    //    daftar info darah darurat
    @POST("info-darah-darurat/{id}/add-pendonor")
    fun infoDarahPost(
        @Body registrationPost: RegistrationPost,
        @Header("Authorization") token: String?,
        @Path("id") id: Int?
    ): Call<RegistrationPost>

    //notifikasi
    @GET("notifikasi")
    fun getnotifikasi(@Header("Authorization") token: String?): Call<JsonElement>

    //faq
    @GET("faq")
    fun getFaq(@Header("Authorization") token: String?): Call<JsonElement>

    //    barang
    @GET("barang")
    fun getBarang(@Header("Authorization") token: String?): Call<JsonElement>

    //    transaksi
    @Headers("Content-Type: application/json")
    @POST("transaksi")
    fun postKuppon(
        @Body changePointPost: ChangePointPost, @Header("Authorization") token: String
    ): Call<ChangePointPost>

    @GET("transaksi")
    fun getDataCoupon(
        @Header("Authorization") token: String?,
        @Query("user") users: Int?
    ): Call<JsonElement>


}