package com.kira.takip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.DetayliDaireRowBinding

class DetayliDaireAdapter (val blokAdListMap: Map<String, MutableList<DetayliDaire>>
    ,val db: FirebaseFirestore, val ayAdi : String) : RecyclerView.Adapter<DetayliDaireAdapter.DetayliHolder>() {

    class DetayliHolder (val binding: DetayliDaireRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetayliHolder {
        val binding = DetayliDaireRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DetayliHolder(binding)
    }

    override fun onBindViewHolder(holder: DetayliHolder, position: Int) {

        val dairelerList = blokAdListMap.values.toList()[position]

        // Blok adını ve bağlı olduğu daireleri göstermek için holder'ı kullan
        holder.binding.blokNameText.text = blokAdListMap.keys.toList()[position]

        // Dairelerin RecyclerView'ını ayarla
        val tersCevrilmisDairelerList = dairelerList.reversed()
        val daireAdapter = DairelerAdapter(tersCevrilmisDairelerList as MutableList<DetayliDaire>,db,ayAdi)
        holder.binding.dairelerRecyclerView.adapter = daireAdapter
        //holder.binding.dairelerRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.binding.dairelerRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context,2)

    }

    override fun getItemCount(): Int {
        return blokAdListMap.size
    }

}
