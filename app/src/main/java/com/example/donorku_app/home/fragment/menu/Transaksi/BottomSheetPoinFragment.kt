package com.example.donorku_app.home.fragment.menu.Transaksi

import android.app.AlertDialog
import android.content.Context
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
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ChangePointPost
import com.example.donorku_app.api.model.PoinDonorPost
import com.example.donorku_app.coupondonorku.CouponDonorkuActivity
import com.example.donorku_app.databinding.FragmentBottomSheetPoinBinding
import com.example.donorku_app.home.fragment.menu.Transaksi.ChangePointActivity.Companion.INTENT_PARCELABLE
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

class BottomSheetPoinFragment : BottomSheetDialogFragment(), BottomSheetPoinPresenter.Listener {
    private lateinit var binding: FragmentBottomSheetPoinBinding
    private lateinit var bottomSheetPoinPresenter: BottomSheetPoinPresenter

    private var user: String? = null
    private var token: String? = null
    private lateinit var userid: JsonObject

    private var listData: ArrayList<JsonObject> = arrayListOf()

    private var userPointTextView: TextView? = null

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
        binding = FragmentBottomSheetPoinBinding.inflate(inflater, container, false)
        userPointTextView = view?.findViewById(R.id.tvPoinPribadi)
        if (user != null) {
            val jsonObject = JsonParser.parseString(user).asJsonObject
            userPointTextView?.text = jsonObject.get("poin_donor").asString
        }
        getData()
        return binding.root
    }



    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getPoin(
            "Bearer " + token,
            userid.get("id")?.asInt
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val dataObject = responseBody.asJsonObject
                        val poinDonor = dataObject.get("data").asJsonObject?.get("poin_donor")?.asInt
                        userPointTextView?.text = poinDonor.toString()
                        val parcelableData =
                            arguments?.getParcelable<ParcelableJsonObject>(INTENT_PARCELABLE)
                        val item = parcelableData?.jsonObject
                        binding.tvPoinPribadi.text = poinDonor.toString()

                        if (item != null) {
                            val title = item.get("nama_barang").asString
                            val description = item.get("harga_kupon").asString
                            val descriptionku = item.get("harga_kupon").asInt
                            val poinDonor = dataObject.get("data").asJsonObject?.get("poin_donor")?.asInt
                            val sisaPoin = poinDonor?.minus(descriptionku)


                            binding.tvNamaBarangKonfir.text = title
                            binding.tvHargaBarangKonfir.text = description
                            binding.tvPoinVoucher.text = description


                            if (sisaPoin != null) {
                                if (sisaPoin <= 0) {
                                    binding.tvSisaPoin.text = "Poin kurang"
                                } else {
                                    binding.tvSisaPoin.text = sisaPoin.toString()
                                }
                            }

                            fun updatePoinDonor() {
                                ApiConfig.instanceRetrofit.minPoin(
                                    "Bearer " + token,
                                    userid.get("id")?.asInt,
                                    sisaPoin
                                ).enqueue(object : Callback<JsonElement> {
                                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                                        if (response.isSuccessful) {
                                            onAddContentSuccess("Data Sukses Dikirim")
                                        } else {
                                            onAddContentFailure("Gagal mengirim data")
                                        }

                                    }

                                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                                        onAddContentFailure("Data Tidak Sesuai" + t.message)
                                        Log.d("Data", "Data Tidak sesuai" + t.message)
                                    }

                                })
                            }

                            binding.btnTukarkanPoin.setOnClickListener {
                                if (sisaPoin != null) {
                                    if (sisaPoin <= 0) {
                                        allertFailure()
                                    } else {
                                        setupDataComponent()
                                        setupListener()
                                        updatePoinDonor()
                                        allertDialogSucces()

                                    }
                                }
                            }
                        }
                    }
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

    private fun setupDataComponent() {
        bottomSheetPoinPresenter =
            BottomSheetPoinPresenter(requireContext(), this@BottomSheetPoinFragment)
    }

    override fun onAddContentSuccess(sccMessage: String) {
        Toast.makeText(requireContext(), sccMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onAddContentFailure(errMessage: String) {
        Toast.makeText(requireContext(), errMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setupListener() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val parcelableData = arguments?.getParcelable<ParcelableJsonObject>(INTENT_PARCELABLE)
        val item = parcelableData?.jsonObject
        if (token != null && item != null) {

            val jsonObject = JsonParser.parseString(user).asJsonObject
            val user1 = jsonObject.get("id").asInt

            val kode = jsonObject.get("no_telp").asString
            val randomCode = Random.nextInt(10, 100).toString()
            val kodeDonor = "DN-${kode}-${randomCode}"

            val status = "Belum Ditukar"

            val clickedDateTime = Calendar.getInstance().time
            val batasPenukaran = calculateBatasPenukaran(clickedDateTime)

            if (item != null) {
                val title = item.get("id").asInt
                val posContent = ChangePointPost(user1, title, kodeDonor, batasPenukaran, status)
                bottomSheetPoinPresenter.makeContent(posContent)

            }
        }
    }


    fun calculateBatasPenukaran(clickedDateTime: Date): String {
        val isWithinSevenDays = isWithinSevenDays(clickedDateTime)
        val batasPenukaran = if (isWithinSevenDays) {
            val batasPenukaranDateTime = getBatasPenukaranDateTime(clickedDateTime)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            dateFormat.format(batasPenukaranDateTime)
        } else {
            ""
        }

        return batasPenukaran
    }

    fun isWithinSevenDays(clickedDateTime: Date): Boolean {
        val currentTime = Calendar.getInstance().time
        val diffInMillies = currentTime.time - clickedDateTime.time
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies)
        return diffInDays <= 7
    }

    fun getBatasPenukaranDateTime(clickedDateTime: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = clickedDateTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }



    fun allertDialogSucces() {
        val dialogBinding = layoutInflater.inflate(R.layout.alert_dialog_custom_, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding)

        val alertDialog = builder.create()

        val judul = dialogBinding.findViewById<TextView>(R.id.tvHead)
        val gambar = dialogBinding.findViewById<ImageView>(R.id.ivdialog)
        val deskripsi = dialogBinding.findViewById<TextView>(R.id.tvdeskripsi)
        val buton = dialogBinding.findViewById<Button>(R.id.btnAlert)

        judul.setText("Penukaran Poin Berhasil")
        gambar.setImageResource(R.drawable.img_success)
        deskripsi.setText("Penukaran poin anda berhasil silahkan melihat kupon anda, penukaran poin hanya berlaku 7 hari kerja")

        buton.setOnClickListener {

            requireActivity().run {
                val intent = Intent(this, CouponDonorkuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

                startActivity(intent)
            }


            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun allertFailure() {
        val dialogBinding = layoutInflater.inflate(R.layout.alert_dialog_custom_, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding)

        val alertDialog = builder.create()

        val judul = dialogBinding.findViewById<TextView>(R.id.tvHead)
        val gambar = dialogBinding.findViewById<ImageView>(R.id.ivdialog)
        val deskripsi = dialogBinding.findViewById<TextView>(R.id.tvdeskripsi)
        val buton = dialogBinding.findViewById<Button>(R.id.btnAlert)

        judul.setText("Penukaran Poin Gagal")
        gambar.setImageResource(R.drawable.img_failure)
        deskripsi.setText("Penukaran poin anda gagal silahkan cek kembali jumlah poin")

        buton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}