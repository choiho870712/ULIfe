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
import kotlinx.android.synthetic.main.fragment_notifications_shop.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationsShopFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_notifications_shop, container, false)
        setUi()
        setButton()

        root.scroll_notification_shop.setOnClickListener {
            if (root.scroll_notification_shop.visibility == View.VISIBLE) {
                root.scroll_notification_shop.visibility = View.GONE
                root.recycler_notification_shop.visibility = View.VISIBLE
            }
        }

        root.image_notifications_shop.setOnClickListener {
            GlobalVariables.functions.navigate(
                R.id.notificationsShopFragment,
                R.id.action_notificationsShopFragment_to_personShopInfoFragment)
        }

        return root
    }

    private fun setUi() {
        if (GlobalVariables.notificationList.isEmpty()) {
            if (GlobalVariables.officialNotificationList.isEmpty())
                root.text_no_shop_notify.text = "您目前還沒有訂閱任何店家喔～"
            else
                root.text_no_shop_notify.text = "您目前訂閱的店家沒有任何通知喔～"

            root.text_no_shop_notify.visibility = View.VISIBLE
        }
        else {
            root.text_no_shop_notify.visibility = View.GONE

            val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notification_shop)
            GlobalVariables.notificationListAdapter = CardShopAdapter(GlobalVariables.notificationList, root)
            GlobalVariables.notificationListLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GlobalVariables.notificationListLayoutManager
                adapter = GlobalVariables.notificationListAdapter
            }
            GlobalVariables.notificationListLayoutManager.scrollToPosition(
                GlobalVariables.notificationListLayoutManager.itemCount-1
            )
        }
    }

    private fun setButton() {

    }
}
