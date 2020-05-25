package com.example.startupboard.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R

class CardPageSection1CardPageFragment(val manager : FragmentManager) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_card_home_section1_page, container, false)
        modifyHeader()
        return root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if ( !hidden ) modifyHeader()
    }

    private fun modifyHeader() {
        val title : TextView = activity!!.findViewById(R.id.text_title_toolbar)
        val buttonRight : ImageButton = activity!!.findViewById(R.id.button_right_toolbar)
        val buttonLeft : ImageButton = activity!!.findViewById(R.id.button_left_toolbar)
        title.text = "最新資訊"
        title.gravity = Gravity.CENTER_VERTICAL
        buttonRight.setImageResource(R.color.colorPrimary)
        buttonRight.setOnClickListener {

        }
        buttonLeft.setImageResource(R.drawable.ic_navigate_before_white_24dp)
        buttonLeft.setOnClickListener {
            manager.popBackStackImmediate()
        }
    }
}