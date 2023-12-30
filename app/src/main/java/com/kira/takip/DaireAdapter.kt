package com.kira.takip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.DaireRowBinding

class DaireAdapter(val siteAdi : String, val daireList : ArrayList<Daire>
        ,val blokHarfi : String) : RecyclerView.Adapter<DaireAdapter.DaireHolder>() {

    class DaireHolder(val binding: DaireRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaireHolder {
        val binding = DaireRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DaireHolder(binding)
    }

    override fun onBindViewHolder(holder: DaireHolder, position: Int) {

        holder.binding.daireText.text = daireList.get(position).name

        if(!daireList.get(position).filled){
            holder.binding.daireText.setBackgroundResource(R.drawable.emailpasswordborder)
        } else {
            holder.binding.daireText.setBackgroundResource(R.drawable.emailpasswordborderselected)
        }

        holder.binding.daireText.setOnClickListener {

            val action = DaireFragmentDirections.actionDaireFragmentToDaireDetayFragment(daireList.get(position).name,position,siteAdi,blokHarfi)
            Navigation.findNavController(it).navigate(action)

        }

    }



    override fun getItemCount(): Int {
       return daireList.size
    }
}