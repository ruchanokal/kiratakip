package com.kira.takip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.BlokRowBinding

class BlokAdapter(val siteAdi : String, val list : ArrayList<String>, val listFromFragment : ArrayList<String>) : RecyclerView.Adapter<BlokAdapter.BlokHolder>() {

    class BlokHolder (val binding : BlokRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlokHolder {
        val binding = BlokRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BlokHolder(binding)
    }

    override fun onBindViewHolder(holder: BlokHolder, position: Int) {

        with(holder.binding) {
            blokButton.text = list.get(position)
        }

        holder.binding.blokButton.setOnClickListener {
            val action = BlokFragmentDirections.actionBlokFragmentToDaireFragment(listFromFragment.get(position).toInt(),position,siteAdi)
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}