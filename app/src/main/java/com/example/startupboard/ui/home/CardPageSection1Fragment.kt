package com.example.startupboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R

class CardPageSection1Fragment(val manager: FragmentManager) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_card_home_section1, container, false)

        val dataList = ArrayList<CardPageSection1Model>()
        dataList.add(CardPageSection1Model("message1"))
        dataList.add(CardPageSection1Model("message2"))
        dataList.add(CardPageSection1Model("message3"))

        root.findViewById<RecyclerView>(R.id.recycler_card_page_home_section1).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = CardPageSection1Adapter(manager, dataList)
        }

        return root
    }
}