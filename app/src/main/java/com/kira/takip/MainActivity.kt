package com.kira.takip


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController


class MainActivity : AppCompatActivity(), OnDataPassedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDataPassed(data: Boolean,position: Int) {
        if (data){
            findNavController(this,R.id.fragmentContainerView).popBackStack()
            val daireFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as DaireFragment?
            daireFragment?.let {
                it.changeItemBackground(position)
            }

        }
    }
}