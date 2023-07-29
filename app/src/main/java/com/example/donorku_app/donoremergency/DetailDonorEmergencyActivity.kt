package com.example.donorku_app.donoremergency

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.RegistrationPost
import com.example.donorku_app.databinding.ActivityDetailDonorBinding
import com.example.donorku_app.databinding.ActivityDetailDonorEmergencyBinding
import com.example.donorku_app.donoractivity.Utils.getSelectedValue
import com.example.donorku_app.home.HomeActivity
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDonorEmergencyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDonorEmergencyBinding
    private lateinit var recyclerView: RecyclerView
    private var selectedRadioValues: Array<Boolean?> = arrayOfNulls(0)


    private var user: String? = null
    private var token: String? = null
    private var selectedValue: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityDetailDonorEmergencyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val judul = intent.getStringExtra("judul")
        val tanggal = intent.getStringExtra("tanggal")
        val deskripsi = intent.getStringExtra("deskripsi")
        val status = "hadir"
        var  userId = 0


        binding.detailJudul.text = judul
        binding.detailTanggal.text = tanggal
        binding.detailDeskripsi.text = deskripsi

        binding.imageDetail.setImageResource(R.drawable.ic_emergency)

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, DonorEmergencyActivity::class.java)
            startActivity(intent)
        }


        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        token = sharedPreferences.getString("token", null)

        if (user != null) {
            val jsonObject = JsonParser.parseString(user).asJsonObject
            binding.namaUser.setText(jsonObject.get("name").asString)
            binding.jenisKelaminUser.setText(jsonObject.get("jenis_kelamin").asString)
            binding.noUser.setText(jsonObject.get("no_telp").asString)
            binding.golonganUser.setText(jsonObject.get("golongan_darah").asString)
            binding.alamatUser.setText(jsonObject.get("alamat").asString)
            binding.tanggalUser.setText(jsonObject.get("tanggal_lahir").asString)
            userId = jsonObject.get("id").asInt

        }



        recyclerView = findViewById(R.id.recyclerViewDaftar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val stringArray = resources.getStringArray(R.array.kuisioner)

        val adapter = PendaftaranAdapter(stringArray,recyclerView)
        adapter.setOnItemClickListener { value ->
            selectedValue = value
        }
        recyclerView.adapter = adapter

        selectedRadioValues = adapter.selectedRadioValues
        binding.btnregis.setOnClickListener {
            val posRegistration = RegistrationPost(selectedRadioValues.contentToString(),status, userId)
            postRegistration(posRegistration)
            allertDialogSucces()

        }


    }




    private fun postRegistration(postContentResponse: RegistrationPost) {
        val idKegiatan = intent.getIntExtra("id", 0)
        ApiConfig.instanceRetrofit.infoDarahPost(
            postContentResponse,
            "Bearer " + token,
            idKegiatan
        ).enqueue(object : Callback<RegistrationPost> {
            override fun onResponse(
                call: Call<RegistrationPost>,
                response: Response<RegistrationPost>
            ) {
                Log.d("Data", "sukser")
            }

            override fun onFailure(call: Call<RegistrationPost>, t: Throwable) {
                Log.d("Data", "Gagal" + t.message)
            }

        })

    }


    fun allertDialogSucces() {
        val dialogBinding = layoutInflater.inflate(R.layout.alert_dialog_custom_, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding)

        val alertDialog = builder.create()

        val judul = dialogBinding.findViewById<TextView>(R.id.tvHead)
        val gambar = dialogBinding.findViewById<ImageView>(R.id.ivdialog)
        val deskripsi = dialogBinding.findViewById<TextView>(R.id.tvdeskripsi)
        val buton = dialogBinding.findViewById<Button>(R.id.btnAlert)

        judul.setText("Pengajuan Kegiatan Donor Darah Berhasil")
        gambar.setImageResource(R.drawable.img_success)
        deskripsi.setText("Pengajuan kegiatan donor darah berhasil silahkan menunggu 1X24 Jam, petugas kami akan menghubungi secepatnya")

        buton.setOnClickListener {
            val intent = Intent(this@DetailDonorEmergencyActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}


class PendaftaranAdapter(
    private val items: Array<String>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<PendaftaranAdapter.ViewHolder>() {
    private var selectedItemPosition: Int = -1
    var selectedRadioValues: Array<Boolean?> = arrayOfNulls(items.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pendaftaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isSelected = selectedRadioValues[position] ?: false
        holder.bind(item, isSelected)

        holder.radioGroup.setOnCheckedChangeListener(null)

        if (isSelected) {
            holder.radioButtonTrue.isChecked = true
            holder.radioButtonFalse.isChecked = false
        } else {
            holder.radioButtonTrue.isChecked = false
            holder.radioButtonFalse.isChecked = true
        }

        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newValue = getSelectedValue(checkedId)
            selectedRadioValues[position] = newValue
        }

        holder.itemView.setOnClickListener {
            val newValue = !isSelected
            selectedRadioValues[position] = newValue
            holder.bind(item, newValue)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private var onItemClickListener: ((Boolean?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Boolean?) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.kuisionerDaftar)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        val radioButtonTrue: RadioButton = itemView.findViewById(R.id.checkBoxTrue)
        val radioButtonFalse: RadioButton = itemView.findViewById(R.id.checkBoxFalse)


        fun bind(item: String, isSelected: Boolean) {
            selectedRadioValues[adapterPosition] = isSelected
            textView.text = item
            val checkedId = radioGroup.checkedRadioButtonId
            radioButtonTrue.isChecked = isSelected && radioButtonTrue.id == checkedId
            radioButtonFalse.isChecked = isSelected && radioButtonFalse.id == checkedId

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val newValue = getSelectedValue(checkedId)
                onItemClickListener?.invoke(newValue)
                selectedRadioValues[adapterPosition] = newValue
            }
        }
    }

    fun getSelectedValuesArray(): Array<Boolean?> {
        return selectedRadioValues
    }
}

object Utils {
    fun getSelectedValue(checkedId: Int): Boolean? {
        return when (checkedId) {
            R.id.checkBoxTrue -> true
            R.id.checkBoxFalse -> false
            else -> null
        }
    }
}
