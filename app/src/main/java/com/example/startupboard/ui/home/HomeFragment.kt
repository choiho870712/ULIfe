package com.example.startupboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startupboard.R

class HomeFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_home_fragment, CardFragment(childFragmentManager), "home_main")
        transaction.addToBackStack(null)
        transaction.commit()
        return root
    }
}