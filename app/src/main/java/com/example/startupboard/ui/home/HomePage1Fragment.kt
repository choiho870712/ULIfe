package com.example.startupboard.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home_page1.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomePage1Fragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home_page1, container, false)

        if (GlobalVariables.proposal != null) {
            root.text_author_home_page1.text = GlobalVariables.proposal!!.name
            val itemSelect = GlobalVariables.proposal!!.proposalItemList[0]

            root.text_title_home_page1.text = itemSelect.title
            root.text_date_home_page1.text = itemSelect.date
            root.text_tag_home_page1.text = itemSelect.getHashTagString()
            root.text_content_home_page1.text = itemSelect.content

            activity!!.runOnUiThread(Runnable {
                root.recycler_proposal_item.apply {
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(activity, 3)
                    adapter = CardProposalItemAdapter(GlobalVariables.proposal!!.proposalItemList, root)
                }
            })

            root.image_proposal.setOnClickListener {
                if (root.image_proposal.visibility == View.VISIBLE)
                    root.image_proposal.visibility = View.GONE
            }
        }

        root.button_toPage2_home_page1.setOnClickListener {
            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_homePage1Fragment_to_homePage2Fragment)
        }

        return root
    }
}
