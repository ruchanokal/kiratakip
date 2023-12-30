package com.kira.takip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.MonthRowBinding

class MonthAdapter3 (val monthList : ArrayList<String>) : RecyclerView.Adapter<MonthAdapter3.MonthHolder>(){

    class MonthHolder(val binding : MonthRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val binding = MonthRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MonthHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        holder.binding.monthButton.text = monthList.get(position)
        holder.binding.monthButton.setOnClickListener {

            val action = AylarFragment2Directions.actionAylarFragment2ToToplamBilancoFragment(monthList.get(position))
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return monthList.size
    }

}