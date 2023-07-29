package com.example.donorku_app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
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

    private val CHANNEL_ID = "my_notification_channel"
    private var isNotificationShown = false

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
        (layoutManager as LinearLayoutManager).reverseLayout = true
        (layoutManager as LinearLayoutManager).stackFromEnd = true
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

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, title: String?, message: String?) {
        createNotificationChannel(context)

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notificationBuilder.build())
        } else {

            Toast.makeText(context, "Izin untuk getar belum diberikan", Toast.LENGTH_SHORT).show()
        }
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

                    if (data != null) {
                        if (data.size() > 0) {
                            val firstItem = data[0].asJsonObject
                            val title = firstItem.get("judul")?.asString
                            val message = firstItem.get("isi_notifikasi")?.asString

                            // Cek apakah notifikasi sudah ditampilkan sebelumnya atau belum
                            if (!isNotificationShown) {
                                showNotification(this@NotificationActivity, title, message)
                                isNotificationShown = true
                            }
                        }
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