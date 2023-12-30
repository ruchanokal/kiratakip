package com.kira.takip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentDuzenliHarcamalar2Binding


class DuzenliHarcamalarFragment2 : Fragment() {

    private var _binding : FragmentDuzenliHarcamalar2Binding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private val TAG ="DuzenliHarcamalarFragment2"
    private val harcamaList = arrayListOf<Harcama>()
    private var duzenliHarcamalarAdapter : AylikHarcamalarAdapter? = null
    var toplamGider = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDuzenliHarcamalar2Binding.inflate(inflater,container,false)
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

                harcamaList.add(Harcama("Kira",""))
                harcamaList.add(Harcama("Kredi",""))
                harcamaList.add(Harcama("Taksit",""))
                harcamaList.add(Harcama("Vergi",""))
                harcamaList.add(Harcama("Aidat",""))

                duzenliHarcamalarAdapter = AylikHarcamalarAdapter(harcamaList)
                duzenliHarcamalarRW.layoutManager = LinearLayoutManager(requireContext())
                duzenliHarcamalarRW.adapter = duzenliHarcamalarAdapter

                harcamalariGetir(ay)

                ekleButton.setOnClickListener {
                    Log.i("AylikHarcamalarAdapter","harcamaList-1: " + harcamaList.size)
                    harcamaList.add(Harcama("",""))
                    Log.i("AylikHarcamalarAdapter","harcamaList-2: " + harcamaList.size)
                    duzenliHarcamalarAdapter?.updateData()
                }



                kaydetButton.setOnClickListener {

                    Log.i(TAG, "kaydetButton clicked: $ay")

                    if (duzenliHarcamalarAdapter?.areAllEditTextsFilled() == true){

                        Log.i(TAG, "edittextler dolu")

                        val myMap = hashMapOf<String,Any>()
                        val myList = arrayListOf<Harcama>()

                        duzenliHarcamalarAdapter?.hashMapAylikHarcama?.forEach {

                            val harcama = Harcama(it.key.text.toString(),it.value.text.toString())
                            myList.add(harcama)
                        }

                        Log.i(TAG,"hashMapAylikHarcama: " + duzenliHarcamalarAdapter?.hashMapAylikHarcama)

                        myMap.put("harcamaList",myList)

                        val documentRef = db.collection("DuzenliHarcamalar").document(ay)

                        createFBDocument(documentRef,myMap,ay)

                    } else {

                        _binding?.let {
                            Toast.makeText(requireContext(),"Lütfen tüm boşlukları doldurunuz!",
                                Toast.LENGTH_SHORT).show()
                        }

                    }
                }


            }

        }

    }

    private fun createFBDocument(documentRef: DocumentReference, myMap: HashMap<String, Any>, ay : String) {

        Log.i(TAG,"createFBDocument")

        documentRef.set(myMap)
            .addOnSuccessListener {
                Log.i(TAG,"gönderme başarılı, yeni döküman oluşturuluyor..")

                toplamGider = 0

                (myMap.get("harcamaList") as ArrayList<Harcama>).forEach {
                    toplamGider += it.miktar.toInt()
                }

                _binding?.let {
                    Toast.makeText(requireContext(),"Veriler gönderildi!",Toast.LENGTH_SHORT).show()
                    binding.toplamGiderText.text = "Toplam Gider: ${toplamGider} ₺"
                }

                val documentRef = db.collection("ToplamBilanço").document(ay)

                documentRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {

                            documentRef.update("duzenliGider",toplamGider.toString()).addOnSuccessListener {

                            }
                        } else {

                            val hashMap = hashMapOf<String,String>()
                            hashMap.put("duzenliGider",toplamGider.toString())

                            documentRef.set(hashMap).addOnSuccessListener {

                            }
                        }
                    }

            }.addOnFailureListener { it ->
                Log.e(TAG,"exception: " + it.localizedMessage)
            }
    }

    private fun harcamalariGetir(ay: String) {

        db.collection("DuzenliHarcamalar").document(ay).get().addOnSuccessListener { value->

            if (value != null) {
                if (value.exists()) {

                    toplamGider = 0

                    val harcamaListFromDB = value.get("harcamaList") as? ArrayList<HashMap<String, String>>

                    if (!harcamaListFromDB.isNullOrEmpty()) {

                        val convertedList = harcamaListFromDB.map { map ->
                            Harcama(map["key"].orEmpty(), map["miktar"].orEmpty())
                        }
                        harcamaList.clear()
                        harcamaList.addAll(convertedList)

                        convertedList.forEach {
                            toplamGider += it.miktar.toInt()
                        }

                        _binding?.let {
                            binding.toplamGiderText.text = "Toplam Gider: ${toplamGider} ₺"
                            duzenliHarcamalarAdapter?.updateData()
                        }



                    }

                }
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}