package com.choiho.ulife.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_person, container, false)
        setButton()
        return root
    }

    private fun setButton() {
        GlobalVariables.uiController.openToolbarBackButton(false)
        setPersonPageButton()
        setSearchFriendButton()
        setSendNotificationButton()
        GlobalVariables.uiController.setNavViewButton()
    }

    private fun setPersonPageButton() {
        root.image_person_icon_boy.setOnClickListener {
            GlobalVariables.proposalUserInfo = GlobalVariables.userInfo
            GlobalVariables.proposalUserInfoDoneLoading = true

            if (GlobalVariables.userInfo.isShop()) {
                if (activity != null) {
                    requireActivity().nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_person_to_personShopInfoFragment
                    )
                }
            }
            else {
                if (activity != null) {
                    requireActivity().nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_person_to_personInfoFragment
                    )
                }
            }
        }
    }

    private fun setSearchFriendButton() {
//        root.image_person_search_friend.setOnClickListener {
//            if (activity != null)
//                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_personSearchFriendFragment)
//        }
    }

    private fun setSendNotificationButton() {
        if (GlobalVariables.userInfo.isShop()) {
            root.image_send_notification_person.setOnClickListener {
                if (activity != null)
                    requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_notificationSendFragment)
            }
        }
        else {
            root.image_send_notification_person.setImageResource(R.mipmap.tool_store_empty_box_foreground)
            root.text_send_notification_person.text = ""
        }
    }

}
