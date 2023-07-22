package com.example.donorku_app.home.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.AddContentPresenter
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.*
import com.example.donorku_app.coupondonorku.CouponDonorkuActivity
import com.example.donorku_app.databinding.FragmentBottomSheetCouponBinding
import com.example.donorku_app.databinding.FragmentBottomSheetHistoryBinding
import com.example.donorku_app.databinding.FragmentBottomSheetPoinBinding
import com.example.donorku_app.home.fragment.menu.Transaksi.ChangePointActivity.Companion.INTENT_PARCELABLE
import com.example.donorku_app.home.fragment.menu.Transaksi.ParcelableJsonObject
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class BottomSheetHistoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetHistoryBinding


    private var user: String? = null
    private var token: String? = null
    private lateinit var userid: JsonObject

    private lateinit var postNotificationPresenter: PostNotificationPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private var isButtonClicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        userid = JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token = sharedPreferences.getString("token", null)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetHistoryBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("ButtonStatus", MODE_PRIVATE)


        getData()
        return binding.root
    }

    //    Melihat status kegiatan donor berdasarkan userId
    fun getData() {
        val idKegiatan = arguments?.getInt("id")
        val user = userid.get("id").asInt
        ApiConfig.instanceRetrofit.getStatusDonor("Bearer " + token, idKegiatan, user)
            .enqueue(object : Callback<ApiResponse> {
                @SuppressLint("ResourceAsColor")
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val data = apiResponse?.data
                        val pendonors = data?.data_pendonors

                        val userStatusDonor = pendonors?.find { it.user == user }?.status_donor

                        if (userStatusDonor != null) {
                            if (userStatusDonor == "lolos") {
                                binding.tvStatus.setTextColor(
                                    ContextCompat.getColorStateList(
                                        requireContext(),
                                        R.color.green
                                    )
                                )
                                isButtonClicked = sharedPreferences.getBoolean("isButtonClicked_$idKegiatan", false)


                                binding.tvStatus.text = "Lolos"
                                binding.tvDeskripsiStatus.text =
                                    "Anda telah mengikuti kegiatan, Anda dapat memasukan poin yang didapatkan"
                                sharedPreferences = requireContext().getSharedPreferences(
                                    "ButtonStatus",
                                    MODE_PRIVATE
                                )

                                if (!isButtonClicked) {
                                    binding.btnUpdatePoin.setOnClickListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Poin telah dimasukan!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.btnUpdatePoin.backgroundTintList =
                                            ContextCompat.getColorStateList(
                                                requireContext(),
                                                R.color.grey
                                            )

                                        isButtonClicked = true
                                        getpoin()
                                        setupDataComponent()
                                        setupListener()

                                        binding.btnUpdatePoin.isEnabled = false

                                        val editor = sharedPreferences.edit()
                                        editor.putBoolean("isButtonClicked_$idKegiatan", true)
                                        editor.apply()
                                    }
                                } else {
                                    disableButton()
                                }
                            } else if (userStatusDonor == "hadir") {
                                binding.tvStatus.setTextColor(
                                    ContextCompat.getColorStateList(
                                        requireContext(),
                                        R.color.green
                                    )
                                )
                                binding.tvStatus.text = "Hadir"
                                binding.tvDeskripsiStatus.text =
                                    "Anda belum dinyatakan Lolos, Anda tidak dapat memasukan poin"

                                binding.btnUpdatePoin.backgroundTintList =
                                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

                            } else if (userStatusDonor == "tidak_lolos") {
                                binding.tvStatus.text = "Tidak Lolos"
                                binding.tvDeskripsiStatus.text =
                                    "Anda belum dinyatakan Lolos, Anda tidak dapat memasukan poin"

                                binding.btnUpdatePoin.backgroundTintList =
                                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

                            } else {

                                binding.tvStatus.text = "Tidak Hadir"
                                binding.tvDeskripsiStatus.text =
                                    "Anda tidak mengikuti kegiatan, Anda tidak dapat memasukan poin"
                                binding.btnUpdatePoin.backgroundTintList =
                                    ContextCompat.getColorStateList(requireContext(), R.color.grey)
                            }

                            println("Status Donor for User $user: $userStatusDonor")
                        } else {

                            println("User with ID $user not found in the pendonors list.")
                        }
                    } else {
                        println("API call failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // Handle network or other errors here
                    println("API call failed: ${t.message}")
                }
            })
    }

    private fun disableButton() {
        // Set the background tint color to gray
        binding.btnUpdatePoin.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.grey)
        Toast.makeText(requireContext(), "Anda sudah memasukan poin", Toast.LENGTH_LONG).show()
        val idKegiatan = arguments?.getInt("id")
        isButtonClicked = sharedPreferences.getBoolean("isButtonClicked_$idKegiatan", false)

        // Disable the button to prevent further clicks
        binding.btnUpdatePoin.isEnabled = false
    }

//    memasukan notifikasi menambah poin

    private fun setupDataComponent() {
        postNotificationPresenter =
            PostNotificationPresenter(requireContext(), this@BottomSheetHistoryFragment)
    }

    fun onAddContentSuccess(sccMessage: String) {
        Toast.makeText(requireContext(), sccMessage, Toast.LENGTH_SHORT).show()

    }

    fun onAddContentFailure(errMessage: String) {
        Toast.makeText(requireContext(), errMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setupListener() {
        val jenis_notifikasi = "single_user"
        val tipe_notifikasi = "pengumuman"
        val user = userid.get("id").asInt
        val judul = "Penambahan Poin"
        val isi = "Selamat anda mendapatkan poin sejumlah 500 poin"


        val posContent =
            PostNotifikasiUpdatePoin(jenis_notifikasi, tipe_notifikasi, user, judul, isi)
        postNotificationPresenter.createContent(posContent)


    }

    //    update Poin ketika status donor dikatakan lolos
    private fun getDataPoinFromListData(listData: List<JsonObject>): Int {
        var totalPoin = 0
        for (data in listData) {
            val poin = data.get("poin_donor")?.asInt
            if (poin != null) {
                totalPoin += poin
            }
        }
        return totalPoin
    }

    private fun getpoin() {
        listData.clear()
        ApiConfig.instanceRetrofit.getPoin(
            "Bearer " + token,
            userid.get("id")?.asInt
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if (json?.has("success") == true && json?.has("data") == true) {
                    val data = json?.get("data")?.asJsonArray
                    data?.forEach { d ->
                        listData.add(d.asJsonObject)
                    }
                    val poinDonor = getDataPoinFromListData(listData)
                    val poinUpdate = poinDonor + 500

                    ApiConfig.instanceRetrofit.minPoin(
                        "Bearer " + token,
                        userid.get("id")?.asInt,
                        poinUpdate
                    ).enqueue(object : Callback<JsonElement> {
                        override fun onResponse(
                            call: Call<JsonElement>,
                            response: Response<JsonElement>
                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Poin Anda Bertambah 500",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        }

                    })

                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error: " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}