package com.kira.takip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kira.takip.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG,"onViewCreated")


        binding.apply {

            toplamBilanco.setOnClickListener {

                val action = MainFragmentDirections.actionMainFragmentToAylarFragment2()
                Navigation.findNavController(it).navigate(action)

            }

            fabSiteEkle.setOnClickListener {

                val action = MainFragmentDirections.actionMainFragmentToSiteEkleFragment()
                Navigation.findNavController(it).navigate(action)

            }

            kiraGeliri.setOnClickListener {

                val action = MainFragmentDirections.actionMainFragmentToKiraGelirleriFragment()
                Navigation.findNavController(it).navigate(action)

            }

            duzenliHarcamalar.setOnClickListener {

                val action = MainFragmentDirections.actionMainFragmentToAySecinizFragment("duzenli")
                Navigation.findNavController(it).navigate(action)

            }

            aylikHarcamalar.setOnClickListener {

                val action = MainFragmentDirections.actionMainFragmentToAySecinizFragment("aylik")
                Navigation.findNavController(it).navigate(action)

            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}