package com.kira.takip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.kira.takip.databinding.FragmentToplamBilancoBinding


class ToplamBilancoFragment : Fragment() {

    private var _binding : FragmentToplamBilancoBinding? = null
    private val binding get() = _binding!!
    private var secilenAy = ""
    private lateinit var db : FirebaseFirestore
    var aylikGider = 0
    var alinankiraGelirleri = 0
    var toplamKiraGelirleri = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentToplamBilancoBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            secilenAy = ToplamBilancoFragmentArgs.fromBundle(it).secilenAy
            db = FirebaseFirestore.getInstance()

            binding.aylikText.text = secilenAy

            toplamBilancoDB()


        }

    }

    private fun toplamBilancoDB() {

        db.collection("ToplamBilanço").document(secilenAy).get()
            .addOnSuccessListener { document ->

            if (document != null && document.exists()){

                val gelir = document.getLong("gelir")
                val toplamBeklenenGelir = document.getLong("toplamBeklenenGelir")
                val duzensizGider = document.getString("duzensizGider")
                val duzenliGider = document.getString("duzenliGider")

                _binding?.let {

                    it.apply {

                        if (gelir != null && duzensizGider != null && duzenliGider != null && toplamBeklenenGelir != null) {

                            toplamGelir.text = "$toplamBeklenenGelir"
                            alinanGelir.text = "$gelir ₺"
                            alinmayanGelir.text = "${toplamBeklenenGelir-gelir}"
                            giderDuzensiz.text = "$duzensizGider ₺"
                            giderDuzenli.text = "$duzenliGider ₺"
                            netToplamBilanco.text = (gelir - duzensizGider.toLong() - duzenliGider.toLong()).toString() + " ₺"
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