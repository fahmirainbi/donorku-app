package com.example.donorku_app.stockdonor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityStockBloodBinding
import com.example.donorku_app.donoractivity.RecyclerViewAdapter
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockBloodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockBloodBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStockBloodBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapterStok(this, listData)

        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        token = sharedPreferences.getString("token", null)

        if (token != null) {
            getData()
        }

        binding.recyclerViewStokDonor.layoutManager = layoutManager
        binding.recyclerViewStokDonor.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getStok("Bearer " + token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val json = response.body()?.asJsonObject
                    if (json?.has("success") == true && json?.has("data") == true) {
                        val data = json?.get("data")?.asJsonArray
                        data?.forEach { d ->
                            listData.add(d.asJsonObject)
                        }

                        adapter.notifyDataSetChanged()
                        binding.recyclerViewStokDonor.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(
                        this@StockBloodActivity,
                        "Error : " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
    }

}

class RecyclerViewAdapterStok(private val context: Context?, val itemList: List<JsonObject>) : RecyclerView.Adapter<RecyclerViewAdapterStok.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val darahA: TextView = itemView.findViewById(R.id.tvA)
        val darahB: TextView = itemView.findViewById(R.id.tvB)
        val darahAB: TextView = itemView.findViewById(R.id.tvAB)
        val darahO: TextView = itemView.findViewById(R.id.tvO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stok_darah, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
    return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.darahA.setText(item.get("stok_darah_a")?.toString())
        holder.darahB.setText(item.get("stok_darah_b")?.toString())
        holder.darahAB.setText(item.get("stok_darah_ab")?.toString())
        holder.darahO.setText(item.get("stok_darah_o")?.toString())
    }
}