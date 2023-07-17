package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class addPostData(
    @SerializedName("admiko_jadwal_kegiatan_donor_id")
    val admikoJadwalKegiatanDonorId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nomor_antrian")
    val nomorAntrian: Int,
    @SerializedName("questioner_pendonor")
    val questionerPendonor: String,
    @SerializedName("status_donor")
    val statusDonor: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: Int
)