package com.kira.takip

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.MonthRowBinding

class MonthAdapter (val monthList : ArrayList<String>, val siteAdi : String) : RecyclerView.Adapter<MonthAdapter.MonthHolder>(){



    class MonthHolder(val binding : MonthRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val binding = MonthRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MonthHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        holder.binding.monthButton.text = monthList.get(position)
        holder.binding.monthButton.setOnClickListener {
            val action = AylarFragmentDirections.actionAylarFragmentToBloklarFragment(siteAdi,monthList.get(position))
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
       return monthList.size
    }

}
