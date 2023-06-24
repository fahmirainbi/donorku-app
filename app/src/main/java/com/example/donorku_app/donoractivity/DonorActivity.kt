package com.example.donorku_app.donoractivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ResponseModel
import com.example.donorku_app.databinding.ActivityDonorBinding
import com.example.donorku_app.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        val donorrv = findViewById<RecyclerView>(R.id.rv_donor)




        binding.ivBack.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}