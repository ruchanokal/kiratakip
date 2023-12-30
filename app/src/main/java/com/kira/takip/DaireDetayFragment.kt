package com.kira.takip

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentDaireDetayBinding
import java.text.SimpleDateFormat
import java.util.*


class DaireDetayFragment : Fragment() {

    private var _binding: FragmentDaireDetayBinding? = null
    private val binding get() = _binding!!
    private val TAG = "DaireDetayFragment"
    private val henuzSecilmedi = "Henüz seçilmedi"
    private lateinit var db : FirebaseFirestore
    private var dataListener: OnDataPassedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaireDetayBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataListener = try {
            context as OnDataPassedListener
        } catch (e: ClassCastException) {
            Log.e(TAG,"hata: " + e.localizedMessage)
            throw ClassCastException(context.toString() + " must implement OnDataPassedListener")
        }
    }

    private fun sendData(data: Boolean,position : Int) {
        if (dataListener != null) {
            dataListener!!.onDataPassed(data, position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            with(binding){

                db = FirebaseFirestore.getInstance()

                telNoEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

                daireDetayBaslikText.text = DaireDetayFragmentArgs.fromBundle(it).secilenDaire
                val siteAdi = DaireDetayFragmentArgs.fromBundle(it).siteAdi
                val blokHarfi = DaireDetayFragmentArgs.fromBundle(it).blokHarfi
                val position = DaireDetayFragmentArgs.fromBundle(it).position

                kaydetButton.setOnClickListener {

                    if (isAllEditTextFilled()) {

                        val adSoyad = adSoyadEditText.text.toString()
                        val tc = tcEditText.text.toString()
                        val telNo = telNoEditText.text.toString()
                        val depozito = depozitoEditText.text.toString()
                        val kira = kiraBedeliEditText.text.toString()
                        val sozlesmeBaslangicTarihi = sozlesmeBaslangicTarihiText.text.toString()
                        val sozlesmeBitisTarihi = sozlesmeBitisTarihiText.text.toString()

                        val anaHashMap = hashMapOf<String,String>()
                        anaHashMap.put("siteAdi",siteAdi)

                        val detayHashMap = hashMapOf<String,String>()
                        detayHashMap.put("adSoyad",adSoyad)
                        detayHashMap.put("tc",tc)
                        detayHashMap.put("telNo",telNo)
                        detayHashMap.put("kira",kira)
                        detayHashMap.put("depozito",depozito)
                        detayHashMap.put("sozlesmeBaslangicTarihi",sozlesmeBaslangicTarihi)
                        detayHashMap.put("sozlesmeBitisTarihiText",sozlesmeBitisTarihi)
                        detayHashMap.put("blokAdi",blokHarfi)


                        db.collection("Siteler").document(siteAdi).set(anaHashMap).addOnCompleteListener {
                            if (it.isSuccessful){

                                db.collection(siteAdi).document(daireDetayBaslikText.text.toString()).set(detayHashMap).addOnSuccessListener {

                                    val (currentMonth, currentYear) = DateUtils.getCurrentMonthAndYear()
                                    val currentMonthAndYear = "$currentMonth $currentYear"

                                    val documentRef = db.collection("Aylar").document(siteAdi)

                                     documentRef.get().addOnSuccessListener { doc ->

                                         if (doc != null){

                                             if (doc.exists()){

                                                 documentRef.update("ayListesi", FieldValue.arrayUnion(currentMonthAndYear)).addOnSuccessListener {

                                                     _binding?.let {
                                                         Toast.makeText(requireContext(),"Veriler başarıyla gönderildi!",Toast.LENGTH_SHORT).show()

                                                     }
                                                 }


                                             } else {

                                                 val hashMap = hashMapOf<String,Any>()
                                                 hashMap.put("ayListesi",Arrays.asList(currentMonthAndYear))

                                                 documentRef.set(hashMap).addOnSuccessListener {

                                                     _binding?.let {
                                                         Toast.makeText(requireContext(),"Veriler başarıyla gönderildi!",Toast.LENGTH_SHORT).show()
                                                     }
                                                 }

                                             }
                                         }
                                     }

                                }.addOnFailureListener { it1 ->

                                    _binding?.let {
                                        Toast.makeText(requireContext(),"Veri gönderilemedi!: " + it1.localizedMessage,Toast.LENGTH_SHORT).show()
                                    }
                                }

                            } else {
                                _binding?.let {
                                    Toast.makeText(requireContext(),"Veri gönderilemedi!",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    } else {

                        _binding?.let {
                            Toast.makeText(requireContext(),"Lütfen gereken bilgileri doldurunuz!",Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                sozlesmeBaslangicTarihiText.setOnClickListener {
                    showDatePickerDialog(it as TextView)
                }

                sozlesmeBitisTarihiText.setOnClickListener {
                    showDatePickerDialog(it as TextView)
                }


            }



        }



    }

    private fun isAllEditTextFilled(): Boolean {

        with(binding){
            return adSoyadEditText.text.isNotBlank() &&
                    telNoEditText.text.isNotBlank() &&
                    tcEditText.text.isNotBlank() &&
                    kiraBedeliEditText.text.isNotBlank() &&
                    depozitoEditText.text.isNotBlank() &&
                    !sozlesmeBaslangicTarihiText.text.equals(henuzSecilmedi) &&
                    !sozlesmeBitisTarihiText.text.equals(henuzSecilmedi)
        }
    }

    private fun showDatePickerDialog(textView : TextView) {

        Log.i(TAG,"showDatePickerDialog-1")

        val currentDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                // Tarih seçildiğinde yapılacak işlemler
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                // Seçilen tarihi istediğiniz formatta kullanabilirsiniz
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)


                // Formatted date'i kullanabilirsiniz
                // Örneğin, bir TextView'e atayabilirsiniz
                // val textView = findViewById<TextView>(R.id.textView)
                // textView.text = formattedDate
                textView.text = formattedDate
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()

        Log.i(TAG,"showDatePickerDialog-2")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}