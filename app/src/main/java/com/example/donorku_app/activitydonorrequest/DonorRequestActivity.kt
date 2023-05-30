package com.example.donorku_app.activitydonorrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityDonorRequestBinding

class DonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}