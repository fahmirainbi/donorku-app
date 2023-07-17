package com.example.donorku_app.coupondonorku

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
import com.example.donorku_app.databinding.FragmentBottomSheetCouponBinding
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

class BottomSheetCouponFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentBottomSheetCouponBinding


    private var user: String? = null
    private var token: String? = null
    private lateinit var userid: JsonObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        userid    =  JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token = sharedPreferences.getString("token", null)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetCouponBinding.inflate(inflater, container, false)

        val parcelableData = arguments?.getParcelable<ParcelableJsonObjectCoupon>(INTENT_PARCELABLE)
        val item = parcelableData?.jsonObject


        if (item != null) {
            val title = item.get("kode_kupon").asString


            binding.tvKupon.text = title


        }
        return binding.root
    }



}