package com.kira.takip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentAySecinizBinding
import java.util.*
import kotlin.collections.ArrayList


class AySecinizFragment : Fragment() {

    private var _binding : FragmentAySecinizBinding? = null
    private val binding get() = _binding!!
    val monthsList = arrayListOf<String>()
    private var monthAdapter : MonthAdapter2? = null
    private lateinit var db : FirebaseFirestore
    private val TAG = "AySecinizFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAySecinizBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            val from = AySecinizFragmentArgs.fromBundle(it).from

            db = FirebaseFirestore.getInstance()

            if (from.equals("duzenli")){
                monthAdapter = MonthAdapter2(monthsList,0)
            } else {
                monthAdapter = MonthAdapter2(monthsList,1)
            }

            binding.aylarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.aylarRecyclerView.adapter = monthAdapter

            val (currentMonth, currentYear) = DateUtils.getCurrentMonthAndYear()
            Log.d(TAG, "Month: $currentMonth, Year: $currentYear")
            val currentMonthAndYear = "$currentMonth $currentYear"
            aylariGetir(currentMonthAndYear)

        }


    }

    private fun aylariGetir(currentMonthAndYear : String) {

        val documentRef = db.collection("Giderler").document("Aylar")

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

                            documentRef.update("ayListesi", FieldValue.arrayUnion(currentMonthAndYear)).addOnSuccessListener {

                                _binding?.let {
                                    Toast.makeText(context,"${currentMonthAndYear} Eklendi",
                                        Toast.LENGTH_SHORT).show()
                                    monthAdapter?.notifyDataSetChanged()
                                }


                            }

                        }

                        monthAdapter?.notifyDataSetChanged()


                    } else {

                        Log.i(TAG,"liste boş")

                        val hashMap = hashMapOf<String,Any>()
                        hashMap.put("ayListesi",Arrays.asList(currentMonthAndYear))


                        documentRef.set(hashMap).addOnSuccessListener {

                            _binding?.let {
                                Toast.makeText(requireContext(),"${currentMonthAndYear} Eklendi", Toast.LENGTH_SHORT).show()
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