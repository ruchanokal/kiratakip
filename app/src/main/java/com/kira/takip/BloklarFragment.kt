package com.kira.takip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentBloklarBinding


class BloklarFragment : Fragment() {

    private var _binding : FragmentBloklarBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private var siteAdi = ""
    private var ayAdi = ""

    private var detayliDaireList = arrayListOf<DetayliDaire>()
    val blokAdListMap = mutableMapOf<String, MutableList<DetayliDaire>>()
    var detayliDaireAdapter : DetayliDaireAdapter? = null

    private val TAG = "BloklarFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBloklarBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {

            db = FirebaseFirestore.getInstance()
            siteAdi = BloklarFragmentArgs.fromBundle(it).siteAdi
            ayAdi = BloklarFragmentArgs.fromBundle(it).ayAdi

            detayliDaireAdapter = DetayliDaireAdapter(blokAdListMap,db,ayAdi)

            binding.bloklarRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)
            binding.bloklarRecyclerView.adapter = detayliDaireAdapter
            bloklariGetir()


        }


    }



    private fun bloklariGetir() {

        db.collection(siteAdi).get().addOnSuccessListener { documents ->

            if (documents != null && !documents.isEmpty) {

                detayliDaireList.clear()
                blokAdListMap.clear()

                for (document in documents){

                    val kiraciAdi = document.getString("adSoyad")
                    val depozito = document.getString("depozito")
                    val kira = document.getString("kira")
                    val sozlesmeBaslangicTarihi = document.getString("sozlesmeBaslangicTarihi")
                    val sozlesmeBitisTarihi = document.getString("sozlesmeBitisTarihiText")
                    val tc = document.getString("tc")
                    val telNo = document.getString("telNo")
                    val blokAdi = document.getString("blokAdi")
                    val odenenAylarList = document.get("odenenAylar") as ArrayList<String>?

                    if (kiraciAdi != null && telNo != null && tc != null && kira != null
                        && depozito != null && sozlesmeBaslangicTarihi != null
                        && sozlesmeBitisTarihi != null && blokAdi != null) {

                        val detayliDaire = DetayliDaire(kiraciAdi,telNo,tc,kira,depozito,blokAdi,
                            sozlesmeBaslangicTarihi,sozlesmeBitisTarihi,document.id,siteAdi,odenenAylarList)

                        detayliDaireList.add(detayliDaire)
                        val blokAdList = blokAdListMap.getOrPut(blokAdi) { mutableListOf() }
                        blokAdList.add(detayliDaire)

                    }

                }

                detayliDaireAdapter?.notifyDataSetChanged()


            }


        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}