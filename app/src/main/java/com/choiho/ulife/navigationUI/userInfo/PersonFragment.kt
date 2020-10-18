package com.choiho.ulife.navigationUI.userInfo


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
        GlobalVariables.toolBarController.openToolbarBackButton(false)
        setPersonPageButton()
        if (GlobalVariables.userInfo.isShop()) {
//            setRandomFoodButton()
            setFoodPriceButton()
            setSendNotificationButton()
            setComplaintButton()
        }
        else {
//            setRandomFoodButton()
            setFoodPriceButton()
            setStudentPremissionButton()
            setComplaintButton()
            setFormButton()
        }
    }

    private fun setPersonPageButton() {
        root.image_person_icon_boy.setOnClickListener {
            GlobalVariables.proposalUserInfo.copy(GlobalVariables.userInfo)

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

    private fun setSendNotificationButton() {
        root.image_tool_box_empty_1_2.setImageResource(R.drawable.ic_shopnotice)
        root.text_tool_box_empty_1_2.text = "推播"
        root.text_tool_box_empty_1_2.visibility = View.VISIBLE
        root.image_tool_box_empty_1_2.visibility = View.VISIBLE
        root.layout_tool_box_empty_1_2.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_notificationSendFragment)
        }
    }

    private fun setRandomFoodButton() {
        root.image_tool_box_empty_1_1.setImageResource(R.drawable.ic_random_food_plate)
        root.text_tool_box_empty_1_1.text = "美食轉盤"
        root.text_tool_box_empty_1_1.visibility = View.VISIBLE
        root.image_tool_box_empty_1_1.visibility = View.VISIBLE
        root.layout_tool_box_empty_1_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_randomPlateFragment)
        }
    }

    private fun setFoodPriceButton() {
        root.image_tool_box_empty_1_1.setImageResource(R.drawable.ic_random_food_price)
        root.text_tool_box_empty_1_1.text = "優惠券"
        root.text_tool_box_empty_1_1.visibility = View.VISIBLE
        root.image_tool_box_empty_1_1.visibility = View.VISIBLE
        root.layout_tool_box_empty_1_1.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_foodPriceFragment)
        }
    }

    private fun setStudentPremissionButton() {
        root.image_tool_box_empty_1_2.setImageResource(R.drawable.ic_student_permission_icon)
        root.text_tool_box_empty_1_2.text = "學生認證"
        root.text_tool_box_empty_1_2.visibility = View.VISIBLE
        root.image_tool_box_empty_1_2.visibility = View.VISIBLE
        root.layout_tool_box_empty_1_2.setOnClickListener {
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_studentPermissionFragment)
        }
    }

    private fun setFormButton() {
        root.image_tool_box_empty_1_3.setImageResource(R.drawable.ic_questionaaire)
        root.text_tool_box_empty_1_3.text = "問卷調查"
        root.text_tool_box_empty_1_3.visibility = View.VISIBLE
        root.image_tool_box_empty_1_3.visibility = View.VISIBLE
        root.layout_tool_box_empty_1_3.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.navigation_person,
                R.id.action_navigation_person_to_formListFragment
            )
        }
    }

    private fun setComplaintButton() {
        if (GlobalVariables.userInfo.isShop()) {
            root.image_tool_box_empty_1_3.setImageResource(R.drawable.ic_view_box)
            root.text_tool_box_empty_1_3.text = "意見箱"
            root.text_tool_box_empty_1_3.visibility = View.VISIBLE
            root.image_tool_box_empty_1_3.visibility = View.VISIBLE
            root.layout_tool_box_empty_1_3.setOnClickListener {
                if (activity != null)
                    requireActivity().nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_person_to_viewBoxFragment2)
            }
        }
        else {
            root.image_tool_box_empty_2_1.setImageResource(R.drawable.ic_view_box)
            root.text_tool_box_empty_2_1.text = "意見箱"
            root.text_tool_box_empty_2_1.visibility = View.VISIBLE
            root.image_tool_box_empty_2_1.visibility = View.VISIBLE
            root.layout_tool_box_empty_2_1.setOnClickListener {
                if (activity != null)
                    requireActivity().nav_host_fragment.findNavController().navigate(
                        R.id.action_navigation_person_to_viewBoxFragment2)
            }
        }
    }
}
