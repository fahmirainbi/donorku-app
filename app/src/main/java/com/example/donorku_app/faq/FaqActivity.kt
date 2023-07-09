package com.example.donorku_app.faq

import android.annotation.SuppressLint
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityFaqBinding
import com.example.donorku_app.databinding.ActivityHomeBinding
import com.example.donorku_app.donoractivity.RecyclerViewAdapter
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFaqBinding
    private var listData: ArrayList<JsonObject> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var user: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        layoutManager = LinearLayoutManager(this)
        adapter = RecycleViewFaqAdapter(this,listData)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user    = sharedPreferences.getString("user", null)
        token   = sharedPreferences.getString("token", null)

        if(token != null){
            getData()
        }

        binding.recyclerViewFaq.layoutManager = layoutManager
        binding.recyclerViewFaq.adapter = adapter
    }

    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getFaq(
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
                    binding.recyclerViewFaq.visibility = View.VISIBLE
                    binding.progressBarFaq.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@FaqActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}

class RecycleViewFaqAdapter(private val context: Context, val itemList:List<JsonObject>): RecyclerView.Adapter<RecycleViewFaqAdapter.ViewHolder>() {
   inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       val judulFaq:TextView = itemView.findViewById(R.id.judulFaq)
       val isiFaq:TextView = itemView.findViewById(R.id.deskripsiFaq)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
    return itemList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = itemList[position]
        holder.judulFaq.setText(item.get("judul_faq")?.asString)
        holder.isiFaq.setText(item.get("isi_faq")?.asString)




    }
}