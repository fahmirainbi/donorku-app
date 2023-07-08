package com.example.donorku_app.bloodrequest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.AddContentPresenter
import com.example.donorku_app.api.model.BloodRequestPost
import com.example.donorku_app.databinding.FragmentBloodRequestBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.home.fragment.HomeFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




class BloodRequestFragment : Fragment(),BloodContentPresenter.Listener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBloodRequestBinding
    private lateinit var bloodContentPresenter:BloodContentPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBloodRequestBinding.inflate(inflater,container,false)

        binding.ivBack.setOnClickListener{
            requireActivity().run {
                startActivity(Intent(this,HomeActivity::class.java))
            }
        }
        binding.btnRegistrationBlood.setOnClickListener{
            setupDataComponent()
            setupListener()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val blood = resources.getStringArray(R.array.blood)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_blood,blood)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter)
    }
    private fun setupDataComponent(){
        bloodContentPresenter = BloodContentPresenter(this@BloodRequestFragment)
    }

    override fun onAddContentSuccess(sccMessage: String) {

    }

    override fun onAddContentFailure(errMessage: String) {
    }

    private fun setupListener(){
        binding.btnRegistrationBlood.setOnClickListener{
            val kebutuhan = binding.etRequest.text.toString().trim()
            val golongan_darah = binding.autoCompleteTextView2.text.toString().trim()
            val nomor = binding.etNumber.text.toString().trim()
            val deskripsi = binding.etDescription.text.toString().trim()

            when {
                (kebutuhan.isEmpty()) -> {
                    binding.etRequest.error = "Kolom Kosong"
                    binding.etRequest.requestFocus()
                }
                (golongan_darah.isEmpty()) -> {
                    binding.autoCompleteTextView2.error = "Kolom Kosong"
                    binding.autoCompleteTextView2.requestFocus()
                }
                (nomor.isEmpty()) -> {
                    binding.etNumber.error = "Kolom Kosong"
                    binding.etNumber.requestFocus()
                }
                (deskripsi.isEmpty()) -> {
                    binding.etDescription.error = "Kolom Kosong"
                    binding.etDescription.requestFocus()
                }


            }

            requireActivity().run {
                val intent =Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }

            val posContent = BloodRequestPost(kebutuhan,golongan_darah,nomor,deskripsi)
            bloodContentPresenter.makeContent(posContent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BloodRequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BloodRequestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}