package com.example.donorku_app.home.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.coupondonorku.RecyclerViewkuponAdapter
import com.example.donorku_app.databinding.FragmentBottomSheetPoinBinding
import com.example.donorku_app.databinding.FragmentHistoryBinding
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private lateinit var user: JsonObject
    private var token: String? = null
    private var idJadwal: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = RecyclerViewHistory(requireContext(), listData)

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user    =  JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token   = sharedPreferences.getString("token", null)

        binding.recyclerHistory.layoutManager = layoutManager
        binding.recyclerHistory.adapter = adapter

        if(token != null){
            getData(idJadwal)
        }
    }

    private fun getData(idjadwal:Int) {
        listData.clear()
            ApiConfig.instanceRetrofit.jadwalGet(
                "Bearer " + token,
            ).enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val json = response.body()?.asJsonObject
                    if (json?.has("success") == true && json?.has("data") == true) {
                        val data = json?.get("data")?.asJsonArray
                        data?.forEach { d ->
                            listData.add(d.asJsonObject)
                        }

                        adapter.notifyDataSetChanged()
                        binding.recyclerHistory.visibility = View.VISIBLE
                        binding.progressHistory.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error : " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })

    }




}

class RecyclerViewHistory(private val context: Context?, val itemList:List<JsonObject>):RecyclerView.Adapter<RecyclerViewHistory.ViewHolder>(){
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val judulView: TextView = itemView.findViewById(R.id.judulHistory)
        val tanggalView: TextView = itemView.findViewById(R.id.tanggalHistory)
        val deskripsiView: TextView = itemView.findViewById(R.id.deskripsiHistory)
        val waktuView: TextView = itemView.findViewById(R.id.waktuHistory)
        val status: TextView = itemView.findViewById(R.id.statusHistory)
        val gambar: ImageView = itemView.findViewById(R.id.imageHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder

    }

    override fun getItemCount(): Int {
    return itemList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.gambar.setImageResource(R.drawable.ic_activity)
        holder.judulView.setText(item.get("organisasi")?.asString)
        holder.tanggalView.setText(item.get("jadwal_mulai_donor")?.asString!!.subSequence(0, 10).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(0, 10))
        holder.deskripsiView.setText(item.get("deskripsi_acara")?.asString)
        holder.waktuView.setText(item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16).toString() + " - " + item.get("jadwal_selesai_donor")?.asString?.subSequence(11, 16))
        holder.status.setText(item.get("status_donor")?.asString)

    }

}