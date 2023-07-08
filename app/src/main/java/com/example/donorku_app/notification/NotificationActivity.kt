package com.example.donorku_app.notification

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
import com.example.donorku_app.databinding.ActivityNotificationBinding
import com.example.donorku_app.donoractivity.RecyclerViewAdapter
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewNotifikasiAdapter(this,listData)

        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        token = sharedPreferences.getString("token", null)

        if (token != null) {
            getData()
        }

        binding.recyclerViewNotifikasi.layoutManager = layoutManager
        binding.recyclerViewNotifikasi.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getnotifikasi(
            "Bearer " + token
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if (json?.has("success") == true && json?.has("data") == true) {
                    val data = json?.get("data")?.asJsonArray
                    data?.forEach { d ->
                        listData.add(d.asJsonObject)
                    }

                    adapter.notifyDataSetChanged()
                    binding.recyclerViewNotifikasi.visibility = View.VISIBLE
                    binding.progressBarNotifikasi.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(
                    this@NotificationActivity,
                    "Error : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })
    }
}


class RecyclerViewNotifikasiAdapter(private val context: Context, val itemList: List<JsonObject>) :
    RecyclerView.Adapter<RecyclerViewNotifikasiAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gambarNotifikasi:ImageView = itemView.findViewById(R.id.imageNotifikasi)
        val jenisNotifikasi: TextView = itemView.findViewById(R.id.jenisNotifikasi)
        val judulNotifikasi: TextView = itemView.findViewById(R.id.judulNotifikasi)
        val deskripsiNotifikasi: TextView = itemView.findViewById(R.id.deskripsiNotifikasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        var jenisNotifikasi = item.get("tipe_notifikasi")?.asString
        if (jenisNotifikasi == "penting"){
            holder.gambarNotifikasi.setImageResource(R.drawable.ic_exclamation)
        }else{
            holder.gambarNotifikasi.setImageResource(R.drawable.ic_notification2)
        }
        holder.jenisNotifikasi.setText(item.get("tipe_notifikasi")?.asString)
        holder.judulNotifikasi.setText(item.get("judul")?.asString)
        holder.deskripsiNotifikasi.setText(item.get("isi_notifikasi")?.asString)

    }

}