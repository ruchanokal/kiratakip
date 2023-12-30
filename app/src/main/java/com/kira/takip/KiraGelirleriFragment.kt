package com.kira.takip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kira.takip.databinding.FragmentDaireBinding
import com.kira.takip.databinding.FragmentKiraGelirleriBinding

class KiraGelirleriFragment : Fragment() {

    private var _binding: FragmentKiraGelirleriBinding? = null
    private val binding get() = _binding!!
    private val TAG = "KiraGelirleriFragment"
    var kiraGelirleriAdapter : KiraGelirleriAdapter? = null
    val siteList = arrayListOf<String>()
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKiraGelirleriBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        kiraGelirleriAdapter = KiraGelirleriAdapter(siteList)
        binding.sitelerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sitelerRecyclerView.adapter = kiraGelirleriAdapter

        db.collection("Siteler").get().addOnSuccessListener { documents ->

            siteList.clear()

            for (document in documents) {
                Log.i(TAG,"document: " + document)
                siteList.add(document.id)
            }

            kiraGelirleriAdapter?.notifyDataSetChanged()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}