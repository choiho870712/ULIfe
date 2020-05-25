package com.example.startupboard.ui.notifications

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.startupboard.R
import com.example.startupboard.GlobalVariables.Companion.globalApi
import com.example.startupboard.GlobalVariables.Companion.globalUserInfo


class CardFragment(val manager: FragmentManager) : Fragment() {

    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_notifications_container, container, false)
        modifyHeader()
        createRecyclerView(root)
        return root
    }

    private fun createRecyclerView(root:View) {
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_notifications)

        Thread {
            val notificationList = globalApi.getNotification(globalUserInfo.ID, "F")
            activity!!.runOnUiThread(Runnable {
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(activity)
                    adapter = CardAdapter(activity!!, manager, notificationList)
                }
            })
        }.start()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if ( !hidden ) modifyHeader()
    }

    private fun modifyHeader() {
        val title : TextView = activity!!.findViewById(R.id.text_title_toolbar)
        val buttonRight : ImageButton = activity!!.findViewById(R.id.button_right_toolbar)
        val buttonLeft : ImageButton = activity!!.findViewById(R.id.button_left_toolbar)
        title.text = "通知"
        title.gravity = Gravity.CENTER
        buttonRight.setImageResource(R.color.colorPrimary)
        buttonRight.setOnClickListener {

        }
        buttonLeft.setImageResource(R.color.colorPrimary)
        buttonLeft.setOnClickListener {

        }
    }
}