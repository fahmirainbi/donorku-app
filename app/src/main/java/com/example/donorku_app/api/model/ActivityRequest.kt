package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class ActivityRequest(
    @SerializedName("nama")
    val nama: String,
    @SerializedName("nama_organisasi")
    val namaOrganisasi: String,
    @SerializedName("jadwal_kegiatan")
    val jadwalKegiatan: String,
    @SerializedName("no_telp")
    val noTelp: String,
    @SerializedName("catatan")
    val catatan: String,

)