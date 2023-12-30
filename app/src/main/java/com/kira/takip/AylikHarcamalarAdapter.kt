package com.kira.takip

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.kira.takip.databinding.AylikHarcamalarRowBinding

class AylikHarcamalarAdapter (val harcamaList : ArrayList<Harcama>) : RecyclerView.Adapter<AylikHarcamalarAdapter.AylikHolder>() {

    private val TAG = "AylikHarcamalarAdapter"

    val nameEditTextList = arrayListOf<EditText>()
    val ucretEditTextList = arrayListOf<EditText>()
    val hashMapAylikHarcama = hashMapOf<EditText,EditText>()
    var toplamGider : Int = 0
    private lateinit var recyclerView: RecyclerView

    inner class AylikHolder (val binding : AylikHarcamalarRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            nameEditTextList.add(binding.editTextNameText)
            ucretEditTextList.add(binding.editTextUcret)
            hashMapAylikHarcama.put(binding.editTextNameText,binding.editTextUcret)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AylikHolder {
        val binding = AylikHarcamalarRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        recyclerView = parent as RecyclerView
        return AylikHolder(binding)
    }

    override fun onBindViewHolder(holder: AylikHolder, position: Int) {

        holder.binding.apply {

            val harcama = harcamaList.get(position)
            editTextNameText.setText(harcama.key)
            editTextUcret.setText(harcama.miktar)

            deleteButton.setOnClickListener {

                Log.i(TAG,"harcamaList size-1: " + harcamaList.size)
                Log.i(TAG,"position: " + position)

                harcamaList.removeAt(position)
                Log.i(TAG,"harcamaList size-2: " + harcamaList.size)
                hashMapAylikHarcama.remove(editTextNameText)
                notifyItemRemoved(position)
                updateListsAndHashMap()
                notifyDataSetChanged()

            }

        }

    }

    fun updateListsAndHashMap() {

        nameEditTextList.clear()
        ucretEditTextList.clear()
        hashMapAylikHarcama.clear()

        recyclerView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recyclerView.viewTreeObserver.removeOnPreDrawListener(this)

                for (i in 0 until recyclerView.childCount) {
                    val holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as? AylikHolder
                    holder?.binding?.let { binding ->
                        nameEditTextList.add(binding.editTextNameText)
                        ucretEditTextList.add(binding.editTextUcret)
                        hashMapAylikHarcama[binding.editTextNameText] = binding.editTextUcret
                    }
                }

                // Güncelleme sonrasında logları kontrol etmek için
                Log.i(TAG, "Güncellenen nameEditTextList boyutu: ${nameEditTextList.size}")
                Log.i(TAG, "Güncellenen ucretEditTextList boyutu: ${ucretEditTextList.size}")
                Log.i(TAG, "Güncellenen hashMapAylikHarcama boyutu: ${hashMapAylikHarcama.size}")

                return true
            }
        })

    }

    fun updateData() {
        updateListsAndHashMap()
        notifyDataSetChanged()
    }



    fun areAllEditTextsFilled(): Boolean {

        for (editText in nameEditTextList) {
            if (editText.text.isEmpty()) {
                return false
            }
        }
        for (editText in ucretEditTextList) {
            Log.e(TAG,"editText is empty-2: " + editText.text.toString())
            if (editText.text.isEmpty()) {
                Log.e(TAG,"editText is empty-3: " + editText.text.toString())
                return false
            }
            Log.e(TAG,"editText is empty-4")
        }
        return true
    }


    override fun getItemCount(): Int {
        return harcamaList.size
    }
}