package com.example.donorku_app.api.model

data class ApiResponse(
    val success: Boolean,
    val data: Data,
    val message: String
)

data class Data(
    val id: Int,
    val organisasi: String,
    val deskripsi_acara: String,
    val jadwal_mulai_donor: String,
    val jadwal_selesai_donor: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val data_pendonors: List<Pendonor>,
    val jadwal_kegiatan_donor_data_pendonors: List<Pendonor>
)

data class Pendonor(
    val id: Int,
    val user: Int,
    val nomor_antrian: String,
    val status_donor: String,
    val questioner_pendonor: String,
    val admiko_jadwal_kegiatan_donor_id: Int,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?
)
