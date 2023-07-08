package com.example.donorku_app.stockdonor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityStockBloodBinding
import com.example.donorku_app.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    fun getdatadonor(responModel: StockBlood){


        ApiConfig.instance.getStok().enqueue(object : Callback<ArrayList<ResponseModel>>{
            override fun onResponse(
                call: Call<ArrayList<ResponseModel>>,
                response: Response<ArrayList<ResponseModel>>
            ) {
                var darahA = findViewById<TextView>(R.id.tvA)
                var darahB = findViewById<TextView>(R.id.tvB)
                var darahAB = findViewById<TextView>(R.id.tvAB)
                var darahO = findViewById<TextView>(R.id.tvO)

                darahA.text = responModel.stok_darah_a.toString()
                darahB.text = responModel.stok_darah_b.toString()
                darahAB.text = responModel.stok_darah_ab.toString()
                darahO.text = responModel.stok_darah_o.toString()
            }

            override fun onFailure(call: Call<ArrayList<ResponseModel>>, t: Throwable) {
            }

        } )


    }
}