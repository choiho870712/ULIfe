package com.choiho.ulife.navigationUI.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_notifications, container, false)
        setUi()
        setButton()

        return root
    }

    private fun setUi() {
        if (GlobalVariables.userInfo.isShop()) {
//            root.card_notification_friend_main.visibility = View.GONE
            root.card_notification_shop_main.visibility = View.GONE
        }
    }

    private fun setButton() {
        GlobalVariables.toolBarController.openToolbarBackButton(false)

        if (!GlobalVariables.userInfo.isShop()) {
            root.card_notification_shop_main.setOnClickListener {
                GlobalVariables.functions.navigate(
                    R.id.navigation_notifications,
                    R.id.action_navigation_notifications_to_notificationsShopFragment
                )
            }

//            root.card_notification_friend_main.setOnClickListener {
//                GlobalVariables.activity.nav_host_fragment.findNavController().navigate(
//                    R.id.action_navigation_notifications_to_notificationFriendFragment)
//            }
        }

        root.card_notification_offical_main.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.navigation_notifications,
                R.id.action_navigation_notifications_to_notificationOfficialFragment
            )
        }
    }

}
