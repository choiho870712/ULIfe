package com.example.startupboard.ui.notifications


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.GlobalVariables

import com.example.startupboard.R
import com.example.startupboard.ui.api.Notification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        activity!!.toolbar.setNavigationIcon(null)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notifications)

        Thread {
            if (activity != null) {
                activity!!.runOnUiThread(Runnable {
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        adapter = CardAdapter(GlobalVariables.notificationList)
                    }
                })
            }
        }.start()

        GlobalVariables.uiController.setNavViewButton()

        return root
    }

}
