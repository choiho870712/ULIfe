package com.choiho.ulife.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
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

            GlobalVariables.uiController.openGoToShopInfoBottom(true)

            root.text_author_home_page1.text = GlobalVariables.proposal!!.name
            val itemSelect = GlobalVariables.proposal!!.proposalItemList[0]

            root.text_title_home_page1.text = itemSelect.title
            root.text_date_home_page1.text = itemSelect.date
            root.text_tag_home_page1.text = itemSelect.getHashTagString()
            root.text_content_home_page1.text = itemSelect.content

            if (activity != null) {
                requireActivity().runOnUiThread(Runnable {
                    root.recycler_proposal_item.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(activity, 3)
                        adapter = CardProposalItemAdapter(
                            GlobalVariables.proposal!!.proposalItemList,
                            root
                        )
                    }
                })
            }

            root.image_proposal.setOnClickListener {
                if (root.image_proposal.visibility == View.VISIBLE)
                    root.image_proposal.visibility = View.GONE
            }
        }

        return root
    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.uiController.openGoToShopInfoBottom(false)
    }
}
