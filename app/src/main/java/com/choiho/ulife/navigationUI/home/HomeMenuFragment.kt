package com.choiho.ulife.navigationUI.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_menu.view.*

class HomeMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_home_menu, container, false)

        root.button_main_menu_choose_food.setOnClickListener {
            GlobalVariables.homeMenuChoose = "food"
            GlobalVariables.homePageView = null
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(
                    R.id.action_homeMenuFragment_to_navigation_home)
        }

        root.button_main_menu_choose_game.setOnClickListener {
            GlobalVariables.homeMenuChoose = "game"
            GlobalVariables.homePageView = null
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(
                    R.id.action_homeMenuFragment_to_navigation_home)
        }

        root.button_main_menu_choose_event.setOnClickListener {
            GlobalVariables.homeMenuChoose = "event"
            GlobalVariables.homePageView = null
            if (activity != null)
                requireActivity().nav_host_fragment.findNavController().navigate(
                    R.id.action_homeMenuFragment_to_navigation_home)
        }

        return root
    }


}
