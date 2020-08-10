package com.choiho.ulife.ui.notifications


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

        root.notification_left_shop.setOnClickListener {
            if (root.scroll_notification_shop.visibility == View.VISIBLE) {
                root.scroll_notification_shop.visibility = View.GONE
                root.recycler_notification_shop.visibility = View.VISIBLE
            }
        }

        return root
    }

    private fun setUi() {
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notification_shop)
        GlobalVariables.notificationListShopAdapter = CardShopAdapter(GlobalVariables.notificationShopList, root)
        GlobalVariables.notificationListShopLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GlobalVariables.notificationListShopLayoutManager
            adapter = GlobalVariables.notificationListShopAdapter
        }
        GlobalVariables.notificationListShopLayoutManager.scrollToPosition(
            GlobalVariables.notificationListShopLayoutManager.itemCount-1
        )
    }

    private fun setButton() {

    }
}
