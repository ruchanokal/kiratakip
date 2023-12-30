package com.kira.takip

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentDuzenliHarcamalarBinding
import java.util.*
import kotlin.collections.ArrayList


class DuzenliHarcamalarFragment : Fragment() {


    private var _binding : FragmentDuzenliHarcamalarBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private val TAG ="DuzenliHarcamalarFragment"

    var kiraFromEditText = ""
    var krediFromEditText = ""
    var vergiFromEditText = ""
    var taksitFromEditText = ""
    var aidatFromEditText = ""

    var kiraDegismis = false
    var krediDegismis = false
    var vergiDegismis = false
    var taksitDegismis = false
    var aidatDegismis = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDuzenliHarcamalarBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            db = FirebaseFirestore.getInstance()

            val ay = DuzenliHarcamalarFragmentArgs.fromBundle(it).ay
            binding.apply {

                ayBaslik.text = ay

                val (currentMonth, currentYear) = DateUtils.getCurrentMonthAndYear()
                Log.d(TAG, "Month: $currentMonth, Year: $currentYear")
                listeners()
                harcamalariGetir(ay)

                kaydetButton.setOnClickListener {

                    kiraFromEditText = kiraEditText.text.toString()
                    krediFromEditText = krediEditText.text.toString()
                    taksitFromEditText = taksitEditText.text.toString()
                    aidatFromEditText = aidatEditText.text.toString()
                    vergiFromEditText = vergiEditText.text.toString()

                    if (!butunEditTextlerDolu() ) {
                        Toast.makeText(requireContext(),"Lütfen tüm boşlukları doldurunuz!", Toast.LENGTH_SHORT).show()
                    } else if (!enAzindanBirTanesiDegismis()) {
                        Toast.makeText(requireContext(),"Hiç değişiklik yapmadınız!", Toast.LENGTH_SHORT).show()
                    } else {

                        val hashMap = hashMapOf<String,Any>()
                        hashMap.put("kira", kiraFromEditText)
                        hashMap.put("kredi", krediFromEditText)
                        hashMap.put("taksit", taksitFromEditText)
                        hashMap.put("aidat", aidatFromEditText)
                        hashMap.put("vergi", vergiFromEditText)

                        Log.i(TAG,"düzenli harcama göndermeden önce")


                        val documentRef = db.collection("DuzenliHarcamalar").document(ay)

                        documentRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    // Belge varsa, yeni alan ekleyin
                                    Log.i(TAG,"update")
                                    updateFBDocument(documentRef,hashMap,ay)
                                } else {
                                    Log.i(TAG,"create")
                                    // Belge yoksa, belge oluşturun ve ardından alan ekleyin
                                    createFBDocument(documentRef,hashMap,ay)
                                }
                            }


                    }

                }

            }

        }

    }

    private fun createFBDocument(documentRef: DocumentReference, hashMap: HashMap<String, Any>, ay : String) {
        documentRef.set(hashMap).addOnSuccessListener {

            Log.i(TAG,"düzenli harcama gönderimi başarılı")

            Toast.makeText(requireContext(),"Veriler gönderildi!", Toast.LENGTH_SHORT).show()
            if (butunEditTextlerDolu()){
                binding.toplamGiderText.text = toplamGider()
            }

            val documentRef = db.collection("ToplamBilanço").document(ay)

            documentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {

                        documentRef.update("duzenliGider",toplamGiderInt()).addOnSuccessListener {

                        }
                    } else {

                        val hashMap = hashMapOf<String,String>()
                        hashMap.put("duzenliGider",toplamGiderInt())

                        documentRef.set(hashMap).addOnSuccessListener {

                        }
                    }
                }

        }
    }

    private fun updateFBDocument(documentRef: DocumentReference, hashMap: HashMap<String, Any>,ay :String) {

        documentRef.update(hashMap).addOnSuccessListener {

            Log.i(TAG,"düzenli harcama güncellemesi başarılı")

            _binding?.let {
                Toast.makeText(requireContext(),"Veriler gönderildi!", Toast.LENGTH_SHORT).show()
                if (butunEditTextlerDolu()){
                    binding.toplamGiderText.text = toplamGider()
                }
            }


            val documentRef = db.collection("ToplamBilanço").document(ay)

            documentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {

                        documentRef.update("duzenliGider",toplamGiderInt()).addOnSuccessListener {

                        }
                    } else {

                        val hashMap = hashMapOf<String,String>()
                        hashMap.put("duzenliGider",toplamGiderInt())

                        documentRef.set(hashMap).addOnSuccessListener {

                        }
                    }
                }


        }

    }

    private fun listeners() {

        binding.apply {

            kiraEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {

                    Log.i(TAG,"kiraFromEditText: " + kiraFromEditText)
                    Log.i(TAG,"kiraYeni: " + p0.toString())


                    kiraDegismis = !p0.toString().equals(kiraFromEditText)
                }

            })

            krediEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    krediDegismis = !p0.toString().equals(krediFromEditText)
                }

            })

            vergiEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    vergiDegismis = !p0.toString().equals(vergiFromEditText)
                }

            })

            taksitEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    taksitDegismis = !p0.toString().equals(taksitFromEditText)
                }

            })

            aidatEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    aidatDegismis = !p0.toString().equals(aidatFromEditText)
                }

            })

        }
    }

    private fun enAzindanBirTanesiDegismis(): Boolean {
        return kiraDegismis || krediDegismis || vergiDegismis || taksitDegismis || aidatDegismis
    }

    private fun butunEditTextlerDolu(): Boolean {
        return kiraFromEditText.isNotEmpty() &&
                krediFromEditText.isNotEmpty() &&
                vergiFromEditText.isNotEmpty() &&
                taksitFromEditText.isNotEmpty() &&
                aidatFromEditText.isNotEmpty()
    }

    private fun harcamalariGetir(ay: String) {

        db.collection("DuzenliHarcamalar").document(ay).get().addOnSuccessListener { value ->

            if (value != null) {

                if (value.exists()){

                    value.getString("kira")?.let { kiraFromEditText = it }
                    value.getString("kredi")?.let { krediFromEditText = it }
                    value.getString("vergi")?.let { vergiFromEditText = it }
                    value.getString("taksit")?.let { taksitFromEditText = it }
                    value.getString("aidat")?.let { aidatFromEditText = it }


                    _binding?.let {

                        if (kiraFromEditText.isNotEmpty())
                            binding.kiraEditText.setText(kiraFromEditText)

                        if (krediFromEditText.isNotEmpty())
                            binding.krediEditText.setText(krediFromEditText)

                        if (vergiFromEditText.isNotEmpty())
                            binding.vergiEditText.setText(vergiFromEditText)

                        if (taksitFromEditText.isNotEmpty())
                            binding.taksitEditText.setText(taksitFromEditText)

                        if (aidatFromEditText.isNotEmpty())
                            binding.aidatEditText.setText(aidatFromEditText)

                        if (butunEditTextlerDolu()){
                            binding.toplamGiderText.text = toplamGider()
                        }


                    }




                }

            }



        }

    }

    private fun toplamGider(): String {
        return "Toplam Gider : " + (kiraFromEditText.toInt()
                + krediFromEditText.toInt()
                + taksitFromEditText.toInt()
                + aidatFromEditText.toInt()
                + vergiFromEditText.toInt()).toString() + " ₺"
    }

    private fun toplamGiderInt(): String {
        return (kiraFromEditText.toInt()
                + krediFromEditText.toInt()
                + taksitFromEditText.toInt()
                + aidatFromEditText.toInt()
                + vergiFromEditText.toInt()).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}