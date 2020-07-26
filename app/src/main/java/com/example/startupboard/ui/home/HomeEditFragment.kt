package com.example.startupboard.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.Proposal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_edit.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home_edit, container, false)

//        val buttonSendNewProposal = activity!!.toolbar.menu.findItem(R.id.button_send_new_proposal)
//        buttonSendNewProposal.setEnabled(true)
//        buttonSendNewProposal.setVisible(true)
//        activity!!.toolbar.setOnMenuItemClickListener{
//            when (it.itemId) {
//                R.id.button_send_new_proposal -> {
//                    GlobalVariables.proposal = Proposal(
//                        GlobalVariables.globalUserInfo.name,
//                        -1,
//                        "",
//                        0,
//                        GlobalVariables.globalUserInfo.ID,
//                        mutableListOf(),
//                        mutableListOf(),
//                        root.text_edit_home_content.text.toString(),
//                        root.text_edit_home_title.text.toString(),
//                        mutableListOf()
//                    )
//                    Thread {
//                        GlobalVariables.globalApi.postProposal(GlobalVariables.proposal, "food", "CYCU")
//                        activity!!.nav_host_fragment.findNavController().navigate(R.id.action_homeEditFragment_to_homePage1Fragment)
//                    }.start()
//                    true
//                }
//                else -> super.onOptionsItemSelected(it)
//            }
//        }
        return root
    }

    override fun onStop() {
        super.onStop()
        val buttonSendNewProposal = activity!!.toolbar.menu.findItem(R.id.button_send_new_proposal)
        buttonSendNewProposal.setEnabled(false)
        buttonSendNewProposal.setVisible(false)
    }
}
