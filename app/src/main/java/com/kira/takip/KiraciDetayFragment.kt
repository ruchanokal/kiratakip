package com.kira.takip

import android.app.DatePickerDialog
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentKiraciDetayBinding
import java.text.SimpleDateFormat
import java.util.*


class KiraciDetayFragment : Fragment() {

    private var _binding : FragmentKiraciDetayBinding? = null
    private val binding get() = _binding!!
    private val TAG = "KiraciDetayFragment"
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var db : FirebaseFirestore

    private var editModeEnabled = false

    private var adiSoyadiChanged = false
    private var telefonChanged = false
    private var tcChanged = false
    private var kiraChanged = false
    private var depozitoChanged = false
    private var sozlesmeBaslangicTarihiChanged = false
    private var sozlesmeBitisTarihiChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKiraciDetayBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            val daireDetay = KiraciDetayFragmentArgs.fromBundle(it).daireDetay

            db = FirebaseFirestore.getInstance()

            binding.apply {

                telNoEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
                daireDetayBaslikText.text = daireDetay.daireNo
                adSoyadEditText.setText(daireDetay.kiraciAdi)
                tcEditText.setText(daireDetay.tcNo)
                kiraBedeliEditText.setText(daireDetay.kiraBedeli)
                depozitoEditText.setText(daireDetay.depozitoBedeli)
                telNoEditText.setText(daireDetay.telefonNo)
                sozlesmeBaslangicTarihiText.text = daireDetay.sozlesmeBaslangicTarihi
                sozlesmeBitisTarihiText.text = daireDetay.sozlesmeBitisTarihi

                editTextListeners(daireDetay)

                editImageView.setOnClickListener {

                    editModeEnabled = !editModeEnabled
                    it.isSelected = editModeEnabled

                    if(editModeEnabled){

                        adSoyadEditText.isEnabled = true
                        tcEditText.isEnabled = true
                        telNoEditText.isEnabled = true
                        kiraBedeliEditText.isEnabled = true
                        depozitoEditText.isEnabled = true

                        sozlesmeBaslangicTarihiText.setOnClickListener {
                            showDatePickerDialog(it as TextView)
                        }

                        sozlesmeBitisTarihiText.setOnClickListener {
                            showDatePickerDialog(it as TextView)
                        }

                    } else {

                        changeInformationsOnDB(daireDetay)

                    }

                }

            }



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

    private fun editTextListeners(detayliDaire: DetayliDaire) {

        with(binding){

            adSoyadEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    adiSoyadiChanged = !detayliDaire.kiraciAdi.equals(p0.toString())
                }
            })

            telNoEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    telefonChanged = !detayliDaire.telefonNo.equals(p0.toString())
                }
            })

            tcEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    tcChanged = !detayliDaire.tcNo.equals(p0.toString())
                }
            })

            kiraBedeliEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    kiraChanged = !detayliDaire.kiraBedeli.equals(p0.toString())
                }
            })


            depozitoEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    depozitoChanged = !detayliDaire.depozitoBedeli.equals(p0.toString())
                }
            })

            sozlesmeBaslangicTarihiText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    sozlesmeBaslangicTarihiChanged = !detayliDaire.sozlesmeBaslangicTarihi.equals(p0.toString())
                }
            })

            sozlesmeBitisTarihiText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    sozlesmeBitisTarihiChanged = !detayliDaire.sozlesmeBitisTarihi.equals(p0.toString())
                }
            })

        }

    }


    private fun changeInformationsOnDB(detayliDaire: DetayliDaire) {

        Log.i(TAG,"changeInformationsOnDB")

        if (adiSoyadiChanged || telefonChanged
            || tcChanged || kiraChanged || depozitoChanged
            || sozlesmeBaslangicTarihiChanged || sozlesmeBitisTarihiChanged) {

            Log.i(TAG,"değişen bilgi var")

            binding.apply {

                val adSoyad = adSoyadEditText.text.toString()
                val telNo = telNoEditText.text.toString()
                val tc = tcEditText.text.toString()
                val kiraBedeli = kiraBedeliEditText.text.toString()
                val depozitoBedeli = depozitoEditText.text.toString()
                val sozlesmeBaslangicTarihi = sozlesmeBaslangicTarihiText.text.toString()
                val sozlesmeBitisTarihi = sozlesmeBitisTarihiText.text.toString()

                if (adSoyad.isNotEmpty() && telNo.isNotEmpty() && tc.isNotEmpty()
                    && kiraBedeli.isNotEmpty() && depozitoBedeli.isNotEmpty()
                    && sozlesmeBaslangicTarihi.isNotEmpty() && sozlesmeBitisTarihi.isNotEmpty()) {


                    val updates = hashMapOf<String, Any>(

                        "adSoyad" to adSoyad,
                        "depozito" to depozitoBedeli,
                        "kira" to kiraBedeli,
                        "tc" to tc,
                        "telNo" to telNo,
                        "sozlesmeBaslangicTarihi" to sozlesmeBaslangicTarihi,
                        "sozlesmeBitisTarihiText" to sozlesmeBitisTarihi,
                    )


                    db.collection(detayliDaire.siteAdi).document(daireDetayBaslikText.text.toString()).update(updates).addOnCompleteListener {

                        if (it.isSuccessful){

                            _binding?.let {
                                Toast.makeText(requireContext(),"Kiracı bilgileri güncellendi!", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Log.e(TAG,"error message: " + it.exception?.localizedMessage)
                        }

                        eskiHalineGetir()

                    }

                } else {
                    Toast.makeText(requireContext(),"Herhangi bir alanı boş bırakmayınız!", Toast.LENGTH_SHORT).show()
                }

            }



        } else {
            Log.i(TAG,"değişen bilgi yok")
            eskiHalineGetir()
        }


    }

    private fun eskiHalineGetir() {

        binding.apply {

            adSoyadEditText.isEnabled = false
            tcEditText.isEnabled = false
            telNoEditText.isEnabled = false
            kiraBedeliEditText.isEnabled = false
            depozitoEditText.isEnabled = false

            sozlesmeBaslangicTarihiText.setOnClickListener(null)
            sozlesmeBitisTarihiText.setOnClickListener(null)

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}