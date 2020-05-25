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
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal

class CardPageFragment(val manager: FragmentManager, val proposal:Proposal) : Fragment() {

    private lateinit var cardPageSection1Fragment : CardPageSection1Fragment
    private lateinit var cardPageSection2Fragment : CardPageSection2Fragment
    private lateinit var button1 : Button
    private lateinit var button2 : Button

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_card_home, container, false)

        modifyHeader()

        cardPageSection1Fragment = CardPageSection1Fragment(manager)
        cardPageSection2Fragment = CardPageSection2Fragment(proposal)
        button1 = root.findViewById(R.id.button1_fragment_card_home)
        button2 = root.findViewById(R.id.button2_fragment_card_home)

        initSectionFragment()
        setButtonOnClickListener()

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
        title.text = "活動"
        title.gravity = Gravity.CENTER_VERTICAL
        buttonRight.setImageResource(R.color.colorPrimary)
        buttonRight.setOnClickListener {

        }
        buttonLeft.setImageResource(R.drawable.ic_navigate_before_white_24dp)
        buttonLeft.setOnClickListener {
            manager.popBackStackImmediate()
        }
    }

    private fun initSectionFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_card_home_fragment, cardPageSection1Fragment)
        transaction.add(R.id.container_card_home_fragment, cardPageSection2Fragment)
        transaction.show(cardPageSection2Fragment)
        transaction.hide(cardPageSection1Fragment)
        transaction.commit()

        button1.setBackgroundResource(R.color.colorPrimaryDark)
        button2.setBackgroundResource(R.color.colorWhiteSmoke)
        button1.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
        button2.setTextColor(resources.getColor(R.color.colorBlack))
    }

    private fun setButtonOnClickListener() {
        button1.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction.show(cardPageSection1Fragment)
            transaction.hide(cardPageSection2Fragment)
            transaction.commit()

            button1.setBackgroundResource(R.color.colorWhiteSmoke)
            button2.setBackgroundResource(R.color.colorPrimaryDark)
            button1.setTextColor(resources.getColor(R.color.colorBlack))
            button2.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
        }
        button2.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction.show(cardPageSection2Fragment)
            transaction.hide(cardPageSection1Fragment)
            transaction.commit()

            button1.setBackgroundResource(R.color.colorPrimaryDark)
            button2.setBackgroundResource(R.color.colorWhiteSmoke)
            button1.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
            button2.setTextColor(resources.getColor(R.color.colorBlack))
        }
    }
}