package com.example.donorku_app.coupondonorku

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
import com.example.donorku_app.databinding.ActivityCouponDonorkuBinding
import com.example.donorku_app.databinding.ActivityDonorBinding
import com.example.donorku_app.donoractivity.RecyclerViewAdapter
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.home.fragment.menu.Transaksi.BottomSheetPoinFragment
import com.example.donorku_app.home.fragment.menu.Transaksi.ChangePointActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponDonorkuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCouponDonorkuBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private lateinit var user: JsonObject
    private var token: String? = null

    companion object {
        val INTENT_PARCELABLE = "OBJECT_INTENT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponDonorkuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewkuponAdapter(supportFragmentManager,this, listData)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user    =  JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token   = sharedPreferences.getString("token", null)

        if(token != null){
            getData()
        }

        binding.recyclerViewkuponDonor.layoutManager = layoutManager
        binding.recyclerViewkuponDonor.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getDataCoupon(
            "Bearer " + token,
            user.get("id")?.asInt
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if(json?.has("success") == true && json?.has("data") == true){
                    val data = json?.get("data")?.asJsonArray
                    data?.forEach { d->
                        listData.add(d.asJsonObject)
                    }

                    adapter.notifyDataSetChanged()
                    binding.recyclerViewkuponDonor.visibility = View.VISIBLE
                    binding.progressBarkuponDonor.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@CouponDonorkuActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}

class RecyclerViewkuponAdapter(private val fragmentManager: FragmentManager, private val context: Context?, val itemList:List<JsonObject>):RecyclerView.Adapter<RecyclerViewkuponAdapter.ViewHolder>(){
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val namaBarang:TextView = itemView.findViewById(R.id.tvNamaBarang)
        val batasPenukaran:TextView = itemView.findViewById(R.id.tvBatasPenukaran)
        val status:TextView = itemView.findViewById(R.id.tvStatus)
        val gambar:ImageView = itemView.findViewById(R.id.imgkupon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kupon_donor, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
    return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = itemList[position]
        val barang = item.get("barang")
        if (!barang.isJsonNull) {
            holder.namaBarang.setText(barang.asString)
            holder.batasPenukaran.setText(item.get("batas_penukaran")?.asString)
            holder.status.setText(item.get("status_penukaran")?.asString)
            holder.gambar.setImageResource(R.drawable.ic_coupon)



            holder.itemView.setOnClickListener {
                val fragment = BottomSheetCouponFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    CouponDonorkuActivity.INTENT_PARCELABLE,ParcelableJsonObjectCoupon(item)
                )
                fragment.arguments = bundle
                fragment.show(fragmentManager, "BottomSheetDialog")
            }
        } else {


        }



    }

}

class ParcelableJsonObjectCoupon(val jsonObject: JsonObject?) : Parcelable {

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

        other as ParcelableJsonObjectCoupon

        if (jsonObject != other.jsonObject) return false

        return true
    }

    override fun hashCode(): Int {
        return jsonObject?.hashCode() ?: 0
    }

    override fun toString(): String {
        return jsonObject?.toString() ?: ""
    }

    companion object CREATOR : Parcelable.Creator<ParcelableJsonObjectCoupon> {
        override fun createFromParcel(parcel: Parcel): ParcelableJsonObjectCoupon {
            return ParcelableJsonObjectCoupon(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableJsonObjectCoupon?> {
            return arrayOfNulls(size)
        }
    }
}
