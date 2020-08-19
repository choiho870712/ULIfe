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
//        setSearchFriendButton()
        if (GlobalVariables.userInfo.isShop()) setSendNotificationButton()
        else {
            setRandomFoodButton()
            setFoodPriceButton()
            setStudentPremissionButton()
        }
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
        root.image_tool_box_empty_1_1.setImageResource(R.mipmap.shop_notice_foreground)
        root.text_tool_box_empty_1_1.text = "推撥"
        root.layout_tool_box_empty_1_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_notificationSendFragment)
        }
    }

    private fun setRandomFoodButton() {
        root.image_tool_box_empty_1_1.setImageResource(R.mipmap.random_food_plate_foreground)
        root.text_tool_box_empty_1_1.text = "轉盤"
        root.layout_tool_box_empty_1_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_randomPlateFragment)
        }
    }

    private fun setFoodPriceButton() {
        root.image_tool_box_empty_2_1.setImageResource(R.drawable.chicken_m)
        root.text_tool_box_empty_2_1.text = "折價券"
        root.layout_tool_box_empty_2_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_foodPriceFragment)
        }
    }

    private fun setStudentPremissionButton() {
        root.image_tool_box_empty_3_1.setImageResource(R.mipmap.icon_boy_foreground)
        root.text_tool_box_empty_3_1.text = "學生認證"
        root.layout_tool_box_empty_3_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_studentPermissionFragment)
        }
    }
}
