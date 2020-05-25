package com.example.startupboard.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.startupboard.R
import com.example.startupboard.GlobalVariables.Companion.globalUserInfo

class InformationSection1Fragment() : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_information_person_section1, container, false)
        showPersonInformation(root)
        return root
    }

    private fun showPersonInformation(root : View) {
        val iconView = root.findViewById<ImageView>(R.id.image_photoshot_information_person_section1)
        val companyView = root.findViewById<TextView>(R.id.text_company_information_person_section1)
        val followerCountView = root.findViewById<TextView>(R.id.text_followerCount_information_person_section1)
        val friendCountView = root.findViewById<TextView>(R.id.text_friendCount_information_person_section1)
        val selfIntroductionView = root.findViewById<TextView>(R.id.text_selfIntroduction_information_person_section1)
        val showAllView = root.findViewById<TextView>(R.id.text_showAll_information_person_section1)
        val tagView = root.findViewById<TextView>(R.id.text_tag_information_person_section1)

        selfIntroductionView.text = globalUserInfo.content
        tagView.text = globalUserInfo.getHashTagString()
        companyView.text = globalUserInfo.getOrganizationString()
        iconView.setImageBitmap(globalUserInfo.icon)
    }
}