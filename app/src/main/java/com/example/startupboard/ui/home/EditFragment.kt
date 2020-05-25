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
import com.example.startupboard.GlobalVariables.Companion.globalUserInfo
import com.example.startupboard.GlobalVariables.Companion.globalApi
import java.time.LocalDateTime

class EditFragment(val manager: FragmentManager) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_edit_home, container, false)
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
        title.text = "新增文章"
        title.gravity = Gravity.CENTER
        buttonRight.setImageResource(R.color.colorPrimary)
        buttonRight.setOnClickListener {
        }
        buttonLeft.setImageResource(R.drawable.ic_close_white_24dp)
        buttonLeft.setOnClickListener {
            manager.popBackStackImmediate()
        }
    }

    private fun setPage(root:View) {
        val buttonSendProposal : Button = root.findViewById(R.id.button_proposal_submit)
        buttonSendProposal.setOnClickListener {
            createNewProposal(root)
        }
    }

    private fun createNewProposal(root:View) {
        val title : TextView = root.findViewById(R.id.text_edit_home_title)
        val content : TextView = root.findViewById(R.id.text_edit_home_content)
        globalApi.postProposal(Proposal(
            globalUserInfo.name,
            0,
            LocalDateTime.now().toString(),
            0,
            globalUserInfo.ID,
            0,
            content.text.toString(),
            title.text.toString(),
            mutableListOf()
        ))
    }
}