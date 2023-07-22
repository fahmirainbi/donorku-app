package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class PostNotifikasiUpdatePoin(

    @SerializedName("jenis_notifikasi")
    val jenisNotifikasi: String,
    @SerializedName("tipe_notifikasi")
    val tipeNotifikasi: String,
    @SerializedName("user")
    val user: Int,
    @SerializedName("judul")
    val judul: String,
    @SerializedName("isi_notifikasi")
val isiNotifikasi: String,
)