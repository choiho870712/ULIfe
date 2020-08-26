package com.choiho.ulife.navigationUI.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_notification_friend.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationFriendFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_notification_friend, container, false)
        setUi()
        setButton()

        root.notification_left_friend.setOnClickListener {
            if (root.scroll_notification_friend.visibility == View.VISIBLE) {
                root.scroll_notification_friend.visibility = View.GONE
                root.recycler_notification_friend.visibility = View.VISIBLE
            }
        }

        return root
    }

    private fun setUi() {
//        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notification_friend)
//        GlobalVariables.notificationListFriendAdapter = CardFriendAdapter(GlobalVariables.notificationFriendList, root)
//        GlobalVariables.notificationListFriendLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
//        recyclerView.apply {
//            setHasFixedSize(true)
//            layoutManager = GlobalVariables.notificationListFriendLayoutManager
//            adapter = GlobalVariables.notificationListFriendAdapter
//        }
//        GlobalVariables.notificationListFriendLayoutManager.scrollToPosition(
//            GlobalVariables.notificationListFriendLayoutManager.itemCount-1
//        )
    }

    private fun setButton() {

    }


}
