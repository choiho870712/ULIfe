package com.choiho.ulife.navigationUI.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_notifications_official.view.*
import kotlinx.android.synthetic.main.fragment_notifications_shop.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationOfficialFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_notifications_official, container, false)
        setUi()
        setButton()

        root.scroll_notification_official.setOnClickListener {
            if (root.scroll_notification_official.visibility == View.VISIBLE) {
                root.scroll_notification_official.visibility = View.GONE
                root.recycler_notification_official.visibility = View.VISIBLE
            }
        }

//        root.image_notifications_official.setOnClickListener {
//            GlobalVariables.functions.navigate(
//                R.id.notificationOfficialFragment,
//                R.id.action_notificationOfficialFragment_to_personShopInfoFragment)
//        }

        return root
    }

    private fun setUi() {
        if (GlobalVariables.officialNotificationList.isEmpty()) {
            root.text_no_official_notify.visibility = View.VISIBLE
        }
        else {
            root.text_no_official_notify.visibility = View.GONE

            val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notification_official)
            GlobalVariables.officialNotificationListAdapter = CardOfficialAdapter(GlobalVariables.officialNotificationList, root)
            GlobalVariables.notificationListLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GlobalVariables.notificationListLayoutManager
                adapter = GlobalVariables.officialNotificationListAdapter
            }
            GlobalVariables.notificationListLayoutManager.scrollToPosition(
                GlobalVariables.notificationListLayoutManager.itemCount-1
            )
        }

    }

    private fun setButton() {

    }
}
