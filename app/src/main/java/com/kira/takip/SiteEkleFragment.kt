package com.kira.takip

import android.R.attr.left
import android.R.attr.right
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.Distribution.BucketOptions.Linear
import com.kira.takip.databinding.FragmentSiteEkleBinding


class SiteEkleFragment : Fragment() {

    private val TAG = "SiteEkleFragment"

    private var _binding: FragmentSiteEkleBinding? = null
    private val binding get() = _binding!!
    private var blokSayisi = 0
    private var previousBlokNameArrayList = arrayListOf<String>()
    private var siteEkleAdapter : SiteEkleAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSiteEkleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG,"onViewCreated")

        with(binding){

            siteEkleAdapter = SiteEkleAdapter(blokSayisi)
            binding.bloklarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.bloklarRecyclerView.adapter = siteEkleAdapter

            editTextBlokSayisi.setOnEditorActionListener { textView, i, keyEvent ->

                if (i == EditorInfo.IME_ACTION_DONE) {
                    blokSayisi = editTextBlokSayisi.text.toString().toInt()
                    bloklariOlustur()
                    true
                }
                false
            }

            buttonDevamEt.setOnClickListener {

                if (siteEkleAdapter?.areAllEditTextsFilled() == true) {
                    previousBlokNameArrayList.clear()
                    siteEkleAdapter?.editTextList?.all { previousBlokNameArrayList.add(it.text.toString()) }
                    Log.i(TAG,"editTextList: " + siteEkleAdapter?.editTextList)
                    Log.i(TAG,"previousBlokNameArrayList: " + previousBlokNameArrayList)
                    if (previousBlokNameArrayList.isNotEmpty() && binding.editTextSite.text.isNotEmpty()) {
                        val action = SiteEkleFragmentDirections.actionSiteEkleFragmentToBlokFragment(previousBlokNameArrayList.toTypedArray(),binding.editTextSite.text.toString())
                        Navigation.findNavController(it).navigate(action)
                    } else {
                        Toast.makeText(requireContext(),"Lütfen tüm bilgileri doldurunuz", Toast.LENGTH_SHORT).show()
                    }

                } else
                    Toast.makeText(requireContext(),"Lütfen tüm bloklarla ilgili bilgileri doldurunuz", Toast.LENGTH_SHORT).show()

            }

        }


    }


    private fun bloklariOlustur() {

        Log.i(TAG,"blokSayısı: " + blokSayisi)
        if (blokSayisi > 0) {
            siteEkleAdapter?.updateBlockCount(blokSayisi)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}