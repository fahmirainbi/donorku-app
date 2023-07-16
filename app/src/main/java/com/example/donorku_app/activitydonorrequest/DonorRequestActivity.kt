package com.example.donorku_app.activitydonorrequest

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.donorku_app.R
import com.example.donorku_app.api.model.ActivityRequest
import com.example.donorku_app.databinding.ActivityDonorRequestBinding
import com.example.donorku_app.home.HomeActivity
import java.util.Calendar

class DonorRequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    AddContentPresenter.Listener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityDonorRequestBinding
    private lateinit var addContentPresenter: AddContentPresenter

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var second = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        pickDate()


        setupDataComponent()
            setupListener()


        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


    }

    //    set Date and Time
    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        second = cal.get(Calendar.SECOND)
    }

    private fun pickDate() {
        binding.pickDateBtn.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        binding.dateTv.text = "$savedDay/$savedMonth/$savedYear    $savedHour:$savedMinute:00"
    }

    private fun setupDataComponent() {
        addContentPresenter = AddContentPresenter(this,this@DonorRequestActivity)
    }

    override fun onAddContentSuccess(sccMessage: String) {
        Toast.makeText(this, sccMessage, Toast.LENGTH_SHORT).show()

    }

    override fun onAddContentFailure(errMessage: String) {
        Toast.makeText(this, errMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setupListener() {
        binding.btnRegistrationBlood.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val organisasi = binding.etOrganisasi.text.toString().trim()
            val tanggal = binding.dateTv.text.toString().trim()
            val nomor = binding.etTelepon.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim()

            when {
                (nama.isEmpty()) -> {
                    binding.etNama.error = "Kolom Kosong"
                    binding.etNama.requestFocus()
                }
                (organisasi.isEmpty()) -> {
                    binding.etOrganisasi.error = "Kolom Kosong"
                    binding.etOrganisasi.requestFocus()
                }
                (tanggal.isEmpty()) -> {
                    binding.dateTv.error = "Kolom Kosong"
                    binding.dateTv.requestFocus()
                }
                (nomor.isEmpty()) -> {
                    binding.etTelepon.error = "Kolom Kosong"
                    binding.etTelepon.requestFocus()
                }
                (alamat.isEmpty()) -> {
                    binding.etAlamat.error = "Kolom Kosong"
                    binding.etAlamat.requestFocus()
                }


            }

            val posContent = ActivityRequest(nama, organisasi, tanggal, nomor, alamat)
            addContentPresenter.createContent(posContent)
            allertDialogSucces()

        }
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
            val intent = Intent(this@DonorRequestActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}