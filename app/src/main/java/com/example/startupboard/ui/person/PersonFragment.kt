package com.example.startupboard.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_person.view.*

/**
 * A simple [Fragment] subclass.
 */
class PersonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_person, container, false)

        activity!!.toolbar.setNavigationIcon(null)

        root.image_person_icon_boy.setOnClickListener {
            val myBundle = Bundle()
            myBundle.putParcelable("userInfo", GlobalVariables.userInfo)

            if (GlobalVariables.userInfo.latitude != 0.0) {
                activity!!.nav_host_fragment.findNavController().navigate(
                    R.id.action_navigation_person_to_personShopInfoFragment, myBundle)
            }
            else {
                activity!!.nav_host_fragment.findNavController().navigate(
                    R.id.action_navigation_person_to_personInfoFragment, myBundle)
            }
        }

        root.image_person_search_friend.setOnClickListener {
            activity!!.nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_personSearchFriendFragment)
        }

        if (GlobalVariables.userInfo.latitude == 0.0) {
            root.image_send_notification_person.visibility = View.INVISIBLE
        }
        else {
            root.image_send_notification_person.setOnClickListener {
                activity!!.nav_host_fragment.findNavController().navigate(R.id.action_navigation_person_to_notificationSendFragment)
            }
        }


        GlobalVariables.uiController.setNavViewButton()

        return root
    }

}
