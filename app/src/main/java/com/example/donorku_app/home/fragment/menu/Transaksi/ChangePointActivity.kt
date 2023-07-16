package com.example.donorku_app.home.fragment.menu.Transaksi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityChangePointBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.home.fragment.menu.Transaksi.ChangePointActivity.Companion.INTENT_PARCELABLE
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePointActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePointBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null

    companion object {
        val INTENT_PARCELABLE = "OBJECT_INTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePointBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewPoinDonorAdapter(supportFragmentManager,this, listData)



        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        token = sharedPreferences.getString("token", null)

        if (token != null) {
            getData()
        }

        binding.recyclerViewPoinDonor.layoutManager = layoutManager
        binding.recyclerViewPoinDonor.adapter = adapter

    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getBarang(
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
                    binding.recyclerViewPoinDonor.visibility = View.VISIBLE
                    binding.progressBarPoinDonor.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(
                    this@ChangePointActivity,
                    "Error : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })
    }
}

class RecyclerViewPoinDonorAdapter(private val fragmentManager: FragmentManager, private val context: Context?, val itemList: List<JsonObject>) :
    RecyclerView.Adapter<RecyclerViewPoinDonorAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val hargaBarang: TextView = itemView.findViewById(R.id.tvHargaBarang)
        val statusBarang: TextView = itemView.findViewById(R.id.tvStatus)
        val imagePoin: ImageView = itemView.findViewById(R.id.imgPoin)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_penukaran_poin, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.namaBarang.setText(item.get("nama_barang")?.asString)
        holder.hargaBarang.setText(item.get("harga_kupon").toString())
        holder.statusBarang.setText(item.get("status_barang")?.asString)
        holder.imagePoin.setImageResource(R.drawable.ic_poin)

        holder.itemView.setOnClickListener {
            val fragment = BottomSheetPoinFragment()
            val bundle = Bundle()
            bundle.putParcelable(INTENT_PARCELABLE, ParcelableJsonObject(item))
            fragment.arguments = bundle
            fragment.show(fragmentManager, "BottomSheetDialog")
        }


    }

}

class ParcelableJsonObject(val jsonObject: JsonObject?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()?.let { com.google.gson.JsonParser.parseString(it).asJsonObject })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jsonObject?.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParcelableJsonObject

        if (jsonObject != other.jsonObject) return false

        return true
    }

    override fun hashCode(): Int {
        return jsonObject?.hashCode() ?: 0
    }

    override fun toString(): String {
        return jsonObject?.toString() ?: ""
    }

    companion object CREATOR : Parcelable.Creator<ParcelableJsonObject> {
        override fun createFromParcel(parcel: Parcel): ParcelableJsonObject {
            return ParcelableJsonObject(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableJsonObject?> {
            return arrayOfNulls(size)
        }
    }
}
