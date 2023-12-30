package com.kira.takip

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentDaireBinding
import com.kira.takip.databinding.FragmentSiteEkleBinding


class DaireFragment : Fragment() {

    private var _binding: FragmentDaireBinding? = null
    private val binding get() = _binding!!
    private val TAG = "DaireFragment"
    var daireAdapter : DaireAdapter? = null
    var daireSayisi : Int = 0
    var daireList = arrayListOf<Daire>()
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaireBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            db = FirebaseFirestore.getInstance()

            daireSayisi = DaireFragmentArgs.fromBundle(it).daire
            val siteAdi = DaireFragmentArgs.fromBundle(it).siteAdi
            val position = DaireFragmentArgs.fromBundle(it).position

            Log.i(TAG,"daireSayisi: " + daireSayisi)

            daireList.clear()
            daireList = createTextViewArrayList(daireSayisi,position)
            val blokHarfi = ('A'.toInt() + position).toChar()
            daireAdapter = DaireAdapter(siteAdi,daireList, blokHarfi.toString())

            with(binding){
                daireRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                daireRecyclerView.adapter = daireAdapter
                daireAdapter?.notifyDataSetChanged()
            }

        }

    }

    fun changeItemBackground(position: Int){
        daireList.get(position).filled = true
        daireAdapter?.notifyDataSetChanged()
    }


    private fun createTextViewArrayList(blokSayisi: Int,position: Int): ArrayList<Daire> {
        val buttonList = arrayListOf<Daire>()
        val blokHarfi = ('A'.toInt() + position).toChar()

        for (i in 0 until blokSayisi) {
            val daire = Daire("${blokHarfi}${i+1}",false)
            buttonList.add(daire)
        }

        return buttonList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}