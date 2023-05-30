package com.example.donorku_app.donoractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityDonorBinding

class DonorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}