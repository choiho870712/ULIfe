package com.example.startupboard.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startupboard.R

class PersonFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_person, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_person_fragment, InformationFragment(childFragmentManager), "person_main")
        transaction.addToBackStack(null)
        transaction.commit()
        return root
    }
}