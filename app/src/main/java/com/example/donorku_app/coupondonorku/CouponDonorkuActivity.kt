package com.example.donorku_app.coupondonorku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityCouponDonorkuBinding

class CouponDonorkuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCouponDonorkuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponDonorkuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}