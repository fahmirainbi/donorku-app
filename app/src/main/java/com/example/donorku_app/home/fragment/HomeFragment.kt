package com.example.donorku_app.home.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.DonorRequestActivity
import com.example.donorku_app.bloodrequest.BloodRequestActivity
import com.example.donorku_app.coupondonorku.CouponDonorkuActivity
import com.example.donorku_app.databinding.FragmentHomeBinding
import com.example.donorku_app.donoractivity.DonorActivity
import com.example.donorku_app.donoremergency.DonorEmergencyActivity
import com.example.donorku_app.home.imageslider.ImageData
import com.example.donorku_app.home.imageslider.ImageSliderAdapter
import com.example.donorku_app.notification.NotificationActivity
import com.example.donorku_app.stockdonor.StockBloodActivity
import com.google.gson.JsonParser

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageData>()

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private var user: String? = null
    private var token: String? = null

    private var userNameTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user    = sharedPreferences.getString("user", null)
        token   = sharedPreferences.getString("token", null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var index = 0
            override fun run() {
                if (index == list.size)
                    index = 0
                Log.e("Runnable", "$index")
                binding.viewPager2.setCurrentItem(index)
                index++
                handler.postDelayed(this, 4000)
            }
        }

//        Image Slider
        list.add(
            ImageData(
                R.drawable.image_slider1
            )
        )

        list.add(
            ImageData(
                R.drawable.image_slider2
            )
        )

        list.add(
            ImageData(
                R.drawable.image_slider3
            )
        )

        adapter = ImageSliderAdapter(list)
        binding.viewPager2.adapter = adapter



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding button
        binding.btnBlood.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,BloodRequestActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnBloodStock.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,StockBloodActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnCoupon.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,CouponDonorkuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()

            }
        }

        binding.btnInfo.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorEmergencyActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnDonorActivity.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnReqActivity.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorRequestActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.icNotification.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,NotificationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        userNameTextView = view?.findViewById(R.id.tv_name)
        if(user != null){
            val jsonObject = JsonParser.parseString(user).asJsonObject
            userNameTextView?.setText(jsonObject.get("name").asString)
        }


    }

//  Image slider
    override fun onStart() {
        super.onStart()
        handler.post(runnable)

    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }



}