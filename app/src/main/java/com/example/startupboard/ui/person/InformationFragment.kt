package com.example.startupboard.ui.person

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
import com.example.startupboard.GlobalVariables.Companion.globalUserInfo

class InformationFragment(val manager: FragmentManager) : Fragment() {

    private lateinit var informationSection1Fragment : InformationSection1Fragment
    private lateinit var informationSection2Fragment : InformationSection2Fragment
    private lateinit var informationSection3Fragment : InformationSection3Fragment
    private lateinit var button1 : Button
    private lateinit var button2 : Button
    private lateinit var button3 : Button

    lateinit var root:View

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_person_container, container, false)

        modifyHeader(root)

        informationSection1Fragment = InformationSection1Fragment()
        informationSection2Fragment = InformationSection2Fragment()
        informationSection3Fragment = InformationSection3Fragment()
        button1 = root.findViewById(R.id.button1_person_main)
        button2 = root.findViewById(R.id.button2_person_main)
        button3 = root.findViewById(R.id.button3_person_main)

        initSectionFragment()
        setButtonOnClickListener()

        return root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if ( !hidden ) modifyHeader(root)
    }

    private fun modifyHeader(root: View) {
        val title : TextView = activity!!.findViewById(R.id.text_title_toolbar)
        val buttonRight : ImageButton = activity!!.findViewById(R.id.button_right_toolbar)
        val buttonLeft : ImageButton = activity!!.findViewById(R.id.button_left_toolbar)
        title.text = globalUserInfo.name
        title.gravity = Gravity.CENTER
        buttonRight.setImageResource(R.drawable.ic_mode_edit_white_24dp)
        buttonRight.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.hide(manager.findFragmentByTag("person_main")!!)
            transaction.add(R.id.container_person_fragment, EditFragment(manager, root), "person_edit")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        buttonLeft.setImageResource(R.color.colorPrimary)
        buttonLeft.setOnClickListener {

        }
    }

    private fun initSectionFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container_person_main, informationSection1Fragment)
        transaction.add(R.id.container_person_main, informationSection2Fragment)
        transaction.add(R.id.container_person_main, informationSection3Fragment)
        transaction.show(informationSection1Fragment)
        transaction.hide(informationSection2Fragment)
        transaction.hide(informationSection3Fragment)
        transaction.commit()

        button1.setBackgroundResource(R.color.colorWhiteSmoke)
        button2.setBackgroundResource(R.color.colorPrimaryDark)
        button3.setBackgroundResource(R.color.colorPrimaryDark)
        button1.setTextColor(resources.getColor(R.color.colorBlack))
        button2.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
        button3.setTextColor(resources.getColor(R.color.colorWhiteSmoke))

    }

    private fun setButtonOnClickListener() {
        button1.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction.show(informationSection1Fragment)
            transaction.hide(informationSection2Fragment)
            transaction.hide(informationSection3Fragment)
            transaction.commit()

            button1.setBackgroundResource(R.color.colorWhiteSmoke)
            button2.setBackgroundResource(R.color.colorPrimaryDark)
            button3.setBackgroundResource(R.color.colorPrimaryDark)
            button1.setTextColor(resources.getColor(R.color.colorBlack))
            button2.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
            button3.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
        }
        button2.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction.hide(informationSection1Fragment)
            transaction.show(informationSection2Fragment)
            transaction.hide(informationSection3Fragment)
            transaction.commit()

            button1.setBackgroundResource(R.color.colorPrimaryDark)
            button2.setBackgroundResource(R.color.colorWhiteSmoke)
            button3.setBackgroundResource(R.color.colorPrimaryDark)
            button1.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
            button2.setTextColor(resources.getColor(R.color.colorBlack))
            button3.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
        }
        button3.setOnClickListener {
            val transaction = childFragmentManager.beginTransaction()
            transaction.hide(informationSection1Fragment)
            transaction.hide(informationSection2Fragment)
            transaction.show(informationSection3Fragment)
            transaction.commit()

            button1.setBackgroundResource(R.color.colorPrimaryDark)
            button2.setBackgroundResource(R.color.colorPrimaryDark)
            button3.setBackgroundResource(R.color.colorWhiteSmoke)
            button1.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
            button2.setTextColor(resources.getColor(R.color.colorWhiteSmoke))
            button3.setTextColor(resources.getColor(R.color.colorBlack))
        }
    }
}