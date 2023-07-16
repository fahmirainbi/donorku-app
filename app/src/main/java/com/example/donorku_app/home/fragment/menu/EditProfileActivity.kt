package com.example.donorku_app.home.fragment.menu

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityEditProfileBinding
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var c = Calendar.getInstance()
    private var year = c.get(Calendar.YEAR)
    private var month = c.get(Calendar.MONTH)
    private var day = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var user: JsonObject
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        user    =  JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token   = sharedPreferences.getString("token", null)

        binding.pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay->
                binding.dateTv.setText(""+mDay+"/"+mMonth+"/"+mYear)
                c.set(mYear, mMonth, mDay)
                year = c.get(Calendar.YEAR)
                month = c.get(Calendar.MONTH)
                day = c.get(Calendar.DAY_OF_MONTH)
            },year,month,day)
            dpd.show()
        }

        if((if(!user.get("tipe_identitas").isJsonNull) user.get("tipe_identitas")?.asString else "") == "paspor"){
            binding.tipeIdentitas.setSelection(1)
        } else {
            binding.tipeIdentitas.setSelection(0)
        }

        binding.nomorIdentitas.setText(if(!user.get("nomor_identitas").isJsonNull) user.get("nomor_identitas")?.asString else "")
        binding.nama.setText(if(!user.get("name").isJsonNull) user.get("name")?.asString else "")

        if((if(!user.get("jenis_kelamin").isJsonNull) user.get("jenis_kelamin")?.asString else "") == "P"){
            binding.jenisKelamin.setSelection(1)
        } else {
            binding.jenisKelamin.setSelection(0)
        }

        binding.tempatLahir.setText(if(!user.get("tempat_lahir").isJsonNull) user.get("tempat_lahir")?.asString else "")
        binding.dateTv.setText(if(!user.get("tanggal_lahir").isJsonNull) user.get("tanggal_lahir")?.asString else "")
        binding.nomorTelepon.setText(if(!user.get("no_telp").isJsonNull) user.get("no_telp")?.asString else "")
        binding.alamat.setText(if(!user.get("alamat").isJsonNull) user.get("alamat")?.asString else "")
        val rt_rw = (if(!user.get("rt").isJsonNull) user.get("rt")?.asString else "") + "/" + (if(!user.get("rw").isJsonNull) user.get("rw")?.asString else "")
        binding.RTRW.setText(if(rt_rw=="/") "" else rt_rw)
        binding.kodePos.setText(if(!user.get("kode_pos").isJsonNull) user.get("kode_pos")?.asString else "")
        binding.kelurahan.setText(if(!user.get("kelurahan").isJsonNull) user.get("kelurahan")?.asString else "")
        binding.kecamatan.setText(if(!user.get("kecamatan").isJsonNull) user.get("kecamatan")?.asString else "")
        binding.kabupaten.setText(if(!user.get("kabupatenkota").isJsonNull) user.get("kabupatenkota")?.asString else "")
        binding.provinsi.setText(if(!user.get("provinsi").isJsonNull) user.get("provinsi")?.asString else "")
        binding.pekerjaan.setText(if(!user.get("pekerjaan").isJsonNull) user.get("pekerjaan")?.asString else "")
        binding.alamatKantor.setText(if(!user.get("alamat_kantor").isJsonNull) user.get("alamat_kantor")?.asString else "")
        binding.telpKantor.setText(if(!user.get("no_telp_kantor").isJsonNull) user.get("no_telp_kantor")?.asString else "")

        val golonganDarah = (if(!user.get("golongan_darah").isJsonNull) user.get("golongan_darah")?.asString else "")
        if(golonganDarah == "B"){
            binding.golonganDarah.setSelection(1)
        }else if(golonganDarah == "O"){
            binding.golonganDarah.setSelection(2)
        }else if(golonganDarah == "AB"){
            binding.golonganDarah.setSelection(3)
        } else {
            binding.golonganDarah.setSelection(0)
        }

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.saveBtn.setOnClickListener{
           updateUser()

        }
    }

    private fun updateUser(){
        ApiConfig.instanceRetrofit.updateUser(
            "Bearer " + token,
            user.get("id")?.asInt,
            binding.nomorIdentitas.text.toString(),
            binding.nama.text.toString(),
            "L",
            binding.tempatLahir.text.toString(),
            binding.dateTv.text.toString(),
            binding.nomorTelepon.text.toString(),
            binding.alamat.text.toString(),
            binding.RTRW.text.toString(),
            binding.RTRW.text.toString(),
            binding.kodePos.text.toString(),
            binding.kelurahan.text.toString(),
            binding.kecamatan.text.toString(),
            binding.kabupaten.text.toString(),
            binding.provinsi.text.toString(),
            binding.pekerjaan.text.toString(),
            binding.alamatKantor.text.toString(),
            binding.telpKantor.text.toString(),
            "A",
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if(json?.has("success") == true && json?.has("data") == true){
                    val data = json?.get("data")?.toString()
                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
                    val editorSharedPreferences = sharedPreferences.edit()
                    editorSharedPreferences.remove("user")
                    editorSharedPreferences.apply()
                    editorSharedPreferences.putString("user", data)
                    editorSharedPreferences.apply()
                    Toast.makeText(this@EditProfileActivity, "Profil Berhasil Diubah", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}