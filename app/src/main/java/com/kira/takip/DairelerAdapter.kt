package com.kira.takip

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.DaireRowBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DairelerAdapter(val dairelerList : MutableList<DetayliDaire>
        , val db: FirebaseFirestore, val ayAdi : String) : RecyclerView.Adapter<DairelerAdapter.DaireHolder>() {
    class DaireHolder (val binding : DaireRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val TAG = "DairelerAdapter"

    var toplamKiraGeliri = 0L


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaireHolder {
        val binding = DaireRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DaireHolder(binding)
    }

    override fun onBindViewHolder(holder: DaireHolder, position: Int) {

        val daire = dairelerList.get(position)
        var kiraBedeli = "0"
        holder.binding.daireText.text = daire.daireNo

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


             db.collection(daire.siteAdi).document(daire.daireNo).addSnapshotListener { value, error ->

                 if (value != null && value.exists()){

                     val odenenAylar = value.get("odenenAylar") as ArrayList<String>?
                     value.getString("kira").let {
                         kiraBedeli = it!!
                         toplamKiraGeliri += it.toLong()
                     }

                     if (!odenenAylar.isNullOrEmpty() && !kiraBedeli.isNullOrEmpty()){

                         if (odenenAylar.contains(ayAdi)){
                             holder.binding.daireText.setBackgroundResource(R.drawable.kira_odendi_bg)
                         } else {
                             kiraDurumu(daire.sozlesmeBaslangicTarihi,holder.binding.daireText,ayAdi)
                         }
                     } else {
                         kiraDurumu(daire.sozlesmeBaslangicTarihi,holder.binding.daireText,ayAdi)
                     }

                 } else {

                     kiraDurumu(daire.sozlesmeBaslangicTarihi,holder.binding.daireText,ayAdi)

                 }



             }

         }


        holder.binding.daireText.setOnClickListener {

            val action = BloklarFragmentDirections
                .actionBloklarFragmentToKiraciDetayFragment(dairelerList.get(position))
            Navigation.findNavController(it).navigate(action)

        }

        holder.binding.daireText.setOnLongClickListener {

            val popupMenu = PopupMenu(holder.itemView.context, it)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.odendi -> {

                        db.collection(daire.siteAdi).document(daire.daireNo).update("odenenAylar",FieldValue.arrayUnion(ayAdi)).addOnSuccessListener {

                            val documentRef = db.collection("ToplamBilanço").document(ayAdi)

                            documentRef.get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {

                                        db.collection("ToplamBilanço").document(ayAdi).update("gelir",FieldValue.increment(kiraBedeli.toLong())).addOnSuccessListener {

                                        }
                                    } else {

                                        val hashMap = hashMapOf<String,Any>()
                                        hashMap.put("gelir",kiraBedeli.toLong())
                                        hashMap.put("toplamBeklenenGelir",toplamKiraGeliri)

                                        db.collection("ToplamBilanço").document(ayAdi).set(hashMap).addOnSuccessListener {

                                        }
                                    }
                                }
                        }

                        true
                    }
                    R.id.odenmedi -> {
                        db.collection(daire.siteAdi).document(daire.daireNo).update("odenenAylar",FieldValue.arrayRemove(ayAdi)).addOnSuccessListener {

                            val documentRef = db.collection("ToplamBilanço").document(ayAdi)

                            documentRef.get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot.exists()) {

                                        db.collection("ToplamBilanço").document(ayAdi).update("gelir",FieldValue.increment(-kiraBedeli.toLong())).addOnSuccessListener {

                                        }
                                    } else {

                                        val hashMap = hashMapOf<String,Any>()
                                        hashMap.put("gelir",-kiraBedeli.toLong())
                                        hashMap.put("toplamBeklenenGelir",toplamKiraGeliri)

                                        db.collection("ToplamBilanço").document(ayAdi).set(hashMap).addOnSuccessListener {

                                        }
                                    }
                                }

                        }
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()

            return@setOnLongClickListener true
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun kiraDurumu(sozlesmeBaslangicTarihi: String, daireText: TextView, ayAdi : String) {

        val bugün = LocalDate.now()
        val kiraOdemeGunu = updateDateWithCurrentMonthAndYear(sozlesmeBaslangicTarihi,ayAdi)

        Log.i(TAG,"bugün: ${bugün}")
        Log.i(TAG,"kiraOdemeGunu: ${kiraOdemeGunu}")
        Log.i(TAG,"ay: ${ayAdi}")

        val compareDate = LocalDate.parse(kiraOdemeGunu, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val comparisonResult = bugün.compareTo(compareDate)

        if (comparisonResult <= 0) {
            // Şu anki tarih daha önceyse (04.12.2023'ten önce)
            daireText.setBackgroundResource(R.drawable.kira_henuz_odenmedi_bg)
        } else {
            // Şu anki tarih daha sonra veya aynı ise
            daireText.setBackgroundResource(R.drawable.kira_odenmedi_bg)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDateWithCurrentMonthAndYear(originalDate: String,ayAdi: String): String {
        // Belirtilen tarihi LocalDate objesine dönüştür
        val originalLocalDate = LocalDate.parse(originalDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        val eskiFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val tarih = eskiFormat.parse(ayAdi)
        val localDate = tarih!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()



        // Belirtilen tarihin gün, ay ve yıl bilgilerini al
        val fixedDay = originalLocalDate.dayOfMonth
        val currentMonth = localDate.monthValue
        val currentYear = localDate.year

        // Yeni tarihi oluştur
        val updatedDate = LocalDate.of(currentYear, currentMonth, fixedDay)

        // Tarihi istediğiniz formatta string olarak döndür
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return updatedDate.format(formatter)
    }

    override fun getItemCount(): Int {
        return dairelerList.size
    }
}