package com.example.donorku_app.donoremergency

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityDonorEmergencyBinding
import com.example.donorku_app.donoractivity.DetailDonorActivity
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonorEmergencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorEmergencyBinding

    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorEmergencyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(this,listData)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user    = sharedPreferences.getString("user", null)
        token   = sharedPreferences.getString("token", null)

        if(token != null){
            getData()
        }

        binding.recyclerViewInfoDarahDarurat.layoutManager = layoutManager
        binding.recyclerViewInfoDarahDarurat.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.infoGet(
            "Bearer " + token
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if(json?.has("success") == true && json?.has("data") == true){
                    val data = json?.get("data")?.asJsonArray
                    data?.forEach { d->
                        listData.add(d.asJsonObject)
                    }

                    println(data.toString())

                    adapter.notifyDataSetChanged()
                    binding.recyclerViewInfoDarahDarurat.visibility = View.VISIBLE
                    binding.progressBarInfoDarahDarurat.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@DonorEmergencyActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}

class RecyclerViewAdapter(private val context: Context?, val itemList: List<JsonObject>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info_darah_darurat, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(R.drawable.ic_emergency)
        holder.judulView.setText(item.get("organisasi")?.asString)
        holder.tanggalView.setText(item.get("jadwal_mulai_donor")?.asString!!.subSequence(0, 10).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(0, 10))
        holder.deskripsiView.setText(item.get("deskripsi_acara")?.asString)

        holder.btn.setOnClickListener {
            showDetail(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageInfoDarah)
        val judulView: TextView = itemView.findViewById(R.id.judulinfo)
        val tanggalView: TextView = itemView.findViewById(R.id.tanggalItemInfo)
        val deskripsiView: TextView = itemView.findViewById(R.id.alamatInfo)
        val btn : Button = itemView.findViewById(R.id.btn_bantu)
    }

    private fun showDetail(item: JsonObject) {
        val judul = item.get("organisasi")?.asString
        val tanggal = item.get("jadwal_mulai_donor")?.asString!!.subSequence(0, 10).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(0, 10)
        val deskripsi = item.get("deskripsi_acara")?.asString
        val idKegiatan = item.get("id").asInt


        val intent = Intent(context, DetailDonorEmergencyActivity::class.java)
        intent.putExtra("judul", judul)
        intent.putExtra("tanggal", tanggal)
        intent.putExtra("deskripsi", deskripsi)
        intent.putExtra("id",idKegiatan)
        context?.startActivity(intent)
    }
}