package com.kira.takip

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.MonthRowBinding

class MonthAdapter2 (val monthList : ArrayList<String>, val from : Int) : RecyclerView.Adapter<MonthAdapter2.MonthHolder>(){

    class MonthHolder(val binding : MonthRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val binding = MonthRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MonthHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        holder.binding.monthButton.text = monthList.get(position)
        holder.binding.monthButton.setOnClickListener {

            if (from == 0){
                val action = AySecinizFragmentDirections.actionAySecinizFragmentToDuzenliHarcamalarFragment2(monthList.get(position))
                Navigation.findNavController(it).navigate(action)
            } else {
                val action = AySecinizFragmentDirections.actionAySecinizFragmentToAylikHarcamalar2(monthList.get(position))
                Navigation.findNavController(it).navigate(action)
            }


        }

    }

    override fun getItemCount(): Int {
       return monthList.size
    }

}
