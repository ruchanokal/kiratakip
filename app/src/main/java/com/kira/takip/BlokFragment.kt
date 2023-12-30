package com.kira.takip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kira.takip.databinding.FragmentBlokBinding

class BlokFragment : Fragment() {

    private var _binding: FragmentBlokBinding? = null
    private val binding get() = _binding!!

    private val TAG = "BlokFragment"
    var blokAdapter : BlokAdapter? = null
    var blokList = arrayListOf<String>()
    var blokListFromFragment = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlokBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG,"onViewCreated")

        arguments?.let {

            blokListFromFragment = BlokFragmentArgs.fromBundle(it).blokList.toList() as ArrayList<String>
            val siteAdi = BlokFragmentArgs.fromBundle(it).siteAdi

            blokList.clear()
            blokList = createButtonArrayList(blokListFromFragment.size)
            blokAdapter = BlokAdapter(siteAdi,blokList,blokListFromFragment)

            with(binding){
                blokRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                blokRecyclerView.adapter = blokAdapter
                blokAdapter?.notifyDataSetChanged()
            }

        }

    }

    private fun createButtonArrayList(blokSayisi: Int): ArrayList<String> {
        val buttonList = arrayListOf<String>()

        for (i in 0 until blokSayisi) {
            val blokHarfi = ('A'.toInt() + i).toChar()
            buttonList.add("$blokHarfi Blok Daireleri")
        }

        return buttonList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}