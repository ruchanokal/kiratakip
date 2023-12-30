package com.kira.takip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.KiraRowBinding

class KiraGelirleriAdapter(val siteList : ArrayList<String>) : RecyclerView.Adapter<KiraGelirleriAdapter.KiraHolder>() {

    class KiraHolder(val binding : KiraRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KiraHolder {
        val binding = KiraRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return KiraHolder(binding)
    }

    override fun onBindViewHolder(holder: KiraHolder, position: Int) {

        val selectedSite = siteList.get(position)

        holder.binding.kiraButton.text = siteList.get(position)
        holder.binding.kiraButton.setOnClickListener {

            val action = KiraGelirleriFragmentDirections.actionKiraGelirleriFragmentToAylarFragment(selectedSite)
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return siteList.size
    }


}