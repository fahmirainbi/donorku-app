package com.example.donorku_app.activitydonorrequest

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donorku_app.databinding.ActivityDonorRequestBinding
import com.example.donorku_app.home.HomeActivity
import java.util.Calendar

class DonorRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonorRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.pickDateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view,mYear,mMonth,mDay->
                binding.dateTv.setText(""+mDay+"/"+mMonth+"/"+mYear)
            },year,month,day)
            dpd.show()
        }

        binding.ivBack.setOnClickListener{
                startActivity(Intent(this, HomeActivity::class.java))
        }

    }

}