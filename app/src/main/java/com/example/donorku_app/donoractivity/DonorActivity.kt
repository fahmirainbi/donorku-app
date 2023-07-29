package com.example.donorku_app.donoractivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.DonorRequestActivity
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityDonorBinding
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DonorActivity: AppCompatActivity(){
    private lateinit var binding: ActivityDonorBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            val intent =Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).reverseLayout = true
        (layoutManager as LinearLayoutManager).stackFromEnd = true
        adapter = RecyclerViewAdapter(this, listData)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user    = sharedPreferences.getString("user", null)
        token   = sharedPreferences.getString("token", null)

        if(token != null){
            getData()
        }

        binding.recyclerViewKegiatanDonor.layoutManager = layoutManager
        binding.recyclerViewKegiatanDonor.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.jadwalGet(
            "Bearer " + token
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if(json?.has("success") == true && json?.has("data") == true){
                    val data = json?.get("data")?.asJsonArray
                    data?.forEach { d->
                        listData.add(d.asJsonObject)
                    }

                    adapter.notifyDataSetChanged()
                    binding.recyclerViewKegiatanDonor.visibility = View.VISIBLE
                    binding.progressBarKegiatanDonor.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@DonorActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}

class RecyclerViewAdapter(private val context: Context?, val itemList: List<JsonObject>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kegiatan_donor, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(R.drawable.ic_activity)
        holder.judulView.setText(item.get("organisasi")?.asString)
        holder.tanggalView.setText(item.get("jadwal_mulai_donor")?.asString!!.subSequence(0, 10).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(0, 10))
        holder.deskripsiView.setText(item.get("deskripsi_acara")?.asString)
        holder.waktuView.setText(item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16))

        holder.itemView.setOnClickListener {
            showDetail(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageItemKegiatanDonor)
        val judulView: TextView = itemView.findViewById(R.id.judulItemKegiatanDonor)
        val tanggalView: TextView = itemView.findViewById(R.id.tanggalItemKegiatanDonor)
        val deskripsiView: TextView = itemView.findViewById(R.id.deskripsiItemKegiatanDonor)
        val waktuView: TextView = itemView.findViewById(R.id.waktuItemKegiatanDonor)
    }

    private fun showDetail(item: JsonObject) {
        val judul = item.get("organisasi")?.asString
        val tanggal = item.get("jadwal_mulai_donor")?.asString!!.subSequence(0, 10).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(0, 10)
        val deskripsi = item.get("deskripsi_acara")?.asString
        val waktu = item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16)
        val idKegiatan = item.get("id").asInt


        val intent = Intent(context, DetailDonorActivity::class.java)
        intent.putExtra("judul", judul)
        intent.putExtra("tanggal", tanggal)
        intent.putExtra("deskripsi", deskripsi)
        intent.putExtra("waktu", waktu)
        intent.putExtra("id",idKegiatan)
        context?.startActivity(intent)
    }


}


