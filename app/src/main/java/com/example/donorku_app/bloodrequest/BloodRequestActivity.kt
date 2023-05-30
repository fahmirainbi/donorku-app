package com.example.donorku_app.bloodrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityBloodRequestBinding

class BloodRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBloodRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}