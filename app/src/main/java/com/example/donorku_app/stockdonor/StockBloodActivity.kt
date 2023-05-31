package com.example.donorku_app.stockdonor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityStockBloodBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.home.fragment.HomeFragment

class StockBloodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockBloodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStockBloodBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}