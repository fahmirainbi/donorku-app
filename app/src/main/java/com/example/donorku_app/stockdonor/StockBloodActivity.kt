package com.example.donorku_app.stockdonor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityStockBloodBinding

class StockBloodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockBloodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStockBloodBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}