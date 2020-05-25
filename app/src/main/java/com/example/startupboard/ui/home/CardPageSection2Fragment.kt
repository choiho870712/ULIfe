package com.example.startupboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import com.example.startupboard.GlobalVariables.Companion.globalApi

class CardPageSection2Fragment(val prposal:Proposal) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_card_home_section2, container, false)
        Thread {
            globalApi.clickInProposal(prposal.id, "Proposal")
        }.start()
        showProposal(root)
        return root
    }

    private fun showProposal(root: View) {
        val authorView = root.findViewById<TextView>(R.id.text_author_home_page_section2)
        val titleView = root.findViewById<TextView>(R.id.text_title_home_page_section2)
        val dateView = root.findViewById<TextView>(R.id.text_date_home_page_section2)
        val tagView = root.findViewById<TextView>(R.id.text_tag_home_page_section2)
        val viewCountView = root.findViewById<TextView>(R.id.text_viewCount_home_page_section2)
        val contentView = root.findViewById<TextView>(R.id.text_content_home_page_section2)
        val headImageView = root.findViewById<ImageView>(R.id.image_authorHead_home_page_section2)
        Thread {
            globalApi.loadImage(activity!!, headImageView, prposal.poster_id, 0, "U")
        }.start()

        authorView.text = prposal.name
        titleView.text = prposal.title
        dateView.text = prposal.date
        tagView.text = prposal.getHashTagString()
        val viewCountStr = "觀看次數：" + prposal.view.toString()
        viewCountView.text = viewCountStr
        contentView.text = prposal.content

    }
}