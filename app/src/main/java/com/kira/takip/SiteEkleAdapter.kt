package com.kira.takip

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.SiteRowBinding


class SiteEkleAdapter (var size : Int) : RecyclerView.Adapter<SiteEkleAdapter.SiteHolder>() {

    private val TAG = "SiteEkleAdapter"
    val editTextList = arrayListOf<EditText>()
    var list = createHintArray(size)

    inner class SiteHolder(val binding : SiteRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            Log.i(TAG,"ViewHolder")
            editTextList.add(binding.editTextBlok)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteHolder {
        Log.i(TAG,"onCreateViewHolder")
        val binding = SiteRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SiteHolder(binding)
    }

    override fun onBindViewHolder(holder: SiteHolder, position: Int) {

        holder.binding.editTextBlok.hint = list.get(position)

    }

    private fun createHintArray(blokSayisi: Int): ArrayList<String> {
        val hintArray = arrayListOf<String>()

        for (i in 0 until blokSayisi) {
            val blokHarfi = ('A'.toInt() + i).toChar()
            hintArray.add("$blokHarfi Blok daire sayısını giriniz")
        }

        return hintArray
    }

    fun updateBlockCount(newBlockCount: Int) {
        size = newBlockCount
        list = createHintArray(newBlockCount)
        notifyDataSetChanged()
    }




    fun areAllEditTextsFilled(): Boolean {
        for (editText in editTextList) {
            if (editText.text.isEmpty()) {
                return false
            }
        }
        return true
    }


    override fun getItemCount(): Int {
        return size
    }
}