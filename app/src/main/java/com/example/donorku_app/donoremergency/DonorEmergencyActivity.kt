package com.example.donorku_app.donoremergency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityDonorEmergencyBinding

class DonorEmergencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorEmergencyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorEmergencyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}