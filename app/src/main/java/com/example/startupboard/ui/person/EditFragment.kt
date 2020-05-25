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
import com.example.startupboard.GlobalVariables
import com.example.startupboard.R

class EditFragment(val manager: FragmentManager, val mainPageView: View) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_edit_person, container, false)
        modifyHeader()
        setPage(root)
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
        title.text = "編輯資料"
        title.gravity = Gravity.CENTER
        buttonRight.setImageResource(R.color.colorPrimary)
        buttonRight.setOnClickListener {

        }
        buttonLeft.setImageResource(R.drawable.ic_navigate_before_white_24dp)
        buttonLeft.setOnClickListener {
            manager.popBackStackImmediate()
        }
    }

    private fun setPage(root:View) {
        val buttonUpdateUserInfo : Button = root.findViewById(R.id.button_update_user_info_submit)
        val username : TextView = root.findViewById(R.id.text_username_edit_person)
        val organization : TextView = root.findViewById(R.id.text_company_edit_person)
        val content : TextView = root.findViewById(R.id.text_content_edit_person)
        val tag : TextView = root.findViewById(R.id.text_tag_edit_person)

        username.text = GlobalVariables.globalUserInfo.name
        organization.text = GlobalVariables.globalUserInfo.getOrganizationString()
        content.text = GlobalVariables.globalUserInfo.content
        tag.text = GlobalVariables.globalUserInfo.getHashTagString()
        buttonUpdateUserInfo.setOnClickListener {
            updateUserInfo(username, organization, content, tag)
        }
    }

    private fun updateUserInfo(username: TextView, organization: TextView, content:TextView, tag:TextView) {
        val isSuccess = GlobalVariables.globalApi.updateUserInfo(
            GlobalVariables.globalUserInfo.ID,
            GlobalVariables.globalUserInfo.iconString,
            content.text.toString(),
            username.text.toString(),
            mutableListOf(tag.text.toString()),
            mutableListOf(organization.text.toString())
        )

        if (isSuccess) {
            GlobalVariables.globalUserInfo.name = username.text.toString()
            GlobalVariables.globalUserInfo.organization = mutableListOf(organization.text.toString())
            GlobalVariables.globalUserInfo.content = content.text.toString()
            GlobalVariables.globalUserInfo.hashtag = mutableListOf(tag.text.toString())

            manager.popBackStackImmediate()

            mainPageView.invalidate()
            mainPageView.requestLayout()
        }
    }
}