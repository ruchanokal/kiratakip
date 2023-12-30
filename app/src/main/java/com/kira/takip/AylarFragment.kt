package com.kira.takip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentAylarBinding


class AylarFragment : Fragment() {

    private var _binding : FragmentAylarBinding? = null
    private val binding get() = _binding!!
    val monthsList = arrayListOf<String>()
    private var monthAdapter : MonthAdapter? = null
    private lateinit var db : FirebaseFirestore
    private val TAG = "AylarFragment"
    private var siteAdi = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAylarBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            db = FirebaseFirestore.getInstance()
            siteAdi = AylarFragmentArgs.fromBundle(it).siteAdi

            monthAdapter = MonthAdapter(monthsList,siteAdi)
            binding.monthsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.monthsRecyclerView.adapter = monthAdapter

            val (currentMonth, currentYear) = DateUtils.getCurrentMonthAndYear()
            Log.d(TAG, "Month: $currentMonth, Year: $currentYear")
            val currentMonthAndYear = "$currentMonth $currentYear"
            aylariGetir(currentMonthAndYear)

        }


    }


    private fun aylariGetir(currentMonthAndYear : String) {

        val documentRef = db.collection("Aylar").document(siteAdi)

        documentRef.addSnapshotListener { value, error ->

            if (error != null) {

                _binding?.let {
                    Toast.makeText(requireContext(),"Hata algılandı: " + error.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            } else {

                if (value != null) {

                    if (value.exists()){

                        monthsList.clear()

                        val listFromDB = value.get("ayListesi") as ArrayList<String>?

                        listFromDB?.let {
                            monthsList.addAll(it)
                        }

                        if (!monthsList.contains(currentMonthAndYear)){

                            Log.i(TAG,"yeni ay eklendi: " + currentMonthAndYear)

                            documentRef.update("ayListesi",FieldValue.arrayUnion(currentMonthAndYear)).addOnSuccessListener {

                                _binding?.let {
                                    Toast.makeText(requireContext(),"${currentMonthAndYear} Eklendi",Toast.LENGTH_SHORT).show()
                                    monthAdapter?.notifyDataSetChanged()
                                }

                            }

                        }

                        monthAdapter?.notifyDataSetChanged()


                    } else {

                        Log.i(TAG,"liste boş")

                        val hashMap = hashMapOf<String,Any>()

                        val ayListesi = arrayListOf<String>()
                        ayListesi.add(currentMonthAndYear)
                        hashMap.put("ayListesi",ayListesi)

                        documentRef.set(hashMap).addOnSuccessListener {

                            _binding?.let {
                                Toast.makeText(requireContext(),"${currentMonthAndYear} Eklendi",Toast.LENGTH_SHORT).show()
                                monthAdapter?.notifyDataSetChanged()
                            }

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